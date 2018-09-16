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
            if(!ClassUtils.isFieldInClass(clazz,fieldName)){
                continue;
            }
            /*if("param".equals(fieldName)) {
                continue;
            }*/
            try {
                Field field = clazz.getDeclaredField(fieldName);
                Class<?> fieldType = field.getType();

                String value = request.getParameter(fieldName);

                Object fieldValue = ClassUtils.converType(field.getType(), value);

                field.setAccessible(true);
                field.set(instance, fieldValue);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static Object params2SystemClass(HttpServletRequest request, Class<?> clazz, String paramName) throws Exception {

        Enumeration<String> params = request.getParameterNames();
        Object fieldValue = null;

        while(params.hasMoreElements()) {
            String fieldName = params.nextElement();
            /*if("param".equals(fieldName)) {
                continue;
            }*/

            //判断request中参数名是否与形参名一致
            if(!fieldName.equals(paramName)) {
                continue;
            }

            String value = request.getParameter(fieldName);
            fieldValue = ClassUtils.converType(clazz, value);
            break;//这里为什么break？？？
            }
        return fieldValue;
    }
}
