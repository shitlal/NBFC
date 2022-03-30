<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Forgot Password Page</title>
		<link href="css/custom.css" type="text/css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css" media="all">
		<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
	body {
		/* background:url(images/bg-lgoin1.jpg) no-repeat center !important; */
		background-image: url(images/bg8.jpg) !important;
		background-position-y: top;
		background-size: cover !important;
		background-repeat: no-repeat;
		background-attachment: fixed;
		z-index: 10;
}
.login-container {
	width: 900px;
	margin: 50px auto 0;
}
.login-wrap,.login-wrap1 {
	float: left;
	margin: 0 !important;
	box-shadow: none !important;
}
.login-wrap {
	width: 475px !important;
	max-width: 500px;
}
.login-wrap1 {
	background-color: white;
	width: 425px !important;
	display: block;
}
/* .login-wrap{    min-height: 550px;} */
.login-form .group .button {
	font-size: 14px;
}
.login-html {
	background: rgba(247, 247, 247, 0.82);
}
.sign-in-htm h5 { /* margin: 20px 0;
    text-align: center;
    font-size: 18px;
    color: red; */
	padding: 20px 5px;
	border-radius: 9px;
	background-color: #f3f3f3;
	text-align: center;
	font-size: 14px;
	color: #077b1f;
}
.login-wrap1 {
	width: 100%;
	max-width: 450px;
	min-height: 580px;
	/* margin:auto;
	max-width:525px;
	min-height:670px; */
	position: relative;
	background: url() images/form_bg.jpg ) no-repeat center;
	background-size: cover;
	/* box-shadow:0 12px 15px 0 rgba(0,0,0,.24),0 17px 50px 0 rgba(0,0,0,.19); */
}
.login-html1 {
	width: 100%;
	height: 100%;
	position: absolute;
	padding: 40px 70px 60px 70px;
	/* padding:60px 70px 50px 70px;	 */
	/* background: rgba(40, 57, 101, 0.68);*/
	background: rgba(247, 247, 247, 0.8);
	/* 	 background: rgba(247, 247, 247, 0.9);	/* white background color*/
}
.login-html { /*  background: rgba(40, 57, 101, 0.68) !important; */
	background: rgba(40, 57, 101, 0.39) !important;
}
.float-l {
	float: left;
	box-shadow: 0 12px 15px 0 rgba(0, 0, 0, .24), 0 17px 50px 0
		rgba(0, 0, 0, .19);
}

.login-inside {
	position: relative;
	width: 100%;
	height: 100vh;
}

.login-inside	h1 {
	color: white;
	text-align: center; /*   top:45%; */
	top: 110px;
	position: absolute;
	padding-bottom: 21px;
	width: 100%;
	border-bottom: 1px solid white;
}

.login-inside	h5 {
	color: white;
	width: 100%;
	text-align: center; /* top: 90%; */
	text-align: center;
	top: 250px;
	position: absolute;
	padding-bottom: 21px;
}

#body-section {
	min-height: 400px !important;
}
</style>

</head>
<body>
	<div class="login-container">
		<div class="float-l">
			<div class="login-wrap">
				<div class="login-html">
					<div class="login-inside">
						<h1>
							Welcome to <br> CGTMSE
						</h1>
						<h5>Credit Guarantee Scheme - II for NBFCs</h5>
					</div>
				</div>
			</div>
			<div class="login-wrap1">
				<div class="login-html1">
					<div class="login-form">
						<div class="sign-in-htm">
							<img src="images/CGTMSE_logo.png" style=" margin: top; height: 100px; width: 100px;">
							<form:form method="POST" action="OTPSubmissions.html" id="A">
								<span Class="error">${message}</span>
								<h1 align="center">
									<font face="Arial">Forgot Password</font>
								</h1>
								<h5>Enter The User ID and We'll Send verification code to
									reset your password to email address.</h5>
								<div class="group">
									<form:input path="usr_id" value="" placeholder="User ID" class="input" readonly="true" />
									<form:errors path="usr_id" Class="error" />
									<span Class="error">${InvalidCredencialKey}</span>
								</div>
								<div class="group">
									<c:choose>
										<c:when test="${success==true || Resend==true}">
											<form:input path="otp" value="" placeholder="OTP" class="input" readonly="" />
											<form:errors path="otp" Class="error" />
											<span Class="error">${InvalidCredencialKeyOTP}</span>
											<input type="submit" name="action1" id="but1" value="Resend" class="button" style="width: 45%; float: left; margin-top: 20px;" onclick="show()" />
											<input type="submit" name="action2" id="but2" value="Submit" class="button" style="width: 45%; margin-top: 20px; margin-left: 20px; display: inline-block; margin-left: 18px;" />
										</c:when>
										<c:when test="${success!=true}">
											<input type="submit" name="action1" id="but1" value="Send Varification Code On Email" class="button" onclick="show()" />
										</c:when>
										<c:when test="${Resend==true}">
											<input type="submit" name="action1" id="but1" value="Resend" class="button" onclick="show()" />
										</c:when>
									</c:choose>
								</div>
							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		function show() {
			document.getElementById("but1").style.visibility = "hidden";
			document.getElementById("but2").style.visibility = "visible";
		}
	</script>
</body>
</html>