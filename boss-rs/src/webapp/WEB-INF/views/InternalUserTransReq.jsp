<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
     <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- Page displaying options for internal user to request external user view transactions and view transactions -->
<html>
<security:authorize access="hasRole('ROLE_EMPLOYEE')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Internal User: Request and View Transactions</title>
</head>
<body>
		<form action="InternalUserTransRequest" method="post">
        <input type="hidden" value="${username}" name="username" />
        <input type="hidden" value="${pageContext.request.userPrincipal.name}" name="loginuser" />
        <input type="submit" value="Request View Transactions">
		</form>
		<br><br>
		<form action="InternalUserExtTransactions" method="post">
        <input type="hidden" value="${username}" name="username" />
        <input type="hidden" value="${pageContext.request.userPrincipal.name}" name="loginuser" />
        <input type="submit" value="View Transactions">
		</form>
<br><br>
<form action="/boss-rs/InternalUser" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>