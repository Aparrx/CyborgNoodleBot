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

import io.github.cyborgnoodle.cli.CLICommand;
import io.github.cyborgnoodle.cli.CLICommands;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

/**
 * Created by arthur on 05.03.17.
 */
public class CLICommandCompleter implements ArgumentCompleter {

    @Override
    public void complete(LineReader lineReader, ParsedLine parsed, List<Candidate> list, List<String> output) {

        CLICommand cmd = CLICommands.getCommands().get(parsed.word());

        if(cmd!=null){
            list.clear();
            list.add(new Candidate(parsed.word()));
            output.add(parsed.word());
        }
        else {
            for(String alias : CLICommands.getCommands().keySet()){
                if(alias.contains(parsed.word())){
                    list.add(new Candidate(alias));
                    output.add(alias);
                }
            }
        }
    }
}
