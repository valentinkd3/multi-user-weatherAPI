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
    private static String apiKeyCurrent;
    private static String apiKeyForecast;
    private String location;
    private WeatherApiService weatherApiService;
    private WeatherDataProcessor weatherDataProcessor;
    private static final String NO_PARAMS = "Передайте в параметры запроса город, в готором Вы хотите узнать погоду";
    private static final String INCORRECT_NAME = "Название города введено некорректо. Попробуйте еще раз.";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
            apiKeyCurrent = properties.getProperty("api.openweather");
            apiKeyForecast = properties.getProperty("api.weatherapi");
            weatherApiService = new WeatherApiService(apiKeyCurrent, apiKeyForecast);
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

        if (location == null || location.isEmpty()) {
            resp.getWriter().write(NO_PARAMS);
        } else {
            session.setAttribute("location", location);

            if (!initJsonNodesAndSetAttributes(req, resp)) return;

            RequestDispatcher dispatcher = req.getRequestDispatcher("/weatherView.jsp");

            dispatcher.forward(req, resp);
        }
    }

    private boolean initJsonNodesAndSetAttributes(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonNode locationData = weatherApiService.getCoordinatesByLocation(location);

        if (locationData.isEmpty()) {
            resp.getWriter().write(INCORRECT_NAME);
            return false;
        }

        JsonNode hourlyWeatherData = weatherApiService.getHourlyWeatherByCityName(location);
        JsonNode weatherForecast = weatherApiService.getWeatherForecastByCityName(location);

        double[] coordinates = weatherDataProcessor.getCoordinates(locationData);
        JsonNode currentWeatherData = weatherApiService.getCurrentWeatherByCoordinates(coordinates[0], coordinates[1]);

        req.setAttribute("location", weatherDataProcessor.getLocalName(locationData));
        req.setAttribute("currentTemp", weatherDataProcessor.getCurrentWeather(currentWeatherData).get(0));
        req.setAttribute("currentFeels", weatherDataProcessor.getCurrentWeather(currentWeatherData).get(1));
        req.setAttribute("weatherPerHours", weatherDataProcessor.getHourlyWeather(hourlyWeatherData));
        req.setAttribute("forecast", weatherDataProcessor.getWeatherForecast(weatherForecast));

        return true;
    }
}
