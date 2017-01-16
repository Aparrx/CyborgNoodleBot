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

package io.github.cyborgnoodle.chatcli.commands.unit;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.misc.UnitConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by arthur on 16.01.17.
 */
public class UnitsCommand extends Command {

    public UnitsCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {

        String msg = "```\n";

        for(UnitConverter.UnitType type : UnitConverter.UnitType.values()){
            String name = type.getName();
            msg = msg.concat(">> "+name+"\n");

            List<UnitConverter.Unit> units = new ArrayList<>(UnitConverter.Unit.getByType(type));
            Collections.sort(units, (o1, o2) -> {
                UnitConverter.UnitSystem sys1 = o1.getSystem();
                UnitConverter.UnitSystem sys2 = o2.getSystem();

                if(sys1.equals(sys2)) return 0;
                else {
                    if(sys1.equals(UnitConverter.UnitSystem.METRIC)) return 1;
                    else return -1;
                }
            });

            for(UnitConverter.Unit unit : units){
                String unitname = unit.getName();
                String unitsystem = unit.getSystem().getName();
                String unitsymbol = unit.getSymbol();
                String row = String.format("%-20s%-6s%-10s", unitname, unitsymbol, unitsystem);
                msg = msg.concat(" "+row+"\n");
            }
            msg = msg.concat("\n");
        }

        msg = msg.concat("```");

        getChannel().sendMessage(msg);

    }

    @Override
    public String[] aliases() {
        return new String[]{"units"};
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
        return "show available units";
    }

    @Override
    public String category() {
        return "UnitConverter commands";
    }
}
