<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html>
<html>
<security:authorize access="hasRole('ROLE_MERCHANT')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Transaction Details</title>
</head>
<body>
<c:choose>
<c:when test="${status.equals('F')}">
No Existing requests
<br/>
</c:when>
<c:otherwise>The requests are
<table>
<tr>
        <th width="80">TransactionID</th>
        <th width="140">TransactionType</th>
        <th width="120">TransactionAmount</th>
        <th width="120">TransactionDescription</th>
        <th width="120">TransactionStatus</th>
        <th width="120">AccountFrom</th>
        <th width="120">AccountTo</th>
        <th width="120">TransactionDate</th>
        
        
    </tr>
<c:forEach items="${merchanttranrequest}" var="tran">
        <tr>
            <td>${tran.transid}</td>
            
            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${tran.transtype}</td>
            <td>${tran.transamt}</td>
            <td>${tran.transdesc}</td>
            <td>${tran.transstatus}</td>
            <td>${tran.transfrom}</td>
            <td>${tran.transto}</td>
            <td>${tran.transdate}</td>
            
            
          </tr>
    </c:forEach>
</table>
<a href="<c:url value='merchanttransrequest/merchantrequest'/>">Approve Requests</a><br><br>
</c:otherwise>
</c:choose>
<br><br><br>
<form action="/boss-rs/merchant" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>