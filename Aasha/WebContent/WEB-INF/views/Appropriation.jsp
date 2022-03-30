<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Claim Report Form</title>

<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-3.2.1.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.11.1.js"></script>
<LINK href="<%=request.getContextPath()%>/css/stylesheet.css"
	rel="stylesheet" type="text/css">

<link href="css/jquery-ui-css.css" rel="stylesheet" type="text/css">
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<style type="text/css">
.instrumentDate{
}
</style>
<%
	String s = (String) request.getAttribute("SName");
%>
<script language="javascript">
$( document ).ready(function() {
	//alert("test2");
	$( "#myTableDiv" ).load(function() {
		//clrField();
	});
});
	function clrField(){
		alert("test1");
		$("input[name=instrumentNo]").val('');
	//	$("input[name=instrumentAmount]").val('');
		$("input[name=instrumentDate]").val('');
		$("input[name='chcktbl']:checkbox").attr('checked', false);
	}
	function submitTenureModificationReportForm(val) {
		debugger;
		if(val == '1'){	// search
			var status = false;
			var fromDate = $("#fromDate").val();
			var toDate = $("#toDate").val();   
			var bankName = $("#bankName").val();
			var payStatus = $("#paymentStatus").val();
			//alert('fromDate:'+fromDate+",toDate:"+toDate+",bankName:"+bankName+",payStatus:"+payStatus);
			
			if((fromDate == null  || fromDate.trim() === '') || (toDate == null  || toDate === '') || (bankName == null  || bankName === 'NONE') || (payStatus == null  || payStatus === 'NONE')){
				alert('Please enter mandetory fields');
				ststus=false;
			}else{
				status=true;
				//alert('status 1::'+status);
			}
			if(status){
				//alert('status2::'+status);
				document.getElementById('A').action = "/Aasha/getDataForEdit.html";
				document.getElementById("A").submit();
			}	
			//return status;
		}else if(val == '2'){ // clear
		//	alert('Clear the form');
			document.getElementById('A').action = "/Aasha/getAppropriation.html";
			document.getElementById("A").submit();
		}else if(val == '3'){ // update
			var favorite = [];
		//	alert('update the form');
            $.each($("input[name='chcktbl']:checked"), function(){
                favorite.push($(this).val());
            });
            
            var res = 0;
            var i = 0;
            if(favorite != null && favorite != 'null' && favorite != 'undifend'){
            	for (i = 0; i < favorite.length; i++) {
					var rp  = favorite[i];
					var instrumentNo = $("#instrumentNo_"+rp).val();
				//	var instrumentAmount = $("#instrumentAmount_"+rp).val();
					var instrumentDate = $("#instrumentDate_"+rp).val();
					if(instrumentNo == null  || instrumentNo == '' ){
						res = 1;
					}
					/* if(instrumentAmount == null  || instrumentAmount == '' ){
						res = 1;
					} */
					if(instrumentDate == null  || instrumentDate == '' ){
						// call method if return true than enter th bolck or false be ok
						// from date, to date and insDt
						// from date >= insDt and insDt <= to date
						if(true){
							res = 1;
						}else{
							
						}
					}
				}
            }
            if(i > 0){
	            if(res == 0){
	            	debugger;
					document.getElementById('A').action = "/Aasha/updatePaymentStatus.html";					
					document.getElementById("A").submit();
	            }else{
	            	alert("Please enter mandetory fields");
	            }
            }else{
            	alert("Plese selct atleast one row.");
            }
		}else if(val == '4'){ // data export
			alert('export to Excel form');
			document.getElementById('A').action = "/Aasha/AppropriationDownload.html";			
			document.getElementById("A").submit();
		}
	}
	//Date Picker UI
	$(function() {
		$("#mliDateOfSanctionOfExposure").datepicker({
			dateFormat : 'dd/mm/yy'
		});
		$("#toDate").datepicker({
			dateFormat : 'dd/mm/yy'//,
	        /* onSelect: function (date) {
	            $("#fromDate").datepicker("option","minDate", selected);
	        } */
		});
		$("#fromDate").datepicker({
			dateFormat : 'dd/mm/yy'//,
	       /*  onSelect: function (date) {
	            $("#toDate").datepicker("option","minDate", selected);
	        } */
		});
		$(".instrumentDate").datepicker({
			dateFormat : 'dd/mm/yy'//,
	        /* onSelect: function (date) {
	        	$("#instrumentDate").datepicker('option', 'maxDate', $("#toDate").datepicker('getDate'));
	            $("#instrumentDate").datepicker('option', 'minDate', $("#fromDate").datepicker('getDate'));
	            
	        } */
		});

	});
