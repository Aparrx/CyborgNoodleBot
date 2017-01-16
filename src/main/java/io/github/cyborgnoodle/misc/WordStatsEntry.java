package io.github.cyborgnoodle.misc;

import de.btobastian.javacord.entities.User;

import java.util.HashMap;

/**
 *
 */
public class WordStatsEntry {

    volatile Long count;
    volatile HashMap<String,Long> users;

    public WordStatsEntry(){
        count = 0L;
        users = new HashMap<>();
    }

    public WordStatsEntry(Long precounted){
        count = precounted;
        users = new HashMap<>();
    }

    public HashMap<String, Long> getUsers() {
        return users;
    }

    public Long getCount() {
        return count;
    }

    public void count(Long c){
        count = count + c;
    }

    public void count(Long c, User user){
        count(c);
        String id = user.getId();
        if(!users.containsKey(id)){
            users.put(id,c);
        }
        else {
            Long aLong = users.get(id);
            long counted = aLong + c;
            users.put(id,counted);
        }
    }
}
