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
import java.util.Date;

/**
 * Created by arthur on 26.01.17.
 */
public class DayTime {

    private int counter;

    public DayTime(int counter) {
        this.counter = counter;
    }

    public DayTime(Calendar now){

        int hours = now.get(Calendar.HOUR_OF_DAY);
        int minutes = now.get(Calendar.MINUTE);

        int minutesofday = (hours * 60) + minutes;

        int fiversofday = minutesofday / 5;

        this.counter = fiversofday;

    }

    public Date asDate(){
        int mod = counter * 5;
        int hours = mod / 60;
        int minutes = mod % 60;

        Calendar now = Calendar.getInstance();
        now.set(now.get(Calendar.YEAR),Calendar.MONTH,Calendar.DAY_OF_MONTH,hours,minutes);

        return now.getTime();

    }

    public int getCounter() {
        return counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DayTime)) return false;

        DayTime dayTime = (DayTime) o;

        return getCounter() == dayTime.getCounter();
    }

    @Override
    public int hashCode() {
        return getCounter();
    }
}
