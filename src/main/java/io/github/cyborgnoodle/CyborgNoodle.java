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
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.chatcli.commands.*;
import io.github.cyborgnoodle.chatcli.commands.funtance.FunAddCommand;
import io.github.cyborgnoodle.chatcli.commands.funtance.FunCommand;
import io.github.cyborgnoodle.chatcli.commands.funtance.FunRemoveCommand;
import io.github.cyborgnoodle.chatcli.commands.levels.AddXPCommand;
import io.github.cyborgnoodle.chatcli.commands.levels.RemXPCommand;
import io.github.cyborgnoodle.chatcli.commands.meme.ChoreCommand;
import io.github.cyborgnoodle.chatcli.commands.meme.FuxkitCommand;
import io.github.cyborgnoodle.chatcli.commands.meme.SueCommand;
import io.github.cyborgnoodle.chatcli.commands.meme.TeaCommand;
import io.github.cyborgnoodle.chatcli.commands.poll.PollCommand;
import io.github.cyborgnoodle.chatcli.commands.poll.ResultCommand;
import io.github.cyborgnoodle.chatcli.commands.poll.VoteCommand;
import io.github.cyborgnoodle.chatcli.commands.unit.ConvertCommand;
import io.github.cyborgnoodle.chatcli.commands.unit.UnitsCommand;
import io.github.cyborgnoodle.chatcli.words.*;
import io.github.cyborgnoodle.cli.CLICommands;
import io.github.cyborgnoodle.cli.CommandLine;
import io.github.cyborgnoodle.cli.CommandLineRunnable;
import io.github.cyborgnoodle.cli.Commands;
import io.github.cyborgnoodle.cli.commands.CLIHelpCommand;
import io.github.cyborgnoodle.cli.commands.MentionCommand;
import io.github.cyborgnoodle.cli.commands.SayCommand;
import io.github.cyborgnoodle.cli.commands.StopCommand;
import io.github.cyborgnoodle.features.converter.AutoConverter;
import io.github.cyborgnoodle.features.levels.Levels;
import io.github.cyborgnoodle.features.news.News;
import io.github.cyborgnoodle.features.news.Reddit;
import io.github.cyborgnoodle.features.timed.CyborgTickRunnable;
import io.github.cyborgnoodle.features.wordstats.WordStats;
import io.github.cyborgnoodle.listener.MessageListener;
import io.github.cyborgnoodle.listener.UserListener;
import io.github.cyborgnoodle.misc.BadWords;
import io.github.cyborgnoodle.misc.Polls;
import io.github.cyborgnoodle.misc.SpamFilter;
import io.github.cyborgnoodle.settings.CyborgSettings;
import io.github.cyborgnoodle.settings.data.ServerChannel;
import io.github.cyborgnoodle.settings.data.ServerRole;
import io.github.cyborgnoodle.settings.data.ServerUser;
import io.github.cyborgnoodle.tick.CyborgTick;
import io.github.cyborgnoodle.util.Log;
import yahoofinance.YahooFinance;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * This class represents the bot itself and holds all methods for interacting with the bot
 */
public class CyborgNoodle {

    public static final Log.LogContext context = new Log.LogContext("BOT");

    boolean testmode;

    boolean running;
    Connection connection;

    // PRIVATE / OLD

    Commands cmds;
    CommandLine cmdline;

    BadWords badwords;
    SpamFilter spam;
    ErrorNotifier notifier;



    // PUBLIC

    /**
     * HashMap containing runnables to run later (for the tick);
     */
    public HashMap<Runnable,Long> later;

    /**
     * Cyborg tick
     */
    public CyborgTick tick;

    /**
     * Reddit Utility for the bot
     */
    public Reddit reddit;

    /**
     * Polls
     */
    public Polls polls;

    /**
     * Word statistics
     */
    public WordStats words;

    /**
     * Levels
     */

    public Levels levels;

    /**
     * Save manager
     */
    public SaveManager savemanager;

    /**
     * The discord API
     */
    public DiscordAPI api;

    /**
     * The bot settings
     */
    public CyborgSettings settings;

