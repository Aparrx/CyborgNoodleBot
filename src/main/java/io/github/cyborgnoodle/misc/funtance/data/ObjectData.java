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

package io.github.cyborgnoodle.misc.funtance.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ObjectData {

    public static List<String> getAll(){

        List<String> l = new ArrayList<>();

        l.addAll(getBaseObjects());
        l.addAll(getBadezimmerObjects());
        l.addAll(getHausKomplettObjects());
        l.addAll(getKuechenObjects());
        l.addAll(getTiere());

        return l;
    }

    public static List<String> getAllWithPersons(){

        List<String> l = new ArrayList<>();

        l.addAll(getAll());
        l.addAll(getNormalPersons());

        return l;
    }

    public static List<String> getBaseObjects(){
        List<String> l = new ArrayList<>();

        l.add("wooden boxes");
        l.add("hammers");
        l.add("toilets");
        l.add("axes");
        l.add("bottles");
        l.add("dildos");
        l.add("vibrators");
        l.add("lubricants");
        l.add("machine guns");

        l.add("hands");
        l.add("feet");

        return l;
    }

    public static List<String> getHausKomplettObjects(){
        List<String> l = new ArrayList<>();

        l.addAll(getKuechenObjects());
        l.addAll(getBadezimmerObjects());


        return l;
    }

    public static List<String> getBadezimmerObjects(){
        List<String> l = new ArrayList<>();

        l.add("soaps");
        l.add("washcloths");
        l.add("washing machines");
        l.add("detergents");


        return l;
    }

    public static List<String> getKuechenObjects() {
        List<String> l = new ArrayList<>();

        l.add("dishwashers");
        l.add("microwaves");
        l.add("water cookers");
        l.add("refrigerators");
        l.add("freezers");
        l.add("mixers");
        l.add("toasters");
        l.add("bread knifes");
        l.add("rinse aid");

        return l;
    }

    public static List<String> getNormalPersons(){

        List<String> l = new ArrayList<>();

        l.add("cops");
        l.add("soldiers");
        l.add("officers");
        l.add("farmers");
        l.add("truckers");
        l.add("savage cleaners");

        l.add("whores");
        l.add("prostitutes");
        l.add("strippers");
        l.add("sex slaves");

        return l;

    }

    public static List<String> getTiere(){

        List<String> l = new ArrayList<>();

        l.add("dogs");
        l.add("frogs");
        l.add("foxes");
        l.add("budgerigars");
        l.add("parrots");
        l.add("cats");
        l.add("snails");
        l.add("magpies");
        l.add("gulls");
        l.add("horses");
        l.add("pigs");


        return l;

    }

}
