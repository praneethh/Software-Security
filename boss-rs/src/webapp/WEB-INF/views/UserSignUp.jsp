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

<title>User Sign Up</title>
</head>
<Style>
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}
</Style>
<body>


</head>
<body>
<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>

	<center>
		<form:form method="POST" commandName="UserSignUp" 
			action="registrationPage">

			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr style="height: 250px">
						<td style="width: 550px; background-color: #333333" valign="top"><br>
							<center>
								<table border="0" cellpadding="10" cellspacing="10">
									<tbody>
										<tr>
											<td>
												<table border="0" cellpadding="3" cellspacing="3">
													<tbody>
														<tr>
															<td colspan="2"><div align="center"
																	style="font-size: 22px; color: #ffffff">Register</div>
																<br></td>
														</tr>
														<tr>
															<td style="color: white">
															  <form:label path="fname">
																	<spring:message code= "label.firstName" />
																</form:label>
																</td>
															<td><form:input path="fname" class="required" id="fname" /><br />
																<font color="red"></font></td>
														</tr>
														<tr>
															<td style="color: white">
															  <form:label path="lname">
																	<spring:message code= "label.lastName" />
																</form:label>
																</td>
															<td><form:input path="lname" class="required" id="lname"/><br />
																<font color="red"></font></td>
														</tr>
														<tr>
															<td style="color: white"><form:label path="uname">
																	<spring:message code="label.username" />
																</form:label></td>
															<td><form:input path="uname" class="required" id="uname" /><br />
																<font color="red"></font></td>
														</tr>
														<tr>
															<td style="color: white"><form:label path="email">
																	<spring:message code="label.email" />
																</form:label></td>
															<td><form:input path="email"  id="EmailField" class="required"/><br />
																<font color="red"></font></td>
														</tr>
														<tr>
															<td style="color: white"><form:label path="phoneno">
																	<spring:message code="label.phone" />
																</form:label></td>
															<td><form:input path="phoneno" class="required" id="phoneno"/><br />
																<font color="red"></font></td>
														</tr>
														<tr>
															<td style="color: white"><form:label path="ssn">
																	<spring:message code="label.ssn" />
																</form:label></td>
															<td><form:input path="ssn" class="required" id="SSN"/><br />
																<font color="red"></font></td>
														</tr>
														<tr>
															<td style="color: white"><form:label path="address">
																	<spring:message code="label.address" />
																</form:label></td>
															<td><form:input path="address" class="required" id="address"/><br />
																<font color="red"></font></td>
														</tr>											
																	
														<tr>
														<td>
														<input id="submit" type="submit" value="Submit"/> <br />
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
												</table> <br>
											</td>
										</tr>
									</tbody>
								</table>
							</center></td>
					</tr>
				</tbody>
			</table>
		</form:form>
	</center>

	<br />
	<br />
	<br />
<a href="<c:url value='/admin'/>">Home</a><br><br>
</body>
</html>