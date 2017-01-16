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
