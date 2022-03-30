<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
<title>Portfolio Creation</title>
<script src="js/jquery-1.11.1.js" type="text/javascript"></script>
<script type="text/javascript"
	src="jquery-ui-1.10.3/ui/jquery.ui.datepicker.js"></script>
<link
	href="https://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css"
	rel="stylesheet">
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
<script src="https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<LINK href="<%=request.getContextPath()%>/css/stylesheet.css"
	rel="stylesheet" type="text/css">
<style>
 img {
		   height:100px;
		   left: 49%;
		   position: absolute;
		   top: 80%;
		   width: 100px;
		}

.clmref {
	cursor: pointer;
	border-right: none;
	border-left: none;
	border-top: none;
	border-bottom: 1px solid rgba(51, 122, 182, 1);
}

.clmref:hover {
	color: rgba(146, 59, 149, 1);
}

.modal-dialog {
	width: 80%;
}

.modal-header, .modal-footer {
	padding: 10px 15px;
}

.modal-body {
	padding: 5px 15px;
}

.declarebtn:hover {
	cursor: pointer;
}

.btn-popup:hover {
	cursor: pointer;
}
</style>
function myFunction() { window.open(this.href, 'mywin',
'left=20,top=20,width=500,height=500,toolbar=1,resizable=0'); return
false; }

