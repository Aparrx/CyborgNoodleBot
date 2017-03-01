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

import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.msg.SystemMessages;
import io.github.cyborgnoodle.settings.data.ServerRole;
import io.github.cyborgnoodle.util.Random;

import java.awt.*;

/**
 * Created by arthur on 16.01.17.
 */
public class FuxkitCommand extends Command{

    public FuxkitCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) {

        getNoodle().api.setGame("with herself");

        Boolean force = false;
        if(args.length>=1){
            if(args[1].equalsIgnoreCase("force")){
                if(getNoodle().hasPermission(getAuthor(),new Permission(ServerRole.STAFF))) force = true;
            }
        }

        int i = Random.randInt(0,19);

        if(i==0 || force){
            String message = SystemMessages.getSmut();

            EmbedBuilder embed = new EmbedBuilder();

            embed.setColor(new Color(191,191,191));
            embed.setImage("https://goo.gl/59skAR");

            getChannel().sendMessage(message,embed);
        }
    }

    @Override
    public String[] aliases() {
        return new String[]{"fuxkit"};
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public String category() {
        return "Meme commands";
    }

    @Override
    public boolean emptyHelp() {
        return false;
    }

    @Override
    public String description() {
        return "...";
    }

    @Override
    public boolean hidden() {
        return true;
    }
}
