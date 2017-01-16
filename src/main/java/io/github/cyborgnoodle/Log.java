package io.github.cyborgnoodle;

import io.github.cyborgnoodle.misc.LogColor;

import java.io.PrintStream;
import java.util.Calendar;

/**
 * Logging Util
 */
public class Log {

    public static void log(String msg, PrintStream s){
        s.println(msg);
    }

    public static void log(String msg){
        log(msg,System.out);
    }

    public static void info(String msg){
        log("["+getTimeStamp()+"][I] "+msg);
    }

    public static void warn(String msg){
        log("["+getTimeStamp()+"]"+LogColor.ANSI_YELLOW+"[W]"+LogColor.ANSI_RESET+" "+msg);
    }

    public static void error(String msg){
        log("["+getTimeStamp()+"]"+LogColor.ANSI_RED+"[E]"+LogColor.ANSI_RESET+" "+msg,System.err);
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
}
