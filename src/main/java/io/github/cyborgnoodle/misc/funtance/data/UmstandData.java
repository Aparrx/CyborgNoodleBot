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
