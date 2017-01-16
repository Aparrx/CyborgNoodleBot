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

package io.github.cyborgnoodle.chatcli;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.permissions.Role;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.Meta;
import io.github.cyborgnoodle.Random;
import io.github.cyborgnoodle.levels.LevelConverser;
import io.github.cyborgnoodle.misc.*;
import io.github.cyborgnoodle.misc.funtance.stories.GenericSentenceGenerator;
import io.github.cyborgnoodle.msg.ConversationMessages;
import io.github.cyborgnoodle.msg.SystemMessages;
import io.github.cyborgnoodle.server.ServerChannel;
import io.github.cyborgnoodle.server.ServerRole;
import io.github.cyborgnoodle.server.ServerUser;
import me.postaddict.instagramscraper.exception.InstagramException;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatCommands {

    CyborgNoodle noodle;

    public ChatCommands(CyborgNoodle noods){
        this.noodle = noods;
    }

    public void execute(String cmd, User author, Channel channel, List<User> mentions, Message dmsg){

        Log.info(author.getName()+" issued commmand: !"+cmd);

        String[] args = cmd.split(" ");

        if(args[0].equalsIgnoreCase("vote")){

            if(args.length>1){
                String choice = args[1];
                noodle.getPolls().vote(author,choice,channel,dmsg);
            }
            else channel.sendMessage("You need to specify what you are voting for!");

            return;
        }

        if(args[0].equalsIgnoreCase("result")){

            if(!noodle.getRole(ServerRole.STAFF).getUsers().contains(author)){
                channel.sendMessage("You are not allowed to stop a poll! "+author.getMentionTag());
                return;
            }

            EmbedBuilder em = noodle.getPolls().result();

            channel.sendMessage("",em);

            return;
        }

        if(args[0].equalsIgnoreCase("units")){

            String msg = "```\n";

            for(UnitConverter.UnitType type : UnitConverter.UnitType.values()){
                String name = type.getName();
                msg = msg.concat(">> "+name+"\n");

                List<UnitConverter.Unit> units = new ArrayList<>(UnitConverter.Unit.getByType(type));
                Collections.sort(units, (o1, o2) -> {
                    UnitConverter.UnitSystem sys1 = o1.getSystem();
                    UnitConverter.UnitSystem sys2 = o2.getSystem();

                    if(sys1.equals(sys2)) return 0;
                    else {
                        if(sys1.equals(UnitConverter.UnitSystem.METRIC)) return 1;
                        else return -1;
                    }
                });

                for(UnitConverter.Unit unit : units){
                    String unitname = unit.getName();
                    String unitsystem = unit.getSystem().getName();
                    String unitsymbol = unit.getSymbol();
                    String row = String.format("%-20s%-6s%-10s", unitname, unitsymbol, unitsystem);
                    msg = msg.concat(" "+row+"\n");
                }
                msg = msg.concat("\n");
            }

            msg = msg.concat("```");

            channel.sendMessage(msg);

            return;

        }

        if(args[0].equalsIgnoreCase("convert")){
            //!convert 3230 kg to ml
            //    0      1   2  3  4  = length(5)
            if(args.length==5){

                String amountstr = args[1];
                String unitfromsym = args[2];
                String unittosym = args[4];

                try {
                    Double amount = Double.valueOf(amountstr);

                    UnitConverter.Unit from = UnitConverter.Unit.getBySymbol(unitfromsym);

                    if(from==null){
                        channel.sendMessage("This is not a valid unit: `"+unitfromsym+"`");
                        return;
                    }

                    UnitConverter.Unit to = UnitConverter.Unit.getBySymbol(unittosym);

                    if(to==null){
                        channel.sendMessage("This is not a valid unit: `"+unittosym+"`");
                        return;
                    }

                    try {
                        double converted = UnitConverter.convert(amount, from, to);
                        channel.sendMessage("`"+amount+" "+from.getSymbol()+"  =  "+converted+" "+to.getSymbol()+"`");
                        return;

                    } catch (UnitConverter.IncompatibleUnitTypesException e) {
                        channel.sendMessage("You can not convert a "+from.getType().getName()+" to a "+to.getType().getName()+"!");
                        return;
                    }

                } catch (NumberFormatException e) {
                    channel.sendMessage("This is not a valid number: `"+amountstr+"`");
                    return;
                }

            } else channel.sendMessage("Usage: `!convert <number> <unit> to <unit2>`");

            return;
        }

        if(args[0].equalsIgnoreCase("fun")){
            channel.sendMessage(GenericSentenceGenerator.create());
            return;
        }

        if(args[0].equalsIgnoreCase("userwords")){
            if(args.length==2){

                String mentiontag = args[1];

                User user = null;
                for(User u : noodle.getAPI().getUsers()){
                    if(u.getMentionTag().equalsIgnoreCase(mentiontag)) user = u;
                }

                if(user==null){
                    Log.info("Can't find user!");
                    return;
                }

                HashMap<String,Long> toplist = new HashMap<>();
                for(String word : noodle.getWordStats().getData().getEntries().keySet()){
                    WordStatsEntry entry = noodle.getWordStats().getData().getEntries().get(word);
                    if(entry.getUsers().containsKey(user.getId())){
                        Long wordcount = entry.getUsers().get(user.getId());
                        toplist.put(word,wordcount);
                    }
                }

                String name = user.getNickname(noodle.getServer());
                if(name==null) name = user.getName();
                if(name==null) name = "UNKNOWN NAME";

                String msg = "Words for "+name+"```\n";
                List<String> listentries = new ArrayList<>();

                toplist.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(20).forEach(entry -> {
                            String word = entry.getKey();
                            Long cl = entry.getValue();
                            String count = cl.toString();
                            word = ellipsize(word,19);
                            String wordspace = getWhitespaces(20-word.length());
                            listentries.add(word+wordspace+count);
                        });

                for(String entry : listentries){
                    msg = msg + entry + "\n";
                }

                msg = msg + "\n```";

                channel.sendMessage(msg);


            }
            else channel.sendMessage("Wrong Argument count! Usage: `!userwords @MentionTag`");

            return;
        }

        // NOT CATCHED

        //disabled for new feature
        //channel.sendMessage("["+author.getName()+" - invalid command]");
        //dmsg.delete();

    }

    public void executePrivate(String cmd, User author, Message msg){

        Log.info(author.getName()+" issued commmand [private]: !"+cmd);

        if(!noodle.getRole(ServerRole.STAFF).getUsers().contains(author)){
            msg.reply("hey man, cou cant message me in private!");
            return;
        }

        if(cmd.startsWith("short")){
            String[] spmsg = cmd.split(" ");

            if(spmsg.length==2){
                String url = spmsg[1];
                try {
                    try {
                        String shorturl = GoogleURLShortening.shortenUrl(url);
                        msg.reply(author.getMentionTag()+" I shortened your URL for you: "+shorturl);
                    } catch (MalformedURLException e) {
                        msg.reply("This is not a valid URL "+author.getMentionTag());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else msg.reply("No user!");



        }

        if(cmd.startsWith("say")){
            String[] spmsg = cmd.split(" ");

            if(spmsg.length>2 && cmd.contains("#")){
                String channel = spmsg[1];

                String s = cmd.replace("say","");
                s = s.replace(channel,"");

                for(String word : spmsg){

                    if(word.startsWith("+")){

                        String nword = word.replace("+","");

                        User u = null;
                        for(User usr : noodle.getAPI().getUsers()){
                            if(usr.getName().equalsIgnoreCase(nword)){
                                u = usr;
                                break;
                            }
                        }

                        if(u!=null) s = s.replace(word,u.getMentionTag());
                        else s = s.replace(word,nword);
                    }


                }




                String chstr = channel.replace("#","");

                ServerChannel sc =  ServerChannel.valueOf(chstr);

                if(sc!=null) noodle.getChannel(sc).sendMessage(s);
                else msg.reply("Channel is null!");

            }
            else{
                msg.reply("Specify Message pls");
            }



        }

    }


    public void onMessage(Message msg){
        String msgs = msg.getContent();
        if(msgs.startsWith("!")){
            String cmd = msgs.replace("!","");

            if(!msg.isPrivateMessage()){
                execute(cmd,msg.getAuthor(),msg.getChannelReceiver(),msg.getMentions(),msg);
            }
            else executePrivate(cmd,msg.getAuthor(),msg);


        }
    }



    private String getWhitespaces(int length){
        String s = "";
        while (length>0){
            length--;
            s = s + " ";
        }
        return s;
    }

    public String ellipsize(String input, int maxLength) {
        if (input == null || input.length() <= maxLength) {
            return input;
        }
        return input.substring(0, maxLength-3) + "...";
    }





}
