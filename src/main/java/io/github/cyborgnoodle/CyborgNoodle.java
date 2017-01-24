/*
 * Copyright 2017 Enveed / Arthur Schüler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.cyborgnoodle;

import ch.qos.logback.classic.Level;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import io.github.cyborgnoodle.chatbot.ChatBot;
import io.github.cyborgnoodle.chatcli.ChatCommands;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.chatcli.commands.*;
import io.github.cyborgnoodle.chatcli.commands.funtance.FunAddCommand;
import io.github.cyborgnoodle.chatcli.commands.funtance.FunCommand;
import io.github.cyborgnoodle.chatcli.commands.funtance.FunRemoveCommand;
import io.github.cyborgnoodle.chatcli.commands.meme.*;
import io.github.cyborgnoodle.chatcli.commands.poll.PollCommand;
import io.github.cyborgnoodle.chatcli.commands.poll.ResultCommand;
import io.github.cyborgnoodle.chatcli.commands.poll.VoteCommand;
import io.github.cyborgnoodle.chatcli.commands.unit.ConvertCommand;
import io.github.cyborgnoodle.chatcli.commands.unit.UnitsCommand;
import io.github.cyborgnoodle.chatcli.words.*;
import io.github.cyborgnoodle.cli.CommandLine;
import io.github.cyborgnoodle.cli.CommandLineRunnable;
import io.github.cyborgnoodle.cli.Commands;
import io.github.cyborgnoodle.levels.LevelUnblockerRunnable;
import io.github.cyborgnoodle.levels.Levels;
import io.github.cyborgnoodle.listener.MessageListener;
import io.github.cyborgnoodle.listener.UserListener;
import io.github.cyborgnoodle.misc.*;
import io.github.cyborgnoodle.news.InstagramRegistry;
import io.github.cyborgnoodle.news.InstagramRunnable;
import io.github.cyborgnoodle.news.InstagramTool;
import io.github.cyborgnoodle.news.Reddit;
import io.github.cyborgnoodle.server.ServerChannel;
import io.github.cyborgnoodle.server.ServerRole;
import io.github.cyborgnoodle.server.ServerUser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * This class represents the bot itself and holds all methods for interacting with the bot
 */
public class CyborgNoodle {

    Boolean running;
    Connection connection;
    DiscordAPI api;

    SaveManager saveman;

    Commands cmds;
    CommandLine cmdline;
    Levels levels;
    ChatCommands chatcmds;
    ChatBot chatBot;
    InstagramTool instatool;
    InstagramRegistry instareg;

    BadWords badwords;

    Reddit reddit;

    Waiter gamenamewt;
    Waiter instawt;
    Waiter lvlunblockwt;
    Waiter redditmsgwt;
    Waiter savewt;
    Waiter wwt;

    Waiter relogwt;

    WordStats words;

    Polls polls;

    SpamFilter spam;


    ErrorNotifier notifier;

    HashMap<Runnable,Long> later;

    String SERVER = "229000154936639488";

    public CyborgNoodle(Connection connection){
        this.running = true;
        this.connection = connection;
        this.api = connection.getAPI();
        this.cmds = new Commands(this);
        this.cmdline = new CommandLine(this);
        this.levels = new Levels(this);
        this.chatcmds = new ChatCommands(this);

        this.instatool = new InstagramTool(this);
        this.instareg = new InstagramRegistry();

        this.badwords = new BadWords(this);

        this.reddit = new Reddit(this);

        this.saveman = new SaveManager(this);

        this.polls = new Polls();

        this.notifier = new ErrorNotifier(this);

        this.words = new WordStats();

        this.spam = new SpamFilter(this);

        this.later = new HashMap<>();

        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        Log.info(api.getChannels().toString());
        startThreads();
        registerListeners();
        api.setAutoReconnect(true);

        saveman.loadAll();

        reddit.setUp();

        registerCommands();

        //say(SystemMessages.getStart());
    }

    public Boolean isRunning(){
        return running;
    }

    public Commands getCommands(){
        return cmds;
    }

    public CommandLine getCommandLine(){
        return cmdline;
    }

    public Levels getLevels(){
        return levels;
    }

    public ChatCommands getChatCommands(){
        return chatcmds;
    }

    public ChatBot getChatBot(){
        return chatBot;
    }

