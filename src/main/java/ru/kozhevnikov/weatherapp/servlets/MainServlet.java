package ru.kozhevnikov.weatherapp.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import ru.kozhevnikov.weatherapp.api.WeatherApiService;
import ru.kozhevnikov.weatherapp.data.WeatherDataProcessor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@WebServlet("/get_weather")
public class MainServlet extends HttpServlet {
    private static String apiKey;
    private static String apiKeyForHours;
    private String location;
    private WeatherApiService weatherApiService;
    private WeatherDataProcessor weatherDataProcessor;
    private static final String NO_PARAMS = "Передайте в параметры запроса город, в готором Вы хотите узнать погоду";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
            apiKey = properties.getProperty("api");
            apiKeyForHours = properties.getProperty("apiHour");
            weatherApiService = new WeatherApiService(apiKey, apiKeyForHours);
            weatherDataProcessor = new WeatherDataProcessor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        location = req.getParameter("location");

        if (location == null) {
            resp.getWriter().write(NO_PARAMS);
        } else {
            session.setAttribute("location", location);

            JsonNode locationData = weatherApiService.getCoordinatesByLocation(location);

            double[] coordinates = weatherDataProcessor.getCoordinates(locationData);
            JsonNode currentWeatherData = weatherApiService.getCurrentWeatherByCoordinates(coordinates[0], coordinates[1]);
            JsonNode hourlyWeatherData = weatherApiService.getHourlyWeatherByCityName(location);
            JsonNode weatherForecast = weatherApiService.getWeatherForecastByCityName(location);

            String localName = weatherDataProcessor.getLocalName(locationData);
            List<String> currentWeather = weatherDataProcessor.getCurrentWeather(currentWeatherData);
            Map<Integer,String> dayWeatherPerHours = weatherDataProcessor.getHourlyWeather(hourlyWeatherData);
            Map<Integer, String> threeDaysWeatherForecast = weatherDataProcessor.getWeatherForecast(weatherForecast);

            req.setAttribute("location", localName);
            req.setAttribute("currentTemp", currentWeather.get(0));
            req.setAttribute("currentFeels", currentWeather.get(1));
            req.setAttribute("weatherPerHours", dayWeatherPerHours);
            req.setAttribute("forecast", threeDaysWeatherForecast);

            RequestDispatcher dispatcher = req.getRequestDispatcher("/weatherView.jsp");

            dispatcher.forward(req, resp);
        }
    }
}
