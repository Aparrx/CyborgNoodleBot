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
import io.github.cyborgnoodle.server.ServerRole;

/**
 * Created by arthur on 16.01.17.
 */
public class TeaCommand extends Command {

    public TeaCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        String message = getNoodle().getAPI().getCachedUserById("217783026275319810").getMentionTag()+" have some tea";
        getChannel().sendMessage(message);
    }

    @Override
    public String[] aliases() {
        return new String[]{"tea"};
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
        return "tell roy to have some tea";
    }

    @Override
    public Permission fullPermission() {
        return new Permission(ServerRole.REGULAR);
    }

    @Override
    public String category() {
        return "Meme commands";
    }
}
