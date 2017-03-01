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
 *
 */
public class VerbData {

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

        l.addAll(get());

        return l;
    }

    public static List<String> get(){
        List<String> l = new ArrayList<>();

        l.add("farts on");
        l.add("burns");
        l.add("urinates on");

        l.add("jacks off");
        l.add("fucks");
        l.add("penetrates");
        l.add("kills");
        l.add("shoots");
        l.add("chops");
        l.add("stabs");
        l.add("eats");
        l.add("inhales");
        l.add("destroys");
        l.add("bangs");
        l.add("licks");
        l.add("insults");
        l.add("drinks");
        l.add("smokes");
        l.add("kidnaps");
        l.add("rapes");

        return l;
    }
}
