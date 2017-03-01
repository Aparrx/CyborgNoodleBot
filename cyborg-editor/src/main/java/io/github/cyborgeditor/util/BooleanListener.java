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

package io.github.cyborgeditor.util;

import io.github.cyborgeditor.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

/**
 * Created by arthur on 24.02.17.
 */
public class BooleanListener implements ChangeListener<Boolean> {

    private CheckBox box;
    private BooleanProperty property;
    private Main main;

    public BooleanListener(Main main, CheckBox box, BooleanProperty property){
        this.box = box;
        this.property = property;
        this.main = main;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        property.set(newValue);
        System.out.println("updateCode called with "+newValue+" ==>");
        main.updateCode();

    }
}
