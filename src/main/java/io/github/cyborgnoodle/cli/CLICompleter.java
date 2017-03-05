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

import com.google.common.base.Joiner;
import io.github.cyborgnoodle.cli.completer.ArgumentCompleter;
import io.github.cyborgnoodle.cli.completer.CLICommandCompleter;
import io.github.cyborgnoodle.util.Log;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthur on 05.03.17.
 */
public class CLICompleter implements Completer {

    private static int OVERFLOW = 8;

    @Override
    public void complete(LineReader reader, ParsedLine parsed, List<Candidate> list) {

        if(parsed.words().size()>1){

            String cmd = parsed.words().get(0);

            CLICommand command = CLICommands.getCommands().get(cmd);

            if(command!=null && parsed.wordIndex()>0){

                ArgumentCompleter completer = command.completer(parsed.wordIndex() - 1);

                if(completer!=null){
                    List<String> output = new ArrayList<>();
                    completer.complete(reader,parsed,list,output);

                    if(output.size()>1){
                        int size = output.size();
                        if(size>OVERFLOW){
                            output.subList(size - OVERFLOW, size).clear();
                            output.add("("+(size-OVERFLOW)+" more ...)");
                        }

                        Log.info(Joiner.on(" ").join(output),true);
                    }
                }

            }

        }
        else {
            if(parsed.wordIndex()==0){

                CLICommandCompleter completer = new CLICommandCompleter();

                List<String> output = new ArrayList<>();
                completer.complete(reader,parsed,list,output);

                if(output.size()>1){
                    int size = output.size();
                    if(size>OVERFLOW){
                        output.subList(size - OVERFLOW, size).clear();
                        output.add("("+(size-OVERFLOW)+" more ...)");
                    }

                    Log.info(Joiner.on(" ").join(output),true);
                }
            }
        }

    }
}
