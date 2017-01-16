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

package io.github.cyborgnoodle.news;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.misc.GoogleURLShortening;
import io.github.cyborgnoodle.server.ServerChannel;
import io.github.cyborgnoodle.server.ServerRole;
import me.postaddict.instagramscraper.Instagram;
import me.postaddict.instagramscraper.exception.InstagramException;
import me.postaddict.instagramscraper.model.Account;
import me.postaddict.instagramscraper.model.Media;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Instant;
import java.util.List;

/**
 * Created by arthur on 29.10.16.
 */
public class InstagramNotifier {

    long NOTBEFORE = 1482167152;
    CyborgNoodle noodle;
    Instagram insta;

    String[] names = new String[]{"gorillaz","hewll","damonalbarn","watashiwanoodle","cyborgnoodlebot"};

    public InstagramNotifier(CyborgNoodle noodle) {
        this.noodle = noodle;
        insta = new Instagram();
    }

    public void check() throws IOException, InstagramException {

        try {
            for(String name : names){

                List<Media> mlist = insta.getMedias(name,10);

                for(Media media : mlist){

                    if(media.createdTime>NOTBEFORE){

                        Boolean seen = noodle.getInstaRegistry().isSeen(media.id);

                        if(!seen){

                            noodle.getInstaRegistry().setSeen(media.id);

                            if(media.type.equalsIgnoreCase("image")){

                                ServerChannel c;
                                if(name.equalsIgnoreCase("cyborgnoodlebot")){
                                    c = ServerChannel.CYBORGTEST;
                                }
                                else c = ServerChannel.NEWS;

                                String url = media.imageStandardResolutionUrl;
                                String caption = media.caption;
                                String location = media.locationName;

                                String originalcaption = caption;

                                if(caption==null) caption = "";
                                else{

                                    String[] csplit = caption.split(" ");
                                    for(String cword : csplit){
                                        if(cword.startsWith("@")){

                                            String uname = cword.replace("@","");
                                            String ulink = getLinkToUser(uname);

                                            if(ulink!=null){
                                                try {
                                                    String ushortlink = GoogleURLShortening.shortenUrl(ulink);
                                                    caption = caption.replace(cword,cword+" ["+ushortlink+"]");
                                                } catch (MalformedURLException e) {
                                                    Log.warn("could not shorten caption tagged URL: "+e.getMessage());
                                                    caption = caption.replace(cword,cword+" ["+ulink+"]");
                                                }

                                            }
                                        }
                                    }

                                }


                                String finalurl;
                                try {
                                    finalurl = GoogleURLShortening.shortenUrl(url);
                                } catch (MalformedURLException e) {
                                    Log.error("Failed to short Instagram URL!");
                                    e.printStackTrace();
                                    finalurl = url;
                                }

                                String msg = "**"+name+"**: "+finalurl+"\n\n "+caption;

                                if(location!=null) msg = msg + "\n*"+location+"*";

                                Log.info("send insta [image] update for "+name);

                                noodle.getChannel(c).sendMessage(msg);

                                if(noodle.getReddit().isConnected()){
                                    noodle.
                                            getReddit().
                                            postNews(url,originalcaption,media.createdTime*1000,name,media.link,"https://instagram.com/"+name+"/");
                                }

                                /*Log.info("caption: "+media.caption);
                                Log.info("HD url: "+media.imageHighResolutionUrl);
                                Log.info("LOW url: "+media.imageLowResolutionUrl);
                                Log.info("STANDARD url: "+media.imageStandardResolutionUrl);
                                Log.info("THUMB url: "+media.imageThumbnailUrl);
                                Log.info("ID: "+media.id);
                                Log.info("code: "+media.code);
                                Log.info("link: "+media.link);*/

                            }
                            else{

                                ServerChannel c;
                                if(name.equalsIgnoreCase("cyborgnoodlebot")){
                                    c = ServerChannel.CYBORGTEST;
                                }
                                else c = ServerChannel.NEWS;

                                String url = media.link;
                                String caption = media.caption;
                                String location = media.locationName;

                                String originalcaption = caption;

                                if(caption==null) caption = "";
                                else{

                                    String[] csplit = caption.split(" ");
                                    for(String cword : csplit){
                                        if(cword.startsWith("@")){

                                            String uname = cword.replace("@","");
                                            String ulink = getLinkToUser(uname);

                                            if(ulink!=null){
                                                try {
                                                    String ushortlink = GoogleURLShortening.shortenUrl(ulink);
                                                    caption = caption.replace(cword,cword+" ["+ushortlink+"]");
                                                } catch (MalformedURLException e) {
                                                    Log.warn("could not shorten caption tagged URL: "+e.getMessage());
                                                    caption = caption.replace(cword,cword+" ["+ulink+"]");
                                                }

                                            }
                                        }
                                    }

                                }

                                String msg = "**"+name+"**: "+url+"\n\n "+caption;

                                if(location!=null) msg = msg + "\n*"+location+"*";

                                Log.info("send insta [video] update for "+name);

                                noodle.getChannel(c).sendMessage(msg);

                                if(noodle.getReddit().isConnected()){
                                    noodle.
                                            getReddit().
                                            postNews(media.videoStandardResolutionUrl,originalcaption,media.createdTime*1000,name,media.link,"https://instagram.com/"+name+"/");
                                }
                            }
                        }
                    }

                }


            }
        } catch (IOException e) {
            Log.warn("IOException while Instagram Checking: "+e.getMessage());
        } catch (InstagramException e) {
            Log.warn("InstagramException while Instagram Checking: "+e.getMessage());
        }

    }

    public String getLinkToUser(String user){
        try {
            Account ac = insta.getAccountByUsername(user);
            return "https://instagram.com/"+ac.username+"/";
        } catch (IOException e) {
            Log.error("could not get link for caption tagged user "+user+": IOException - "+e.getMessage());
            return null;
        } catch (InstagramException e) {
            Log.error("could not get link for caption tagged user "+user+": InstaException - "+e.getMessage());
            return null;
        }
    }

}
