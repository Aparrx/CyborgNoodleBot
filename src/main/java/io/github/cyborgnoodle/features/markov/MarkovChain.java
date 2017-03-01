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

package io.github.cyborgnoodle.features.markov;

import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

/**
 * Created by arthur on 16.02.17.
 */
public class MarkovChain {

    private Hashtable<String, Vector<String>> chain = new Hashtable<>();
    private static volatile String START = "_start";
    private static volatile String END = "_end";
    private static volatile int MIN_SIZE = 10;
    private static volatile int MAX_WORDS = 25;

    public MarkovChain(){
        chain.put(START, new Vector<>());
        chain.put(END, new Vector<>());
    }

    public void addWords(String text){

        String[] words = text.split(" ");

        if(words.length<=2) return;

        for (int i=0; i<words.length; i++) {

            if (i == 0) {
                Vector<String> startWords = chain.get(START);
                startWords.add(words[i]);

                Vector<String> suffix = chain.get(words[i]);
                if (suffix == null) {
                    suffix = new Vector<>();
                    suffix.add(words[i+1]);
                    chain.put(words[i], suffix);
                }

            } else if (i == words.length-1) {
                Vector<String> endWords = chain.get(END);
                endWords.add(words[i]);

            } else {
                Vector<String> suffix = chain.get(words[i]);
                if (suffix == null) {
                    suffix = new Vector<>();
                    suffix.add(words[i+1]);
                    chain.put(words[i], suffix);
                } else {
                    suffix.add(words[i+1]);
                    chain.put(words[i], suffix);
                }
            }

        }

    }

    public Vector<String> generate() {

        Random random = new Random();

        if(!isOfMinSize()) throw new AssertionError("Not enough data to create sentence!");

        // Vector to hold the phrase
        Vector<String> newPhrase = new Vector<>();

        // String for the next word
        String nextWord;

        // Select the first word
        Vector<String> startWords = chain.get(START);
        int startWordsLen = startWords.size();
        nextWord = startWords.get(random.nextInt(startWordsLen));
        newPhrase.add(nextWord);

        char lastchar = nextWord.charAt(nextWord.length() - 1);

        // Keep looping through the words until we've reached the end
        int wordcount = 0;
        while (!nextWord.endsWith("\\.") && !nextWord.endsWith("?") && !nextWord.endsWith("!")) {

            wordcount++;

            if(wordcount>MAX_WORDS){
                break;
            }

            if(!chain.containsKey(nextWord)) continue;

            Vector<String> wordSelection = chain.get(nextWord);

            int wordSelectionLen = wordSelection.size();
            nextWord = wordSelection.get(random.nextInt(wordSelectionLen));
            String word = nextWord.replace(" ","");
            word = word.replace("@","");
            newPhrase.add(word);

        }

        return newPhrase;
    }

    public Hashtable<String, Vector<String>> getChain() {
        return chain;
    }

    public boolean isOfMinSize(){
        return chain.size()>=MIN_SIZE;
    }

}
