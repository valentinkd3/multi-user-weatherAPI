package ru.kozhevnikov.weatherapp.servlets.users;

import ru.kozhevnikov.weatherapp.dao.UserDAO;
import ru.kozhevnikov.weatherapp.entity.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users/new")
public class New extends HttpServlet {
    private final UserDAO userDAO = UserDAO.getInstance();
    private static final String NO_UNIQUE = "Пользователь с таким именем уже существует";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/users/new.jsp");
        requestDispatcher.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");


        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (checkUsername(username)) {
            resp.getWriter().write(NO_UNIQUE);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        userDAO.save(user);

        resp.sendRedirect("/users");
    }
    private boolean checkUsername(String username){
        return userDAO.findByName(username).isPresent();
    }
}
