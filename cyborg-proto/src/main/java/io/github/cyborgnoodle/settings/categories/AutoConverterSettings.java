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
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 */
public class AutoConverterSettings implements SettingsCategory {

    private Settings.Setting proto;
    private CyborgSettings main;

    public BooleanProperty enable;

    public BooleanProperty time;
    public BooleanProperty temperature;
    public BooleanProperty length;
    public BooleanProperty area;
    public BooleanProperty volume;
    public BooleanProperty mass;
    public BooleanProperty currency;

    public AutoConverterSettings(Settings.Setting proto, CyborgSettings main){
        this.proto = proto;
        this.main = main;

        this.enable = new SimpleBooleanProperty(true);

        this.time = new SimpleBooleanProperty(true);
        this.temperature = new SimpleBooleanProperty(true);
        this.length = new SimpleBooleanProperty(true);
        this.area = new SimpleBooleanProperty(true);
        this.volume = new SimpleBooleanProperty(true);
        this.mass = new SimpleBooleanProperty(true);
        this.currency = new SimpleBooleanProperty(true);

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
        builder.setAutoconvEnable(enable.getValue());

        builder.setAutoconvTime(time.getValue());
        builder.setAutoconvTemperature(temperature.getValue());
        builder.setAutoconvLength(length.getValue());
        builder.setAutoconvArea(area.getValue());
        builder.setAutoconvVolume(volume.getValue());
        builder.setAutoconvMass(mass.getValue());
        builder.setAutoconvCurrency(currency.getValue());
    }

    @Override
    public void load(Settings.Setting setting) {
        enable.set(setting.getAutoconvEnable());

        time.set(setting.getAutoconvTime());
        temperature.set(setting.getAutoconvTemperature());
        length.set(setting.getAutoconvLength());
        area.set(setting.getAutoconvArea());
        volume.set(setting.getAutoconvVolume());
        mass.set(setting.getAutoconvMass());
        currency.set(setting.getAutoconvCurrency());
    }
}
