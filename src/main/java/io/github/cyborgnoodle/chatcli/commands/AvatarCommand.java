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
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.chatcli.Command;

/**
 * Created by arthur on 16.01.17.
 */
public class AvatarCommand extends Command {

    public AvatarCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) {

        String mention = args[0];

        User user = null;
        for(User u : getNoodle().getAPI().getUsers()){
            if(u.getMentionTag().equalsIgnoreCase(mention)) user = u;
        }

        if(user==null){
            Log.info("Can't find user!");
            return;
        }

        getChannel().sendMessage(user.getAvatarUrl().toString());
    }

    @Override
    public String[] aliases() {
        return new String[]{"avatar","ava","profile","profilepic","pic","picture"};
    }

    @Override
    public String usage() {
        return "!avatar @MentionTag";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "show the avatar of a user";
    }
}
