package com.example.freetradewip;

import java.text.DecimalFormat;

public class Utils {

    public static double formatDouble(double money){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(money));
    }
}
