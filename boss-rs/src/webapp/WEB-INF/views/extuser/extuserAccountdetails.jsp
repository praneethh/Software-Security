<%@page import="boss.data.entities.BossAccount"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
     <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<security:authorize access="hasRole('ROLE_USER')">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Account Details</title>

</head>
<body>
<table>
	<tr>
        <th width="80">AccountNumber</th>
        <th width="140">Balance</th>
        <th width="120">Created Date</th>
    </tr>
<c:forEach items="${useraccountdetails}" var="accounts">
        <tr>
      <!--    	
      <% /* Object accObj = pageContext.getAttribute("accounts"); 
        		BossAccount acc = (BossAccount) accObj;
        		String strAccId = Long.toString(acc.getAccountId());
        		
        		char[] charAcc = strAccId.toCharArray();
    			
    			for(int i=0; i<charAcc.length; i++){
    				if(i<charAcc.length-2){
    					charAcc[i]='X';
    				}
    			}
    			String strAccOpt = new String(charAcc);
        		
        	*/%>
        	<td><% /*=strAccOpt */%></td> -->
            <td>${accounts.accountId}</td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${accounts.accountbal}</td>
            <td>${accounts.createddate}</td>
       </tr>
    </c:forEach>
</table>

<a href="<c:url value='/extuser'/>">Home</a><br><br>

${condition}
</body>
</security:authorize>
</html>