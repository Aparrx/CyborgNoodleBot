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

package io.github.cyborgnoodle.features.quotes;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.util.JCUtil;
import io.github.cyborgnoodle.util.Log;

import java.util.concurrent.ExecutionException;

/**
 * Created by arthur on 30.01.17.
 */
public class Quotes {

    //still needs some work

    private CyborgNoodle noodle;

    private Quotes(CyborgNoodle noodle){
        this.noodle = noodle;
    }

    public static Quotes create(CyborgNoodle noodle){
        return new Quotes(noodle);
    }

    public Quote byMessageID(Channel channel, String msg){
        try {
            Message message = JCUtil.getChannelMessageByID(noodle.api, channel, msg).get();
            return new Quote(message);
        } catch (InterruptedException | ExecutionException e) {
            Log.stacktrace(e);
            return null;
        }
    }

    public Quote byRandom(String channel){
        return null;
    }

}
