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

package io.github.cyborgeditor.parts;

import io.github.cyborgeditor.Main;
import io.github.cyborgnoodle.settings.data.ServerChannel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthur on 25.02.17.
 */
public class Markov {

    @FXML
    public VBox excluded_channels;

    private List<Pair<CheckBox,ServerChannel>> boxes;
    private Main main;

    public Markov(){
        boxes = new ArrayList<>();
    }

    public void init(Main main){
        this.main = main;

        populateExcludedList(ServerChannel.values());

        main.settings().markov.exluded_channels.addListener((ListChangeListener<ServerChannel>) c -> {
            for(Pair<CheckBox,ServerChannel> pair : boxes){
                ServerChannel channel = pair.getValue();
                CheckBox box = pair.getKey();
                if(main.settings().markov.exluded_channels.contains(channel)) box.setSelected(true);
                else box.setSelected(false);
            }
        });
    }

    private void populateExcludedList(ServerChannel[] chs){

        for(ServerChannel ch : chs){
            CheckBox box = new CheckBox("#" + ch.name().toLowerCase());
            box.selectedProperty().addListener((observable, oldValue, newValue) -> onExcludedBoxSelect(ch,newValue));
            excluded_channels.getChildren().add(box);
            boxes.add(new Pair<>(box,ch));
        }

    }

    private void onExcludedBoxSelect(ServerChannel channel, boolean selected){

        if(selected){
            if(!main.settings().markov.exluded_channels.contains(channel)){
                main.settings().markov.exluded_channels.add(channel);
                main.updateCode();
            }

        }
        else {
            main.settings().markov.exluded_channels.remove(channel);
            main.updateCode();
        }


    }

}
