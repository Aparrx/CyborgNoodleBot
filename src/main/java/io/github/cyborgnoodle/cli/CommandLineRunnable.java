package io.github.cyborgnoodle.cli;

/**
 * Created by arthur on 16.10.16.
 */
public class CommandLineRunnable implements Runnable{

    CommandLine cli;

    public CommandLineRunnable(CommandLine cli){
        this.cli = cli;
    }

    public void run() {
        cli.listen();
    }
}
