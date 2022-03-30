<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>NPA Report Form</title>

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
	function submitMLIForm() {
		document.getElementById('A').action = "/Aasha/DANAppropriationReportDetailGFASF.html";
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
		<div class="container-fluid">
			<div>
			
				<div class="frm-section">
				 
					<div class="col-md-12">
						 <h2 align="center" class="heading">DAN Appropriation Report For GF/ASF  </h2>
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
									<div class="col-md-12 prl-8">
										<label>From Date : <span style="color: red">*</span></label>
										<form:input path="fromDate" value="" size="28" id="fromDate"
											class="form-control cus-control" style="text-align: left"
											placeholder="eg.dd/mm/yyyy" autocomplete="off" />
										<form:errors path="fromDate" cssClass="error" />
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
									<div class="col-md-12 prl-8">
										<label>To Date : <span style="color: red">*</span></label>
										<form:input path="toDate" type="" size="28" id="toDate"
											class="form-control cus-control" placeholder="eg.dd/mm/yyyy"
											autocomplete="off" />
										<form:errors path="toDate" cssClass="error" />
										<div id="requiredMliValidityOfExposureLimitEndDate"
											Class="displayErrorMessageInRedColor"></div>

									</div>
								</div>
							</div>

						
                             <div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">
									<label class="d-block text-purple" style="visibility: hidden";>
										5</label>
								<div class="col-md-12 prl-8">
											<label>DAN Type : <span style="color:red">*</span></label>								
											  	<input type="text" class="form-control cus-control d-none" placeholder="danType" >
											 	   <form:select path="danType" id="danType"  class="form-control cus-control">
												  <form:option value="" label="---Select---"/>	
												<form:option value="GF" label="GF"/> 
														<form:option value="ASF" label="ASF"/> 
										
											</form:select>  
											<form:errors path="danType" cssClass="error" />
											<div id="requireddanType" Class="displayErrorMessageInRedColor"></div>
										</div>
								</div>

							</div>
							
					

<%-- 
							<%
								}
							%> --%>
							<%-- <center><input type="submit" value="Submit" class="btn btn-reset"
											class="btn btn-reset" onclick="submitMLIForm()" /></center> --%>

							  <div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">

									<div class="d-inlineblock mt-35">
										<center><input type="submit" value="Submit" class="btn btn-reset"
											class="btn btn-reset" onclick="submitMLIForm()" /></center>
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
	
	<c:if test="${!empty rows1}">
		<!-- <div
			STYLE="height: 450px; width: 1030px; font-size: 12px; overflow: auto;"> -->
		<div class="container-fluid">
			<div class="frm-section">
				<div class="col-md-12">
				 <div style="text-align: left;">
				<!-- <button style="border:none !important;" > -->
		    		<a href="DANAppropriationGFASFReportDetailDownload.html">
		    			<img src="images/excel.png" alt="" data-toggle="tooltip" title="Export To Excel"  >
		    		</a>
		    		<!-- </button> -->
		    		 </div>
			            <table id="myTable" cellpadding=5
						class="table table-bordered table-hover cus-table mt-10 mb-0"
						cellspacing=5 align=center width=90%>
						<thead>
							<tr>
								    <th>SNo.</th>
								    <th> Name of the NBFC </th>
									<th> Demand  Advice Number</th>
									<th> Guaranteed Amount</th>
									<th>Dan Amount</th>
									<th>Appropriation Status ( Y / N )</th>
						
							</tr>
						</thead>
						<tbody>
						
						  <%
										int counter = 0;
									%>
						  <c:forEach items="${rows1}" var="item" varStatus="status">
									<tr>
									    <td><%=counter + 1%></td>
									  <!--  // <a href="a.html?fruit=apple" id="a">Hello</a> -->
									<td align="center">${item[1]}</td>
										 	<td align="center">${item[0]}</td>
										
										<td align="center">${item[2]}</td>
										<td align="center">${item[3]}</td>
										 <td align="center">${item[4]}</td>
										<td align="center">${item[5]}</td>
										
										<%
												counter += 1;
										%>
									</tr>
								</c:forEach>    
						
						
						
						   <%--  <c:forEach items="${rows}" var="columns">
								<tr>
									<c:forEach items="${columns}" var="column">
										<td align="center">${column.value}</td>
									</c:forEach>
								</tr>
							</c:forEach>  --%>   
						</tbody>
					</table>
					<center>
					
									 </center>
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