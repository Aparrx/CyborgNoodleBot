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

import com.google.common.util.concurrent.MoreExecutors;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.features.converter.ExchangeRates;
import io.github.cyborgnoodle.features.levels.LevelUnblockerRunnable;
import io.github.cyborgnoodle.features.news.InstagramRunnable;
import io.github.cyborgnoodle.features.simulator.Simulator;
import io.github.cyborgnoodle.features.statistics.Statistics;
import io.github.cyborgnoodle.features.timed.AutoSaveRunnable;
import io.github.cyborgnoodle.features.timed.GameNameRunnable;
import io.github.cyborgnoodle.util.Log;

import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * Created by arthur on 01.03.17.
 */
public class CyborgTick {

    private CyborgNoodle noodle;

    // WAITERS

    Waiter gamenamewt;
    Waiter instawt;
    Waiter lvlunblockwt;
    Waiter redditmsgwt;
    Waiter savewt;
    Waiter statswt;

    Waiter exchangeratewt;

    Waiter simwaiter;

    public CyborgTick(CyborgNoodle noodle){
        this.noodle = noodle;
        init();
    }

    public void init(){

        gamenamewt = new Waiter(60000,new GameNameRunnable(noodle));
        instawt = new Waiter(10000,new InstagramRunnable(noodle));
        lvlunblockwt = new Waiter(1000,new LevelUnblockerRunnable(noodle));

        savewt = new Waiter(1800000,new AutoSaveRunnable(noodle));

        // 2.5 minutes
        statswt = new Waiter(150000, () -> Statistics.statsTick(null,null,noodle.getServer(),0));

        // 5 minutes
        simwaiter = new Waiter(300000, () -> Simulator.post(noodle));

        // 1 hr
        exchangeratewt = new Waiter(3600000, () -> MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor()).submit((Callable<Void>) () -> {
            Log.info("Updating exchange rate cache ...");
            ExchangeRates.updateRates();
            Log.info("Exchange rate cache was updated.");
            return null;
        }));
    }

    public void doTick(){
        try {
            tick();
        } catch (Exception e) {
            Log.error("Exception in TICK: "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void tick() throws Exception {

        //waiters

        lvlunblockwt.run();
        gamenamewt.run();
        if(!noodle.isTestmode()) instawt.run();
        //redditmsgwt.run();

        if(!noodle.isTestmode()) savewt.run();

        statswt.run();

        simwaiter.run();

        exchangeratewt.run();


        // run later

        HashSet<Runnable> toremove = new HashSet<>();

        for(Runnable r : noodle.later.keySet()){
            long millis = noodle.later.get(r);
            if(millis<System.currentTimeMillis()){
                r.run();
                toremove.add(r);
            }
        }

        for(Runnable r : toremove) noodle.later.remove(r);
    }

}
