<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<html>
<security:authorize access="hasRole('ROLE_ADMIN')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
Admin: Revoke Transaction Request Status
<br><br>
${status}
<br><br>
<form action="/boss-rs/admin" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>