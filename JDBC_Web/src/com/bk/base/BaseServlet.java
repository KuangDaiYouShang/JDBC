package com.bk.base;

import com.bk.util.BeanUtils;
import com.ruanmou.vip.orm.common.ArrayUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseServlet extends HttpServlet {
    @Override
/*    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodName = req.getParameter("param");
        try {
            Method method = this.getClass().
                    getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            String returnType = method.getReturnType().getSimpleName().toLowerCase();
            if("string".equals(returnType)) {
                Object url = method.invoke(this, req, resp);
                //Dispatch
                req.getRequestDispatcher(url+"").forward(req,resp);
            } else if ("void".equals(returnType)){
                method.invoke(this, req, resp);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodName = req.getParameter("param");

        List<Object> paramObjects = new ArrayList<>();

        Class<? extends BaseServlet> aClass = this.getClass();

        Method[] declaredMethods = aClass.getDeclaredMethods();
        try {
            Method method = null;

            if(ArrayUtils.isNotEmpty(declaredMethods)) {
                for(Method md : declaredMethods) {
                    if(md.getName().equals(methodName)) {
                        method = md;
                        break;
                    }
                }
            }

            paramObjects.add(req);
            paramObjects.add(resp);

            Class<?>[] parameterTypes = method.getParameterTypes();
            if(ArrayUtils.isNotEmpty(parameterTypes)) {
                for(Class<?> c : parameterTypes) {
                    if(!c.isInterface()) {
                        paramObjects.add(BeanUtils.params2Fields(req, c));
                    }
                }
            }

            String returnType = method.getReturnType().getSimpleName().toLowerCase();

            if("string".equals(returnType)) {
                Object url = null;
                if(paramObjects.size() > 2) {
                    url = method.invoke(this,paramObjects.toArray());
                } else {
                    url = method.invoke(this, req, resp);
                }
                //Dispatch
                req.getRequestDispatcher(url+"").forward(req,resp);
            } else if ("void".equals(returnType)){
                if(paramObjects.size()>2) {
                    method.invoke(this, paramObjects.toArray());
                } else {
                    method.invoke(this, req, resp);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
