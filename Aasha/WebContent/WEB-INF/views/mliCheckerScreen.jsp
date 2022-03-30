<%@page import="java.io.Console"%>
<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Quarterly File Upload Approval-MLI Checker</title>
<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-3.2.1.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.11.1.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<link href="<%=request.getContextPath()%>/css/stylesheet.css" rel="stylesheet" type="text/css">

<%
	String s = (String) request.getAttribute("SName");
%>

</head>
<body onload="linkFunction()" bgcolor="#E0FFFF">
<div class="main-section">
<div class="container-fluid">
<!-- <div class="row"> -->
	<div>	
		<div class="frm-section">
		<div class="col-md-12">
			<h5 class="notification-message"><strong>${errormessage}</strong></h5>
		<h5 class="notification-message"><strong>${message}</strong></h5>
			<form:form method="POST" id="A" action="/nbfc/mliCheckerUpdateFirst.html" class="form-horizontal">			
			<div class="col-md-3 col-sm-4 col-xs-12">
			  <div class="form-group">
			  <div class="col-md-12 prl-10">
			   <%-- <form:label path="PORTFOLIO_START_DATE">Portfolio Start Date(dd/mm/yyyy):</form:label> --%>
			   <form:label path="EXP_NO" type="hidden"></form:label>
			   <form:label path="EXP_NO">Exposure Name:</form:label>
			    <!-- <div class="col-sm-8"> -->			    
			    <form:input path="EXP_NO" value="" size="28" id="EXP_NO" disabled="true" class="form-control cus-control" style="text-align: right"/>	      
			    <!-- </div> -->		
			    </div>	    			   
			  </div>
			</div>
			<div class="col-md-3 col-sm-4 col-xs-12">
			  <div class="form-group">
			  <div class="col-md-12 prl-10">
		<!-- 	<form:label path="fileid">Select FileID <span style="color: red;">*</span> </form:label>				
			    <!-- <div class="col-sm-8"> 
			 <form:select path="fileid" id="PORTFOLIO_NO" class="form-control cus-control"
							name="fileid" onchange="onchangeApp();disableUnable()">
							 <option value="--Select For Lodgement--" selected="selected">--Select For Lodgement--</option>
							<!--   <form:option value="" label="--Select For Lodgement--"/>
					<c:forEach var="fileid" items="${appRefNoList}">
						<form:option id="${fileid}"	value="${fileid}">${fileid}</form:option>
					</c:forEach>
				</form:select>  -->
					<form:label path="fileid">Select FileID <span style="color: red;">*</span> </form:label>	
					<select name='fileid' id="PORTFOLIO_NO" class="form-control cus-control"
							 onchange="onchangeApp();disableUnable()">
					 <option value="--Select For Lodgement--" selected="selected">--Select For Lodgement--</option>
				        <c:forEach items="${appRefNoList}" var="fileid">
				               <option value="${fileid}">${fileid}</option>
				     </c:forEach>
				 </select>
				
				 
			    <!-- </div> -->	
			    </div>		    			   
			  </div>
			</div>

	<div class="clearfix"></div>
	<hr style=" margin: 5px 0 10px;  border: 1px solid #d8d8d8;">
	
	<div class="col-md-2 col-sm-4 col-xs-12">
			  <div class="form-group">
			  <div class="col-md-12 prl-10">
			   <form:label path="totExpSize1" >Total Exposure Size(&#8377;):</form:label>
			   <!--  <div class="col-sm-8"> -->
			    <form:input path="totExpSize1" type="hidden"></form:input>
			    <form:input path="totExpSize1" size="28" id="TOT_EXP_SIZE" class="form-control cus-control" disabled="true" style="text-align: right"/>	      
			  <!--   </div>	 -->	
			  </div>	    			   
			  </div>
			</div>		
			
			<div class="col-md-2 col-sm-4 col-xs-12">
			  <div class="form-group">
			  <div class="col-md-12 prl-10">
			   <form:label path="utilExp1">Utilized Exposure(&#8377;):</form:label>
			    <!-- <div class="col-sm-8"> -->
			    <form:input path="utilExp1"  size="28" class="form-control cus-control" id="UTIL_EXP" disabled="true" style="text-align: right" />	      
			    <!-- </div> -->	
			    </div>		    			   
			  </div>
			</div> 								
			<div class="col-md-2 col-sm-4 col-xs-12">
			  <div class="form-group">
			  <div class="col-md-12 prl-10">
			   <form:label path="pendingExp1"><p>Unutilized Exposure Limit(&#8377;):</p></form:label>
			    <!-- <div class="col-sm-8"> -->
			    <form:input path="pendingExp1" value="" size="28" class="form-control cus-control" id="PENDING_EXP" disabled="true" style="text-align: right" />	      
			    <!-- </div> -->	
			    </div>		    			   
			  </div>
			</div> 
					
			
							
			<div class="clearfix"></div>
			<hr style=" margin: 5px 0;  border: 1px solid #d8d8d8;">
			<div class="col-md-3 col-sm-4 col-xs-12">
			  <div class="form-group">
			  <div class="col-md-12 prl-10">
			   <%-- <form:label path="PORTFOLIO_START_DATE">Portfolio Start Date(dd/mm/yyyy):</form:label> --%>
			   <form:label path="PAYOUT_CAP" type="hidden"></form:label>
			   <form:label path="PAYOUT_CAP">Pay-out Cap:</form:label>
			    <!-- <div class="col-sm-8"> -->			    
			    <form:input path="PAYOUT_CAP" value="" size="28" id="PAYOUT_CAP" disabled="true" class="form-control cus-control" style="text-align: right"/>	      
			    <!-- </div> -->		
			    </div>	    			   
			  </div>
			</div>
			<div class="col-md-3 col-sm-4 col-xs-12">
			  <div class="form-group">
			  <div class="col-md-12 prl-10">
			   <%-- <form:label path="PORTFOLIO_START_DATE">Portfolio Start Date(dd/mm/yyyy):</form:label> --%>
			   <form:label path="GURANTEE_COVERAGE" type="hidden"></form:label>
			   <form:label path="GURANTEE_COVERAGE">Guarantee Coverage:</form:label>
			    <!-- <div class="col-sm-8"> -->			    
			    <form:input path="GURANTEE_COVERAGE" value="" size="28" id="GURANTEE_COVERAGE" disabled="true" class="form-control cus-control" style="text-align: right"/>	      
			    <!-- </div> -->		
			    </div>	    			   
			  </div>
			</div>
			<div class="col-md-3 col-sm-4 col-xs-12">
			  <div class="form-group">
			  <div class="col-md-12 prl-10">
			   <%-- <form:label path="PORTFOLIO_START_DATE">Portfolio Start Date(dd/mm/yyyy):</form:label> --%>
			   <form:label path="GURANTEE_FEE" type="hidden"></form:label>
			   <form:label path="GURANTEE_FEE">Guarantee Fee:</form:label>
			    <!-- <div class="col-sm-8"> -->			    
			    <form:input path="GURANTEE_FEE" value="" size="28" id="GURANTEE_FEE" disabled="true" class="form-control cus-control" style="text-align: right"/>	      
			    <!-- </div> -->		
			    </div>	    			   
			  </div>
			</div>							
			
			<% String portfolioNo="dd"; %>
			
			<div class="d-inlineblock mt-25">
				<input type="button" value="Accept" id="acceptRecords" class="btn btn-reset"
						 onclick="linkFunctionShow()" disabled="true">
				<input type="button" value="Reject"  class="btn btn-reset" name="action2" id="rejectRecords" disabled="true" 
						onclick="disableUnableRemarks()" />												
			</div>
			<div class="clearfix"></div>
			<div class="d-inlineblock">
				<input value="Success Records"  type="submit" class="btn btn-reset"	name="action3" disabled="true" id="showSuccessRecords" onclick="showSuccess()" />
				<input value="Failed Records"  type="submit" class="btn btn-reset" name="action4" disabled="true" id="ShowFailedRecords" onclick="showFailed()"/>
				<input type="hidden" id="portfolioNum1" name="portfolioNum1" />
			</div>
			
			<div class="clearfix"></div>
			<hr style=" margin: 5px 0 10px;  border: 1px solid #d8d8d8;">
			
			<div class="col-md-4 prl-5">
			<c:set var="contextPath" value="${pageContext.request.contextPath}" />
			<span style="color: red;">*</span><form:checkbox path="acceptStatus" value="Y" id="acceptStatus"/>
			<form:errors path="acceptStatus" cssClass="error" />	      
			We the MLI certify and forward the Management Certificate.</br> <a href="${contextPath}/nbfcCertificate.html" >
			Click here to certify and forward the Management Certificate</a>			
			</div>
			
			<div class="d-inlineblock">
				<input value="Forward to CGTMSE" class="btn btn-reset" id="accept" name="action1" disabled="true" onclick="clickToForwordCGTMSE()">
				<input type="submit" value=" Exit "	onclick="exitMLIDetails()" class="btn btn-reset" />											
			</div>
			
			</form:form>				
		</div>
		</div>
		
	</div>	
</div>
</div>		
	<script type="text/javascript">
	debugger;
	function exitMLIDetails() {
		//alert('Exit');
		document.getElementById('A').action = "/Aasha/nbfcCheckerHomeBack.html";
		document.getElementById('A').submit();
	}
		function disableUnableRemarks() {
		
			var diveForReject = document.getElementById("REMARKS1");
			diveForReject.style.display = "block";
			var REMARKS = document.getElementById("REMARKS").value;
			alert(REMARKS);
		if(REMARKS!=''){
		 document.getElementById("A").submit();
		}else{
			diveForReject.style.display = "block";
			alert('Please fill the Remarks');
			}
		}
function clickToForwordCGTMSE(){
	//alert('save');
	debugger;
	var acceptStatus = document.getElementById("acceptStatus").value;
	//alert(acceptStatus);
	//form.target='_parent';
	form=document.getElementById('A');
	form.target='_parent';
	//form.target='_blank';
	document.getElementById('A').action = "/Aasha/mliCheckerUpdateFirstForwordToCGTMSC.html";
	document.getElementById('A').submit();
}
function showSuccess(){
	debugger;
	/* alert('success');
	form=document.getElementById('A');
	form.target='_blank';
	form.action="/nbfc/mliCheckerUpdateFirstShowSuccessRecord.html";
	form.submit(); */
	//alert('success');
	form=document.getElementById('A');
	form.target='_blank';
	form.action="/Aasha/mliCheckerUpdateFirstShowSuccessRecord.html";
	form.submit();
	/* document.getElementById('A').action = "/nbfc/mliCheckerUpdateFirstShowSuccessRecord.html";
	document.getElementById('A').submit(); */
}
function showFailed(){
	debugger;
	//alert('success');
	form=document.getElementById('A');
	form.target='_blank';
	form.action="/Aasha/mliCheckerUpdateFirstShowFailedRecord.html";
	form.submit();
	/* document.getElementById('A').action = "/nbfc/mliCheckerUpdateFirstShowSuccessRecord.html";
	document.getElementById('A').submit(); */
}

		function onchangeApp() {
			debugger;
			var portfolioNum='';
			portfolioNum = document.getElementById("PORTFOLIO_NO").value;
			document.getElementById("portfolioNum1").value=portfolioNum;
			if(null!=portfolioNum || portfolioNum!=''){
			//alert(document.getElementById("portfolioNum1").value);
			try {			$.ajax({type : "POST",url : "getPortfolioDetails.html",data : "portfolioNum=" + portfolioNum,success : function(data) {
								var select2 = document.getElementById('SENCTIONED_PORTFOLIO');
								var portfolioNum = data.result.portfolioNum;   
				
								var portfolio_max_size = data.result.senctioned_portfolio;
								var guarantee_commission = data.result.guarantee_commission;
								var payout_cap = data.result.payout_cap;
								var portfolio_start_date = data.result.portfolio_start_date;
								var dout = Date.parse(portfolio_start_date);
								var portfolioFailedRecords = data.result.portfolioFailedRecords;
								var portfolioSuccessRecords = data.result.portfolioSuccessRecords;
								var portfolio_period = data.result.portfolio_period;
								$('#SENCTIONED_PORTFOLIO').val(
										portfolio_max_size);
								$('#GUARANTEE_COMMISSION').val(
										guarantee_commission);
								//$('#PAYOUT_CAP').val(payout_cap);
								$('#PORTFOLIO_PERIOD').val(portfolio_period);
								$('#PORTFOLIO_START_DATE').val(
										portfolio_start_date);
								$('#showSuccessRecords').val(
										portfolioSuccessRecords);

								$('#ShowFailedRecords').val(
										portfolioFailedRecords);

								//alert(portfolioNum+' '+portfolio_base_yer+' '+senctioned_portfolio+''+guarantee_commission+''+payout_cap+''+portfolio_start_date+''+typeOfLone+''+portfolio_period);

							},
							error : function(e) {

							}
						});

			} catch (err) {
				//alert('Error is : ' + err);
			}

		} 
		}

		
		function linkFunction() {
			var diveForReject = document.getElementById("link");
			diveForReject.style.display = "none";
			
 
		}

		function linkFunctionShow() {
			var diveForReject = document.getElementById("link");
			document.getElementById("accept").disabled = false;
			diveForReject.style.display = "block";

		}
		function viewRemarks() {
			alert('Please fill the remarks');
			var diveForReject = document.getElementById("remark");

			if (diveForReject.style.display !== "none") {
				diveForReject.style.display = "none";

			} else {
				diveForReject.style.display = "block";

			}

		}
		function disableUnable() {
			document.getElementById("acceptRecords").disabled = false;
			document.getElementById("rejectRecords").disabled = false;
			document.getElementById("ShowFailedRecords").disabled = false;
			document.getElementById("showSuccessRecords").disabled = false;
		}
		
	</script>
</body>
</html>