<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String sN=(String)request.getAttribute("sName");
    String expLim=(String)request.getAttribute("eXposureLimit");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
		<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
		<title>Modify MLI Details</title>
		<script src="js/jquery-1.11.1.js" type="text/javascript"></script>
		<script type="text/javascript" src="jquery-ui-1.10.3/ui/jquery.ui.datepicker.js"></script>
		<link href="https://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css" rel="stylesheet">
		<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
		<script src="https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
		<link href="<%=request.getContextPath()%>/css/stylesheet.css" rel="stylesheet" type="text/css">

		<script>
			$(function() {
		
				$("#ratingDate").datepicker({
					dateFormat : 'dd/mm/yy'
				});
		
			});
		</script>

	</head>

<body onload="onload()">
	<div class="main-section">
	<div class="container-fluid">
		<nav aria-label="breadcrumb">
			  <ol class="breadcrumb cus-breadcrumb">
			    <li class="breadcrumb-item"><a href="/Aasha/mliRegistrationPage.html">MLI Registration</a></li>  
			    <li class="breadcrumb-item active" aria-current="page">Modify MLI Details</li>
			  </ol>
		</nav>
		<div class="frm-section">
			<div class="col-md-12">
			<h2 align="center" class="heading">Modify MLI Details</h2>
			<h5 class="notification-message"><strong>${message}</strong></h5>
			<form:form method="GET" id="A" class="form-horizontal">
			
			<h4 class="detail_sp1">MLI Basic Detail</h4>
			<div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
			 <form:label path="longName">Long Name <span style="color: red;">*</span></form:label>
			  <form:input path="longName" size="28" value="${mliDetail.longName}"  class="form-control cus-control" readonly="true" />				   
			   <form:errors path="longName" Class="error" />
			   <form:input path="" size="28" type="hidden" />
			  </div>
			  </div>
		 </div>
		 	 
		 <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
			<form:label path="shortName">Short Name <span style="color: red;">*</span></form:label>
			<form:input path="shortName" size="28" value="${mliDetail.shortName}" class="form-control cus-control" readonly="true" />		    
			  <form:errors path="shortName" cssClass="error" />		   
			  </div>
			  </div>
		 </div>
		 	 
		 <div class="col-md-6 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
			<form:label path="companyAddress">Registered Address of the Company<span style="color: red;">*</span> </form:label>
			<form:input path="companyAddress" size="28" value="${mliDetail.companyAddress}" class="form-control cus-control"  />		    
			  <form:errors path="companyAddress" cssClass="error" />   <form:input path="" size="28" type="hidden" />
			  </div>
			  </div>
		 </div>					
		 	 
		 <div class="clearfix"></div>
		 	 <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
			<form:label path="state">State <span style="color: red;">*</span></form:label>
				<form:select path="state" id="state" onchange="doAjaxPost4()"
												class="form-control cus-control">
												<c:forEach var="state1" items="${stateNameObj}">
													<form:option value="${state1.ste_name}"></form:option>
												</c:forEach>
					
												<c:forEach var="state" items="${stateList}">
													<form:option id="${state.ste_code}"
														value="${state.ste_code}">${state.ste_name}</form:option>
												</c:forEach> 
											</form:select>
			<form:errors path="state" cssClass="error" />	<form:input path="" size="28" type="hidden" />
			  </div>
			  </div>
		 	</div>	
		 	 
		 	  <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
			<form:label path="city">City <span style="color: red;">*</span> </form:label>
			<form:input path="city" size="28" value="${mliDetail.city}" class="form-control cus-control" />
			<form:errors path="city" cssClass="error" /> <form:input path="" size="28" type="hidden" />
			  </div>
			  </div>
		 	 </div>	 
		 	 
		 	   <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
			<form:label path="district">District <span style="color: red;">*</span> </form:label>
			<form:select path="district" id="district"
												class="form-control cus-control">
												<form:option value="${mliDetail.district}" />
												<c:forEach var="district" items="${districtList}">
													<form:option id="${district.dst_name}"
														value="${district.dst_name}">${district.dst_name}</form:option>
												</c:forEach>
	
											</form:select>
			<form:errors path="district" cssClass="error" />
			  </div>
			  </div>
		 	 </div>	 
		 	 
		 	 <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
		<form:label path="pincode">Pincode <span style="color: red;">*</span> </form:label>
			<form:input path="pincode" size="28" maxlength="6" value="${mliDetail.pincode}" class="form-control cus-control" />
			<form:errors path="pincode" cssClass="error" /> <form:input path="" size="28" type="hidden" />
			  </div>
			  </div>
		 	 </div>	 
		 	 
		 	 <div class="clearfix"></div>
		 	  <h4 class="detail_sp1">MLI Contact Details</h4>
		 	  <!-- <hr style=" margin: 10px 0;  border: 1px solid #d8d8d8"> -->
		 	  
		 	   <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
			<form:label path="landlineNumber" class="d-block">Land Line Number  </form:label>
			<form:input path="phoneCode" size="3" class="form-control cus-control  w-28 d-inline" maxlength="3" value="${mliDetail.phone_code}" />
			 <form:input path="landlineNumber" class="form-control cus-control  w-69 d-inline" size="20" maxlength="8" value="${mliDetail.landlineNumber}" /> 
			<form:errors path="phoneCode" cssClass="error" /> <form:errors path="landlineNumber" cssClass="error" />
			<form:input path="" size="28" type="hidden" />
			  </div>
			  </div>
		 	 </div>
		 	 
		 	 	 <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
		<form:label path="emailId">Email Id <span style="color: red;">*</span> </form:label>
			<form:input path="emailId" size="28" value="${mliDetail.emailId}"  class="form-control cus-control"/>
			<form:errors path="emailId" cssClass="error" /> <form:input path="" size="28" type="hidden" />
			  </div>
			  </div>
		 	 </div>	
		 	  
		 	    <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
		<form:label path="contactPerson">Contact Person <span style="color: red;">*</span></form:label>
		<form:input path="contactPerson" size="28" value="${mliDetail.contactPerson}"  class="form-control cus-control"/>
		<form:errors path="contactPerson" cssClass="error" />
			  </div>
			  </div>
		 	 </div>	
		 	 
		 	  <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
		<form:label path="mobileNUmber">Mobile No.<span style="color: red;">*</span> </form:label>
		<form:input path="mobileNUmber" size="28" maxlength="10" value="${mliDetail.mobileNUmber}" class="form-control cus-control" /> 
		<form:errors path="mobileNUmber" cssClass="error" />
			  </div>
			  </div>
		 	 </div>	
		 	 
		 	<!--  <div class="col-md-3 col-sm-4 col-xs-12 d-none">
				<div class="form-group">
				<div class="col-md-12 prl-10">
				<form:label path="faxNumber" class="d-block">Fax Number </form:label>
				<form:input path="faxCode" class="form-control cus-control  w-28 d-inline" size="3" maxlength="3" value="${mliDetail.faxCode}" />
				 <form:input path="faxNumber" size="20" maxlength="8" value="${mliDetail.faxNumber}" class="form-control cus-control w-69 d-inline"  />
				 <form:errors path="faxNumber" cssClass="error" />
					
				  </div>
				  </div>
		 	 </div>	 -->
		 	 
		 	  <div class="clearfix"></div>
		 	  <h4 class="detail_sp2">MLI Registration Detail</h4>
		 	  <!-- <hr style=" margin: 10px 0;  border: 1px solid #d8d8d8"> -->
		 	  
		 	   <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
			<form:label path="rbiReggistrationNumber">RBI Registration No. <span style="color: red;">*</span> </form:label>
			<form:input path="rbiReggistrationNumber" size="28" maxlength="15" value="${mliDetail.rbiReggistrationNumber}" class="form-control cus-control" readonly="true" />
			<form:errors path="rbiReggistrationNumber" cssClass="error" />
			  </div>
			  </div>
		 	 </div>	
		 	  
		 	  <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
			<form:label path="companyCIN">CIN of the Company <span style="color: red;">*</span> </form:label>
		<form:input path="companyCIN" size="28" maxlength="21" value="${mliDetail.companyCIN}" class="form-control cus-control" />
					<form:errors path="companyCIN" cssClass="error" />
			  </div>
			  </div>
		 	 </div>	
		 	 
		 	   
		 	 
		 	 <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
			<form:label path="companyPAN">PAN of the Company<span style="color: red;">*</span>	</form:label>
			<form:input path="companyPAN" size="28" maxlength="11" value="${mliDetail.companyPAN}" readonly="true" class="form-control cus-control" />
				<form:errors path="companyPAN" cssClass="error" />
			  </div>
			  </div>
		 	 </div>	
		 	 
		 	
		 	 
		 	  <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
			<form:label path="gstinNumber">GSTIN  No.<span style="color: red;">*</span> </form:label>
			<form:input path="gstinNumber" size="28" maxlength="15" class="form-control cus-control"
							value="${mliDetail.gstinNumber}" readonly="true" />
			<form:errors path="gstinNumber" cssClass="error" />
			  </div>
			  </div>
		 	 </div>	
		 	 
		  <div class="clearfix"></div>
		   <h4 class="detail_sp3">Rating Detail</h4>
		 	  <!-- <hr style=" margin: 10px 0;  border: 1px solid #d8d8d8"> -->
		 	 
		 	 <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
		<form:label path="rating">Rating<span style="color: red;">*</span> </form:label>
			<form:select path="rating" id="rating"
												class="form-control cus-control">
												<form:option value="${mliDetail.rating}" />
												<c:forEach var="rating" items="${ratingAgencyList}">
													<form:option id="${rating.mliReating}"
														value="${rating.mliReating}">${rating.mliReating}</form:option>
												</c:forEach>
	
	
											</form:select>
			<form:errors path="rating" cssClass="error" />
			  </div>
			  </div>
		 	 </div>	   	 
		 	 
		 	 
		 	 
		 	 <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
		<form:label path="ratingAgency">Rating Agency<span style="color: red;">*</span> </form:label>
			<form:select path="ratingAgency" id="ratingAgency"
												class="form-control cus-control">
												<form:option value="${mliDetail.ratingAgency}" />
												<form:option value="CARE RATINGS" label="CARE RATINGS" />
												<form:option value="INDIA RATINGS AND RESEARCH"
													label="INDIA RATINGS AND RESEARCH" />
												<form:option value="CRISIL" label="CRISIL" />
												<form:option value="ICRA" label="ICRA" />
												<form:option value="Fitch Ratings" label="Fitch Ratings" />
												<form:option value="Acuite Ratings" label="Acuite Ratings" />
												<form:option value="NA" label="NA" />
											</form:select>
				<form:errors path="ratingAgency" cssClass="error" />
			  </div>
			  </div>
		 	 </div>	 
		 	 
		 
		 	 
		 	  <div class="col-md-3 col-sm-4 col-xs-12">
			<div class="form-group">
			<div class="col-md-12 prl-10">
		<form:label path="ratingDate">Date of Rating<span style="color: red;">*</span> </form:label>
		<form:input path="ratingDate" class="date-picker form-control cus-control" size="28" value="${mliDetail.ratingDate}" />
		<form:errors path="ratingDate" cssClass="error" />
			  </div>
			  </div>
		 	 </div>	
		 	 
		 	
			
			<div class="d-inlineblock mt-25">
			<input type="submit" value="Update" class="btn btn-reset" onclick="saveMLIDetails()" />
			<!-- <input type="submit" value="Reset" onclick="resetMLIDetails()" class="btn btn-reset" /> -->
			<input type="submit" value="Close" onclick="exitMLIDetails()" class="btn btn-reset" />
			</div>
			
			</form:form>
			</div>
		</div>	
		</div>
	</div>
	</div>	
	
		
	</body>
	<script type="text/javascript">
		function onload() {
	
			var val0 = "${mliDetail.state}";
			var val1 = "${mliDetail.district}";
			
			var val2 = "${mliDetail.ratingAgency}";
			var val3 = "${mliDetail.rating}";
	
			if (val0.length>0) {
				//alert(val0 + '    ' + val1 + '    ' + val2 + '   ' + val3);
				//alert("1");
				$('#rating').val(val3).trigger('change', [ true ]);
				$('#state').val(val0).trigger('change', [ true ]);
				$('#ratingAgency').val(val2).trigger('change', [ true ]);
				
				
				
			} else {
				//alert('not NUll');
				//alert("2");
				var val0 = "${mliDetail.state}";
				var val1 = "${mliDetail.district}";
				//alert(val1);
				var val2 = "${mliDetail.ratingAgency}";
				var val3 = "${mliDetail.rating}";
				$('#rating').val(val3).trigger('change', [ true ]);
				$('#state').val(val0).trigger('change', [ true ]);		
				$('#ratingAgency').val(val2).trigger('change', [ true ]);
				$('#district').val(val1).trigger('change',[ true ]);
				
				
				
			}
			
	
		}
	
		function saveMLIDetails() {
			//alert('save');
			document.getElementById('A').action = "/Aasha/updatetMLIDetails.html";
			document.getElementById('A').submit();
		}
	
		function resetMLIDetails() {
			//alert('Reset');
			document.getElementById('A').action = "/Aasha/mliRegistrationPage.html";
			document.getElementById('A').submit();
		}
	
		/* function exitMLIDetails() {
			alert('Exit');
			document.getElementById('A').action = "/nbfc/cgtmseMakerHome.html";
			document.getElementById('A').submit();
		} */
		function exitMLIDetails() {
			//alert('Exit');
			document.getElementById('A').action = "/Aasha/mliRegistrationPageBack.html";
			document.getElementById('A').submit();
		}
	
		function doAjaxPost4() {
			
			//alert("1 Calling");
			
			 
			var distSelected = "${mliDetail.district}";
			
			var state = document.getElementById("state").value;
			try {
				$.ajax({
					type : "POST",
					url : "getDistrictList.html",
					data : "state=" + state,
					success : function(response) {
					
						var select2 = document.getElementById('district');
						
						if (response.status == "SUCCESS") {
							document.getElementById('district').options.length = 0;
							for (var i = 0; i < response.result.length; i++) {
								option = document.createElement('option');
								option.text = response.result[i].dst_name;
								if(distSelected==option.text){
								 option.setAttribute('selected', true);
								}										
								select2.add(option);
							}
						}
					},
					error : function(e) {
						alert('Server Error : ' + e);
					}
				});
				
				
				//alert("2Calling");
	
				
			} catch (err) {
				alert('Error is : ' + err);
			}
		}
	</script>

</html>



