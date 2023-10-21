package ru.kozhevnikov.weatherapp.servlets.users;

import ru.kozhevnikov.weatherapp.dao.UserCityDAO;
import ru.kozhevnikov.weatherapp.dao.UserDAO;
import ru.kozhevnikov.weatherapp.dao.WeatherDAO;
import ru.kozhevnikov.weatherapp.entity.City;
import ru.kozhevnikov.weatherapp.entity.UserCity;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

@WebServlet("/users/*")
public class Show extends HttpServlet {
    private Integer userId;
    private final UserDAO userDAO = UserDAO.getInstance();
    private final WeatherDAO weatherDAO = WeatherDAO.getInstance();
    private final UserCityDAO userCityDAO = UserCityDAO.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        StringBuilder stringBuilder = new StringBuilder(req.getPathInfo());
        userId = Integer.parseInt(stringBuilder.replace(0,1,"").toString());
        List<UserCity> journal = userCityDAO.findCitiesByUserId(userId);

        req.setAttribute("journal", journal);
        req.setAttribute("user", userDAO.findById(userId).get());

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/users/show.jsp");
        requestDispatcher.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userDAO.delete(userId);
        resp.sendRedirect("/users");
    }
}
