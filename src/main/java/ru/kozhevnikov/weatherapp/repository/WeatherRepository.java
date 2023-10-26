package ru.kozhevnikov.weatherapp.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.kozhevnikov.weatherapp.entity.Weather;

import java.time.LocalDateTime;

public class WeatherRepository extends RepositoryBase<Integer,Weather>{
    private SessionFactory sessionFactory;
    public WeatherRepository(SessionFactory sessionFactory){
        super(Weather.class,sessionFactory);
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Weather save(Weather entity) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            entity.setCreateAt(LocalDateTime.now());
            session.getTransaction().commit();
        }
        return entity;
    }
}
