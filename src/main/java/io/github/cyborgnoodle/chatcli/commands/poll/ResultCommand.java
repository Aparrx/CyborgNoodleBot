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

import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.server.ServerRole;

/**
 * Created by arthur on 16.01.17.
 */
public class ResultCommand extends Command {

    public ResultCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        EmbedBuilder em = getNoodle().getPolls().result();
        getChannel().sendMessage("",em);
    }

    @Override
    public String[] aliases() {
        return new String[]{"result","results"};
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
        return "show poll results";
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
