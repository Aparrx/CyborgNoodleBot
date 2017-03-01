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

package io.github.cyborgnoodle.features.funtance.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by arthur on 11.03.16.
 */
public class OrtData {

    private static Set<String> data = new HashSet<>(getAll());

    public static Set<String> getData() {
        return data;
    }

    public static void setData(Set<String> d) {
        data = d;
        data.addAll(getAll());
    }

    private static List<String> getAll(){

        List<String> l = new ArrayList<>();

        l.add("in the basement");
        l.add("in the children's room");
        l.add("in the kitchen");
        l.add("in the living room");
        l.add("in the bathroom");
        l.add("in the shower");
        l.add("in the workshop");
        l.add("in the corridor");
        l.add("in the garage");
        l.add("in the underground garage");
        l.add("on the attic");
        l.add("in the storage room");
        l.add("in the kindergarten");
        l.add("in the town hall");
        l.add("in the school");
        l.add("in the elementary school");
        l.add("in the upper school");
        l.add("in the cafe");
        l.add("in the restaurant");
        l.add("at the garbage site");
        l.add("in the church");
        l.add("in the vicarage");
        l.add("at IKEA");
        l.add("in veterinary practice");
        l.add("in the doctor's office");
        l.add("in the forest");
        l.add("in the nightclub");
        l.add("in the electrical shop");
        l.add("at Starbucks");
        l.add("in the garden");
        l.add("in the hotel");
        l.add("in the hotel pool");
        l.add("on the hotel toilet");
        l.add("in the motel");
        l.add("behind the motel");
        l.add("in the bush");
        l.add("at ALDI");
        l.add("behind ALDI");
        l.add("in the sauna");
        l.add("in the gym");
        l.add("in the cinema");
        l.add("in the theater");
        l.add("in opera");
        l.add("in the operetta");
        l.add("in the cabaret");
        l.add("in the circus");
        l.add("in the zoo");
        l.add("under the bridge");
        l.add("in a dark corner");
        l.add("in a dark alley");
        l.add("in the brothel");
        l.add("in the drugstore");
        l.add("in the methlab");
        l.add("in the erotic shop");
        l.add("in the ghetto");
        l.add("in the porn studio");
        l.add("on a pornoparty");
        l.add("on a dildoparty");

        l.add("in Portland");
        l.add("in australia");
        l.add("in germany");
        l.add("in the netherlands");
        l.add("in the USA");
        l.add("in the UK");
        l.add("in Leeds");

        return l;
    }

}
