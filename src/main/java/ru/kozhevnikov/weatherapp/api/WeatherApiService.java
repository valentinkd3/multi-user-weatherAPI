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

    private final String apiKey;
    public WeatherApiService(String apiKey) {
        this.apiKey = apiKey;
    }
    public JsonNode getCurrentWeatherByLocation(String location){
        String currentTimeUrl = "http://api.weatherapi.com/v1/current.json?key=" + apiKey
                + "&q=" + location + "&aqi=no&lang=ru";
        return getJsonNodeFromUrl(currentTimeUrl);
    }
    public JsonNode getHourlyWeatherByLocation(String location){
        String hourlyTimeUrl = "http://api.weatherapi.com/v1/forecast.json?key="+ apiKey
                +"&q="+location+"&days=1&aqi=no&alerts=no";
        return getJsonNodeFromUrl(hourlyTimeUrl);
    }
    public JsonNode getWeatherForecastByLocation(String location){
        String hourlyTimeUrl = "http://api.weatherapi.com/v1/forecast.json?key="+ apiKey
                +"&q="+location+"&days=3&aqi=no&alerts=no";
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
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;

                while ((line = reader.readLine()) != null) {
                    jsonAnswer.append(line);
                }
            }
            else {
                return null;
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
