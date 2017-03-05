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

import io.github.cyborgnoodle.util.Log;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by arthur on 05.03.17.
 */
public class CLICommands {

    private static HashMap<String,CLICommand> commands = new HashMap<>();

    public static void register(CLICommand command){
        for(String alias : command.aliases()){
            commands.put(alias,command);
        }
    }

    public static HashMap<String, CLICommand> getCommands() {
        return commands;
    }

    public static boolean execute(String message){

        String[] localargs;
        localargs = message.split(" +(?=((.*?(?<!\\\\\\\\)'){2})*[^']*$)"); // filter 'as one word'

        for (int i=0; i<localargs.length;i++){
            //replace if word contains 'this' (when there is more than one word)
            localargs[i] = localargs[i].replace("'","");
        }

        String cmd = localargs[0];
        String[] args;
        try {
            args = Arrays.copyOfRange(localargs, 1, localargs.length);
        } catch (Exception e) {
            args = new String[0];
        }

        CLICommand command = commands.get(cmd);

        if(command!=null){

            try {
                command.execute(message,args);
            } catch (Exception e) {
                Log.error("Error executing command: "+e.getClass().getSimpleName()+": "+e.getMessage());
                Log.stacktrace(e);

            }
            return true;
        }
        else{
            Log.error(cmd+": Command not found!");
            return false;
        }

    }


}
