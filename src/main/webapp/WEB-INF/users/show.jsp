<%@ page import="ru.kozhevnikov.weatherapp.entity.User" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="ru.kozhevnikov.weatherapp.entity.City" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.time.temporal.ChronoUnit" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: vkozh
  Date: 19.10.2023
  Time: 16:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Страница пользователя</title>
</head>
<body>
<h1>Страница пользователя:</h1>
<%
    User user = (User) request.getAttribute("user");
    String editPath = "/users/edit/" + user.getId();
    Map<LocalDateTime, City> journal = (LinkedHashMap <LocalDateTime, City>) request.getAttribute("journal");
%>
<p>Имя пользователя: <%= user.getUsername() %></p>
<form action="<%= editPath %>" method="GET" accept-charset="UTF-8">
    <input type="submit" value="Редактировать пользователя">
</form>
<form action="/users/*" method="POST" accept-charset="UTF-8">
    <input type="submit" value="Удалить пользователя">
</form>
<br><hr>
<h2>Журнал просмотра погоды</h2>
<%
    for(Map.Entry<LocalDateTime, City> entry : journal.entrySet()){
        out.println("<p>Дата: " + entry.getKey().toLocalDate() + ". Время: " + entry.getKey().toLocalTime().truncatedTo(ChronoUnit.SECONDS)
                + ". Город: " + entry.getValue().getName() + "</p>");
    }

%>
</body>
</html>
