package ru.kozhevnikov.weatherapp.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherApiService {

    private final String apiKeyCurrent;
    private final String apiKeyForecast;

    public WeatherApiService(String apiKeyCurrent, String apiKeyForHours) {
        this.apiKeyCurrent = apiKeyCurrent;
        this.apiKeyForecast = apiKeyForHours;
    }
    public JsonNode getCoordinatesByLocation(String location){
        String currentPlace = "http://api.openweathermap.org/geo/1.0/direct?q=" + location + "&limit=1&appid=" + apiKeyCurrent;
        return getJsonNodeFromUrl(currentPlace);
    }
    public JsonNode getCurrentWeatherByCoordinates(double lat,double lon){
        String currentTimeUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat
                + "&lon=" + lon + "&appid=" + apiKeyCurrent + "&units=metric";
        return getJsonNodeFromUrl(currentTimeUrl);
    }
    public JsonNode getHourlyWeatherByCityName(String city){
        String hourlyTimeUrl = "http://api.weatherapi.com/v1/forecast.json?key="+ apiKeyForecast
                +"&q="+city+"&days=1&aqi=no&alerts=no";
        return getJsonNodeFromUrl(hourlyTimeUrl);
    }
    public JsonNode getWeatherForecastByCityName(String city){
        String hourlyTimeUrl = "http://api.weatherapi.com/v1/forecast.json?key="+ apiKeyForecast
                +"&q="+city+"&days=3&aqi=no&alerts=no";
        return getJsonNodeFromUrl(hourlyTimeUrl);
    }
    private JsonNode getJsonNodeFromUrl(String url){
        String jsonAnswer = getJsonAnswerFromAPI(url);
        return getJsonNode(jsonAnswer);
    }
    private String getJsonAnswerFromAPI(String url) {
        StringBuilder jsonAnswer = new StringBuilder();
        try {
            URL urlObj = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlObj.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;

            while ((line = reader.readLine()) != null) {
                jsonAnswer.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonAnswer.toString();
    }
    private JsonNode getJsonNode(String jsonAnswer){
        if (jsonAnswer == null) return null;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(jsonAnswer);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonNode;
    }
}
