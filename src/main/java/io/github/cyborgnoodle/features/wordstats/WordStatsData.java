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

package io.github.cyborgnoodle.features.wordstats;

import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.util.Log;

import java.util.HashMap;

/**
 *
 */
public class WordStatsData {

    volatile HashMap<String,Long> map; //this is the old data
    volatile HashMap<String,WordStatsEntry> entries;

    public WordStatsData(){
        this.map = new HashMap<>();
        this.entries = new HashMap<>();
    }

    public synchronized void count(String word, long n, User user){

        if(entries==null || entries.size()==0) copyOld();

        if(entries.containsKey(word)){
            entries.get(word).count(n,user);
        }
        else entries.put(word,new WordStatsEntry(n));
    }

    public synchronized HashMap<String, WordStatsEntry> getEntries() {
        return entries;
    }

    public synchronized HashMap<String, Long> getMap() {
        return map;
    }

    public synchronized void remove(String word){
        map.remove(word);
    }

    private synchronized void copyOld(){
        Log.info("Copying old word stats data to new format ... "+map.size()+" Words");
        for(String word : map.keySet()){
            Long count = map.get(word);
            WordStatsEntry entry = new WordStatsEntry(count);
            entries.put(word,entry);
        }
        Log.info("Finished Copying! Set the old data to EMPTY!");
        map.clear();
    }
}
