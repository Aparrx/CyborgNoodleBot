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

package io.github.cyborgnoodle.chatcli.words;

import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.features.wordstats.WordStats;
import io.github.cyborgnoodle.util.StringUtils;

/**
 * Created by arthur on 17.01.17.
 */
public class ExceptionsCommand extends Command {

    public ExceptionsCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        String listing = StringUtils.toCommaList(WordStats.EXCEPT);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Word Counter exceptions");
        builder.setDescription(listing);
        getChannel().sendMessage("",builder);
    }

    @Override
    public String[] aliases() {
        return new String[]{"exceptions","exception","ex","except"};
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
        return "show word counter exceptions";
    }

    @Override
    public String category() {
        return "Word commands";
    }

    @Override
    public boolean hidden() {
        return true;
    }
}
