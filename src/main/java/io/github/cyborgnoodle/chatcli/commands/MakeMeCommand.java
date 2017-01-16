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

package io.github.cyborgnoodle.chatcli.commands;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.server.ServerRole;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by arthur on 16.01.17.
 */
public class MakeMeCommand extends Command {

    public MakeMeCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception{

        if(args.length==1 || args.length==2){
            String rs = args[0];

            User user;

            if(args.length==2){

                if(!getNoodle().hasPermission(getAuthor(),new Permission(ServerRole.STAFF))){
                    getChannel().sendMessage("You are not allowed to set other peoples character! "+getAuthor().getMentionTag());
                    return;
                }

                String usrstr = args[1];

                User u = null;
                for(User usr : getNoodle().getAPI().getUsers()){
                    if(usr.getMentionTag().equalsIgnoreCase(usrstr)){
                        u = usr;
                        break;
                    }
                }

                if(u==null){
                    getChannel().sendMessage("I never heard of \""+usrstr+"\". "+getAuthor().getMentionTag());
                    return;
                }
                else user = u;
            }
            else{

                if(getNoodle().getLevels().getRegistry().getLevel(getAuthor().getId())<16){
                    getChannel().sendMessage("Sorry you need at least **Level 16** to change your own character! "+getAuthor().getMentionTag());
                    return;
                }


                user = getAuthor();
            }

            if(getNoodle().getRole(ServerRole.GORILLAZ).getUsers().contains(user)){
                getChannel().sendMessage("You have an **Official Character Role**. You cant change your character! "+user.getMentionTag());
                return;
            }



            Boolean murdoc = rs.equalsIgnoreCase("murdoc");
            Boolean russel = rs.equalsIgnoreCase("russel");
            Boolean noods = rs.equalsIgnoreCase("noodle");
            Boolean twod = rs.equalsIgnoreCase("2d");
            Boolean nothing = rs.equalsIgnoreCase("nothing");

            if(murdoc || russel || noods || twod || nothing){
                Role[] toremove = new Role[]{
                        getNoodle().getRole(ServerRole.MURDOC),
                        getNoodle().getRole(ServerRole.RUSSEL),
                        getNoodle().getRole(ServerRole.NOODLE),
                        getNoodle().getRole(ServerRole.TWOD)
                };
                List<Role> trmv = Arrays.asList(toremove);
                Collection<Role> rls = user.getRoles(getNoodle().getServer());
                rls.removeAll(trmv);
                try {
                    getNoodle().getServer().updateRoles(user, rls.toArray(new Role[rls.size()])).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    throw e;
                }

                ServerRole role = null;
                String repl = "";
                if(murdoc){
                    role = ServerRole.MURDOC;
                    repl = "You are a real Murdoc now "+user.getMentionTag()+"!";
                }
                if(russel){
                    role = ServerRole.RUSSEL;
                    repl = "Big Russel "+user.getMentionTag()+"!";
                }
                if(noods){
                    role = ServerRole.NOODLE;
                    repl = "Now you are a real Noodle ... *and I'll never never be like that* "+user.getMentionTag()+"!";
                }
                if(twod){
                    role = ServerRole.TWOD;
                    repl = "Now you are a 2D *and have this eye fracture* "+user.getMentionTag()+"!";
                }
                if(rs.equalsIgnoreCase("nothing")){
                    getChannel().sendMessage("Aaand you are nothing again "+user.getMentionTag()+".");
                    Log.info("Changed character role of "+user.getName()+" to nothing.");
                    return;
                }

                try {
                    getNoodle().getRole(role).addUser(user).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    throw e;
                }
                getChannel().sendMessage(repl);
                Log.info("Changed character role of "+user.getName()+" to "+role.name());

            }
            else getChannel().sendMessage("You have to tell me who you want to be: 2D, Murdoc, Russel, Noodle or Nothing! "+getAuthor().getMentionTag());

        }
        else getChannel().sendMessage("You have to tell me who you want to be: 2D, Murdoc, Russel, Noodle or Nothing! "+getAuthor().getMentionTag());


    }

    @Override
    public String[] aliases() {
        return new String[]{"makeme"};
    }

    @Override
    public String usage() {
        return "!makeme <character> [@SomeoneElse]";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }
}
