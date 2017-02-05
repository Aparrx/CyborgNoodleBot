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

package io.github.cyborgnoodle.chatcli.commands;

import com.google.common.util.concurrent.*;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.stats2.Stats2Callable;
import io.github.cyborgnoodle.statistics.Statistics;
import io.github.cyborgnoodle.statistics.StatsPair;
import io.github.cyborgnoodle.statistics.data.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by arthur on 26.01.17.
 */
public class Stats2Command extends Command{

    public Stats2Command(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {

        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
        Message message = getChannel().sendMessage("`Loading statistics ...`").get();

        Stats2Callable callable = new Stats2Callable(args, this, message);

        ListenableFuture<Void> fut = service.submit(callable);

        Futures.addCallback(fut, new FutureCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void aVoid) {
                // suc
            }

            @Override
            public void onFailure(Throwable throwable) {
                message.delete();
                getChannel().sendMessage("Failed to create graph!");
                Log.error("Failed to create graph!");
                throwable.printStackTrace();
            }
        });
    }

    public StatsPair getForRange(DisplayType type){

        StatsPair m;

        switch (type){

            case TODAY:
                m = getForToday();
                break;
            case WEEK:
                m = getForPast(7);
                break;
            case MONTH:
                m = getForPast(31);
                break;
            case YEAR:
                m = getForPast(365);
                break;
            case ALL:
                m = getForPast(Statistics.getStats().size());
                break;
            case HOUR:
                m = getForPast(Calendar.HOUR_OF_DAY,1);
                break;
            case HOUR12:
                m = getForPast(Calendar.HOUR_OF_DAY,12);
                break;
            default:
                m = getForToday();
                break;
        }

        return m;

    }

    public StatsPair getForToday(){
        Calendar now = Calendar.getInstance();
        Calendar ago = Calendar.getInstance();
        ago.add(Calendar.HOUR_OF_DAY,-24);
        return Statistics.forTime(ago,now);
    }

    public StatsPair getForPast(int days){
        return getForPast(Calendar.DAY_OF_YEAR,days);
    }

    public StatsPair getForPast(int type, int amount){
        Calendar now = Calendar.getInstance();
        Calendar ago = Calendar.getInstance();
        ago.add(type,-amount);
        return Statistics.forTime(ago,now);
    }

    @Deprecated
    public SortedMap<FullTime,Minute5Statistics> getForDayXX(Calendar time){

        SortedMap<FullTime,Minute5Statistics> m = new TreeMap<>();

        StatsTime now = new StatsTime(time);

        DayStatistics daystats = Statistics.getStats().get(now);

        if(daystats!=null){
            List<DayTime> dates = new ArrayList<>(daystats.getMinute5s().keySet());
            dates.sort((o1, o2) -> {
                if (o1.getCounter() < o2.getCounter()) return -1;
                if (o1.getCounter() > o2.getCounter()) return 1;

                return 0;
            });

            for(DayTime dt : dates){
                Minute5Statistics minstats = daystats.getMinute5s().get(dt);
                if(minstats!=null) m.put(new FullTime(now,dt),minstats);
            }
        }

        return m;

    }


    @Override
    public String[] aliases() {
        return new String[]{"sts","stats","statistics","st","s","stats2"};
    }

    @Override
    public String usage() {
        return "!stats [hour | hour12 | today | week | month | year | all]\n[users | count | @User @User2 ... | #channel #channel2 ...]";
    }

    @Override
    public boolean emptyHelp() {
        return false;
    }

    @Override
    public String detailusage() {
        return "\n- `[hour | hour12 | today | week | month | year | all]` - specify time range\n" +
                "- `[@User | #channel | users | count]`\n\n" +
                "> `users [@User @User2]` - show user count on the server (and compare by users)\n" +
                "> `count` - show message count on the server\n" +
                "> `@User @User2 ..` - compare message speed by users\n" +
                "> `#channel #channel2 ...` - compare message speed by channels\n\n" +
                "Default / fallback is `today` and message speed from all channels";
    }

    @Override
    public String description() {
        return "show statistics (as a graph)";
    }

    public enum DisplayType{
        HOUR, HOUR12, TODAY, WEEK, MONTH, YEAR, ALL
    }
}
