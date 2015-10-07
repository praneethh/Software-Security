<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    	<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html> 
<html>
<security:authorize access="hasRole('ROLE_USER')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fund Transfer</title>
</head>
<body>
<br><br>
<form action="extusertransferfunds/fundtransfer" method="post" enctype="multipart/form-data">
Enter the Payee Account number 	: <input type="text" name="account" required/><br><br>
Enter Amount to be transferred&nbsp;&nbsp;&nbsp;&nbsp; :&nbsp;<input type="text" name="amount" required/><br><br>
<tr>
	<td>
	<input type="file" id="file" name="file"  required/>
	</td>
	</tr>

<input type="submit" value="Transfer">
</form>
<br><br>
<form action="/boss-rs/extuser" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>