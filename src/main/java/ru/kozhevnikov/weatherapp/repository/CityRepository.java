package ru.kozhevnikov.weatherapp.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.kozhevnikov.weatherapp.entity.City;

import java.util.Optional;

public class CityRepository extends RepositoryBase<Integer, City> {
    private SessionFactory sessionFactory;
    public CityRepository(SessionFactory sessionFactory){
        super(City.class,sessionFactory);
        this.sessionFactory = sessionFactory;
    }
    @Override
    public City save(City entity) {
        if (findCityByName(entity.getName()).isPresent()){
            return findCityByName(entity.getName()).get();
        }
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
        return entity;
    }
    public Optional<City> findCityByName(String name){
        Optional<City> city = null;
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query query = session.createQuery("SELECT c FROM City c WHERE c.name = :name");
            query.setParameter("name", name);
            city = Optional.ofNullable((City) query.getSingleResult());

            session.getTransaction().commit();
        }
        return city;
    }
}
