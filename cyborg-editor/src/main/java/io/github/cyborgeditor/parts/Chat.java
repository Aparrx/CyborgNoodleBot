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
 * Created by arthur on 25.02.17.
 */
public class Chat {
    @FXML
    public CheckBox edits;
    @FXML
    public CheckBox badwords;
    @FXML
    public CheckBox newuser;
    @FXML
    public CheckBox banneduser;
    @FXML
    public CheckBox leaveuser;
    @FXML
    public TextField edits_chance;

    public void init(Main main){

        this.edits.selectedProperty().bindBidirectional(main.settings().chat.comment_edits);
        this.edits.selectedProperty().addListener((ob, o, n) -> updateCode(main));

        this.badwords.selectedProperty().bindBidirectional(main.settings().chat.comment_badwords);
        this.badwords.selectedProperty().addListener((ob, o, n) -> updateCode(main));

        this.newuser.selectedProperty().bindBidirectional(main.settings().chat.comment_newuser);
        this.newuser.selectedProperty().addListener((ob, o, n) -> updateCode(main));

        this.banneduser.selectedProperty().bindBidirectional(main.settings().chat.comment_banuser);
        this.banneduser.selectedProperty().addListener((ob, o, n) -> updateCode(main));

        this.leaveuser.selectedProperty().bindBidirectional(main.settings().chat.comment_leaveuser);
        this.leaveuser.selectedProperty().addListener((ob, o, n) -> updateCode(main));

        this.edits_chance.textProperty().bindBidirectional(main.settings().chat.comment_edits_chance,new NumberStringConverter("####"));
        this.edits_chance.textProperty().addListener((ob, o, n) -> updateCode(main));
    }

    private void updateCode(Main main){
        main.updateCode();
    }
}
