package ru.kozhevnikov.weatherapp.servlets.weather;

import com.fasterxml.jackson.databind.JsonNode;
import ru.kozhevnikov.weatherapp.api.WeatherApiService;
import ru.kozhevnikov.weatherapp.dao.CityDAO;
import ru.kozhevnikov.weatherapp.dao.UserCityDAO;
import ru.kozhevnikov.weatherapp.dao.UserDAO;
import ru.kozhevnikov.weatherapp.dao.WeatherDAO;
import ru.kozhevnikov.weatherapp.entity.City;
import ru.kozhevnikov.weatherapp.entity.User;
import ru.kozhevnikov.weatherapp.entity.UserCity;
import ru.kozhevnikov.weatherapp.entity.Weather;
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
@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {
    /**
     * Ключ API для доступа к сервису погоды.
     */
    private static final String API_KEY = ApiConnection.getApiKey();
    private final CityDAO cityDAO = CityDAO.getInstance();
    private final UserDAO userDAO = UserDAO.getInstance();
    private final UserCityDAO userCityDAO = UserCityDAO.getInstance();
    private final WeatherDAO weatherDAO = WeatherDAO.getInstance();
    private final WeatherApiService weatherApiService = new WeatherApiService(API_KEY);
    private static final String NO_USE = """
            Ранее вы не использовали наш сервис для просмотра погоды.
            \nВведите название интересующего Вас города в адресную строку (прим. /weather?location=Moscow).
            """;
    private static final String NO_ENTER = "Для использования сервиса необходимо авторизоваться";
    private static final String INCORRECT_NAME = "Название города введено некорректо. Попробуйте еще раз.";
    private String location;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();

        String username = (String) session.getAttribute("username");
        if (username == null) {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/authorization/needauth.jsp");
            requestDispatcher.forward(req,resp);
        }

        User user = userDAO.findByName(username).get();
        City lastCity = userCityDAO.getLastCity(user.getId());

        location = req.getParameter("location");

        if (lastCity == null && location == null) {
            resp.getWriter().write(NO_USE);
            return;
        }
        else if (location == null){
            location = lastCity.getName();
        }

        session.setAttribute("location", location);

        City city = new City();
        city.setName(location);
        cityDAO.save(city);
        City savedCity = cityDAO.findByName(location).get();

        Weather weather = initWeatherFromJSON(location);
        if (weather == null){
            resp.getWriter().write(INCORRECT_NAME);
            cityDAO.deleteByName(location);
            return;
        }
        Weather savedWeather = weatherDAO.save(weather);
        UserCity userCity = new UserCity();
        userCity.setUser(user);
        userCity.setCity(savedCity);
        userCityDAO.save(userCity);

        req.setAttribute("weather", savedWeather);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/weather.jsp");
        dispatcher.forward(req, resp);
    }

    private Weather initWeatherFromJSON(String location) {
        JsonNode currentData = weatherApiService.getJsonCurrentWeather(location);
        if (currentData == null) return null;
        Weather weather = new Weather();
        weather.setCity(cityDAO.findByName(location).orElse(null));
        weather.setText(currentData.get("current").get("condition").get("text").asText());
        weather.setCurTemp(currentData.get("current").get("temp_c").asInt());
        weather.setFeelsLike(currentData.get("current").get("feelslike_c").asInt());
        weather.setCloud(currentData.get("current").get("cloud").asInt());
        weather.setPrecipitation(currentData.get("current").get("precip_mm").asDouble());
        return weather;
    }
}
