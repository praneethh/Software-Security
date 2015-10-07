<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<security:authorize access="hasRole('ROLE_MERCHANT')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Transfer transactions</title>
</head>
<body>
<c:choose>
    <c:when test="${status.equals('F')}">
    No requests found
    <br />
      </c:when>
       <c:otherwise>
<table>
<tr>
        <th width="80">TransactionID</th>
      	<th width="120">TransactionAmount</th>
        <th width="120">TransactionDescription</th>
        <th width="120">TransactionStatus</th>
        <th width="120">AccountFrom</th>
        <th width="120">AccountTo</th>
        <th width="120">TransactionDate</th>
            </tr>
<c:forEach items="${revoke}" var="tran">
        <tr>
            <td>${tran.transid}</td>
            
            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${tran.transtype}</td>
            <td>${tran.transamt}</td>
            <td>${tran.transdesc}</td>
            <td>${tran.transstatus}</td>
            <td>${tran.transfrom}</td>
            <td>${tran.transto}</td>
            <td>${tran.transdate}</td>
            <td>
            <form action="merchantrevoke/merchantsrevoke" method="post">
		<input type="hidden" name="tranid" value="${tran.transid}" required/>
				
<input type="submit" value="revoke">
</form>
            </td>
         </tr>
              
    </c:forEach>
</table>
</c:otherwise>
</c:choose>
<br><br>
<form action="/boss-rs/merchant" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>