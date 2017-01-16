package io.github.cyborgnoodle.misc;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.Random;
import io.github.cyborgnoodle.msg.SystemMessages;
import io.github.cyborgnoodle.server.ServerChannel;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

/**
 * Created by arthur on 17.11.16.
 */
public class BadWords {

    CyborgNoodle noodle;

    List<String> BADWORDS;

    public BadWords(CyborgNoodle noodle){
        this.noodle = noodle;
        BADWORDS = Arrays.asList("sausagesoup","sausagesup","nihilism","ally","vegan","cummies","gay","homosexual","shit","ass","cunt","fuck","butt","tits","penis",
                ":100:isayso:100:");
    }

    public void onMessage(Message m){

        String msg = m.getContent();

        msg = adjustMsg(msg);

        User u = m.getAuthor();

        if(m.getChannelReceiver().getId().equals(noodle.getChannel(ServerChannel.TRASH))){
            return;
        }

        for(String word : BADWORDS){
            if(msg.contains(word)){
                punish(m.getChannelReceiver());

                return;
            }
        }

    }

    private void punish(Channel c){

        int i = Random.randInt(1,4);

        if(i==1){
            String msg = SystemMessages.getBadWord();

            c.sendMessage(msg);
        }

    }

    public static String adjustMsg(String s){

        s = s.replace(" ","");
        s = s.replace(".","");
        s = s.replace("#","");
        s = s.replace("=","");
        s = s.replace("@","a");
        s = s.replace("5","s");
        s = s.replace("8","b");
        s = s.replace("*","");
        s = s.replace("_","");
        s = s.replace("~","");
        s = s.replace("-","");
        s = s.replace("'","");
        s = s.replace("\"","");
        s = s.replace("`","");

        s = s.toLowerCase();

        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("\\p{M}", "");

        return s;
    }

}
