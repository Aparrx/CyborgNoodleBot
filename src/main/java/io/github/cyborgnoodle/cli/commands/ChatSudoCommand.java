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

package io.github.cyborgnoodle.cli.commands;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.cli.CLICommand;
import io.github.cyborgnoodle.cli.completer.ArgumentCompleter;
import io.github.cyborgnoodle.cli.completer.ChannelNameCompleter;
import io.github.cyborgnoodle.cli.completer.UserNameCompleter;
import io.github.cyborgnoodle.util.Arguments;
import io.github.cyborgnoodle.util.Log;
import io.github.cyborgnoodle.util.discord.ChannelNameResolver;
import io.github.cyborgnoodle.util.discord.ResolverExceptions;
import io.github.cyborgnoodle.util.discord.UserNameResolver;
import io.github.cyborgnoodle.util.table.CodeTable;

/**
 * Created by arthur on 05.03.17.
 */
public class ChatSudoCommand extends CLICommand {

    public ChatSudoCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] a) throws Exception {
        Arguments args = new Arguments(a);

        if (args.has(2)) {

            String uname = args.get(0);
            User reuser;
            Channel channel;
            try {
                reuser = UserNameResolver.create(getNoodle()).forString(uname);
                if (reuser == null) {
                    Log.error("Could not find user '" + uname + "'!");
                    return;
                }
            } catch (ResolverExceptions.DiscordException e) {
                Log.error("Failed to resolve user name '" + uname + "': " + e.getMessage());
                Log.stacktrace(e);
                return;
            } catch (ResolverExceptions.UserAmbiguousException e) {
                Log.error("Ambiguous user name '" + uname + "':");
                CodeTable table = new CodeTable(40, 40);
                for (User u : e.getUsers()) {
                    table.addRow("@" + u.getName() + "#" + u.getDiscriminator(), "[" + u.getId() + "]");
                }
                Log.error(table.asRaw(), true);
                return;
            }

            String chname = args.get(1);
            try {
                Channel resch = ChannelNameResolver.create(getNoodle()).forString(chname);
                if (resch == null) {
                    Log.error("Could not find channel '" + chname + "'!");
                    return;
                } else channel = resch;
            } catch (ResolverExceptions.DiscordException e) {
                Log.error("Failed to resolve channel name '" + chname + "': " + e.getMessage());
                Log.stacktrace(e);
                return;
            } catch (ResolverExceptions.ChannelAmbiguousException e) {
                Log.error("Ambiguous channel name '" + chname + "':");
                CodeTable table = new CodeTable(20, 40);
                for (Channel ch : e.getChannels()) {
                    table.addRow("#" + ch.getName(), "[" + ch.getId() + "]");
                }
                Log.error(table.asRaw(), true);
                return;
            }


        } else showInvalidArguments();
    }

    @Override
    public String[] aliases() {
        return new String[]{"sudo", "cc", "chat"};
    }

    @Override
    public String usage() {
        return "sudo @User #channel '!command arg1 arg2'";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "Execute a chat command in the name of a specific user";
    }

    @Override
    public ArgumentCompleter completer(int index) {
        switch (index) {
            case 0:
                return new UserNameCompleter(getNoodle());
            case 1:
                return new ChannelNameCompleter(getNoodle());
            default:
                return null;
        }
    }
}
