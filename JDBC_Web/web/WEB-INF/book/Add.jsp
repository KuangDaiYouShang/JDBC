<%--
  Created by IntelliJ IDEA.
  User: Cheng Li
  Date: 2018/9/4
  Time: 22:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
    <title>添加图书</title>
    <base href = "${pageContenxt.request.contextPath}/" />
    <!-- 引入样式 -->
    <link rel="stylesheet" href="css/main.css" />
</head>
<body>
    <form action="book?param=add" method="post">
        <table class="tb">
            <tr>
                <th>书名</th>
                <td><input type="text" name="bookName" placeholder="请输入书名"></td>
            </tr>
            <tr>
                <th>作者</th>
                <td><input type="text" name="bookAuthor" placeholder="请输入作者"></td>
            </tr>
            <tr>
                <th>价格</th>
                <td><input type="text" name="bookPrice" placeholder="请输入价格"></td>
            </tr>
            <tr>
                <th>日期</th>
                <td><input type="date" name="bookDate" placeholder="请输入日期"></td>
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
