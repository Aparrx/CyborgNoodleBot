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

import de.btobastian.javacord.entities.User;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

/**
 * Created by arthur on 24.01.17.
 */
public class TempUser {

    private TempRegistry registry;

    private User user;

    private LongProperty xp;
    private IntegerProperty level;

    private long gifttimeout;

    public TempUser(TempRegistry registry, User user, long xp, int level, long gifttimeout) {
        this.registry = registry;
        this.user = user;
        this.xp = new SimpleLongProperty(xp);
        this.level = new SimpleIntegerProperty(level);
        this.gifttimeout = gifttimeout;

        this.xp.addListener(new XPListener(this));
        this.level.addListener(new LevelListener(this));
    }

    public TempUser(TempRegistry registry, User user) {
        this.registry = registry;
        this.user = user;
        this.xp = new SimpleLongProperty(0);
        this.level = new SimpleIntegerProperty(0);
        this.gifttimeout = 0L;

        this.xp.addListener(new XPListener(this));
        this.level.addListener(new LevelListener(this));
    }

    public TempRegistry getRegistry() {
        return registry;
    }

    public User getUser() {
        return user;
    }

    public long getXp() {
        return xp.get();
    }

    public long getGiftTimeout() {
        return gifttimeout;
    }

    public void setGiftTimeout(long gifttimeout) {
        this.gifttimeout = gifttimeout;
    }

    public LongProperty xpProperty() {
        return xp;
    }

    public int getLevel() {
        return level.get();
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public void setXp(long xp) {
        this.xp.set(xp);
    }

    public void addXp(long xp){
        this.xp.set(this.xp.get()+xp);
    }

    public void substractXp(long xp){
        this.xp.set(this.xp.get()-xp);
    }

    public void setLevel(int level) {
        this.level.set(level);
    }
}
