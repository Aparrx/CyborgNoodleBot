package io.github.cyborgnoodle.news;

import java.util.HashMap;

/**
 *
 */
public class RedditData {

    HashMap<String,RedditPost> tracked;

    public RedditData(){
        tracked = new HashMap<>();
    }

    public RedditPost getPost(String id){
        return tracked.get(id);
    }

    public void setPost(RedditPost post){
        tracked.put(post.getID(),post);
    }

}
