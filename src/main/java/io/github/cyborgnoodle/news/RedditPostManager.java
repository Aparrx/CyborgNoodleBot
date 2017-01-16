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
import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.managers.AccountManager;
import net.dean.jraw.managers.InboxManager;
import net.dean.jraw.managers.ModerationManager;
import net.dean.jraw.models.FlairTemplate;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Message;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.InboxPaginator;
import net.dean.jraw.paginators.Sorting;

import java.util.Iterator;
import java.util.List;

/**
 * Created by arthur on 16.11.16.
 */
public class RedditPostManager {


    RedditClient reddit;
    CyborgNoodle noodle;

    public RedditPostManager(RedditClient client, CyborgNoodle noodle){
        this.reddit = client;
        this.noodle = noodle;
    }

    public void doCheck(){

        if(!noodle.getReddit().isConnected()) return;

        try {
            checkMessages();
        } catch (Exception e) {
            if(e instanceof NetworkException){
                Log.error("NetworkException while reddit message checking... Reauthenticating...");
                e.printStackTrace();
                noodle.getReddit().logout();
                noodle.getReddit().login();
            }

        }
    }

    public void checkMessages() throws Exception{

        InboxPaginator inbox = new InboxPaginator(reddit,"inbox");

        inbox.setLimit(10);
        inbox.setSorting(Sorting.NEW);

        Iterator<Listing<Message>> it = inbox.iterator();

        Listing<Message> listing = it.next();

        for(Message m : listing){
            if(!m.isComment() && !m.isRead()){
                Log.info("new inbox item");
                String subj = m.getSubject();
                InboxManager man = new InboxManager(reddit);
                man.setRead(true,m);
                if(subj.equalsIgnoreCase("unrelated")){
                    String id = m.getBody();
                    if(!id.isEmpty() && id!=null){
                        Submission sub = reddit.getSubmission(id);
                        if(sub!=null){
                            Log.info("Got unrelated notice for "+id);
                            RedditPost post = noodle.getReddit().getData().getPost(id);

                            if(post==null){
                                post = new RedditPost(id);
                            }

                            post.addUnrelatedVote();
                            updateFlair(sub,post);

                            noodle.getReddit().getData().setPost(post);
                        }
                        else Log.warn("Got unrelated report, but Submission is null!");
                    }
                    else Log.warn("Got unrelated report, but ID is empty!");

                }
                else Log.info("NEW PM ["+m.getAuthor()+"/"+m.getSubject()+"]: "+m.getBody()+"\n");
            }
        }
    }

    public void updateFlair(Submission sub, RedditPost post) throws Exception{
        int redditvotes = sub.getScore();
        int unrelatedvotes = post.getUnrelatedVotes();

        if(redditvotes==0){
            if(unrelatedvotes>2){
                setFlair(sub);
            }
            else removeFlair(sub);
        }
        else {
            Double rad = (double)unrelatedvotes/(double)redditvotes;
            if(rad>0.1){
                setFlair(sub);
            }
            else removeFlair(sub);
        }


    }

    public void setFlair(Submission sub) throws Exception{

        Log.warn("Tried to add flair to "+sub.getId()+" but it's currently not supported.");

    }

    public void removeFlair(Submission sub) throws Exception{
        Log.warn("Tried to remove flair from "+sub.getId()+" but it's currently not supported.");
    }


}
