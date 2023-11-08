package ru.kozhevnikov.weatherapp.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.kozhevnikov.weatherapp.entity.User;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class UserRepository extends RepositoryBase<Integer, User> {
    private final SessionFactory sessionFactory;
    public UserRepository(SessionFactory sessionFactory){
        super(User.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }
    public Optional<User> findUserByName(String username){
        Optional<User> user;
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query query = session.createQuery("SELECT u FROM User u WHERE u.username = :username")
                    .setParameter("username", username);
            try{
                user = Optional.of((User) query.getSingleResult());
            } catch (NoResultException e){
                user = Optional.empty();
            }
            session.getTransaction().commit();
        }
        return user;
    }
}
