package MySpringMVC.util.javassits;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Classes {
    private Classes() {};

    protected static String[] getMethodParamNames(CtMethod cm) {
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                .getAttribute(LocalVariableAttribute.tag);

        String[] paramNames = null;
        try {
            paramNames = new String[cm.getParameterTypes().length];
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i + pos);
        }
        return paramNames;
    }

    public static String[] getMethodParamNames(Class<?> clazz, String method,
                                               Class<?>... paramTypes) {

        ClassPool pool = ClassPool.getDefault();
        CtClass cc = null;
        CtMethod cm = null;
        try {
            cc = pool.get(clazz.getName());

            String[] paramTypeNames = new String[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++)
                paramTypeNames[i] = paramTypes[i].getName();

            cm = cc.getDeclaredMethod(method, pool.get(paramTypeNames));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return getMethodParamNames(cm);
    }

    public static String[] getMethodParamNames(Method method) {

        ClassPool pool = ClassPool.getDefault();
        CtClass cc;
        CtMethod cm = null;
        try {
            cc = pool.get(method.getDeclaringClass().getName());
            cm = cc.getDeclaredMethod(method.getName());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return getMethodParamNames(cm);
    }
}