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

package io.github.cyborgnoodle.cli.commands;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.cli.CLICommand;
import io.github.cyborgnoodle.cli.completer.ArgumentCompleter;

/**
 * Created by arthur on 05.03.17.
 */
public class StopCommand extends CLICommand {

    public StopCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        getNoodle().stopAsync();
    }

    @Override
    public String[] aliases() {
        return new String[]{"stop","halt","shutdown"};
    }

    @Override
    public String usage() {
        return "stop";
    }

    @Override
    public boolean emptyHelp() {
        return false;
    }

    @Override
    public String description() {
        return "stop the bot";
    }

    @Override
    public ArgumentCompleter completer(int index) {
        return null;
    }
}
