package ru.kozhevnikov.weatherapp.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "users_city")
public class UserCity implements BaseEntity<Integer>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    @Column(name = "createAt")
    @CreationTimestamp
    private LocalDateTime createAt;

    public UserCity() {
    }
    public UserCity(User user, City city){
        this.user = user;
        this.city = city;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
