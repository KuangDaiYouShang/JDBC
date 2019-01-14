package com.bk.util;

import com.ruanmou.vip.orm.common.ArrayUtils;
import com.us.entity.UserEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static String getCookiesByName(String cookieName, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if(ArrayUtils.isNotEmpty(cookies)) {
            for(Cookie c : cookies) {
                String name = c.getName();
                if(cookieName.equals(name)) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    public static void addCookie(HttpServletRequest req, HttpServletResponse resp, String flag, UserEntity user) {
        String al = req.getParameter(flag);
        if(al == "on") {
            Cookie userCookie = new Cookie(flag, user.getAccount()+"_"+user.getPassword());
            userCookie.setMaxAge(3600*24);
            userCookie.setPath(req.getContextPath()+"/");
            resp.addCookie(userCookie);
        } else {
            Cookie userCookie = new Cookie("al", null);
            userCookie.setMaxAge(0);
            userCookie.setPath(req.getContextPath()+"/");
            resp.addCookie(userCookie);
        }

    }
}
