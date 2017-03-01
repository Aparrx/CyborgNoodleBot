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
import java.util.Calendar;

/**
 *
 */
public class Meta {

    public static String getVersion(){
        return "27.7-0";
    }

    public static String getChangelog(){

        String s = "```\n";
        s = s + "27.7-0 THE 4-MONTH BIRTHDAY UPDATE\n" +
                "\n" +
                "== NEW FEATURES ==\n" +
                "\n" +
                "- new settings system with an external desktop application\n" +
                "  - includes the possibility to exclude channels from markov (e.g #founders)\n" +
                "- new convertible currency units with hourly updating exchange rates!\n" +
                "  - US Dollars         [USD]\n" +
                "  - Australian Dollars [AUD]\n" +
                "  - Swiss francs       [CHF]\n" +
                "  - British pounds     [GBP]\n" +
                "  - Euro               [EUR]\n" +
                "- autoconverter now works with <number> <unit>, <unit> <number>,\n" +
                "  <number><currency> AND <currency><number> !!!\n" +
                "- !makeme minimum level is now 25\n" +
                "\n" +
                "- '!help' now displays only the commands the user has permissions to by default.\n" +
                "  '!help all' will display all commands, ignoring the permissions.\n" +
                "  '!help full' will display all commands, including hidden ones.\n" +
                "\n" +
                "== IMPROVEMENTS & FIXES ==" +
                "\n" +
                "- fixed 'Halleluja' ;)\n" +
                "- fixed the discord link in the reddit post\n" +
                "\n" +
                "- completely reworked news reposting mechanism\n" +
                "- internal channel list (and settings list) now reflects the actual channel names\n" +
                "- better error handling for random values\n" +
                "- better error handling for poll pie charts (polls should work as expected now!)\n" +
                "- some save files now use Googles binary encoding system, this makes them smaller and more reliable!\n" +
                "  (and fixes the data loss problem, hopefully)\n" +
                "- fixed a warning message when someone < Level 3 leveled up\n" +
                "";

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

        embed.setFooter("Developed with a lot of suffering and unpaid work by Enveed. © "+ Calendar.getInstance().get(Calendar.YEAR));

        return embed;
    }

}
