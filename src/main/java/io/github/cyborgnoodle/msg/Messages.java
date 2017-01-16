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
