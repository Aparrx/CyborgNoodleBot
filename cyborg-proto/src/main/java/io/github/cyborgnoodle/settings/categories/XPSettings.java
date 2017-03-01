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
public class XPSettings implements SettingsCategory {

    private Settings.Setting proto;
    private CyborgSettings main;

    public BooleanProperty gain;

    public IntegerProperty msg_max;
    public IntegerProperty msg_min;
    public IntegerProperty bomb_max;
    public IntegerProperty bomb_min;
    public IntegerProperty bomb_timeout_max;
    public IntegerProperty bomb_timeout_min;

    public XPSettings(Settings.Setting proto, CyborgSettings main){
        this.proto = proto;
        this.main = main;

        this.gain = new SimpleBooleanProperty(true);
        this.msg_max = new SimpleIntegerProperty(85);
        this.msg_min = new SimpleIntegerProperty(25);
        this.bomb_max = new SimpleIntegerProperty(8500);
        this.bomb_min = new SimpleIntegerProperty(1000);
        this.bomb_timeout_max = new SimpleIntegerProperty(720);
        this.bomb_timeout_min = new SimpleIntegerProperty(60);

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
        builder.setXpGain(gain.getValue());
        builder.setXpMsgMax(msg_max.getValue());
        builder.setXpMsgMin(msg_min.getValue());
        builder.setXpBombMax(bomb_max.getValue());
        builder.setXpBombMin(bomb_min.getValue());
        builder.setXpBombTimeoutMax(bomb_timeout_max.getValue());
        builder.setXpBombTimeoutMin(bomb_timeout_min.getValue());
    }

    @Override
    public void load(Settings.Setting setting) {
        gain.set(setting.getXpGain());

        msg_max.set(setting.getXpMsgMax());
        msg_min.set(setting.getXpMsgMin());

        bomb_max.set(setting.getXpBombMax());
        bomb_min.set(setting.getXpBombMin());

        bomb_timeout_max.set(setting.getXpBombTimeoutMax());
        bomb_timeout_min.set(setting.getXpBombTimeoutMin());
    }
}
