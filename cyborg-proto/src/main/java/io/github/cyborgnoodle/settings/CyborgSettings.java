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

package io.github.cyborgnoodle.settings;

import io.github.cyborgnoodle.Settings;
import io.github.cyborgnoodle.settings.categories.AutoConverterSettings;
import io.github.cyborgnoodle.settings.categories.ChatSettings;
import io.github.cyborgnoodle.settings.categories.MarkovSettings;
import io.github.cyborgnoodle.settings.categories.XPSettings;

/**
 * Created by arthur on 24.02.17.
 */
public class CyborgSettings implements SettingsCategory {

    private Settings.Setting proto;

    public XPSettings xp;
    public ChatSettings chat;
    public AutoConverterSettings autoconv;
    public MarkovSettings markov;

    public CyborgSettings(Settings.Setting proto){
        this.proto = proto;

        this.xp = new XPSettings(proto,this);
        this.chat = new ChatSettings(proto,this);
        this.autoconv = new AutoConverterSettings(proto,this);
        this.markov = new MarkovSettings(proto,this);

        load(proto);
    }

    public Settings.Setting proto() {
        return proto;
    }

    public CyborgSettings main() {
        return this;
    }

    public SettingsCategory parent() {
        return this;
    }

    public void copy(Settings.Setting.Builder builder) {
        xp.copy(builder);
        chat.copy(builder);
        autoconv.copy(builder);
        markov.copy(builder);
    }

    @Override
    public void load(Settings.Setting setting) {
        xp.load(setting);
        chat.load(setting);
        autoconv.load(setting);
        markov.load(setting);
    }

    public void update(CyborgSettings other){
        Settings.Setting.Builder builder = Settings.Setting.newBuilder();
        other.copy(builder);
        load(builder.build());
    }
}
