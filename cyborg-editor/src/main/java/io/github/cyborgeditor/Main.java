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

package io.github.cyborgeditor;

import io.github.cyborgeditor.parts.AutoConverter;
import io.github.cyborgeditor.parts.Chat;
import io.github.cyborgeditor.parts.Markov;
import io.github.cyborgeditor.parts.XP;
import io.github.cyborgnoodle.Settings;
import io.github.cyborgnoodle.settings.CyborgSettings;
import io.github.cyborgnoodle.settings.util.SettingsCode;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by arthur on 24.02.17.
 */
public class Main extends Application{

    @FXML
    VBox root;

    @FXML
    TextArea codearea;

    @FXML
    Label status;

    @FXML
    Button loadbtn;

    @FXML
    CheckBox autoload;

    //

    @FXML
    XP xpController;

    @FXML
    Chat chatController;

    @FXML
    AutoConverter autoconvController;

    @FXML
    Markov markovController;

    private CyborgSettings settings;

    public static void main(String args[]){
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        loader.setLocation(Main.class.getResource("/fxml/main.fxml"));

        loader.load();

        Scene scene = new Scene(root);

        scene.getStylesheets().add("/css/style.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Cyborg Settings Utility");

        primaryStage.show();

        launch();
    }

    public void launch(){
        settings = new CyborgSettings(Settings.Setting.getDefaultInstance());

        codearea.textProperty().addListener((o, ov, nv) -> onEdit(nv));

        loadbtn.setOnAction(event -> onPasteLoad());

        autoload.selectedProperty().addListener((ob, o, n) -> {
            if(n){
                loadbtn.setVisible(false);
                codearea.setEditable(true);
                codearea.setPromptText("Paste settings code here");
            }
            else {
                loadbtn.setVisible(true);
                codearea.setEditable(false);
                codearea.setPromptText("Click the button to paste the settings code");
            }
        });

        //

        xpController.init(this);
        chatController.init(this);
        autoconvController.init(this);
        markovController.init(this);

        //
        updateCode();
    }

    public CyborgSettings settings(){
        return settings;
    }

    public void updateCode(){
        String code = SettingsCode.create(settings);
        codearea.setText(code);
        System.out.println("set code "+code);
    }

    private void load(String code){
        try {
            CyborgSettings other = SettingsCode.parse(code);
            settings.update(other);
            status.setText("Successfully parsed.");
        } catch (IOException e) {
            status.setText("Failed to parse settings code!");
        }
    }

    private void onEdit(String code){
        if(!code.isEmpty() && autoload.isSelected()){
            load(code);
        }
    }

    private void onPasteLoad(){
        if(!autoload.isSelected()){

            Clipboard clipboard = Clipboard.getSystemClipboard();

            if(clipboard.hasContent(DataFormat.PLAIN_TEXT)){
                String code = clipboard.getString();
                load(code);
            }
            else status.setText("INVALID CLIPBOARD CONTENT!");

        }
    }
    //qAZVsAYeuAa0QsAG3AvIBtAF0AYe0AwB2AwB4AwB4BIB6BIB8BIB+BIBgBMBiBMBkBMBmBMB
    //oAYBqAZVsAYeuAa0QsAG3AvIBtAF0AY80AwB2AwB4AwB4BIB6BIB8BIB+BIBgBMBiBMBkBMBmBMB

    //with 0 and unchecked
    //qAZVsAYZuAa0QsAG6AfIBtAF0AwB2AwB4AwB4BIB6BIB8BIB+BIBgBMBiBMBkBMBmBMB


    //DEFAULT
    //oAYBqAZVsAYZuAaQTsAGuBfIBqwC0AY80AwB2AwB4AwB6AwU4BIB8BIB+BIBgBMBiBMBkBMBmBMB

    //DEF 2
    //oAYBqAZVsAYZuAaQTsAGuBfIBqwC0AY80AwB2AwB4AwB6AwU4BIB8BIB+BIBgBMBiBMBkBMBmBMBoh8DBgUD

    //SICK
    ///oAYBuAbJAeASAfASAYATAYgTAZATAZgTAaIfBQYDAgAF

    //NEWDEF
    //oAYBqAZVsAYZuAaQTsAG6AfIBqwC0AY80AwB2AwB4AwB6AwU4BIB8BIB+BIBgBMBiBMBkBMBmBMBoh8DBgME
}
