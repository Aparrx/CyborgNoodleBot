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

package io.github.cyborgnoodle.util;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Created by arthur on 16.02.17.
 */
public class Compression {

    public static byte[] decompress(byte[] bytes) throws CompressionException{

        InputStream in = new InflaterInputStream(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[8192];
            int len;
            while((len = in.read(buffer))>0)
                baos.write(buffer, 0, len);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new CompressionException(e);
        }

    }

    public static byte[] compress(byte[] data) throws CompressionException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            OutputStream out = new DeflaterOutputStream(baos);
            out.write(data);
            out.close();
        } catch (IOException e) {
            throw new CompressionException(e);
        }
        return baos.toByteArray();

    }

    public static class CompressionException extends Exception {
        public CompressionException(Throwable cause) {
            super(cause);
        }
    }

}
