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

package io.github.cyborgnoodle.settings;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by arthur on 29.01.17.
 */
public class Settings {

    //Settings

    @Setting(category = "XP")
    private boolean gainxp = true;


    @Setting
    private boolean autoconverter = true;
    @Setting
    private boolean trackstats = true;
    @Setting
    private boolean addemoji = true;

    @Setting(category = "Chat")
    private boolean comment_edits = true;
    @Setting(category = "Chat")
    private boolean commentbadwords = false;
    @Setting(category = "Chat")
    private boolean comment_newuser = true;
    @Setting(category = "Chat")
    private boolean comment_banuser = true;
    @Setting(category = "Chat")
    private boolean comment_leaveuser = true;

    @Setting(category = "News")
    private boolean postnewsdiscord = true;
    @Setting(category = "News")
    private boolean postnewsreddit = true;

    @Setting(category = "XP")
    private int lvl_msgxp_min = 25;
    @Setting(category = "XP")
    private int lvl_msgxp_max = 85;
    @Setting(category = "XP")
    private int lvl_bombxp_min = 300;
    @Setting(category = "XP")
    private int lvl_bombxp_max = 5200;

    @Setting(category = "Currency")
    private double euro_to_usd = 1.07d;

    @Setting(category = "Currency")
    private double usd_to_euro = 0.94d;


    //

    private transient static Settings settings;

    static {
        settings = new Settings();
    }

    public static boolean set(String field, Object value) throws IllegalArgumentException {

        try {
            Field f = Settings.class.getDeclaredField(field);
            if(f.getAnnotation(Setting.class)==null) return false;
            f.setAccessible(true);

            Object e;
            if(f.getType().equals(int.class) || f.getType().equals(Integer.class)) e = ((Number) value).intValue();
            else if(f.getType().equals(long.class) || f.getType().equals(Long.class)) e = ((Number) value).longValue();
            else if(f.getType().equals(double.class) || f.getType().equals(Double.class)) e = ((Number) value).doubleValue();
            else if(f.getType().equals(short.class) || f.getType().equals(Short.class)) e = ((Number) value).shortValue();
            else if(f.getType().equals(float.class) || f.getType().equals(Float.class)) e = ((Number) value).floatValue();
            else e = value;

            f.set(settings,e);
            return true;
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                throw (IllegalArgumentException) e;
            }
            if(e instanceof ClassCastException){
                throw new IllegalArgumentException("Invalid Type!");
            }
            e.printStackTrace();
            return false;
        }

    }

    public static HashMap<String,SettingsEntry> get(){

        HashMap<String,SettingsEntry> m = new HashMap<>();

        for (Field field : Settings.class.getDeclaredFields()) {
            field.setAccessible(true);
            Setting setting = field.getAnnotation(Setting.class);

            if(setting!=null){
                try {
                    Object val = field.get(Settings.settings);
                    String name = field.getName();
                    m.put(name,new SettingsEntry(setting.category(),name,val));
                } catch (IllegalAccessException ignored) {}
            }

        }

        return m;
    }

    public static Settings getSettings(){
        return settings;
    }

    public static void setSettings(Settings set){
        settings = set;
    }

    public static boolean gainxp() {
        return settings.gainxp;
    }

    public static boolean commentbadwords() {
        return settings.commentbadwords;
    }

    public static boolean autoconverter() {
        return settings.autoconverter;
    }

    public static boolean trackstats() {
        return settings.trackstats;
    }

    public static boolean addemoji() {
        return settings.addemoji;
    }

    public static boolean postnewsdiscord() {
        return settings.postnewsdiscord;
    }

    public static boolean postnewsreddit() {
        return settings.postnewsreddit;
    }

    public static int lvl_msgxp_min() {
        return settings.lvl_msgxp_min;
    }

    public static int lvl_msgxp_max() {
        return settings.lvl_msgxp_max;
    }

    public static int lvl_bombxp_min() {
        return settings.lvl_bombxp_min;
    }

    public static int lvl_bombxp_max() {
        return settings.lvl_bombxp_max;
    }

    public static double euro_to_usd() {
        return settings.euro_to_usd;
    }

    public static double usd_to_euro() {
        return settings.usd_to_euro;
    }

    public static boolean comment_edits() {
        return settings.comment_edits;
    }

    public static boolean comment_newuser() {
        return settings.comment_newuser;
    }

    public static boolean comment_banuser() {
        return settings.comment_banuser;
    }

    public static boolean comment_leaveuser() {
        return settings.comment_leaveuser;
    }
}
