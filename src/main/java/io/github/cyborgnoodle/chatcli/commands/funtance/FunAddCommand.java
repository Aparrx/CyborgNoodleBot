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

package io.github.cyborgnoodle.chatcli.commands.funtance;

import com.google.common.base.Joiner;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.features.funtance.data.*;
import io.github.cyborgnoodle.settings.data.ServerRole;

import java.util.Arrays;

/**
 * Created by arthur on 17.01.17.
 */
public class FunAddCommand extends Command {

    public FunAddCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        if(!(args.length<2)){

            String type = args[0];
            String[] wordarray = Arrays.copyOfRange(args, 1, args.length);
            String word = Joiner.on(" ").skipNulls().join(wordarray);

            if(type.equalsIgnoreCase("object")){
                ObjectData.getData().add(word);
                getChannel().sendMessage("Added OBJECT "+word);
                return;
            }
            if(type.equalsIgnoreCase("place")){
                OrtData.getData().add(word);
                getChannel().sendMessage("Added PLACE "+word);
                return;
            }
            if(type.equalsIgnoreCase("person")){
                PersonenData.getData().add(word);
                getChannel().sendMessage("Added PERSON "+word);
                return;
            }
            if(type.equalsIgnoreCase("time")){
                UmstandData.getData().add(word);
                getChannel().sendMessage("Added TIME/CIRCUMSTANCE "+word);
                return;
            }
            if(type.equalsIgnoreCase("verb")){
                VerbData.getData().add(word);
                getChannel().sendMessage("Added VERB "+word);
                return;
            }
            getChannel().sendMessage("Unknown type: `"+type+"`");
        }
        else showInvalidArguments();
    }

    @Override
    public String[] aliases() {
        return new String[]{"funadd","fa"};
    }

    @Override
    public String usage() {
        return "!funadd <object/place/person/time/verb> <...>";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "Add a !fun word";
    }

    @Override
    public Permission fullPermission() {
        return new Permission(ServerRole.OWNER);
    }

    @Override
    public String category() {
        return "Sentence generator commands";
    }

    @Override
    public boolean hidden() {
        return true;
    }
}
