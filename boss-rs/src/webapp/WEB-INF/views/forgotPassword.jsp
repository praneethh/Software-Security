<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="net.tanesha.recaptcha.ReCaptcha"%>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Boss Forgot Password Page</title>
</head>
<body>

											
<form:form action="forgotPasswordAction"  commandName="forgotpassword" method="POST">
<table>
										<tbody>

												<tr>
													<td colspan="2">
														<div align="left" style="font-size: 22px; color: #ffffff">Forgot
															Password</div> <br> <br>
													</td>
												</tr>

												<tr>																						
												<td>
												Enter Username
												</td>
													<td>
													<form:input path="userName" id="userName"/>
													</td>
												</tr>
												
											<tr>		
													<td>
								
					<input id="submit" type="submit" value="Send new password to my email"  />
																<br>
																
																																					
													</td>
												</tr>
												<tr>
														<% ReCaptcha c = ReCaptchaFactory.newSecureReCaptcha("6LeHaP0SAAAAAE4T8SJq-eYPhm_BtNQ6Hi3U3GfW", 

"6LeHaP0SAAAAALdnTmrUmOH4BBvrWEt8KfBFZYrQ", 

false);

out.print(c.createRecaptchaHtml(null, null));

  %>
</tr>
											
										</tbody>
									</table> 
									</form:form>
</body>
</html>