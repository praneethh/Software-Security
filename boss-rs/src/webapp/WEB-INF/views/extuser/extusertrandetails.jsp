<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="boss.data.entities.BossExtTransaction"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html>
<html>
<security:authorize access="hasRole('ROLE_USER')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Transaction Details</title>
</head>
<body>
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
<c:forEach items="${extusertrandetails}" var="tran">
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
${piiautho}
<br>
<a href="<c:url value='/extuser'/>">Home</a><br><br>
</body>
</security:authorize>
</html>