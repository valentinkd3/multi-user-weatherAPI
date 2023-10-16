package ru.kozhevnikov.weatherapp.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import ru.kozhevnikov.weatherapp.api.WeatherApiService;
import ru.kozhevnikov.weatherapp.dao.CityDAO;
import ru.kozhevnikov.weatherapp.data.WeatherDataProcessor;
import ru.kozhevnikov.weatherapp.model.City;
import ru.kozhevnikov.weatherapp.utils.ApiConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Сервлет, в котором реализована логика передачи данных, полученных от стороннего сервиса, на представление - JSP страницу.
 */
@WebServlet("/get_weather")
public class MainServlet extends HttpServlet {
    /**
     * Ключ API для доступа к сервису погоды.
     */
    private static final String API_KEY = ApiConnection.getApiKey();
    private CityDAO cityDAO = new CityDAO();
    private WeatherApiService weatherApiService = new WeatherApiService(API_KEY);
    private WeatherDataProcessor weatherDataProcessor = new WeatherDataProcessor();
    private static final String NO_PARAMS = "Передайте в параметры запроса город, в котором Вы хотите узнать погоду";
    private static final String INCORRECT_NAME = "Название города введено некорректо. Попробуйте еще раз.";
    private String location;
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
            if (!initJsonNodesAndSetAttributes(req)) {
                resp.getWriter().write(INCORRECT_NAME);
                return;
            }

            RequestDispatcher dispatcher = req.getRequestDispatcher("/weatherView.jsp");
            dispatcher.forward(req, resp);
        }
    }
    private boolean initJsonNodesAndSetAttributes(HttpServletRequest req) throws IOException {
        JsonNode currentData = weatherApiService.getJsonCurrentWeather(location);
        JsonNode hourlyWeatherData = weatherApiService.getJsonHourlyWeather(location);
        JsonNode weatherForecastData = weatherApiService.getJsonForecastWeather(location);

        if (hourlyWeatherData == null || weatherForecastData == null){
            return false;
        }

        City city = weatherDataProcessor.getCity(location, currentData,hourlyWeatherData,weatherForecastData);
        cityDAO.acceptToDatabase(city);
        req.setAttribute("city", city);

        return true;
    }
}
