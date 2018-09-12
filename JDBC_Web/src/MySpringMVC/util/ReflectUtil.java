package MySpringMVC.util;

import annotation.MyController;
import annotation.MyRepository;
import annotation.MyService;

public class ReflectUtil {
    private static String getLowerAllias(Class<?> cl) {
        String simpleName = cl.getSimpleName();
        String start = simpleName.substring(0,1).toLowerCase();
        String end = simpleName.substring(1);
        return start + end;
    }

    public static String getAnnotationAllias(Class<?> cl) {
        String annoName = getLowerAllias(cl);
        MyRepository myRepository = cl.getAnnotation(MyRepository.class);
        MyController myController = cl.getAnnotation(MyController.class);
        MyService myService = cl.getAnnotation(MyService.class);

        if(myController != null && !myController.value().equals("") ) {
            annoName = myController.value();
        } else if (myRepository != null && !myRepository.value().equals("")) {
            annoName = myRepository.value();
        } else if (myService != null && !myService.value().equals("")) {
            annoName = myService.value();
        }
        return annoName;
    }

    public static String handleUrl(String s) {
        if(!s.startsWith("/")) {
            return "/" + s;
        }
        return s;
    }
}
