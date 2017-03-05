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

package io.github.cyborgnoodle.features.timed;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.tick.CyborgTick;
import io.github.cyborgnoodle.util.Log;

/**
 * Created by arthur on 04.11.16
 */
public class CyborgTickRunnable implements Runnable {

    CyborgNoodle noodle;

    public CyborgTickRunnable(CyborgNoodle noodle){
        this.noodle = noodle;
    }

    @Override
    public void run() {
        Log.info("Starting tick.", CyborgTick.context);
        while (noodle.isRunning()){
            noodle.tick.doTick();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.error("Something interrupted the TICK!",CyborgTick.context);
                Log.stacktrace(e);
                //noodle..notifyError(e); TODO !!!
            }
        }
        Log.info("Stopped tick.", CyborgTick.context);
    }
}
