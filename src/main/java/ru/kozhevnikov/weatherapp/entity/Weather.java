package ru.kozhevnikov.weatherapp.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "weather")
public class Weather implements BaseEntity<Integer>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;
    @Column(name = "text")
    private String text;
    @Column(name = "curTemp")
    private Integer curTemp;
    @Column(name = "feelsLike")
    private Integer feelsLike;
    @Column(name = "cloud")
    private Integer cloud;
    @Column(name = "precipitation")
    private Double precipitation;
    @Column(name = "createAt")
    @CreationTimestamp
    private LocalDateTime createAt;

    public Weather() {
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
