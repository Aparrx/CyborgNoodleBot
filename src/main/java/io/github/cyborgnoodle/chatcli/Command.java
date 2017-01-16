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
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.CyborgNoodle;

/**
 *
 */
public abstract class Command {

    private Message message;
    private Channel channel;
    private User author;

    private CyborgNoodle noodle;

    public void execute(Message message, String[] args){

        this.message = message;
        this.channel = message.getChannelReceiver();
        this.author = message.getAuthor();

        if(!noodle.hasPermission(author,fullPermission())){
            channel.sendMessage("Insufficient permissions!");
            return;
        }

        if(emptyHelp() && args.length==0){
            channel.sendMessage("Invalid arguments! Usage: `"+usage()+"`");
        }
        else onCommand(args);
    }

    public abstract void onCommand(String[] args);

    public abstract String[] aliases();

    public abstract String usage();

    public abstract boolean emptyHelp();

    public Permission fullPermission(){
        return new Permission();
    }

    public Command(CyborgNoodle noodle){
        this.noodle = noodle;
    }

    public Message getMessage() {
        return message;
    }

    public Channel getChannel() {
        return channel;
    }

    public User getAuthor() {
        return author;
    }

    public CyborgNoodle getNoodle() {
        return noodle;
    }
}
