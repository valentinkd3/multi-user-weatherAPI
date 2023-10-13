package ru.kozhevnikov.weatherapp.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import ru.kozhevnikov.weatherapp.api.WeatherApiService;
import ru.kozhevnikov.weatherapp.dao.WeatherDAO;
import ru.kozhevnikov.weatherapp.data.WeatherDataProcessor;
import ru.kozhevnikov.weatherapp.model.Weather;

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
import java.util.Properties;

/**
 * Сервлет, в котором реализована логика передачи данных, полученных от стороннего сервиса, на представление - JSP страницу.
 */
@WebServlet("/get_weather")
public class MainServlet extends HttpServlet {
    /**
     * Ключ API для доступа к сервису погоды.
     */
    private static String apiKey;
    private WeatherDAO weatherDAO;
    private String location;
    private WeatherApiService weatherApiService;
    private WeatherDataProcessor weatherDataProcessor;
    private static final String NO_PARAMS = "Передайте в параметры запроса город, в котором Вы хотите узнать погоду";
    private static final String INCORRECT_NAME = "Название города введено некорректо. Попробуйте еще раз.";

    /**
     * Метод init() инициализирует сервлет, загружая настройки из файла "api.properties"
     * и создавая объекты для работы с API и данными о погоде.
     *
     * @param config предоставляет информацию о конфигурации сервлета
     * @throws ServletException если происходит ошибка инициализации сервлета
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("api.properties")) {
            properties.load(inputStream);
            apiKey = properties.getProperty("api.key");
            weatherApiService = new WeatherApiService(apiKey);
            weatherDataProcessor = new WeatherDataProcessor();
            weatherDAO = new WeatherDAO();
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

        Weather weather = weatherDataProcessor.getWeather(location, currentData,hourlyWeatherData,weatherForecastData);
        weatherDAO.acceptToDatabase(weather);
        req.setAttribute("weather", weather);

        return true;
    }
}
