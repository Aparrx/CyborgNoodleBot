package io.github.cyborgnoodle.msg;

import io.github.cyborgnoodle.Random;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthur on 16.10.16.
 */
public class SystemMessages {

    public static String getStart(){

        List<String> l = new ArrayList<String>();

        l.add("Heeyy i'm back");
        l.add("Yo i'm back");
        l.add("I'm back");
        l.add("I'm back online");
        l.add("I'm back online guys");
        l.add("back");
        l.add("Online now");
        l.add("Aaaand i'm online now");
        l.add("yo i'm online");
        l.add("I'm online now");
        l.add("Hey, I'm online");

        return (String) Random.choose(l);
    }

    public static String getStop(){

        List<String> l = new ArrayList<String>();

        l.add("Yo i'm shutting down, brb");
        l.add("brb");
        l.add("Shutting down, brb");
        l.add("Someone is doing maintenance on me, brb");
        l.add("brb soon");

        return (String) Random.choose(l);
    }

    public static String getBlocked(){

        List<String> l = new ArrayList<String>();

        l.add("No");
        l.add("Nah");
        l.add("Nope");
        l.add("Nope.");
        l.add("Not possible");
        l.add("Sorry, but no");
        l.add("Hey, no!");
        l.add("Will not happen.");
        l.add("Fuck you");
        l.add("Gay");
        l.add("NO!");
        l.add("Not like this!");
        l.add("What did you expect?");
        l.add("Woah, no.");
        l.add("Stop with this shit");
        l.add("Shut up");

        return (String) Random.choose(l);
    }

    public static String getWelcome(){

        List<String> l = new ArrayList<String>();

        l.add("Hey {0}, welcome to the server!");
        l.add("Welcome to the server {0}!");
        l.add("Welcome {0}!");
        l.add("Heyy {0}!");
        l.add("{0} joined the server!");

        return (String) Random.choose(l);
    }

    public static String getBanned(){

        List<String> l = new ArrayList<String>();

        l.add("{0} was banned from the server.");
        l.add("{0} is now banned from the server.");
        l.add("{0} is now banned.");
        l.add("{0} was banned.");

        return (String) Random.choose(l);
    }

    public static String getLeave(){

        List<String> l = new ArrayList<String>();

        l.add("{0} left the server!");
        l.add("{0} left the server! \uD83D\uDE14");
        l.add("{0} left the server! \uD83D\uDE13");
        l.add("{0} left the server! \uD83D\uDE25");

        return (String) Random.choose(l);
    }

    public static String getSmut(){

        List<String> l = new ArrayList<String>();

        l.add("Oh well, I remember this");
        l.add("Not again");
        l.add("Why do you want me to post this?");
        l.add("Oh well..");
        l.add("Good thing is .. I was not involved");
        l.add("Ahh I remember this");
        l.add("*sigh*");
        l.add("why?");
        l.add("Oh come on, not again");

        return (String) Random.choose(l);
    }

    public static String getBadWord(){

        List<String> l = new ArrayList<String>();

        l.add("Dude");
        l.add("No");
        l.add("What?");
        l.add("No...");
        l.add("Oh god");
        l.add("Ewww");
        l.add("What the ..");
        l.add("wtf");
        l.add("huh?");
        l.add("*facepalm*");
        l.add("*sigh*");
        l.add("Why?");

        return (String) Random.choose(l);
    }

    public static String getGame(){

        List<String> l = new ArrayList<String>();

        // currently playing

        l.add("with her guns");
        l.add("gorillaz music");
        l.add("guitar");
        l.add("escape to plastic beach");
        l.add("with her one eyed squid buddy");
        l.add("Feel Good Inc.");
        l.add("Clint Eastwood");
        l.add("DARE");
        l.add("On Melancholy Hill");
        l.add("Tomorrow comes today");
        l.add("Rock the House");
        l.add("Stylo");
        l.add("El Mañana");

        return (String) Random.choose(l);
    }

    public static String getHeads(){

        List<String> l = new ArrayList<String>();

        // currently playing

        l.add("Ohhh, it's heads");
        l.add("It's heads");
        l.add("Aaaand ... it's heads");
        l.add("heads.");
        l.add("Oh, it's heads");

        return (String) Random.choose(l);
    }

    public static String getTails(){

        List<String> l = new ArrayList<String>();

        // currently playing

        l.add("Ohhh, it's tails");
        l.add("It's tails");
        l.add("Aaaand ... it's tails");
        l.add("heads.");
        l.add("Oh, it's tails");

        return (String) Random.choose(l);
    }



}
