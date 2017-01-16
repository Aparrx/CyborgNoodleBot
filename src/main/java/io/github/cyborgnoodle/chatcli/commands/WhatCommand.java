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

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Random;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.msg.ConversationMessages;

/**
 * Created by arthur on 16.01.17.
 */
public class WhatCommand extends Command {

    public WhatCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) {

        //UNSUPPORTED TODO

        String cmd = "";

        String rest = cmd.replace("what","");

        if(rest.contains("or")){
            String[] words = rest.split(" ");
            if(words.length>2){
                String[] sides = rest.split("or");
                String left = sides[0];
                String right = sides[1];

                if(Random.choose()){
                    getChannel().sendMessage(getAuthor().getMentionTag()+" "+left);
                }
                else {
                    getChannel().sendMessage(getAuthor().getMentionTag()+" "+right);
                }
            }
            else  getChannel().sendMessage(ConversationMessages.getNotUnderstood());
        }
        else  getChannel().sendMessage(ConversationMessages.getNotUnderstood());

    }

    @Override
    public String[] aliases() {
        return new String[]{"what","which"};
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public boolean emptyHelp() {
        return false;
    }

    @Override
    public String description() {
        return "let the cyborg decide";
    }
}
