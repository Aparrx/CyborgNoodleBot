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

package io.github.cyborgnoodle;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 * Main class for CyborgNoodle Discord Bot
 */
public class Main {

    public static String TOKEN = "MjM3MTQzNzg4NzMwOTc0MjA4.CuanLQ.xMs3QaaPL2T7WZg1T4UXKsjns8s";

    private static CyborgNoodle NOODLE;

    //APP ENTRY POINT
    public static void main(String[] args){

        Thread.currentThread().setName("CN Main");

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.getLogger("de.btobastian").setLevel(Level.WARN);

        NOODLE = null;
        Connection con = connect();
        launchBot(con);
    }

    public static Connection connect(){

        Connection connection = new Connection(TOKEN);
        connection.setConnected(true);
        return connection;
    }

    public static void launchBot(Connection connection){

        while(connection.isConnecting()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Log.error("Waiting for connection was interrupted: "+e.getMessage());
                e.printStackTrace();
            }
        }

        if(connection.isConnected()){
            CyborgNoodle noodle = new CyborgNoodle(connection);
            NOODLE = noodle;
            Log.info("Successfully created CyborgNoodle Bot, ready.");
        }
        else{
            NOODLE = null;
            Log.info("Stopping bot as no connection could be established.");
        }

    }

    public synchronized CyborgNoodle getNoodle(){
        return NOODLE;
    }

}
