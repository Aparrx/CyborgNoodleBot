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

package io.github.cyborgnoodle.features.funtance;

import io.github.cyborgnoodle.features.funtance.data.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by arthur on 17.01.17.
 */
public class DataCollection {

    private Set<String> objects;
    private Set<String> places;
    private Set<String> persons;
    private Set<String> time;
    private Set<String> verbs;

    public DataCollection() {
        this.objects = new HashSet<>();
        this.places = new HashSet<>();
        this.persons = new HashSet<>();
        this.time = new HashSet<>();
        this.verbs = new HashSet<>();
    }

    public void pull(){
        this.objects = ObjectData.getData();
        this.places = OrtData.getData();
        this.persons = PersonenData.getData();
        this.time = UmstandData.getData();
        this.verbs = VerbData.getData();
    }

    public void push(){
        ObjectData.setData(objects);
        OrtData.setData(places);
        PersonenData.setData(persons);
        UmstandData.setData(time);
        VerbData.setData(verbs);
    }
}
