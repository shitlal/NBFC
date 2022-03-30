<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@	taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
		<title>Approval/Rejection from CGTMSE</title>
		<link href="css/jquery-ui-css.css" rel="stylesheet" type="text/css">
		<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<LINK href="<%=request.getContextPath()%>/css/stylesheet.css" rel="stylesheet" type="text/css">
		
		<script type="text/javascript">		
		
		function rejectWithRemake(memberId, param){
			//alert("param::"+param);
			
			var remakes = $('#cgtmseCheckerRemarks_'+param).val();
			//var remakes = document.getElementById('cgtmseCheckerRemarks_'+param+'').value();
			//alert("remakes ::"+remakes);
			//alert("memberId:1:"+memberId);
			//alert("remakes ::"+remakes);
			if(remakes != "" && remakes != 'null' && remakes != null && (remakes.trim()).length > 0){
				var url = ("updBankMandateStatusFromCGTMSE.html?memberId="+memberId+"&cgtmseCheckerRemarks="+remakes);
				//alert("url::"+url);
				document.getElementById('rejectionBankMandateFromCGTMSEId').action =  url;
				document.getElementById('rejectionBankMandateFromCGTMSEId').submit();
			}else{
				alert("Checker Remarks is required.");
			}
			
		}
		</script>
		
	</head>
	<body onload="clearField()">
		<div class="main-section">
			<div class="frm-section">
						<h5 class="notification-message">${message}</h5>
						<h5 class="error1">${Errormessage}</h5>
				<div class="col-md-12">
					<form:form method="POST" action=""  class="form-horizontal" id="rejectionBankMandateFromCGTMSEId" name="frm" >
						<h5><strong>Bank Mandate Details for Rejection</strong></h5>
							<table id="myTable"  class="table table-bordered table-hover cus-table mt-10 mb-0">
							<% int counter=0;%>
							<c:if test="${!empty bankmanDateList}">
								<thead>
									<tr>
										<th>Sr.No.</th>
										<th>MLI Name</th>
										<th>Member Id</th>
										<th>MLI Name ( Beneficiary)</th>
										<th>Beneficiary's Bank Name</th>
										<th>Branch Name</th>
										<th>Branch Code</th>
										<th>Account No</th>
										<th>Checker Remarks</th>
										<th>Action</th>
									</tr>
									<c:forEach items="${bankmanDateList}" var="bankmanDateList">	
										<tr <% if(counter%2==0){%>bgcolor="silver" <%}%>>
											<td>${bankmanDateList.ROWNUM}</td>
											<td>${bankmanDateList.mliName}</td>
											<td>${bankmanDateList.memberId}</td>
											<td>${bankmanDateList.mliBeneficiaryName}</td>
											<td>${bankmanDateList.mliBeneficiaryBankName}</td>
											<td>${bankmanDateList.branchName}</td>
											<td>${bankmanDateList.branchCode}</td>
											<td>${bankmanDateList.accountNo}</td>															
											<td><form:input type="text" path="cgtmseCheckerRemarks" placeholder="Remarks" class="form-control cus-control"	id="cgtmseCheckerRemarks_${bankmanDateList.memberId}" style="top-margine:50"/></td>															
										   <td><a onclick="rejectWithRemake('${bankmanDateList.memberId}', '${bankmanDateList.memberId}');" href="javascript:void(0);">Reject</a></td>
										</tr>
									</c:forEach>	
								</thead>
						</c:if>
						<c:if test="${empty bankmanDateList}">
								<thead>
									<tr>
										<th>Sr.No.</th>
										<th>MLI Name</th>
										<th>Member Id</th>
										<th>MLI Name ( Beneficiary)</th>
										<th>Beneficiary's Bank Name</th>
										<th>Branch Name</th>
										<th>Branch Code</th>
										<th>Account No</th>
										<th>Checker Remarks</th>
										<th>Action</th>
									</tr>
									
								</thead>
						</c:if>
							
				</table>
			</form:form>
			</div>
		</div>
	</div>
	</body>
</html>
