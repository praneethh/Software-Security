<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- Internal User: View list of external users. Only username is displayed -->
<html>
<security:authorize access="hasRole('ROLE_ADMIN')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View External Users</title>
</head>
<body>
Admin: View Internal User
<br>
<c:choose>
    <c:when test="${Status.equals('F')}">
    <br>
    No Internal Users exists
    <br />
      </c:when>
       <c:otherwise>
<table>
		<tr>
        <th width="80">Username</th>
        </tr>    
<c:forEach items="${adminViewInternalUser}" var="extuser">
        <tr>
        <td>
        <form action="adminDeleteInternalUser" method="post">
        <input type="hidden" value="${extuser}" name="username" />
        <input type="submit" value="${extuser}">
		</form>
            </td>
          </tr>
    </c:forEach>
</table>
</c:otherwise>
</c:choose>
<br><br>
<form action="/boss-rs/admin" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>