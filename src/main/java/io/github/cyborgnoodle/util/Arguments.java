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

package io.github.cyborgnoodle.util;

/**
 * Created by arthur on 05.03.17.
 */
public class Arguments {

    private String[] array;

    public Arguments(String[] array) {
        this.array = array;
    }

    public String get(int index) throws ArrayIndexOutOfBoundsException {
        return array[index];
    }

    public boolean has(int index) {
        return (index+1)<=array.length;
    }
}
