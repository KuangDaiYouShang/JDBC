package com.bk.util;

public class StringUtils {
    public static String stringTrim(String s, String prefix) {
            String newS = s.replace(prefix, "");
            return newS;
    }
}
