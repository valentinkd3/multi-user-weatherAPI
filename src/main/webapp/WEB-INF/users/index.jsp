<%--
  Created by IntelliJ IDEA.
  User: vkozh
  Date: 19.10.2023
  Time: 15:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.kozhevnikov.weatherapp.entity.User" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Список пользователей</title>
</head>
<body>
<h1>Список пользователей:</h1>
<%
    List<User> users = (List<User>) request.getAttribute("users");
    for(User user : users){
        String userPagePath = request.getContextPath()+"/users/"+user.getId();
        out.println("<a href="+userPagePath+">" + user.getUsername() + "</a><br>");
    }
%>
<br>
<a href="/users/new">Создать нового пользователя</a>
</body>
</html>
