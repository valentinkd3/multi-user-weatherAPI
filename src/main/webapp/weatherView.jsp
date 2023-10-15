<%--
  Created by IntelliJ IDEA.
  User: vkozh
  Date: 11.10.2023
  Time: 11:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.*" %>
<%@ page import="ru.kozhevnikov.weatherapp.model.City" %>
<%@ page import="ru.kozhevnikov.weatherapp.model.Weather" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Прогноз погоды</title>
</head>
<body>
<h1>
    <%
        City city = (City) request.getAttribute("city");
        Weather weather = city.getWeather();
        out.println(city.getName() + " - погода на текущий момент:");
    %>
</h1>
<p>
    <%=
        "Температура воздуха: " + weather.getCurrentTemperature() + " °C, " + weather.getCurrentText()
    %>
</p>
<p>
    <%= "Ощущается как: " + weather.getCurrentTemperatureFeelsLike() + " °C." %>
</p>
<h2>Погода в ближайшие дни</h2>
<%
    Map<String, Integer> forecast = weather.getForecastWeather();
    for (Map.Entry<String,Integer> forecastWeather : forecast.entrySet()) {
        out.println("<p>" + forecastWeather.getKey() + " : " + forecastWeather.getValue() +" °C.</p>");
    }
%>
<h2>Погода сегодня в течение дня</h2>
<%
    Map<String, Integer> hourly = weather.getHourlyWeather();
    for (Map.Entry<String,Integer> hour : hourly.entrySet()) {
        out.println("<p>" + hour.getKey() + " : " + hour.getValue() + " °C.</p>");
    }
%>
</body>
</html>
