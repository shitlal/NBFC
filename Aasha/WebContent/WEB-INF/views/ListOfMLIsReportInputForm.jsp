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
		document.getElementById('A').action = "/Aasha/ListofMLIsReportData.html";
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
						 <h2 align="center" class="heading"> List of MLIs  Report</h2>
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
										5</label>
								<div class="col-md-12 prl-8">
											<label>MLI Long Name : <span style="color:red">*</span></label>								
											  	<input type="text" class="form-control cus-control d-none" placeholder="MLI Long Name" >
											 	   <form:select path="mliLongName" id="mliLongName" onchange="getMliShortName()" class="form-control cus-control">
												  <form:option value="" label="---Select---"/>	
												 <form:options items="${mliLongName}" />	
											</form:select>  
											<form:errors path="mliLongName" cssClass="error" />
											<div id="requiredMliLongName" Class="displayErrorMessageInRedColor"></div>
										</div>
								</div>

							</div>
							
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
		    		<a href="ListofMLIsReportDataDownload.html">
		    			<img src="images/excel.png" alt="" data-toggle="tooltip" title="Export To Excel"  >
		    		</a>
		    		<!-- </button> -->
		    		 </div>
			            <table id="myTable" cellpadding=5
						class="table table-bordered table-hover cus-table mt-10 mb-0"
						cellspacing=5 align=center width=90%>
						<thead>
							<tr>
								     <%-- <c:forEach items="${rows[0]}" var="column">  
									 <b> </b>
									 <th><c:out value="${column.key}" /></th>  
								  </c:forEach>  --%>   
								     <th>SNo.</th>
									<th>MLI NAME</th>
									<th>OPERATING OFFICES</th>
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
										<td align="center">${item[0]}</td>
										<td align="center"> <a href="/Aasha/GetMlisDetels.html?bankMemberId=${item[2]}">${item[1]}</a></td>
										 
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
						<div class="d-inlineblock mt-35">
										<form:form action="/Aasha/MLIListsbackButton.html">
										<input type="submit" value="Back" class="btn btn-reset"
											class="btn btn-reset" />
											</form:form>
									 </div>
									 </center>
				</div>
			</div>
		</div>
	</c:if>
	
	
	
	<c:if test="${!empty rows2}">
		<!-- <div
			STYLE="height: 450px; width: 1030px; font-size: 12px; overflow: auto;"> -->
		<div class="container-fluid">
			<div class="frm-section">
				<div class="col-md-12">
				 <div style="text-align: left;">
				<!-- <button style="border:none !important;" > -->
		    		<a href="GetMlisDetelsDownload.html">
		    			<img src="images/excel.png" alt="" data-toggle="tooltip" title="Export To Excel"  >
		    		</a>
		    		<!-- </button> -->
		    		 </div>
			            <table id="myTable" cellpadding=5
						class="table table-bordered table-hover cus-table mt-10 mb-0"
						cellspacing=5 align=center width=90%>
						<thead>
							<tr>
								     <%-- <c:forEach items="${rows[0]}" var="column">  
									 <b> </b>
									 <th><c:out value="${column.key}" /></th>  
								  </c:forEach>  --%>   
								    <th>SNo.</th>
									<th>OPERATING OFFICES</th>
									<th>ADDRESS</th>
									<th>Phone No</th>
									 
								     
							</tr>
						</thead>
						<tbody>
						
						  <%
										int counter = 0;
									%>
						  <c:forEach items="${rows2}" var="item" varStatus="status">
									<tr>
									    <td><%=counter + 1%></td>
									    <td align="center">${item[2]}</td>  
										<td align="center">${item[0]}</td>
										<td align="center">${item[1]}</td>
										<%
												counter += 1;
										%>
									</tr>
								</c:forEach>    
						
						 </tbody>
					</table>
					<div align="center">
						<!-- <div class="d-inlineblock mt-35"> -->
										<form:form action="/Aasha/ListofMLIsReportDataBackButton.html">
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