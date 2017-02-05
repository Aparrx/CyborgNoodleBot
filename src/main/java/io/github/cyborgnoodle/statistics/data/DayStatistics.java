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
public class DayStatistics {

    private Map<DayTime,Minute5Statistics> parts;

    public DayStatistics(){
        this.parts = new HashMap<>();
    }

    public DayStatistics(Map<DayTime, Minute5Statistics> parts) {
        this.parts = parts;
    }

    public Map<DayTime,Minute5Statistics> getMinute5s() {
        return parts;
    }

    public long getMessageCount(){
        long c = 0;
        for (Minute5Statistics part : parts.values()) {
            c = c + part.getMessageCount();
        }
        return c;
    }

    public long getMessageCountUser(String user){
        long l = 0;
        for (Minute5Statistics part : parts.values()) {
            if(part.getPerUser().containsKey(user)) l = l + part.getPerUser().get(user);
        }
        return l;
    }

    public long getMessageCountChannel(String channel){
        long l = 0;
        for (Minute5Statistics part : parts.values()) {
            if(part.getPerChannel().containsKey(channel)) l = l + part.getPerChannel().get(channel);
        }
        return l;
    }

    /**
     * msg / minute
     */
    public double getSpeed(){
        return (double) getMessageCount() / (parts.size()*5);
    }

    public int getSavedPartAmount(){
        return parts.size();
    }
}
