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

import de.btobastian.javacord.entities.User;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.settings.data.ServerUser;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by arthur on 16.01.17.
 */
public class TimeUtils {

    public static String getSingleUserTime(CyborgNoodle noodle, ServerUser su){

        User user = noodle.getUser(su);
        String name = user.getNickname(noodle.getServer());
        if(name==null) name = user.getName();
        if(name==null) name = "UNKNOWN";
        name = StringUtils.ellipsize(name,29);
        String zname = su.getZone().getDisplayName(Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance(su.getZone());
        Integer hour = calendar.get(Calendar.HOUR);
        Integer minute = calendar.get(Calendar.MINUTE);
        Integer ampm = calendar.get(Calendar.AM_PM);

        int nlength = name.length();
        String filler = StringUtils.getWhitespaces(30-nlength);

        String suffix;
        if(ampm == Calendar.AM) suffix = "AM";
        else suffix = "PM";

        String sh;
        if(hour>9) sh = hour.toString();
        else sh = "0"+hour.toString();

        String sm;
        if(minute>9) sm = minute.toString();
        else sm = "0"+minute.toString();

        return name+filler+sh+":"+sm+" "+suffix+"     ["+zname+"]\n";
    }

}
