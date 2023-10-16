package ru.kozhevnikov.weatherapp.servlets;

import ru.kozhevnikov.weatherapp.dao.CityDAO;
import ru.kozhevnikov.weatherapp.model.City;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
/**
 * Сервлет, в котором реализована логика передачи данных о погоде, содержащихся в базе данных, на представление - JSP страницу.
 */
@WebServlet("/get_data")
public class DataAnalyzer extends HttpServlet {
    private CityDAO cityDAO = new CityDAO();
    private List<City> cities;
    private static final String NO_DATA = "В базе данных нет информации о погоде";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        cities = cityDAO.getTodayDateFromDb();
        if (cities.isEmpty()) {
            resp.getWriter().write(NO_DATA);
            return;
        }
        req.setAttribute("cities", cities);
        initialAnalysis(req);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/analysisView.jsp");
        dispatcher.forward(req, resp);
    }
    private void initialAnalysis(HttpServletRequest request){
        Comparator<City> comparator = (o1,o2) -> o1.getWeather().getAverageTemperature() - o2.getWeather().getAverageTemperature();
        City hottest = cities.stream().max(comparator).get();
        City coldest = cities.stream().min(comparator).get();
        request.setAttribute("hot", hottest);
        request.setAttribute("cold", coldest);
    }

}
