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
import io.github.cyborgnoodle.server.ServerChannel;
import me.postaddict.instagramscraper.Instagram;
import me.postaddict.instagramscraper.exception.InstagramException;
import me.postaddict.instagramscraper.model.Media;

import java.io.IOException;

/**
 * Created by arthur on 29.10.16.
 */
public class InstagramTool {

    CyborgNoodle noodle;

    public InstagramTool(CyborgNoodle noods){
        this.noodle = noods;
    }

    public void listPosts(String user) throws IOException, InstagramException {



    }

}
