package ru.kozhevnikov.weatherapp.servlets.users;

import ru.kozhevnikov.weatherapp.dao.UserDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users")
public class Index extends HttpServlet {
    private final UserDAO userDAO = UserDAO.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        req.setAttribute("users", userDAO.findAll());

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/users/index.jsp");
        requestDispatcher.forward(req,resp);
    }
}
