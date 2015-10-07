<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
    
<!DOCTYPE html>
<%@ page session="true" %>
<html>
<security:authorize access="hasRole('ROLE_USER')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>PaymentForm</title>
</head>
<body>
Submit your details below to make payment to the merchant:${merchant}<br><br><br>

<form action="extuserMerchant/fillForm" method="post">
UserName: <input type="text" name="uname" required/><br><br>
AccountNumber: <input type="text" name="account" required/><br><br>
Amount: <input type="text" name="amount" required/>
<input type="hidden" name="merchant" value="${merchant }" />
<br><br>
<input type="submit" value="Submit">
</form>
<br><br>
<form action="/boss-rs/extuser" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>