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

package io.github.cyborgnoodle.features.statistics;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.UserStatus;
import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.features.statistics.data.*;
import io.github.cyborgnoodle.util.Log;

import javax.annotation.Nullable;
import java.util.*;

/**
 *
 */
public class Statistics {

    public static final Log.LogContext context = new Log.LogContext("STATS");

    private static Map<StatsTime,DayStatistics> stats;
    private static long msgcount = 432884L;

    private static int STEP_LIMIT = 105120; //one year in 5 minutes

    public static void load(StatisticsData d){
        Log.info("Loading stats from data",context);
        msgcount = d.getMessagecount();
        stats = d.getDays();
    }

    public static StatisticsData save(){
        Log.info("Saving stats from data",context);
        return new StatisticsData(stats,msgcount);
    }

    public static long getMsgcount() {
        return msgcount;
    }

    private static int getOnlineMemberCount(Server server){

        Collection<User> col = server.getMembers();

        int count = 0;
        for(User user : col){
            if(user.getStatus().equals(UserStatus.ONLINE)) count++;
        }

        return count;

    }

    private static Collection<String> getOnlineUsers(Server server){
        Collection<User> col = server.getMembers();
        Collection<String> online = new ArrayList<>();
        for(User user : col){
            if(user.getStatus().equals(UserStatus.ONLINE)) online.add(user.getId());
        }

        return online;
    }

    public static void onMessage(Message message){
        statsTick(message.getAuthor(),message.getChannelReceiver(),message.getChannelReceiver().getServer(),1);
    }

    public static void statsTick(@Nullable User usero, @Nullable Channel channelo, Server server, int count){

        msgcount = msgcount + count; //full count

        String user;
        if(usero==null) user = null;
        else user = usero.getId();

        String channel;
        if(channelo==null) channel = null;
        else channel = channelo.getId();

        Calendar cnow = Calendar.getInstance();
        StatsTime today = new StatsTime(cnow);

        if(stats.containsKey(today)){
            DayStatistics daystats = stats.get(today);

            DayTime now = new DayTime(cnow);

            if(daystats.getMinute5s().containsKey(now)){
                Minute5Statistics minstats = daystats.getMinute5s().get(now);
                minstats.count(user,channel,count);
                minstats.setUserCount(server.getMemberCount());
                minstats.setOnlineCount(getOnlineMemberCount(server));
                minstats.setOnlineusers(getOnlineUsers(server));

                minstats.setAllMessageCount(msgcount);
            }
            else {

                Minute5Statistics minstats = new Minute5Statistics();
                minstats.count(user,channel,count);
                minstats.setUserCount(server.getMemberCount());
                minstats.setOnlineCount(getOnlineMemberCount(server));
                minstats.setOnlineusers(getOnlineUsers(server));

                minstats.setAllMessageCount(msgcount);

                daystats.getMinute5s().put(now,minstats);

            }

        }
        else {

            DayStatistics daystats = new DayStatistics();

            DayTime now = new DayTime(cnow);

            Minute5Statistics minstats = new Minute5Statistics();
            minstats.count(user,channel,count);
            minstats.setUserCount(server.getMemberCount());
            minstats.setOnlineCount(getOnlineMemberCount(server));
            minstats.setOnlineusers(getOnlineUsers(server));

            minstats.setAllMessageCount(msgcount);

            daystats.getMinute5s().put(now,minstats);
            stats.put(today,daystats);
        }

    }

    public static Map<StatsTime, DayStatistics> getStats() {
        return stats;
    }

    public static StatsPair forTime(Date from, Date to){
        Calendar fc = Calendar.getInstance();
        Calendar tc = Calendar.getInstance();
        fc.setTime(from);
        tc.setTime(to);
        return forTime(fc,tc);
    }

    public static StatsPair forTime(Calendar from, Calendar to){

        StatsPair pair = new StatsPair();

        Calendar current = from;

        int i = 0;
        while (i<STEP_LIMIT){

            StatsTime day = new StatsTime(current);
            DayTime time = new DayTime(current);

            DayStatistics dst = getStats().get(day);
            if(dst!=null){
                Minute5Statistics mstats = dst.getMinute5s().get(time);
                if(mstats!=null) pair.add(current.getTime(),mstats);
            }

            current.add(Calendar.MINUTE,5);
            i++;

            if(current.getTimeInMillis()>to.getTimeInMillis()) break;
        }

        return pair;

    }
}
