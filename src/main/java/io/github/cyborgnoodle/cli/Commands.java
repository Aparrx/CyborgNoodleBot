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

package io.github.cyborgnoodle.cli;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.features.levels.LevelConverser;
import io.github.cyborgnoodle.features.levels.RankCalculator;
import io.github.cyborgnoodle.features.news.Reddit;
import io.github.cyborgnoodle.features.news.RedditPost;
import io.github.cyborgnoodle.settings.data.ServerChannel;
import io.github.cyborgnoodle.settings.data.ServerRole;
import io.github.cyborgnoodle.util.Log;
import net.dean.jraw.ApiException;
import net.dean.jraw.managers.AccountManager;
import net.dean.jraw.models.FlairTemplate;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by arthur on 16.10.16.
 */
public class Commands {

    CyborgNoodle noodle;

    public Commands(CyborgNoodle noods){
        this.noodle = noods;
    }

    public void execute(String cmd) throws Exception{

        if(cmd.equals("stop")){
            noodle.stop();
            return;
        }

        if(cmd.equals("save")){
            noodle.savemanager.saveAll();
        }

        if(cmd.equals("load")){
            noodle.savemanager.loadAll();
        }

        if(cmd.startsWith("say")){

            String msg = cmd.replace("say ","");
            noodle.getChannel(ServerChannel.GENERAL).sendMessage(msg);

        }

        if(cmd.startsWith("make")){

            String[] args = cmd.split(" ");

            if(args.length==3){
                String userid = args[1];
                String roleid = args[2];

                User user;
                try {
                   user = noodle.api.getUserById(userid).get(10L, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    if(e instanceof TimeoutException) Log.error("Timeout 10 secs: USER");
                    else{
                        Log.error("Interrupted / Other: USER");
                        Log.stacktrace(e);
                    }
                    return;
                }
                if(user==null){
                    Log.warn("User not found!");
                    return;
                }

                Role role = noodle.getServer().getRoleById(roleid);
                if(role==null){
                    Log.warn("Role not found!");
                    return;
                }

                Log.info("Assigning ["+role.getName()+"] to @"+user.getName()+" ...");

                Collection<Role> roles = user.getRoles(noodle.getServer());
                roles.add(role);
                try {
                    noodle.getServer().updateRoles(user,roles.toArray(new Role[roles.size()])).get(10L,TimeUnit.SECONDS);
                    Log.info("Success!");
                    return;
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    if(e instanceof TimeoutException) Log.error("Timeout 10 secs: UPDATE_ROLES");
                    else{
                        Log.error("Interrupted / Other: UPDATE_ROLES");
                        Log.stacktrace(e);
                    }
                    return;
                }

            }
            else Log.error("make <userid> <roleid>");

            return;
        }

        if(cmd.startsWith("cmd")){

            String msg = cmd.replace("cmd ","");



            return;
        }

        if(cmd.startsWith("count")){

            String[] args = cmd.split(" ");

            try {
                Integer size = noodle.
                        getChannel(ServerChannel.GENERAL)
                        .getMessageHistory(
                                Integer.valueOf(args[1]))
                        .get(120l,TimeUnit.SECONDS)
                        .getMessages()
                        .size();
                Log.info(size+" msgs in #general");
            } catch (InterruptedException e) {
                Log.info("Interrupted during counting");
                Log.stacktrace(e);
            } catch (ExecutionException e) {
                Log.info("EXE EXCEPTION");
                Log.stacktrace(e);
            } catch (TimeoutException e) {
                Log.info("Connection timed out");
                Log.stacktrace(e);
            }
        }

        if(cmd.equals("bounty")){
            Log.info((noodle.levels.registry().getNextBounty()-System.currentTimeMillis())+" ms left till next bounty.\n");
        }

        if(cmd.equals("redditlogout")){
            Reddit reddit = noodle.reddit;

            if(reddit.isConnected()){
                reddit.logout();
            }
            else Log.warn("Not logged in to reddit, can not logout!c");
        }

        if(cmd.equals("redditlogin")){
            Reddit reddit = noodle.reddit;

            if(!reddit.isConnected()){
                reddit.login();
            }
            else Log.warn("Already logged in!");
        }

        if(cmd.startsWith("postdata")){

            String msg = cmd.replace("postdata ","");
            RedditPost rp = noodle
                    .reddit
                    .getData()
                    .getPost(msg);
            Log.info("Post "+msg+": "+rp.getUnrelatedVotes()+" UR votes");
        }

        if(cmd.equals("flairs")){
            Reddit reddit = noodle.reddit;

            AccountManager man = new AccountManager(reddit.getReddit());
            try {
                List<FlairTemplate> flairs = man.getFlairChoices(reddit.getReddit().getSubmission("5d7wcm"));
                for(FlairTemplate t : flairs){
                    Log.info(t.getId()+" - "+t.getText()+" | "+t.getCssClass()+" | "+t.getPosition());
                }
            } catch (ApiException e) {
                Log.stacktrace(e);
            }
        }

        if(cmd.startsWith("getrole")){

            String msg = cmd.replace("getrole ","");
            Integer i = Integer.valueOf(msg);
            ServerRole role = RankCalculator.getRoleforLevel(i);
            Server srv = noodle.api.getServerById("229000154936639488");
            Log.info("Role you get for Level "+i+": "+srv.getRoleById(role.getID()).getName());
        }

        if(cmd.startsWith("getxp")){

            String msg = cmd.replace("getxp ","");
            Integer i = Integer.valueOf(msg);
            long xp = LevelConverser.getXPforLevel(i);
            Log.info("XP you need to get Level "+i+": "+xp+" XP");
        }

        if(cmd.startsWith("getlvl")){

            String msg = cmd.replace("getlvl ","");
            Integer i = Integer.valueOf(msg);
            int lvl = LevelConverser.getLevelforXP(i);

            Log.info("Level you get for "+i+" XP: Level "+lvl);

        }

        if(cmd.startsWith("postreddit")){

            noodle.reddit.postNews("http://i.imgur.com/LII89SM.jpg","this is a test caption featuring @gorillaz and @watashiwanoodle and not more. yo.",System.currentTimeMillis(),"cyborgnoodlebot","http://what.ever/com","http://instagram.com/cyborgnoodlebot/");

        }

        if(cmd.startsWith("gainxp")){

            String[] msg = cmd.split(" ");

            String user = msg[1];

            User u = null;
            for(User usr : noodle.api.getUsers()){
                if(usr.getName().equalsIgnoreCase(user)){
                    u = usr;
                    break;
                }
            }

            if(u==null) Log.error("User not found: "+user);
            else{
                noodle.levels.gainXP(u);
            }
        }

        if(cmd.startsWith("rank")){

            String msg = cmd.replace("rank ","");
            User u = null;
            for(User usr : noodle.api.getUsers()){
                if(usr.getName().equalsIgnoreCase(msg)){
                    u = usr;
                    break;
                }
            }

            long xptotal = noodle.levels.registry().get(u).getXp();
            int level = noodle.levels.registry().get(u).getLevel();

            Log.info("Total XP: "+xptotal);
            Log.info("Level: "+level);

            long xpnext = LevelConverser.getXPforLevel(level+1);
            Log.info("XP needed for next level: "+xpnext);
            long xpcurrent = LevelConverser.getXPforLevel(level);
            Log.info("XP needed for current level: "+xpcurrent);
            long xpfill = xpnext - xptotal;
            Log.info("XP in this level until the next: "+xpfill);
            long xpfornext = xpnext - xpcurrent;
            Log.info("XP in this level needed for the next: "+xpfornext);

            Log.info(u.getMentionTag()+": "+xpfill+" / "+xpfornext+" XP ["+xptotal+" total] | Level "+level);

        }

        if(cmd.startsWith("setxp")){

            String[] msg = cmd.split(" ");

            String user = msg[1];
            String xp = msg[2];
            String lvl = msg[3];
            int xpi = Integer.valueOf(xp);
            int lvli = Integer.valueOf(lvl);

            User u = null;
            for(User usr : noodle.api.getUsers()){
                if(usr.getName().equalsIgnoreCase(user)){
                    u = usr;
                    break;
                }
            }

            if(u==null) Log.error("User not found: ");
            else{
                //noodle.getLevels().getRegistry().setXP(u.getId(),xpi);
                //noodle.getLevels().getRegistry().setLevel(u.getId(),lvli);
                Log.warn("Will not work. This was disabled!!!"); // added warning
                Log.info("Set xp of "+u.getId()+" to "+xpi);
            }
        }

    }

}
