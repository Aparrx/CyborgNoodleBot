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

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.settings.CyborgSettings;
import io.github.cyborgnoodle.util.StringUtils;
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

        process(parts,noodle.settings,message.getChannelReceiver());
    }

    private static void process(String[] words, CyborgSettings settings, Channel channel){

        List<Pair<Double,UnitConverter.Unit>> list = new ArrayList<>();

        int index = 0;
        for(String word : words){

            for(String a : aliases.keySet()){

                boolean found = false;
                UnitConverter.Unit unit = null;
                Double number = null;

                if(word.equalsIgnoreCase(a)){
                    // is unit
                    unit = aliases.get(a);
                    if(index>0){
                        String before = words[index - 1];
                        if(StringUtils.isNumeric(before)){
                            number = Double.valueOf(before);
                            found = true;
                        }
                    }
                    if((index+1)<=(words.length-1)){
                        String after = words[index + 1];
                        if(StringUtils.isNumeric(after)){
                            number = Double.valueOf(after);
                            found = true;
                        }
                    }
                }
                else {
                    String alow = a.toLowerCase();
                    String wordlow = word.toLowerCase();
                    if(wordlow.contains(alow)){
                        //contains unit
                        unit = aliases.get(a);
                        String numtext = wordlow.replace(alow, "");
                        if(StringUtils.isNumeric(numtext)){
                            number = Double.valueOf(numtext);
                            found = true;
                        }
                        else found = false;
                    }
                }

                if(found){

                    boolean enabled = false;
                    switch (unit.getType()){
                        case TEMPERATURE:
                            enabled = settings.autoconv.temperature.get();
                            break;
                        case LENGTH:
                            enabled = settings.autoconv.length.get();
                            break;
                        case AREA:
                            enabled = settings.autoconv.area.get();
                            break;
                        case VOLUME:
                            enabled = settings.autoconv.volume.get();
                            break;
                        case MASS:
                            enabled = settings.autoconv.mass.get();
                            break;
                        case CURRENCY:
                            enabled = settings.autoconv.currency.get();
                            break;
                        case TIME:
                            enabled = settings.autoconv.time.get();
                            break;
                        case DISCORD:
                            enabled = true; // don't convert discord units
                            break;
                    }

                    if(enabled){
                        list.add(new Pair<>(number,unit));
                    }

                }
            }

            index++;
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
                channel.sendMessage(msg);
            }

        }

    }

    private static UnitConverter.Unit findUnit(String part){
        UnitConverter.Unit found = UnitConverter.Unit.getByTyped(part);
        if(found!=null) return found;
        else{
            return null;
        }
    }
}
