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

import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.util.Log;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by arthur on 24.01.17.
 */
public class TempRegistry {

    private CyborgNoodle noodle;
    private HashMap<String,TempUser> users;

    private long nextbounty;

    public TempRegistry(CyborgNoodle noodle, LevelRegistry registry){
        this.noodle = noodle;
        this.users = new HashMap<>();

        this.nextbounty = registry.nextbounty;

        for(String uid : registry.registry.keySet()){

            User user;

            try {
               user = noodle.api.getUserById(uid).get();
            } catch (InterruptedException | ExecutionException ignored) {
                Log.error("Failed to get user by ID "+uid);
                break;
            }

            TempUser temp = new TempUser(
                    this,user,
                    registry.getXP(uid),
                    registry.getLevel(uid),
                    registry.getGiftStamp(uid)
            );

            users.put(uid,temp);

        }
    }

    public TempRegistry(CyborgNoodle noodle){
        this.noodle = noodle;
        this.users = new HashMap<>();
        this.nextbounty = System.currentTimeMillis()+200000L;
    }

    public TempUser get(User user){
        if(users.containsKey(user.getId())){
            return users.get(user.getId());
        }
        else{
            users.put(user.getId(), new TempUser(this,user));
            return users.get(user.getId());
        }
    }

    public long getNextBounty() {
        return nextbounty;
    }

    public void setNextBounty(long nextbounty) {
        this.nextbounty = nextbounty;
    }

    public LevelRegistry toRegistry(){

        LevelRegistry reg = new LevelRegistry();

        reg.setNextBounty(nextbounty);

        for(String uid : users.keySet()){

            TempUser temp = users.get(uid);

            RegistryUser user = new RegistryUser(temp.getXp(),temp.getLevel(),uid,temp.getGiftTimeout());

            reg.registry.put(uid,user);

        }

        return reg;
    }

    public CyborgNoodle getNoodle() {
        return noodle;
    }
}
