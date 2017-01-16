package io.github.cyborgnoodle.levels;

/**
 * Calculate Levels / XP
 */
public class LevelConverser {

    public static long getXPforLevel(int lvl){

        long p = 0;
        int i = lvl;
        while(i>0){
            p = p + getXPforNextLevel(i);
            i--;
        }

        return p;
    }

    public static long getXPforNextLevel(int lvl){
        double xp = 5 * (Math.pow(lvl,2)+(8*lvl)+11);

        return (long) xp;
    }

    public static int getLevelforXP(long xp){

        int lvl = 0;

        long p = 0;

        Boolean run = true;

        while(run){
            p = getXPforLevel(lvl);
            if(p<=xp){
                lvl++;
                continue;
            }
            else{
                lvl = lvl - 1;
                break;
            }
        }

        return lvl;

    }

}
