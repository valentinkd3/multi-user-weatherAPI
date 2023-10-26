package ru.kozhevnikov.weatherapp.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.kozhevnikov.weatherapp.entity.User;

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

            user = Optional.ofNullable((User) query.getSingleResult());

            session.getTransaction().commit();
        }
        return user;
    }
}
