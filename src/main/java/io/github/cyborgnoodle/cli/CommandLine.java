package io.github.cyborgnoodle.cli;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by arthur on 16.10.16.
 */
public class CommandLine {

    CyborgNoodle noodle;

    public CommandLine(CyborgNoodle noods){
        this.noodle = noods;
    }

    public void listen(){
        Log.info("[CLI] Running and waiting for input");
        while (noodle.isRunning()){
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                String s = br.readLine();
                noodle.getCommands().execute(s);
            } catch (IOException e) {
                Log.error("Exception while reading commandline");
                e.printStackTrace();
            } catch (Exception e) {
                Log.error("Exception while executing command: "+e.getMessage());
                e.printStackTrace();
            }
        }
        Log.info("[CLI] Stopped");

    }

}
