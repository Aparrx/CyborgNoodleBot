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
import io.github.cyborgnoodle.util.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Formatter;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class LevelsCommand extends Command {

    public LevelsCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat deciformat = new DecimalFormat("#,###", symbols);

        int i = 1;
        String msg = "**Leaderboard**\n```";
        msg = msg + "    "+new Formatter().format("%-20s%20s%6s","NAME","XP","LEVEL") + "\n";
        msg = msg + "\n";
        for(String uid : getNoodle().getLevels().getLeaderboard().keySet()){
            Long xp = getNoodle().getLevels().getLeaderboard().get(uid);

            String sxp = deciformat.format(xp);
            Integer level = getNoodle().getLevels().getRegistry().getLevel(uid);
            String ifiller = StringUtils.getWhitespaces(2-Integer.valueOf(i).toString().length());
            String name;
            try {
                User user = getNoodle().getAPI().getUserById(uid).get();
                name = user.getNickname(getNoodle().getServer());
                if(name==null){
                    name = user.getName();
                    if(name==null) name = "UNKNOWN";
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                name = "UNKNOWN";
            }

            name = StringUtils.ellipsize(name,19);

            String filler = StringUtils.getWhitespaces(20-name.length());
            String xpfiller = StringUtils.getWhitespaces(10-xp.toString().length());

            Formatter formatter = new Formatter();

            msg = msg + "#"+ifiller+i+" "+formatter.format("%-20s%20s%6s",name,sxp,level.toString()) + "\n";

            //msg = msg + "#"+ifiller+i+" "+name+filler+"  -  "+xp+xpfiller+" "+"XP"+"  -  Level "+level+ "\n";

            i++;
            if(i>20) break;
        }

        msg = msg + "```";


        getChannel().sendMessage(msg);

    }

    @Override
    public String[] aliases() {
        return new String[]{"levels","lvls","lvl","leaderboard","toplist"};
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
