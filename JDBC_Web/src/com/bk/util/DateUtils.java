package com.bk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static Date string2Date(String s) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sim.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
