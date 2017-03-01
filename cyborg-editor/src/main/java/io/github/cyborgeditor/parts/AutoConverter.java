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

/**
 * Created by arthur on 25.02.17.
 */
public class AutoConverter {

    @FXML
    public CheckBox enable;

    @FXML
    public CheckBox time;
    @FXML
    public CheckBox temperature;
    @FXML
    public CheckBox length;
    @FXML
    public CheckBox area;
    @FXML
    public CheckBox volume;
    @FXML
    public CheckBox mass;
    @FXML
    public CheckBox currency;

    public void init(Main main){

        enable.selectedProperty().bindBidirectional(main.settings().autoconv.enable);
        enable.selectedProperty().addListener((ob, o, n) -> main.updateCode());
        enable.selectedProperty().addListener((ob, o, n) -> {
            time.setDisable(!n);
            temperature.setDisable(!n);
            length.setDisable(!n);
            area.setDisable(!n);
            volume.setDisable(!n);
            mass.setDisable(!n);
            currency.setDisable(!n);
        });

        time.selectedProperty().bindBidirectional(main.settings().autoconv.time);
        time.selectedProperty().addListener((ob, o, n) -> main.updateCode());

        temperature.selectedProperty().bindBidirectional(main.settings().autoconv.temperature);
        temperature.selectedProperty().addListener((ob, o, n) -> main.updateCode());

        length.selectedProperty().bindBidirectional(main.settings().autoconv.length);
        length.selectedProperty().addListener((ob, o, n) -> main.updateCode());

        area.selectedProperty().bindBidirectional(main.settings().autoconv.area);
        area.selectedProperty().addListener((ob, o, n) -> main.updateCode());

        volume.selectedProperty().bindBidirectional(main.settings().autoconv.volume);
        volume.selectedProperty().addListener((ob, o, n) -> main.updateCode());

        mass.selectedProperty().bindBidirectional(main.settings().autoconv.mass);
        mass.selectedProperty().addListener((ob, o, n) -> main.updateCode());

        currency.selectedProperty().bindBidirectional(main.settings().autoconv.currency);
        currency.selectedProperty().addListener((ob, o, n) -> main.updateCode());


    }
}
