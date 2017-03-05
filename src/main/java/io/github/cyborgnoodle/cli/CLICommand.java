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

package io.github.cyborgnoodle.cli;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.cli.completer.ArgumentCompleter;
import io.github.cyborgnoodle.util.Log;

/**
 *
 */
public abstract class CLICommand {

    private String message;

    private CyborgNoodle noodle;

    public CLICommand(CyborgNoodle noodle){
        this.noodle = noodle;
    }

    public void execute(String message, String[] args) throws Exception {
        this.message = message;

        if(emptyHelp() && args.length==0){
            showInvalidArguments();
        }
        else onCommand(args);
    }

    public void showInvalidArguments(){
        Log.warn("Invalid arguments! Usage: `" + usage() + "`");
    }

    public abstract void onCommand(String[] args) throws Exception;

    public abstract String[] aliases();

    public abstract String usage();

    public String detailusage(){
        return null;
    }

    public abstract boolean emptyHelp();

    public abstract String description();

    public boolean hidden(){
        return false;
    }

    public String category(){
        return "Main commands";
    }

    public abstract ArgumentCompleter completer(int index);

    public CyborgNoodle getNoodle() {
        return noodle;
    }

    public String getMessage(){
        return message;
    }
}
