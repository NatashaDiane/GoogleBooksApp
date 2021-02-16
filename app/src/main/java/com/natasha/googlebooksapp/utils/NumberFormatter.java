package com.natasha.googlebooksapp.utils;

import java.text.DecimalFormat;

//a utility class that format numbers
public class NumberFormatter {
    public static String formatNumber(int value){
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formatted = formatter.format(value);
        return formatted;
    }
}