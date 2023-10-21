<%@ page import="ru.kozhevnikov.weatherapp.entity.User" %>
<%@ page import="java.time.temporal.ChronoUnit" %>
<%@ page import="ru.kozhevnikov.weatherapp.entity.UserCity" %>
<%@ page import="java.util.List" %><%--
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
    List<UserCity> journal = (List<UserCity>) request.getAttribute("journal");
%>
<p>Имя пользователя: <%= user.getUsername() %>
</p>
<form action="<%= editPath %>" method="GET" accept-charset="UTF-8">
    <input type="submit" value="Редактировать пользователя">
</form>
<form action="/users/*" method="POST" accept-charset="UTF-8">
    <input type="submit" value="Удалить пользователя">
</form>
<br>
<hr>
<h2>Журнал просмотра погоды</h2>
<%
    for (UserCity record : journal) {
        out.println("<p>Дата: " + record.getCreateAt().toLocalDate() +
                    ". Время: " + record.getCreateAt().toLocalTime().truncatedTo(ChronoUnit.SECONDS) +
                    ". Город: " + record.getCity().getName() + "</p>");
    }

%>
</body>
</html>
