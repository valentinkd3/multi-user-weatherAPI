package ru.kozhevnikov.weatherapp.data;

import com.fasterxml.jackson.databind.JsonNode;

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
    public List<String> getCurrentWeather(JsonNode currentWeatherNode) {
        List<String> currentWeather = new ArrayList<>();

        String currentTemperature = currentWeatherNode.get("current").get("temp_c").toString();
        String currentText = currentWeatherNode.get("current").get("condition").get("text").toString();
        String currentFeelsTemperature = currentWeatherNode.get("current").get("feelslike_c").toString();

        currentWeather.add(currentTemperature);
        currentWeather.add(currentText);
        currentWeather.add(currentFeelsTemperature);

        return currentWeather;
    }
    /**
     * Получает почасовой прогноз погоды и возвращает его в виде отображения (Map) с ключами - часами и значениями - данными о погоде.
     *
     * @param hourlyWeatherNode JSON-структура с почасовым прогнозом погоды
     * @return отображение с данными о погоде для каждого часа в течение суток
     */
    public Map<Integer, String> getHourlyWeather(JsonNode hourlyWeatherNode) {
        Map<Integer, String> hourlyWeather = new TreeMap<>();
        for (int i = 0; i < 24; i++) {
            String date = hourlyWeatherNode.get("forecast")
                    .get("forecastday")
                    .get(0)
                    .get("hour")
                    .get(i)
                    .get("time").toString();
            String temperature = hourlyWeatherNode.get("forecast")
                    .get("forecastday")
                    .get(0)
                    .get("hour")
                    .get(i)
                    .get("temp_c").toString();

            hourlyWeather.put(i, date+" : "+temperature + " °C.");
        }
        return hourlyWeather;
    }
    /**
     * Получает прогноз погоды на ближайшие дни и возвращает его в виде отображения (Map) с ключами - датами и значениями - данными о погоде.
     *
     * @param hourlyWeatherNode JSON-структура с прогнозом погоды на ближайшие дни
     * @return отображение с данными о погоде на ближайшие дни
     */
    public Map<Integer, String> getWeatherForecast(JsonNode hourlyWeatherNode) {
        Map<Integer, String> weatherForecast = new TreeMap<>();
        for (int i = 0; i < 3; i++) {
            String date = hourlyWeatherNode.get("forecast")
                    .get("forecastday")
                    .get(i)
                    .get("date").toString();
            String temperature = hourlyWeatherNode.get("forecast")
                    .get("forecastday")
                    .get(i)
                    .get("day")
                    .get("avgtemp_c").toString();

            weatherForecast.put(i, date+" : "+temperature + " °C.");
        }
        return weatherForecast;
    }
}
