package com.bk.util;

public class ClassUtils {
    public static boolean isSystemClass(Class<?> cl) {
        if(cl != null && cl.getClassLoader() == null) {
            return true;
        }
        return false;
    }
}
