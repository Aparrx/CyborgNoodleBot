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

import com.google.common.collect.HashMultimap;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Commands;
import io.github.cyborgnoodle.util.StringUtils;

/**
 * Created by arthur on 16.01.17.
 */
public class HelpCommand extends Command {

    public HelpCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {

        if(args.length==0){
            showFullHelp();
        }
        else {
            String cmd = args[0];
            Command command = Commands.getCommands().get(cmd);
            if(command==null) getChannel().sendMessage(
                    "Unkown command! Use `!help` without any argument to show a list of existing commands!");
            else showCommandHelp(command);
        }

    }

    private void showCommandHelp(Command cmd) throws Exception{

        EmbedBuilder e = new EmbedBuilder();

        String mainalias = cmd.aliases()[0];

        e.setTitle("!"+mainalias+" - Help");

        String desc = cmd.description()+"\n\n";

        if(cmd.usage()!=null) desc = desc + "**Usage:** `"+cmd.usage()+"`";
        else desc = desc + "**Usage:** "+"`!"+mainalias+"`";

        desc = desc + "\n";

        desc = desc + "**Aliases: **"+ StringUtils.toCommaList(cmd.aliases());

        desc = desc + "\n";

        if(cmd.fullPermission().getLevel()!=0 && cmd.fullPermission().getRole()!=null){
            desc = desc + "**Permissions: ** Requires level "+cmd.fullPermission().getLevel()+" and role "+getNoodle().getRole(cmd.fullPermission().getRole()).getName()+" or up!";
        }
        else {
            if(cmd.fullPermission().getLevel()!=0 && cmd.fullPermission().getRole()==null){
                desc = desc + "**Permissions: ** Requires level "+cmd.fullPermission().getLevel()+" or up!";
            }
            else if (cmd.fullPermission().getLevel()==0 && cmd.fullPermission().getRole()!=null){
                desc = desc + "**Permissions: ** Requires "+getNoodle().getRole(cmd.fullPermission().getRole())+" role!";
            }
        }

        desc = desc + "\n";

        e.setDescription(desc);

        e.setFooter(cmd.category());

        getChannel().sendMessage("",e);

    }

    private void showFullHelp() throws Exception{

        //           category, command
        HashMultimap<String,Command> commands = HashMultimap.create();

        for(Command cmd : Commands.getCommands().values()){
            commands.put(cmd.category(),cmd);
        }

        EmbedBuilder e = new EmbedBuilder();

        e.setTitle("Cyborg Help");

        String desc = "Use `!help <cmd>` to show help for a specific command";

        e.setDescription(desc);

        for(String category : commands.keySet()){

            String msg = "";
            for (Command cmd : commands.get(category)) {
                String usage = "";
                if(cmd.usage()!=null) usage = usage + "`"+cmd.usage()+"`";
                else usage = usage + "`!"+cmd.aliases()[0]+"`";

                msg = msg + usage + " " + cmd.description() + "\n";
            }

            e.addField(category,msg,false);

        }

        getChannel().sendMessage("",e);

    }

    @Override
    public String[] aliases() {
        return new String[]{"help","?"};
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
        return "Show general help or command help";
    }
}
