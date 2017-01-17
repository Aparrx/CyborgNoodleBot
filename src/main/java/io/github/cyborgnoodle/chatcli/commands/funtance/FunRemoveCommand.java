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

import com.google.common.base.Joiner;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.misc.funtance.data.*;
import io.github.cyborgnoodle.server.ServerRole;

import java.util.Arrays;

/**
 * Created by arthur on 17.01.17.
 */
public class FunRemoveCommand extends Command {

    public FunRemoveCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        if(!(args.length<2)){

            String type = args[0];
            String[] wordarray = Arrays.copyOfRange(args, 1, args.length);
            String word = Joiner.on(" ").skipNulls().join(wordarray);

            if(type.equalsIgnoreCase("object")){
                boolean is = ObjectData.getData().remove(word);
                if(is) getChannel().sendMessage("Removed OBJECT "+word);
                else getChannel().sendMessage("Failed to remove OBJECT! Not found!");
                return;
            }
            if(type.equalsIgnoreCase("place")){
                boolean is = OrtData.getData().remove(word);
                if(is) getChannel().sendMessage("Removed PLACE "+word);
                else getChannel().sendMessage("Failed to remove PLACE! Not found!");
                return;
            }
            if(type.equalsIgnoreCase("person")){
                boolean is = PersonenData.getData().remove(word);
                if(is) getChannel().sendMessage("Removed PERSON "+word);
                else getChannel().sendMessage("Failed to remove PERSON! Not found!");
                return;
            }
            if(type.equalsIgnoreCase("time")){
                boolean is = UmstandData.getData().remove(word);
                if(is) getChannel().sendMessage("Removed TIME "+word);
                else getChannel().sendMessage("Failed to remove TIME! Not found!");
                return;
            }
            if(type.equalsIgnoreCase("verb")){
                boolean is = VerbData.getData().remove(word);
                if(is) getChannel().sendMessage("Removed VERB "+word);
                else getChannel().sendMessage("Failed to remove VERB! Not found!");
                return;
            }
            getChannel().sendMessage("Unknown type: `"+type+"`");
        }
        else showInvalidArguments();
    }

    @Override
    public String[] aliases() {
        return new String[]{"funremove","fr","fd","fundel"};
    }

    @Override
    public String usage() {
        return "!funremove <object/place/person/time/verb> <...>";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "remove a !fun word";
    }

    @Override
    public Permission fullPermission() {
        return new Permission(ServerRole.OWNER);
    }

    @Override
    public String category() {
        return "Sentence generator commands";
    }
}
