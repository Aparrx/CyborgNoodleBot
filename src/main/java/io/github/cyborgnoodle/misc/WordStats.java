package io.github.cyborgnoodle.misc;

import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.Log;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class WordStats {

    private boolean first;
    WordStatsData data;
    public static String[] EXCEPT = new String[]{"cum","cumcum","it","the","to","you","is","are","not","be","just","how","its","bot","okay","crash","what","why","when",
                                                "also","dont","by","for","this","that","than","there","then","we","us","other","me","yo","own","self","myself","yourself","nah",
                                                "no","yes","tho","though","can","do","real","you","cant","up","down","left","right","ok","okay","kay","of","as",
                                                "and","or",
    "in","my","have","like","on","was","thats","oh","one","wew","wow","im","he","she","so","here","now","make",
    "but","get","with","did","they","will","would","think","all","yeah","lmao","dunno",
    "hey","out","only","from","really","your","youre","time","his","her","say",
    "know","because","even","best","haha","hahaha","ha","hahahaha","hahahahaha","at","if","thanks","some",
    "didnt","him","see","too","well","gonna","going","will","would",
    "word","words","them","nice","these","much","more","few","lot","whole","many","clod",
    "try","said","ya","yo","jo","ur","makes","su","am","hes","shes","hed","shed","hell","shell","theyll","into","had","have","havent","mb","very","where","thought","idk",
    "want","about","list","wait","were","lets","last","first","ill","sorry","name","got","time","times","warn","mean","been","test","use",
    "stop","an","pls","big","bad","good","pez","roy",
    "go","still","does","need","isnt","ive","has",
    "smong","smog"};

    public WordStats(){
        first = true;
        this.data = new WordStatsData();
    }

    public WordStatsData getData() {
        return data;
    }

    public void setData(WordStatsData data) {
        this.data = data;
    }

    public void onMessage(Message message){
        removeOldExceptions();
        String m = message.getContent();
        String[] words = m.split(" ");

        Map<String,Long> counts = new HashMap<>();

        for(String word : words){
            String lwo = BadWords.adjustMsg(word.toLowerCase());

            if(lwo.length()<2) continue;
            if(lwo.length()>30) continue;
            if(Arrays.asList(EXCEPT).contains(lwo)) continue;
            if(lwo.contains("!")) continue;

            Long num = counts.get(lwo);
            if(num==null) counts.put(lwo,1L);
            else{
                counts.put(lwo,num+1L);
            }
        }

        for(String word : counts.keySet()){
            Long count = counts.get(word);
            if(count<6) data.count(word,count,message.getAuthor());
            else Log.warn("didnt count word "+word+" because of spam (word occured "+count+"x)");
        }
    }

    public LinkedHashMap<String,Long> getWordBoard(){

        return sortByValue(strapUserData(data.getEntries()));
    }

    public Map<String,Long> strapUserData(Map<String,WordStatsEntry> entryMap){

        HashMap<String,Long> strapped = new HashMap<>();
        for (Map.Entry<String, WordStatsEntry> entry : entryMap.entrySet()) {
            strapped.put(entry.getKey(),entry.getValue().getCount());
        }

        return strapped;

    }

    public static <String, Long extends Comparable<? super Long>> LinkedHashMap<String, Long> sortByValue(Map<String, Long> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public void removeOldExceptions(){
        if(first){
            Log.info("Executing new exceptions in Word counter ...");
            HashMap<String,Long> map = new HashMap<>(data.getMap());
            for(String word : map.keySet()){
                String cword = BadWords.adjustMsg(word);
                if(Arrays.asList(EXCEPT).contains(cword)){
                    Log.info(" > removing "+word+" ("+map.get(word)+"x)");
                    data.remove(word);
                }
            }
            first = false;
        }

    }
}
