package io.github.cyborgnoodle;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;

/**
 * Created by arthur on 04.11.16
 */
public class ErrorNotifier {

    String USER = "145575908881727488";

    String MARKER = "```";

    CyborgNoodle noodle;

    public ErrorNotifier(CyborgNoodle noodle){
        this.noodle = noodle;
    }

    public void notifyError(Exception e){

        String emsg = e.getMessage();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String trace = sw.toString();

        String msg = emsg+"\n\n"+
                MARKER+"\n"+trace+"\n"+MARKER;

        try {
            noodle.getAPI().getUserById(USER).get().sendMessage(msg);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }


    }

}
