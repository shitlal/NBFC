<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Claim Report Form</title>

<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-3.2.1.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.11.1.js"></script>
<LINK href="<%=request.getContextPath()%>/css/stylesheet.css"
	rel="stylesheet" type="text/css">

<link href="css/jquery-ui-css.css" rel="stylesheet" type="text/css">
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<%
	String s = (String) request.getAttribute("SName");
%>
<script language="javascript">
	function submitClaimReportForm() {
		debugger;
		document.getElementById('A').action = "/Aasha/ClaimDetailsReportData.html";
		document.deliveryForm.submit();
	}
	//Date Picker UI
	$(function() {
		$("#mliDateOfSanctionOfExposure").datepicker({
			dateFormat : 'dd/mm/yy'
		});
		$("#toDate").datepicker({
			dateFormat : 'dd/mm/yy'
		});
		$("#fromDate").datepicker({
			dateFormat : 'dd/mm/yy'
		});

	});
</script>
</head>
<body>
	<div class="main-section">
		 <nav aria-label="breadcrumb">
   <ol class="breadcrumb cus-breadcrumb">
    <li class="breadcrumb-item"><a href="/Aasha/ClaimStatusWiseDetailsReport.html">Claim Status Wise Report</a></li>  
  </ol> 
</nav>
		<div class="container-fluid">
			<div>
				<div class="frm-section">
					<div class="col-md-12">
						<!-- 	<h1>MLI User Registration Form</h1> -->
						<h5 class="notification-message">${message}</h5>
						<h5 class="error1">${Errormessage}</h5>
						<div class="loader"></div>
						<form:form method="POST" action="" id="A" class="form-horizontal">
							<%
								String userRole = (String) session.getAttribute("uRole");
							%>
							<div class="col-md-2 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">
									 
									<div class="col-md-12 prl-10"> 
									<label>From Date : <span style="color: red">*</span></label>
										<form:input path="toDate" value="" size="28" id="toDate"
											class="form-control cus-control" style="text-align: left"
											placeholder="eg.dd/mm/yyyy" autocomplete="off" />
										<form:errors path="toDate" cssClass="error" />
										<div id="requiredMliValidityOfExposureLimitStartDate"
											Class="displayErrorMessageInRedColor"></div>
										<div id="startDateShouldBeGreaterThanSanctionDate"
											Class="displayErrorMessageInRedColor"></div>
									 </div> 
								</div>
							</div>
							<div class="col-md-2 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">
									 
									<div class="col-md-12 prl-10"> 
										<label>To Date : <span style="color: red">*</span></label>
										<form:input path="fromDate" type="text" size="28" id="fromDate"
											class="form-control cus-control" placeholder="eg.dd/mm/yyyy"
											autocomplete="off" />
										<form:errors path="fromDate" cssClass="error" />
										<div id="requiredMliValidityOfExposureLimitEndDate"
											Class="displayErrorMessageInRedColor"></div>

									</div> 
								</div>
							</div>

							<%
								if (userRole.equals("NMAKER") || (userRole.equals("NCHECKER"))) {
							%>
							<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">
									<!-- <label class="d-block text-purple" style="visibility: hidden";>
										5</label> -->
									<div class="col-md-12 prl-10">
										<label>Member Id : <span style="color: red">*</span></label>
										<form:input path="memberId" value="${memberId}" size="28" id="memberId"
											readonly="true" class="form-control cus-control"
											placeholder="eg.memberId" />
										<form:errors path="memberId" cssClass="error" />
										<div id="requiredGuranteeCoverage"
											Class="displayErrorMessageInRedColor"></div>
									</div>
								</div>

							</div>

							<%
								}
							%>
							<%
								if (userRole.equals("CMAKER") || userRole.equals("CCHECKER")) {
							%>
                             <div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">
									<label class="d-block text-purple" style="visibility: hidden";>
										5</label>
								<div class="col-md-12 prl-10">
											<label>MLI Long Name : <span style="color:red">*</span></label>								
											  	<input type="text" class="form-control cus-control d-none" placeholder="MLI Long Name" >
											 	<form:select path="mliLongName" id="mliLongName" onchange="getMliShortName()" class="form-control cus-control">
												<form:option value="NONE" label="---Select---"/>
												<form:option value="All" label="All"/>	
												<form:options items="${mliLongName}" />	
											</form:select>
											<div id="requiredMliLongName" Class="displayErrorMessageInRedColor"></div>
										</div>
								</div>

							</div>



							<%
								}
							%>
	<div class="clearfix"></div>
							<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<!--<label>Internal and/or external enquiry has been concluded<span style="color: red">*</span>:</label>
											--><label class="d-block"> 
											 <form:label path = "claimStatus">Select Status : </form:label>
							              </label>
							                  <form:radiobutton path = "claimStatus" value = "NE" label = "NEW(NE)" />
							                  <form:radiobutton path = "claimStatus" value = "RT" label = "RETURN(RT)" />
							                  <form:radiobutton path = "claimStatus" value = "RU" label = "RETURN UPDATED(RU)" />
							                  <form:radiobutton path = "claimStatus" value = "AP" label = "CLAIM SETTLED(CS)" />
							                  <form:radiobutton path = "claimStatus" value = "AS" label = "ALL STATUS" />
							              
											
										</div>
									</div>
									</div>
						
							<div class="col-md-2 col-sm-4 col-xs-12 mt-20">
								<div class="form-group">

									<div class="d-inlineblock">
										<input type="submit" value="Submit" class="btn btn-reset"
											class="btn btn-reset" onclick="submitClaimReportForm()" />
									</div>
								</div>
							</div>
							<div></div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="js/jquery-1.10.2.js" type="text/javascript"></script>
	<div class="loader"></div>
	<script type="text/javascript">
		$(window).load(function() {
			$(".loader").fadeOut("slow");
		});
	</script>
</body>
</html>