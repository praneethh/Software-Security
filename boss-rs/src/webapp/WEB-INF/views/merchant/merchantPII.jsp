<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<html>
<security:authorize access="hasRole('ROLE_MERCHANT')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>PII Authorization</title>
</head>
<body>
<c:choose>
<c:when test="${status.equals('F')}">
No Existing requests
<br/>
</c:when>
<c:otherwise>A new request is there

<a href="<c:url value='merchantpiiauth/merchantpiiauthorize'/>">Approve Request</a><br><br>
</c:otherwise>
</c:choose>
<br>
${piiauth}
<br><br>
<form action="/boss-rs/merchant" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>