package com.example.dpene.wallefy.controller.controllerutils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormater {

    //from  dMMMyyyy to yyyyMMddHHmmss
    public static String from_dMMMyyyy_To_yyyyMMddHHmmss(String dateToFormat) {
        if (dateToFormat == null || dateToFormat.equals(""))
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM,yyyy");
        Date date = new Date();
        try {
            date = sdf.parse(dateToFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }


    //    exmp 2016-04-19 to 19 Apr,2016
    public static String from_yyyyMMdd_To_dMMMyyyy(String dateToFormat) {
        if (dateToFormat == null || dateToFormat.equals(""))
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(dateToFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("d MMM,yyyy");
        return sdf.format(date);
    }

    //    from 2016-04-19 23:06:16 to 19 Apr, 2016 - 23:06:16
    public static String from_yyyyMMddHHmmss_To_dMMMyyyyHHmmss(String dateToFormat) {
        if (dateToFormat == null || dateToFormat.equals(""))
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(dateToFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("d MMM,yyyy - HH:mm:ss");
        return sdf.format(date);
    }

    //    from 19 Apr, 2016  to 2016-04-19
    public static String from_dMMMyyyy_To_yyyyMMdd(String dateToFormat) {
        if (dateToFormat == null || dateToFormat.equals(""))
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM,yyyy");
        Date date = new Date();
        try {
            date = sdf.parse(dateToFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

}
