package org.example.controller;

import org.example.util.DBConnector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/stats")
public class StatsServlet extends HttpServlet {

    private static class StatisticalData {
        private double classAverage;
        private int median;
        private int highestMark;
        private int lowestMark;


        public void setClassAverage(double classAverage) {
            this.classAverage = classAverage;
        }

        public void setMedian(int median) {
            this.median = median;
        }


        public void setHighestMark(int highestMark) {
            this.highestMark = highestMark;
        }

        public void setLowestMark(int lowestMark) {
            this.lowestMark = lowestMark;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username != null) {
            try (Connection connection = DBConnector.getConnection()) {
                double classAverage = calculateClassAverage(connection);
                int median = calculateMedian(connection);
                int highestMark = calculateHighestMark(connection);
                int lowestMark = calculateLowestMark(connection);

                // Set the statistical data as request attributes for JSP
                request.setAttribute("classAverage", classAverage);
                request.setAttribute("median", median);
                request.setAttribute("highestMark", highestMark);
                request.setAttribute("lowestMark", lowestMark);

                request.getRequestDispatcher("stats.jsp").forward(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp");
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    private StatisticalData retrieveStatisticalData(Connection connection) throws SQLException {
        StatisticalData stats = new StatisticalData();

        stats.setClassAverage(calculateClassAverage(connection));

        stats.setMedian(calculateMedian(connection));

        stats.setHighestMark(calculateHighestMark(connection));

        stats.setLowestMark(calculateLowestMark(connection));

        return stats;
    }

    private double calculateClassAverage(Connection connection) throws SQLException {
        String query = "SELECT AVG(mark) AS class_average FROM grades_2";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("class_average");
            }
        }
        return 0.0;
    }

    private int calculateMedian(Connection connection) throws SQLException {
        // Assuming that there is a column named 'mark' in the 'grades_2' table
        String query = "SELECT mark FROM grades_2 ORDER BY mark";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            int rowCount = 0;
            int middleRow = rowCount / 2;

            while (resultSet.next()) {
                if (rowCount == middleRow) {
                    return resultSet.getInt("mark");
                }
                rowCount++;
            }
        }
        return 0;
    }

    private int calculateHighestMark(Connection connection) throws SQLException {
        String query = "SELECT MAX(mark) AS highest_mark FROM grades_2";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("highest_mark");
            }
        }
        return 0;
    }

    private int calculateLowestMark(Connection connection) throws SQLException {
        String query = "SELECT MIN(mark) AS lowest_mark FROM grades_2";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("lowest_mark");
            }
        }
        return 0;
    }
}
