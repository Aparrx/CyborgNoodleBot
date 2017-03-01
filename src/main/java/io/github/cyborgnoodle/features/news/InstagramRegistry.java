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

package io.github.cyborgnoodle.features.news;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by arthur on 29.10.16.
 */
public class InstagramRegistry {

    private Set<String> seen;

    public InstagramRegistry(){
        seen = new HashSet<>();
    }

    public synchronized void setSeen(String id){
        seen.add(id);
    }

    public synchronized Boolean isSeen(String id){
        return seen.contains(id);
    }

    public Set<String> getSeen() {
        return seen;
    }
}
