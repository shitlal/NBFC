<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<link href="<%=request.getContextPath()%>/js/jquery-ui.css"
	rel="stylesheet">
<script src="<%=request.getContextPath()%>/js/jquery-1.10.2.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-3.2.1.min.js"></script>
<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
<link
	href="https://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css"
	rel="stylesheet">
<!--<script src="https://code.jquery.com/jquery-1.10.2.js"></script>-->
<script src="https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link href="<%=request.getContextPath()%>/css/stylesheet.css"
	rel="stylesheet" type="text/css">

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<script type="text/javascript">
window.onload = function() {
	checkflag();
	}
	
	function checkflag(){
		var cboxes = (document.getElementsByName("chk")[0].value);	
		//  var len = cboxes.length;
		  alert(cboxes);
		   // for (var i=0; i<len; i++) {
		   //     alert(i + (cboxes[i].checked?' checked ':' unchecked ') + cboxes[i].value);
		  //  }
	}
</script>


</head>
<body>
<%
	String uesrName = (String) session.getAttribute("userId");
	if (uesrName == null) {
		response.sendRedirect("login");
	}
	System.out.println("uesrName....." + uesrName);
%>


	<div class="main-section">
		 <nav aria-label="breadcrumb">
   <ol class="breadcrumb cus-breadcrumb">
    <li class="breadcrumb-item"><a href="/Aasha/NPAUpgradationApproval.html">NPA Upgradation Approval/Return</a></li>  
    <li class="breadcrumb-item active" aria-current="page">NPA Upgradation Details</li>
  </ol> 
</nav>
		<div class="container-fluid">
			<!-- <div class="row"> -->
			<div>
				<div class="frm-section">
					<div class="col-md-12">
						<h5 class="error1 mtb-0">
							<strong>${message}</strong>
						</h5>

						<div id="successMsgId">
							<h5 class="notification-message">
								<strong>${SuccessMsg} ${showPortfolioNoKey} </strong>
							</h5>
						</div>

						<form:form method="POST" action="NpaUpgradationUpdate.html" id="A"
							class="form-horizontal">
							<div class="clearfix"></div>
							<hr style="margin: 0; border: 1px solid #d8d8d8">
                          <h5>
								<strong> General Details</strong>
							</h5>
							<div class="col-md-2 col-sm-4 col-xs-12">
								<div class="form-group">
									<div class="col-md-12 prl-10">
										<label>NBFC MLI Name:</label>
										<form:input path="bankName"
											class="date-picker form-control cus-control" size="28" readonly="true" />
										<form:errors path="bankName" cssClass="error" />
									</div>
								</div>
							</div>

							<div class="col-md-2 col-sm-4 col-xs-12">
								<div class="form-group">
									<div class="col-md-12 prl-10">
										<label>Member ID:</label>
										<form:input path="mLIID"
											class="date-picker form-control cus-control" size="28" readonly="true" />
										<form:errors path="mLIID" cssClass="error" />
									</div>
								</div>
							</div>

							<div class="col-md-2 col-sm-4 col-xs-12">
								<div class="form-group">
									<div class="col-md-12 prl-10">
										<label>Unit Name:</label>
										<form:input path="borrowerName"
											class="date-picker form-control cus-control" size="28" readonly="true" />
										<form:errors path="borrowerName" cssClass="error" />
									</div>
								</div>
							</div>

							<div class="col-md-2 col-sm-4 col-xs-12">
								<div class="form-group">
									<div class="col-md-12 prl-10">
										<label>CGPAN:</label>
										<form:input path="CGPAN"  size="20" readonly="true"
											class="form-control cus-control" id="CGPAN" />
									</div>
								</div>
							</div>


							<div class="col-md-2 col-sm-4 col-xs-12">
								<div class="form-group">
									<div class="col-md-12 prl-10">
										<label>Application Status:</label>
										<form:input path="status"  
											class="date-picker form-control cus-control" size="28" readonly="true"/>
										<form:errors path="status" cssClass="error" />
									</div>
								</div>
							</div>



							<div class="clearfix"></div>
							<hr style="margin: 0; border: 1px solid #d8d8d8">
							<h5 class="sub-head">
								<strong> NPA Details: </strong>
							</h5>

							<div class="col-md-2 col-sm-4 col-xs-12">
								<div class="form-group">
									<div class="col-md-12 prl-10">
										<label>Date on which Account turned NPA:</label>
										<form:input path="npaDt"
											class="date-picker form-control cus-control" size="28" readonly="true"/>
										<form:errors path="npaDt" cssClass="error" />
									</div>
								</div>
							</div>

							<div class="col-md-2 col-sm-4 col-xs-12">
								<div class="form-group">
									<div class="col-md-12 prl-10">
										<label>Reason For Account Turning NPA:</label>
										<form:input path="npaReason"
											class="date-picker form-control cus-control" size="40" readonly="true"/>
										<form:errors path="npaReason" cssClass="error" />
									</div>
								</div>
							</div>

							<div class="col-md-2 col-sm-4 col-xs-12">
								<div class="form-group">
									<div class="col-md-12 prl-5">
										<label class="d-block"> NPA Upgradation Date: </label>
										<form:input path="npaUpgradationDt"
											class="date-picker form-control cus-control" id="lasInspDt"
											placeholder="e.g:dd/mm/yyyy" />
										<form:errors path="npaUpgradationDt" cssClass="error" />

									</div>
								</div>
							</div>
							
							<div class="col-md-2 col-sm-4 col-xs-12">
								<div class="form-group">
									<div class="col-md-12 prl-10">
										<label>Reason for Upgradation:</label>
										<form:input path="upgradationRemarks"
											class="date-picker form-control cus-control" size="28"
											placeholder="Upgradation Reason.." />
										<form:errors path="upgradationRemarks" cssClass="error" />
									</div>
								</div>
							</div>
									
							
					

						</form:form>
					</div>
				</div>

			</div>
		</div>
	</div>

</body>

</html>


