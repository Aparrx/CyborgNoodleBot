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

package io.github.cyborgnoodle.levels;

import io.github.cyborgnoodle.server.ServerRole;

import java.util.HashMap;

/**
 * get the rank role for a level
 */
public class RankCalculator {

    private static HashMap<Integer,ServerRole> roles;

    static {
        roles = new HashMap<>();
        create();
    }

    private static void create(){

        roles.put(3,ServerRole.RANK_JANITOR);
        roles.put(7,ServerRole.RANK_LIFEBOATGUY);
        roles.put(12,ServerRole.RANK_DAREMECH);
        roles.put(19,ServerRole.RANK_TRUCKDRIVER);
        roles.put(26,ServerRole.RANK_WINDMILLINSPECTOR);
        roles.put(35,ServerRole.RANK_CEO);
        roles.put(65,ServerRole.RANK_GRAV_SOUND_CHECK);
        roles.put(78,ServerRole.RANK_STARSHINE);
        roles.put(91,ServerRole.RANK_LAST_SOUL);
        roles.put(100,ServerRole.RANK_GHOST_TRAIN);

    }

    public static ServerRole getRoleforLevel(int lvl){

        ServerRole role = null;
        while(lvl>0){
            role = roles.get(lvl);
            if(role==null){
                lvl--;
            }
            else break;
        }

        if(role==null) return null;
        else return role;
    }

    public static HashMap<Integer, ServerRole> getRoles() {
        return roles;
    }
}
