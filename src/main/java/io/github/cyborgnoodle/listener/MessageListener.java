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

package io.github.cyborgnoodle.listener;

import com.vdurmont.emoji.EmojiManager;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import de.btobastian.javacord.listener.message.MessageDeleteListener;
import de.btobastian.javacord.listener.message.MessageEditListener;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Random;
import io.github.cyborgnoodle.chatcli.Commands;
import io.github.cyborgnoodle.misc.AutoConverter;
import io.github.cyborgnoodle.misc.Util;
import io.github.cyborgnoodle.msg.ConversationMessages;
import io.github.cyborgnoodle.msg.SystemMessages;
import io.github.cyborgnoodle.server.ServerChannel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 */
public class MessageListener implements MessageCreateListener, MessageEditListener, MessageDeleteListener {

    CyborgNoodle noodle;

    public MessageListener(CyborgNoodle noods){
        this.noodle = noods;
    }

    public void onMessageCreate(DiscordAPI discordAPI, Message message) {

        if(!message.getAuthor().equals(noodle.getAPI().getYourself())) {
            if (true) { //workaround original: !noodle.getSpamFilter().isSpam(message)

                if(noodle.getSpamFilter().isTimeout(message.getAuthor())){
                    message.delete();
                    return;
                }

                noodle.getLevels().onMessage(message.getAuthor());
                Commands.execute(message);
                //noodle.getBadWords().onMessage(message);

                noodle.getWordStats().onMessage(message);

                if(message.getMentions().contains(noodle.getAPI().getYourself())){
                    message.addUnicodeReaction(Random.choose(EmojiManager.getAll()).getUnicode());
                }

                if(!message.getContent().startsWith("!")){
                    AutoConverter.onMsg(message);
                }
            }
        }

    }

    public void onMessageDelete(DiscordAPI discordAPI, Message message) {
        //noodle.getChannel(ServerChannel.LOG).sendMessage(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)+" DEL - "+message.getAuthor().getName()+": "+message.getContent());
    }

    public void onMessageEdit(DiscordAPI discordAPI, Message message, String s) {

        int r = Random.randInt(0,50);

        if(r==1){
            message.getChannelReceiver().sendMessage("I saw that edit "+message.getAuthor().getMentionTag());
        }

    }
}
