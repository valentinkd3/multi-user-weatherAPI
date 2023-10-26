package ru.kozhevnikov.weatherapp.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.kozhevnikov.weatherapp.entity.BaseEntity;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class RepositoryBase<K extends Serializable, E extends BaseEntity<K>> implements Repository<K,E> {
    private final Class<E> entityClass;
    private final SessionFactory sessionFactory;
    public RepositoryBase(Class<E> entityClass, SessionFactory sessionFactory) {
        this.entityClass = entityClass;
        this.sessionFactory = sessionFactory;
    }
    @Override
    public E save(E entity) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
        return entity;
    }
    @Override
    public void delete(K id) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            E entity = session.get(entityClass, id);
            session.delete(entity);
            session.flush();
            session.getTransaction().commit();
        }
    }
    @Override
    public void update(E entity) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        }
    }
    @Override
    public Optional<E> findById(K id) {
        Optional<E> entity;
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            entity = Optional.ofNullable(session.find(entityClass, id));
            session.getTransaction().commit();
        }
        return entity;
    }

    @Override
    public List<E> findAll() {
        List<E> entities;
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaQuery<E> criteria = session.getCriteriaBuilder().createQuery(entityClass);
            criteria.from(entityClass);
            entities = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
        }
        return entities;
    }
}
