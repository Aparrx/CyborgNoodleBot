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

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.settings.data.ServerRole;
import io.github.cyborgnoodle.util.JCUtil;
import io.github.cyborgnoodle.util.Random;
import io.github.cyborgnoodle.util.discord.Roles;

import java.util.Arrays;
import java.util.Collection;
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

        String ch = args[0];

        Collection<String> good = Arrays.asList("an amazing","an impressive","a wonderful","a beautiful","a godlike","a muscular","a delicious","a sexy");
        Collection<String> bad = Arrays.asList("a green","an alcoholic","a stinking","an ugly","a gay");

        Collection<String> making = Arrays.asList("transforming you into","making you","changing you into");

        boolean nothing = false;
        ServerRole sr;
        String rname;
        switch (ch.toLowerCase()){
            case "noodle":
                sr = ServerRole.CHAR_NOODLE;
                rname = Random.choose(good)+" Noodle";
                break;
            case "2d":
                sr = ServerRole.CHAR_2D;
                rname = Random.choose(good)+" 2D";
                break;
            case "russel":
                sr = ServerRole.CHAR_RUSSEL;
                rname = Random.choose(good)+" Russel";
                break;
            case "murdoc":
                sr = ServerRole.CHAR_MURDOC;
                rname = Random.choose(bad)+" Murdoc";
                break;
            case "nothing":
                sr = null;
                rname = "NOTHING"; //should never
                nothing = true;
                break;
            default:
                showInvalidArguments();
                return;
        }

        User user = getAuthor();

        if(args.length>=2){

            String mention = args[1];

            if(!new Permission(ServerRole.STAFF).has(getNoodle(),getAuthor())){
                getChannel().sendMessage("You are not allowed to change other peoples character!");
                showInvalidArguments();
                return;
            }

            User other = JCUtil.getUserByMention(getNoodle().api, mention);

            if(other==null){
                getChannel().sendMessage("User not found!");
                showInvalidArguments();
                return;
            }

            user = other;

        }

        Server server = getChannel().getServer();

        Collection<Role> current = user.getRoles(server);

        current.removeAll(Arrays.asList(Roles.resolveAll(getNoodle(),ServerRole.getCharacters())));

        if(nothing){
            getChannel().sendMessage(Random.choose(making)+" nothing...");
            try {
                server.updateRoles(user,current.toArray(new Role[current.size()])).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                getChannel().sendMessage("Oh no, something went wrong! D:");
                return;
            }
            return;
        }

        current.add(getNoodle().getRole(sr));

        getChannel().sendMessage(Random.choose(making)+" "+rname+"...");

        try {
            server.updateRoles(user,current.toArray(new Role[current.size()])).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            getChannel().sendMessage("Oh no, something went wrong! D:");
            return;
        }

        getChannel().sendMessage("Success!");

    }

    @Override
    public String[] aliases() {
        return new String[]{"makeme","make","char","character","c","mm"};
    }

    @Override
    public String usage() {
        return "!makeme <noodle | 2d | russel | murdoc | nothing> [@SomeoneElse]";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "change your character role";
    }

    @Override
    public Permission fullPermission() {
        return new Permission(25);
    }

    @Override
    public String category() {
        return "Discord";
    }
}
