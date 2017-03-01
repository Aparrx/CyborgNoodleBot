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

package io.github.cyborgnoodle.settings.data;

import java.util.TimeZone;

/**
 *
 */
public enum ServerUser {

    WONKAWOE("229083996615606272",TimeZone.getTimeZone("GMT-6")),
    TWOD("229307462895796224",TimeZone.getTimeZone("GMT-8")),
    DAX("200688166930350080",TimeZone.getTimeZone("GMT")),
    QUATRER("229001776404234241",TimeZone.getTimeZone("GMT+2")),
    TRAILER("95824087221305344",TimeZone.getTimeZone("GMT+1")),
    ENVEED("145575908881727488",TimeZone.getTimeZone("GMT+1")),
    JUSSA("199790842464960512",TimeZone.getTimeZone("GMT+1")),
    EMMA("54549376751632384",TimeZone.getTimeZone("GMT+10")),
    ROY("217783026275319810",TimeZone.getTimeZone("GMT")),
    KIU("178591608416108545",TimeZone.getTimeZone("GMT+1")),
    SPAGET("235608192833421313",TimeZone.getTimeZone("EST")),
    ;

    String id;
    TimeZone zone;

    ServerUser(String id, TimeZone zone){
        this.id = id;
        this.zone = zone;
    }

    public String getID(){
        return id;
    }

    public TimeZone getZone() {
        return zone;
    }

    public static ServerUser byID(String id){
        for(ServerUser user : values()){
            if(user.getID().equalsIgnoreCase(id)) return user;
        }
        return null;
    }

}
