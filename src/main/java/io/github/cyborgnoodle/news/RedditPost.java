package io.github.cyborgnoodle.news;

/**
 *
 */
public class RedditPost {

    String id;

    int unrelated;

    public RedditPost(String id){
        this.id = id;
        this.unrelated = 0;
    }

    public String getID(){
        return id;
    }

    public int getUnrelatedVotes(){
        return unrelated;
    }

    public void setUnrelatedVotes(int votes){
        this.unrelated = votes;
    }

    public void addUnrelatedVote(){
        this.unrelated++;
    }

}
