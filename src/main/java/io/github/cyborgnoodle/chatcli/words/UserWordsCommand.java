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

package io.github.cyborgnoodle.chatcli.words;

import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.misc.WordStatsEntry;
import io.github.cyborgnoodle.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arthur on 16.01.17.
 */
public class UserWordsCommand extends Command {

    public UserWordsCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {

        String mentiontag = args[0];

        User user = null;
        for(User u : getNoodle().getAPI().getUsers()){
            if(u.getMentionTag().equalsIgnoreCase(mentiontag)) user = u;
        }

        if(user==null){
            Log.info("Can't find user!");
            return;
        }

        HashMap<String,Long> toplist = new HashMap<>();
        for(String word : getNoodle().getWordStats().getData().getEntries().keySet()){
            WordStatsEntry entry = getNoodle().getWordStats().getData().getEntries().get(word);
            if(entry.getUsers().containsKey(user.getId())){
                Long wordcount = entry.getUsers().get(user.getId());
                toplist.put(word,wordcount);
            }
        }

        String name = user.getNickname(getNoodle().getServer());
        if(name==null) name = user.getName();
        if(name==null) name = "UNKNOWN NAME";

        String msg = "Words for "+name+"```\n";
        List<String> listentries = new ArrayList<>();

        toplist.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(20).forEach(entry -> {
            String word = entry.getKey();
            Long cl = entry.getValue();
            String count = cl.toString();
            word = StringUtils.ellipsize(word,19);
            String wordspace = StringUtils.getWhitespaces(20-word.length());
            listentries.add(word+wordspace+count);
        });

        for(String entry : listentries){
            msg = msg + entry + "\n";
        }

        msg = msg + "\n```";

        getChannel().sendMessage(msg);
    }

    @Override
    public String[] aliases() {
        return new String[]{"userwords","uwords"};
    }

    @Override
    public String usage() {
        return "!userwords @MentionTag";
    }

    @Override
    public boolean emptyHelp() {
        return true;
    }

    @Override
    public String description() {
        return "show word toplist per user";
    }

    @Override
    public String category() {
        return "Word commands";
    }
}
