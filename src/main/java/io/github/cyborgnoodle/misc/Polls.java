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

package io.github.cyborgnoodle.misc;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 *
 */
public class Polls {

    Boolean active;
    HashMap<String,Integer> votes;

    Set<String> voters;

    String title;

    public Polls(){
        active = false;
        votes = new HashMap<>();
        voters = new HashSet<>();
        title = null;
    }

    public void vote(User user, String choice, Channel channel, Message message){
        if (votes.containsKey(choice)) {
            if (!voters.contains(user.getId())) {

                int i = votes.get(choice);
                i++;
                votes.put(choice, i);
                voters.add(user.getId());
                message.delete();
                channel.sendMessage("["+user.getName()+" voted]");

            } else{
                message.delete();
                channel.sendMessage("[" + user.getName() + " already voted]");
            }
        } else{
            message.delete();
            channel.sendMessage("[" + user.getName() + " - invalid vote]");
        }
    }

    public void start(String[] choices, Channel channel, String title){
        if(!active){
            active = true;
            voters.clear();
            votes.clear();

            if(choices.length<2){
                channel.sendMessage("Can't start a poll with less than 2 choices!");
                return;
            }
            if(choices.length>15){
                channel.sendMessage("Can't start a poll with more than 15 options!");
            }

            for(String c : choices){
                if(c.length()>12){
                    channel.sendMessage("Choice words can't be longer than 12 characters!");
                    return;
                }
            }

            for(String c : choices){
                votes.put(c,0);
            }

            String msg;

            if(title!=null){
                this.title = title;
                msg = "**"+title+"**\nOptions: ";
            }
            else{
                msg = "**Started a poll with the following choice options:** ";
            }



            for(String c : choices){
                msg = msg + c + " ";
            }

            msg = msg + "\nUse `!vote <choice>` to vote! You can only vote once!";

            channel.sendMessage(msg);
        }
        else channel.sendMessage("There is already a poll running!");
    }

    public EmbedBuilder result(){

        EmbedBuilder embed = new EmbedBuilder();

        if(active){
            active = false;
            embed.setTitle("Poll Results");
            embed.setColor(new Color(150,150,150));

            int count = getVoteCount();

            String fieldtitle;
            if(title!=null){
                fieldtitle = "__"+title+"__";
            } else {
                fieldtitle = "__Result(s)__";
            }
            String fielddesc = "";

            List<String> winners = getWinners();

            for(String winner : winners){
                fielddesc = fielddesc + winner + " ";
            }

            String detailtitle = "__Details__";
            String details = "```\n";

            for(String choice : votes.keySet()){
                int a = votes.get(choice);
                String filler = getWhitespaces(12-choice.length());
                String visual = getVisualisation(a,count);
                details = details + choice + filler + " - "+visual+" "+a+"/"+count+"\n";
            }

            details = details + "```";

            embed.addField(fieldtitle,fielddesc,false);
            embed.addField(detailtitle,details,false);

            voters.clear();
            votes.clear();

            title = null;
        }
        else{
            embed.setTitle("Result");
            embed.setDescription("There is no poll running!");
        }

        return embed;
    }

    public HashMap<String, Integer> getVotes() {
        return votes;
    }

    public String getTitle() {
        return title;
    }

    private List<String> getWinners(){
        List<String> win = new ArrayList<>();
        int maxValueInMap=(Collections.max(votes.values()));  // This will return max value in the Hashmap
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==maxValueInMap) {
                    win.add(entry.getKey());
            }
        }

        return win;
    }

    private int getVoteCount(){
        int i = 0;
        for(String choice : votes.keySet()){
            int a = votes.get(choice);
            i = i+a;
        }
        return i;
    }

    private String getWhitespaces(int length){
        String s = "";
        while (length>0){
            length--;
            s = s + " ";
        }
        return s;
    }

    private String getVisualisation(long of, long full){

        double zc = (double) of/full;
        double ta = zc*10;


        String s = "[";
        int i = 1;

        while (i<=10){
            if(ta>i) s = s + "█";
            else s = s + " ";
            i++;
        }

        Double percent = Math.round((ta*10)*100.0)/100.0;

        s = s + "] "+percent+"%";

        return s;

    }
}
