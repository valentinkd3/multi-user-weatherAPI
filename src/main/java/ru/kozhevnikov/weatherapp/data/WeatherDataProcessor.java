package ru.kozhevnikov.weatherapp.data;

import com.fasterxml.jackson.databind.JsonNode;
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
    private Weather weather;

    public Weather getWeather(String location,
                              JsonNode cur,
                              JsonNode hourly,
                              JsonNode forecast) {
        weather = new Weather(location);
        initCurrentWeather(cur);
        initHourlyWeather(hourly);
        initWeatherForecast(forecast);
        return weather;
    }

    public void initCurrentWeather(JsonNode currentWeatherNode) {
        int currentTemperature = currentWeatherNode.get("current").get("temp_c").asInt();
        String currentText = currentWeatherNode.get("current").get("condition").get("text").toString();
        int currentTemperatureFeelsLike = currentWeatherNode.get("current").get("feelslike_c").asInt();
        int avgTemperature = currentWeatherNode.get("current").get("avgtemp_c").asInt();

        weather.setCurrentTemperature(currentTemperature);
        weather.setCurrentText(currentText);
        weather.setCurrentTemperatureFeelsLike(currentTemperatureFeelsLike);
        weather.setAverageTemperature(avgTemperature);
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
            Integer temperature = hourlyWeatherNode.get("forecast")
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
            Integer temperature = hourlyWeatherNode.get("forecast")
                    .get("forecastday")
                    .get(i)
                    .get("day")
                    .get("avgtemp_c").asInt();

            weatherForecast.put(date, temperature);
        }
        weather.setForecastWeather(weatherForecast);
    }
}
