<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<security:authorize access="hasRole('ROLE_EMPLOYEE')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<c:choose>
    <c:when test="${status.equals('F')}">
    Access Denied
    <br />
      </c:when>
      <c:when test="${status.equals('N')}">
    No transactions exists for user ${username}
    <br />
      </c:when>
      <c:when test="${status.equals('E')}">
    Exception occured. Please try after sometime;
    <br />
      </c:when>
       <c:otherwise>
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
       <c:forEach items="${RetrieveExtTransactions}" var="tran">
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
      </c:otherwise>
</c:choose>
<br><br>
<form action="/boss-rs/InternalUser" >
<input type="submit" value="Home">
</form>
<br><br>
<form action="/boss-rs/InternalUser" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>