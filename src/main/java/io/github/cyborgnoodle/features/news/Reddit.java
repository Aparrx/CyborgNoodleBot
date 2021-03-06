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

package io.github.cyborgnoodle.features.news;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Main;
import io.github.cyborgnoodle.Meta;
import io.github.cyborgnoodle.util.Log;
import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.managers.AccountManager;
import net.dean.jraw.models.Submission;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by arthur on 07.11.16.
 */
public class Reddit {

    public static final Log.LogContext context = new Log.LogContext("RDIT");
    public static String CURRENT_LINK = "https://discord.gg/FPdpChq";

    CyborgNoodle noodle;

    String USERNAME;
    String PASSWORD;
    String CLIENT_ID;
    String CLIENT_SECRET;

    String SUBREDDIT;

    RedditClient reddit;
    AccountManager man;

    Boolean connected;

    Credentials credentials;

    RedditData data;

    public Reddit(CyborgNoodle noodle){
        this.noodle = noodle;
        this.connected = false;
        this.data = new RedditData();

        this.USERNAME = Main.AUTH.getReddit_username();
        this.PASSWORD = Main.AUTH.getReddit_pw();
        this.CLIENT_ID = Main.AUTH.getReddit_client_id();
        this.CLIENT_SECRET = Main.AUTH.getReddit_client_secret();
        this.SUBREDDIT = Main.AUTH.getReddit_sub();
    }

    public void setUp(){

        Log.info("Connecting to reddit...",context);

        UserAgent ua = UserAgent.of("desktop", "io.github.cyborgnoodle", "1.0", USERNAME);
        reddit = new RedditClient(ua);

        credentials = Credentials.script(USERNAME, PASSWORD, CLIENT_ID, CLIENT_SECRET);

        login();
    }

    public void login(){

        Log.info("Logging in to reddit...",context);

        try {
            OAuthData authData = reddit.getOAuthHelper().easyAuth(credentials);
            reddit.authenticate(authData);
            man = new AccountManager(reddit);
            connected = true;
            Log.info("Logged in to reddit.",context);
        } catch (OAuthException e) {
            Log.error("Could not authenticate with Reddit",context);
            Log.stacktrace(e);
            connected = false;

        }
    }

    public void logout(){

        Log.info("Logging out from reddit...",context);

        reddit.getOAuthHelper().revokeAccessToken(credentials);
        reddit.deauthenticate();

        Log.info("Logged out from reddit.",context);

        connected = false;

    }

    public RedditClient getReddit(){
        return reddit;
    }

    public Boolean isConnected(){
        return connected;
    }

    public void postNews(String mediaurl, String caption, long stamp, String author, String instalink, String authorlink){

        Submission sub;

        if(caption==null || caption==""){
            caption = author+" on Instagram";
        }

        try {
            if(!author.equalsIgnoreCase("cyborgnoodlebot")){
                sub = submitLink(SUBREDDIT,mediaurl,caption);
            }
            else sub = submitLink("testingground4bots",mediaurl,caption);

        } catch (ApiException e) {
            if(e.getReason().equalsIgnoreCase("RATELIMIT")){
                Log.warn("Could not post news because of rate limit, scheduling 5 minutes into future",context);
                String finalCaption1 = caption;
                noodle.doLater(() -> noodle.reddit.postNews(mediaurl, finalCaption1, stamp, author, instalink, authorlink),1000*60*5);
                return;
            }
            else {
                Log.error("Reddit Api Exception " + e.getMessage(),context);
                Log.stacktrace(e,context);
                return;
            }

        } catch (MalformedURLException e) {
            Log.error("Reddit URL submitting: URL malformed: "+e.getMessage(),context);
            Log.stacktrace(e,context);
            return;
        } catch (NetworkException e){
            Log.error("NetworkException while reddit posting. Reauthenticating... [try reposting again in 30 secs]",context);
            logout();
            login();
            String finalCaption1 = caption;
            noodle.doLater(() -> noodle.reddit.postNews(mediaurl, finalCaption1, stamp, author, instalink, authorlink),30000);
            return;
        }

        if(sub==null){
            Log.error("Failed to post "+mediaurl+" to reddit! Submission is null!",context);
            return;
        }

        String finalCaption = caption;
        Runnable run = () -> {
            try {
                man.reply(sub.getComments().getComment(),getCommentMessage(stamp,author,instalink,authorlink, finalCaption,sub.getId()));
            } catch (ApiException e) {
                Log.error("Api Exception while making info comment: "+e.getMessage(),context);
                Log.stacktrace(e,context);
            }
        };

        noodle.doLater(run,30000);


    }


    public Submission submitLink(String subreddit, String url, String title) throws MalformedURLException, ApiException, NetworkException {



        Submission s = man.submit(new AccountManager.SubmissionBuilder(new URL(url),subreddit,title));


        return s;


    }

    public String getCommentMessage(long stamp, String author, String instalink, String authorlink, String caption, String subid){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(stamp);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH) + 1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        String s = "Posted on "+mDay+"."+mMonth+"."+mYear+" by **["+author+"]("+authorlink+")** on Instagram\n\n" +
                "Instagram Link: "+instalink+"\n\n";

        HashMap<String,String> mention = getMentioned(caption);

        for(String name : mention.keySet()){
            String url = mention.get(name);
            s = s + "[@"+name+"]("+url+")\n\n";
        }

        s = s+"\n\n*****\n\n" +
                " **[Official Discord]("+CURRENT_LINK+")** ^^^NEW!! | *Post suggestions and bugs you found there.* | " +
                //" | [Report this as not Gorillaz related](http://www.reddit.com/message/compose/?to=cyborgnoodlebot&subject=unrelated&message="+subid+" \"Click here to report this post as not Gorillaz related. It will get a special flair if enough users do this.\") | " +
                "[π](#cynoodsinfo \"generated by CyborgNoodle "+ Meta.getVersion()+" on "+Log.getTimeStamp()+"\")";

        return s;
    }

    public HashMap<String,String> getMentioned(String caption){

        HashMap<String,String> map = new HashMap<>();

        String[] csplit = caption.split(" ");
        for(String cword : csplit){
            if(cword.startsWith("@")){

                String uname = cword.replace("@","");
                String ulink = "https://instagram.com/"+uname+"/";

                if(ulink!=null){
                    map.put(uname,ulink);

                }
            }
        }

        return map;
    }

    public RedditData getData(){
        return data;
    }

    public void setData(RedditData data){
        this.data = data;
    }

}
