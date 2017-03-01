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
import io.github.cyborgnoodle.settings.CyborgSettings;
import io.github.cyborgnoodle.settings.data.ServerRole;
import io.github.cyborgnoodle.settings.util.SettingsCode;

import java.io.IOException;

/**
 * Created by arthur on 29.01.17.
 */
public class SettingsCommand extends Command{

    public SettingsCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        if(args.length>=1){

            String code = args[0];

            CyborgSettings newset;
            try {
                newset = SettingsCode.parse(code);
            } catch (IOException e) {
                getChannel().sendMessage("Invalid Settings code!");
                return;
            }

            getNoodle().settings.update(newset);

            getChannel().sendMessage("Settings were updated.");

        } else if(args.length==0){

            String code = SettingsCode.create(getNoodle().settings);

            getChannel().sendMessage("Current Settings:\n```\n"+code+"\n```");


        } else showInvalidArguments();
    }

    @Override
    public String[] aliases() {
        return new String[]{"set","settings"};
    }

    @Override
    public String usage() {
        return "!set | !set <code>";
    }

    @Override
    public boolean emptyHelp() {
        return false;
    }

    @Override
    public String description() {
        return "set and get bot settings for use with the external settings app";
    }

    @Override
    public Permission fullPermission() {
        return new Permission(ServerRole.OWNER);
    }

    @Override
    public String category() {
        return "System";
    }
}
