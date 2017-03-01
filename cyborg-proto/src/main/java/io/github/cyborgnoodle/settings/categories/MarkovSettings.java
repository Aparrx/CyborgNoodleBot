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
import io.github.cyborgnoodle.settings.data.ServerChannel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * Created by arthur on 25.02.17.
 */
public class MarkovSettings implements SettingsCategory {

    private Settings.Setting proto;
    private CyborgSettings main;

    public ListProperty<ServerChannel> exluded_channels;

    public MarkovSettings(Settings.Setting proto, CyborgSettings main) {
        this.proto = proto;
        this.main = main;

        this.exluded_channels = new SimpleListProperty<>(FXCollections.observableArrayList());

        load(proto);
    }

    @Override
    public Settings.Setting proto() {
        return proto;
    }

    @Override
    public CyborgSettings main() {
        return main;
    }

    @Override
    public SettingsCategory parent() {
        return main;
    }

    @Override
    public void copy(Settings.Setting.Builder builder) {

        for(ServerChannel ch : exluded_channels){
            builder.addMarkovExludedChannels(ch.getPersistentID());
        }

    }

    @Override
    public void load(Settings.Setting setting) {

        exluded_channels.clear();
        for(int id : setting.getMarkovExludedChannelsList()){
            exluded_channels.add(ServerChannel.byPersistentID(id));
        }

    }
}
