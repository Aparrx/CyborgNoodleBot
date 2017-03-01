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

/**
 *
 */
public class DiscordUnits {

    public static UnitConverter.Unit getHighestUnit(double xp){

        if(xp<1000) return UnitConverter.Unit.XP;
        if(xp<25000) return UnitConverter.Unit.SMONG;
        if(xp<100000) return UnitConverter.Unit.LSD;
        if(xp<200000) return UnitConverter.Unit.MEME;

        return UnitConverter.Unit.NOOD;
    }

    public double xp(double xp, UnitConverter.Unit unit){
        try {
            return UnitConverter.convert(xp,unit, UnitConverter.Unit.XP);
        } catch (UnitConverter.IncompatibleUnitTypesException e) {
            return xp;
        }
    }

}
