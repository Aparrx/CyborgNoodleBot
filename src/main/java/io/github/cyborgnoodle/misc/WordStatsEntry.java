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

import de.btobastian.javacord.entities.User;

import java.util.HashMap;

/**
 *
 */
public class WordStatsEntry {

    volatile Long count;
    volatile HashMap<String,Long> users;

    public WordStatsEntry(){
        count = 0L;
        users = new HashMap<>();
    }

    public WordStatsEntry(Long precounted){
        count = precounted;
        users = new HashMap<>();
    }

    public HashMap<String, Long> getUsers() {
        return users;
    }

    public Long getCount() {
        return count;
    }

    public void count(Long c){
        count = count + c;
    }

    public void count(Long c, User user){
        count(c);
        String id = user.getId();
        if(!users.containsKey(id)){
            users.put(id,c);
        }
        else {
            Long aLong = users.get(id);
            long counted = aLong + c;
            users.put(id,counted);
        }
    }
}
