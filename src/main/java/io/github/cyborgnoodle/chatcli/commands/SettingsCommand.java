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

package io.github.cyborgnoodle.chatcli.commands;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.settings.Settings;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.server.ServerRole;
import io.github.cyborgnoodle.settings.SettingsEntry;
import io.github.cyborgnoodle.util.StringUtils;

/**
 * Created by arthur on 29.01.17.
 */
public class SettingsCommand extends Command{

    public SettingsCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception {
        if(args.length==2){

            String field = args[0];
            String value = args[1];

            Object vo;

            try {
                vo = fromSetting(value);
            } catch (Exception e) {
                getChannel().sendMessage("Not a valid value. Value must be `true`, `false` or a number (e.g. `27.3`)!");
                return;
            }

            boolean suc;
            try {
                suc = Settings.set(field, vo);
            } catch (IllegalArgumentException e) {
                getChannel().sendMessage("Invalid field type!");
                return;
            }

            if(suc) getChannel().sendMessage("*"+field+"*: "+vo);
            else getChannel().sendMessage("Failed to set settings property!");

        } else if(args.length==0){

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Bot Settings");

            Multimap<String,SettingsEntry> categories = HashMultimap.create();

            for(String key : Settings.get().keySet()){

                SettingsEntry entry = Settings.get().get(key);

                String category = entry.getCategory();
                categories.put(category,entry);
            }

            for(String cat : categories.keySet()){

                String desc = "";

                for(SettingsEntry entry : categories.get(cat)){

                    String name = entry.getName();
                    Object value = entry.getValue();

                    desc = desc + name + ": `"+value+"`\n";

                }

                embed.setFooter("Use `!set <field> <true/false/number>` to change settings");

                embed.addField(cat,desc,false);

            }

            getChannel().sendMessage("",embed);

        } else showInvalidArguments();
    }

    private Object fromSetting(String setting){

        if(setting.equalsIgnoreCase("true")) return true;
        else if(setting.equalsIgnoreCase("false")) return false;
        else if(StringUtils.isNumeric(setting)){
            try {
                Double d = Double.valueOf(setting);
                return d;
            } catch (NumberFormatException e) {
                Log.info("StringUtils::isNumeric failed??");
                e.printStackTrace();
                throw e;
            }

        } else throw new IllegalArgumentException("No readable format!");

    }

    @Override
    public String[] aliases() {
        return new String[]{"set","settings"};
    }

    @Override
    public String usage() {
        return "!set | !set <field> true/false";
    }

    @Override
    public boolean emptyHelp() {
        return false;
    }

    @Override
    public String description() {
        return "set and view bot settings";
    }

    @Override
    public Permission fullPermission() {
        return new Permission(ServerRole.OWNER);
    }
}
