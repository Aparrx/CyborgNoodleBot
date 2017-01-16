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
import io.github.cyborgnoodle.server.ServerRole;
import io.github.cyborgnoodle.util.table.CodeTable;

import java.util.ArrayList;

/**
 * Created by arthur on 16.01.17.
 */
public class RanksCommand extends Command {

    public RanksCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) {

        String title = "**Available Ranks**";

        ArrayList<Integer> levels = new ArrayList<>(getNoodle().getLevels().getCalculator().getRoles().keySet());
        levels.sort((o1, o2) -> {
            int i1 = o1;
            int i2 = o2;
            if (i1 == i2) return 0;
            else if (i1 < i2) return -1;
            else return 1;
        });

        CodeTable table = new CodeTable(20,6);
        table.setRightBound(false,true);
        table.addRow("ROLE","LEVEL");
        table.addRow("","");

        for(int lvl : levels){
            ServerRole sr = getNoodle().getLevels().getCalculator().getRoles().get(lvl);
            String rname = getNoodle().getRole(sr).getName();
            table.addRow(rname,Integer.valueOf(lvl).toString());
        }

        String msg = title + "\n" + table.asCodeBlock();

        getChannel().sendMessage(msg);
    }

    @Override
    public String[] aliases() {
        return new String[]{"ranks"};
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
        return "show available ranks";
    }
}
