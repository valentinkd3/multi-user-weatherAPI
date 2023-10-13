package ru.kozhevnikov.weatherapp.model;

import java.util.Map;
import java.util.Objects;

public class Weather {
    private int id;
    private String location;
    private int currentTemperature;
    private int currentTemperatureFeelsLike;
    private String currentText;
    private int averageTemperature;
    private Map<String, Integer> hourlyWeather;
    private Map<String, Integer> forecastWeather;

    public Weather(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation(){
        return location;
    }

    public int getAverageTemperature() {
        return averageTemperature;
    }
    public void setAverageTemperature(int averageTemperature) {
        this.averageTemperature = averageTemperature;
    }
    public int getCurrentTemperatureFeelsLike() {
        return currentTemperatureFeelsLike;
    }
    public void setCurrentTemperatureFeelsLike(int currentTemperatureFeelsLike) {
        this.currentTemperatureFeelsLike = currentTemperatureFeelsLike;
    }

    public int getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(int currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }

    public Map<String, Integer> getHourlyWeather() {
        return hourlyWeather;
    }

    public void setHourlyWeather(Map<String, Integer> hourlyWeather) {
        this.hourlyWeather = hourlyWeather;
    }

    public Map<String, Integer> getForecastWeather() {
        return forecastWeather;
    }

    public void setForecastWeather(Map<String, Integer> forecastWeather) {
        this.forecastWeather = forecastWeather;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return Objects.equals(location, weather.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

    @Override
    public String toString() {
        return "{" +
                "location='" + location + '\'' +
                ", currentTemperature=" + currentTemperature +
                ", currentTemperatureFeelsLike=" + currentTemperatureFeelsLike +
                ", currentText='" + currentText + '\'' +
                ", averageTemperature=" + averageTemperature +
                '}';
    }
}
