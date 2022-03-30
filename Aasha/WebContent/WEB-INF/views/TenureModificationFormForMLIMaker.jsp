<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@	taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
<title>Claim Lodgment</title>
<link href="css/jquery-ui-css.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<LINK href="<%=request.getContextPath()%>/css/stylesheet.css"
	rel="stylesheet" type="text/css">
<title>Update Recovery Details</title>

</head>
<script type="text/javascript">
	function UpdateTenure() {
	
		var chkflag = 5;
		var remark = document.getElementById('modificationRemark').value;
		var Rtenure = document.getElementById('reviseTenure').value;
		var expDate = document.getElementById('reviseExpirydate').value;
	//	var accStandard = document.getElementById('accStandard').value;
		//var bankAuthority = document.getElementById('bankAuthority').value;
		var Tenure_id = document.getElementById('tId').value;


		if (remark == null || remark == "Select") {
		
			document.getElementById("requiredremk").innerHTML = "Select Modification Remark";
			chkflag = chkflag - 1;
		} else {
			document.getElementById("requiredremk").innerHTML = "";

		}
		if (Rtenure = null || Rtenure == "") {
		
			document.getElementById("requiredtenure").innerHTML = "Enter Revised Tenure";
			chkflag = chkflag - 1;
		} else {
			
			document.getElementById("requiredtenure").innerHTML = "";
		
		}
		if (expDate = null || expDate == "") {
			
			document.getElementById("ReqreviseExpirydate").innerHTML = "Revised Expiry date is blank.kindly enter valid Revised Tenure";
			chkflag = chkflag - 1;
		} else {
			
			document.getElementById("ReqreviseExpirydate").innerHTML = "";
		
		}
		
	if (!document.getElementById('accStandard').checked == true) {
	
			document.getElementById("ReqreviseaccStandard").innerHTML = "Kindly Confirm.";
			chkflag = chkflag - 1;
		} else {
		
			document.getElementById("ReqreviseaccStandard").innerHTML = "";
		
		}
	

	if (!document.getElementById('bankAuthority').checked == true) {
	
		document.getElementById("ReqrevisedBankAuthority").innerHTML = "Kindly certify the declaration.";
		chkflag = chkflag - 1;
	} else {
		
		document.getElementById("ReqrevisedBankAuthority").innerHTML = "";
	
	}
if (!document.getElementById('noIntrest').checked == true) {
		
		document.getElementById("ReqrevisednoIntrest").innerHTML = "Kindly Confirm.";
		chkflag = chkflag - 1;
	} else {
		
		document.getElementById("ReqrevisednoIntrest").innerHTML = "";
	
	}
		if (chkflag == 5) {
			
			document.getElementById('A').action = "/Aasha/editTenureDataReturnByNBFCMaker.html?TenId="
				+ Tenure_id;
		document.getElementById('A').submit();
			//document.getElementById('A').submit();
		} else {
			
			return false;
		}
	}
	
function calExpiryDt(id) {

		var subsidyAmount = 0;
		var anySubsidyInvolvedVal1 = "";
		var tenure = document.getElementById('tenure').value;
		
		var Rtenure = document.getElementById('reviseTenure').value;
	
		
	//	var FTenure =  parseInt(Rtenure) + parseInt(tenure);

	
if(parseInt(Rtenure)<= 120 && parseInt(tenure)< parseInt(Rtenure)){
	
		
		

		var date = document.getElementById('firstDisburseDate').value;
		date = date.split("/").reverse().join("-");
		result = new Date(date);

		var tenure = document.getElementById('reviseTenure').value;

		result.setMonth(result.getMonth() + parseInt(tenure));
		//----------------------------------------------------------

		var d = new Date(result);

		result = [ (d.getDate()), (d.getMonth() + 1), d.getFullYear() ]
				.join('/');

		document.getElementById('reviseExpirydate').value = result;
		document.getElementById('requiredtenure').innerHTML ="";
}else{
	document.getElementById('reviseExpirydate').value = "";
	document.getElementById('requiredtenure').innerHTML = "Maximum tenure of loan to be 120 months and should be greater than Original Tenure";
	
}
	}
		
