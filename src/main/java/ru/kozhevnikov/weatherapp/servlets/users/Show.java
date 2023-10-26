package ru.kozhevnikov.weatherapp.servlets.users;

import org.hibernate.SessionFactory;
import ru.kozhevnikov.weatherapp.entity.UserCity;
import ru.kozhevnikov.weatherapp.repository.UserCityRepository;
import ru.kozhevnikov.weatherapp.repository.UserRepository;
import ru.kozhevnikov.weatherapp.repository.WeatherRepository;
import ru.kozhevnikov.weatherapp.utils.HibernateUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users/*")
public class Show extends HttpServlet {
    private Integer userId;
    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private final UserRepository userRepository = new UserRepository(sessionFactory);
    private final UserCityRepository userCityRepository = new UserCityRepository(sessionFactory);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        StringBuilder stringBuilder = new StringBuilder(req.getPathInfo());
        userId = Integer.parseInt(stringBuilder.replace(0,1,"").toString());
        List<UserCity> journal = userCityRepository.getJournalByUserId(userId);

        req.setAttribute("journal", journal);
        req.setAttribute("user", userRepository.findById(userId).get());

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/users/show.jsp");
        requestDispatcher.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userRepository.delete(userId);
        resp.sendRedirect("/users");
    }
}
