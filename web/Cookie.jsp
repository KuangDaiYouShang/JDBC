<%@ page import="com.bk.util.CookieUtils" %><%--
  Created by IntelliJ IDEA.
  User: Cheng Li
  Date: 2018/9/18
  Time: 13:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String al = CookieUtils.getCookiesByName("al", request);
    if(al != null) {
        pageContext.setAttribute("account", al.split("_")[0]);
        pageContext.setAttribute("password", al.split("_")[1]);
        pageContext.setAttribute("al", al);
    }
%>