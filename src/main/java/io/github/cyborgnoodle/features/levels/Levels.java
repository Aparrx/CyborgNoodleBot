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

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.misc.Util;
import io.github.cyborgnoodle.settings.data.ServerChannel;
import io.github.cyborgnoodle.settings.data.ServerRole;
import io.github.cyborgnoodle.util.Log;
import io.github.cyborgnoodle.util.Random;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Leveling system
 */
public class Levels {

    public static final Log.LogContext context = new Log.LogContext("XP");

    CyborgNoodle noodle;

    private HashMap<String,Long> blocklist;

    TempRegistry registry;

    public Levels(CyborgNoodle noods){
        this.noodle = noods;
        this.blocklist = new HashMap<>();
        this.registry = new TempRegistry(noods);
    }

    public synchronized void onMessage(User user){
        String uid = user.getId();
        if(!Objects.equals(uid, noodle.api.getYourself().getId())){
            if(!blocklist.containsKey(uid)){
                gainXP(user);
                blockUser(uid);
            }
        }
    }

    public synchronized void gainXP(User usr){
        long now = System.currentTimeMillis();

        Boolean ready = registry.get(usr).getGiftTimeout()<now;

        if(registry.getNextBounty()>now){
            int pxp = Random.randInt(noodle.settings.xp.msg_min.get(), noodle.settings.xp.msg_max.get());
            gainXP(usr,pxp);
        }
        else{
            if(ready){
                long nextbounty = now + Random.randInt(noodle.settings.xp.bomb_timeout_min.get(), noodle.settings.xp.bomb_timeout_max.get())*60*1000;
                registry.setNextBounty(nextbounty);
                registry.get(usr).setGiftTimeout(LevelRegistry.TIMEOUT);
                //Log.info("Blocked "+usr.getName()+" for 48 hrs from xp gifts.",context);
                Log.info("Next random xp gift was set to "+ Util.toTimeFormat(nextbounty-now) + " in the future!",context);
                int pxp = Random.randInt(noodle.settings.xp.bomb_min.get(),noodle.settings.xp.bomb_max.get());

                noodle.getChannel(ServerChannel.GENERAL).sendMessage(usr.getMentionTag()+" was lucky enough to get a random **XP gift of "+pxp+" XP**!");
                gainXP(usr,pxp);
            }
            else{
                int pxp = Random.randInt(noodle.settings.xp.msg_min.get(), noodle.settings.xp.msg_max.get());
                gainXP(usr,pxp);
            }
        }
    }

    public synchronized void gainXP(User usr, int pxp){

        registry.get(usr).addXp(pxp);

        //changeRoles(usr,rolebefore,roleafter);
    }

    @Deprecated
    public void changeRolesXXXX(User user, ServerRole before, ServerRole after){

        if(after==null){
            return;
        }

        if(!after.equals(before)){

            if(before!=null){

                Server srv = noodle.getServer();

                try {
                    srv.getRoleById(before.getID()).removeUser(user).get();
                    srv.getRoleById(after.getID()).addUser(user).get();
                } catch (InterruptedException | ExecutionException e) {
                    Log.stacktrace(e);
                }

                noodle.getChannel(ServerChannel.GENERAL).sendMessage(user.getMentionTag()+" is now a **"+srv.getRoleById(after.getID()).getName()+"**!");
            }

        }
    }

    public LinkedHashMap<String,TempUser> getLeaderboard(){
        HashMap<String,TempUser> dat = new HashMap<>();
        for(User user : noodle.api.getUsers()){

            TempUser t = registry.get(user);

            if(t.getLevel()>0){
                dat.put(user.getId(),t);
            }

        }

        return sortByValue(dat);
    }

    public static LinkedHashMap<String, TempUser> sortByValue(Map<String, TempUser> map) {
        return map.entrySet()
                .stream()
                .sorted((o1, o2) -> {
                    Long l1 = o1.getValue().getXp();
                    Long l2 = o2.getValue().getXp();

                    return l2.compareTo(l1);
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public synchronized void blockUser(String uid){
        blocklist.put(uid,System.currentTimeMillis());
    }

    public synchronized void unblockUser(String uid){
        blocklist.remove(uid);
    }

    public synchronized HashMap<String,Long> getBlocklist(){
        return blocklist;
    }

    public void doUnblock(){

        HashMap<String,Long> cpl = new HashMap<>(blocklist);

        for(String uid : cpl.keySet()) {
            Long stamp = cpl.get(uid);

            if ((System.currentTimeMillis() - stamp) > 60000) {
                unblockUser(uid);
            }
        }
    }

    public LevelRegistry toLevelRegistry(){
        return registry.toRegistry();
    }

    public TempRegistry registry(){
        return registry;
    }

    public void setRegistry(LevelRegistry reg){
        this.registry = new TempRegistry(noodle,reg);
    }
}
