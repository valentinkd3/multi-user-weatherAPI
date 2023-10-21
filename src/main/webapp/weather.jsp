<%--
  Created by IntelliJ IDEA.
  User: vkozh
  Date: 11.10.2023
  Time: 11:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.*" %>
<%@ page import="ru.kozhevnikov.weatherapp.entity.City" %>
<%@ page import="ru.kozhevnikov.weatherapp.entity.Weather" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Прогноз погоды</title>
</head>
<body>
<h1>
    <%
        Weather weather = (Weather) request.getAttribute("weather");
        String cityName = weather.getCity().getName();
        out.println(cityName + " - погода на текущий момент:");
    %>
</h1>
<p>
    <%=
        "Температура воздуха: " + weather.getCurTemp() + " °C, " + weather.getText()
    %>
</p>
<p>
    <%= "Ощущается как: " + weather.getFeelsLike() + " °C." %>
</p>
<p>
    <%= "Облачность: " + weather.getCloud() + " %." %>
</p>
<p>
    <%= "Количество осадков: " + weather.getPrecipitation() + " мм." %>
</p>
</body>
</html>
