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

package io.github.cyborgnoodle.auth;

/**
 * Created by arthur on 30.01.17.
 */
public class Authentication {

    private String discordtoken;
    private String reddit_username;
    private String reddit_pw;
    private String reddit_client_id;
    private String reddit_client_secret;

    private String reddit_sub;

    public String getDiscordtoken() {
        return discordtoken;
    }

    public void setDiscordtoken(String discordtoken) {
        this.discordtoken = discordtoken;
    }

    public String getReddit_username() {
        return reddit_username;
    }

    public void setReddit_username(String reddit_username) {
        this.reddit_username = reddit_username;
    }

    public String getReddit_pw() {
        return reddit_pw;
    }

    public void setReddit_pw(String reddit_pw) {
        this.reddit_pw = reddit_pw;
    }

    public String getReddit_client_id() {
        return reddit_client_id;
    }

    public void setReddit_client_id(String reddit_client_id) {
        this.reddit_client_id = reddit_client_id;
    }

    public String getReddit_client_secret() {
        return reddit_client_secret;
    }

    public void setReddit_client_secret(String reddit_client_secret) {
        this.reddit_client_secret = reddit_client_secret;
    }

    public String getReddit_sub() {
        return reddit_sub;
    }

    public void setReddit_sub(String reddit_sub) {
        this.reddit_sub = reddit_sub;
    }
}
