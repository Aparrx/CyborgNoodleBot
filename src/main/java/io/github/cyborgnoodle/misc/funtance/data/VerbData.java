package io.github.cyborgnoodle.misc.funtance.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class VerbData {

    public static List<String> getAll(){

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
