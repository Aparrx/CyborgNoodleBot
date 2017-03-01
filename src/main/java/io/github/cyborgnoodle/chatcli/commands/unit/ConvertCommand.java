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
import io.github.cyborgnoodle.features.converter.UnitConverter;

/**
 * Created by arthur on 16.01.17.
 */
public class ConvertCommand extends Command {

    public ConvertCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {

        if(args.length==4){

            String amountstr = args[0];
            String unitfromsym = args[1];
            String unittosym = args[3];

            try {
                Double amount = Double.valueOf(amountstr);

                UnitConverter.Unit from = UnitConverter.Unit.getBySymbol(unitfromsym);

                if(from==null){
                    getChannel().sendMessage("This is not a valid unit: `"+unitfromsym+"`");
                    return;
                }

                UnitConverter.Unit to = UnitConverter.Unit.getBySymbol(unittosym);

                if(to==null){
                    getChannel().sendMessage("This is not a valid unit: `"+unittosym+"`");
                    return;
                }

                try {
                    double converted = UnitConverter.convert(amount, from, to);
                    getChannel().sendMessage("`"+amount+" "+from.getSymbol()+"  =  "+converted+" "+to.getSymbol()+"`");

                } catch (UnitConverter.IncompatibleUnitTypesException e) {
                    getChannel().sendMessage("You can not convert a "+from.getType().getName()+" to a "+to.getType().getName()+"!");
                }

            } catch (NumberFormatException e) {
                getChannel().sendMessage("This is not a valid number: `"+amountstr+"`");
            }

        } else showInvalidArguments();

    }

    @Override
    public String[] aliases() {
        return new String[]{"convert","c"};
    }

    @Override
    public String usage() {
        return "!convert <number> <unit> to <unit2>";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "convert units";
    }

    @Override
    public String category() {
        return "UnitConverter commands";
    }
}
