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

package io.github.cyborgnoodle.chatcli.commands;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageAttachment;
import de.btobastian.javacord.entities.message.MessageHistory;
import de.btobastian.javacord.entities.message.embed.Embed;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.message.embed.EmbedImage;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.misc.GoogleURLShortening;
import io.github.cyborgnoodle.util.JCUtil;
import io.github.cyborgnoodle.util.Random;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by arthur on 18.01.17.
 */
public class QuoteCommand extends Command {

    private static int LIMIT = 300;

    public QuoteCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        String str = args[0];

        MessageHistory history = null;
        Channel channel = getChannel();
        if(args.length>=2){
            String ch = args[1];
            if(ch.contains("<#")){
                String chid = ch.replace("<#","").replace(">","");
                Channel xc = getNoodle().api.getChannelById(chid);
                if(xc!=null) channel = xc;
                else {
                    getChannel().sendMessage("Channel not found! Falling back to this channel.");
                    channel = getChannel();
                }

            }
            else {
                getChannel().sendMessage("This is not a valid Channel Tag! Falling back to this channel.");
                channel = getChannel();
            }

            if(args.length==3){
                String option = args[2];

                if(option.startsWith("b")){
                    String mid = option.replace("b","");
                    if(!mid.matches("[0-9]+")){
                        getChannel().sendMessage("This is not a valid option message ID!");
                        return;
                    }
                    else {
                        history = getLastMessages(LIMIT,channel,Option.BEFORE,mid);
                    }
                } else if(option.startsWith("a")){
                    String mid = option.replace("a","");
                    if(!mid.matches("[0-9]+")){
                        getChannel().sendMessage("This is not a valid option message ID!");
                        return;
                    }
                    else {
                        history = getLastMessages(LIMIT,channel,Option.AFTER,mid);
                    }
                } else {
                    if(option.equalsIgnoreCase("-")){
                        history = getLastMessages(LIMIT,channel,Option.NONE,null);
                    }
                    else{
                        getChannel().sendMessage("This is not a valid option! Use `a123456789` or `b123456789`");
                        history = getLastMessages(LIMIT,channel,Option.NONE,null);
                    }

                }

            }
            else history = getLastMessages(LIMIT,channel, Option.NONE, null);

        }

        Message message;
        if(str.contains("<@")){
            //mention tag
            String userid = str.replace("<@","").replace(">","");

            Message msg = getLast(findByUser(history, userid));
            if(msg!=null) message = msg;
            else{
                getChannel().sendMessage("Quote failed! User message not found in the last "+LIMIT+" messages!");
                return;
            }
        }
        else {

            if(str.equalsIgnoreCase("?")){
                Message msg = Random.choose(history.getMessages(),
                        findByUser(history,getNoodle().api.getYourself().getId()).toArray(
                                new Message[findByUser(history,getNoodle().api.getYourself().getId()).size()]));
                if(msg!=null) message = msg;
                else{
                    getChannel().sendMessage("Quote failed! No messages in this channel?");
                    return;
                }
            }
            else {
                String msgid = str;
                if(!msgid.matches("[0-9]+")){
                    getChannel().sendMessage("This is not a valid message ID!");
                    return;
                }
                Message msg = null;
                try {
                    msg = JCUtil.getChannelMessageByID(getNoodle().api,channel,msgid).get();
                } catch (InterruptedException | ExecutionException e) {
                    getChannel().sendMessage("Failed to find message! (Does it exist?)");
                }

                if(msg!=null) message = msg;
                else{
                    getChannel().sendMessage("Quote failed! Message with ID not found!");
                    return;
                }
            }

        }

        Calendar calendar = message.getCreationDate();
        User author = message.getAuthor();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z");
        dateFormat.setTimeZone(calendar.getTimeZone());

        String footer = dateFormat.format(calendar.getTime()) + " #"+channel.getName();

        URL icon = author.getAvatarUrl();
        String nick = author.getNickname(getNoodle().getServer());
        String name = author.getName();

        String fullname;
        if(nick!=null) fullname = nick + " (" + name + ")";
        else fullname = name;

        String content = message.getContent();

        //

        EmbedBuilder builder = new EmbedBuilder();

        builder.setAuthor(fullname,null,icon.toString());
        builder.setFooter(footer);
        builder.setDescription(content);

        for(Embed embed : message.getEmbeds()){
            EmbedImage image = embed.getImage();
            if(image!=null) builder.setImage(image.getUrl().toString());
        }

        for(MessageAttachment attachment : message.getAttachments()){
            URL url = attachment.getUrl();
            builder.setImage(url.toString());
        }

        String authortag = getAuthor().getMentionTag();

        getMessage().delete();
        getChannel().sendMessage(authortag+":",builder).get();
    }

    private MessageHistory getLastMessages(int limit, Channel channel, Option option, @Nullable String msgid) throws Exception{

        switch (option){
            case NONE:
                return channel.getMessageHistory(limit).get();
            case BEFORE:
                return channel.getMessageHistoryBefore(msgid,limit).get();
            case AFTER:
                return channel.getMessageHistoryAfter(msgid,limit).get();
            default:
                return channel.getMessageHistory(limit).get();
        }

    }

    private List<Message> findByUser(MessageHistory history, String id){
        List<Message> l = new ArrayList<>();
        for(Message message : history.getMessagesSorted()){
           if(message.getAuthor().getId().equals(id)) l.add(message);
        }

        return l;

    }

    private Message getLast(List<Message> l){
        if(l.size()==0) return null;


        try {
            return l.get(l.size() - 1);
        } catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    private String getLinkToMessage(Message message){
        String msgid = message.getId();
        String channelid = message.getChannelReceiver().getId();
        String serverid = getNoodle().getServer().getId();

        String longlink = "https://discordapp.com/channels/"+serverid+"/"+channelid+"?jump="+msgid;
        try {
            return GoogleURLShortening.shortenUrl(longlink);
        } catch (MalformedURLException e) {
            getChannel().sendMessage("Failed to create short URL!");
            e.printStackTrace();
            return longlink;
        }
    }

    @Override
    public String[] aliases() {
        return new String[]{"quote","q","qu","quo","~"};
    }

    @Override
    public String usage() {
        return "!quote (@MentionTag | <message-id> | ?) [#channel] [a/b<message-id>]";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "Quote a message";
    }

    @Override
    public String detailusage() {
        return "You can either quote the last message of a user, a specific message or a random message.\n\n" +
                "`!quote @MentionTag` - quote the last message of a user (Limit: "+LIMIT+" messages above)\n" +
                "`!quote <message-id>` - quote a specific message with an ID (no limit)\n" +
                "`!quote ?` - choose a random message from the last 300\n" +
                " use `a<msg-id>` or `b<msg-id>` to choose messages before or after the specified option message.\n\n" +
                "*How do I find the ID?* - Settings > Appearance > Developer Mode. Then click on `COPY ID` in the message context menu.";
    }

    private enum Option {

        NONE, BEFORE, AFTER

    }

    @Override
    public String category() {
        return "Utility";
    }
}
