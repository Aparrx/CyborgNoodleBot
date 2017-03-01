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

package io.github.cyborgnoodle.features.simulator;

import com.google.common.base.Joiner;
import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.features.markov.Markov;
import io.github.cyborgnoodle.features.markov.MarkovChain;
import io.github.cyborgnoodle.settings.data.ServerChannel;
import io.github.cyborgnoodle.util.Random;

import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

/**
 * Created by arthur on 17.02.17.
 */
public class Simulator {

    public static void post(CyborgNoodle noodle){

        Set<Map.Entry<String, MarkovChain>> entries = Markov.getData().entries();
        entries.removeIf(e -> !e.getValue().isOfMinSize());

        Map.Entry<String, MarkovChain> chosen = Random.choose(entries);

        if(chosen==null) return;

        String uid = chosen.getKey();
        MarkovChain chain = chosen.getValue();

        Vector<String> result = chain.generate();

        String sentence = Joiner.on(" ").join(result);

        if (!sentence.endsWith("\\.") || !sentence.endsWith(".") || !sentence.endsWith("?") || !sentence.endsWith("!")) {
            sentence = sentence + Random.choose("\\.", "?", "!");
        }

        User user;
        try {
            user = noodle.api.getUserById(uid).get();
        } catch (InterruptedException | ExecutionException e) {
            return;
        }
        if(user==null) return;
        noodle.getChannel(ServerChannel.SIMULATOR).sendMessage("**"+user.getName()+"**: "+sentence);

    }

}
