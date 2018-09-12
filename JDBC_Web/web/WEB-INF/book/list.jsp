<%--
  Created by IntelliJ IDEA.
  User: Cheng Li
  Date: 2018/9/1
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
    <title>图书列表</title>
    <base href = "${pageContenxt.request.contextPath}/" />
    <!-- 引入样式 -->
    <link rel="stylesheet" href="css/main.css" />
</head>
<body>
<table class="tb">
    <tr>
        <th>编号</th>
        <th>名称</th>
        <th>作者</th>
        <th>价格</th>
        <th>时间</th>
        <th>操作</th>
    </tr>
    <c:forEach items="${books}" var="book">
    <tr>
        <td>${book.bookId}</td>
        <td>${book.bookName}</td>
        <td>${book.bookAuthor}</td>
        <td>${book.bookPrice}</td>
        <td><fmt:formatDate value="${book.bookDate}" pattern="yyyy-MM-dd"></fmt:formatDate></td>
        <td><a href="book/initUpdate?bookId=${book.bookId}">更新</a>|<a href="book/deleteBook?bookId=${book.bookId}">删除</a></td>
    </tr>
    </c:forEach>
</table>
<caption><a href="book/initAdd">添加图书</a></caption>
</body>
</html>
