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

package io.github.cyborgnoodle.settings.categories;

import io.github.cyborgnoodle.Settings;
import io.github.cyborgnoodle.settings.CyborgSettings;
import io.github.cyborgnoodle.settings.SettingsCategory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by arthur on 24.02.17.
 */
public class ChatSettings implements SettingsCategory {

    private CyborgSettings main;
    private Settings.Setting proto;

    public BooleanProperty comment_edits;
    public BooleanProperty comment_badwords;
    public BooleanProperty comment_newuser;
    public BooleanProperty comment_banuser;
    public BooleanProperty comment_leaveuser;

    public IntegerProperty comment_edits_chance;

    public ChatSettings(Settings.Setting proto, CyborgSettings settings){
        this.proto = proto;
        this.main = settings;

        this.comment_edits = new SimpleBooleanProperty(false);
        this.comment_badwords = new SimpleBooleanProperty(false);
        this.comment_newuser = new SimpleBooleanProperty(true);
        this.comment_banuser = new SimpleBooleanProperty(true);
        this.comment_leaveuser = new SimpleBooleanProperty(true);

        this.comment_edits_chance = new SimpleIntegerProperty(20);

        load(proto);

    }

    public Settings.Setting proto() {
        return proto;
    }

    public CyborgSettings main() {
        return main;
    }

    public SettingsCategory parent() {
        return main;
    }

    public void copy(Settings.Setting.Builder builder) {
        builder.setChatCommentEdits(comment_edits.getValue());
        builder.setChatCommentBadwords(comment_badwords.getValue());
        builder.setChatCommentNewuser(comment_newuser.getValue());
        builder.setChatCommentBanuser(comment_banuser.getValue());
        builder.setChatCommentLeaveuser(comment_leaveuser.getValue());

        builder.setChatCommentEditsChance(comment_edits_chance.getValue());
    }

    @Override
    public void load(Settings.Setting setting) {

        comment_edits.set(setting.getChatCommentEdits());
        comment_badwords.set(setting.getChatCommentBadwords());
        comment_newuser.set(setting.getChatCommentNewuser());
        comment_banuser.set(setting.getChatCommentBanuser());
        comment_leaveuser.set(setting.getChatCommentLeaveuser());

        comment_edits_chance.set(setting.getChatCommentEditsChance());

    }
}
