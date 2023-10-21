package ru.kozhevnikov.weatherapp.entity;

import java.time.LocalDateTime;

public class UserCity {
    private Integer id;
    private User user;
    private City city;
    private LocalDateTime createAt;

    public UserCity() {
    }

    public UserCity(Integer id, User user, City city, LocalDateTime createAt) {
        this.id = id;
        this.user = user;
        this.city = city;
        this.createAt = createAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "UserCity{" +
               "id=" + id +
               ", user=" + user +
               ", city=" + city +
               ", createAt=" + createAt +
               '}';
    }
}
