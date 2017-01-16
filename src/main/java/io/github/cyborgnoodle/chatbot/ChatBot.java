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

package io.github.cyborgnoodle.chatbot;

import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.CyborgNoodle;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

/**
 * ChatBot
 */
public class ChatBot {

    CyborgNoodle noodle;
    Bot bot;
    Chat usrchat;

    public ChatBot(CyborgNoodle noodle){
        this.noodle = noodle;

        bot = new Bot("noodle",noodle.getSaveManager().getConfigDir().toString());
        usrchat = new Chat(bot);
    }

    public void onMessage(Message msg){

        if(msg.getMentions().contains(noodle.getAPI().getYourself())){

            String cont = msg.getContent().replace(noodle.getAPI().getYourself().getMentionTag(),"");
            react(msg,cont);
        }

    }

    public void react(Message m, String cont){


        String resp = usrchat.multisentenceRespond(cont);
        m.reply(resp+" "+m.getAuthor().getMentionTag());
    }

}
