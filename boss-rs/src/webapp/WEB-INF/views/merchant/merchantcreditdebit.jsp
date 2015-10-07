<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html>
<html>
<security:authorize access="hasRole('ROLE_MERCHANT')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Credit/Debit</title>
</head>
<body>
<br><br>
<form action="merchantcreditdebit/merchantcredit" method="post">
Enter amount to Credit: <input type="text" name="credit" required/>
<input type="submit" value="submit">
</form>
<br><br>
<form  action="merchantcreditdebit/merchantdebit" method="post">
Enter amount to Debit: <input type="text" name="debit" required/>
<input type="submit" value="submit">
</form>

<br><br><br>
<form action="/boss-rs/merchant" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>