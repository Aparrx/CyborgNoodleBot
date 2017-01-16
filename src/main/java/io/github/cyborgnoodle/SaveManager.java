package io.github.cyborgnoodle;

import com.google.gson.Gson;
import io.github.cyborgnoodle.levels.LevelRegistry;
import io.github.cyborgnoodle.misc.WordStatsData;
import io.github.cyborgnoodle.news.InstagramRegistry;
import io.github.cyborgnoodle.news.RedditData;

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

        Gson gson = new Gson();

        String json = gson.toJson(noodle.getLevels().getRegistry());
        write(getConfigFile(ConfigFile.LEVELS),json);
        String instajson = gson.toJson(noodle.getInstaRegistry());
        write(getConfigFile(ConfigFile.GENERAL),instajson);

        String redditjson = gson.toJson(noodle.getReddit().getData());
        write(getConfigFile(ConfigFile.REDDIT),redditjson);

        String wordjson = gson.toJson(noodle.getWordStats().getData());
        write(getConfigFile(ConfigFile.WORDS),wordjson);


    }

    public void loadAll(){

        Log.info("loading data...");

        Gson gson = new Gson();

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

    private String read(File f){
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

    private void write(File f, String content){
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
        GENERAL, LEVELS, REDDIT, WORDS
    }

}
