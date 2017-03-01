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

/**
 * Channels on the server
 */
public enum ServerChannel {

    WELCOME("274441684144619521",0),          //0
    ANNOUNCEMENTS("275711346409078784",1),    //1
    NEWS("274441331554910208",2),             //2
    GENERAL("274439447234347010",3),          //3
    FOUNDERS("274442897615618049",4),         //4
    SIMULATOR("282203160792006656",5),        //5
    STAFF("274459433625452544",6),            //6
    ART("283386116130996225",7),              //7
    LISTEN("274647161541492747",8),           //8
    WATCH("285815110503497730",14),
    TRASH("274445071095562261",9),    //9
    SOCIAL("278562195359531020",10),   //10
    MUSEUM("274445034085154828",11),           //11
    SPAM("275677608241528833",12),             //12

    OWNER("282203016226930689",13),            //14



    ;

    String id;
    int sid;

    ServerChannel(String id, int sid){
        this.id = id;
        this.sid = sid;
    }

    public String getID(){
        return id;
    }

    public int getPersistentID(){
        return sid;
    }

    public static ServerChannel byID(String id){
        for(ServerChannel ch : values()){
            if(ch.id.equals(id)) return ch;
        }
        return null;
    }

    public static ServerChannel byPersistentID(int sid){
        for(ServerChannel ch : values()){
            if(ch.sid==sid) return ch;
        }
        return null;
    }

}
