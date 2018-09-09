package com.bk.util;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

public class BeanUtils {
    public static <T> T  params2Fields(HttpServletRequest request, Class<T> clazz) throws Exception {
        T instance = clazz.newInstance();

        Enumeration<String> params = request.getParameterNames();

        while(params.hasMoreElements()) {
            String fieldName = params.nextElement();
            if("param".equals(fieldName)) {
                continue;
            }
            try {
                Field field = clazz.getDeclaredField(fieldName);
                Class<?> fieldType = field.getType();

                String value = request.getParameter(fieldName);
                Object fieldValue = null;
                if (fieldType == Integer.class) {
                    fieldValue = Integer.valueOf(value);
                } else if (fieldType == String.class) {
                    fieldValue = value;
                } else if (fieldType == Double.class) {
                    fieldValue = Double.valueOf(value);
                } else if (fieldType == Date.class) {
                    fieldValue = new SimpleDateFormat("yyyy-MM-dd").parse(value);
                } else if (fieldType == BigDecimal.class) {
                    fieldValue = new BigDecimal(value);
                } else if (fieldType == double.class) {
                    fieldValue = Double.parseDouble(value);
                } else if (fieldType == int.class) {
                    fieldValue = Integer.parseInt(value);
                }

                field.setAccessible(true);
                field.set(instance, fieldValue);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static Object params2SystemClass(HttpServletRequest request, Class<?> clazz) throws Exception {

        Enumeration<String> params = request.getParameterNames();
        Object fieldValue = null;

        while(params.hasMoreElements()) {
            String fieldName = params.nextElement();
            if("param".equals(fieldName)) {
                continue;
            }
            try {

                String value = request.getParameter(fieldName);
                if (clazz == Integer.class) {
                    fieldValue = Integer.valueOf(value);
                } else if (clazz == String.class) {
                    fieldValue = value;
                } else if (clazz == Double.class) {
                    fieldValue = Double.valueOf(value);
                } else if (clazz == Date.class) {
                    fieldValue = new SimpleDateFormat("yyyy-MM-dd").parse(value);
                } else if (clazz == BigDecimal.class) {
                    fieldValue = new BigDecimal(value);
                } else if (clazz == double.class) {
                    fieldValue = Double.parseDouble(value);
                } else if (clazz == int.class) {
                    fieldValue = Integer.parseInt(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fieldValue;
    }
}