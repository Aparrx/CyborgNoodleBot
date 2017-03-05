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

import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.cli.CLICommand;
import io.github.cyborgnoodle.cli.CLICommands;
import io.github.cyborgnoodle.cli.completer.ArgumentCompleter;
import io.github.cyborgnoodle.cli.completer.CLICommandCompleter;
import io.github.cyborgnoodle.misc.LogColor;
import io.github.cyborgnoodle.util.Arguments;
import io.github.cyborgnoodle.util.Log;
import io.github.cyborgnoodle.util.table.CodeTable;

/**
 * Created by arthur on 05.03.17.
 */
public class CLIHelpCommand extends CLICommand {

    public CLIHelpCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] a) throws Exception {
        Arguments args = new Arguments(a);

        if(args.has(0)){
            showCommandHelp(args.get(0));
        }
        else showFullHelp();
    }

    private void showFullHelp() {

        //           category, command
        HashMultimap<String, CLICommand> commands = HashMultimap.create();

        for (CLICommand cmd : CLICommands.getCommands().values()) {
            commands.put(cmd.category(), cmd);
        }

        String msg = LogColor.ANSI_BOLD+"CyborgNoodleBot Help"+LogColor.ANSI_RESET+"\n\n";

        msg = msg + "Use help <cmd> to show help for a specific command\n\n";
        for(String category : commands.keySet()) {

            msg = msg + LogColor.ANSI_UNDERLINE+category+LogColor.ANSI_RESET+"\n\n";

            CodeTable table = new CodeTable(30,70);

            for (CLICommand cmd : commands.get(category)) {
                table.addRow(cmd.aliases()[0],cmd.description());
            }

            msg = msg + table.asRaw()+ "\n";
        }

        Log.info(msg,true);
    }


    private void showCommandHelp(String c){

        CLICommand cmd = CLICommands.getCommands().get(c);

        if(cmd!=null){

            String msg = LogColor.ANSI_BOLD+cmd.aliases()[0]+" - Help"+LogColor.ANSI_RESET+"\n\n";

            msg = msg + cmd.description()+"\n\n";

            CodeTable table = new CodeTable(40,70);

            if(cmd.usage()!=null) table.addRow("Usage:",cmd.usage());
            else table.addRow("Usage:",cmd.aliases()[0]);

            table.addRow("Aliases:", Joiner.on(",").join(cmd.aliases()));

            msg = msg + table.asRaw()+"\n";

            if(cmd.detailusage()!=null) msg = msg + cmd.detailusage();

            Log.info(msg,true);

        }
        else Log.error("Help: Command not found: '"+c+"'");
    }

    @Override
    public String[] aliases() {
        return new String[]{"help","?"};
    }

    @Override
    public String usage() {
        return "help [command]";
    }

    @Override
    public boolean emptyHelp() {
        return false;
    }

    @Override
    public String description() {
        return "show full or command help";
    }

    @Override
    public ArgumentCompleter completer(int index) {
        if(index==0) return new CLICommandCompleter();
        else return null;
    }
}
