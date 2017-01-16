package io.github.cyborgnoodle;

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
