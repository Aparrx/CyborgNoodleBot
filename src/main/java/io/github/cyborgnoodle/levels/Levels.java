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

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.Random;
import io.github.cyborgnoodle.misc.DiscordUnits;
import io.github.cyborgnoodle.misc.UnitConverter;
import io.github.cyborgnoodle.misc.Util;
import io.github.cyborgnoodle.msg.Messages;
import io.github.cyborgnoodle.msg.SystemMessages;
import io.github.cyborgnoodle.server.ServerRole;
import io.github.cyborgnoodle.server.ServerUser;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Leveling system
 */
public class Levels {

    CyborgNoodle noodle;

    private HashMap<String,Long> blocklist;

    LevelRegistry registry;

    RankCalculator calculator;

    public static int MIN_MSG_XP = 25;
    public static int MAX_MSG_XP = 85;
    public static int MIN_BOMB_XP = 300;
    public static int MAX_BOMB_XP = 5200;

    public Levels(CyborgNoodle noods){
        this.noodle = noods;
        this.blocklist = new HashMap<>();
        this.registry = new LevelRegistry();
        this.calculator = new RankCalculator();
    }

    public synchronized void onMessage(User user){
        registry.addMsg();
        String uid = user.getId();
        if(uid!=noodle.getAPI().getYourself().getId()){
            if(!blocklist.containsKey(uid)){
                gainXP(user);
                blockUser(uid);
            }
        }
    }

    public synchronized void gainXP(User usr){
        long now = System.currentTimeMillis();

        Boolean ready = registry.getGiftStamp(usr.getId())<now;

        if(registry.getNextBounty()>now){
            int pxp = Random.randInt(MIN_MSG_XP,MAX_MSG_XP);
            gainXP(usr,pxp);
        }
        else{
            if(ready){
                //1hr     //24h
                long nextbounty = now + Random.randInt(3600000,86400000);
                registry.setNextBounty(nextbounty);
                registry.setGiftTimeout(usr.getId());
                Log.info("Blocked "+usr.getName()+" for 48 hrs from xp gifts.");
                Log.info("Next random xp gift was set to "+ Util.toTimeFormat(nextbounty-now) + " in the future!");
                int pxp = Random.randInt(MIN_BOMB_XP,MAX_BOMB_XP);

                noodle.say(usr.getMentionTag()+" was lucky enough to get a random **XP gift of "+pxp+" XP**!");
                gainXP(usr,pxp);
            }
            else{
                int pxp = Random.randInt(MIN_MSG_XP,MAX_MSG_XP);
                gainXP(usr,pxp);
            }
        }
    }

    public synchronized void gainXP(User usr, int pxp){

        String uid = usr.getId();

        long xpbefore = registry.getXP(uid);

        registry.addXP(uid,pxp);

        long xpafter = registry.getXP(uid);

        int lvlbefore = LevelConverser.getLevelforXP(xpbefore);
        int lvlafter = LevelConverser.getLevelforXP(xpafter);

        ServerRole rolebefore = calculator.getRoleforLevel(lvlbefore);
        ServerRole roleafter = calculator.getRoleforLevel(lvlafter);

        Log.info(usr.getName()+" gained "+pxp+" XP, "+xpbefore+"B "+xpafter+"A XP, "+lvlbefore+"B "+lvlafter+"A Level ["+uid+"]");

        if(lvlafter>lvlbefore){
            noodle.say(usr.getMentionTag()+" advanced to **Level "+lvlafter+"**!");
            registry.setLevel(uid,lvlafter);
        }

        if(xpbefore<100000 && xpafter>100000){
            noodle.say("**Congratulations "+usr.getMentionTag()+" you reached 100 000 XP!**");
        }

        changeRoles(usr,rolebefore,roleafter);
    }

    public void changeRoles(User user, ServerRole before, ServerRole after){

        if(after==null){
            return;
        }

        if(!after.equals(before)){

            if(before!=null){

                Server srv = noodle.getAPI().getServerById("229000154936639488");

                try {
                    srv.getRoleById(before.getID()).removeUser(user).get();
                    srv.getRoleById(after.getID()).addUser(user).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                noodle.say(user.getMentionTag()+" is now a **"+srv.getRoleById(after.getID()).getName()+"**!");
            }

        }
    }

    public LinkedHashMap<String,Long> getLeaderboard(){
        HashMap<String,Long> dat = new HashMap<>();
        for(User user : noodle.getAPI().getUsers()){
            long xp = registry.getXP(user.getId());
            int level = registry.getLevel(user.getId());

            if(level>0){
                dat.put(user.getId(),xp);
            }




        }

        return sortByValue(dat);
    }

    public static <String, Long extends Comparable<? super Long>> LinkedHashMap<String, Long> sortByValue(Map<String, Long> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
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

    public LevelRegistry getRegistry(){
        return registry;
    }

    public RankCalculator getCalculator(){
        return calculator;
    }

    public void setRegistry(LevelRegistry reg){
        this.registry = reg;
    }
}
