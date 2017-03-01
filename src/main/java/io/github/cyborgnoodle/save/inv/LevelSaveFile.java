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

package io.github.cyborgnoodle.save.inv;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.features.levels.LevelRegistry;
import io.github.cyborgnoodle.save.GSONSaveFile;

/**
 * Created by arthur on 17.02.17.
 */
public class LevelSaveFile extends GSONSaveFile<LevelRegistry> {

    private CyborgNoodle noodle;

    public LevelSaveFile(CyborgNoodle noodle) {
        super(LevelRegistry.class, false);
        this.noodle = noodle;
    }

    @Override
    public LevelRegistry defaultObject() {
        return new LevelRegistry();
    }

    @Override
    public LevelRegistry onSave() {
        return noodle.levels.toLevelRegistry();
    }

    @Override
    public void onLoad(LevelRegistry object) {
        noodle.levels.setRegistry(object);
    }
}
