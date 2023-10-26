package ru.kozhevnikov.weatherapp.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.kozhevnikov.weatherapp.entity.City;
import ru.kozhevnikov.weatherapp.entity.User;
import ru.kozhevnikov.weatherapp.entity.UserCity;
import ru.kozhevnikov.weatherapp.entity.Weather;

public class HibernateUtil {
    private HibernateUtil() {}
    public static SessionFactory buildSessionFactory(){
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(UserCity.class)
                .addAnnotatedClass(Weather.class);
        configuration.configure();
        return configuration.buildSessionFactory();
    }
}
