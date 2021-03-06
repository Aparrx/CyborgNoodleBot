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

package io.github.cyborgnoodle.tick;

/**
 * Created by arthur on 04.11.16.
 *
 */
public class Waiter {

    private long millis;
    private Runnable run;

    private long last;

    public Waiter(long millis, Runnable r){
        this.run = r;
        this.millis = millis;
        this.last = 0;
    }

    public void run(){
        long now = System.currentTimeMillis();
        if((now-last)>millis){
            last = now;
            run.run();
        }
    }

}
