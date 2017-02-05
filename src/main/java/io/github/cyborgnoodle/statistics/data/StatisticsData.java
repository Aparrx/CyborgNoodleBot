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

package io.github.cyborgnoodle.statistics.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arthur on 26.01.17.
 */
public class StatisticsData {

    private Map<StatsTime,DayStatistics> days;

    private long messagecount;

    public StatisticsData(){
        days = new HashMap<>();
        messagecount = 339974L;
    }

    public StatisticsData(Map<StatsTime, DayStatistics> days, long msgcount) {
        this.days = days;
        this.messagecount = msgcount;
    }

    public Map<StatsTime, DayStatistics> getDays() {
        return days;
    }

    public long getMessagecount() {
        return messagecount;
    }

    public void setMessagecount(long messagecount) {
        this.messagecount = messagecount;
    }
}
