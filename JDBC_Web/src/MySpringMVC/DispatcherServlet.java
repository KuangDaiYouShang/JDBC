package MySpringMVC;

import MySpringMVC.util.ReflectUtil;
import annotation.MyAutoWired;
import annotation.MyController;
import annotation.MyRepository;
import annotation.MyService;
import com.bk.dao.bookDao;
import com.ruanmou.vip.orm.common.ArrayUtils;
import com.ruanmou.vip.orm.core.handler.mysql.MySQLTemplateHandler;
import org.dom4j.DocumentException;
import xml.XMLConfigurationParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@WebServlet(name = "DispatcherServlet")
public class DispatcherServlet extends HttpServlet {

    private List<Class<?>> classesList = Collections.synchronizedList(new ArrayList<Class<?>>());

    private Map<String, Object> contextContainer = Collections.synchronizedMap(new HashMap<String, Object>());

    private String contextConfigLocation = "applicationContext.xml";

    public void setContextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }


    @Override
    public void init() throws ServletException {
        //super.init();
        try {
            String path = XMLConfigurationParser.readXMLBasePackage(contextConfigLocation);
            scanBasePackageAnnotationClass(path);
            doIOC();
            //完成运行时以来对象的装配。
            doDI();
        } catch (Exception e) {
            e.printStackTrace();
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
        System.out.println(ds.contextContainer);
        bookDao bd = (bookDao) ds.getContextBean("bookDaoImpl");
        System.out.println(bd.getBookById(1001).getBookName());
    }
}
