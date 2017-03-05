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

import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.util.discord.ResolverExceptions;
import io.github.cyborgnoodle.util.discord.UserNameResolver;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

/**
 * Created by arthur on 05.03.17.
 */
public class UserNameCompleter implements ArgumentCompleter {

    private CyborgNoodle noodle;
    private UserNameResolver resolver;

    public UserNameCompleter(CyborgNoodle noodle){
        this.noodle = noodle;
        this.resolver = UserNameResolver.create(noodle);
    }

    @Override
    public void complete(LineReader reader, ParsedLine parsed, List<Candidate> list, List<String> output) {
        try {
            User user = resolver.forString(parsed.word());
            if(user==null) list.clear();
            else{
                list.add(new Candidate("@"+user.getName()+"#"+user.getDiscriminator()));
                output.add("@"+user.getName()+"#"+user.getDiscriminator());
            }
        } catch (ResolverExceptions.DiscordException e) {
            list.clear();
        } catch (ResolverExceptions.UserAmbiguousException e) {
            for(User user: e.getUsers()){
                list.add(new Candidate("@"+user.getName()+"#"+user.getDiscriminator()));
                output.add("@"+user.getName()+"#"+user.getDiscriminator());
            }
        }
    }
}
