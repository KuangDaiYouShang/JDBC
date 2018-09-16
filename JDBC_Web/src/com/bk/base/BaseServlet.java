package com.bk.base;

import com.bk.util.BeanUtils;
import com.bk.util.StringUtils;
import com.ruanmou.vip.orm.common.ArrayUtils;
import com.sun.deploy.net.HttpResponse;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.bk.util.ClassUtils.isSystemClass;

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

        Object url = null;
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

            Class<?>[] parameterTypes = method.getParameterTypes();
            if(ArrayUtils.isNotEmpty(parameterTypes)) {
                for(Class<?> c : parameterTypes) {
                    if(!c.isInterface()) {
                        if(!isSystemClass(c)) {
                            paramObjects.add(BeanUtils.params2Fields(req, c));
                        } else {
                            paramObjects.add(BeanUtils.params2SystemClass(req, c, null));
                        }
                    } else {
                        if(c == HttpServletRequest.class) {
                            paramObjects.add(req);
                        } else if (c == HttpServletResponse.class) {
                            paramObjects.add(resp);
                        } else if (c == HttpSession.class) {
                            paramObjects.add(req.getSession());
                        } else if (c == ServletContext.class) {
                            paramObjects.add(this.getServletContext());
                        }
                    }
                }
            }
            //invoke the method
            if(paramObjects != null && paramObjects.size() > 0) {
               url = method.invoke(this, paramObjects.toArray());
            } else {
                url = method.invoke(this);
            }

            if(url != null) {
                String urlString = url.toString();
                if(urlString.startsWith("forward:")) {
                    req.getRequestDispatcher(StringUtils.stringTrim(urlString, "forward:")).forward(req,resp);
                } else if (urlString.startsWith("redirect")) {
                    resp.sendRedirect(StringUtils.stringTrim(req.getContextPath()+urlString, "redirect:"));
                } else {
                    req.getRequestDispatcher(urlString).forward(req,resp);
                }
            } else {
                System.out.println("访问的"+ url + "路径不存在");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
