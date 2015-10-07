<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Boss Reset Password Page</title>
<style>
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}

#reset-box {
	width: 300px;
	padding: 20px;
	margin: 100px auto;
	background: #fff;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 1px solid #000;
}
</style>
</head>
<body onload='document.resetPassword.username.focus();'>

<div id="login-box">
<table border="0" cellpadding="3" cellspacing="3">
										<tbody>

		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>											
<form:form action="resetPasswordAction"  commandName="resetpassword" method="POST">

												<tr>
													<td colspan="2">
														<div align="left" style="font-size: 22px; color: #ffffff">Reset
															Password</div> <br> <br>
													</td>
												</tr>

												<tr>
												<td>
												UserName
												</td>
													<td>
													<form:input path="userName" id="userName" />
													</td>
												</tr>
												<tr>
												<td>
												OTP
												</td>
													<td>
													<form:input path="otp" id="otp" />
													</td>
												</tr>
													<tr>
													<td>
												Password
												</td>
													<td>
													<form:password path="password" id="password" />
													</td>
												</tr>
													<tr>
													<td>
												Enter Password Again
												</td>
													<td>
													<form:password path="confirmPassword" id="confirmPassword" />
													</td>
												</tr>	
												<tr>
													<td></td>
													<td>
														<div align="left">


															<input class="btn btn-primary" type="submit"
																value="Reset Password"  />
																<br><br> 
																
																
														</div>
														
													</td>
												</tr>

													<script type="text/javascript">
														function button_actions() {
															if (document
																	.getElementById('otp').value == "") {
																alert("Please enter your otp");
																return false;
															}

														}
													</script>


											</form:form>
										</tbody>
									</table> <br>
									</div>
</body>
</html>