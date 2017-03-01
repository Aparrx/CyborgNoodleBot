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
