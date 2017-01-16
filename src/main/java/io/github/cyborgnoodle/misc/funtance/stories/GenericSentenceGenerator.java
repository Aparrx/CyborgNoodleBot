package io.github.cyborgnoodle.misc.funtance.stories;

import io.github.cyborgnoodle.Random;
import io.github.cyborgnoodle.misc.funtance.data.*;

/**
 *
 */
public class GenericSentenceGenerator {

    public static String create(){

        String person = Random.choose(PersonenData.getAll());
        String does = Random.choose(VerbData.getAll());

        String object;
        if(Random.choose()){
            object = Random.choose(ObjectData.getAll());
        }
        else {
            object = Random.choose(PersonenData.getAll(),person);
        }

        String umstand = Random.choose(UmstandData.getAll());

        String place = Random.choose(OrtData.getAll());

        if(Random.choose()){
            return person+" "+does+" "+object+" "+umstand+" "+place+".";
        }
        else if(Random.choose()){
            return umstand+" "+person+" "+does+" "+object+" "+place+".";
        } else {
            return person+" "+does+" "+object+" "+place+".";
        }

    }

}
