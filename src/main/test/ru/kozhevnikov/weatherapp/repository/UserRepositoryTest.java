package ru.kozhevnikov.weatherapp.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kozhevnikov.weatherapp.entity.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    private UserRepository userRepository;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    private static final Integer FAKE_ID = 1;
    private static final String FAKE_NAME = "dump";

    @BeforeEach
    void prepare() {
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        transaction = mock(Transaction.class);
        userRepository = new UserRepository(sessionFactory);

        doReturn(session).when(sessionFactory).openSession();
        doReturn(transaction).when(session).beginTransaction();
        doReturn(transaction).when(session).getTransaction();
    }

    @Test
    void saveUserInDatabase() {
        User entity = new User(FAKE_NAME);
        User savedEntity = userRepository.save(entity);

        verify(session, times(1)).save(entity);
        verify(session, times(1)).beginTransaction();
        verify(transaction, times(1)).commit();
        verify(session, times(1)).close();

        assertThat(savedEntity).isEqualTo(entity);
    }

    @Test
    void deleteUserFromDatabase() {
        User entity = new User();

        doReturn(entity).when(session).get(User.class, FAKE_ID);

        userRepository.delete(FAKE_ID);

        verify(session, times(1)).delete(entity);
        verify(session, times(1)).beginTransaction();
        verify(transaction, times(1)).commit();
        verify(session, times(1)).close();
    }

    @Test
    void updateUserInDatabase() {
        User entity = new User(FAKE_NAME);
        userRepository.update(entity);

        verify(session, times(1)).merge(entity);
        verify(session, times(1)).beginTransaction();
        verify(transaction, times(1)).commit();
        verify(session, times(1)).close();
    }

    @Test
    void findUserByIdInDatabase() {
        User entity = new User();
        entity.setId(FAKE_ID);
        entity.setUsername(FAKE_NAME);

        userRepository.findById(FAKE_ID);

        verify(session, times(1)).find(User.class, FAKE_ID);
        verify(session, times(1)).beginTransaction();
        verify(transaction, times(1)).commit();
        verify(session, times(1)).close();
    }
    @Test
    void findAllUsersByIdInDatabase() {
        List<User> entities = List.of(new User(FAKE_NAME), new User());
        CriteriaQuery<User> criteriaQuery = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        Query<User> query = mock(Query.class);

        doReturn(criteriaBuilder).when(session).getCriteriaBuilder();
        doReturn(criteriaQuery).when(criteriaBuilder).createQuery(User.class);
        doReturn(query).when(session).createQuery(criteriaQuery);
        doReturn(List.of(new User(FAKE_NAME), new User())).when(query).getResultList();

        List<User> findAll = userRepository.findAll();

        verify(session, times(1)).beginTransaction();
        verify(transaction, times(1)).commit();
        verify(session, times(1)).close();

        assertThat(findAll).isEqualTo(entities);
    }
}