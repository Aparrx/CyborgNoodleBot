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

import io.github.cyborgnoodle.features.statistics.data.Minute5Statistics;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by arthur on 29.01.17.
 */
public class StatsPair {

    private List<Date> dates;
    private List<Minute5Statistics> minstats;

    public StatsPair() {
        this.dates = new ArrayList<>();
        this.minstats = new ArrayList<>();
    }

    public void add(Date date, Minute5Statistics stat){
        dates.add(date);
        minstats.add(stat);
    }

    public List<Date> getDates() {
        return dates;
    }

    public List<Minute5Statistics> getMinute5Stats() {
        return minstats;
    }

    public List<Number> asYNumbers(StatsType type, Object... args){

        List<Number> list = new ArrayList<>();

        for(Minute5Statistics s : minstats){

            switch (type){

                case MSG_SPEED:
                    list.add(s.getMessageCount()/5);
                    break;
                case MSG_COUNT:
                    list.add(s.getAllMessageCount());
                    break;
                case USER_COUNT:
                    list.add(s.getUserCount());
                    break;
                case USER_ONLINE_COUNT:
                    list.add(s.getOnlineCount());
                    break;
                case MSG_SPEED_CHANNEL:
                    if(args.length>0){
                        Object o1 = args[0];
                        if(o1 instanceof String){
                            String ch = (String) o1;
                            Long l = s.getPerChannel().get(ch);
                            if(l!=null) list.add(l/5);
                            else list.add(0);
                            break;
                        }
                    }
                    list.add(0);
                    break;
                case MSG_SPEED_USER:
                    if(args.length>0){
                        Object o1 = args[0];
                        if(o1 instanceof String){
                            String ch = (String) o1;
                            Long l = s.getPerUser().get(ch);
                            if(l!=null) list.add(l/5);
                            else list.add(0);
                            break;
                        }
                    }
                    list.add(0);
                    break;
                case USER_ONLINE_USER:
                    if(args.length>0){
                        Object o1 = args[0];
                        if(o1 instanceof String){
                            String ch = (String) o1;
                            boolean is = s.getOnlineusers().contains(ch);
                            if(is) list.add(1);
                            else list.add(0);
                            break;
                        }
                    }
                    list.add(0);
                    break;
            }

        }

        return list;

    }

    public void apply(XYChart chart, String name, StatsType type){
        chart.addSeries(name,dates,asYNumbers(type));
    }
}
