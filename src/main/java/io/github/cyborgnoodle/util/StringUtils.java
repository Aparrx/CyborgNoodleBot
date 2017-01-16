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

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by arthur on 16.01.17.
 */
public class StringUtils {

    public static String getWhitespaces(int length){
        String s = "";
        while (length>0){
            length--;
            s = s + " ";
        }
        return s;
    }

    public static String ellipsize(String input, int maxLength) {
        if (input == null || input.length() <= maxLength) {
            return input;
        }
        return input.substring(0, maxLength-3) + "...";
    }

    public static String removeEmojiAndSymbol(String content) {
        String utf8tweet = "";
        try {
            byte[] utf8Bytes = content.getBytes("UTF-8");

            utf8tweet = new String(utf8Bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Pattern unicodeOutliers = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                                Pattern.UNICODE_CASE |
                                Pattern.CANON_EQ |
                                Pattern.CASE_INSENSITIVE);
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(utf8tweet);

        utf8tweet = unicodeOutlierMatcher.replaceAll("");
        return utf8tweet;
    }

    public static String getVisualisation(long of, long full){

        double zc = (double) of/full;
        double ta = zc*10;


        String s = "[";
        int i = 1;

        while (i<=10){
            if(ta>i) s = s + "█";
            else s = s + " ";
            i++;
        }

        s = s + "] "+(ta*10)+"%";

        return s;

    }

    public static String toCommaList(String[] name){

        if (name.length > 0) {
            StringBuilder nameBuilder = new StringBuilder();

            for (String n : name) {
                nameBuilder.append("'").append(n.replace("'", "\\'")).append("',");
                // can also do the following
                // nameBuilder.append("'").append(n.replace("'", "''")).append("',");
            }

            nameBuilder.deleteCharAt(nameBuilder.length() - 1);

            return nameBuilder.toString();
        } else {
            return "";
        }

    }
}
