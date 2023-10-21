package ru.kozhevnikov.weatherapp.dao;

import java.util.Optional;

public interface DAO<E> {
    E save(E element);
}
