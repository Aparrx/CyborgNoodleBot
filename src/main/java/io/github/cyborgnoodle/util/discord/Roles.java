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

package io.github.cyborgnoodle.util.discord;

import de.btobastian.javacord.entities.permissions.Role;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.settings.data.ServerRole;

import java.util.ArrayList;

/**
 * Created by arthur on 25.02.17.
 */
public class Roles {

    public static Role[] resolveAll(CyborgNoodle noodle, ServerRole... roles){

        ArrayList<Role> list = new ArrayList<>();
        for(ServerRole role : roles){
            list.add(noodle.getRole(role));
        }
        return list.toArray(new Role[list.size()]);
    }

}
