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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.cyborgnoodle.auth.Authentication;
import io.github.cyborgnoodle.util.Log;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Main class for CyborgNoodle Discord Bot
 */
public class Main {

    private static CyborgNoodle NOODLE;
    public static Authentication AUTH;
    public static long START_STAMP;

    //APP ENTRY POINT
    public static void main(String[] args){

        Log.info("STARTING "+Meta.getVersion());

        boolean tm = Arrays.asList(args).contains("test");

        Thread.currentThread().setName("CN Main");

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.getLogger("de.btobastian").setLevel(Level.WARN);

        try {
            readAuth();
        } catch (Exception e) {
            Log.error("Failed to read Auth file!");
            e.printStackTrace();
            return;
        }

        if(AUTH==null){
            Log.error("Failed to read Auth file! (AUTH is empty)");
            return;
        }

        START_STAMP = System.currentTimeMillis();

        NOODLE = null;
        Connection con = connect();
        launchBot(con,tm);


    }

    public static Connection connect(){

        Connection connection = new Connection(AUTH.getDiscordtoken());
        connection.setConnected(true);
        return connection;
    }

    public static void launchBot(Connection connection, boolean testmode){

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
            noodle.setTestmode(testmode);
            NOODLE = noodle;
            Log.info("Successfully created CyborgNoodle Bot, ready.");
            if(noodle.isTestmode()) Log.warn("STARTING BOT IN TEST MODE!!!");
        }
        else{
            NOODLE = null;
            Log.info("Stopping bot as no connection could be established.");
        }

    }

    public synchronized CyborgNoodle getNoodle(){
        return NOODLE;
    }

    private static void readAuth() throws Exception {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        SaveManager man = new SaveManager(null);
        String json = man.read(SaveManager.ConfigFile.AUTH.getConfigFile());

        AUTH = gson.fromJson(json, Authentication.class);

    }
}
