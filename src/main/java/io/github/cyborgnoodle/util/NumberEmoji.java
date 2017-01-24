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

package io.github.cyborgnoodle.util;

import com.vdurmont.emoji.EmojiManager;

/**
 * Created by arthur on 18.01.17.
 */
public enum NumberEmoji {
    TEN(EmojiManager.getForAlias("keycap_ten").getUnicode(),10),
    NINE(EmojiManager.getForAlias("nine").getUnicode(),9),
    EIGHT(EmojiManager.getForAlias("eight").getUnicode(),8),
    SEVEN(EmojiManager.getForAlias("seven").getUnicode(),7),
    SIX(EmojiManager.getForAlias("six").getUnicode(),6),
    FIVE(EmojiManager.getForAlias("five").getUnicode(),5),
    FOUR(EmojiManager.getForAlias("four").getUnicode(),4),
    THREE(EmojiManager.getForAlias("three").getUnicode(),3),
    TWO(EmojiManager.getForAlias("two").getUnicode(),2),
    ONE(EmojiManager.getForAlias("one").getUnicode(),1),
    ;

    private String emoji;
    private int num;

    NumberEmoji(String emoji, int num) {
        this.emoji = emoji;
        this.num = num;
    }

    public String toEmoji() {
        return emoji;
    }

    public int getNumber() {
        return num;
    }
}
