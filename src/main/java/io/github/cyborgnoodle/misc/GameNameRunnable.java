package io.github.cyborgnoodle.misc;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.msg.SystemMessages;

/**
 * randomly change game naeme
 */
public class GameNameRunnable implements Runnable{

    CyborgNoodle noodle;

    public GameNameRunnable(CyborgNoodle noods){
        this.noodle = noods;
    }

    @Override
    public void run() {
        this.noodle.getAPI().setGame(SystemMessages.getGame());
    }
}
