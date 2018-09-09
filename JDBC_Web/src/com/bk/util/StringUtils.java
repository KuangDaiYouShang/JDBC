package com.bk.util;

public class StringUtils {
    public static String stringTrim(String s, String prefix) {
        if(s.startsWith("redirect:")) {
            String newS = s.replace(prefix, "");
            int index = newS.lastIndexOf("/");
            String start = newS.substring(0,index);
            String end = newS.substring(index+1);
            return start+"?param="+end;
        } else {
            String newS = s.replace(prefix, "");
            return newS;
        }
    }
}
