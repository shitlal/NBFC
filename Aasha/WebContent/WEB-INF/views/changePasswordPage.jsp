<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>NBFC Login Page</title>
<link href="css/style.css" rel="stylesheet" type="text/css" media="all">
<!-- <link rel="icon" type="image/ico" href="image/google.ico"/> -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="<%=request.getContextPath()%>/css/stylesheet.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
<style type="text/css">
.footer1{position:relative !important}
</style>
</head>
<body>
	<div STYLE="height: 690px; font-size: 12px; overflow: auto;">
		<div class="wrapper">
			<div class="topwrapper">
				<!-- <img src="image/google.png" alt="googlelogo"> -->
				<!--  <img src="images/Logo.jpg" style="margin: top" hight="50%" width="30%"> -->
			</div>
			<div class="banner">
				<!-- <h1>
			One account. All of Google.
		  </h1> -->
				<!-- <h2 class="small">
			Sign in to continue to Gmail
			</h2>	 -->
			</div>
			<div class="main">
				<div class="login-card">
					<div class="circle-mask">
						<img src="images/Logo.jpg" style="margin: top" hight="100%" width="100%">
					</div>
					<br>
					<br>
					<form:form method="POST" action="nbfcLoginSubmitForm.html">

						<form:input path="usr_id" value="" placeholder="User ID" readonly="true" />
						<form:errors path="usr_id" Class="error"/>
						<form:input path="usr_password" value="" type="password" placeholder="Password"/>
						<form:errors path="usr_password" Class="error"/>
						<form:input path="usr_password" value="" type="password"placeholder="Password" />
						<form:errors path="usr_password" Class="error"/>
						<span Class="error">${InvalidCredencialKey}</span>
						<input type="submit" value="Sign In" class="login login-submit" />
						<!-- <input type="text" name="user" placeholder="Enter Your Email">
					<input type="submit" name="login" class="login login-submit" value="Next"> -->
					</form:form>
					<a href="#" class="need-help"> <b>Forgot Password</b></a>
				</div>
				<div class="one-google">
					<p class="create-account">
						<span id="link-signup"> <!-- <a href="">
                            Create account
                            </a>  -->
						</span>
					</p>
					<p class="tagline">
						<!--  One Google Account for everything Google -->
					</p>
					<div class="logo-strip">
						<!--  <img src="image/gmailfooterpic.JPG"> -->
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>