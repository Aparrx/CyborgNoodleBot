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

package io.github.cyborgnoodle.chatcli.commands.levels;

import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.server.ServerRole;
import io.github.cyborgnoodle.util.JCUtil;

/**
 * Created by arthur on 29.01.17.
 */
public class RemXPCommand extends Command {

    public RemXPCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        if(args.length>=2){

            String mtag = args[0];
            String xpstr = args[1];

            User user = JCUtil.getUserByMention(getNoodle().getAPI(), mtag);

            if(user==null){
                getChannel().sendMessage("Unknown user!");
                return;
            }

            Long xp;

            try {
                xp = Long.valueOf(xpstr);
            } catch (NumberFormatException e) {
                getChannel().sendMessage("Unknown user!");
                return;
            }

            if(xp<0){
                getChannel().sendMessage("You can not remove a negative amount of XP!");
                return;
            }

            getNoodle().getLevels().registry().get(user).substractXp(xp);
            getChannel().sendMessage("Successfully removed "+xp+"!");

        } else this.showInvalidArguments();
    }

    @Override
    public String[] aliases() {
        return new String[]{"remxp","rxp","xp-","x-"};
    }

    @Override
    public String usage() {
        return "!remxp @MentionTag <xp>";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "Remove the given amount of xp from a users account";
    }

    @Override
    public Permission fullPermission() {
        return new Permission(ServerRole.OWNER);
    }
}
