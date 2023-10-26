package ru.kozhevnikov.weatherapp.entity;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public interface BaseEntity<T> extends Serializable {
    T getId();
    void setId(T id);

}
