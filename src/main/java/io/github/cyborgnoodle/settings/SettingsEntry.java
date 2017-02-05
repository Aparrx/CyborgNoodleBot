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

package io.github.cyborgnoodle.settings;

/**
 * Created by arthur on 30.01.17.
 */
public class SettingsEntry {

    private String category;
    private String name;
    private Object value;

    public SettingsEntry(String category, String name, Object value) {
        this.category = category;
        this.name = name;
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