</script>
<body onload="clearField()">
<div class="main-section">
<div class="frm-section">
<div class="col-md-12"><form:form method="POST" 
	class="form-horizontal" id="A">
	<h5 class="error1 mtb-0"><strong>${message}</strong></h5>
	<div class="main-section"><nav aria-label="breadcrumb">
	
	<ol class="breadcrumb cus-breadcrumb">
		<li class="breadcrumb-item"><a
			href="/Aasha/TenureModification.html">Tenure Modification</a></li>
		<li class="breadcrumb-item active" aria-current="page">Modify Tenure Details
		</li>
	</ol>
	</nav></div>
	<div class="panel-group" id="accordion" role="tablist"
		aria-multiselectable="true">
	<div class="panel panel-default cus-pnl cus-pnl">
	<div class="panel-heading cus-pheading cus-pheading" role="tab"
		id="headingOne">
	<h4 class="panel-title cus-ptitle cus-ptitle"><a role="button"
		data-toggle="collapse" data-parent="#accordion" href="#collapseOne"
		aria-expanded="true" aria-controls="collapseOne"> Tenure
	Modification Details: <i class="fa fa-chevron-circle-down"
		aria-hidden="true"></i> <!-- <span><img src="images/plus.png"  style="width:18px;" class="more-less plus" alt=""></span>
        <span><img src="images/negative.png"  style="width:18px;" class="more-less minus" alt=""></span> -->
	</a></h4>
	</div>
	<!-- panel-head -->
	<div id="collapseOne" class="panel-collapse collapse in"
		role="tabpanel" aria-labelledby="headingOne">
		<input type="hidden" path ="tId" id ="tId" value="${formData.tId}">
	<!--<form:input path="tId" id="tId" value="${formData.tId}"/>
	--><div class="panel-body">
	<div class="col-md-2 col-sm-4 col-xs-12">
	<div class="form-group">
	<div class="col-md-12 prl-10"><form:label path="bankName">Name Of NBFC<span
			style="color: red;">*</span>
	</form:label> <form:input path="bankName" class="form-control cus-control"
		id="bankName" value="${formData.bankName}" disabled="true" /></div>
	</div>
	</div>
	<div class="col-md-2 col-sm-4 col-xs-12">
	<div class="form-group">
	<div class="col-md-12 prl-10"><form:label path="mliID">MLI ID <span
			style="color: red;">*</span>
	</form:label> <form:input path="mliID" id="mliID" value="${formData.mliID}"
		class="form-control cus-control" disabled="true" /></div>
	</div>
	</div>
