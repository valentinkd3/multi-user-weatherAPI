package ru.kozhevnikov.weatherapp.data;

import com.fasterxml.jackson.databind.JsonNode;
import ru.kozhevnikov.weatherapp.model.City;
import ru.kozhevnikov.weatherapp.model.Weather;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 * Класс {@code WeatherDataProcessor} предоставляет методы для обработки данных о погоде, полученных из JSON-структур.
 */
public class WeatherDataProcessor {
    private City city;
    private Weather weather;

    /**
     * Возвращает объект класса {@code City}, содержащий информацию о названии города, текущей погоде,
     * почасовом прогнозе погоды и о прогнозе погоды на ближайшие дни, полученную из соотвутствующих JSON-структур.
     *
     * @param name     название города
     * @param cur      JSON-структура с текущей погодой
     * @param hourly   JSON-структура с почасовым прогнозом погоды
     * @param forecast JSON-структура с прогнозом погоды на ближайшие дни
     * @return объект {@code City} с данными о городе и погоде
     */
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
    private void initCurrentWeather(JsonNode currentWeatherNode) {
        int  currentTemperature = currentWeatherNode.get("current").get("temp_c").asInt();
        String currentText = currentWeatherNode.get("current").get("condition").get("text").asText();
        int currentTemperatureFeelsLike = currentWeatherNode.get("current").get("feelslike_c").asInt();
        int cloud = currentWeatherNode.get("current").get("cloud").asInt();
        double precipitation = currentWeatherNode.get("current").get("precip_mm").asDouble();
        LocalDate date = getLocalDateFromJson(currentWeatherNode);

        weather.setCurrentTemperature(currentTemperature);
        weather.setCurrentText(currentText);
        weather.setCurrentTemperatureFeelsLike(currentTemperatureFeelsLike);
        weather.setCloud(cloud);
        weather.setPrecipitation(precipitation);
        weather.setDate(date);
    }
    private LocalDate getLocalDateFromJson(JsonNode currentWeatherNode){
        String currentDateStr = currentWeatherNode.get("location").get("localtime").asText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
        LocalDateTime dateTime = LocalDateTime.parse(currentDateStr,formatter);

        return dateTime.toLocalDate();
    }

    private void initHourlyWeather(JsonNode hourlyWeatherNode) {
        Map<String, Integer> hourlyWeather = new LinkedHashMap<>();
        for (int i = 0; i < 24; i++) {
            String date = hourlyWeatherNode.get("forecast")
                    .get("forecastday")
                    .get(0)
                    .get("hour")
                    .get(i)
                    .get("time").asText();
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

    private void initWeatherForecast(JsonNode hourlyWeatherNode) {
        Map<String, Integer> weatherForecast = new LinkedHashMap<>();
        for (int i = 0; i < 3; i++) {
            String date = hourlyWeatherNode.get("forecast")
                    .get("forecastday")
                    .get(i)
                    .get("date").asText();

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
