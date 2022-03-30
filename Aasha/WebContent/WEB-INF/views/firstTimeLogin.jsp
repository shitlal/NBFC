<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>Confirm Password Page</title>
<link href="css/custom.css" rel="stylesheet" type="text/css" media="all">
<link href="css/style.css" rel="stylesheet" type="text/css" media="all">
<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="<%=request.getContextPath()%>/css/stylesheet.css" rel="stylesheet" type="text/css">


<style type="text/css">
body{	/* background:url(images/bg-lgoin1.jpg) no-repeat center !important; */
background-image:url(images/bg8.jpg) !important;	    background-position-y: top;
background-size:cover !important; 		background-repeat:no-repeat;	background-attachment:fixed;
z-index:10;		}
.login-container{	width:900px;     margin: 50px auto 0;}
.login-wrap,.login-wrap1{	 float:left;		  margin: 0 !important;	box-shadow:none !important;}
.login-wrap{	width:475px !important;	 max-width: 500px;	}
.login-wrap1{	background-color:white;  width:425px !important; display:block;	}

/* .login-wrap{    min-height: 550px;} */

/* .login-form .group .button{	    font-size: 14px;	} */
.login-html{background: rgba(247, 247, 247, 0.82);}
.sign-in-htm h5{margin: 20px 0;
    text-align: center;
    font-size: 18px;
    color: red;
}

.sign-in-htm h1{    
margin: 20px 0 30px;
    text-align: center;    
        font-size: 22px;
    color: #134f98;    
}

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
<body>	

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
				<form:form method="POST" action="changePassword.html" id="A">				
			<h1 align="center">Reset Password</h1>
			<span Class="error">${message}</span>
			
			
			<div class="group">
					<label for="usr_id" class="label">User ID</label>					
					<form:input path="usr_id" value="" placeholder="usr_id" class="input" readonly="true"/>
					<form:errors path="usr_id" Class="error"/>										
			</div>
			
			<div class="group">
					<label for="new password" class="label">New Password</label>					
					<form:input path="newPassword" value="" placeholder="******" class="input" type="password"/>
						<form:errors path="newPassword" Class="error"/>
			</div>
			<div class="group">
					<label for="reEnter password" class="label">ReEnter Password</label>					
					<form:input path="reEnterPassword" value="" placeholder="******"  class="input"  type="password"/>
					<form:errors path="reEnterPassword" Class="error"/>										
			</div>
			<div class="group">
				 <input type="submit" id="but2" value="Submit" class="button" />
			</div>						
				
				</form:form>
			</div>
		
	</div>
</div>

</div>
</div>

</div>



<%-- 	<div class="main">
				<div class="login-card">				
					
					<div class="banner">
				    	<h1 align="center"><font face="Arial">NEW Password</font></h1>
			        </div>
			
					<br>
					<span Class="error">${message}</span>
					<br>
					<form:form method="POST" action="ResetPassword.html" id="A">
                       
                        <div></div>
                     
						<form:input path="newPassword" value="" placeholder="New_Password" maxlength="8" type="password"/>
						<form:errors path="newPassword" Class="error"/>
					   
					
				    	<form:input path="confirm_password" value="" placeholder="Confirm_Password" maxlength="8" type="password"/>
						<form:errors path="confirm_password" Class="error"/>
			          
			       
					   <input type="submit" id="but2" value="Submit" class="login login-submit" />
						
					</form:form>
					
				</div>				
			</div> --%>

<script type="text/javascript">

function show(){
	
	document.getElementById("but1").style.visibility = "hidden";
	document.getElementById("but2").style.visibility = "visible";
	
}
</script>
</body>
</html>