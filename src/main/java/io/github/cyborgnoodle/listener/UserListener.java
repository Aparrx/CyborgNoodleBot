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

package io.github.cyborgnoodle.listener;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.listener.server.ServerMemberAddListener;
import de.btobastian.javacord.listener.server.ServerMemberBanListener;
import de.btobastian.javacord.listener.server.ServerMemberRemoveListener;
import de.btobastian.javacord.listener.server.ServerMemberUnbanListener;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.msg.SystemMessages;
import io.github.cyborgnoodle.server.ServerChannel;

import java.text.MessageFormat;

/**
 * Created by arthur on 14.01.17.
 */
public class UserListener implements ServerMemberBanListener, ServerMemberUnbanListener, ServerMemberAddListener, ServerMemberRemoveListener {

    CyborgNoodle noodle;

    public UserListener(CyborgNoodle noodle) {
        this.noodle = noodle;
    }

    @Override
    public void onServerMemberAdd(DiscordAPI discordAPI, User user, Server server) {
        Log.info(" NEW USER > "+user.getName());
        noodle.getChannel(ServerChannel.GENERAL).sendMessage(MessageFormat.format(SystemMessages.getWelcome(),user.getName()));
    }

    @Override
    public void onServerMemberBan(DiscordAPI discordAPI, User user, Server server) {
        Log.info(" BANNED USER > "+user.getName());
        noodle.getChannel(ServerChannel.GENERAL).sendMessage(MessageFormat.format(SystemMessages.getBanned(),user.getName()));
    }

    @Override
    public void onServerMemberRemove(DiscordAPI discordAPI, User user, Server server) {
        Log.info(" REMOVED USER > "+user.getName());
        noodle.getChannel(ServerChannel.GENERAL).sendMessage(MessageFormat.format(SystemMessages.getLeave(),user.getName()));
    }

    @Override
    public void onServerMemberUnban(DiscordAPI discordAPI, String s, Server server) {
        // Nothing (tm)
    }
}
