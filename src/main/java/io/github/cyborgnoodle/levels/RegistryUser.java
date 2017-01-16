package io.github.cyborgnoodle.levels;

import java.io.Serializable;

/**
 *
 */
public class RegistryUser implements Serializable{

    long xp;
    int level;
    String uid;

    long gifttimeout;

    public RegistryUser(long xp, int level, String uid, long gifttimeout) {
        this.xp = xp;
        this.level = level;
        this.uid = uid;
        this.gifttimeout = gifttimeout;
    }

    public RegistryUser(String uid) {
        this.xp = 0;
        this.level = 0;
        this.uid = uid;
        this.gifttimeout = 0;
    }

    public long getXP() {
        return xp;
    }

    public void setXP(long xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getID() {
        return uid;
    }

    public void setGiftTimeout(long timeout){
        this.gifttimeout = System.currentTimeMillis()+timeout;
    }

    public void setGiftStamp(long stamp){
        this.gifttimeout = stamp;
    }

    public long getGiftStamp(){
        return this.gifttimeout;
    }
}
