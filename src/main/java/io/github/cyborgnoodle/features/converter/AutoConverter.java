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

package io.github.cyborgnoodle.features.converter;

import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.CyborgNoodle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class AutoConverter {

    private static Map<String,UnitConverter.Unit> aliases;

    static {
        aliases = new HashMap<>();
    }

    public static void cacheAliases(){

        for(UnitConverter.Unit unit : UnitConverter.Unit.values()){

            UnitConverter.Aliases a = unit.getAliases();
            if(a!=null){
                for(String al : a.getAliases()) aliases.put(al,unit);
            }

        }

    }

    public static void onMsg(Message message, CyborgNoodle noodle){
        String content = message.getContent();

        String[] parts = content.split(" ");

        List<Pair<Double,UnitConverter.Unit>> list = new ArrayList<>();

        int i = 0;
        for(String part : parts){
            UnitConverter.Unit unit = findUnit(part);

            if(unit!=null){
                boolean skip = false;
                switch (unit.getType()){
                    case TEMPERATURE:
                        skip = !noodle.settings.autoconv.temperature.get();
                        break;
                    case LENGTH:
                        skip = !noodle.settings.autoconv.length.get();
                        break;
                    case AREA:
                        skip = !noodle.settings.autoconv.area.get();
                        break;
                    case VOLUME:
                        skip = !noodle.settings.autoconv.volume.get();
                        break;
                    case MASS:
                        skip = !noodle.settings.autoconv.mass.get();
                        break;
                    case CURRENCY:
                        skip = !noodle.settings.autoconv.currency.get();
                        break;
                    case TIME:
                        skip = !noodle.settings.autoconv.time.get();
                        break;
                    case DISCORD:
                        skip = true; // don't convert discord units
                        break;
                }

                if(!skip){
                    try {
                        String before = parts[i - 1]; // <num> <unit>
                        Double num = Double.valueOf(before);
                        Pair<Double,UnitConverter.Unit> pair = new Pair<>(num,unit);
                        list.add(pair);
                    } catch (Exception ignored) {}
                    try {
                        String before = parts[i + 1]; // <unit> <num>
                        Double num = Double.valueOf(before);
                        Pair<Double,UnitConverter.Unit> pair = new Pair<>(num,unit);
                        list.add(pair);
                    } catch (Exception ignored) {}

                    try {
                        // <unit><num> or <num><unit>

                        Double num = 0d;
                        for(String al : unit.getAliases().getAliases()){ // maybe NullPointer ??
                            part = part.replace(al, "");
                        }
                        num = Double.valueOf(part);
                        Pair<Double,UnitConverter.Unit> pair = new Pair<>(num,unit);
                        list.add(pair);
                    } catch (Exception ignored) {}
                }

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
        else{
            for(String a : aliases.keySet()){
                if(part.contains(a)){
                    return aliases.get(a);
                }
            }
            return null;
        }
    }
}
