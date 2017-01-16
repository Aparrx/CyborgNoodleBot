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

package io.github.cyborgnoodle;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;

/**
 * Created by arthur on 04.11.16
 */
public class ErrorNotifier {

    String USER = "145575908881727488";

    String MARKER = "```";

    CyborgNoodle noodle;

    public ErrorNotifier(CyborgNoodle noodle){
        this.noodle = noodle;
    }

    public void notifyError(Exception e){

        String emsg = e.getMessage();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String trace = sw.toString();

        String msg = emsg+"\n\n"+
                MARKER+"\n"+trace+"\n"+MARKER;

        try {
            noodle.getAPI().getUserById(USER).get().sendMessage(msg);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }


    }

}
