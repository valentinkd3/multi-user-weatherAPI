package ru.kozhevnikov.weatherapp.exception;

public class DaoException extends RuntimeException{
    public DaoException(Throwable throwable){
        super(throwable);
    }
}
