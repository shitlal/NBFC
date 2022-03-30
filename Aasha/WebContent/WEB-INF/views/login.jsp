<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Aasha Login Page</title>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">  
<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Open+Sans|Roboto" rel="stylesheet">    
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
  <link href="css/custom.css" type="text/css" rel="stylesheet">
     
   <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
      
<style type="text/css">
body{	/* background:url(images/bg-lgoin1.jpg) no-repeat center !important; */
background-image:url(images/bg8.jpg) !important;	    background-position-y: top;
background-size:cover !important; 		background-repeat:no-repeat;	background-attachment:fixed;
z-index:10;		}
.login-container{	width:900px;     margin: 50px auto 0;}
.login-wrap,.login-wrap1{	 float:left;		  margin: 0 !important;	box-shadow:none !important;}
.login-wrap{	width:475px !important;	 max-width: 500px;	}
.login-wrap1{	background-color:white;  width:425px !important; display:block;	}

.login-wrap1{
	width:100%;		  
    max-width: 450px;
    min-height: 580px;    
	/* margin:auto;
	max-width:525px;
	min-height:670px; */	
	position:relative;
	background:url()images/form_bg.jpg) no-repeat center;
	background-size:cover;
	/* box-shadow:0 12px 15px 0 rgba(0,0,0,.24),0 17px 50px 0 rgba(0,0,0,.19); */
}

.login-html1{
	width:100%;
	height:100%;
	position:absolute;
	padding: 40px 70px 60px 70px;
	/* padding:60px 70px 50px 70px;	 */
	/* background: rgba(40, 57, 101, 0.68);*/
	background: rgba(247, 247, 247, 0.8);
	/* 	 background: rgba(247, 247, 247, 0.9);	/* white background color*/
}
.login-html{		/*  background: rgba(40, 57, 101, 0.68) !important; */ background: rgba(40, 57, 101, 0.39) !important;	}
.float-l{	    float: left;	box-shadow:0 12px 15px 0 rgba(0,0,0,.24),0 17px 50px 0 rgba(0,0,0,.19);}


.login-inside{	position:relative;	width:100%; height:100vh;}
.login-inside	h1{    color: white;
    text-align: center;	   /*   top:45%; */		top: 110px;    position: absolute;	    padding-bottom: 21px;	    width: 100%;
    border-bottom: 1px solid white;}
.login-inside	h5{		color: white;		width:100%;
    text-align: center;	    /* top: 90%; */	  text-align:center;    top: 250px;	  position: absolute;	    padding-bottom: 21px;	}
    #body-section{min-height:400px !important;}    
</style>
</head>

<body >		


<div class="login-container">
<div class="float-l">
<div class="login-wrap">
	
	<div class="login-html">
	<div class="login-inside">
		<h1>Welcome to	<br>
		 CGTMSE</h1>
		 <h5>Credit Guarantee Scheme - II for NBFCs</h5>
	</div>
	</div>
</div>

<div class="login-wrap1">
	<div class="login-html1">
	<!--<input id="tab-1" type="radio" name="tab" class="sign-in" checked><label for="tab-1" class="tab">Login</label>
		<input id="tab-2" type="radio" name="tab" class="sign-up"><label for="tab-2" class="tab">Sign Up</label>
		-->
		<div class="login-form">
			<div class="sign-in-htm">
				<img src="images/CGTMSE_logo1.png" style="margin: top; height:100px; width:200px;">
			<form:form method="POST" action="nbfcLoginSubmitForm.html" id="A">
			<span Class="error">${message}</span>
				<div class="group">
					<label for="user" class="label">Username</label>
					<form:input path="usr_id" placeholder="User ID" class="input" />
					<form:errors path="usr_id" Class="error"/>
					
					<!--<input id="user" type="text" class="input">--></div>
				<div class="group">
					<label for="pass" class="label">Password</label>
					
					<form:input path="usr_password" type="password" placeholder="Password" class="input" />
					<form:errors path="usr_password" Class="error"/>
					<span Class="error">${InvalidCredencialKey}</span>
																
					<!--<input id="pass" type="password" class="input" data-type="password">-->
				</div>
				<!-- <div class="group">
					<input id="check" type="checkbox" class="check" checked>
					<label for="check"><span class="icon"></span> Keep me Signed in</label>
				</div> -->
				<div class="group">
				<!--<input type="submit" value="Sign In" class="login login-submit" />
				-->
				<input type="submit" value="Sign In" class="button" />
					<!--<input type="submit" class="button"  value="Sign In">
				--></div>
				</form:form>
				<div class="hr"></div>
				<div class="foot-lnk">
				 <a href="javascript:callfun();" class="need-help"> <b>Forgot Password</b> </a>
					<!--<a href="#forgot">Forgot Password?</a>
				--></div>
			</div>
		
	</div>
</div>

</div>
</div>



</div>

<%-- <div class="login-wrap">
	<div class="login-html">
	<!--<input id="tab-1" type="radio" name="tab" class="sign-in" checked><label for="tab-1" class="tab">Login</label>
		<input id="tab-2" type="radio" name="tab" class="sign-up"><label for="tab-2" class="tab">Sign Up</label>
		-->
		<div class="login-form">
			<div class="sign-in-htm">
				<img src="images/CGTMSE_logo1.png" style="margin: top; height:100px; width:200px;">
			<form:form method="POST" action="nbfcLoginSubmitForm.html" id="A">
			<span Class="error">${message}</span>
				<div class="group">
					<label for="user" class="label">Username</label>
					<form:input path="usr_id" placeholder="User ID" class="input" />
					<form:errors path="usr_id" Class="error"/>
					
					<!--<input id="user" type="text" class="input">--></div>
				<div class="group">
					<label for="pass" class="label">Password</label>
					
					<form:input path="usr_password" type="password" placeholder="Password" class="input" />
					<form:errors path="usr_password" Class="error"/>
					<span Class="error">${InvalidCredencialKey}</span>
						
					
					<!--<input id="pass" type="password" class="input" data-type="password">
				-->
				</div>
				<div class="group">
					<input id="check" type="checkbox" class="check" checked>
					<label for="check"><span class="icon"></span> Keep me Signed in</label>
				</div>
				<div class="group">
				<!--<input type="submit" value="Sign In" class="login login-submit" />
				-->
				<input type="submit" value="Sign In" class="button" />
					<!--<input type="submit" class="button"  value="Sign In">
				--></div>
				</form:form>
				<div class="hr"></div>
				<div class="foot-lnk">
				 <a href="javascript:callfun();" class="need-help"> <b>Forgot Password</b> </a>
					<!--<a href="#forgot">Forgot Password?</a>
				--></div>
			</div>
		
	</div>
</div>
</div> --%>


</body>

<!--<script src="js/jquery-1.10.2.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
--><script type="text/javascript">
function callfun(){

document.getElementById('A').action = "/Aasha/ForgotPassword.html.html";
document.getElementById('A').submit();
}
</script>
</html>