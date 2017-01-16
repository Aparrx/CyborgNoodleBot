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

import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.levels.LevelConverser;
import io.github.cyborgnoodle.misc.Util;
import io.github.cyborgnoodle.util.StringUtils;

/**
 * Created by arthur on 16.01.17.
 * TODO
 */
public class RankCommand extends Command {

    public RankCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) {

        User user;
        if(args.length==0){
            user = getAuthor();
        }
        else {
            String us = args[0];
            User u = null;
            for(User usr : getNoodle().getAPI().getUsers()){
                if(usr.getMentionTag().equalsIgnoreCase(us)){
                    u = usr;
                    break;
                }
            }
            user = u;
        }

        if(user==null){
            getChannel().sendMessage("Could not find user!");
            return;
        }

        long xptotal = getNoodle().getLevels().getRegistry().getXP(user.getId());
        int level = getNoodle().getLevels().getRegistry().getLevel(user.getId());


        long xpnext = LevelConverser.getXPforLevel(level+1);
        long xpcurrent = LevelConverser.getXPforLevel(level);
        long xpleft = xptotal - xpcurrent;
        long xpfornext = xpnext - xpcurrent;

        long giftleft = getNoodle().getLevels().getRegistry().getGiftStamp(user.getId())-System.currentTimeMillis();

        String tilgift;
        if(giftleft>=0){
            tilgift = Util.toHMS(giftleft) + " GTO";
        }
        else tilgift = "no GTO";

        String visual = "`"+ StringUtils.getVisualisation(xpleft,xpfornext)+"`";

        getChannel().sendMessage(user.getMentionTag()+": "+xpleft+" / "+xpfornext+" XP"+" ["+xptotal+" "+"XP"+" total] | Level "+level+" | "+tilgift+" | "+visual);
    }

    @Override
    public String[] aliases() {
        return new String[]{"rank","rnk"};
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public boolean emptyHelp() {
        return false;
    }
}
