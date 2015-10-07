<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    	<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<security:authorize access="hasRole('ROLE_USER')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Operation page</title>
</head>
<body>
${status}
${mesg}
${statu}
${masge}
${email}
${phone}
${account}
${amount}
${number}
${Fname}
${Lname}
${Address}
${tex}
${tries}
${am}
${ams}
${ac}
${tranid}
${pii}
${revoke}
${delete}
${piiautho}
${deluse}
${accnu}
${pay}
${devuda}
${usernames}
${accounts}
${numbers}
${wrong}
${wrongs}
${helo}
${fileerror}
${tranferfailed}
${encodingerror}
${invalidFile} 
<br>
<form action="/boss-rs/extuser" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>