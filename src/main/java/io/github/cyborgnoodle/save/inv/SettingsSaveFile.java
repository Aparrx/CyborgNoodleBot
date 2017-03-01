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

package io.github.cyborgnoodle.save.inv;

import com.google.protobuf.InvalidProtocolBufferException;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Settings;
import io.github.cyborgnoodle.save.SaveFile;
import io.github.cyborgnoodle.settings.CyborgSettings;

/**
 * Created by arthur on 17.02.17.
 */
public class SettingsSaveFile extends SaveFile<CyborgSettings> {

    private CyborgNoodle noodle;

    public SettingsSaveFile(CyborgNoodle noodle) {
        super(CyborgSettings.class, false);
        this.noodle = noodle;
    }

    @Override
    public CyborgSettings loadUncompressed(byte[] bytes) throws SaveException {

        Settings.Setting setting;
        try {
            setting = Settings.Setting.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            throw new SaveException(e);
        }

        return new CyborgSettings(setting);

    }

    @Override
    public byte[] saveUncompressed(CyborgSettings object) throws SaveException {
        Settings.Setting.Builder builder = Settings.Setting.newBuilder();
        object.copy(builder);
        return builder.build().toByteArray();
    }

    @Override
    public String saveString(CyborgSettings object) throws SaveException {
        return saveString64(object);
    }

    @Override
    public CyborgSettings loadString(String str) throws SaveException {
        return loadString64(str);
    }

    @Override
    public CyborgSettings defaultObject() {
        return new CyborgSettings(Settings.Setting.getDefaultInstance());
    }

    @Override
    public CyborgSettings onSave() {
        return noodle.settings;
    }

    @Override
    public void onLoad(CyborgSettings object) {
        noodle.settings.update(object);
    }
}
