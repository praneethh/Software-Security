<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<security:authorize access="hasRole('ROLE_ADMIN')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<c:choose>
    <c:when test="${status.equals('F')}">
    No existing request
    <br />
      </c:when>
       <c:otherwise>
       <table>
		<tr>
        <th width="80">TransID</th>
        <th width="120">Username</th>
        <th width="120">TransAmount</th>
        <th width="120">TransStatus</th>
        <th width="120">AccountFrom</th>
        <th width="120">AccountTo</th>
        <th width="120">TransDate</th>
        </tr>
       <c:forEach items="${RetrieveMakePaymentAuthReq}" var="tran">
        <tr>
            <td>${tran.transid}</td>
            <td>${tran.username}</td>
            <td>${tran.transamt}</td>
            <td>${tran.transstatus}</td>
            <td>${tran.transfrom}</td>
            <td>${tran.transto}</td>
            <td>${tran.transdate}</td>
            <td>
        <form action="adminAuthMakePaymentApprove" method="post">
        <input type="hidden" value="${tran.transid}" name="transid" />
        <input type="hidden" value="${tran.username}" name="username" />
        <input type="submit" value="Approve" name = "request">
		</form>  
		</td> 
          </tr>
    </c:forEach>
</table>
      </c:otherwise>
</c:choose>
<br><br>
<form action="/boss-rs/admin" >
<input type="submit" value="Home">
</form>
</body>
</security:authorize>
</html>