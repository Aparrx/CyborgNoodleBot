package io.github.cyborgnoodle.levels;

import io.github.cyborgnoodle.CyborgNoodle;

/**
 * Created by arthur on 04.11.16.
 */
public class LevelUnblockerRunnable implements Runnable{

    CyborgNoodle noodle;

    public LevelUnblockerRunnable(CyborgNoodle noodle){
        this.noodle = noodle;
    }

    @Override
    public void run() {
        noodle.getLevels().doUnblock();
    }
}
