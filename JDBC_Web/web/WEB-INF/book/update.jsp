<%--
  Created by IntelliJ IDEA.
  User: Cheng Li
  Date: 2018/9/2
  Time: 22:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
    <title>图书更新页面</title>
    <base href = "${pageContenxt.request.contextPath}/" />
    <!-- 引入样式 -->
    <link rel="stylesheet" href="css/main.css" />
</head>
<body>
    <form action="book?param=update" method="post">
        <table class="tb">
            <tr>
                <th>编号</th>
                <td><input type="text" name="bookId" value="${book.bookId}"></td>
            </tr>
            <tr>
                <th>名称</th>
                <td><input type="text" name="bookName" value="${book.bookName}"></td>
            </tr>
            <tr>
                <th>作者</th>
                <td><input type="text" name="bookAuthor" value="${book.bookAuthor}"></td>
            </tr>
            <tr>
                <th>价格</th>
                <td><input type="text" name="bookPrice" value="${book.bookPrice}"></td>
            </tr>
            <tr>
                <th>日期</th>
                <td><input type="date" name="bookDate" value="<fmt:formatDate value="${book.bookDate}" pattern="yyyy-MM-dd" />"></td>
            </tr>
            <tr>
                <td colspan="2">
                    <button>提交</button>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>
