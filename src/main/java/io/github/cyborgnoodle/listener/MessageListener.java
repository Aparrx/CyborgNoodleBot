package io.github.cyborgnoodle.listener;

import com.vdurmont.emoji.EmojiManager;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import de.btobastian.javacord.listener.message.MessageDeleteListener;
import de.btobastian.javacord.listener.message.MessageEditListener;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Random;
import io.github.cyborgnoodle.misc.AutoConverter;
import io.github.cyborgnoodle.misc.Util;
import io.github.cyborgnoodle.msg.ConversationMessages;
import io.github.cyborgnoodle.msg.SystemMessages;
import io.github.cyborgnoodle.server.ServerChannel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 */
public class MessageListener implements MessageCreateListener, MessageEditListener, MessageDeleteListener {

    CyborgNoodle noodle;

    public MessageListener(CyborgNoodle noods){
        this.noodle = noods;
    }

    public void onMessageCreate(DiscordAPI discordAPI, Message message) {

        if(!message.getAuthor().equals(noodle.getAPI().getYourself())) {
            if (true) { //workaround original: !noodle.getSpamFilter().isSpam(message)

                if(noodle.getSpamFilter().isTimeout(message.getAuthor())){
                    message.delete();
                    return;
                }

                noodle.getLevels().onMessage(message.getAuthor());
                noodle.getChatCommands().onMessage(message);
                //noodle.getBadWords().onMessage(message);

                noodle.getWordStats().onMessage(message);

                if(message.getMentions().contains(noodle.getAPI().getYourself())){
                    message.addUnicodeReaction(Random.choose(EmojiManager.getAll()).getUnicode());
                }

                if(!message.getContent().startsWith("!")){
                    AutoConverter.onMsg(message);
                }
            }
        }

    }

    public void onMessageDelete(DiscordAPI discordAPI, Message message) {
        //noodle.getChannel(ServerChannel.LOG).sendMessage(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)+" DEL - "+message.getAuthor().getName()+": "+message.getContent());
    }

    public void onMessageEdit(DiscordAPI discordAPI, Message message, String s) {

        int r = Random.randInt(0,50);

        if(r==1){
            message.getChannelReceiver().sendMessage("I saw that edit "+message.getAuthor().getMentionTag());
        }

    }
}
