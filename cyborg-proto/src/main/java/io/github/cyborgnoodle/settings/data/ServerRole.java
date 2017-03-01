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
 * Roles on the discord Server
 */
public enum ServerRole {

    MOD("274441063463125004"),
    GUARD("274442072264802304"),

    STAFF("274444183668916225"),

    NOODLE("229018306349236234"),
    RUSSEL("230712812790480896"),
    MURDOC("230712906336043009"),
    TWOD("230712668695166976"),

    GORILLAZ("230772457525477386"),

    BOT("230772028960014336"),

    INVISIBLE("230716055448715264"),

    NEWS_SUB("244222039207051266"),

    OWNER("274440138992648195"),

    RANK_JANITOR("274442653058465793"),
    RANK_LIFEBOATGUY("274442601099427851"),
    RANK_DAREMECH("274442574620524544"),
    RANK_TRUCKDRIVER("274442536687370240"),
    RANK_WINDMILLINSPECTOR("274442504349286411"),
    RANK_CEO("274442457632997376"),
    RANK_GRAV_SOUND_CHECK("274442418928091137"),
    RANK_STARSHINE("274442383368650763"),
    RANK_LAST_SOUL("274442340683481089"),
    RANK_GHOST_TRAIN("274442249381871617"),

    CHAR_NOODLE("276722668005228544"),
    CHAR_2D("276723007853035522"),
    CHAR_RUSSEL("276722842442399744"),
    CHAR_MURDOC("276723101579083776"),


    FOUNDER("274442728090107905"),
    ;

    String id;

    ServerRole(String id){
        this.id = id;
    }

    public String getID(){
        return id;
    }

    public static ServerRole[] getRanks(){
        return new ServerRole[]{
                RANK_JANITOR,
                RANK_LIFEBOATGUY,
                RANK_DAREMECH,
                RANK_TRUCKDRIVER,
                RANK_WINDMILLINSPECTOR,
                RANK_CEO,
                RANK_GRAV_SOUND_CHECK,
                RANK_STARSHINE,
                RANK_LAST_SOUL,
                RANK_GHOST_TRAIN
        };
    }

    public static ServerRole[] getCharacters(){
        return new ServerRole[]{
                CHAR_2D,
                CHAR_MURDOC,
                CHAR_NOODLE,
                CHAR_RUSSEL
        };
    }


}
