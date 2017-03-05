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

import io.github.cyborgnoodle.save.SaveFile;
import io.github.cyborgnoodle.save.inv.*;
import io.github.cyborgnoodle.util.Log;
import javafx.util.Pair;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by arthur on 16.10.16.
 */
public class SaveManager {

    public static final Log.LogContext context = new Log.LogContext("SAVE");

    CyborgNoodle noodle;

    private static Charset CHARSET = StandardCharsets.ISO_8859_1;

    private HashMap<String,Pair<SaveFile,ConfigFile>> savefiles;

    public SaveManager(CyborgNoodle noods){
        this.noodle = noods;
        this.savefiles = new HashMap<>();

        savefiles.put("levels",new Pair<>(new LevelSaveFile(noodle),ConfigFile.LEVELS));
        savefiles.put("general",new Pair<>(new InstagramSaveFile(noodle),ConfigFile.GENERAL));
        savefiles.put("words",new Pair<>(new WordStatsSaveFile(noodle),ConfigFile.WORDS));
        savefiles.put("funwords",new Pair<>(new FuntanceSaveFile(),ConfigFile.FUNTANCE));
        savefiles.put("stats",new Pair<>(new StatisticsDataSaveFile(noodle),ConfigFile.STATS));
        savefiles.put("reactions",new Pair<>(new ReactionWordsSaveFile(),ConfigFile.REACTIONS));
        savefiles.put("markov",new Pair<>(new MarkovSaveFile(),ConfigFile.MARKOV));
        savefiles.put("settings",new Pair<>(new SettingsSaveFile(noodle),ConfigFile.SETTINGS));
    }

    public HashMap<String, Pair<SaveFile, ConfigFile>> getSavefiles() {
        return savefiles;
    }

    public boolean saveAll(){

        boolean errorfree = true;

        Log.info("Saving data...",context);

        for(String name : savefiles.keySet()){
            Pair<SaveFile, ConfigFile> entry = savefiles.get(name);
            ConfigFile file = entry.getValue();
            SaveFile save = entry.getKey();
            Log.info("Saving "+name+" ...",context);
            try {
                String data = save.saveInternalString();
                write(file.getConfigFile(),data);
            } catch (SaveFile.SaveException e) {
                Log.error("FAILED TO SAVE "+name,context);
                Log.stacktrace(e,context);
                errorfree = false;
            }
        }

        return errorfree;

    }

    public boolean loadAll(){

        boolean errorfree = true;

        Log.info("loading data...",context);

        for(String name : savefiles.keySet()){
            Pair<SaveFile, ConfigFile> entry = savefiles.get(name);
            ConfigFile file = entry.getValue();
            SaveFile save = entry.getKey();
            Log.info("Loading "+name+" ...",context);
            try {
                String data = read(file.getConfigFile());
                save.loadInternalString(data);
            } catch (SaveFile.SaveException e) {
                Log.error("FAILED TO LOAD "+name,context);
                Log.stacktrace(e,context);
                errorfree = false;
            }
        }

        return errorfree;
    }

    // UTILITY

    public static File getJarDirectory() throws URISyntaxException {
        File jardir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        return jardir;
    }

    public static File getConfigDir(){
        try {
            File f = getJarDirectory();
            return f;
        } catch (Exception e) {
            Log.error("Could not find out where jar is located! FATAL ERROR!",context);
            Log.stacktrace(e,context);
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
            else return readFile(f.toString(),CHARSET);

        } catch (IOException e) {
            Log.error("Could not read from file! FATAL ERROR!",context);
            Log.stacktrace(e,context);
            return "";
        }
    }

    public void write(File f, String content){
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), CHARSET));
            if(!f.exists()) f.createNewFile();
            try{
                out.write(content);
            }
            finally {
                out.close();
            }
        } catch (Exception e) {
            Log.error("Could not write to file! FATAL ERROR!",context);
            Log.stacktrace(e,context);
        }

    }

    public enum ConfigFile{
        GENERAL("general.json"),
        LEVELS("levels.json"),
        REDDIT("reddit.json"),
        WORDS("words.json"),
        FUNTANCE("sentencegen.json"),
        STATS("statistics.json"),
        SETTINGS("settings.json"),
        AUTH("auth.json"),
        REACTIONS("reactions.json"),
        MARKOV("markov.json"),
        ;

        private String f;

        ConfigFile(String f){
            this.f = f;
        }

        public File getConfigFile(){
            return new File(getConfigDir()+File.separator+f);
        }

    }

}
