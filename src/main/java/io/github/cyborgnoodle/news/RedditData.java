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
