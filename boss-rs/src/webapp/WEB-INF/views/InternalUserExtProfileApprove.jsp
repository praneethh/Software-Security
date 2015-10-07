<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<security:authorize access="hasRole('ROLE_EMPLOYEE')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Internal User: Approve External User profile change request</title>
</head>
<body>
Internal User: Approve External User profile change request
<br><br>
${status}
<br><br>
<form action="/boss-rs/InternalUser" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>