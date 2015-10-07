<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- Internal User Homepage -->
<html>
<security:authorize access="hasRole('ROLE_EMPLOYEE')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Internal User Homepage</title>
</head>
<body>
	Internal User Homepage
	<br><br>
	${welcome}
	${message}
	
	<c:url value="/j_spring_security_logout" var="logoutUrl" />
	
	<form action="${logoutUrl}" method="post" id="logoutForm">
	
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	
	</form>
	
	<script>
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>

	<c:if test="${pageContext.request.userPrincipal.name != null}">
		<h2>
			Welcome : ${pageContext.request.userPrincipal.name} | <a
				href="javascript:formSubmit()"> Logout</a>
		</h2>
	</c:if>
<br><br><br>
<a href="<c:url value='InternalUserViewUsers' />">View External Users</a><br><br>	
<a href="<c:url value='InternalUserViewExtProfileReq' />">View External Users Profile Change Request</a><br><br>	
<br><br>
<form action="/boss-rs/InternalUser" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>