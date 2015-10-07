<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html>
<html>
<security:authorize access="hasRole('ROLE_USER')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Merchant Details</title>
</head>
<body>
<table>
<tr>
<th>Available Merchants</th>
</tr>

<c:forEach items="${hi}" var="tran">
<tr>
<td>${tran}<br>
</td>
<td>
<br>
 
<form action="extuserPayment/extuserMerchant" method="POST">

<input type="hidden" name="initpay" value="${tran}" required/>
				
<input type="submit" value="Select">
</form>
 
<!--  <a href="<c:url value='extuserPayment/extuserMerchant/${tran}'/>">Select</a><br><br> -->
</td></tr>
</c:forEach>
</table>
<br><br>
<form action="/boss-rs/extuser" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>