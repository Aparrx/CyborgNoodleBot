package io.github.cyborgnoodle;

import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

import java.awt.*;

/**
 *
 */
public class Meta {

    public static String getVersion(){
        return "27.1";
    }

    public static String getChangelog(){

        String s = "```\n";
        s = s + "27.1\n" +
                "- added bullshit detector";

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

        embed.setFooter("Developed with a lot of time and coffee by Enveed.");

        return embed;
    }

}
