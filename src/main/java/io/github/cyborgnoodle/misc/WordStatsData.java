package io.github.cyborgnoodle.misc;

import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.Log;

import java.util.HashMap;

/**
 *
 */
public class WordStatsData {

    volatile HashMap<String,Long> map;
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
