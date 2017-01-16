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

package io.github.cyborgnoodle.server;

/**
 * Roles on the discord Server
 */
public enum ServerRole {

    MOD("229000415050465281"),
    GUARD("229382170542604292"),

    STAFF("230772632818155520"),

    NOODLE("229018306349236234"),
    RUSSEL("230712812790480896"),
    MURDOC("230712906336043009"),
    TWOD("230712668695166976"),

    GORILLAZ("230772457525477386"),

    ANNOUNCER("236492015020408835"),

    BOT("230772028960014336"),

    INVISIBLE("230716055448715264"),

    NEWS_SUB("244222039207051266"),

    OWNER("246667742168481793"),

    RANK_JANITOR("231136361640361985"),
    RANK_LIFEBOATGUY("231136842026582027"),
    RANK_DAREMECH("231137158570835970"),
    RANK_TRUCKDRIVER("233625728543883264"),
    RANK_WINDMILLINSPECTOR("233625934807302145"),
    RANK_CEO("231756645481316352"),

    REGULAR("244222039207051266"),
    ;

    String id;

    ServerRole(String id){
        this.id = id;
    }

    public String getID(){
        return id;
    }

}
