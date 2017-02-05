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

package io.github.cyborgnoodle.chatcli;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.misc.ReactionWords;
import io.github.cyborgnoodle.server.ServerRole;
import io.github.cyborgnoodle.util.JCUtil;

import java.util.concurrent.ExecutionException;

/**
 * Created by arthur on 30.01.17.
 */
public class AddReactCommand extends Command {

    public AddReactCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        if(args.length>=2){
            String name = args[0];
            String msgid = args[1];

            Channel channel = getChannel();
            if(args.length>=3){
                String chs = args[2];
                Channel tc = JCUtil.getChannelByMention(getNoodle().getAPI(), chs);
                if(tc!=null) channel = tc;
            }

            Message msg;
            try {
                msg = JCUtil.getChannelMessageByID(getNoodle().getAPI(), channel, msgid).get();
            } catch (InterruptedException | ExecutionException e) {
                getChannel().sendMessage("Message not found in #"+channel.getName()+"!");
                getMessage().delete();
                return;
            }

            if(msg==null){
                getChannel().sendMessage("Message not found in #"+channel.getName()+"!");
                getMessage().delete();
                return;
            }

            ReactionWords.addReaction(name,msg);
            getChannel().sendMessage("Success!");

        }
        else showInvalidArguments();

        getMessage().delete();
    }

    @Override
    public String[] aliases() {
        return new String[]{"addreact","ar"};
    }

    @Override
    public String usage() {
        return "!ar <name> <msgid> [#channel]";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "add a react";
    }

    @Override
    public Permission fullPermission() {
        return new Permission(ServerRole.STAFF);
    }
}
