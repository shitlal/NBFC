<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MIS Report Form</title>

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

	function submitNPAForm() {
		debugger;
		var Guarantee_CheckBox = document
				.getElementById("guaranteeeDataReport").checked;
		var claimDataReport = document.getElementById("claimDataReport").checked;
		var otherDataReport = document.getElementById("otherDataReport").checked;

		if (Guarantee_CheckBox == true && claimDataReport == false
				&& otherDataReport == false) {
			document.getElementById('A').action = "/Aasha/MISReportInputForm.html";
			document.getElementById('A').submit();
		} else if (Guarantee_CheckBox == false && claimDataReport == true
				&& otherDataReport == false) {
			document.getElementById('A').action = "/Aasha/ClaimreportInputForm.html";
			document.getElementById('A').submit();

		} else if (Guarantee_CheckBox == false && claimDataReport == false
				&& otherDataReport == true) {
			document.getElementById('A').action = "/Aasha/OtherMISInputForm.html";
			document.getElementById('A').submit();
		}else if(Guarantee_CheckBox == true && claimDataReport == true
				&& otherDataReport == true){
			alert("kindly select atmost one checkbox")
			document.getElementById('A').action = "/Aasha/MISReportInputForm.html";
			
		} else {
			alert("kindly select atleast one checkbox")
			document.getElementById('A').action = "/Aasha/MISReportInputForm.html";
		}
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
							<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">
									<label class="d-block text-purple" style="visibility: hidden;">
										1</label>
									<div class="col-md-6 prl-10">
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
							<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">
									<label class="d-block text-purple" style="visibility: hidden;">
										4</label>
									<div class="col-md-6 prl-10">
										<label>To Date : <span style="color: red">*</span></label>
										<form:input path="fromDate" type="" size="28" id="fromDate"
											class="form-control cus-control" placeholder="eg.dd/mm/yyyy"
											autocomplete="off" />
										<form:errors path="fromDate" cssClass="error" />
										<div id="requiredMliValidityOfExposureLimitEndDate"
											Class="displayErrorMessageInRedColor"></div>

									</div>
								</div>
							</div>

							<div class="col-md-3 col-sm-6 col-xs-12 mar-">
									<div style="margin-top: 42px;margin-left: -100px;">									
									<label><input type="checkbox" class="GuaranteeeDataReport" id="guaranteeeDataReport">Guarantee Report
									&nbsp;&nbsp;<input type="checkbox" class="ClaimDataReport" id="claimDataReport">Claim Report
									&nbsp;&nbsp;<input type="checkbox"  class="OtherReport" id="otherDataReport">Outstanding/Live Report</label>
								
							        </div>
							</div>
							
			</form:form>
			<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">
									<div class="d-inlineblock mt-35">
										<a href="#" class="btn btn-reset"
											 onclick="submitNPAForm();">
											
											 <img src="images/excel.png"  style="width:20px,height:20px;" />
											 </a>
									</div>
								</div>
							</div>
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