package com.us.filter;

import MySpringMVC.DispatcherServlet;
import com.bk.util.CookieUtils;
import com.ruanmou.vip.orm.common.ArrayUtils;
import com.ruanmou.vip.orm.core.handler.HandlerTemplate;
import com.ruanmou.vip.orm.core.handler.mysql.MySQLTemplateHandler;
import com.us.dao.UserDao;
import com.us.dao.impl.UserDaoImpl;
import com.us.entity.UserEntity;
import com.us.service.UserService;
import com.us.service.impl.UserServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@WebFilter(filterName = "AutoLoginFilter", urlPatterns = "/*")
public class AutoLoginFilter implements Filter {

    private Properties commUrls;


    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request= (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String property = commUrls.getProperty("com.url");
        String[] comms = property.split(",");

        String visitUrl = request.getRequestURI();

        //判断访问路径是否为公共访问资源
        if(ArrayUtils.isNotEmpty(comms)) {
            for(String s : comms) {
                if(visitUrl.contains(s)) {
                    chain.doFilter(request,response);
                    return;
                }
            }
        }


        Object account = request.getSession().getAttribute("account");
        String url = request.getContextPath()+"/login.jsp";
        if(account != null) {
            chain.doFilter(request, response);
        } else {
            String al = CookieUtils.getCookiesByName("al", request);
            if (al != null) {
                String userName = al.split("_")[0];
                String password = al.split("_")[1];
                UserEntity u = new UserEntity();
                u.setAccount(userName);
                u.setPassword(password);

                //调用的是UserService而非UserController中的方法，故需要实例化
                HandlerTemplate template = new MySQLTemplateHandler();
                UserDao ud = new UserDaoImpl();
                UserService usv = new UserServiceImpl();
                ud.setTemplate(template);
                usv.setUserDao(ud);
                //UserService usv = (UserService) new DispatcherServlet().getContextBean("userServiceImpl");
                boolean flag = usv.login(u);

                if (flag) {
                    request.getSession().setAttribute("account",userName);
                    chain.doFilter(request, response);
                } else {
                    response.getWriter().print("<script>alert('cookie失效，');location.href='" + url + "'</script>");
                }
            }
            response.getWriter().print("<script>alert('未登录，请登录后操作');location.href='" + url + "'</script>");
        }

    }
    public void init(FilterConfig config) throws ServletException {
        commUrls = new Properties();
        try {
            commUrls.load(this.getClass().getClassLoader().getResourceAsStream("commonUrl.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