<div class="col-md-2 col-sm-4 col-xs-12">
	<div class="form-group">
	<div class="col-md-12 prl-10"><form:label path="cgpan">CGPAN <span
			style="color: red;">*</span>
	</form:label> <form:input path="cgpan" id="cgpan" value="${formData.cgpan}"
		class="form-control cus-control" disabled="true" /></div>
	</div>
	</div>

	
	
	
	<div class="clearfix"></div>
	<div class="col-md-2 col-sm-4 col-xs-12">
	<div class="form-group">
	<div class="col-md-12 prl-10"><form:label path="mseName">MSE Name <span
			style="color: red;">*</span>
	</form:label> <form:input path="mseName" class="form-control cus-control"
		id="mseName" value="${formData.mseName}" disabled="true" /></div>
	</div>
	</div>
	<div class="col-md-2 col-sm-4 col-xs-12">
	<div class="form-group">
	<div class="col-md-12 prl-10"><form:label path="state">MLI State<span
			style="color: red;">*</span>
	</form:label> <form:input path="state" class="form-control cus-control" id="state"
		value="${formData.state}" disabled="true" /></div>
	</div>
	</div>

	
	<div class="col-md-2 col-sm-4 col-xs-12">
	<div class="form-group">
	<div class="col-md-12 prl-10"><form:label path="gstNo">GSTIN No<span
			style="color: red;">*</span>
	</form:label> <form:input path="gstNo" class="form-control cus-control" id="state"
		value="${formData.gstNo}" disabled="true" /></div>
	</div>
	</div>
	<div class="clearfix"></div>
	
	<div class="clearfix"></div>
		<div class="col-md-2 col-sm-4 col-xs-12">
	<div class="form-group">
	<div class="col-md-12 prl-10"><form:label path="tenure">First Disbursement Date<span
			style="color: red;">*</span>
	</form:label> <form:input path="firstDisburseDate" class="form-control cus-control" id="firstDisburseDate"
		value="${formData.firstDisburseDate}" disabled="true" /></div>
	</div>
	</div>
	<div class="col-md-2 col-sm-4 col-xs-12">
	<div class="form-group">
	<div class="col-md-12 prl-10"><form:label path="tenure">Tenure[In months]<span
			style="color: red;">*</span>
	</form:label> <form:input path="tenure" class="form-control cus-control" id="tenure"
		value="${formData.tenure}" disabled="true" /></div>
	</div>
	</div>
	<div class="col-md-2 col-sm-4 col-xs-12">
	<div class="form-group">
	<div class="col-md-12 prl-10"><form:label path="expiryDate">Expiry Date <span
			style="color: red;">*</span>
	</form:label> <form:input path="expiryDate" id="expirydate"
		value="${formData.expiryDate}" class="form-control cus-control"
		disabled="true" /></div>
	</div>
	</div>
	<div class="clearfix"></div>
	
	<div class="col-md-2 col-sm-4 col-xs-12">
	<div class="form-group">
	<div class="col-md-12 prl-10"><form:label path="reviseTenure">Revised TenureIn months]<span
			style="color: red;">*</span>
	</form:label><form:input path="reviseTenure" value="${formData.reviseTenure}"
		class="form-control cus-control" onchange="calExpiryDt(this.value);" id="reviseTenure" />
	<div id="requiredtenure" Class="displayErrorMessageInRedColor"></div>

	</div>
	</div>
	</div>
	<div class="col-md-2 col-sm-4 col-xs-12">
	<div class="form-group">
	<div class="col-md-12 prl-10"><form:label path="reviseExpirydate">Revised Expiry Date <span
			style="color: red;">*</span>
	</form:label> <form:input path="reviseExpirydate" id="reviseExpirydate" value="${formData.reviseExpirydate}"
		class="form-control cus-control" maxlength="10" readonly="true"/> 
		<div id="ReqreviseExpirydate" Class="displayErrorMessageInRedColor"></div>
	
	</div>
	</div>
	</div>
	

	<div class="col-md-2 col-sm-4 col-xs-12">
	<div class="form-group">
	<div class="col-md-12 prl-10"><form:label
		path="modificationRemark">Modification Remarks <span
			style="color: red;">*</span>
	</form:label> <form:select path="modificationRemark" class="form-control cus-control" id="modificationRemark">
		<form:option value= "${formData.modificationRemark}"  id="${formData.modificationRemark}"/>  
		<form:option value="Reshedulement/Restructuring of account"
			label="Reshedulement/Restructuring of account" /><!--
		<form:option value="Slow down in Business economy" label="Slow down in Business economy" />
		<form:option value="High Competition" label="High Competition" />
		<form:option value="Death of Borrower" label="Death of Borrower" />
		<form:option value="Natural Calamity" label="Natural Calamity" />
		<form:option value="Typing Error" label="Typing Error" />

	--></form:select>
	<div id="requiredremk" Class="displayErrorMessageInRedColor"></div>
	</div>
	</div>
	</div>
	<div class="clearfix"></div>
	<div class="col-md-2 col-sm-4 col-xs-12 mar-top21">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<label>The account is standard as per RBI guidelines:<span style="color: red">*</span>:</label> 
											<label class="d-block"> 
												<c:choose>
											 		<c:when test="${formData.accStandard=='Y'}">
											 			<input type="checkbox" name="accStandard" id="accStandard" value="Y" checked> 
											 			
											 		</c:when>
											 		<c:when test="${formData.accStandard=='N'}">
											 			<input type="checkbox" name="accStandard" id="accStandard" value="Y" > 
											 		</c:when>
											 	
											 		<c:otherwise>
											 		<input type="checkbox" name="accStandard" id="accStandard" value="Y"> 
											 		</c:otherwise>
												 </c:choose>
												 <div id="ReqreviseaccStandard" Class="displayErrorMessageInRedColor"></div>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2 col-sm-4 col-xs-12 mar-top21">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<label>I duly certify and declare that the changes made are as per the record available with the NBFC :<span style="color: red">*</span>:</label> 
											<label class="d-block"> 
												<c:choose>
											 		<c:when test="${formData.bankAuthority=='Y'}">
											 			<input type="checkbox" name="bankAuthority"  id="bankAuthority" value="Y" checked> 
											 			
											 			
											 		</c:when>
											 		<c:when test="${formData.bankAuthority=='N'}">
											 		<input type="checkbox" name="bankAuthority" id="bankAuthority" value="Y"> 
											 		</c:when>
											 	
											 		<c:otherwise>
											 		<input type="checkbox" name="bankAuthority" id="bankAuthority" value="Y" > 
											 		</c:otherwise>
												 </c:choose>
												 <div id="ReqrevisedBankAuthority" Class="displayErrorMessageInRedColor"></div>
											</label>
										</div>
									</div>
								</div>
<div class="col-md-2 col-sm-4 col-xs-12 mar-top21">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<label>No interest/overdue funding or capitalization carried out in the above account. :<span style="color: red">*</span>:</label> 
											<label class="d-block"> 
												<c:choose>
											 		<c:when test="${formData.noIntrest=='Y'}">
											 			<input type="checkbox" name="noIntrest"  id="noIntrest" value="Y" checked > 
											 			
											 		</c:when>
											 		<c:when test="${formData.noIntrest=='N'}">
											 		<input type="checkbox" name="noIntrest" id="noIntrest" value="Y" > 
											 		</c:when>
											 	
											 		<c:otherwise>
											 		<input type="checkbox" name="noIntrest" id="noIntrest" value="Y" > 
											 		</c:otherwise>
												 </c:choose>
												 <div id="ReqrevisednoIntrest" Class="displayErrorMessageInRedColor"></div>
											</label>
										</div>
									</div>
								</div>
	<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
	<div class="form-group">
	<div class="col-md-12 prl-10"><input type="button" value="Update"
		class="btn btn-reset mt-15" onclick="UpdateTenure();" /></div>
	</div>
	</div>
	</div>
	</div>
	</div>
	<!-- panel --></div>
	<!-- panel-group -->

</form:form></div>

</div>
</div>



</body>
</html>
