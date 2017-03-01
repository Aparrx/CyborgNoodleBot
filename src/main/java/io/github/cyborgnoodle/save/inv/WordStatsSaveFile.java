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

package io.github.cyborgnoodle.save.inv;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Save;
import io.github.cyborgnoodle.features.wordstats.WordStatsData;
import io.github.cyborgnoodle.features.wordstats.WordStatsEntry;
import io.github.cyborgnoodle.save.SaveFile;

/**
 * Created by arthur on 17.02.17.
 */
public class WordStatsSaveFile extends SaveFile<WordStatsData> {

    private CyborgNoodle noodle;

    public WordStatsSaveFile(CyborgNoodle noodle) {
        super(WordStatsData.class, true);
        this.noodle = noodle;
    }

    @Override
    public WordStatsData loadUncompressed(byte[] bytes) throws SaveException {

        Save.WordStats protowstats;
        try {
            protowstats = Save.WordStats.parseFrom(bytes);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw new SaveException(e);
        }

        WordStatsData wdata = new WordStatsData();

        for (String word : protowstats.getWordsMap().keySet()) {
            Save.WordStatsEntry protowentry = protowstats.getWordsMap().get(word);

            long count = protowentry.getCount();

            WordStatsEntry entry = new WordStatsEntry();
            entry.count(count);

            for (String userid : protowentry.getUsersMap().keySet()) {
                Long usercount = protowentry.getUsersMap().get(userid);
                entry.getUsers().put(userid,usercount);
            }

            wdata.getEntries().put(word,entry);

        }

        return wdata;

    }

    @Override
    public byte[] saveUncompressed(WordStatsData data) throws SaveException {

        Save.WordStats.Builder builder = Save.WordStats.newBuilder();

        for (String word : data.getEntries().keySet()) {
            WordStatsEntry entry = data.getEntries().get(word);
            Long count = entry.getCount();

            Save.WordStatsEntry.Builder entrybuilder = Save.WordStatsEntry.newBuilder();
            entrybuilder.setCount(count);

            for (String userid : entry.getUsers().keySet()) {
                Long usercount = entry.getUsers().get(userid);
                entrybuilder.putUsers(userid,usercount);
            }

            builder.putWords(word,entrybuilder.build());

        }

        return builder.build().toByteArray();

    }

    @Override
    public String saveString(WordStatsData object) throws SaveException {
        return saveString64(object);
    }

    @Override
    public WordStatsData loadString(String str) throws SaveException {
        return loadString64(str);
    }

    @Override
    public WordStatsData defaultObject() {
        return new WordStatsData();
    }

    @Override
    public WordStatsData onSave() {
        return noodle.words.getData();
    }

    @Override
    public void onLoad(WordStatsData object) {
        noodle.words.setData(object);
    }
}
