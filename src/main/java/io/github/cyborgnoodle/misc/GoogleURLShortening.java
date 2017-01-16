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

package io.github.cyborgnoodle.misc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class GoogleURLShortening {
    private static final String GOOGLE_URL_SHORT_API = "https://www.googleapis.com/urlshortener/v1/url";
    private static final String GOOGLE_API_KEY = "AIzaSyCn7EK2LAznvVolfly51Ti60Cl_FBE82xQ";

    public static String shortenUrl(String longUrl) throws MalformedURLException{

        if (longUrl == null) {
            return longUrl;
        }else if(!longUrl.startsWith("http://") && !longUrl.startsWith("https://")){
            longUrl = "http://"+longUrl;
        }
        try {
            String json = "{\"longUrl\": \""+longUrl+"\"}";
            String apiURL = GOOGLE_URL_SHORT_API+"?key="+GOOGLE_API_KEY;

            HttpPost postRequest = new HttpPost(apiURL);
            postRequest.setHeader("Content-Type", "application/json");
            postRequest.setEntity(new StringEntity(json, "UTF-8"));

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(postRequest);
            String responseText = EntityUtils.toString(response.getEntity());

            Gson gson = new Gson();
            @SuppressWarnings("unchecked")
            HashMap<String, String> res = gson.fromJson(responseText, HashMap.class);

            return res.get("id");

        } catch (IOException e) {
            return "error";
        }
    }

    public static String getActualURLAgainstGoogleShortURL(String shortUrl){

        if (shortUrl == null) {
            return shortUrl;
        }else if(!shortUrl.startsWith("http://") && !shortUrl.startsWith("https://")){
            shortUrl = "http://"+shortUrl;
        }
        try {
            String apiURL =GOOGLE_URL_SHORT_API+"?key="+GOOGLE_API_KEY + "&shortUrl=" + shortUrl;
            HttpGet getRequest = new HttpGet(apiURL);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(getRequest);
            String responseText = EntityUtils.toString(response.getEntity());

            Gson gson = new Gson();
            @SuppressWarnings("unchecked")
            HashMap<String, String> res = gson.fromJson(responseText, HashMap.class);

            return res.get("longUrl");

        } catch (MalformedURLException e) {
            return "error";
        } catch (IOException e) {
            return "error";
        }
    }
}
