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

package io.github.cyborgnoodle.util;

import com.google.common.util.concurrent.ListenableFuture;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.utils.ratelimits.RateLimitType;
import org.json.JSONObject;
import java.util.concurrent.Future;

/**
 * Created by arthur on 18.01.17.
 */
public class JCUtil {

    public static Future<Message> getChannelMessageByID(DiscordAPI apiraw, Channel channel, String id){

        ImplDiscordAPI api;
        if(apiraw instanceof ImplDiscordAPI){
            api = (ImplDiscordAPI) apiraw;
        }
        else throw new IllegalStateException("API has no implementation!");

        ListenableFuture<Message> future = api.getThreadPool().getListeningExecutorService().submit(
                () -> {
                    String link = "https://discordapp.com/api/channels/" + channel.getId() + "/messages/" + id;
                    HttpResponse<JsonNode> response = Unirest.get(link).header("authorization", api.getToken()).asJson();
                    api.checkResponse(response);
                    api.checkRateLimit(response, RateLimitType.UNKNOWN, null, null);
                    JSONObject msgjson = response.getBody().getObject();
                    return (Message) new ImplMessage(msgjson,api,channel);
                });
        return future;
    }

}
