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

package io.github.cyborgnoodle.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by arthur on 01.03.17.
 */
public abstract class GSONSaveFile<T> extends SaveFile<T>{

    private static Charset CHARSET = StandardCharsets.ISO_8859_1;
    private Gson gson;

    public GSONSaveFile(Class<T> type, boolean compression) {
        super(type, compression);
        this.gson = new GsonBuilder().enableComplexMapKeySerialization().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
                .setPrettyPrinting().create();
    }

    public T loadUncompressed(byte[] bytes) throws SaveFile.SaveException {

        String json = new String(bytes,CHARSET);
        if(json.isEmpty() || json.equals("")) return defaultObject();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw new SaveFile.SaveException(e);
        }

    }

    public byte[] saveUncompressed(T object) throws SaveFile.SaveException {
        String json = gson.toJson(object, type);
        return json.getBytes(CHARSET);
    }

    public String saveString(T object) throws SaveException {
        return new String(save(object),CHARSET);
    }

    public T loadString(String str) throws SaveException {
        return load(str.getBytes(CHARSET));
    }

    public String saveString64(T object) throws SaveException {
        return new BASE64Encoder().encode(save(object));
    }

    public T loadString64(String str) throws SaveException {
        try {
            return load(new BASE64Decoder().decodeBuffer(str));
        } catch (IOException e) {
            throw new SaveException(e);
        }
    }

    public String saveInternalString() throws SaveException {
        return saveString(onSave());
    }

    public String saveInternalString64() throws SaveException {
        return saveString64(onSave());
    }

    public void loadInternalString(String str) throws SaveException {
        onLoad(loadString(str));
    }

    public void loadInternalString64(String str) throws SaveException {
        onLoad(loadString64(str));
    }

}
