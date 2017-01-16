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
 * Channels on the server
 */
public enum ServerChannel {

    CYBORGLABS("236518682405109760"),
    GENERAL("229000154936639488"),
    CYBORGTEST("237199236305911808"),
    NEWS("236492526431764480"),
    MUSEUM("248089767130955778"),
    TRASH("229009187085090817"),
    ;

    String id;

    ServerChannel(String id){
        this.id = id;
    }

    public String getID(){
        return id;
    }

}
