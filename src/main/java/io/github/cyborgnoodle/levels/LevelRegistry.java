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

import io.github.cyborgnoodle.CyborgNoodle;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by arthur on 16.10.16.
 */
public class LevelRegistry implements Serializable{

    HashMap<String,RegistryUser> registry;

    long nextbounty;

    long msgs;

    volatile long TIMEOUT = 172800000l;

    public LevelRegistry(){
        registry = new HashMap<String,RegistryUser>();
        nextbounty = 0;
        msgs = 200000;
    }

    public long getNextBounty(){
        return nextbounty;
    }

    public void setNextBounty(long nb){
        this.nextbounty = nb;
    }

    public void setXP(String uid, long xp){
        getUser(uid).setXP(xp);
    }

    public void addXP(String uid, int xp){
        long nxp = getUser(uid).getXP()+xp;
        getUser(uid).setXP(nxp);
    }

    public long getXP(String uid){
        return getUser(uid).getXP();
    }

    public int getLevel(String uid){
        return getUser(uid).getLevel();
    }

    public void setLevel(String uid, int lvl){
        getUser(uid).setLevel(lvl);
    }

    public void setGiftTimeout(String uid){
        getUser(uid).setGiftTimeout(TIMEOUT);
    }

    public long getGiftStamp(String uid){
        return getUser(uid).getGiftStamp();
    }

    public void addMsg(){
        msgs++;
    }

    public long getMsgs(){
        return msgs;
    }

    public RegistryUser getUser(String uid){
        if(registry.containsKey(uid)){
            return registry.get(uid);
        }
        else{
            registry.put(uid,new RegistryUser(uid));
            return registry.get(uid);
        }
    }



}
