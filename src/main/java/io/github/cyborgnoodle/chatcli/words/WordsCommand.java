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

package io.github.cyborgnoodle.chatcli.words;

import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.chatcli.Command;
import io.github.cyborgnoodle.chatcli.Permission;
import io.github.cyborgnoodle.server.ServerRole;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthur on 16.01.17.
 */
public class WordsCommand extends Command {

    public WordsCommand(CyborgNoodle noodle) {
        super(noodle);
    }

    @Override
    public void onCommand(String[] args) throws Exception{

        int max;
        if(args.length==1){
            if(args[0].equalsIgnoreCase("max")){
                if(getNoodle().getRole(ServerRole.OWNER).getUsers().contains(getAuthor())){
                    max = getNoodle().getWordStats().getData().getMap().size();
                }
                else max = 20;
            }
            else {
                try {
                    Integer num = Integer.valueOf(args[0]);
                    if(num<=70){
                        max = num;
                    } else{
                        if(getNoodle().hasPermission(getAuthor(),new Permission(ServerRole.STAFF))){
                            if(num<200) max = num;
                            else max = 200;
                        }
                        else {
                            Log.warn("entered too big number: "+num);
                            max = 20;
                        }

                    }
                } catch (NumberFormatException e) {
                    Log.warn("entered misformed number: "+args[1]);
                    max = 20;
                }
            }

        }
        else max = 20;

        int i = 1;
        String msg = "*Most used words:*";

        List<String> words = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        for(String word : getNoodle().getWordStats().getWordBoard().keySet()){
            long num = getNoodle().getWordStats().getWordBoard().get(word);
            words.add(word);
            counts.add(num);
            i++;
            if(i>max) break;
        }

        CategoryChart chart = new CategoryChartBuilder().theme(Styler.ChartTheme.GGPlot2).xAxisTitle("Words").yAxisTitle("Usage count")
                .height(500).width(1500).build();

        chart.addSeries("Words",words,counts);

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);


        //to image
        byte[] data = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

        InputStream is = new ByteArrayInputStream(data);

        getChannel().sendFile(is,"png");
    }

    @Override
    public String[] aliases() {
        return new String[]{"words","ws","wrds"};
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public boolean emptyHelp() {
        return false;
    }

    @Override
    public String description() {
        return "show word toplist";
    }

    @Override
    public String category() {
        return "Word commands";
    }
}
