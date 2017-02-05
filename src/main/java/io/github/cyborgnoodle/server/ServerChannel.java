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
    GENERAL("274439447234347010"),
    CYBORGTEST("274444962836250624"),
    NEWS("274441331554910208"),
    MUSEUM("274445034085154828"),
    TRASH("274445071095562261"),
    ;

    String id;

    ServerChannel(String id){
        this.id = id;
    }

    public String getID(){
        return id;
    }

}
