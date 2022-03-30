<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@	taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
		<title> TenureModification Form</title>
		<link href="css/jquery-ui-css.css" rel="stylesheet" type="text/css">
		<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<LINK href="<%=request.getContextPath()%>/css/stylesheet.css" rel="stylesheet" type="text/css">
		<title>Update Recovery Details</title>
		<script type="text/javascript">
		/* function toggleIcon(e) {
			debugger;
		    $(e.target)
		        .prev('.panel-heading')
		        .find(".more-less")
		        .toggleClass('fa-plus fa-minus');
		}
		$('.panel-group').on('hidden.bs.collapse', toggleIcon);
		$('.panel-group').on('shown.bs.collapse', toggleIcon); */
		
			$(function() {
				$("#dateOfNotice").datepicker({
					// showAnim : "fold"
					dateFormat : "dd/mm/yy"
				});
			});
		
			$(function() {
				$("#dateOfSarfaesi").datepicker({
					// showAnim : "fold"
					dateFormat : "dd/mm/yy"
				});
			});
		
			$(function() {
				$("#dateOfSubsidy").datepicker({
					// showAnim : "fold"
					dateFormat : "dd/mm/yy"
				});
			});

		</script>
	</head>
	<body onload="clearField()">
		<div class="main-section">
			<div class="frm-section">
				<div class="col-md-12">
					<form:form method="POST" action="TenureModificationInputForm.html" class="form-horizontal" id="exposureMasterMakerDetailsFormId">
                 <h5 class="error1 mtb-0">
				 	<strong>${message}</strong>
			  </h5>
	
	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
  <div class="panel panel-default cus-pnl cus-pnl">
    <div class="panel-heading cus-pheading cus-pheading" role="tab" id="headingOne">
      <h4 class="panel-title cus-ptitle cus-ptitle">
        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
        	Modification of Tenure Request:
        	<i class="fa fa-chevron-circle-down" aria-hidden="true"></i>
        	<!-- <span><img src="images/plus.png"  style="width:18px;" class="more-less plus" alt=""></span>
        <span><img src="images/negative.png"  style="width:18px;" class="more-less minus" alt=""></span> -->
        </a>
      </h4>
    </div>	<!-- panel-head -->
      <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
      <div class="panel-body">
    <div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
			<div class="form-group">
				<div class="col-md-12 prl-10">
					<label>Member Id :</label>
					<form:input path="mliID" value="${memberId}" size="20" style="text-align: right" id="memberId" class="form-control cus-control" readonly="true"/>
				</div>
			</div>
		</div>
		
		<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
			<div class="form-group">
				<div class="col-md-12 prl-10">
					<label>Enter CGPAN :</label>
					<form:input path="cgpan" value="" size="20" id="cgpan" class="form-control cus-control" />
				</div>
			</div>
		</div>
		<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
			<div class="form-group">
				<div class="col-md-12 prl-10">
				
					<input type="submit" value="Submit"   class="btn btn-reset mt-15" />			
				</div>
			</div>
		</div>
      </div>
    </div>
  </div> <!-- panel -->
  
   <div class="panel panel-default cus-pnl">
    <div class="panel-heading cus-pheading" role="tab" id="headingOne">
      <h4 class="panel-title cus-ptitle">
        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="true" aria-controls="collapseOne">
        	2. Tenure Return By NBFC Checker.
        	<i class="fa fa-chevron-circle-down" aria-hidden="true"></i>
        	<!-- <span><img src="images/plus.png"  style="width:18px;" class="more-less plus" alt=""></span>
        <span><img src="images/negative.png"  style="width:18px;" class="more-less minus" alt=""></span> -->
        </a>
      </h4>
    </div>	<!-- panel-head -->
      <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
      <div class="panel-body">
      
     	<div class="tbl-details">
			<div class="col-md-12">
				<div class="d-inlineblock float-right "></div>
				<table id="myTable"  class="table table-bordered table-hover cus-table mt-10 mb-0">
					<c:if test="${!empty TenureReturnedRecordsList}">
						<thead>
						<tr>
							<th>Sr.No.</th>
							<th>CGPAN</th>
							<th>Original Tenure.</th>
							<th>Revised Tenure</th>
							<th>Revised Expiry Date</th>
							<th>Modification Remarks</th>
							<th>Return Remarks</th>
							<th>Status</th>
							<th>Decision</th>
						</tr>
			
						</thead>
						<% int counter=0;%>
	
						<c:forEach items="${TenureReturnedRecordsList}" var="TenureReturnedRecordsList">
								<tr <% if(counter%2==0){%>bgcolor="silver" <%}%>>
									<td><%=counter+1%></td>
									<td>${TenureReturnedRecordsList.cgpan}</td> 
									<td>${TenureReturnedRecordsList.tenure}</td>
									<td>${TenureReturnedRecordsList.reviseTenure}</td>  
									<td>${TenureReturnedRecordsList.reviseExpirydate}</td>
									<td>${TenureReturnedRecordsList.modificationRemark} </td>
										<td>${TenureReturnedRecordsList.returnRemark} </td>
									<td>${TenureReturnedRecordsList.status} </td>
							
									<td align="center" class="btn-edit">
										<a href="/Aasha/TenureDataForModification.html?TenId=${TenureReturnedRecordsList.tId}" class="btn-edit">Edit</a> 
									
									</td>																	
								</tr>
								 <%counter+=1;%>
						</c:forEach>
					</c:if>
					<c:if test="${empty TenureReturnedRecordsList}">
						<thead>
							<tr>
								<th>Sr.No.</th>
							<th>CGPAN</th>
							<th>Original Tenure.</th>
							<th>Revised Tenure</th>
							<th>Revised Expiry Date</th>
							<th>Modification Remarks</th>
							<th>Return Remarks</th>
							<th>Status</th>
							<th>Decision</th>
							</tr>
						</thead>
						<tr>Record Not Found</tr>
					</c:if>
				</table>
			</div>	
		</div>	
      </div>
    </div>
  </div> <!-- panel -->
    
</div><!-- panel-group -->									
 	
	</form:form>
	</div>
	
</div>
</div>

	

	</body>
</html>
