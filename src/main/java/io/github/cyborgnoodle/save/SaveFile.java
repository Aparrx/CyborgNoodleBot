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

import io.github.cyborgnoodle.util.Compression;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * Created by arthur on 17.02.17.
 */
public abstract class SaveFile<T> {

    Class<T> type;
    boolean compression;

    public SaveFile(Class<T> type, boolean compression){
        this.type = type;
        this.compression = compression;
    }

    public boolean isCompressed() {
        return compression;
    }

    public abstract T loadUncompressed(byte[] bytes) throws SaveException;

    public abstract byte[] saveUncompressed(T object) throws SaveException;

    public T load(byte[] bytes) throws SaveException {
        if(compression){
            try {
                bytes = Compression.decompress(bytes);
            } catch (Compression.CompressionException e) {
                throw new SaveFile.SaveException(e);
            }
        }
        return loadUncompressed(bytes);
    }

    public byte[] save(T object) throws SaveException {
        byte[] bytes = saveUncompressed(object);
        if(compression){
            try {
                bytes = Compression.compress(bytes);
            } catch (Compression.CompressionException e) {
                throw new SaveFile.SaveException(e);
            }
        }

        return bytes;
    }

    public byte[] saveInternal() throws SaveException {
        return save(onSave());
    }

    public void loadInternal(byte[] bytes) throws SaveException {
        onLoad(load(bytes));
    }

    public abstract String saveString(T object) throws SaveException;

    public abstract T loadString(String str) throws SaveException;

    public String saveInternalString() throws SaveException {
        return saveString(onSave());
    }

    public void loadInternalString(String str) throws SaveException {
        onLoad(loadString(str));
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

    public abstract T defaultObject();

    public abstract T onSave();

    public abstract void onLoad(T object);

    public static class SaveException extends Exception {
        public SaveException(Throwable cause) {
            super(cause);
        }
    }

}
