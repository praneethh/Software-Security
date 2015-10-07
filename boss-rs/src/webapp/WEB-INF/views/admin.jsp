<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<html>
<security:authorize access="hasRole('ROLE_ADMIN')">
<body>
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
<a href="<c:url value='adminViewUsers' />">View External Users</a><br><br>
<a href="<c:url value='admincreateExternalUser' />">Add External Users</a><br><br>
<a href="<c:url value='admincreateInternalUser' />">Add Internal Users</a><br><br>
<a href="<c:url value='admincreateMerchant' />">Add Merchant</a><br><br>
<a href="<c:url value='adminViewExtProfileReq' />">View External Users Profile Change Request</a><br><br>	
<a href="<c:url value='adminDeleteUserReq' />">Delete External User Request</a><br><br>
<a href="<c:url value='adminViewInternalUser' />">Delete Internal User</a><br><br>
<a href="<c:url value='adminViewRevokeTransReq' />">View Revoke Transaction Request</a><br><br>
<a href="<c:url value='adminViewAuthMakePayment' />">View Make Payment Authorization Request</a><br><br>
</body>
</security:authorize>
</html>