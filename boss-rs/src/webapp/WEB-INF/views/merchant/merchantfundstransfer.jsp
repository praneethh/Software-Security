<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html>
<html>
<security:authorize access="hasRole('ROLE_MERCHANT')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FundsTransfer</title>
</head>
<body>
<br><br>
<form action="merchanttransferfunds/fundtransfer" method="post">
Enter the Payee Account number 	: <input type="text" name="account" required/><br><br>
Enter Amount to be transferred&nbsp;&nbsp;&nbsp;&nbsp; :&nbsp;<input type="text" name="amount" required/><br><br>
<input type="submit" value="Transfer">
</form>
</body>
</security:authorize>
</html>