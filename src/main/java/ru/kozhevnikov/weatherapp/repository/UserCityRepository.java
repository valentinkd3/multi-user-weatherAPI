package ru.kozhevnikov.weatherapp.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.kozhevnikov.weatherapp.entity.City;
import ru.kozhevnikov.weatherapp.entity.User;
import ru.kozhevnikov.weatherapp.entity.UserCity;

import java.time.LocalDateTime;
import java.util.List;

public class UserCityRepository extends RepositoryBase<Integer, UserCity> {
    private SessionFactory sessionFactory;
    private final UserRepository userRepository;
    private static final String JOURNAL_HQL = """
            SELECT j 
            FROM UserCity j 
            LEFT JOIN FETCH j.city 
            WHERE j.user = :user 
            ORDER BY j.id DESC
            """;
    public UserCityRepository(SessionFactory sessionFactory){
        super(UserCity.class,sessionFactory);
        this.sessionFactory = sessionFactory;
        userRepository = new UserRepository(sessionFactory);
    }
    @Override
    public UserCity save(UserCity entity) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            entity.setCreateAt(LocalDateTime.now());
            session.getTransaction().commit();
        }
        return entity;
    }
    public List<UserCity> getJournalByUserId(Integer id){
        List<UserCity> journal;
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = userRepository.findById(id).get();

            journal = (List<UserCity>) session.createQuery(JOURNAL_HQL)
                    .setParameter("user", user)
                    .getResultList();

            session.getTransaction().commit();
        }
        return journal;
    }
    public City getLastCity(int userId) {
        List<UserCity> journal = getJournalByUserId(userId);
        if (journal.isEmpty()) return null;
        return journal.get(0).getCity() ;
    }

}
