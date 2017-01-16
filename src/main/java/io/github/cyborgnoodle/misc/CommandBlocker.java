package io.github.cyborgnoodle.misc;

import de.btobastian.javacord.entities.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by arthur on 15.01.17.
 */
public class CommandBlocker {
    private static Set<String> blocked = new HashSet<>();

    public static void block(User u){
        blocked.add(u.getId());
    }

    public static boolean isBlocked(User user){
        return blocked.contains(user.getId());
    }

    public static void unblock(User user){
        blocked.remove(user.getId());
    }
}
