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

package io.github.cyborgnoodle.settings.util;

import io.github.cyborgnoodle.Settings;
import io.github.cyborgnoodle.settings.CyborgSettings;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * This class is used for generating the code used for updating the cyborg settings from external applications.
 */
public class SettingsCode {

    public static CyborgSettings parse(String code) throws IOException {

        byte[] codedata = new BASE64Decoder().decodeBuffer(code);

        Settings.Setting protosetting = Settings.Setting.parseFrom(codedata);
        return new CyborgSettings(protosetting);
    }

    public static String create(CyborgSettings settings){

        Settings.Setting.Builder builder = Settings.Setting.newBuilder();
        settings.copy(builder);
        Settings.Setting protosetting = builder.build();
        byte[] codedata = protosetting.toByteArray();

        return new BASE64Encoder().encode(codedata);

    }

}
