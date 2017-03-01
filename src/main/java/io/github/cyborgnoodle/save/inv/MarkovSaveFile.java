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

import com.google.protobuf.InvalidProtocolBufferException;
import io.github.cyborgnoodle.Save;
import io.github.cyborgnoodle.features.markov.Markov;
import io.github.cyborgnoodle.features.markov.MarkovChain;
import io.github.cyborgnoodle.features.markov.MarkovData;
import io.github.cyborgnoodle.save.SaveFile;

import java.util.Vector;

/**
 * Created by arthur on 17.02.17.
 */
public class MarkovSaveFile extends SaveFile<MarkovData> {

    public MarkovSaveFile() {
        super(MarkovData.class, true);
    }

    @Override
    public MarkovData loadUncompressed(byte[] bytes) throws SaveException {

        Save.Markov markov;
        try {
            markov = Save.Markov.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            throw new SaveException(e);
        }

        MarkovData data = new MarkovData();

        for (String userid : markov.getUsersMap().keySet()) {
            Save.MarkovChain protochain = markov.getUsersMap().get(userid);

            MarkovChain chain = new MarkovChain();

            for (String word : protochain.getChainMap().keySet()) {
                Save.WordList protolist = protochain.getChainMap().get(word);
                chain.getChain().put(word,new Vector<>(protolist.getWordsList()));
            }

            data.getChains().put(userid,chain);

        }

        return data;

    }

    @Override
    public byte[] saveUncompressed(MarkovData data) throws SaveException {

        Save.Markov.Builder builder = Save.Markov.newBuilder();

        for (String userid : data.getChains().keySet()) {
            MarkovChain chain = data.getChains().get(userid);

            Save.MarkovChain.Builder protochain = Save.MarkovChain.newBuilder();

            for (String word : chain.getChain().keySet()) {
                Vector<String> vec = chain.getChain().get(word);
                Save.WordList.Builder protolist = Save.WordList.newBuilder();
                protolist.addAllWords(vec);
                protochain.putChain(userid,protolist.build());
            }

            builder.putUsers(userid,protochain.build());

        }

        return builder.build().toByteArray();

    }

    @Override
    public String saveString(MarkovData object) throws SaveException {
        return saveString64(object);
    }

    @Override
    public MarkovData loadString(String str) throws SaveException {
        return loadString64(str);
    }

    @Override
    public MarkovData defaultObject() {
        return new MarkovData();
    }

    @Override
    public MarkovData onSave() {
        return Markov.getData();
    }

    @Override
    public void onLoad(MarkovData object) {
        Markov.setData(object);
    }
}
