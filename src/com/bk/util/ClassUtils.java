package com.bk.util;

import com.ruanmou.vip.orm.common.ArrayUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClassUtils {

    private static final String SERIALIZE_UID = "serialVersionUID";

    public static boolean isSystemClass(Class<?> cl) {
        if(cl != null && cl.getClassLoader() == null) {
            return true;
        }
        return false;
    }

    public static  boolean isFieldInClass(Class<?> cl, String fieldName) {
        Field[] fields = cl.getDeclaredFields();
        if(ArrayUtils.isNotEmpty(fields)) {
            for(Field f : fields) {
                if(SERIALIZE_UID.equals(f.getName())) {
                    continue;
                }

                if(fieldName.equals(f.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    把一个字符串转换为对应字段类型
    **/
    public static Object converType(Class<?> fieldType, String value) throws Exception {

        //Class<?> fieldType = field.getType();
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
        } else if (fieldType == Boolean.class) {
            fieldValue = Boolean.valueOf(value);
        } else if (fieldType == boolean.class) {
            fieldValue = Boolean.parseBoolean(value);
        }
        return fieldValue;
    }
}
