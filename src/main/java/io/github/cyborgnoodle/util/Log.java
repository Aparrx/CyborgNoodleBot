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

import com.google.common.base.Throwables;
import io.github.cyborgnoodle.misc.LogColor;

import java.io.PrintStream;
import java.util.Calendar;

/**
 * Logging Util
 */
public class Log {

    private static Logger logger;
    private static final LogContext defaultcontext = new LogContext(null);

    static {
        logger = new StandardLogger();
    }

    public static void setLogger(Logger l){
        logger = l;
    }

    public static Logger getLogger(){
        return logger;
    }

    public static void logCustom(String msg, PrintStream s){
        s.println(msg);
    }

    public static void logError(String msg){
        logger.logError(msg);
    }

    public static void logNormal(String msg){
        logger.logNormal(msg);
    }

    public static void info(String msg, boolean raw, LogContext context){
        if(raw) logNormal(msg);
        else logNormal("["+getTimeStamp()+"][I]"+getContextBox(context)+" "+msg);
    }

    public static void warn(String msg, boolean raw, LogContext context){
        if(raw) logNormal(msg);
        else logNormal("["+getTimeStamp()+"]"+LogColor.ANSI_YELLOW+"[W]"+LogColor.ANSI_RESET+getContextBox(context)+" "+msg);
    }

    public static void error(String msg, boolean raw, LogContext context){
        if(raw) logError(msg);
        else logError("["+getTimeStamp()+"]"+LogColor.ANSI_RED+"[E]"+LogColor.ANSI_RESET+getContextBox(context)+" "+msg);
    }

    public static void stacktrace(Throwable throwable, boolean stampless, LogContext context){
        String trace = Throwables.getStackTraceAsString(throwable);
        error(trace, stampless,context);
    }

    public static void info(String msg){
        info(msg,false,defaultcontext);
    }

    public static void warn(String msg){
        warn(msg,false,defaultcontext);
    }

    public static void error(String msg){
        error(msg,false,defaultcontext);
    }

    public static void stacktrace(Throwable throwable){
        stacktrace(throwable,false,defaultcontext);
    }

    public static void info(String msg, boolean raw){
        info(msg,raw,defaultcontext);
    }

    public static void warn(String msg, boolean raw){
        warn(msg,raw,defaultcontext);
    }

    public static void error(String msg, boolean raw){
        error(msg,raw,defaultcontext);
    }

    public static void stacktrace(Throwable throwable, boolean raw){
        stacktrace(throwable,raw,defaultcontext);
    }

    public static void info(String msg, LogContext context){
        info(msg,false,context);
    }

    public static void warn(String msg, LogContext context){
        warn(msg,false,context);
    }

    public static void error(String msg, LogContext context){
        error(msg,false,context);
    }

    public static void stacktrace(Throwable throwable, LogContext context){
        stacktrace(throwable,false,context);
    }

    private static String getContextBox(LogContext context){
        String cbox;
        if(context==null || context.getPrefix()==null) cbox = "";
        else cbox = "["+context.getPrefix()+"]";
        return cbox;
    }

    public static String getTimeStamp(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        Integer mYear = calendar.get(Calendar.YEAR);
        Integer mMonth = calendar.get(Calendar.MONTH) + 1;
        Integer mDay = calendar.get(Calendar.DAY_OF_MONTH);
        Integer mHour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer mMinute = calendar.get(Calendar.MINUTE);
        Integer mSecond = calendar.get(Calendar.SECOND);

        String syear = mYear.toString();

        String smonth;
        if(mMonth>9) smonth = mMonth.toString();
        else smonth = "0"+mMonth.toString();

        String sday;
        if(mDay>9) sday = mDay.toString();
        else sday = "0"+mDay.toString();

        String shour;
        if(mHour>9) shour = mHour.toString();
        else shour = "0"+mHour.toString();

        String sminute;
        if(mMinute>9) sminute = mMinute.toString();
        else sminute = "0"+mMinute.toString();

        String ssecond;
        if(mSecond>9) ssecond = mSecond.toString();
        else ssecond = "0"+mSecond.toString();

        return sday+"-"+smonth+"-"+syear+" "+shour+":"+sminute+":"+ssecond;

    }

    public interface Logger {
        PrintStream normal();
        PrintStream error();
        void logNormal(String msg);
        void logError(String msg);
    }

    public static class StandardLogger implements Logger{
        @Override
        public PrintStream normal() {
            return System.out;
        }
        @Override
        public PrintStream error() {
            return System.err;
        }

        @Override
        public void logNormal(String msg) {
            normal().println(msg);
        }

        @Override
        public void logError(String msg) {
            error().println(msg);
        }
    }

    public static class LogContext {

        private final String prefix;

        public LogContext(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
