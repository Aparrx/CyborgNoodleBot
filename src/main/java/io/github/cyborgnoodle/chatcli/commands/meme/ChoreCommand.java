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

package io.github.cyborgnoodle.chatcli.commands.meme;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.settings.data.ServerRole;
import io.github.cyborgnoodle.util.Random;

/**
 * Created by arthur on 17.01.17.
 */
public class ChoreCommand extends Command {

    public ChoreCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        String k;
        if(Random.randInt(1,15)==1){
            k = "Go fuck yourself, ";
        } else k = "Go do your chores, ";
        String message = k+ getNoodle().api.getCachedUserById("229083996615606272").getMentionTag();
        getChannel().sendMessage(message);
    }

    @Override
    public String[] aliases() {
        return new String[]{"chore"};
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
        return "tell wonka to do his chores";
    }

    @Override
    public Permission fullPermission() {
        return new Permission(ServerRole.FOUNDER);
    }

    @Override
    public String category() {
        return "Meme commands";
    }

    @Override
    public boolean hidden() {
        return true;
    }
}
