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

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Meta;
import io.github.cyborgnoodle.chatcli.Command;

/**
 * Created by arthur on 16.01.17.
 */
public class VersionCommand extends Command {

    public VersionCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) {
        if(args.length==1) getChannel().sendMessage("**"+ Meta.getVersion()+"** Changelog:\n"+Meta.getChangelog());
        else getChannel().sendMessage("**"+ Meta.getVersion()+"** Changelog:\n"+Meta.getChangelog(),Meta.getEmbed());
    }

    @Override
    public String[] aliases() {
        return new String[]{"version","ver"};
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public boolean emptyHelp() {
        return false;
    }

    @Override
    public String description() {
        return "show bot version";
    }
}