    public SaveManager getSaveManager(){
        return saveman;
    }

    public DiscordAPI getAPI(){
        return api;
    }

    public InstagramTool getInstagram(){
        return instatool;
    }

    public InstagramRegistry getInstaRegistry(){
        return instareg;
    }

    public void setInstaRegistry(InstagramRegistry r){
        this.instareg = r;
    }

    public ErrorNotifier getNotifier(){
        return notifier;
    }

    public Reddit getReddit(){
        return reddit;
    }

    public BadWords getBadWords() {
        return badwords;
    }

    public SpamFilter getSpamFilter(){
        return spam;
    }

    public WordStats getWordStats(){
        return words;
    }

    public Polls getPolls() {
        return polls;
    }

    public void say(String message){
        Channel channel = api.getChannelById("229000154936639488");
        channel.sendMessage(message);
    }

    private void startThreads(){

        initTick();

        Thread tickt = new Thread(new CyborgTick(this));
        tickt.setName("CN Tick");
        tickt.start();

        Thread clithread = new Thread(new CommandLineRunnable(cmdline));
        clithread.setName("CN CLI");
        clithread.start();

    }

    private void registerListeners(){
        api.registerListener(new MessageListener(this));
        api.registerListener(new UserListener(this));
    }

    public void stop(){

        //say(SystemMessages.getStop());

        this.running = false;
        connection.setConnected(false);

        saveman.saveAll();

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Log.info("RUNNING THREADS:");
        for(Thread t : threadSet){
            Log.info("- "+t.getName()+" | "+t.getState().toString());
        }
        Log.info("Kill the process if it doesnt exit automatically within 10 seconds. (Ctrl+C)");

        api.disconnect();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    public Server getServer(){
        return api.getServerById(SERVER);
    }

    public Channel getChannel(ServerChannel c){
        return api.getChannelById(c.getID());
    }

    public Role getRole(ServerRole r){
        return getServer().getRoleById(r.getID());
    }


    //==============================//
    //2s runner

    public void initTick(){
        gamenamewt = new Waiter(60000,new GameNameRunnable(this));
        instawt = new Waiter(10000,new InstagramRunnable(this));
        lvlunblockwt = new Waiter(1000,new LevelUnblockerRunnable(this));
        redditmsgwt = new Waiter(10000, () -> {
            if(getReddit().isConnected()){
                getReddit().getManager().doCheck();
            }
        });
        relogwt = new Waiter(10800000, () -> {
            Log.info("Reconnecting ...");
            connection.getAPI().disconnect();
            connection.getAPI().connectBlocking();
            Log.info("Reconnected.");
        });

        savewt = new Waiter(1800000,new AutoSaveRunnable(this));
    }

    public void runTick() throws Exception{

        lvlunblockwt.run();
        gamenamewt.run();
        instawt.run();
        //redditmsgwt.run();

        savewt.run();

        //relogwt.run(); disable because JC has fixed the crash and it causes double logins
        HashSet<Runnable> toremove = new HashSet<>();

        for(Runnable r : later.keySet()){
            long millis = later.get(r);
            if(millis<System.currentTimeMillis()){
                r.run();
                toremove.add(r);
            }
        }

        for(Runnable r : toremove) later.remove(r);
    }

    public synchronized void doLater(Runnable r, long millis){
        later.put(r,System.currentTimeMillis()+millis);
    }

    public void registerCommands(){
        io.github.cyborgnoodle.chatcli.Commands.register(new LevelsCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new RankCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new WordCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new WordStatsCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new AvatarCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new WordsCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new TimeCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new FlipCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new WhatCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new RanksCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new FuxkitCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new StatsCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new WhenCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new ShortCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new VersionCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new MakeMeCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new TeaCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new PollCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new VoteCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new UnitsCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new ConvertCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new FunCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new UserWordsCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new HelpCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new ResultCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new ChoreCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new ExceptionsCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new SueCommand(this));

        io.github.cyborgnoodle.chatcli.Commands.register(new FunAddCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new FunRemoveCommand(this));

        io.github.cyborgnoodle.chatcli.Commands.register(new QuoteCommand(this));
    }

    public boolean hasPermission(User user, Permission permission){
        return permission.has(this,user);
    }

    public User getUser(ServerUser user){
        try {
            return api.getUserById(user.getID()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
