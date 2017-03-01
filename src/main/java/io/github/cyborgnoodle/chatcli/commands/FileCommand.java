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

import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.SaveManager;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.misc.Util;

/**
 * Created by arthur on 18.02.17.
 */
public class FileCommand extends Command {

    public FileCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Cyborg data files");

        String desc = "";

        long totalsize = 0L;

        for(String name : getNoodle().savemanager.getSavefiles().keySet()){
            SaveManager.ConfigFile file = getNoodle().savemanager.getSavefiles().get(name).getValue();
            boolean compressed = getNoodle().savemanager.getSavefiles().get(name).getKey().isCompressed();

            long size = file.getConfigFile().length();
            totalsize = totalsize + size;

            String ctext;
            if(compressed){
                ctext = " (compressed)";
            }
            else ctext = "";

            desc = desc + "**"+name+"**: "+ Util.readableFileSize(size)+ctext+"\n";
        }

        desc = desc + "\n**Total used disk space:** "+Util.readableFileSize(totalsize);

        embed.setDescription(desc);

        embed.setFooter("Sizes may be inaccurate as unsaved data is not included");

        getChannel().sendMessage("",embed);
    }

    @Override
    public String[] aliases() {
        return new String[]{"file","files","size"};
    }

    @Override
    public String usage() {
        return "!file";
    }

    @Override
    public boolean emptyHelp() {
        return false;
    }

    @Override
    public String description() {
        return "show file sizes";
    }

    @Override
    public String category() {
        return "System";
    }
}
