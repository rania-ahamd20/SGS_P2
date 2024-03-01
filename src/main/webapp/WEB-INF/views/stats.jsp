<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Statistical Data Page</title>
</head>
<body>
    <h1>Statistical Data</h1>

    <p>Class Average: <%= request.getAttribute("classAverage") %></p>
    <p>Median: <%= request.getAttribute("median") %></p>
    <p>Highest Mark: <%= request.getAttribute("highestMark") %></p>
    <p>Lowest Mark: <%= request.getAttribute("lowestMark") %></p>
</body>
</html>
