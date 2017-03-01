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

package io.github.cyborgnoodle.chatcli.commands;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.features.quotes.Quote;
import io.github.cyborgnoodle.misc.ReactionWords;
import io.github.cyborgnoodle.settings.data.ServerRole;

/**
 * Created by arthur on 30.01.17.
 */
public class ReactCommand extends Command {

    public ReactCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        String name = args[0];

        Quote quote = ReactionWords.get(getNoodle(), name);

        if(quote!=null){
            getChannel().sendMessage(getAuthor().getMentionTag()+":",quote.toEmbed());

        } else getChannel().sendMessage("Unknown name!");

        getMessage().delete();
    }

    @Override
    public String[] aliases() {
        return new String[]{"react","r"};
    }

    @Override
    public String usage() {
        return "!react <name>";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "react by name";
    }

    @Override
    public Permission fullPermission() {
        return new Permission(ServerRole.STAFF);
    }

    @Override
    public String category() {
        return "Discord";
    }
}
