package ru.kozhevnikov.weatherapp.model;

import java.util.Objects;
/**
 * Класс {@code City} является моделью, представляющей собой информацию о городе и о связанной с ним погодой.
 */
public class City {
    private int id;
    private String name;
    private Weather weather;

    /**
     * Конструктор для создания нового объекта {@code City} с заданным назывнием города объекта {@code Weather} для этого города.
     *
     * @param name название города
     */
    public City(String name) {
        this.name = name;
        weather = new Weather();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
        this.weather.setCity(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weather=" + weather +
                '}';
    }
}
