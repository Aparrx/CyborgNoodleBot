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
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

/**
 * Created by arthur on 24.02.17.
 */
public class XP {

    @FXML
    public CheckBox gainxp;
    @FXML
    public TextField msgmax;
    @FXML
    public TextField bombmax;
    @FXML
    public TextField msgmin;
    @FXML
    public TextField bombmin;
    @FXML
    public TextField timeoutmax;
    @FXML
    public TextField timeoutmin;

    public void init(Main main){

        this.gainxp.selectedProperty().bindBidirectional(main.settings().xp.gain);
        this.gainxp.selectedProperty().addListener((observable, oldValue, newValue) -> updateCode(main));

        this.msgmax.textProperty().bindBidirectional(main.settings().xp.msg_max, new NumberStringConverter("#######"));
        this.msgmax.textProperty().addListener((observable, oldValue, newValue) -> updateCode(main));
        this.msgmin.textProperty().bindBidirectional(main.settings().xp.msg_min, new NumberStringConverter("#######"));
        this.msgmin.textProperty().addListener((observable, oldValue, newValue) -> updateCode(main));

        this.bombmax.textProperty().bindBidirectional(main.settings().xp.bomb_max, new NumberStringConverter("#######"));
        this.bombmax.textProperty().addListener((observable, oldValue, newValue) -> updateCode(main));
        this.bombmin.textProperty().bindBidirectional(main.settings().xp.bomb_min, new NumberStringConverter("#######"));
        this.bombmin.textProperty().addListener((observable, oldValue, newValue) -> updateCode(main));

        this.timeoutmax.textProperty().bindBidirectional(main.settings().xp.bomb_timeout_max, new NumberStringConverter("#######"));
        this.timeoutmax.textProperty().addListener((observable, oldValue, newValue) -> updateCode(main));
        this.timeoutmin.textProperty().bindBidirectional(main.settings().xp.bomb_timeout_min, new NumberStringConverter("#######"));
        this.timeoutmin.textProperty().addListener((observable, oldValue, newValue) -> updateCode(main));
    }

    private void updateCode(Main main){
        main.updateCode();
    }


}
