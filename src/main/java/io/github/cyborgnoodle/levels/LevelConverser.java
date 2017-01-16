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
