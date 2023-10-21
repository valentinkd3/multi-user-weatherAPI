<%--
  Created by IntelliJ IDEA.
  User: vkozh
  Date: 19.10.2023
  Time: 17:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Редактирование пользователя</title>
</head>
<body>
<h1>Страница редактирования пользователя</h1>
<form action="/users/edit/" method="POST" accept-charset="UTF-8">
  <label for="username">Имя пользователя</label>
  <input type="text" name="username" id="username" required><br><br>

  <label for="password">Пароль</label>
  <input type="text" name="password" id="password" required><br><br>

  <input type="submit" value="Редактировать">
</form>
</body>
</html>
