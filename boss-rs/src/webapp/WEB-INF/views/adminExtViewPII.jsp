<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<security:authorize access="hasRole('ROLE_ADMIN')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<c:choose>
    <c:when test="${status.equals('F')}">
    Access Denied
    <br />
      </c:when>
      <c:when test="${status.equals('N')}">
    Could not process the request. Please try after sometime.
    <br />
      </c:when>
      <c:when test="${status.equals('NU')}">
   No Such user exist now.
    <br />
      </c:when>
       <c:otherwise>

        <th width="80">SSN/License No</th> <!-- Priya changes -->
        <br>
          ${RetrievePII}         
      </c:otherwise>
</c:choose>
<br><br>
<form action="/boss-rs/admin" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>