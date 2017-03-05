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
import de.btobastian.javacord.entities.User;

import java.util.Collection;

/**
 * Created by arthur on 05.03.17.
 */
public class ResolverExceptions {

    public static class AmbiguousException extends Exception {
        public AmbiguousException(String message) {
            super(message);
        }
    }

    public static class UserAmbiguousException extends AmbiguousException {

        private Collection<User> users;

        public UserAmbiguousException(String search, Collection<User> users) {
            super("Ambiguous user name '"+search+"'");
            this.users = users;
        }

        public Collection<User> getUsers() {
            return users;
        }
    }

    public static class ChannelAmbiguousException extends AmbiguousException {

        private Collection<Channel> users;

        public ChannelAmbiguousException(String search, Collection<Channel> users) {
            super("Ambiguous channel name '"+search+"'");
            this.users = users;
        }

        public Collection<Channel> getChannels() {
            return users;
        }
    }

    public static class DiscordException extends Exception {
        public DiscordException(String message) {
            super(message);
        }

        public DiscordException(Throwable cause) {
            super(cause);
        }

        public DiscordException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
