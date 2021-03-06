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
import io.github.cyborgnoodle.features.levels.TempUser;
import io.github.cyborgnoodle.util.StringUtils;
import io.github.cyborgnoodle.util.table.CodeTable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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

        CodeTable table = new CodeTable(4,25,15,6);
        table.setRightBound(false,false,true,true);

        int i = 1;
        String title = "**Leaderboard**";

        table.addRow("","NAME","XP","LEVEL");
        table.addRow("","","","");

        for(String uid : getNoodle().levels.getLeaderboard().keySet()){

            TempUser tu = getNoodle().levels.getLeaderboard().get(uid);

            Long xp = tu.getXp();
            String sxp = deciformat.format(xp);
            Integer level = tu.getLevel();
            String ifiller = StringUtils.getWhitespaces(2-Integer.valueOf(i).toString().length());
            String name;
            User user = tu.getUser();
            name = user.getNickname(getNoodle().getServer());
            if(name==null){
                name = user.getName();
                if(name==null) name = "UNKNOWN";
            }

            name = StringUtils.removeEmojiAndSymbol(name);
            name = StringUtils.ellipsize(name,19);

            //Formatter formatter = new Formatter();

            table.addRow("#"+ifiller+i,name,sxp,level.toString());

            //msg = msg + "#"+ifiller+i+" "+formatter.format("%-20s%20s%6s",name,sxp,level.toString()) + "\n";

            //msg = msg + "#"+ifiller+i+" "+name+filler+"  -  "+xp+xpfiller+" "+"XP"+"  -  Level "+level+ "\n";

            i++;
            if(i>20) break;
        }

        String codeblock = table.asCodeBlock();

        String msg = title + "\n" +codeblock;


        getChannel().sendMessage(msg);

    }

    @Override
    public String[] aliases() {
        return new String[]{"levels","lvl","lvls","level"};
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
        return "show the leaderboard";
    }

    @Override
    public String category() {
        return "XP";
    }
}
