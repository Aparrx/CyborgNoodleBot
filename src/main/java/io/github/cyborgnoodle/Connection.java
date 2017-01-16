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

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;

/**
 * Represents the connection to the Discord API
 */
public class Connection {

    ConnectionState status;
    DiscordAPI api;

    public Connection(String token){
        DiscordAPI api = Javacord.getApi(token, true);
        this.api = api;
    }

    public void setConnected(Boolean c){
        //connect
        if(c){
            Log.info("Connecting to Discord ...");
            status = ConnectionState.CONNECTING;
            api.connect(new FutureCallback<DiscordAPI>() {
                public void onSuccess(DiscordAPI discordAPI) {
                    connected();
                }

                public void onFailure(Throwable t) {
                    status = ConnectionState.UNCONNECTED;
                    Log.error("Could not connect to Discord: "+t.getMessage());
                    t.printStackTrace();
                }
            });
        }
        //disconnect
        else{
            Log.info("Disconnecting from Discord");
            api.disconnect();
            status = ConnectionState.UNCONNECTED;
            Log.info("Disconnected.");
        }
    }

    private void connected(){
        Log.info("Connected to Discord");
        status = ConnectionState.CONNECTED;
    }

    public Boolean isConnected(){
        if(status==ConnectionState.CONNECTED) return true;
        else return false;
    }

    public Boolean isConnecting(){
        if(status==ConnectionState.CONNECTING) return true;
        else return false;
    }

    public ConnectionState getStatus(){
        return status;
    }

    public DiscordAPI getAPI(){
        return api;
    }

    public enum ConnectionState{
        UNCONNECTED,CONNECTING,CONNECTED
    }

}
