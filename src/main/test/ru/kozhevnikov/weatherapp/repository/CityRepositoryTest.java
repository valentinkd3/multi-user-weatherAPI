package ru.kozhevnikov.weatherapp.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.kozhevnikov.weatherapp.entity.City;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CityRepositoryTest {
    @Mock
    private Session session;
    private SessionFactory sessionFactory;
    private CityRepository cityRepository;
    @BeforeEach
    void prepare(){
        this.sessionFactory = Mockito.mock(SessionFactory.class);
        this.cityRepository = new CityRepository(sessionFactory);
    }
    @Test
    void firstTest(){
        City entity = new City("Moscow");
        entity.setId(1);

        Mockito.doReturn(session).when(sessionFactory).openSession();
//        City saved = cityRepository.findById(1);
//
//        assertThat(saved).isEqualTo(entity);
    }

}