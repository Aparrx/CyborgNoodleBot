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

package io.github.cyborgnoodle.misc.funtance.stories;

import io.github.cyborgnoodle.Random;
import io.github.cyborgnoodle.misc.funtance.data.*;

/**
 *
 */
public class GenericSentenceGenerator {

    public static String create(){

        String person = Random.choose(PersonenData.getData());
        String does = Random.choose(VerbData.getData());

        String object;
        if(Random.choose()){
            object = Random.choose(ObjectData.getData());
        }
        else {
            object = Random.choose(PersonenData.getData(),person);
        }

        String umstand = Random.choose(UmstandData.getData());

        String place = Random.choose(OrtData.getData());

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
