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

package io.github.cyborgnoodle.cli.completer;

import de.btobastian.javacord.entities.Channel;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.util.discord.ChannelNameResolver;
import io.github.cyborgnoodle.util.discord.ResolverExceptions;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

/**
 * Created by arthur on 05.03.17.
 */
public class ChannelNameCompleter implements ArgumentCompleter {

    private CyborgNoodle noodle;
    private ChannelNameResolver resolver;

    public ChannelNameCompleter(CyborgNoodle noodle){
        this.noodle = noodle;
        this.resolver = ChannelNameResolver.create(noodle);
    }

    @Override
    public void complete(LineReader reader, ParsedLine parsed, List<Candidate> list, List<String> output) {

        try {
            Channel channel = resolver.forString(parsed.word());
            if(channel==null) list.clear();
            else{
                list.add(new Candidate("#"+channel.getName()));
                output.add("#"+channel.getName());
            }
        } catch (ResolverExceptions.DiscordException e) {
            list.clear();
        } catch (ResolverExceptions.ChannelAmbiguousException e) {
            for(Channel ch: e.getChannels()){
                list.add(new Candidate("#"+ch.getName()));
                output.add("#"+ch.getName());
            }
        }

    }
}
