<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<security:authorize access="hasRole('ROLE_USER')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Home </title>
</head>
<body>
${welcome} <br>
${message}
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
<a href="<c:url value='extuseraccountdetails' />">View Account Details</a><br><br>
<a href="<c:url value='extusertransactiondetails' />">View Transaction Details</a><br><br>
<a href="<c:url value='extuserprofiledetails'/>">View Profile Details</a><br><br>
<a href="<c:url value='extusercreditdebit' />">Credit/Debit</a><br><br>
<a href="<c:url value='extusertransferfunds'/>">Transfer Funds</a><br><br>
<a href="<c:url value='extusertransrequest'/>">View Transaction Requests</a><br><br>
<a href="<c:url value='extuserpiiauth'/>">Authorize PII Requests</a><br><br>
<a href="<c:url value='extuserevoke'/>">Revoke Transactions</a><br><br>
<a href="<c:url value='extuserdelete'/>">Delete Account</a><br><br>
<a href="<c:url value='extuserPayment'/>">Initiate a payment</a><br><br>

</body>
</security:authorize>
</html>