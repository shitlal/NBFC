<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='spring' uri='http://www.springframework.org/tags'%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String sN=(String)request.getAttribute("sName");
    String expLim=(String)request.getAttribute("eXposureLimit");
%>
<html>
	<head>
		<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
		<title>MLI Registration</title>
		<script src="js/jquery-1.11.1.js" type="text/javascript"></script>
		<link href="css/jquery-ui-css.css" rel="stylesheet" type="text/css">
		<style type="text/css"></style>
		<script>
			$(function() {
		
				$("#ratingDate").datepicker({
					dateFormat : 'dd/mm/yy'
				});
		
			});
		</script>
		
		<script type="text/javascript">
			function updateMLIDetails() {
		
		 		/*var dateString = document.getElementById("ratingDate").value;
				var elem1 = dateString.split('/');
				var day = elem1[0];
				alert(day);
				var month = elem1[1];
				alert(month);
				var year = elem1[2];
				alert(year);
				
				var today = new Date();
				var currentDate = today.getDate() + '/' + today.getMonth() + 1 + '/'+ today.getFullYear();
				alert(currentDate);
				
		
				if ((mydate) < Date.now()) {
					alert("greater");
				} else {
					alert("smaller")
				} */
		 
				document.getElementById('A').action = "/Aasha/updateNewMLIDetails.html";
			     document.getElementById('A').submit();
		
			}
		
			function resetMLIDetails() {
				//alert('Reset');
				document.getElementById('A').reset();
				document.getElementById('A').submit();
			}
		
			/* function exitMLIDetails() {
				alert('Exit');
				document.getElementById('A').action = "/nbfc/cgtmseMakerHome.html";
				document.getElementById('A').submit();
			}
			 */
			function exitMLIDetails() {
				//alert('Exit');
				document.getElementById('A').action = "/Aasha/mliRegistrationPageBack.html";
				document.getElementById('A').submit();
			}
		
			function doAjaxPost4() {
				var state = document.getElementById("state").value;
				try {
					$.ajax({
						type : "POST",
						url : "getNewDistrict.html",
						data : "state=" + state,
						success : function(response) {
							var select2 = document.getElementById('district');
							if (response.status == "SUCCESS") {
								document.getElementById('district').options.length = 0;
								for (var i = 0; i < response.result.length; i++) {
									option = document.createElement('option');
									option.text = response.result[i].dst_name;
									select2.add(option);
									
									
								}
							}
						},
						error : function(e) {
							alert('Server Error : ' + e);
						}
					});
		
				} catch (err) {
					alert('Error is : ' + err);
				}
			}
			
			
		</script>

	</head>

	<body onload="clearField()">
		<div class="main-section">
			<div class="container-fluid">
				<div>
					<nav aria-label="breadcrumb">
					<ol class="breadcrumb cus-breadcrumb">
						<li class="breadcrumb-item"><a
							href="/Aasha/mliRegistrationPage.html">MLI Registration</a></li>
						<li class="breadcrumb-item active" aria-current="page">New MLI
							Registration</li>
					</ol>
					</nav>
					<form:form method="GET" id="A" class="form-horizontal">
						<div class="frm-section">
							<div class="col-md-12">
								<h2 align="center" class="heading">New MLI Registration</h2>
								<h5 class="notification-message">
									<strong>${message}</strong>
								</h5>
								<h5 class="error1">
									<strong>${error}</strong>
								</h5>
								<h4 class="detail_sp1">MLI Basic Detail</h4>
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="longName">Long Name <span
													style="color: red;">*</span>
											</form:label>
											<form:input path="longName" size="28"
												placeholder="e.g:ABC Steel Company"
												class="form-control cus-control" />
											<form:input path="" size="28" type="hidden" />
											<form:errors path="longName" Class="error" />
											<span Class="error">${longNameerror}</span>
										</div>
									</div>
								</div>
	
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="shortName">Short Name <span
													style="color: red;">*</span>
											</form:label>
											<form:input path="shortName" size="28"
												placeholder="e.g:ABC Steel " class="form-control cus-control" />
											<form:errors path="shortName" cssClass="error" />
											<span Class="error">${shortNameerror}</span>
										</div>
									</div>
								</div>
	
								<div class="col-md-6 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="companyAddress">Registered Address<span
													style="color: red;">*</span>
											</form:label>
											<form:input path="companyAddress" size="28"
												class="form-control cus-control"
												placeholder="e.g:1402 9 10,AJA Compound,Bharat Nagar,Mumbai-400007 " />
											<form:errors path="companyAddress" cssClass="error" />
											<form:input path="" size="28" type="hidden" />
	
										</div>
									</div>
								</div>
	
								<div class="clearfix"></div>
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="state">State <span
													style="color: red;">*</span>
											</form:label>
											<form:select path="state" id="state" class="form-control cus-control" onchange="doAjaxPost4()">
												<form:option value="" label="--Select State--" />
												<c:forEach var="state" items="${stateList}">
													<form:option id="${state.ste_code}"
														value="${state.ste_code}">${state.ste_name}</form:option>
												</c:forEach>
											</form:select>
											<form:errors path="state" cssClass="error" />
											<form:input path="" size="28" type="hidden" />
	
										</div>
									</div>
								</div>
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="district">District <span
													style="color: red;">*</span>
											</form:label>
											<form:select id="district" path="district"
												class="form-control cus-control">
												<form:option value="" label="--Select District--" />
												<!--<form:option id="${district}" value="${district}">${district}</form:option>-->
											</form:select>
											<form:errors path="district" cssClass="error" />
											<form:input path="" size="28" type="hidden" />
										</div>
									</div>
								</div>
	
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="city">City <span
													style="color: red;">*</span>
											</form:label>
											<form:input path="city" size="28"
												class="form-control cus-control" placeholder="e.g:Mumbai" />
											<form:errors path="city" cssClass="error" />
											<form:input path="" size="28" type="hidden" />
										</div>
									</div>
								</div>
	
	
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="pincode">Pincode <span
													style="color: red;">*</span>
											</form:label>
											<form:input path="pincode" size="28" maxlength="6"
												class="form-control cus-control" placeholder="e.g:421405" />
											<form:errors path="pincode" cssClass="error" />
											<form:input path="" size="28" type="hidden" />
										</div>
									</div>
								</div>
								<div class="clearfix"></div>
								<h4 class="detail_sp1">MLI Contact Details</h4>
	
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="landlineNumber" class="d-block"> Land Line Number
											</form:label>
											<form:input path="phoneCode"
												class="form-control cus-control  w-28 d-inline" size="3"
												maxlength="3" placeholder="e.g:022" />
											<form:input path="landlineNumber" size="20"
												class="form-control cus-control w-69 d-inline" maxlength="8"
												placeholder="e.g:67834562" />
											<form:errors path="phoneCode" cssClass="error" />
											<form:errors path="landlineNumber" cssClass="error" />
											<form:input path="" size="28" type="hidden" />
										</div>
									</div>
								</div>
	
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="emailId">Official Email Id <span
													style="color: red;">*</span>
											</form:label>
											<form:input path="emailId" size="28"
												placeholder="akshay_kumar@xxxx.com"
												class="form-control cus-control" />
											<form:errors path="emailId" cssClass="error" />
											<form:input path="" size="28" type="hidden" />
										</div>
									</div>
								</div>
	
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="contactPerson">Contact Person <span
													style="color: red;">*</span>
											</form:label>
											<form:input path="contactPerson" size="28"
												class="form-control cus-control"
												placeholder="e.g:Mr Akshay Kumar " />
											<form:errors path="contactPerson" cssClass="error" />
										</div>
									</div>
								</div>
	
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="mobileNUmber">Mobile No.<span
													style="color: red;">*</span>
											</form:label>
											<form:input path="mobileNUmber" size="28" maxlength="10"
												class="form-control cus-control"
												placeholder="e.g:9890190283 " />
											<form:errors path="mobileNUmber" cssClass="error" />
										</div>
									</div>
								</div>
	
								<!-- <div class="col-md-3 col-sm-4 col-xs-12 d-none">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="faxNumber" class="d-block">Fax Number</form:label>
											<form:input path="faxCode" size="3" maxlength="3"
												class="form-control cus-control  w-28 d-inline"
												placeholder="e.g:022" />
											<form:input path="faxNumber" size="20" maxlength="8"
												class="form-control cus-control  w-69 d-inline"
												placeholder="e.g:658322182 " />
											<form:errors path="faxNumber" cssClass="error" />
										</div>
									</div>
								</div> -->
	
								<div class="clearfix"></div>
								<h4 class="detail_sp2">MLI Registration Detail</h4>
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="rbiReggistrationNumber">RBI Registration No. <span
													style="color: red;">*</span>
											</form:label>
											<form:input path="rbiReggistrationNumber" size="28"
												class="form-control cus-control" maxlength="10"
												placeholder="e.g:N-09.00425" />
											<form:errors path="rbiReggistrationNumber" cssClass="error" />
											<span Class="error">${rbiREGerror}</span>
	
										</div>
									</div>
								</div>
	
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="companyCIN">CIN of the Company <span
													style="color: red;">*</span>
											</form:label>
											<form:input path="companyCIN" size="28" maxlength="21"
												class="form-control cus-control"
												placeholder="e.g:U67190MH2001PLC227379" />
											<form:errors path="companyCIN" cssClass="error" />
											<span Class="error">${companyCINNoGerror} </span>
										</div>
									</div>
								</div>

								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="companyPAN">PAN of the Company<span
													style="color: red;">*</span>
											</form:label>
											<form:input path="companyPAN" size="28" maxlength="10"
												class="form-control cus-control" placeholder="e.g:AACCK6354L" />
											<form:errors path="companyPAN" cssClass="error" />
											<span Class="error">${panOFCompanyerror}</span>
										</div>
									</div>
								</div>
	
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="gstinNumber">GSTIN No.<span
													style="color: red;">*</span>
											</form:label>
											<form:input path="gstinNumber" size="28"
												class="form-control cus-control" maxlength="15"
												placeholder="e.g:26AAACB1534F1Z8" />
											<form:errors path="gstinNumber" cssClass="error" />
											<span Class="error">${gstinNOerror}</span>
										</div>
									</div>
								</div>
	
								<div class="clearfix"></div>
								<h4 class="detail_sp3">Rating Detail</h4>
	
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="ratingAgency">Rating Agency<span
													style="color: red;">*</span>
											</form:label>
											<form:select path="ratingAgency"
												class="form-control cus-control" id="ratingAgency">
												<form:option value="" label="---Select Rating Agency---" />
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
											<form:label path="rating">Credit Rating<span
													style="color: red;">*</span>
											</form:label>
											<form:select path="rating" id="rating"
												class="form-control cus-control">
												<form:option value="" label="--Select Rating--" />
												<c:forEach var="ratingAgency" items="${ratingAgencyList}">
													<form:option id="${ratingAgency.mliReating}"
														value="${ratingAgency.mliReating}">${ratingAgency.mliReating}</form:option>
												</c:forEach>
											</form:select>
											<form:errors path="rating" cssClass="error" />
										</div>
									</div>
								</div>
	
								<div class="col-md-3 col-sm-4 col-xs-12">
									<div class="form-group">
										<div class="col-md-12 prl-10">
											<form:label path="ratingDate">Date of Rating<span
													style="color: red;">*</span>
											</form:label>
											<form:input path="ratingDate"
												class="date-picker form-control cus-control" size="28"
												placeholder="e.g:dd/mm/yyyy" id="ratingDate" />
											<form:errors path="ratingDate" cssClass="error" />
											<span Class="error">${ratDateMsg}</span>
										</div>
									</div>
								</div>
	
								<div class="d-inlineblock mt-25">
									<input type="submit" value="Save" class="btn btn-reset"
										onclick="updateMLIDetails()" /> <input type="submit"
										value=" Reset " onclick="resetMLIDetails()"
										class="btn btn-reset" /> <input type="submit" value=" Close "
										onclick="exitMLIDetails()" class="btn btn-reset" />
								</div>
	
							</div>
						</div>
	
					</form:form>
				</div>
			</div>
		</div>
	</body>

</html>



