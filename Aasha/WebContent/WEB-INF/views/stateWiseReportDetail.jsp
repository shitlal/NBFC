<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>State Wise Report Form</title>
		<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
		<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
		<script src="<%=request.getContextPath()%>/js/jquery-3.2.1.min.js"></script>
		<script src="<%=request.getContextPath()%>/js/jquery-1.11.1.js"></script>
		<LINK href="<%=request.getContextPath()%>/css/stylesheet.css" rel="stylesheet" type="text/css">
		<link href="css/jquery-ui-css.css" rel="stylesheet" type="text/css">
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<%
			String s = (String) request.getAttribute("SName");
		%>
		<script language="javascript">
			function getStateWiseReportForm() {
				document.getElementById('A').action = "/Aasha/searchstatewisereport.html";
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
		<%
			String f_date=(String)session.getAttribute("FDate");
			String s_date=(String)session.getAttribute("TDate");
			String guaranteeStatus=(String)session.getAttribute("guaranteeStatus");
		%>
	</head>
	<body>
		<div class="main-section">
			<div class="container-fluid">
				<div>
					<div class="frm-section">
						<div class="col-md-12">
							 <h2 align="center" class="heading">State Wise Report</h2>
							<h5 class="notification-message">${message}</h5>
							<h5 class="error1">${Errormessage}</h5>
							<div class="loader"></div>
							
							<form:form method="POST" action="" id="A" class="form-horizontal">
								<%
									String userRole = (String) session.getAttribute("uRole");
								%>
								<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
									<div class="form-group">
										<label class="d-block text-purple" style="visibility: hidden";>
											1</label>
										<div class="col-md-6 prl-10">
											<label>From Date : <span style="color: red">*</span></label>
											<form:input path="toDate" size="28" id="toDate"
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
										<label class="d-block text-purple" style="visibility: hidden";>
											4</label>
										<div class="col-md-6 prl-10">
											<label>To Date : <span style="color: red">*</span></label>
											<form:input path="fromDate" size="28" id="fromDate"
												class="form-control cus-control" placeholder="eg.dd/mm/yyyy"
												autocomplete="off" />
											<form:errors path="fromDate" cssClass="error" />
											<div id="requiredMliValidityOfExposureLimitEndDate"
												Class="displayErrorMessageInRedColor"></div>
	
										</div>
									</div>
								</div>
	
								<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
									<div class="form-group">
									<label class="d-block text-purple" style="visibility: hidden";>
											5</label>
										<div class="col-md-12 prl-10">
												<label>Select Report :<span style="color: red">*</span></label>
												<label class="d-block"> 
												<form:radiobutton path="guaranteeStatus" value="Y"/>Guarantee Approved 
												<form:radiobutton path="guaranteeStatus" value="N"/>Guarantee Issued
												</label>
												<form:errors path="guaranteeStatus" cssClass="error" />
											<div id="requiredGuranteeOption"
												Class="displayErrorMessageInRedColor"></div>
											</div>
									</div>
								</div>
								
								<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
									<div class="form-group">
										<div class="d-inlineblock mt-35">
											<input type="submit" value="Submit" class="btn btn-reset"
												class="btn btn-reset" onclick="getStateWiseReportForm()" />
										</div>
									</div>
								</div>
								<div>
								</div>
							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<c:if test="${!empty rows}">
			<div class="container-fluid">
				<div class="frm-section">
					<div class="col-md-12">
						<table id="myTable" cellpadding=5
							class="table table-bordered table-hover cus-table mt-10 mb-0"
							cellspacing=5 align=center width=90%>
							<thead>
								<tr>
									<c:forEach items="${rows[0]}" var="column">
										<b> </b>
										<th><c:out value="${column.key}" /></th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${rows}" var="columns">
									<tr>
										<c:forEach items="${columns}" var="column">
											<td align="center">${column.value}</td>
										</c:forEach>
									</tr>
								</c:forEach>
							</tbody>
							<tfoot>
						            <tr>
						               <th colspan="2" style="background:rgba(176, 174, 178, 1); text-align:center; font-weight:700; font-size:16px;"> Total</th>
						               <th style="background:rgba(176, 174, 178, 1); text-align:center; font-weight:700; font-size:16px;"><%=request.getAttribute("sumGurTotCountStateWiseKey")%></th>
						               <th style="background:rgba(176, 174, 178, 1); text-align:center; font-weight:700; font-size:16px;"><%=request.getAttribute("sumGurTotAmtStateWiseKey") %></th>
						            </tr>
						    </tfoot>
						</table>
							<div class="d-inlineblock mt-35">
											<form:form action="/Aasha/backButtonData.html">
												<input type="submit" value="Back" class="btn btn-reset"
												class="btn btn-reset" />
											</form:form>
							</div>
					</div>
				</div>
			</div>
		</c:if>
		
		<c:if test="${!empty rowsStateMilWise}">
			<div class="container-fluid">
				<div class="frm-section">
					<div class="col-md-12">
						<table id="myTable" cellpadding=5
							class="table table-bordered table-hover cus-table mt-10 mb-0"
							cellspacing=5 align=center width=90%>
							<thead>
								<tr>
									<c:forEach items="${rowsStateMilWise[0]}" var="column">
										<b> </b>
										<th><c:out value="${column.key}" /></th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${rowsStateMilWise}" var="columns">
									<tr>
										<c:forEach items="${columns}" var="column">
											<td align="center">${column.value}</td>
										</c:forEach>
									</tr>
								</c:forEach>
							</tbody>
							
						</table>
							<div class="d-inlineblock mt-35">
											<form:form action="/Aasha/backButtonData.html">
												<input type="submit" value="Back" class="btn btn-reset"
												class="btn btn-reset" />
											</form:form>
							</div>
					</div>
				</div>
			</div>
		</c:if>
		
		
		<c:if test="${!empty rowsDistrictWise}">
			<div class="container-fluid">
				<div class="frm-section">
					<div class="col-md-12">
						<table id="myTable" cellpadding=5
							class="table table-bordered table-hover cus-table mt-10 mb-0"
							cellspacing=5 align=center width=90%>
							<thead>
								<tr>
									<c:forEach items="${rowsDistrictWise[0]}" var="column">
										<b> </b>
										<th><c:out value="${column.key}" /></th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${rowsDistrictWise}" var="columns">
									<tr>
										<c:forEach items="${columns}" var="column">
											<td align="center">${column.value}</td>
										</c:forEach>
									</tr>
								</c:forEach>
							</tbody>
							
						</table>
							<div class="d-inlineblock mt-35">
											<form:form action="/Aasha/backButtonData.html">
												<input type="submit" value="Back" class="btn btn-reset"
												class="btn btn-reset" />
											</form:form>
							</div>
					</div>
				</div>
			</div>
		</c:if>
		
		<c:if test="${!empty rowsStateMilWiseGI}">
			<div class="container-fluid">
				<div class="frm-section">
					<div class="col-md-12">
						<table id="myTable" cellpadding=5
							class="table table-bordered table-hover cus-table mt-10 mb-0"
							cellspacing=5 align=center width=90%>
							<thead>
								<tr>
									<c:forEach items="${rowsStateMilWiseGI[0]}" var="column">
										<b> </b>
										<th><c:out value="${column.key}" /></th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${rowsStateMilWiseGI}" var="columns">
									<tr>
										<c:forEach items="${columns}" var="column">
											<td align="center">${column.value}</td>
										</c:forEach>
									</tr>
								</c:forEach>
							</tbody>
							
						</table>
							<div class="d-inlineblock mt-35">
											<form:form action="/Aasha/backButtonData.html">
												<input type="submit" value="Back" class="btn btn-reset"
												class="btn btn-reset" />
											</form:form>
							</div>
					</div>
				</div>
			</div>
		</c:if>
		
		<c:if test="${!empty rowsGuaranteeIssuedDistWise}">
			<div class="container-fluid">
				<div class="frm-section">
					<div class="col-md-12">
						<table id="myTable" cellpadding=5
							class="table table-bordered table-hover cus-table mt-10 mb-0"
							cellspacing=5 align=center width=90%>
							<thead>
								<tr>
									<c:forEach items="${rowsGuaranteeIssuedDistWise[0]}" var="column">
										<b> </b>
										<th><c:out value="${column.key}" /></th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${rowsGuaranteeIssuedDistWise}" var="columns">
									<tr>
										<c:forEach items="${columns}" var="column">
											<td align="center">${column.value}</td>
										</c:forEach>
									</tr>
								</c:forEach>
							</tbody>
							
						</table>
							<div class="d-inlineblock mt-35">
											<form:form action="/Aasha/backButtonData.html">
												<input type="submit" value="Back" class="btn btn-reset"
												class="btn btn-reset" />
											</form:form>
							</div>
					</div>
				</div>
			</div>
		</c:if>
		
		<script src="js/jquery-1.10.2.js" type="text/javascript"></script>
			<div class="loader"></div>
		<script type="text/javascript">
			$(window).load(function() {
				$(".loader").fadeOut("slow");
			});
		</script>
		
	</body>
</html>
