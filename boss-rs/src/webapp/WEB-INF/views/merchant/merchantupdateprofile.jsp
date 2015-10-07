<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html>
<html>
<security:authorize access="hasRole('ROLE_MERCHANT')">
<head>
<meta charset="UTF-8">
<title>Update</title>
</head>

<script>
</script>
<body>
<br><br>
<form action="merchantupdate/merchants" method="post" name="updateform">
CompanyName: <input type="text" name="fname" required/><br><br>
OwnerName:  <input type="text" name="lname" required/><br><br>
Address:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="address" required/><br><br>
Mobile:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="phoneno" required/><br><br>
Email:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="email" required/><br><br><br>
<input type="submit" value="Update">
</form>
<br><br><br>
<form action="/boss-rs/merchant" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>	