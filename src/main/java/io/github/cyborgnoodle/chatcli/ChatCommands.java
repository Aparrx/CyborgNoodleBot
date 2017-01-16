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

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.misc.GoogleURLShortening;
import io.github.cyborgnoodle.server.ServerChannel;
import io.github.cyborgnoodle.server.ServerRole;

import java.net.MalformedURLException;

public class ChatCommands {

    CyborgNoodle noodle;

    public ChatCommands(CyborgNoodle noods){
        this.noodle = noods;
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
                // ignore
            }
            else executePrivate(cmd,msg.getAuthor(),msg);


        }
    }




}
