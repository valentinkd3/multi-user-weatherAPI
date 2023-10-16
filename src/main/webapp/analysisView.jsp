<%--
  Created by IntelliJ IDEA.
  User: vkozh
  Date: 15.10.2023
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.*" %>
<%@ page import="ru.kozhevnikov.weatherapp.model.City" %>
<%@ page import="ru.kozhevnikov.weatherapp.model.Weather" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Анализ данных о погоде</title>
</head>
<body>
  <%
    List<City> cities = (List<City>) request.getAttribute("cities");
    City hotCity = (City) request.getAttribute("hot");
    City coldCity = (City) request.getAttribute("cold");
    out.println("<h1>Сегодня " + cities.get(0).getWeather().getDate() + ". Сводные данные о погоде: " + "</h1>");
    for(City city : cities){
      Weather weather = city.getWeather();
      out.println("<p>" + city.getName() + ": Средняя температура : " + weather.getAverageTemperature() + " °C. " +
              "Облачность : " + weather.getCloud() + " %. " + " Количество осадков : " +
      weather.getPrecipitation() + " мм. " + weather.getCurrentText() + ".</p>");
    }
  %>
  <p>
    <%=
      "Самый теплый город - " + hotCity.getName() + "."
    %>
  </p>
  <p>
    <%=
    "Самый холодный город - " + coldCity.getName() + "."
    %>
  </p>
</body>
</html>
