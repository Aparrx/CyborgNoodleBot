package io.github.cyborgnoodle.news;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.msg.SystemMessages;
import me.postaddict.instagramscraper.exception.InstagramException;

import java.io.IOException;

/**
 * Created by arthur on 29.10.16.
 */
public class InstagramRunnable implements Runnable{

    CyborgNoodle noodle;

    InstagramNotifier not;
    public InstagramRunnable(CyborgNoodle noodle){
        this.noodle = noodle;
        not = new InstagramNotifier(noodle);
    }

    public void run() {
        try {
            not.check();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstagramException e) {
            e.printStackTrace();
        }
    }
}
