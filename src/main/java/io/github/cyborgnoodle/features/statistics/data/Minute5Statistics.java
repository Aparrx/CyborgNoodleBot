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

package io.github.cyborgnoodle.features.statistics.data;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arthur on 26.01.17.
 */
public class Minute5Statistics {

    private long msgcount;
    private Map<String,Long> peruser;
    private Map<String,Long> perchannel;
    private int usercount = 0;
    private long allmsgcount = 339974L;
    private int onlinecount = 0;
    private Collection<String> onlineusers = new ArrayList<>();
    private long allxp;
    private Map<String,Long> userxp;

    public Minute5Statistics(){
        msgcount = 0;
        peruser = new HashMap<>();
        perchannel = new HashMap<>();
        this.allmsgcount = 339974L;
        this.usercount = 0;
        this.onlinecount = 0;
        this.onlineusers  = new ArrayList<>();
        this.allxp = 0;
        this.userxp = new HashMap<>();
    }

    public Collection<String> getOnlineusers() {
        return onlineusers;
    }

    public void setOnlineusers(Collection<String> onlineusers) {
        this.onlineusers = onlineusers;
    }

    public long getMessageCount(){
        return msgcount;
    }

    public long getAllMessageCount() {
        return allmsgcount;
    }

    public int getUserCount() {
        return usercount;
    }

    public double getSpeed(){
        return (double) msgcount / 5;
    }

    public Map<String, Long> getPerUser() {
        return peruser;
    }

    public void setUserCount(int usercount) {
        this.usercount = usercount;
    }

    public void setAllMessageCount(long allmsgcount) {
        this.allmsgcount = allmsgcount;
    }

    public Map<String, Long> getPerChannel() {
        return perchannel;
    }

    public int getOnlineCount() {
        return onlinecount;
    }

    public void setOnlineCount(int onlinecount) {
        this.onlinecount = onlinecount;
    }

    public long getAllxp() {
        return allxp;
    }

    public void setAllxp(long allxp) {
        this.allxp = allxp;
    }

    public Map<String, Long> getUserxp() {
        return userxp;
    }

    public void count(@Nullable String user, @Nullable String channel, int amount){
        this.msgcount = this.msgcount + amount;

        if(user!=null){
            if(peruser.containsKey(user)){
                long l = peruser.get(user);
                l = l + amount;
                peruser.put(user,l);
            }
            else peruser.put(user,1L);
        }

        if(channel!=null) {
            if(perchannel.containsKey(channel)){
                long l = perchannel.get(channel);
                l = l + amount;
                perchannel.put(channel,l);
            }
            else perchannel.put(channel,1L);
        }
    }
}
