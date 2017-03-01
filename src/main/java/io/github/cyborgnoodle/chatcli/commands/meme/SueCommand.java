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

package io.github.cyborgnoodle.chatcli.commands.meme;

import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.features.funtance.data.ObjectData;
import io.github.cyborgnoodle.features.funtance.data.OrtData;
import io.github.cyborgnoodle.features.funtance.data.VerbData;
import io.github.cyborgnoodle.util.Random;

/**
 * Created by arthur on 17.01.17.
 */
public class SueCommand extends Command {

    public SueCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {

        String mention = args[0];
        String id = mention.replace("<@", "").replace(">", "");
        User user = getNoodle().api.getCachedUserById(id);

        if(user==null){
            getChannel().sendMessage("User not found!");
            return;
        }

        String verb = Random.choose(VerbData.getData());
        String objects = Random.choose(ObjectData.getData());
        String place = Random.choose(OrtData.getData());

        getChannel().sendMessage(getAuthor().getMentionTag()+" sued "+user.getMentionTag()+" because he / she "+verb+" "+objects+" "+place+"!");
    }

    @Override
    public String[] aliases() {
        return new String[]{"sue"};
    }

    @Override
    public String usage() {
        return "!sue @MentionTag";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "sue someone";
    }

    @Override
    public String category() {
        return "Meme commands";
    }

    @Override
    public boolean hidden() {
        return true;
    }

}
