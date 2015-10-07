<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html>
<html>
<security:authorize access="hasRole('ROLE_MERCHANT')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Home </title>
</head>
<body>
Welcome Merchant <br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<c:url value="/j_spring_security_logout" var="logoutUrl" />
	
	<form action="${logoutUrl}" method="post" id="logoutForm">
	
	</form>
	
	<script>
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>

	<c:if test="${pageContext.request.userPrincipal.name != null}">
		
			Username : ${pageContext.request.userPrincipal.name} | 
			
				<a href="javascript:formSubmit()"> Logout</a>
		
	</c:if>
<br><br><br><br><br><br><br>
<a href="<c:url value='merchantaccountdetails' />">View Account Details</a><br><br>
<a href="<c:url value='merchanttransactiondetails' />">View Transaction Details</a><br><br>
<a href="<c:url value='merchantprofiledetails' />">View Profile Details</a><br><br>
<a href="<c:url value='merchantcreditdebit' />">Credit/Debit</a><br><br>
<a href="<c:url value='merchanttransferfunds'/>">Transfer Funds</a><br><br>
<a href="<c:url value='merchanttransrequest'/>">View Transaction Requests</a><br><br>
<a href="<c:url value='merchantpiiauth'/>">Authorize PII Requests</a><br><br>
<a href="<c:url value='merchantrevoke'/>">Revoke Transactions</a><br><br>
<a href="<c:url value='merchantdelete'/>">Delete Account</a><br><br>
<a href="<c:url value='merchantpayrequests'/>">View payment Requests</a><br><br>
</body>
</security:authorize>
</html>