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

package io.github.cyborgnoodle.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Utility for random numbers and choosing from lists;
 * (C) 2016 Enveed (Arthur Schüler) http://github.com/fusionlightcat
 */
public class Random {

    public static <T> T choose(Collection<T> l){

        if(l.size()==0) return null;

        int ran = randInt(1, l.size());
        List<T> list = new ArrayList<>(l);
        return list.get(ran-1);
    }

    public static int randInt(int min, int max) {

        if(!(max>min)) return min;

        java.util.Random rand = new java.util.Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        return rand.nextInt((max - min) + 1) + min;
    }

    public static Boolean choose(){
        int r = randInt(0,1);
        if(r==1){
            return true;
        }
        return false;
    }

    public static <T> T choose(T... from){
        return choose(Arrays.asList(from));
    }

    public static <T> T choose(Collection<T> l, T... objss){

        List<T> objs = Arrays.asList(objss);

        return choose(l,objs);
    }

    public static <T> T choose(Collection<T> l, Collection<T> objs){

        int counter = 0;

        while (true){

            T o = choose(l);
            if(!objs.contains(o)){
                return o;
            }
            else{
                counter++;
                if(counter>30000){
                    return o;
                }
            }

        }
    }

    public static <T> Collection<T> chooseMore(Collection<T> l, int amount, T... objss){
        List<T> list = new ArrayList<>();
        while(amount>0){
            T chosed = choose(l, list);
            list.add(chosed);
        }
        return list;
    }

}
