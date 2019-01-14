package MySpringMVC;

import MySpringMVC.util.ReflectUtil;
import MySpringMVC.util.javassits.Classes;
import annotation.*;
import com.bk.dao.bookDao;
import com.bk.util.BeanUtils;
import com.bk.util.StringUtils;
import com.ruanmou.vip.orm.common.ArrayUtils;
import com.ruanmou.vip.orm.core.handler.mysql.MySQLTemplateHandler;
import org.dom4j.DocumentException;
import xml.XMLConfigurationParser;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static MySpringMVC.util.ReflectUtil.handleUrl;
import static com.bk.util.ClassUtils.isSystemClass;

//@WebServlet(name = "DispatcherServlet")
public class DispatcherServlet extends HttpServlet {

    private List<Class<?>> classesList = Collections.synchronizedList(new ArrayList<Class<?>>());

    private Map<String, Object> contextContainer = Collections.synchronizedMap(new HashMap<String, Object>());

    private String contextConfigLocation;

    private Map<String, Object> urlMappingContext = Collections.synchronizedMap(new HashMap<String, Object>());

    public DispatcherServlet() {
        System.out.println("初始化成功");
    }

    public void setContextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation.replace("classpath:","");
    }


    @Override
    public void init() throws ServletException {

        System.out.println(getInitParameterNames());
        setContextConfigLocation(getInitParameter("Location"));
        try {
            String path = XMLConfigurationParser.readXMLBasePackage(contextConfigLocation);
            scanBasePackageAnnotationClass(path);
            doIOC();
            //完成运行时以来对象的装配。
            doDI();
            urlMappingToMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String cont = req.getContextPath();
        String path = uri.replace(cont, "");
        Method method = (Method)urlMappingContext.get(path);
        List<Object> paramObjects = null;
        //定义一个存储形参名称与形参类型的Map
        Map<String, Class<?>> paramMappingToClass = new LinkedHashMap<String, Class<?>>();

        Object url = null;
        if(method != null) {
            try {
                Class<?>[] parameterTypes = method.getParameterTypes();
                String[] methodParamNames = Classes.getMethodParamNames(method);
                for(int i = 0; i < parameterTypes.length; i++) {
                    paramMappingToClass.put(methodParamNames[i], parameterTypes[i]);
                }
                if(!paramMappingToClass.isEmpty()) {
                    paramObjects = new ArrayList<Object>();
                    for(Map.Entry<String, Class<?>> map : paramMappingToClass.entrySet()) {
                        Class<?> c = map.getValue();
                        if(!c.isInterface()) {
                            if(!isSystemClass(c)) {
                                paramObjects.add(BeanUtils.params2Fields(req, c));
                            } else {
                                paramObjects.add(BeanUtils.params2SystemClass(req, c, map.getKey()));
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
                    /*for(Class<?> c : parameterTypes) {
                        if(!c.isInterface()) {
                            if(!isSystemClass(c)) {
                                paramObjects.add(BeanUtils.params2Fields(req, c));
                            } else {
                                paramObjects.add(BeanUtils.params2SystemClass(req, c));
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
                    }*/
                }

                //获取Controller类的实例。
                Class<?> c = method.getDeclaringClass();
                Object o = getContextBean(ReflectUtil.getAnnotationAllias(c));
                //从实例中调用方法。
                if(paramObjects != null && paramObjects.size() > 0) {
                    url = method.invoke(o, paramObjects.toArray());
                } else {
                    url = method.invoke(o);
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

    private void urlMappingToMethod() {
        if(classesList.size() == 0) {
            return;
        }
        for(int i = 0; i < classesList.size(); i++) {
            Class<?> cl = classesList.get(i);
            if(cl.isAnnotationPresent(MyRequestMapping.class)) {
                MyRequestMapping myRequestMapping = cl.getAnnotation(MyRequestMapping.class);
                String baseUrl = null;
                if(!myRequestMapping.value().equals("")) {
                    baseUrl = handleUrl(myRequestMapping.value());
                }
                Method[] methods = cl.getDeclaredMethods();
                if(ArrayUtils.isNotEmpty(methods)) {
                    for(Method m : methods) {
                        if(m.isAnnotationPresent(MyRequestMapping.class) &&
                                m.getModifiers() == Modifier.PUBLIC) {
                            MyRequestMapping myRequestMapping1 = m.getAnnotation(MyRequestMapping.class);
                            String methodUrlMapping = baseUrl;
                            if(!myRequestMapping1.value().equals("")) {
                                String url = handleUrl(myRequestMapping1.value());
                                methodUrlMapping = baseUrl + url;
                            }
                            urlMappingContext.put(methodUrlMapping, m);
                        }
                    }
                }
            }
        }
    }

    private void doDI() throws IllegalAccessException {
        if(classesList.size()==0) {
            return;
        }
        for(int i = 0; i < classesList.size(); i++) {
            Class<?> cl = classesList.get(i);
            String annoName = ReflectUtil.getAnnotationAllias(cl);
            Object oj = contextContainer.get(annoName);

            Field[] fields = cl.getDeclaredFields();
            if(ArrayUtils.isNotEmpty(fields)) {
                for (Field f : fields) {
                    if(f.isAnnotationPresent(MyAutoWired.class)) {
                        MyAutoWired myAutoWired = f.getAnnotation(MyAutoWired.class);
                        Object injectObj = null;
                        if(!myAutoWired.value().equals("")) {
                            String instanceName = myAutoWired.value();
                            injectObj = contextContainer.get(instanceName);
                        }else {
                            Class<?> fieldType = f.getType();
                            Collection values = contextContainer.values();
                            Iterator it = values.iterator();
                            while(it.hasNext()) {
                                Object o = it.next();
                                if(fieldType.isAssignableFrom(o.getClass())) {
                                    injectObj = o;
                                    break;
                                }
                            }
                        }
                        f.setAccessible(true);
                        f.set(oj, injectObj);
                    }
                }
            }
        }

    }

    private void doIOC() throws Exception {
        if(classesList.size()==0) {
            return;
        }
        for(int i = 0; i < classesList.size(); i++) {
            Class<?> clazz = classesList.get(i);
            String annoName= ReflectUtil.getAnnotationAllias(clazz);
            contextContainer.put(annoName, clazz.newInstance());
        }
        //加入模板
        String templateName = ReflectUtil.getAnnotationAllias(MySQLTemplateHandler.class);
        contextContainer.put(templateName, new MySQLTemplateHandler());

    }

    private void scanBasePackageAnnotationClass(String path) throws URISyntaxException {
        URL url = this.getClass().getClassLoader().getResource(path.replace(".", "/"));
        File file = new File(url.toURI());

        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File childFile) {
                if(childFile.isDirectory()) {
                    try {
                        scanBasePackageAnnotationClass(path+"."+childFile.getName());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    if(childFile.getName().endsWith(".class")) {
                        String className = childFile.getName().replace(".class", "");
                        try {
                            Class<?> clazz = this.getClass().getClassLoader().loadClass(path+"."+className);
                            if(clazz.isAnnotationPresent(MyController.class)||
                                    clazz.isAnnotationPresent(MyRepository.class)||
                                    clazz.isAnnotationPresent(MyService.class)) {
                                System.out.println(clazz.getName());
                                classesList.add(clazz);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });
    }

    public Object getContextBean(String beanName) {
        return contextContainer.get(beanName);
    }

    public static void main(String[] args) throws ServletException {
        DispatcherServlet ds = new DispatcherServlet();
        ds.init();
        System.out.println(ds.urlMappingContext);
    }
}
