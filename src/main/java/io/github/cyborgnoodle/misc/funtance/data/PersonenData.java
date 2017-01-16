package io.github.cyborgnoodle.misc.funtance.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthur on 06.03.16.
 */
public class PersonenData {

    public static List<String> getAll(){

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

        l.add("Frank Ocean");
        l.add("Childish Gambino");

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
