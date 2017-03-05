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

import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.util.Arguments;
import io.github.cyborgnoodle.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class UserNameResolver {

    private CyborgNoodle noodle;

    public static UserNameResolver create(CyborgNoodle noodle){
       return new UserNameResolver(noodle);
    }

    private UserNameResolver(CyborgNoodle noodle){
        this.noodle = noodle;
    }

    public User forName(String name) throws ResolverExceptions.UserAmbiguousException {
        name = name.replace("@","");
        List<User> users = new ArrayList<>();
        for(User user : noodle.api.getUsers()) {

            String nick = user.getNickname(noodle.getServer());
            boolean containsnick;
            containsnick = nick != null && nick.contains(name);

            if (user.getName().contains(name) || containsnick) {
                users.add(user);
            }

        }
        if(users.size()>1) throw new ResolverExceptions.UserAmbiguousException(name,users);
        else if(users.size()!=1) return null;
        else return users.get(0);
    }

    public User forWithDiscriminator(String name) throws ResolverExceptions.UserAmbiguousException {
        List<User> users = new ArrayList<>();
        if (name.contains("#")) {

            Arguments parts = new Arguments(name.split("#"));

            if(parts.has(0) && parts.has(1)){
                String uname = parts.get(0).replace("@","");
                String disc = parts.get(1);

                for(User user : noodle.api.getUsers()){

                    String nick = user.getNickname(noodle.getServer());
                    boolean containsnick;
                    containsnick = nick != null && nick.contains(uname);

                    if (user.getName().contains(uname) || containsnick) {

                        if(user.getDiscriminator().contains(disc)) users.add(user);
                        else continue;

                        if(users.size()>1) throw new ResolverExceptions.UserAmbiguousException(name,users);
                        else if(users.size()!=1) return null;
                        else return users.get(0);
                    }
                }

                return null;
            }
            else return null;
        }
        else return null;
    }

    public User forID(String id) throws ResolverExceptions.DiscordException {
        try {
            return noodle.api.getUserById(id).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ResolverExceptions.DiscordException(e);
        }
    }

    public User forMentionTag(String tag) throws ResolverExceptions.DiscordException {
        User user = null;
        if(tag.contains("<@")){
            String chid = tag.replace("<@","").replace(">","");
            User xc = null;
            try {
                xc = noodle.api.getUserById(chid).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new ResolverExceptions.DiscordException(e);
            }
            if(xc!=null) user = xc;
        }
        return user;
    }

    public User forString(String string) throws ResolverExceptions.DiscordException, ResolverExceptions.UserAmbiguousException {

        if(StringUtils.isNumeric(string)) return forID(string);
        if(string.contains("<@")) return forMentionTag(string);
        if(string.contains("#")) return forWithDiscriminator(string);
        return forName(string);
    }


}
