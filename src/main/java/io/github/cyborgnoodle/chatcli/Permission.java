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

package io.github.cyborgnoodle.chatcli;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.server.ServerRole;

/**
 *
 */
public class Permission {

    private ServerRole role;
    private int level;

    public Permission(){
        role = null;
        level = 0;
    }

    public Permission(ServerRole r){
        role = r;
        level = 0;
    }

    public Permission(int l){
        role = null;
        level = l;
    }

    public Permission(ServerRole r, int l){
        role = r;
        level = l;
    }

    public boolean has(CyborgNoodle noodle, User user){
        boolean roleperm;
        if(this.role!=null){
            Role r = noodle.getRole(this.role);
            if(r==null) roleperm = true;
            else {
                if(r.getUsers().contains(user)) roleperm = true;
                else roleperm = false;
            }
        } else roleperm = true;

        boolean lvlperm;
        if(this.level==0) lvlperm = true;
        else {
            int level = noodle.getLevels().getRegistry().getLevel(user.getId());
            if(level>=this.level) lvlperm = true;
            else lvlperm = false;
        }

        return roleperm && lvlperm;
    }

    public ServerRole getRole() {
        return role;
    }

    public int getLevel() {
        return level;
    }
}
