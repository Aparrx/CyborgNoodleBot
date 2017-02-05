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

/**
 * Created by arthur on 26.01.17.
 */
public class FullTime implements Comparable{

    StatsTime time;
    DayTime day;

    public FullTime(StatsTime time, DayTime day) {
        this.time = time;
        this.day = day;
    }

    public StatsTime getStatsTime() {
        return time;
    }

    public DayTime getDayTime() {
        return day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FullTime)) return false;

        FullTime fullTime = (FullTime) o;

        if (time != null ? !time.equals(fullTime.time) : fullTime.time != null) return false;
        return day != null ? day.equals(fullTime.day) : fullTime.day == null;
    }

    @Override
    public int hashCode() {
        int result = time != null ? time.hashCode() : 0;
        result = 31 * result + (day != null ? day.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Object o) {
        if(o.equals(this)) return 0;

        if (o instanceof FullTime){

            FullTime ft = (FullTime) o;

            if(ft.getStatsTime().getYear()<this.getStatsTime().getYear()) return 1;
            if(ft.getStatsTime().getYear()>this.getStatsTime().getYear()) return -1;

            if(ft.getStatsTime().getDay()<this.getStatsTime().getDay()) return 1;
            if(ft.getStatsTime().getDay()>this.getStatsTime().getDay()) return -1;

            if(ft.getDayTime().getCounter()<this.getDayTime().getCounter()) return 1;
            if(ft.getDayTime().getCounter()>this.getDayTime().getCounter()) return 1;

            return 0;

        }
        else throw new ClassCastException("Cannot cast "+o.getClass().getName()+" to FullTime!");
    }
}
