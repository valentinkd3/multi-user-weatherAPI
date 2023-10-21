package ru.kozhevnikov.weatherapp.dao;

public interface DAO<E> {
    E save(E element);
}
