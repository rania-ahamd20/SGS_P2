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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/marks")
public class MarksServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username != null) {
            try (Connection connection = DBConnector.getConnection()) {
                List<MarkEntry> marks = retrieveStudentMarks(connection, username);

                request.setAttribute("marks", marks);

                request.getRequestDispatcher("marks.jsp").forward(request, response);

            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp");
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    private List<MarkEntry> retrieveStudentMarks(Connection connection, String username) throws SQLException {
        List<MarkEntry> marks = new ArrayList<>();

        String query = "SELECT courses.course_name, grades_2.mark " +
                "FROM students " +
                "JOIN grades_2 ON students.student_id = grades_2.student_id " +
                "JOIN courses ON grades_2.course_id = courses.course_id " +
                "WHERE students.username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                int mark = resultSet.getInt("mark");
                marks.add(new MarkEntry(courseName, mark));
            }
        }

        return marks;
    }

    private static class MarkEntry {
        private final String courseName;
        private final int mark;

        public MarkEntry(String courseName, int mark) {
            this.courseName = courseName;
            this.mark = mark;
        }

        public String getCourseName() {
            return courseName;
        }

        public int getMark() {
            return mark;
        }
    }
}
