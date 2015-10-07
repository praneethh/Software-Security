<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html>
<html>
<security:authorize access="hasRole('ROLE_USER')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Modify Transaction</title>
</head>
<body>
<form action="modifytransaction/modify" method="post">
TransactionID: <input type="text" name="tranid" required/><br><br>
TransactionTo: <input type="text" name="tranto" required/><br><br>
TransactionAmount: <input type="text" name="amount" required/><br><br>
TransactionType: <input type="text" name="type" required/>
(credit/debit)
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