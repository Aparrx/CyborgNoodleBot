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

package io.github.cyborgnoodle.util.discord;

import de.btobastian.javacord.entities.Channel;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthur on 05.03.17.
 */
public class ChannelNameResolver {

    private CyborgNoodle noodle;

    public static ChannelNameResolver create(CyborgNoodle noodle){
        return new ChannelNameResolver(noodle);
    }

    private ChannelNameResolver(CyborgNoodle noodle){
        this.noodle = noodle;
    }

    public Channel forName(String name) throws ResolverExceptions.ChannelAmbiguousException {
        name = name.replace("#","");
        List<Channel> channels = new ArrayList<>();
        for(Channel channel : noodle.api.getChannels()){
            if(channel.getName().contains(name)){
                channels.add(channel);
            }

        }
        if(channels.size()>1) throw new ResolverExceptions.ChannelAmbiguousException(name,channels);
        else if(channels.size()!=1) return null;
        else return channels.get(0);
    }

    public Channel forID(String id) {
        return noodle.api.getChannelById(id);
    }

    public Channel forMentionTag(String tag) throws ResolverExceptions.DiscordException {
        Channel channel = null;
        if(tag.contains("<#")){
            String chid = tag.replace("<#","").replace(">","");
            Channel xc;
            xc = forID(chid);
            if(xc!=null) channel = xc;
        }
        return channel;
    }

    public Channel forString(String string) throws ResolverExceptions.DiscordException, ResolverExceptions.ChannelAmbiguousException {

        if(StringUtils.isNumeric(string)) return forID(string);
        if(string.contains("<#")) return forMentionTag(string);

        return forName(string);
    }

}
