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
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.msg.SystemMessages;
import io.github.cyborgnoodle.util.Random;

/**
 * Created by arthur on 16.01.17.
 */
public class FlipCommand extends Command {

    public FlipCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) {
        if(Random.choose()){
            getChannel().sendMessage(SystemMessages.getHeads());
        }
        else {
            getChannel().sendMessage(SystemMessages.getTails());
        }
    }

    @Override
    public String[] aliases() {
        return new String[]{"flip","coin"};
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
        return "flip a coin";
    }

    @Override
    public String category() {
        return "Utility";
    }

    @Override
    public boolean hidden() {
        return true;
    }
}
