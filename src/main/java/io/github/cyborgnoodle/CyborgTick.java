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

package io.github.cyborgnoodle;

/**
 * Created by arthur on 04.11.16
 */
public class CyborgTick implements Runnable {

    CyborgNoodle noodle;

    public CyborgTick(CyborgNoodle noodle){
        this.noodle = noodle;
    }

    @Override
    public void run() {
        Log.info("[TICK] Starting tick.");
        while (noodle.isRunning()){
            try {
                noodle.runTick();
            } catch (Exception e) {
                e.printStackTrace();
                Log.error("[TICK] Exception in TICK!");
                noodle.getNotifier().notifyError(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.error("[TICK] Something interrupted the TICK!");
                e.printStackTrace();
                noodle.getNotifier().notifyError(e);
            }
        }
        Log.info("[TICK] Stopped tick.");
    }
}
