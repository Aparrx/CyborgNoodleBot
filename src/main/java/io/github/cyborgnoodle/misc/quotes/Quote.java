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

package io.github.cyborgnoodle.misc.quotes;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageAttachment;
import de.btobastian.javacord.entities.message.embed.Embed;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.message.embed.EmbedImage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by arthur on 30.01.17.
 */
public class Quote {

    Message message;

    public Quote(Message message) {
        this.message = message;
    }

    public EmbedBuilder toEmbed(){

        Calendar calendar = message.getCreationDate();
        User author = message.getAuthor();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z");
        dateFormat.setTimeZone(calendar.getTimeZone());

        String footer = dateFormat.format(calendar.getTime()) + " #"+message.getChannelReceiver().getName();

        URL icon = author.getAvatarUrl();
        String nick = author.getNickname(message.getChannelReceiver().getServer());
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

        return builder;

    }
}
