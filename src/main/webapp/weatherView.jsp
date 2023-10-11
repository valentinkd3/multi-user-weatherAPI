<%--
  Created by IntelliJ IDEA.
  User: vkozh
  Date: 11.10.2023
  Time: 11:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Прогноз погоды</title>
</head>
<body>
<h1>
    <%= request.getAttribute("location") + " - погода на текущий момент:" %>
</h1>
<p>
    <%= "Температура воздуха: " + request.getAttribute("currentTemp") + " °C, " + request.getAttribute("currentText") %>
</p>
<p>
    <%= "Ощущается как: " + request.getAttribute("currentFeels") + " °C." %>
</p>
<h2>Погода в ближайшие дни</h2>
<%
    Map<Integer, String> forecast = (Map<Integer, String>) request.getAttribute("forecast");
    for (int i = 0; i < forecast.size(); i++) {
        out.println("<p>" + forecast.get(i) + "</p>");
    }
%>
<h2>Погода сегодня в течение дня</h2>
<%
    Map<Integer, String> weather = (Map<Integer, String>) request.getAttribute("weatherPerHours");
    for (int i = 0; i < weather.size(); i++) {
        out.println("<p>" + weather.get(i) + "</p>");
    }
%>
</body>
</html>