    /**
     * The news
     */
    public News news;

    //constant

    final private String SERVER = "274439447234347010";

    public CyborgNoodle(Connection connection){
        this.running = true;
        this.connection = connection;
        this.api = connection.getAPI();
        this.cmds = new Commands(this);
        this.cmdline = new CommandLine(this);
        this.levels = new Levels(this);

        this.badwords = new BadWords(this);
        this.reddit = new Reddit(this);
        this.savemanager = new SaveManager(this);
        this.polls = new Polls();
        this.notifier = new ErrorNotifier(this);
        this.words = new WordStats();
        this.spam = new SpamFilter(this);
        this.later = new HashMap<>();

        this.tick = new CyborgTick(this);
        this.settings = new CyborgSettings(Settings.Setting.getDefaultInstance());
        this.news = new News(this);

        initBot();
    }

    public Boolean isTestmode() {
        return testmode;
    }

    public void setTestmode(Boolean testmode) {
        this.testmode = testmode;
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

    public BadWords getBadWords() {
        return badwords;
    }

    // STARTUP / SHUTDOWN

    private void initBot(){

        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        YahooFinance.logger.setLevel(java.util.logging.Level.OFF);

        AutoConverter.cacheAliases();

        Log.info(api.getChannels().toString());
        startThreads();
        registerListeners();
        api.setAutoReconnect(true);

        savemanager.loadAll();

        reddit.setUp();

        registerCommands();
        registerCLICommands();

        Locale.setDefault(Locale.ENGLISH);

    }

    private void startThreads(){

        tick.init();

        Thread tickt = new Thread(new CyborgTickRunnable(this));
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

    public synchronized void stop(){

        //say(SystemMessages.getStop());

        this.running = false;
        connection.setConnected(false);

        if(isTestmode()){
            Log.warn("NOT SAVING DUE TO TEST MODE!!!",context);
        }
        else savemanager.saveAll();

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Log.info("RUNNING THREADS:",context);
        for(Thread t : threadSet){
            Log.info("- "+t.getName()+" | "+t.getState().toString());
        }
        Log.info("Kill the process if it doesnt exit automatically within 10 seconds. (Ctrl+C)",context);

        api.disconnect();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.stacktrace(e);
        }

        System.exit(0);
    }

    public void stopAsync(){
        Executors.newSingleThreadExecutor().submit((Callable<Void>) () -> {
            stop();
            return null;
        });
    }

    // INIT

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

        io.github.cyborgnoodle.chatcli.Commands.register(new Stats2Command(this));

        io.github.cyborgnoodle.chatcli.Commands.register(new AddXPCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new RemXPCommand(this));

        io.github.cyborgnoodle.chatcli.Commands.register(new SettingsCommand(this));

        io.github.cyborgnoodle.chatcli.Commands.register(new ReactCommand(this));
        io.github.cyborgnoodle.chatcli.Commands.register(new AddReactCommand(this));

        io.github.cyborgnoodle.chatcli.Commands.register(new IncidentCommand(this));

        io.github.cyborgnoodle.chatcli.Commands.register(new MarkovCommand(this));

        io.github.cyborgnoodle.chatcli.Commands.register(new FileCommand(this));
    }

    public void registerCLICommands(){
        CLICommands.register(new StopCommand(this));
        CLICommands.register(new SayCommand(this));
        CLICommands.register(new MentionCommand(this));

        CLICommands.register(new CLIHelpCommand(this));
    }

    // API GETTERS

    public Server getServer(){
        return api.getServerById(SERVER);
    }

    public Channel getChannel(ServerChannel c){
        return api.getChannelById(c.getID());
    }

    public Role getRole(ServerRole r){
        return getServer().getRoleById(r.getID());
    }

    public boolean hasPermission(User user, Permission permission){
        return permission.has(this,user);
    }

    public User getUser(ServerUser user){
        try {
            return api.getUserById(user.getID()).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.stacktrace(e);
            return null;
        }
    }

    // TIMED TASKS

    public synchronized void doLater(Runnable r, long millis){
        later.put(r,System.currentTimeMillis()+millis);
    }
}
