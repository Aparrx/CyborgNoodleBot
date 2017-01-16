package io.github.cyborgnoodle.news;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by arthur on 29.10.16.
 */
public class InstagramRegistry {

    private Set<String> seen;

    public InstagramRegistry(){
        seen = new HashSet<>();
    }

    public synchronized void setSeen(String id){
        seen.add(id);
    }

    public synchronized Boolean isSeen(String id){
        return seen.contains(id);
    }

}
