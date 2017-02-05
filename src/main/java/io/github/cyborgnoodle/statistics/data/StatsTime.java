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

import java.util.Calendar;

/**
 * Created by arthur on 26.01.17.
 */
public class StatsTime {

    private int day;
    private int year;

    public StatsTime(int day, int year) {
        this.day = day;
        this.year = year;
    }

    public StatsTime(Calendar now){
        this.day = now.get(Calendar.DAY_OF_YEAR);
        this.year = now.get(Calendar.YEAR);
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatsTime)) return false;

        StatsTime statsTime = (StatsTime) o;

        if (getDay() != statsTime.getDay()) return false;
        return getYear() == statsTime.getYear();
    }

    @Override
    public int hashCode() {
        int result = getDay();
        result = 31 * result + getYear();
        return result;
    }
}
