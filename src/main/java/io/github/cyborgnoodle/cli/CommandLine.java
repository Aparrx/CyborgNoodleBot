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

package io.github.cyborgnoodle.cli;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.util.Log;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;

/**
 * Created by arthur on 16.10.16.
 */
public class CommandLine {

    public static final Log.LogContext context = new Log.LogContext("CLI");

    CyborgNoodle noodle;

    private Terminal terminal;
    private LineReader reader;

    private boolean running;

    public CommandLine(CyborgNoodle noods){
        this.noodle = noods;
        setUp();
    }

    private void setUp(){
        try {
            terminal = TerminalBuilder.terminal();
            reader = LineReaderBuilder.builder()
                    .completer(new CLICompleter())
                    .terminal(terminal)
                    .build();

            reader.unsetOpt(LineReader.Option.MENU_COMPLETE);
            reader.unsetOpt(LineReader.Option.AUTO_MENU);
            reader.unsetOpt(LineReader.Option.AUTO_LIST);

            Log.Logger logger = new Log.StandardLogger(){
                @Override
                public void logNormal(String msg) {
                    before();
                    terminal.writer().println(msg);
                    after();
                }

                @Override
                public void logError(String msg) {
                    before();
                    terminal.writer().println(msg);
                    after();
                }

                private void before(){
                    reader.getTerminal().puts(InfoCmp.Capability.carriage_return);
                }

                private void after(){


                    try {
                        reader.callWidget(LineReader.REDRAW_LINE);
                        reader.callWidget(LineReader.REDISPLAY);
                    } catch (Exception e) {
                        // useless runtime exception when not "reading"
                    }

                    reader.getTerminal().writer().flush();
                }
            };

            Log.setLogger(logger);



        } catch (IOException e) {
            Log.error("Failed to create the console reader / setup the output!",context);
            Log.stacktrace(e,context);
        }
    }

    private void disable(){
        Log.setLogger(new Log.StandardLogger());
    }


    public void listen(){
        Log.info("Running and waiting for input",context);

        while (noodle.isRunning()){
            this.running = true;

                String s = reader.readLine(">");

                if(!s.isEmpty()){
                    try {
                        CLICommands.execute(s);
                        if(!noodle.isRunning()) break;
                    } catch (Exception e) {
                        Log.error("Exception while executing command!",context);
                        Log.stacktrace(e);
                    }
                }

//            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//            try {
//                String s = br.readLine();
//                noodle.getCommands().execute(s);
//            } catch (IOException e) {
//                Log.error("Exception while reading commandline");
//                Log.stacktrace(e);
//            } catch (Exception e) {
//                Log.error("Exception while executing command: "+e.getMessage());
//                Log.stacktrace(e);
//            }
        }
        this.running = false;
        disable();
        Log.info("Stopped",context);

    }

}
