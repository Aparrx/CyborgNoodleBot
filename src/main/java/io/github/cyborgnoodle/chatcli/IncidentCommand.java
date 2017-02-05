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

package io.github.cyborgnoodle.chatcli;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Main;
import io.github.cyborgnoodle.Random;
import io.github.cyborgnoodle.util.NumberEmoji;
import io.github.cyborgnoodle.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by arthur on 31.01.17.
 */
public class IncidentCommand extends Command {

    public IncidentCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {

        long l = System.currentTimeMillis() - Main.START_STAMP;
        String read = StringUtils.asReadableTime(l);
        String res = NumberEmoji.numsToEmoji(read);

        Collection<String> c = new ArrayList<>();
        c.add("crash");
        c.add("incident");
        c.add("catastrophe");
        c.add("unfortunate event");
        String name = Random.choose(c);

        String txt = "`DAYS SINCE THE LAST "+name.toUpperCase()+":`\n"+res;

        getChannel().sendMessage(txt);
    }

    @Override
    public String[] aliases() {
        return new String[]{"inc","i"};
    }

    @Override
    public String usage() {
        return "!inc";
    }

    @Override
    public boolean emptyHelp() {
        return false;
    }

    @Override
    public String description() {
        return "show time since last incident";
    }
}
