<%@ page import="org.ingcar_boot_war.dto.DeptDTO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
    DeptDTO data = (DeptDTO) request.getAttribute("resultdata"); //  == ${data}

    // 웹상에 출력
    int num = data.getDeptno();
    String name = data.getDname();
    String loc = data.getLoc();
    System.out.println(num + name + loc);

%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>list.jsp</title>
</head>
<body>
<h2>list.jsp</h2>

<table border="1">
    <thead>
    <th>deptno</th>
    <th>dname</th>
    <th>loc</th>
    </thead>
    <tbody>
<%--    <tr>
        <td>1</td>
        <td>1</td>
        <td>1</td>
    </tr>--%>
    <c:forEach var = "item" items="${resultdata }">
        <tr>
            <td>${item.deptno }</td>
            <td>${item.dname }</td>
            <td>${item.loc }</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>