<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<html>
<security:authorize access="hasRole('ROLE_MERCHANT')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile Details</title>
</head>
<body>
<table>
<tr>
        
        <th width="140">CompanyName</th>
        <th width="120">OwnerName</th>
        <th width="120">RegistrationID</th>
        <th width="120">Address</th>
        <th width="120">PhoneNumber</th>
        <th width="120">Email</th>
        
    </tr>
<c:forEach items="${merchantprofiledetails}" var="accounts">
        <tr>
                        
            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${accounts.fname}</td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;${accounts.lname}</td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${accounts.ssn}</td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${accounts.address}</td>
            <td>${accounts.phoneno}</td>
            <td>${accounts.email}</td>
         </tr>
    </c:forEach>
    </table>
    <br>
    
<br><br>
    <a href="<c:url value='merchantprofiledetails/merchantupdate' />">Update Profile</a><br><br>
    
    <a href="<c:url value='/merchant'/>">Home</a><br><br>
</body>
</security:authorize>
</html>