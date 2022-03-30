<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css?family=Open+Sans|Roboto"
	rel="stylesheet">


<link href="css/customstyle.css" type="text/css" rel="stylesheet">
<link href="css/font-awesome.min.css" type="text/css" rel="stylesheet">
 
<style>
.header p {
	padding: 0;
	margin: 0;
	display: inline;
	color: white;
}

.header span {
	 
	color: white;
}
</style>
</head>
<body>
<%
	String uesrName = (String) session.getAttribute("uName");
	String mliName = (String) session.getAttribute("mliName");
	String userRole = (String) session.getAttribute("uRole");
%>

<div
	style="position: fixed; top: 0; display: block; width: 100%; margin-bottom: 100px; z-index: 9 !important;">
<div class="header">

<p><strong>Welcome </strong> <%=mliName%></p>

<div class="personinfo"  >
<span><strong>User Name :</strong> <%=uesrName%></span> 
 <div class="dropdown1">
  <button class="dropbtn"><img src="images/student.png" alt="" style="width:24px;"></button>
  <div class="dropdown-content">
    <ul id="menu-list1" class="dropdown-menu dropdown-user">
		<li><strong>NBFC Name :</strong> <%=mliName%></li>
		<hr class="line">
		<li><strong>User Name :</strong> <%=uesrName%></li>
		<hr class="line">
		<li><strong>User Role: </strong> <br><%=userRole%></li>
	</ul>
  </div>
</div>
<%-- <ul>
<li class="dropdown"><a href="#" class="dropdown-toggle"
		 data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false"> <img src="images/student.png" alt="" style="width:24px;">
	</a> <!--  <span class="caret"></span>-->
	<ul id="menu-list1" class="dropdown-menu dropdown-user">
		<li><strong>NBFC Name :</strong> <%=mliName%></li>
		<hr class="line">
		<li><strong>User Name :</strong> <%=uesrName%></li>
		<hr class="line">
		<li><strong>User Role: </strong> <br><%=userRole%></li>
	</ul>
	</li>
</ul>	 --%>
	</div>
	
<c:forEach
	var="actName" items="${actName}">
	
	<h5 style="display: inline;"><strong>${actName.act_value}</strong>

	</h5>

</c:forEach></div>
<!-- Static navbar -->
<div class="navi"><nav class="navbar navbar-default custom-navbar">
<div class="container-fluid">
<div class="navbar-header">
<button type="button" class="navbar-toggle"
	data-toggle="collapse" data-target=".navbar-collapse" aria-expanded="false"
	aria-controls="navbar"><span class="sr-only">Toggle
navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span>
<span class="icon-bar"></span></button>
<!-- <a class="navbar-brand logo" href="#"> <img src="images/udaan.png"
	style="display: inline;" class="img-responsive" alt=""> <h4 style="display:inline;">User Role Creation Page</h4>
