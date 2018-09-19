<%--
  Created by IntelliJ IDEA.
  User: Cheng Li
  Date: 2018/9/14
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Cookie.jsp"%>
<html>
<head>
    <base href="${pageContext.request.contextPath}/" />
    <title>登陆</title>
</head>
<body>
    <fieldset style="margin:10% auto;width:30%;">
        <legend>Entry</legend>
        <form action="user/login" method="post">
            <p>账号：<input type="text" name="account" value="${account == null ? "" : account}"></p>
            <p>密码：<input type="text" name="password" value="${password == null ? "" : password}"></p>
            <p><input type="checkbox" ${al == null ? "" : "checked='checked'"} name="al" id="al"><label for="al">自动登陆</label></p>
            <p><input type="submit" value="LOGIN"></p>
        </form>
    </fieldset>
</body>
</html>
