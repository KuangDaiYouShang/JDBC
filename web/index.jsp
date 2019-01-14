<%--
  Created by IntelliJ IDEA.
  User: Cheng Li
  Date: 2019/1/12
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*, java.util.*, java.sql.*" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html>
<head>
    <title>Getting labels from DB</title>
</head>
<body>
    <%
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages.welcome");
    %>
    <p id="label"></p>
    <span style="display:none" id="one"><%=resourceBundle.getString("msg.first") %></span>
    <span style="display:none" id="two"><%=resourceBundle.getString("msg.second") %></span>
    <span style="display:none" id="three"><%=resourceBundle.getString("msg.third") %></span>
    <sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver"
        url="jdbc:mysql://127.0.0.1:3306/testdb?useSSL=true&useUnicode=true&characterSet=UTF-8"
        user="root" password="123456" />

    <sql:query dataSource="${snapshot}" var="result">
        select * from t_book;
    </sql:query>

    <table border="1" width="100%" id="bookTable">
        <c:forEach var="row" items="${result.rows}">
            <tr>
                <td><c:out value="${row.book_id}"/></td>
                <td><c:out value="${row.author}"/></td>
                <td><c:out value="${row.book_price}"/></td>
                <td><fmt:formatDate value="${row.book_date}" pattern="yyyy-MM-dd"></fmt:formatDate></td>
            </tr>
        </c:forEach>
    </table>
    <button>Register</button>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/welcome.js"></script>
</body>
</html>
