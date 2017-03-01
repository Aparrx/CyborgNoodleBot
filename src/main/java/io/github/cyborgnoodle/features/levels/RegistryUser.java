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

package io.github.cyborgnoodle.features.levels;

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
