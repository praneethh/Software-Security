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
    No existing requests
    <br />
      </c:when>
       <c:otherwise>
      <table>
		<tr>
        <th width="80">FNAME</th>
        <th width="140">LNAME</th>
        <th width="120">ADDRESS</th>
        <th width="120">PHONENUMBER</th>
        <th width="120">EMAIL</th>
        
      <c:forEach items="${DeleteExtUserReq}" var="userreq">
        <tr>
            <td>${userreq.fname}</td> 
            <td>${userreq.lname}</td>
            <td>${userreq.address}</td> 
            <td>${userreq.phoneno}</td> 
            <td>${userreq.email}</td>  
        <td>
        <form action="adminDeleteExtUserApprove" method="post">
        <input type="hidden" value="${userreq.uname}" name="username" />
        <input type="submit" value="Approve">
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