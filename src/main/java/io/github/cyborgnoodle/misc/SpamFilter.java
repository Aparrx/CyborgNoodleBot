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

package io.github.cyborgnoodle.misc;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.msg.ConversationMessages;
import io.github.cyborgnoodle.settings.data.ServerRole;
import io.github.cyborgnoodle.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class SpamFilter {

    private CyborgNoodle noodle;

    private Map<String,SpamData> lastdata;
    private Map<String,Long> timeouts;
    private Map<String,Integer> counter;
    private Map<String,Long> spammed;

    private static int MAX_WORD_REPEATS = 10;
    private static long MSG_TIMEOUT_MILLIS = 750;
    private static int MAX_CHAR_REPEATS = 5;
    private static long SPAM_TIMEOUT_CRITERIA_MILLIS = 30000;
    private static int SPAM_TIMEOUT_CRITERIA_COUNT_MAX = 5;
    private static long TIMEOUT = 60000*10;
    private static boolean IGNORE_REGULARS = true;

    public SpamFilter(CyborgNoodle noodle){
        this.noodle = noodle;
        lastdata = new HashMap<>();
        timeouts = new HashMap<>();
        counter = new HashMap<>();
        spammed = new HashMap<>();
    }

    private boolean checkSpam(Message message){

        String content = message.getContent();
        User user = message.getAuthor();

        if(IGNORE_REGULARS){
            if(user.getRoles(noodle.getServer()).contains(noodle.getRole(ServerRole.FOUNDER))){
                return false;
            }
        }

        SpamData data = lastdata.get(user.getId());

        if(data!=null){
            if(!data.isOutdated()){
                return true;
            }
        }
        else data = new SpamData();

        data.setLastMsg(content);
        data.setLastStamp(System.currentTimeMillis());

        lastdata.put(user.getId(),data);

        //check if repeative
        if(content.matches(".*(\\w)\\1{"+MAX_CHAR_REPEATS+",}+.*")) return true;

        String[] words = content.split(" ");

        Map<String,Long> counts = new HashMap<>();

        //Count word occurances
        for(String word : words){
            String lwo = BadWords.adjustMsg(word.toLowerCase());
            Long num = counts.get(lwo);
            if(num==null) counts.put(lwo,1L);
            else{
                counts.put(lwo,num+1L);
            }
        }

        //test word counts
        for(String word : counts.keySet()){
            Long count = counts.get(word);
            if(count>MAX_WORD_REPEATS){
                return true;
            }
        }

        return false;



    }

    public void addSpammed(String uid, User user){

        if(counter.containsKey(uid)){
            Integer integer = counter.get(uid);
            integer = integer + 1;
            counter.put(uid,integer);
            if(integer>SPAM_TIMEOUT_CRITERIA_COUNT_MAX){
                timeouts.put(uid,System.currentTimeMillis());
                user.sendMessage("You were blocked from sending messages for 10 minutes because you spammed too much.");
            }
        }
        else{
            counter.put(uid,1);
            spammed.put(uid,System.currentTimeMillis());
        }
    }

    public boolean isSpam(Message message){
        Channel channel = message.getChannelReceiver();
        User u = message.getAuthor();

        String uid = u.getId();

        //test if 30 seconds in the past that the person spammed
        if(spammed.containsKey(uid)){
            Long stamp = spammed.get(uid);
            long diff = System.currentTimeMillis()-stamp;
            if(diff>SPAM_TIMEOUT_CRITERIA_MILLIS){
                spammed.remove(uid);
                counter.remove(uid);
            }
        }

        boolean b = checkSpam(message);
        if(b){
            addSpammed(u.getId(),message.getAuthor());
            message.delete();
            try {
                Message message1 = channel.sendMessage(ConversationMessages.getInsult() + " " + u.getMentionTag()).get();
                noodle.doLater(() -> message1.edit("[spam by "+u.getName()+"]"),10000L);
            } catch (Exception e) {
                Log.stacktrace(e);
            }

            return true;
        }
        else return false;
    }

    public boolean isTimeout(User user){

        if(timeouts.containsKey(user.getId())){

            Long stamp = timeouts.get(user.getId());
            long diff = System.currentTimeMillis()-stamp;
            if(diff>TIMEOUT){
                timeouts.remove(user.getId());
                return false;
            }
            else return true;

        } else return false;

    }

    private class SpamData{

        String lastmsg;
        Long laststamp;

        public SpamData(){
            lastmsg = "";
            laststamp = 0L;
        }

        public String getLastMsg() {
            return lastmsg;
        }

        public void setLastMsg(String lastmsg) {
            this.lastmsg = lastmsg;
        }

        public Long getLastStamp() {
            return laststamp;
        }

        public void setLastStamp(Long laststamp) {
            this.laststamp = laststamp;
        }

        public boolean isOutdated(){

            long diff = System.currentTimeMillis()-laststamp;
            if(diff>MSG_TIMEOUT_MILLIS) return true;
            else return false;

        }
    }

}