</a> --></div>
<div id="navbar" class="collapse navbar-collapse custom-navi shift">
<ul class="nav navbar-nav custom-nav">
	<li><a href="${homePage}.html" class="">Home</a></li>


	<!--For NBFCMaker & NBFCCHECKER  Menu  -->

	<%
		if (userRole.equals("NMAKER") || (userRole.equals("NCHECKER"))) {
	%>
	<li class="dropdown"><a href="#" class="dropdown-toggle"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false">Application Processing<span class="caret"></span>
	</a>
	<ul id="menu-list" class="dropdown-menu">
		<c:forEach var="applicationList" items="${applicationList}">
			<li><a href="/Aasha/${applicationList.act_name}.html">${applicationList.act_value}</a></li>
		</c:forEach>
	</ul>
	</li>


	<li class="dropdown"><a href="#" class="dropdown-toggle"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false">Receipt & Payments <span class="caret"></span>
	</a>
	<ul id="menu-list" class="dropdown-menu">
		<c:forEach var="RPaymentList" items="${RPaymentList}">
			<li><a href="/Aasha/${RPaymentList.act_name}.html">${RPaymentList.act_value}</a></li>
		</c:forEach>
	</ul>
	</li>
	<li class="dropdown"><a href="#" class="dropdown-toggle"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false">Guarantee Maintainence<span class="caret"></span></a>
	<ul id="menu-list" class="dropdown-menu">
		<c:forEach var="GMaintainlist" items="${GMaintainlist}">
			<li><a href="/Aasha/${GMaintainlist.act_name}.html">${GMaintainlist.act_value}</a></li>
		</c:forEach>
	</ul>
	</li>
	<li class="dropdown dropdown-large"><a href="#" class="dropdown-toggle"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false">Claim Processing<span class="caret"></span></a>
	<ul id="menu-list" class="dropdown-menu dropdown-menu-large row ">
		<c:forEach var="CList" items="${CList}">
			<li><a href="/Aasha/${CList.act_name}.html">${CList.act_value}</a></li>
		</c:forEach>
	</ul>
	</li>

	<li class="dropdown"><a href="#" class="dropdown-toggle"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false">Sys Admin Audit <span class="caret"></span></a>
	<ul id="menu-list" class="dropdown-menu">
		<c:forEach var="CBMFList" items="${CBMFList}">
			<li><a href="/Aasha/${CBMFList.act_name}.html">${CBMFList.act_value}</a></li>
		</c:forEach>
	</ul>
	</li>
	<li class="dropdown dropdown-large"><a href="#" class="dropdown-toggle"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false">Report & MIS <span class="caret"></span></a>
	<ul id="menu-list" class="dropdown-menu dropdown-menu-large row ">
		<c:forEach var="repList" items="${repList}">
			<li class="col-sm-6"><a href="/Aasha/${repList.act_name}.html">${repList.act_value}</a></li>
		</c:forEach>
	</ul>
	</li>

	<%
		}
	%>
	<!--For CMaker & CCHECKER  Menu  -->
	<%
		if (userRole.equals("CMAKER") || userRole.equals("CCHECKER")) {
	%>

	<li class="dropdown"><a href="#" class="dropdown-toggle"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false">Registration<span class="caret"></span> </a>
	<ul id="menu-list" class="dropdown-menu">
		<c:forEach var="guaranteelist" items="${guaranteelist}">
			<li><a href="/Aasha/${guaranteelist.act_name}.html">${guaranteelist.act_value}</a></li>
		</c:forEach>
	</ul>
	</li>

	<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false">Application Processing<span class="caret"></span>
	</a>
	<ul id="menu-list" class="dropdown-menu">
		<c:forEach var="applicationList" items="${applicationList}">
			<li><a href="/Aasha/${applicationList.act_name}.html">${applicationList.act_value}</a></li>
		</c:forEach>
	</ul>
	</li>

	<li class="dropdown"><a href="#" class="dropdown-toggle"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false">Receipt & Payments <span class="caret"></span>
	</a>
	<ul id="menu-list" class="dropdown-menu">
		<c:forEach var="RPaymentList" items="${RPaymentList}">
			<li><a href="/Aasha/${RPaymentList.act_name}.html">${RPaymentList.act_value}</a></li>
		</c:forEach>
	</ul>
	</li>

	<li class="dropdown dropdown-large"><a href="#" class="dropdown-toggle"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false">Claim Processing <span class="caret"></span></a>
	<ul id="menu-list" class="dropdown-menu dropdown-menu-large row">
		<c:forEach var="CList" items="${CList}">
			<li><a href="/Aasha/${CList.act_name}.html">${CList.act_value}</a></li>
		</c:forEach>
	</ul>
	</li>
	<%
		if (userRole.equals("CCHECKER")) {
	%>
	<li class="dropdown"><a href="#" class="dropdown-toggle"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false">Sys Admin Audit <span class="caret"></span></a>
	<ul id="menu-list" class="dropdown-menu">
		<c:forEach var="CBMFList" items="${CBMFList}">
			<li class="col-sm-12"><a href="/Aasha/${CBMFList.act_name}.html">${CBMFList.act_value}</a></li>
		</c:forEach>
	</ul>
	</li>
	<%
		}
	%>

	<li class="dropdown dropdown-large"><a href="#" class="dropdown-toggle"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false">Report & MIS <span class="caret"></span></a>
	<ul id="menu-list" class="dropdown-menu dropdown-menu-large row ">
		<c:forEach var="repList" items="${repList}">
			<li class="col-sm-6"><a href="/Aasha/${repList.act_name}.html">${repList.act_value}</a></li>
		</c:forEach>
	</ul>
	</li>


	<%
		}
	%>

	<!-- 	<li><a href="#">Password Change</a></li> -->
	<li class="logout"><a href="logout.html">Log Out <i
		class="fa fa-sign-out" aria-hidden="true"></i></a></li>

	
</ul>

<!--<DIV>
		<strong>Welcome </strong> <%=mliName%>	
		
	</DIV>
					--></div>
<!--/.nav-collapse --></div>
<!--/.container-fluid --> </nav>
</div>


</div>
	



<script src="js/jquery-1.10.2.js" type="text/javascript"></script> 



<script type="text/javascript">
$(document).ready(function(){
	  $("#btn-asf").click(function(){
	    $("#asf").show();
	  });
	  $("#btn-mliexposure").click(function(){
		    $("#mliexposure").show();
		  });
		});
	/* 
	 $(window).scroll(function() {
	 var sticky = $('.navi'),
	 scroll = $(window).scrollTop();
	
	 if (scroll >= 5) { 
	 sticky.addClass('fixed'); }
	 else { 
	 sticky.removeClass('fixed');

	 }
	 });
	 */
	$(function() {
		$(".personinfo .dropdown").hover(
				function() {
					$('#menu-list1.dropdown-menu', this).stop(true, true)
							.fadeIn("fast");
					$(this).toggleClass('open');
				},
				function() {
					$('#menu-list1.dropdown-menu', this).stop(true, true)
							.fadeOut("fast");
					$(this).toggleClass('open');
				}); 
		$("#navbar .dropdown").hover(
				function() {
					$('#menu-list.dropdown-menu', this).stop(true, true)
							.fadeIn("fast");
					$(this).toggleClass('open');
				},
				function() {
					$('#menu-list.dropdown-menu', this).stop(true, true)
							.fadeOut("fast");
					$(this).toggleClass('open');
				});
	});


</script>
</body>
</html>
