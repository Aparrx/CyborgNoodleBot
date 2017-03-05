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
import io.github.cyborgnoodle.chatcli.Commands;
import io.github.cyborgnoodle.features.calculator.AutoCalculator;
import io.github.cyborgnoodle.features.converter.AutoConverter;
import io.github.cyborgnoodle.features.markov.Markov;
import io.github.cyborgnoodle.features.statistics.Statistics;
import io.github.cyborgnoodle.settings.data.ServerChannel;
import io.github.cyborgnoodle.util.Log;
import io.github.cyborgnoodle.util.Random;

/**
 *
 */
public class MessageListener implements MessageCreateListener, MessageEditListener, MessageDeleteListener {

    CyborgNoodle noodle;

    public MessageListener(CyborgNoodle noods){
        this.noodle = noods;
    }

    public void onMessageCreate(DiscordAPI discordAPI, Message message) {

        try {
            if (!message.getAuthor().equals(noodle.api.getYourself())) {
                if (true) { //workaround original: !noodle.getSpamFilter().isSpam(message)

                    if(noodle.settings.xp.gain.get() && !noodle.isTestmode()) noodle.levels.onMessage(message.getAuthor());

                    boolean wascmd = Commands.execute(message, noodle.isTestmode());
                    if(!wascmd){
                        ServerChannel sch = ServerChannel.byID(message.getChannelReceiver().getId());
                        if(!noodle.settings.markov.exluded_channels.contains(sch))
                        Markov.getData().message(message.getAuthor().getId(),message.getContent());
                    }
                    if(true) Statistics.onMessage(message); //TODO
                    if(noodle.settings.chat.comment_badwords.get()) noodle.getBadWords().onMessage(message);

                    noodle.words.onMessage(message);

                    if (message.getMentions().contains(noodle.api.getYourself())) {
                        message.addUnicodeReaction(Random.choose(EmojiManager.getAll()).getUnicode());
                    }

                    if (!message.getContent().startsWith("!")) {
                        if(noodle.settings.autoconv.enable.get()) AutoConverter.onMsg(message,noodle);
                        if(false) AutoCalculator.onMsg(message); //TODO
                    }
                }
            }

        }
        catch (Exception e){
            Log.error("listener error");
            Log.stacktrace(e);
        }

    }

    public void onMessageDelete(DiscordAPI discordAPI, Message message) {
        //noodle.getChannel(ServerChannel.LOG).sendMessage(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)+" DEL - "+message.getAuthor().getName()+": "+message.getContent());
    }

    public void onMessageEdit(DiscordAPI discordAPI, Message message, String s) {

        int r = Random.randInt(0,noodle.settings.chat.comment_edits_chance.get());

        if(r==1){
            if(noodle.settings.chat.comment_edits.get()) message.getChannelReceiver().sendMessage("I saw that edit "+message.getAuthor().getMentionTag());
        }

    }
}
