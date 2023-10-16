package ru.kozhevnikov.weatherapp.model;

import java.time.LocalDate;
import java.util.Map;
/**
 * Класс {@code Weather} является моделью, представляющей собой информацию о погода в городе.
 */
public class Weather {
    private int id;
    private City city;
    private int currentTemperature;
    private int currentTemperatureFeelsLike;
    private String currentText;
    private int averageTemperature;
    private int cloud;
    private double precipitation;
    private LocalDate date;
    private Map<String, Integer> hourlyWeather;
    private Map<String, Integer> forecastWeather;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getCloud() {
        return cloud;
    }

    public void setCloud(int cloud) {
        this.cloud = cloud;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "currentTemperature=" + currentTemperature +
                ", currentTemperatureFeelsLike=" + currentTemperatureFeelsLike +
                ", currentText='" + currentText + '\'' +
                ", averageTemperature=" + averageTemperature +
                ", cloud=" + cloud +
                ", precipitation=" + precipitation +
                ", date=" + date +
                '}';
    }
}
