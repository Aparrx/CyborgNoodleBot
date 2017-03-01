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

package io.github.cyborgnoodle.chatcli.commands.poll;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.settings.data.ServerRole;

import java.util.Arrays;

/**
 * Created by arthur on 16.01.17.
 */
public class PollCommand extends Command {

    public PollCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {

        if(!Arrays.asList(args).contains("#")){
            String[] choices = args;

            getNoodle().polls.start(choices,getChannel(),null);
        }
        else {

            String cmd = getMessage().getContent().replace("!","");

            String[] parts = cmd.replace("poll ","").replace(" # ","#").split("#");
            String choicepart = parts[0];
            String descpart = parts[1];

            String[] choices = choicepart.split(" ");

            getNoodle().polls.start(choices,getChannel(),descpart);

        }

    }

    @Override
    public String[] aliases() {
        return new String[]{"poll"};
    }

    @Override
    public String usage() {
        return "!poll <option1> <option2> ... [# Title text]";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "start a new poll";
    }

    @Override
    public Permission fullPermission() {
        return new Permission(ServerRole.STAFF);
    }

    @Override
    public String category() {
        return "Poll commands";
    }


}
