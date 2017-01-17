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

package io.github.cyborgnoodle;

import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

import java.awt.*;

/**
 *
 */
public class Meta {

    public static String getVersion(){
        return "27.3";
    }

    public static String getChangelog(){

        String s = "```\n";
        s = s + "27.3\n" +
                "- added !sue, !chore and !exceptions";

        s = s + "\n```";
        return s;

    }

    public static EmbedBuilder getEmbed(){

        String vmversion = System.getProperty("java.version");

        String os = System.getProperty("os.name")+" "+ System.getProperty("os.arch");
        String osver = System.getProperty("os.version");

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(90,90,90));
        embed.setTitle("CyborgNoodleBot "+getVersion());

        embed.addField("Details","Version: "+getVersion()+"\n" +
                "Library: "+ Javacord.VERSION+"\n",true);

        embed.addField("System","OS: "+os+"\n" +
                "Version: "+osver+"\n" +
                "Java: "+vmversion,true);

        embed.setFooter("Developed with a lot of suffering and unpaid work by Enveed.");

        return embed;
    }

}