</script>
</head>
<body>
	<div class="main-section">
		<nav aria-label="breadcrumb">
		<ol class="breadcrumb cus-breadcrumb">
		
			<li class="breadcrumb-item"><a href="/Aasha/TenureModificationReport.html">Appropriation</a></li>
		</ol>
		</nav>
		<div class="container-fluid">
			<div>
				<div class="frm-section">
					<div class="col-md-12">
						<!-- 	<h1>MLI User Registration Form</h1> -->						
						<h5 class="notification-message"><strong>${message}</strong></h5>
						<h5 class="error1">${Errormessage}</h5>
						<div class="loader"></div>
						<form:form method="POST" action="" id="A" class="form-horizontal" >
							<%
								String userRole = (String) session.getAttribute("uRole");
							    String shbuton = (String) session.getAttribute("shbutton");
							%>
							<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">
									<label class="d-block text-purple" style="visibility: hidden";>1</label>
									<div class="col-md-6 prl-10">
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
									<label class="d-block text-purple" style="visibility: hidden";>4</label>
									<div class="col-md-6 prl-10">
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

						<%
								if (userRole.equals("CMAKER") ) {
							%>
                                <div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">
									<label class="d-block text-purple" style="visibility: hidden";>
										5</label>
								<div class="col-md-12 prl-10">
											<label>MLI Long Name : <span style="color:red">*</span></label>								
											  	<input type="text" class="form-control cus-control d-none" placeholder="MLI Long Name" >
											 	<form:select path="bankName" id="bankName" onchange="getMliShortName()" class="form-control cus-control">
												<form:option value="NONE" label="---Select---"/>
												<form:option value="All" label="All"/>	
												<form:options items="${bankName}" />	
											</form:select>
											<div id="requiredMliLongName" Class="displayErrorMessageInRedColor"></div>
										</div>
								</div>

							</div>


							<%
								}
							%>
						
						
						<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">
									<label class="d-block text-purple" style="visibility: hidden";>
										5</label>
								<div class="col-md-12 prl-10">
											<label>Payment Status : <span style="color:red">*</span></label>								
											  	<input type="text" class="form-control cus-control d-none" placeholder="Payment Status" >
											 	<form:select path="paymentStatus" id="paymentStatus" onchange="getMliShortName()" class="form-control cus-control">
												<form:option value="NONE" label="---Select---"/>
												<form:option value="All" label="All"/>
												<form:option value="I" label="I"/>
												<form:option value="R" label="R"/>													
											</form:select>
											<div id="requiredMliLongName" Class="displayErrorMessageInRedColor"></div>
										</div>
								</div>

							</div>
						

							<div class="col-md-3 col-sm-4 col-xs-12 mar-top21">
								<div class="form-group">
									<div class="d-inlineblock mt-35">
										<input type="button" value="Search" 									
											class="btn btn-reset" 	onclick="submitTenureModificationReportForm('1')" />
									</div>
									
									<div class="d-inlineblock mt-35">
										<input type="button" value="Clear" class="btn btn-reset" 										
											class="btn btn-reset" 	onclick="submitTenureModificationReportForm('2')" />
									</div>
									
									
								<%
								  if (shbuton.equals("show") ) {
								%>
									
									<div class="d-inlineblock mt-35">
										<input type="button" value="Update" class="btn btn-reset" 										
											class="btn btn-reset" 	onclick="submitTenureModificationReportForm('3')" />
									</div>
									
									<div class="d-inlineblock mt-35">
										<input type="button" value="Export" class="btn btn-reset" 										
											class="btn btn-reset" 	onclick="submitTenureModificationReportForm('4')" />
									</div>
									<%  } %>
									
								</div>
							</div>
							<div></div>
							<div id="myTableDiv">
							<table id="myTable"
								class="table table-bordered table-hover cus-table mt-10 mb-0">
								<c:if test="${!empty appropriationList}">
									<thead>
										<tr>
											<th>SR.No.</th>
											<th >MEMBER ID</th>					
											<th >NAME OF NBFC</th>
											<th >PAYMENT ID</th>
											<th >INWARD AMOUNT IN RS.</th>
											<th >INSTRUMENT_NO</th>
											<th >INSTRUMENT AMOUNT IN RS. </th>
											<th >STATUS</th>
											<th >DAN TYPE</th>																				
											<th>UTR NO.</th>
											<!-- <th>PAYMENT AMOUNT</th> -->
											<th>APPROPRIATION DATE  <span style="color: red"> * </span></th>
											<th>APPROPRIATION ALL</br><form:checkbox path="selectAll" id="select_all" onchange="selectAllCheckBox();" cssClass="select_all"/></th>
										</tr>
									</thead>									
									
									
									<%
										int counter = 0;
									%>
									<c:forEach items="${appropriationList}" var="appropriationList">
									<c:if test="${counter % 2 == 0}">
										<tr bgcolor="silver">
											<td><%=counter + 1%></td>
											<td><c:out value="${appropriationList.memberId}" /></td>
											<td><c:out value="${appropriationList.memberName}" /></td>
											<td><c:out value="${appropriationList.rpNumber}" /></td>
											<td><c:out value="${appropriationList.inwardAmount}" /></td>
											<td><c:out value="${appropriationList.virtualAccountNumber}" /></td>																			
											<td><c:out value="${appropriationList.inwardAmount}" /></td>
											<td><c:out value="${appropriationList.status}" /></td>																					
											<td><c:out value="${appropriationList.dan_type}" /></td>
											<td><form:input type="text" path="instrumentNo" value="${appropriationList.instrumentNo}" name="instrumentNo" placeholder="Instrument No" class="form-control cus-control"	id="instrumentNo_${appropriationList.rpNumber}" style="top-margine:50"/></td>										   
										    <td><form:input type="text"  path="instrumentDate" value="${appropriationList.instrumentDate}" name="instrumentDate" placeholder="dd/mm/yy" class="form-control cus-control instrumentDate"	id="instrumentDate_${appropriationList.rpNumber}" style="top-margine:50"/></td>
											<td align="center">
											
											<c:if test="${appropriationList.status=='I'}">
											<form:checkbox path="chcktbl" name="chcktbl" value="${appropriationList.rpNumber}" onchange="uncheckSelectAll();"  cssClass="chcktbl" />
											</c:if>
											<c:if test="${appropriationList.status=='R'}">
											<input type="checkbox"  disabled="disabled"/>
											</c:if>
											</td>									
											<%
												counter += 1;
											%>
										</tr>
										</c:if>
									</c:forEach>	
									
								</c:if>
							</table>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="js/jquery-1.10.2.js" type="text/javascript"></script>
	<div class="loader"></div>
	<script type="text/javascript">
		$(window).load(function() {
			$(".loader").fadeOut("slow");
			
		});
	</script>
