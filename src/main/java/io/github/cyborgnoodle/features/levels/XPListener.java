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

package io.github.cyborgnoodle.features.levels;
import io.github.cyborgnoodle.settings.data.ServerChannel;
import io.github.cyborgnoodle.util.Log;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Created by arthur on 29.01.17.
 */
public class XPListener implements ChangeListener<Number> {

    private TempUser tempUser;

    public XPListener(TempUser tempUser) {
        this.tempUser = tempUser;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number onum, Number nnum) {

        long oldValue = onum.longValue();
        long newValue = nnum.longValue();

        if(oldValue<newValue){ //mehr
            Log.info(" + XP "+(newValue-oldValue)+" @"+tempUser.getUser().getName()+" => "+newValue);
            //tempUser.getRegistry().getNoodle().getChannel(ServerChannel.GENERAL).sendMessage("*"+(newValue-oldValue)+" XP was added to "+tempUser.getUser().getMentionTag()+"*");

            int nlvl = LevelConverser.getLevelforXP(newValue);
            if(nlvl>tempUser.getLevel()){
                tempUser.setLevel(nlvl);
            }
        }
        else { // weniger
            Log.info(" - XP "+(oldValue-newValue)+" @"+tempUser.getUser().getName()+" => "+newValue);
            tempUser.getRegistry().getNoodle().getChannel(ServerChannel.GENERAL).sendMessage("*"+(oldValue-newValue)+" XP was removed from "+tempUser.getUser().getMentionTag()+"*");
        }

    }

}
