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
import io.github.cyborgnoodle.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by arthur on 16.10.16.
 */
public class CommandLine {

    CyborgNoodle noodle;

    public CommandLine(CyborgNoodle noods){
        this.noodle = noods;
    }

    public void listen(){
        Log.info("[CLI] Running and waiting for input");
        while (noodle.isRunning()){
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                String s = br.readLine();
                noodle.getCommands().execute(s);
            } catch (IOException e) {
                Log.error("Exception while reading commandline");
                e.printStackTrace();
            } catch (Exception e) {
                Log.error("Exception while executing command: "+e.getMessage());
                e.printStackTrace();
            }
        }
        Log.info("[CLI] Stopped");

    }

}
