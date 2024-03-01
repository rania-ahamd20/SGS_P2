<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Marks Page</title>
</head>
<body>
    <h1>Student Marks</h1>

    <table border="1">
        <tr>
            <th>Course</th>
            <th>Mark</th>
        </tr>

        <%-- Assuming 'marks' is a list of objects with 'course' and 'mark' properties --%>
        <c:forEach var="mark" items="${marks}">
            <tr>
                <td>${mark.course}</td>
                <td>${mark.mark}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
