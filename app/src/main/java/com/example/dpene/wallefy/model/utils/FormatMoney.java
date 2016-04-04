package com.example.dpene.wallefy.model.utils;

import java.text.DecimalFormat;

/**
 * Created by Petkata on 24.2.2016 г..
 */
public class FormatMoney {
    public static String format(double money){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        return String.valueOf(df.format(money));
    }
}
