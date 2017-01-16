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

import io.github.cyborgnoodle.Random;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthur on 11.03.16.
 */
public class UmstandData {

    public static List<String> getAll(){

        List<String> l = new ArrayList<>();

        l.add("at moonlight");
        l.add("at rain");
        l.add("at daytime");
        l.add("in the evening");
        l.add("in the morning");
        l.add("at night");
        l.add("at midnight");
        l.add("during a thunder");

        l.add("everyday");
        l.add("normally");
        l.add("frequently");
        l.add("often");

        l.add("every monday");
        l.add("every tuesday");
        l.add("every wednesday");
        l.add("every thursday");
        l.add("every friday");
        l.add("every saturday");
        l.add("every sunday");


        l.add("at "+getRanTime());


        return l;
    }

    public static String getRanTime(){
        Integer h = Random.randInt(0,12);
        Integer m = Random.randInt(0,59);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("AM");
        strings.add("PḾ");

        String ampm = Random.choose(strings);

        String sh = String.valueOf(h);
        String sm = String.valueOf(m);

        if(sm.length()==1){
            sm = "0"+sm;
        }

        return sh+":"+sm;

    }
}