</body>







<script type="text/javascript">



function clickToInitiate(){
	debugger;

	$selectedCBs = $('.danRpDataTable input[type="checkbox"]:checked');
	
    if ($selectedCBs.length === 0) {
        alert("Please select the record for approval.");
        //return;
    }else{    //	var accept=document.getElementById('accept').value;
    	//var reject=document.getElementById('reject').value;
 	
    	// alert("selected row size is "+$selectedCBs.length);
    	 var approveCount;
    	 	if (document.getElementById('select_all').checked == true) {   
        	 	//alert("hellow ",$selectedCBs.length-1); 	       
    		 approveCount=$selectedCBs.length-1;
    	    } else {    	       
    	    	approveCount=$selectedCBs.length;    	    	
    	    }
    	 	
    	 	var hiddenValue=document.getElementById('approveCount').value=approveCount;
    	 	//alert("hellow "+hiddenValue); 
    		document.getElementById('A').action = "/Aasha/saveAppropriationData.html";
    		document.getElementById('A').submit();
    	
	
    }
}

function selectAllCheckBox() {
    if (document.getElementById('select_all').checked == true) {
        $('.chcktbl').each(function() {
            this.checked = true;
        });
        $selectedCBs = $('.danRpDataTable input[type="checkbox"]:checked');
    	var count=$selectedCBs.length-1;
    	document.getElementById('count').innerHTML=count;
    } else {
        $('.chcktbl').each(function() {
            this.checked = false;
        });
        $selectedCBs = $('.danRpDataTable input[type="checkbox"]:checked');
    	var count=$selectedCBs.length;
    	document.getElementById('count').innerHTML=count;
    }

}
function uncheckSelectAll()
{
	$('.chcktbl').click(function () {
        if (this.checked == false) {
            $('#select_all').attr('checked', false);
        }
        $selectedCBs = $('.danRpDataTable input[type="checkbox"]:checked');
    	var count=$selectedCBs.length;
    	document.getElementById('count').innerHTML=count;
    });
	$selectedCBs = $('.danRpDataTable input[type="checkbox"]:checked');
	var count=$selectedCBs.length;
	document.getElementById('count').innerHTML=count;
	
}


</script>







</html>