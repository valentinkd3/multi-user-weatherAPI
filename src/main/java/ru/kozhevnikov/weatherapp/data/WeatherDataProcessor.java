package ru.kozhevnikov.weatherapp.data;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

public class WeatherDataProcessor {

    public double[] getCoordinates(JsonNode coordinatesNode) {
        double[] coordinates = new double[2];

        coordinates[0] = Double.parseDouble(coordinatesNode.get(0).get("lat").toString());
        coordinates[1] = Double.parseDouble(coordinatesNode.get(0).get("lon").toString());

        return coordinates;
    }

    public String getLocalName(JsonNode coordinatesNode) {
        return coordinatesNode.get(0).get("local_names").get("ru").toString();
    }

    public List<String> getCurrentWeather(JsonNode currentWeatherNode) {
        List<String> currentWeather = new ArrayList<>();

        String current = currentWeatherNode.get("main").get("temp").toString();
        String feelsLike = currentWeatherNode.get("main").get("feels_like").toString();

        currentWeather.add(current);
        currentWeather.add(feelsLike);

        return currentWeather;
    }

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
