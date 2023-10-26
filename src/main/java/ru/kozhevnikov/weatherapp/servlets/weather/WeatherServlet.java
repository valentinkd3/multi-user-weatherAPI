package ru.kozhevnikov.weatherapp.servlets.weather;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.SessionFactory;
import ru.kozhevnikov.weatherapp.api.WeatherApiService;
import ru.kozhevnikov.weatherapp.entity.City;
import ru.kozhevnikov.weatherapp.entity.User;
import ru.kozhevnikov.weatherapp.entity.UserCity;
import ru.kozhevnikov.weatherapp.entity.Weather;
import ru.kozhevnikov.weatherapp.repository.CityRepository;
import ru.kozhevnikov.weatherapp.repository.UserCityRepository;
import ru.kozhevnikov.weatherapp.repository.UserRepository;
import ru.kozhevnikov.weatherapp.repository.WeatherRepository;
import ru.kozhevnikov.weatherapp.utils.ApiConnection;
import ru.kozhevnikov.weatherapp.utils.HibernateUtil;

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
    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private final UserRepository userRepository = new UserRepository(sessionFactory);
    private final WeatherRepository weatherRepository = new WeatherRepository(sessionFactory);
    private final UserCityRepository userCityRepository = new UserCityRepository(sessionFactory);
    private final CityRepository cityRepository = new CityRepository(sessionFactory);
    private final WeatherApiService weatherApiService = new WeatherApiService(API_KEY);
    private static final String NO_USE = "Ранее вы не использовали наш сервис для просмотра погоды.\n" +
                                         "Введите название интересующего Вас города в адресную строку (прим. /weather?location=Moscow).";
    private static final String INCORRECT_NAME = "Название города введено некорректо. Попробуйте еще раз.";
    private String location;
    private User authorizedUser;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        checkUsername(req, resp, username);

        initialLocation(req, resp, session, username);

        City savedCity = saveCity();
        Weather weather = initWeatherFromJSON(savedCity);

        if (weather == null) {
//            deleteCityFromDb(resp);
        } else {
            req.setAttribute("weather", weatherRepository.save(weather));

            initialUserCity(savedCity);

            RequestDispatcher dispatcher = req.getRequestDispatcher("/weather.jsp");
            dispatcher.forward(req, resp);
        }
    }

    private void initialUserCity(City savedCity) {
        UserCity userCity = new UserCity();
        userCity.setUser(authorizedUser);
        userCity.setCity(savedCity);
        userCityRepository.save(userCity);
    }

//    private void deleteCityFromDb(HttpServletResponse resp) throws IOException {
//        cityRepository.delete(location);
//        resp.getWriter().write(INCORRECT_NAME);
//    }

    private City saveCity() {
        City city = new City();
        city.setName(location);
        cityRepository.save(city);
        return cityRepository.findCityByName(location).get();
    }

    private void initialLocation(HttpServletRequest req, HttpServletResponse resp, HttpSession session, String username) throws IOException {
        authorizedUser = userRepository.findUserByName(username).get();
        City lastCity = userCityRepository.getLastCity(authorizedUser.getId());
        location = req.getParameter("location");
        if (lastCity == null && location == null) {
            resp.getWriter().write(NO_USE);
        } else if (location == null) {
            location = lastCity.getName();
        }
        session.setAttribute("location", location);
    }

    private static void checkUsername(HttpServletRequest req, HttpServletResponse resp, String username) throws ServletException, IOException {
        if (username == null) {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/authorization/toLogin.jsp");
            requestDispatcher.forward(req, resp);
        }
    }

    private Weather initWeatherFromJSON(City city) {
        JsonNode currentData = weatherApiService.getJsonCurrentWeather(location);
        if (currentData == null) return null;
        Weather weather = new Weather();
        weather.setCity(city);
        weather.setText(currentData.get("current").get("condition").get("text").asText());
        weather.setCurTemp(currentData.get("current").get("temp_c").asInt());
        weather.setFeelsLike(currentData.get("current").get("feelslike_c").asInt());
        weather.setCloud(currentData.get("current").get("cloud").asInt());
        weather.setPrecipitation(currentData.get("current").get("precip_mm").asDouble());
        return weather;
    }
}
