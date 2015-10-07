<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- Page displaying options for internal user to request view transactions and view transactions -->
<html>
<security:authorize access="hasRole('ROLE_ADMIN')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin: Request and View Transactions</title>
</head>
<body>
		<form action="adminTransRequest" method="post">
        <input type="hidden" value="${username}" name="username" />
        <input type="hidden" value="${pageContext.request.userPrincipal.name}" name="loginuser" />
        <input type="submit" value="Request View Transactions">
		</form>
		<br><br>
		<form action="adminExtTransactions" method="post">
        <input type="hidden" value="${username}" name="username" />
        <input type="hidden" value="${pageContext.request.userPrincipal.name}" name="loginuser" />
        <input type="submit" value="View Transactions">
		</form>
		<br>
		<form action="adminExtPIIRequest" method="post">
        <input type="hidden" value="${username}" name="username" />
        <input type="submit" value="Request PII">
		</form>
		<br>
		<form action="adminExtViewPII" method="post">
        <input type="hidden" value="${username}" name="username" />
        <input type="submit" value="View PII">
		</form>
<br><br>
<form action="/boss-rs/admin" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>