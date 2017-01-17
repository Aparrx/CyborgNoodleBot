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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by arthur on 06.03.16.
 */
public class PersonenData {

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

        l.addAll(getDiscordPersons());

        return l;
    }

    public static List<String> getDiscordPersons(){
        List<String> l = new ArrayList<>();


        l.add("Enveed");
        l.add("Jussa");
        l.add("Grandpa");
        l.add("Dax");
        l.add("Quatrer");
        l.add("TrailerPoopers");
        l.add("MurdocOfficial");
        l.add("2D");
        l.add("Pez");
        l.add("Wonka");
        l.add("Roy");

        l.add("ralphwiggum16");

        l.add("A subreddit mod");
        l.add("Someone from the other discord");
        l.add("Marvelking 9000");

        l.add("Damon");
        l.add("Jamie");

        l.add("CyborgNoodle");
        l.add("Noodle");
        l.add("Murdoc");
        l.add("Russel");
        l.add("The Boogieman");


        return l;
    }

}
