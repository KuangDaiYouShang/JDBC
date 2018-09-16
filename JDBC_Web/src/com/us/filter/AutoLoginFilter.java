package com.us.filter;

import com.ruanmou.vip.orm.common.ArrayUtils;

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
        if(account != null) {
            chain.doFilter(request, response);
        } else {
            String url = request.getContextPath()+"/login.jsp";
            response.getWriter().print("<script>alert('未登录，请登录后操作');location.href='"+url +"'</script>");
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
