package ru.kozhevnikov.weatherapp.servlets.authorization;

import org.hibernate.SessionFactory;
import ru.kozhevnikov.weatherapp.entity.User;
import ru.kozhevnikov.weatherapp.repository.UserRepository;
import ru.kozhevnikov.weatherapp.utils.HibernateUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class Authorization extends HttpServlet {
    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private final UserRepository userRepository = new UserRepository(sessionFactory);
    private static final String NO_USER = "Пользователя с таким именем и/или паролем не существует";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/authorization/login.jsp");
        requestDispatcher.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (!isValidUser(username,password)){
            resp.getWriter().write(NO_USER);
            return;
        }
        addCookies(req, resp, username);
        resp.sendRedirect("/weather");
    }

    private static void addCookies(HttpServletRequest req, HttpServletResponse resp, String username) {
        HttpSession session = req.getSession();
        session.setAttribute("username", username);
        String sessionId = session.getId();
        Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
        sessionCookie.setMaxAge(-1);
        resp.addCookie(sessionCookie);
    }

    private boolean isValidUser(String username, String password){
        Optional<User> maybeUser = userRepository.findUserByName(username);
        if (!maybeUser.isEmpty()){
            return maybeUser.get().getPassword().equals(password);
        }
        return false;
    }
}
