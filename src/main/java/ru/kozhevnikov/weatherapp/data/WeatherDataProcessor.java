package ru.kozhevnikov.weatherapp.data;

import com.fasterxml.jackson.databind.JsonNode;
import ru.kozhevnikov.weatherapp.model.City;
import ru.kozhevnikov.weatherapp.model.Weather;

import java.util.*;

/**
 * Класс {@code WeatherDataProcessor} предоставляет методы для обработки данных о погоде, полученных из JSON-структур.
 */
public class WeatherDataProcessor {
    /**
     * Получает текущие данные о погоде и возвращает их в виде списка строк.
     *
     * @param currentWeatherNode JSON-структура с текущими данными о погоде
     * @return список строк, содержащих текущую температуру, описание и ощущаемую температуру
     */
    private City city;
    private Weather weather;

    public City getCity(String name,
                        JsonNode cur,
                        JsonNode hourly,
                        JsonNode forecast) {
        city = new City(name);
        weather = new Weather();
        initCurrentWeather(cur);
        initHourlyWeather(hourly);
        initWeatherForecast(forecast);
        city.setWeather(weather);
        return city;
    }

    public void initCurrentWeather(JsonNode currentWeatherNode) {
        int  currentTemperature = currentWeatherNode.get("current").get("temp_c").asInt();
        String currentText = currentWeatherNode.get("current").get("condition").get("text").toString();
        int currentTemperatureFeelsLike = currentWeatherNode.get("current").get("feelslike_c").asInt();
        int cloud = currentWeatherNode.get("current").get("cloud").asInt();
        double precipitation = currentWeatherNode.get("current").get("precip_mm").asDouble();

        weather.setCurrentTemperature(currentTemperature);
        weather.setCurrentText(currentText);
        weather.setCurrentTemperatureFeelsLike(currentTemperatureFeelsLike);
        weather.setCloud(cloud);
        weather.setPrecipitation(precipitation);
    }

    /**
     * Получает почасовой прогноз погоды и возвращает его в виде отображения (Map) с ключами - часами и значениями - данными о погоде.
     *
     * @param hourlyWeatherNode JSON-структура с почасовым прогнозом погоды
     * @return отображение с данными о погоде для каждого часа в течение суток
     */
    public void initHourlyWeather(JsonNode hourlyWeatherNode) {
        Map<String, Integer> hourlyWeather = new LinkedHashMap<>();
        for (int i = 0; i < 24; i++) {
            String date = hourlyWeatherNode.get("forecast")
                    .get("forecastday")
                    .get(0)
                    .get("hour")
                    .get(i)
                    .get("time").toString();
            int temperature = hourlyWeatherNode.get("forecast")
                    .get("forecastday")
                    .get(0)
                    .get("hour")
                    .get(i)
                    .get("temp_c").asInt();

            hourlyWeather.put(date, temperature);
        }
        weather.setHourlyWeather(hourlyWeather);
    }

    /**
     * Получает прогноз погоды на ближайшие дни и возвращает его в виде отображения (Map) с ключами - датами и значениями - данными о погоде.
     *
     * @param hourlyWeatherNode JSON-структура с прогнозом погоды на ближайшие дни
     * @return отображение с данными о погоде на ближайшие дни
     */
    public void initWeatherForecast(JsonNode hourlyWeatherNode) {
        Map<String, Integer> weatherForecast = new LinkedHashMap<>();
        for (int i = 0; i < 3; i++) {
            String date = hourlyWeatherNode.get("forecast")
                    .get("forecastday")
                    .get(i)
                    .get("date").toString();

            int temperature = hourlyWeatherNode.get("forecast")
                    .get("forecastday")
                    .get(i)
                    .get("day")
                    .get("avgtemp_c").asInt();

            weatherForecast.put(date, temperature);
        }
        weather.setForecastWeather(weatherForecast);

        int avgTemperature = hourlyWeatherNode.get("forecast")
                .get("forecastday")
                .get(0)
                .get("day")
                .get("avgtemp_c").asInt();
        weather.setAverageTemperature(avgTemperature);
    }
}
