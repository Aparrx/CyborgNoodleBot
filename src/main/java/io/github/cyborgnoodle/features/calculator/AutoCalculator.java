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

package io.github.cyborgnoodle.features.calculator;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import de.btobastian.javacord.entities.message.Message;

/**
 * Created by arthur on 22.02.17.
 */
public class AutoCalculator {

    public static void onMsg(Message message){

        String content = message.getContent();
        String[] words = content.split(" ");

        DoubleEvaluator evaluator = new DoubleEvaluator();

        String answer = "";

        for(String word : words){

            Double result = null;
            try {
                result = evaluator.evaluate(word);
            } catch (Exception ignored) {}
            if(result!=null) answer = answer + "`"+word+" = "+result+"`\n";
        }

        if(!answer.equalsIgnoreCase("")){
            message.getChannelReceiver().sendMessage(answer);
        }

    }

}
