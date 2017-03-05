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

import de.btobastian.javacord.entities.permissions.Role;
import io.github.cyborgnoodle.settings.data.ServerChannel;
import io.github.cyborgnoodle.settings.data.ServerRole;
import io.github.cyborgnoodle.util.Log;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.concurrent.ExecutionException;

/**
 * Created by arthur on 29.01.17.
 */
public class LevelListener implements ChangeListener<Number> {

    private TempUser tempUser;

    public LevelListener(TempUser tempUser) {
        this.tempUser = tempUser;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number onum, Number nnum) {

        int oldValue = onum.intValue();
        int newValue = nnum.intValue();

        if(oldValue<newValue){ //mehr
            Log.info(" + LEVEL "+(newValue-oldValue)+" @"+tempUser.getUser().getName()+" => "+newValue,Levels.context);
            tempUser.getRegistry().getNoodle().getChannel(ServerChannel.GENERAL).sendMessage(tempUser.getUser().getMentionTag()+" advanced to Level **"+newValue+"**!");

            ServerRole srole = RankCalculator.getRoleforLevel(newValue);

            if(srole==null) return;

            Role role = tempUser.getRegistry().getNoodle().getRole(srole);

            if(!role.getUsers().contains(tempUser.getUser())){

                for(ServerRole r : ServerRole.getRanks()){
                    Role ro = tempUser.getRegistry().getNoodle().getRole(r);
                    try {
                        ro.removeUser(tempUser.getUser()).get();
                    } catch (InterruptedException | ExecutionException e) {
                        Log.error("Failed to remove role "+r+" from @"+tempUser.getUser().getName(),Levels.context);
                        Log.stacktrace(e,Levels.context);
                    }
                }

                //finally add the new one (but delayed 20 seconds)

                tempUser.getRegistry().getNoodle().doLater(() -> {
                    try {
                        role.addUser(tempUser.getUser()).get();
                    } catch (InterruptedException | ExecutionException e) {
                        Log.error("Failed to add role "+srole+" to @"+tempUser.getUser().getName(),Levels.context);
                        Log.stacktrace(e,Levels.context);
                    }

                    tempUser.getRegistry().getNoodle().getChannel(ServerChannel.GENERAL).sendMessage(tempUser.getUser().getMentionTag()+" is now a **"+role.getName()+"**!");
                },20000);

            }
        }
        // weniger level ignoriert

    }
}
