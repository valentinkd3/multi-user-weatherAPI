<%--
  Created by IntelliJ IDEA.
  User: vkozh
  Date: 20.10.2023
  Time: 11:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Авторизация пользователя</title>
</head>
<body>
<h1>Авторизация пользователя</h1>
    <form action="/login" method="post" accept-charset="UTF-8">
        <label for="username">Имя пользователя: </label>
        <input type="text" name="username" id="username" required><br><br>

        <label for="password">Пароль: </label>
        <input type="text" name="password" id="password" required><br><br>

        <input type="submit" value="Войти">
    </form>

</body>
</html>
