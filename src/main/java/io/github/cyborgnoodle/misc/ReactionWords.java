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

package io.github.cyborgnoodle.misc;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.features.quotes.Quote;
import io.github.cyborgnoodle.features.quotes.Quotes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arthur on 30.01.17.
 */
public class ReactionWords {

    private Map<String,ReactionEntry> entries;

    private static ReactionWords words;

    private ReactionWords(){
        entries = new HashMap<>();
    }

    static {
        words = new ReactionWords();
    }

    public static void setWords(ReactionWords w){
        if(w!=null) words = w;
    }

    public static ReactionWords getWords(){
        return words;
    }

    public static void addReaction(String name, Message message){
        addReaction(name,message.getChannelReceiver().getId(),message.getId());
    }

    public static void addReaction(String name, String channel, String message){
        words.entries.put(name,new ReactionEntry(channel,message));
    }

    public static Quote get(CyborgNoodle noodle, String name){

        ReactionEntry entry = words.entries.get(name);
        if(entry==null) return null;
        Channel channel = noodle.api.getChannelById(entry.getChannelID());
        return Quotes.create(noodle).byMessageID(channel,entry.getMessageID());

    }

    //entry

    public static class ReactionEntry {

        private String channelid;
        private String messageid;

        public ReactionEntry(String channelid, String messageid) {
            this.channelid = channelid;
            this.messageid = messageid;
        }

        public String getChannelID() {
            return channelid;
        }

        public String getMessageID() {
            return messageid;
        }
    }
}
