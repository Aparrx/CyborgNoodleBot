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

import com.google.common.base.Joiner;
import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.features.markov.Markov;
import io.github.cyborgnoodle.features.markov.MarkovChain;
import io.github.cyborgnoodle.util.JCUtil;
import io.github.cyborgnoodle.util.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

/**
 * Created by arthur on 16.02.17.
 */
public class MarkovCommand extends Command {

    public MarkovCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {

        String tag = args[0];

        MarkovChain chain;
        User user;
        if (tag.equalsIgnoreCase("?")) {
            int loop = 0;
            user = null;
            chain = null;
            while (loop < 20) {
                Map.Entry<String, MarkovChain> entry = Random.choose(Markov.getData().entries());
                if (entry.getValue().isOfMinSize()) {
                    try {
                        user = getNoodle().api.getUserById(entry.getKey()).get();
                        chain = entry.getValue();
                    } catch (InterruptedException | ExecutionException e) {
                        continue;
                    }
                    break;
                }
                loop++;
            }
            if(user==null){
                getChannel().sendMessage("Failed to randomly find a user!");
                return;
            }
        } else {
            user = JCUtil.getUserByMention(getNoodle().api, tag);
            if (user != null) {
                chain = Markov.getData().chain(user.getId());
            } else{
                getChannel().sendMessage("Unknown User!");
                return;
            }
        }

        if (chain.isOfMinSize()) {

            int amount = 1;
            if (args.length > 1) {
                int a2;
                try {
                    a2 = Integer.valueOf(args[1]);
                } catch (Exception e) {
                    a2 = 1;
                }

                if (a2 <= 15) amount = a2;
            }

            List<String> sentences = new ArrayList<>();
            for (int i = 0; i < amount; ++i) {
                Vector<String> words = chain.generate();
                List<String> newwords = new ArrayList<>();
                for (String word : words) {

                    //replace in-text mention tags
                    if (word.startsWith("<@")) {
                        User intextuser = JCUtil.getUserByMention(getNoodle().api, word);
                        if (intextuser != null) word = intextuser.getName();
                        else word = word.replace("<@", "").replace(">", "");
                    }

                    newwords.add(word);
                }

                String sentence = Joiner.on(" ").join(newwords);

                if (!sentence.endsWith("\\.") || !sentence.endsWith(".") || !sentence.endsWith("?") || !sentence.endsWith("!")) {
                    sentence = sentence + Random.choose("\\.", "?", "!");
                }
                sentences.add(sentence);
            }

            String fin = Joiner.on(" ").join(sentences);

            getChannel().sendMessage("**"+user.getName() + "**: " + fin);

        } else getChannel().sendMessage("There is not enough data about this user to generate a sentence!");

    }

    @Override
    public String[] aliases() {
        return new String[]{"markov", "mk", "fun2"};
    }

    @Override
    public String usage() {
        return "!markov <@MentionTag | ?> [amount default 1] [force first words]";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "generate a sentence based on this users previous messages";
    }

    @Override
    public String category() {
        return "Markov";
    }
}
