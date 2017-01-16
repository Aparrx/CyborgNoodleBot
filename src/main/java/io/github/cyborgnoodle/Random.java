package io.github.cyborgnoodle;

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

        int ran = randInt(1, l.size());
        List<T> list = new ArrayList<>(l);
        return list.get(ran-1);
    }

    public static int randInt(int min, int max) {


        java.util.Random rand = new java.util.Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static Boolean choose(){
        int r = randInt(0,1);
        if(r==1){
            return true;
        }
        return false;
    }

    public static <T> T choose(Collection<T> l, T... objss){

        List<T> objs = Arrays.asList(objss);

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

}