</script>
<script>
<!--
	/* var Pass='PASS';
	 var Fail='FAILED'; */

	$(function() {
		$("#ratingDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
	});

		var d_status;
		function showProgress(d_status){
	   document.getElementById('progress_id').style.display=d_status;   
		}
	 
	debugger;
	function validatedata() {
		var flag = "";
		var chflag = 5;
		var submit = "";
		var str = '';
		var d_status;
		var qqqq = document.getElementById('ckbankAuthority').value;
		//   alert(document.getElementById('ckbankAuthority').checked);
		debugger;
		submit = "1";

		$('#myTable > tbody  > tr')
				.each(
						function(index, tr) {

							if ($(this).find("#AcceptReturn").val() == "Return") {
								if ($(this).find("#returnRemark").val() == ""
										|| $(this).find("#returnRemark").val() == null) {
									$(this).find("#returnRemark").val(
											"Enter Remarks");
									submit = "0";
								}
							}
						});

		if (!document.getElementById('ckbankAuthority').checked == true) {
			document.getElementById("ReqrevisedBankAuthority").innerHTML = "Kindly certify the declaration";
			submit = "0";
		} else {
			//submit = "1";
			chflag;
		}
		if (submit == "1") {
			document.getElementById("disabledButton").disabled = true;  // after submitting 
			showProgress('block');// function to show progressBar.
			document.getElementById('mliApproveOrRejectClaimId').action = "/Aasha/mliApproveOrRejectTenure.html?CHK="
					+ qqqq;
			document.getElementById('mliApproveOrRejectClaimId').submit();
		} else {
			flag = false;
		}
	}
</script>

</head>

<body bgcolor="#E0FFFF" onload="showProgress('none');">
	<form:form action="" method="POST" id="mliApproveOrRejectClaimId"
		class="form-horizontal">

		<!-- mliApproveOrRejectClaim.html -->
		<div class="main-section">

			<div class="container-fluid">
				<div class="panel-group" id="accordion" role="tablist"
					aria-multiselectable="true">
					<div class="panel panel-default cus-pnl cus-pnl">
						<div class="panel-heading cus-pheading cus-pheading" role="tab"
							id="headingOne">
							<h4 class="panel-title cus-ptitle cus-ptitle">
								<a role="button" data-toggle="collapse" data-parent="#accordion"
									href="#collapseOne" aria-expanded="true"
									aria-controls="collapseOne"> Tenure Modification Details
									For Approval: <i class="fa fa-chevron-circle-down"
									aria-hidden="true"></i> <!-- <span><img src="images/plus.png"  style="width:18px;" class="more-less plus" alt=""></span>
        <span><img src="images/negative.png"  style="width:18px;" class="more-less minus" alt=""></span> -->
								</a>
							</h4>
						</div>
						<div class="panel-body">
							<div>
								<div class="tbl-details">
									<div class="col-md-12">
										<div class="d-inlineblock float-right ">
											<!-- <button style="border:none !important; cursor: not-allowed;">
		    						<img src="images/excel.png" alt="" data-toggle="tooltip" title="Export To Excel">
		    					</button> -->
										</div>
								<div>	<IMG src="images/progress_Images.gif" alt="Wait.." width="49" height="37" border="0"  id="progress_id" style="display:none"/></div>	
							 	<IMG src="images/progress_Images.gif" alt="OK"  width="49" height="37" border="0" style="display:none"   >  
										<h5 class="notification-message">${msg}</h5>
										<table id="myTable"
											class="table table-bordered table-hover cus-table mt-10 mb-0">

											<c:if test="${!empty danDetailList}">
												<thead>
													<tr>
														<th>Sr.No.</th>
														<th>Member Id</th>
														<th>CGPAN</th>
														<th>Unit Name</th>
														<th>App Status</th>
														<th>First Disbursement Date</th>
														<th>Original Tenure</th>

														<th>Revised Tenure</th>
														<th>Revised Expiry Date</th>

														<th>Modification Remarks</th>

														<th>Return Remarks</th>

														<th>Decision</th>
														<!--
						<th>I duly certify and declare that the changes made and /are
					as per the record available with the NBFC</th>

				-->
													</tr>
												</thead>
												<%
													int counter = 0;
												%>

												<c:forEach items="${danDetailList}" var="danDetailList">
													<tr <%if (counter % 2 == 0) {%> bgcolor="silver" <%}%>>
														<td><%=counter + 1%></td>
														<td><c:out value="${danDetailList.mliID}" /></td>
														<td><a
															href="TenureDataModificationDetails.html?TenId=${danDetailList.tId}"
															data-toggle="tooltip" target="_blank"
															style="cursor: pointer !important"
															title="Click Here To Open Tenure form!"> <form:input
																	path="cgpan" value="${danDetailList.cgpan}"
																	class="clmref" readonly="true" /></a></td>
														<td><c:out value="${danDetailList.mseName}" /></td>
														<td><c:out value="${danDetailList.status}" /></td>
														<td><c:out value="${danDetailList.firstDisburseDate}" /></td>
														<td><c:out value="${danDetailList.tenure}" /></td>
														<td><c:out value="${danDetailList.reviseTenure}" /></td>
														<td><c:out value="${danDetailList.reviseExpirydate}" /></td>


														<td><c:out
																value="${danDetailList.modificationRemark}" /></td>

														<td><form:input path="returnRemark" id="returnRemark"
																class="form-control cus-control" /></td>

														<td><form:select path="AcceptReturn"
																class="form-control cus-control" id="AcceptReturn"
																readonly="true">
																<!-- 	<form:option value="Select" />  -->
																<form:option value="Accept" />
																<form:option value="Return" />

															</form:select></td>
														<!--<TD><form:checkbox path="ckbankAuthority" value="Y" id="ckbankAuthority"/></TD>

					-->
														<%
															counter += 1;
														%>
													</tr>
												</c:forEach>
											</c:if>
											<c:if test="${empty danDetailList}">
												<thead>
													<tr>
														<th>Sr.No.</th>
														<th>Member Id</th>
														<th>CGPAN</th>
														<th>Unit Name</th>
														<th>App Status</th>
														<th>First Disbursement Date</th>
														<th>Original Tenure</th>
														<th>Revised Expiry Date</th>
														<th>Revised Tenure</th>

														<th>Modification Remarks</th>

														<th>Return Remarks</th>

														<th>Decision</th>

													</tr>
												</thead>
												<tr>Record Not Found
												</tr>
											</c:if>
										</table>
										<div class="modal-footer">

											<div class="form-group">

												<lable>
												<form:checkbox path="ckbankAuthority" value="Y"
													id="ckbankAuthority" /> I duly certify and declare that the
												changes made and /are as per the record available with the
												NBFC :<span style="color: red">*</span>
												</label>
												<div id="ReqrevisedBankAuthority"
													Class="displayErrorMessageInRedColor"></div>
											</div>
										</div>

										<div class="modal-footer">
											<button type="button"   id="disabledButton"  class="btn btn-default"
												data-dismiss="modal" onclick="validatedata();">Submit</button>
										</div>
										<!--    <div class="modal-footer">
						        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						      </div> -->
									</div>

								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="panel panel-default cus-pnl"></div>
				<!-- panel -->

				<div id="" class="modal fade" role="dialog">
					<div class="modal-dialog">

						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">&times;</button>
								<!--  <h4 class="modal-title">Modal Header</h4> -->
							</div>
							<div class="modal-body"></div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Close</button>
							</div>
						</div>

					</div>
				</div>

			</div>
		</div>
		</div>
		</div>
		</div>
		</div>
	</form:form>
</body>
</html>




