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

package io.github.cyborgnoodle.chatcli.stats2;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.chatcli.commands.Stats2Command;
import io.github.cyborgnoodle.statistics.StatsPair;
import io.github.cyborgnoodle.statistics.StatsType;
import io.github.cyborgnoodle.util.JCUtil;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by arthur on 30.01.17.
 */
public class Stats2Callable implements Callable<Void> {

    private String[] args;
    private Stats2Command c;
    private Message status;

    public Stats2Callable(String[] args, Stats2Command c, Message status) {
        this.args = args;
        this.c = c;
        this.status = status;
    }

    private byte[] createGraph() throws Exception {

        XYChart chart = new XYChartBuilder().height(400).width(750).xAxisTitle("Time").yAxisTitle("messages / minute")
                .theme(Styler.ChartTheme.GGPlot2).build();

        status.edit("`collecting data ...`");
        StatsPair times;
        if(args.length>=1){

            String type = args[0];
            Stats2Command.DisplayType t;
            try {
                t = Stats2Command.DisplayType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                t = null;
            }

            times = c.getForRange(t);
        } else times = c.getForToday();

        List<Date> dtsstr = new ArrayList<>();
        List<Number> counts = new ArrayList<>();


        StatsType type = StatsType.MSG_SPEED;
        String title = "Message Statistics";
        String yaxis = "messages / minute";

        dtsstr.addAll(times.getDates());

        status.edit("`sorting data ...`");

        if(args.length>=2){

            String argument = args[1];

            if(argument.startsWith("<#")){

                String[] rest = Arrays.copyOfRange(args, 1, args.length);

                for(String ch : rest){
                    Channel channel = JCUtil.getChannelByMention(c.getNoodle().getAPI(),ch);
                    if(channel!=null){
                        List<Number> nums = times.asYNumbers(StatsType.MSG_SPEED_CHANNEL, channel.getId());
                        XYSeries s = chart.addSeries("#" + channel.getName(), dtsstr, nums);
                        s.setMarker(SeriesMarkers.NONE);
                    }
                }
            } else if(argument.startsWith("<@")){

                String[] rest = Arrays.copyOfRange(args, 1, args.length);

                for(String ch : rest){
                    User user = JCUtil.getUserByMention(c.getNoodle().getAPI(),ch);
                    if(user!=null){
                        XYSeries s = chart.addSeries("@"+user.getName(),dtsstr,times.asYNumbers(StatsType.MSG_SPEED_USER,user.getId()));
                        s.setMarker(SeriesMarkers.NONE);
                    }
                }

            } else if(argument.equalsIgnoreCase("users")){

                if(args.length<=2){
                    type = StatsType.USER_COUNT;
                    title = "User count";
                    yaxis = "users";
                }
                else {
                    String[] rest = Arrays.copyOfRange(args, 2, args.length);

                    for(String mention : rest){

                        User user = JCUtil.getUserByMention(c.getNoodle().getAPI(),mention);

                        String name;
                        if(user==null) break;
                        else name = "@"+user.getName();

                        XYSeries s = chart.addSeries(name,dtsstr,times.asYNumbers(StatsType.USER_ONLINE_USER,user.getId()));
                        s.setMarker(SeriesMarkers.NONE);

                    }

                    type = StatsType.USER_COUNT;
                    title = "User count";
                    yaxis = "users";
                }


            } else if(argument.equalsIgnoreCase("count")){
                type = StatsType.MSG_COUNT;
                title = "Message count";
                yaxis = "messages";
            }



        }

        counts.addAll(times.asYNumbers(type));
        //main count
        XYSeries mainseries = chart.addSeries("All", dtsstr, counts);
        mainseries.setMarker(SeriesMarkers.NONE);

        if(type.equals(StatsType.USER_COUNT)){

            List<Number> oc = new ArrayList<>();
            oc.addAll(times.asYNumbers(StatsType.USER_ONLINE_COUNT));
            XYSeries onlineseries = chart.addSeries("Online", dtsstr, oc);
            onlineseries.setMarker(SeriesMarkers.NONE);
        }

        status.edit("`rendering graph ...`");

        chart.setTitle(title);
        chart.setYAxisTitle(yaxis);

        //setup
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);


        //to image
        byte[] data = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

        return data;
    }

    @Override
    public Void call() throws Exception {

        status.edit("`creating graph ...`");

        byte[] graph = createGraph();

        status.edit("`uploading graph ...`");

        InputStream is = new ByteArrayInputStream(graph);
        c.getChannel().sendFile(is, "png", new FutureCallback<Message>() {
            @Override
            public void onSuccess(@Nullable Message message) {
                status.delete();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.error("Failed to delete status message for graph creation: "+ throwable.getMessage());
                throwable.printStackTrace();
            }
        });

        return null;

    }
}
