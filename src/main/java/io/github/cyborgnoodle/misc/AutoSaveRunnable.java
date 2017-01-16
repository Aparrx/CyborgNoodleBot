package io.github.cyborgnoodle.misc;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;

/**
 * Created by arthur on 17.11.16.
 */
public class AutoSaveRunnable implements Runnable {

    CyborgNoodle noodle;

    public AutoSaveRunnable(CyborgNoodle noodle){
        this.noodle = noodle;
    }

    @Override
    public void run() {
        noodle.getSaveManager().saveAll();
        Log.info("Saved all data");
    }
}
