package ru.kozhevnikov.weatherapp.servlets.users;

import org.hibernate.SessionFactory;
import ru.kozhevnikov.weatherapp.repository.UserRepository;
import ru.kozhevnikov.weatherapp.utils.HibernateUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users")
public class Index extends HttpServlet {
    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private final UserRepository userRepository = new UserRepository(sessionFactory);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        req.setAttribute("users", userRepository.findAll());

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/users/index.jsp");
        requestDispatcher.forward(req,resp);
    }
}
