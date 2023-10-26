import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import ru.kozhevnikov.weatherapp.api.WeatherApiService;
import ru.kozhevnikov.weatherapp.entity.City;
import ru.kozhevnikov.weatherapp.entity.UserCity;
import ru.kozhevnikov.weatherapp.entity.Weather;
import ru.kozhevnikov.weatherapp.repository.CityRepository;
import ru.kozhevnikov.weatherapp.repository.UserCityRepository;
import ru.kozhevnikov.weatherapp.repository.UserRepository;
import ru.kozhevnikov.weatherapp.repository.WeatherRepository;
import ru.kozhevnikov.weatherapp.utils.ApiConnection;
import ru.kozhevnikov.weatherapp.utils.HibernateUtil;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class HibernateRunner {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            //testAllSystems(sessionFactory);
            UserCityRepository userCityRepository = new UserCityRepository(sessionFactory);
            List<UserCity> journalByUserId = userCityRepository.getJournalByUserId(43);
            for (UserCity userCity : journalByUserId) {
                System.out.println(userCity.getUser().getUsername() + " " + userCity.getCity().getName() +
                                   " " + userCity.getCreateAt().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

        }
    }

    private static void testAllSystems(SessionFactory sessionFactory) {
        String api = ApiConnection.getApiKey();
        String location = "Pekin";
        CityRepository cityRepository = new CityRepository(sessionFactory);
        UserRepository userRepository = new UserRepository(sessionFactory);
        WeatherRepository weatherRepository = new WeatherRepository(sessionFactory);
        UserCityRepository userCityRepository = new UserCityRepository(sessionFactory);

        City city = cityRepository.save(new City(location));

        WeatherApiService weatherApiService = new WeatherApiService(api);
        JsonNode currentData = weatherApiService.getJsonCurrentWeather(city.getName());
        if (currentData == null) {
            return;
        }
        Weather weather = new Weather();
        weather.setCity(city);
        weather.setText(currentData.get("current").get("condition").get("text").asText());
        weather.setCurTemp(currentData.get("current").get("temp_c").asInt());
        weather.setFeelsLike(currentData.get("current").get("feelslike_c").asInt());
        weather.setCloud(currentData.get("current").get("cloud").asInt());
        weather.setPrecipitation(currentData.get("current").get("precip_mm").asDouble());

        weatherRepository.save(weather);

        userCityRepository.save(new UserCity(userRepository.findById(43).get(), city));
    }
}
