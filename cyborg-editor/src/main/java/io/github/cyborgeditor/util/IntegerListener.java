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
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Created by arthur on 24.02.17.
 */
public class IntegerListener implements ChangeListener<String> {

    private TextField field;
    private IntegerProperty property;
    private Main main;

    public IntegerListener(Main main, TextField field, IntegerProperty property){
        this.field = field;
        this.property = property;
        this.main = main;
    }

    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if(!newValue.matches("^[+-]?\\d+$")){
            field.setText(newValue.replaceAll("[^\\d]", ""));
        }
        else{
            Integer i = Integer.valueOf(newValue);
            property.set(i);
            main.updateCode();
        }
    }
}
