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

package io.github.cyborgnoodle.msg;

/**
 * Created by arthur on 09.11.16.
 */
public class Messages {

    static String GUESS = "$";

    public static String get(String message, String... replace){

        int index = message.indexOf(GUESS);
        while (index >= 0) {

            StringBuffer buf = new StringBuffer(message);

            int start = index;
            int end = index+1;
            try{
                message = buf.replace(start, end, replace[index]).toString();
            } catch (ArrayIndexOutOfBoundsException e){
                return message;
            }

            index = message.indexOf(GUESS, index + 1);
        }

        return message;
    }

}
