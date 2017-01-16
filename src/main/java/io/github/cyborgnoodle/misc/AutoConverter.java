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

package io.github.cyborgnoodle.misc;

import de.btobastian.javacord.entities.message.Message;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AutoConverter {

    public static void onMsg(Message message){
        String content = message.getContent();

        String[] parts = content.split(" ");

        List<Pair<Double,UnitConverter.Unit>> list = new ArrayList<>();

        int i = 0;
        for(String part : parts){
            UnitConverter.Unit unit = findUnit(part);
            if(unit!=null){
                try {
                    String before = parts[i - 1];
                    Double num = Double.valueOf(before);
                    Pair<Double,UnitConverter.Unit> pair = new Pair<>(num,unit);
                    list.add(pair);
                } catch (Exception ignored) {}
            }
            i++;
        }

        if(list.size()>0){

            String msg = "";

            for(Pair<Double,UnitConverter.Unit> pair : list){
                Double num = pair.getKey();
                UnitConverter.Unit unit = pair.getValue();
                UnitConverter.Unit equivalent = unit.getEquivalent();
                if(equivalent!=null){
                    try {
                        double convert = UnitConverter.convert(num, unit, equivalent);
                        msg = msg + "`"+num+" "+unit.getSymbol()+" = "+convert+" "+equivalent.getSymbol()+"`\n";
                    } catch (UnitConverter.IncompatibleUnitTypesException ignored) {}
                }
            }

            if(msg.length()>0){
                message.getChannelReceiver().sendMessage(msg);
            }

        }
    }

    private static UnitConverter.Unit findUnit(String part){
        UnitConverter.Unit found = UnitConverter.Unit.getByTyped(part);
        if(found!=null) return found;
        else return null;
    }
}
