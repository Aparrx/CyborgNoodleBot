package io.github.cyborgnoodle.chatbot;

import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.CyborgNoodle;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

/**
 * ChatBot
 */
public class ChatBot {

    CyborgNoodle noodle;
    Bot bot;
    Chat usrchat;

    public ChatBot(CyborgNoodle noodle){
        this.noodle = noodle;

        bot = new Bot("noodle",noodle.getSaveManager().getConfigDir().toString());
        usrchat = new Chat(bot);
    }

    public void onMessage(Message msg){

        if(msg.getMentions().contains(noodle.getAPI().getYourself())){

            String cont = msg.getContent().replace(noodle.getAPI().getYourself().getMentionTag(),"");
            react(msg,cont);
        }

    }

    public void react(Message m, String cont){


        String resp = usrchat.multisentenceRespond(cont);
        m.reply(resp+" "+m.getAuthor().getMentionTag());
    }

}
