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

package io.github.cyborgnoodle.chatcli;

import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.Log;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by arthur on 16.01.17.
 */
public class Commands {

    private static HashMap<String,Command> commands = new HashMap<>();

    public static void register(Command command){
        for(String alias : command.aliases()){
            commands.put(alias,command);
        }
    }

    public static void execute(Message message){

        String content = message.getContent();


        if(content.startsWith("!")){

            String[] localargs = content.replace("!","").split(" ");
            String cmd = localargs[0];
            String[] args;
            try {
                args = Arrays.copyOfRange(localargs, 1, localargs.length);
            } catch (Exception e) {
                args = new String[0];
            }
            Log.info(message.getAuthor().getName()+" issued command: !"+cmd);

            Command command = commands.get(cmd);

            if(command!=null){
                command.execute(message,args);
            }
            else{
                message.getChannelReceiver().sendMessage("[Invalid Command]");
                message.delete();
            }
        }

    }

}
