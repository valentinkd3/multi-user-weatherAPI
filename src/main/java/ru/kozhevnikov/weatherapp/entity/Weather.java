package ru.kozhevnikov.weatherapp.entity;

import java.time.LocalDateTime;

public class Weather {
    private int id;
    private City city;
    private String text;
    private Integer curTemp;
    private Integer feelsLike;
    private Integer cloud;
    private Double precipitation;
    private LocalDateTime createAt;

    public Weather() {
    }
    public Weather(int id, City city, String text, Integer curTemp, Integer feelsLike, Integer cloud, Double precipitation, LocalDateTime createAt) {
        this.id = id;
        this.city = city;
        this.text = text;
        this.curTemp = curTemp;
        this.feelsLike = feelsLike;
        this.cloud = cloud;
        this.precipitation = precipitation;
        this.createAt = createAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getCurTemp() {
        return curTemp;
    }

    public void setCurTemp(Integer curTemp) {
        this.curTemp = curTemp;
    }

    public Integer getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Integer feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Integer getCloud() {
        return cloud;
    }

    public void setCloud(Integer cloud) {
        this.cloud = cloud;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Weather{" +
               "id=" + id +
               ", city=" + city +
               ", text='" + text + '\'' +
               ", curTemp=" + curTemp +
               ", feelsLike=" + feelsLike +
               ", cloud=" + cloud +
               ", precipitation=" + precipitation +
               ", createAt=" + createAt +
               '}';
    }
}
