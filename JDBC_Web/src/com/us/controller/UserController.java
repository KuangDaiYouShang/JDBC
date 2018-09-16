package com.us.controller;

import annotation.MyAutoWired;
import annotation.MyController;
import annotation.MyRequestMapping;
import com.us.entity.UserEntity;
import com.us.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@MyController
@MyRequestMapping(value = "user")
public class UserController {

    @MyAutoWired
    private UserService userService;

    @MyRequestMapping("login")
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, UserEntity user, String al) throws IOException {
        System.out.println("正在尝试登陆");
        System.out.println(user.getAccount());
        System.out.println(user.getPassword());
        System.out.println(al);

        boolean flag = userService.login(user);

        if(flag) {
            session.setAttribute("account", user.getAccount());
            if(al != null) {
                Cookie userCookie = new Cookie("al", user.getAccount()+"_"+user.getPassword());
                userCookie.setMaxAge(3600*24);
                userCookie.setPath(request.getContextPath()+"/");
                response.addCookie(userCookie);
            } else {
                Cookie userCookie = new Cookie("al", null);
                userCookie.setMaxAge(0);
                userCookie.setPath(request.getContextPath()+"/");
                response.addCookie(userCookie);
            }
            return "redirect:/book/listBook";
        } else {
            //return "redirect:/login.jsp";
            String url = request.getContextPath()+"/login.jsp";
            response.getWriter().print("<script>alert('登陆失败');location.href='"+url +"'</script>");
        }
        return null;
    }
}
