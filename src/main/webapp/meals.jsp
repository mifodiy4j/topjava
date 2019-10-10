<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table style="border: 1px solid black;">
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Excess</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="meal" items="${mealToList}">
        <tr>
            <td>${meal.dateTime}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <c:choose>
                <c:when test="${meal.excess}">
                    <td style="color: red">ПРЕВЫШЕНО</td>
                </c:when>
                <c:otherwise>
                    <td style="color: black">НЕПРЕВЫШЕНО</td>
                </c:otherwise>
            </c:choose>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
