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

package io.github.cyborgnoodle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.cyborgnoodle.levels.LevelRegistry;
import io.github.cyborgnoodle.misc.ReactionWords;
import io.github.cyborgnoodle.misc.WordStatsData;
import io.github.cyborgnoodle.misc.funtance.DataCollection;
import io.github.cyborgnoodle.news.InstagramRegistry;
import io.github.cyborgnoodle.news.RedditData;
import io.github.cyborgnoodle.settings.Settings;
import io.github.cyborgnoodle.statistics.Statistics;
import io.github.cyborgnoodle.statistics.data.StatisticsData;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by arthur on 16.10.16.
 */
public class SaveManager {

    CyborgNoodle noodle;

    private static String CHARSET = "UTF-8";

    public SaveManager(CyborgNoodle noods){
        this.noodle = noods;
    }

    public void saveAll(){

        Log.info("saving data...");

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
                .setPrettyPrinting().create();

        String json = gson.toJson(noodle.getLevels().toLevelRegistry());
        write(getConfigFile(ConfigFile.LEVELS),json);
        String instajson = gson.toJson(noodle.getInstaRegistry());
        write(getConfigFile(ConfigFile.GENERAL),instajson);

        String redditjson = gson.toJson(noodle.getReddit().getData());
        write(getConfigFile(ConfigFile.REDDIT),redditjson);

        String wordjson = gson.toJson(noodle.getWordStats().getData());
        write(getConfigFile(ConfigFile.WORDS),wordjson);

        DataCollection dc = new DataCollection();
        dc.pull();
        String dcjson = gson.toJson(dc);
        write(getConfigFile(ConfigFile.FUNTANCE),dcjson);

        StatisticsData statdat = Statistics.save();
        String stjson = gson.toJson(statdat);
        write(getConfigFile(ConfigFile.STATS),stjson);

        Settings set = Settings.getSettings();
        String setson = gson.toJson(set);
        write(getConfigFile(ConfigFile.SETTINGS),setson);

        ReactionWords words = ReactionWords.getWords();
        String wjson = gson.toJson(words);
        write(getConfigFile(ConfigFile.REACTIONS),wjson);

    }

    public void loadAll(){

        Log.info("loading data...");

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
                .setPrettyPrinting().create();

        String regcont = read(getConfigFile(ConfigFile.LEVELS));
        if(!regcont.isEmpty()) {
            LevelRegistry reg = gson.fromJson(regcont, LevelRegistry.class);
            noodle.getLevels().setRegistry(reg);
        }

        String instaregcont = read(getConfigFile(ConfigFile.GENERAL));
        if(!regcont.isEmpty()) {
            InstagramRegistry reg = gson.fromJson(instaregcont, InstagramRegistry.class);
            noodle.setInstaRegistry(reg);
        }
        else{
            noodle.setInstaRegistry(new InstagramRegistry());
        }

        String redditcont = read(getConfigFile(ConfigFile.REDDIT));
        if(!redditcont.isEmpty()) {
            RedditData data = gson.fromJson(redditcont, RedditData.class);
            noodle.getReddit().setData(data);
        }
        else{
            noodle.getReddit().setData(new RedditData());
        }

        String wordscont = read(getConfigFile(ConfigFile.WORDS));
        if(!wordscont.isEmpty()) {
            WordStatsData data = gson.fromJson(wordscont,WordStatsData.class);
            noodle.getWordStats().setData(data);
        }
        else{
            noodle.getWordStats().setData(new WordStatsData());
        }

        DataCollection collection;
        String sgcont = read(getConfigFile(ConfigFile.FUNTANCE));
        if(!sgcont.isEmpty()) {
            collection = gson.fromJson(sgcont,DataCollection.class);
        }
        else {
            collection = new DataCollection();
            collection.pull();
        }

        collection.push();

        String statscont = read(getConfigFile(ConfigFile.STATS));
        if(!statscont.isEmpty()) {
            StatisticsData data = gson.fromJson(statscont,StatisticsData.class);
            Statistics.load(data);
        }
        else{
            Statistics.load(new StatisticsData());
        }

        String sjson = read(getConfigFile(ConfigFile.SETTINGS));
        if(!sjson.isEmpty()) {
            Settings settings = gson.fromJson(sjson, Settings.class);
            Settings.setSettings(settings);
        }

        String rjson = read(getConfigFile(ConfigFile.REACTIONS));
        if(!sjson.isEmpty()) {
            ReactionWords words = gson.fromJson(rjson, ReactionWords.class);
            ReactionWords.setWords(words);
        }

    }

    // UTILITY

    public File getConfigFile(ConfigFile file){
        String conf = getConfigDir().toString();
        String f;
        switch (file){
            case GENERAL:
                f = "general.json";
                break;
            case LEVELS:
                f = "levels.json";
                break;
            case REDDIT:
                f = "reddit.json";
                break;
            case WORDS:
                f = "words.json";
                break;
            case FUNTANCE:
                f = "sentencegen.json";
                break;
            case STATS:
                f = "statistics.json";
                break;
            case SETTINGS:
                f = "settings.json";
                break;
            case AUTH:
                f = "auth.json";
                break;
            case REACTIONS:
                f = "reactions.json";
                break;
            default:
                throw new NullPointerException("ConfigFile type can not be null or of other type!");
        }

        File cf = new File(conf+File.separator+f);
        return cf;
    }

    public File getJarDirectory() throws URISyntaxException {
        File jardir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        return jardir;
    }

    public File getConfigDir(){
        try {
            File f = getJarDirectory();
            return f;
        } catch (Exception e) {
            Log.error("Could not find out where jar is located! FATAL ERROR!");
            e.printStackTrace();
            return null;
        }
    }

    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public String read(File f){
        try {
            if(!f.exists()){
                f.createNewFile();
                return "";
            }
            else return readFile(f.toString(),Charset.forName(CHARSET));

        } catch (IOException e) {
            Log.error("Could not read from file! FATAL ERROR!");
            e.printStackTrace();
            return "";
        }
    }

    public void write(File f, String content){
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), Charset.forName(CHARSET)));
            if(!f.exists()) f.createNewFile();
            try{
                out.write(content);
            }
            finally {
                out.close();
            }
        } catch (Exception e) {
            Log.error("Could not write to file! FATAL ERROR!");
            e.printStackTrace();
        }

    }

    public enum ConfigFile{
        GENERAL, LEVELS, REDDIT, WORDS, FUNTANCE, STATS, SETTINGS, AUTH, REACTIONS
    }

}
