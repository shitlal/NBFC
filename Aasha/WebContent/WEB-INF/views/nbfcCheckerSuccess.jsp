<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="<%=request.getContextPath()%>/css/stylesheet.css"
	rel="stylesheet" type="text/css">
<!-- <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<script src="js/jquery-1.10.2.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script> -->
<script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>


<title>NBFC Checker Success</title>
<style type="text/css">
#body-section{	background-color:white !important;	  padding-bottom: 0px !important;	height: auto !important;    }
.footer1{	margin: 0 0 0 0 !important; }
</style>
</head>
<body>

<div class="main-section" >

	<!-- <div> -->		
	<div id="wel-sec">
		<div class="container">
 			<div class="row">
 <div class="col-md-4">
 		<div class="d-box bg-greensead" id="btn-asf">
 			<h3>ASF DUE FILES</h3>
 			<p><span>${TotalASFDueCountKey}</span> Pending</p>
 			<i class="fa fa-file-text" aria-hidden="true"></i>
 		</div>
 		<!--<div class="d-box bg-blued" id="btn-mliexposure">
 			<h3>MLI-Wise Exposure Details</h3>
 			<p><span>05</span> EXPIRED</p>
 			<i class="fa fa-file-text" aria-hidden="true"></i>
 		</div>
 		<div class="d-box bg-promogranted" id="btn-mlibatches" onClick="window.open('cgtmseCheckerBatchUploadsPendingForApprovalRM.html?paramID=${statusCMAKey}', '_blank')"> 
 			<h3>No. of Batch Uploads pending for approval</h3>
 			<p><span>${statusCMACountKey}</span>  </p>
 			<i class="fa fa-file-text" aria-hidden="true"></i>
 		</div>
 		<div class="d-box bg-promogranted" id="btn-mlibatches" onClick="window.open('cgtmseCheckerBatchUploadsPendingForApprovalRM.html?paramID=${statusCMAKey}', '_blank')"> 
 			<h3>No. of Gurantee fee generation pending for approval</h3>
 			<p><span>0</span>  </p>
 			<i class="fa fa-file-text" aria-hidden="true"></i>
 		</div>-->
 		 </div>
 	<div class="col-md-8">
	
		<div class="wel-sec">
		<h1>	Welcome To CGTMSE-UDAAN	</h1>
			<p>Credit Guarantee Fund Trust for Micro and Small
								Enterprises (CGTMSE) has been set up jointly by Ministry of
								MSME, Government of India and Small Industries Development Bank
								of India (SIDBI) to catalyze flow institutional credit to Micro
								Small Enterprises (MSEs). Over the past 18 years, CGTMSE has
								been instrumental in providing guarantee cover amounting to
								<a style="color: black">&#8377;</a>1.76 lakh crore covering 34 lakh guarantees extended by
								eligible Member Lending Institutions [MLIs] to MSEs. Pursuant to
								announcement made by the Hon<a style="color: black">&#700;</a>ble Prime Minister, CGTMSE has
								framed a new Credit Guarantee Scheme(CGS-II) for NBFCs as a
								measure towards hassle free access to credit by MSE sector.<br>
																		
			</p>
		
		</div>	
		  
			
	</div>
	<div class="clearfix"></div>
	<div class="col-md-6  col-sm-12 col-xs-12">
		<table class="table table-bordered table-hover cus-table mt-10 mb-0">
		<thead>
			<tr>
				<td colspan=5 align=center style="font-size: 17px !important;"><strong>User
						Dashboard</strong></td>
			</tr>
			<tr>
				<c:forEach items="${rows[0]}" var="column">
					<th class="tableHeader1"><c:out value="${column.key}" /></th>
				</c:forEach>
			</tr>
		</thead> 
			<tbody>
				<c:forEach items="${rows}" var="columns">
					<tr>
						<c:forEach items="${columns}" var="column">
							<td align="center"><c:out value="${column.value}" /></td>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
	</table>  
		
	</div>
		<div class="col-md-6 col-sm-12 col-xs-12" id="asf"  style="display:none">
	<div class="tbl-details"> 
					<div class="d-inlineblock float-right "></div>
					<table id="myTable2"  class="table table-bordered table-hover cus-table mt-10 mb-0">
			<thead>
				<tr><td colspan=7 align=center style="font-size:17px !important;"><strong>Annual Service Fee details(ASF DUE FILES)</strong></td>
				<tr><td colspan=7 align=center style="font-size:12px !important;">Note -> While uploading the file No capitalization of EMI/Interest/further Interest/Other charges etc is to be added to the principal outstanding amount in ASF outstanding amount</td>
				
					</tr>
			<tr>
				   <th>SR.NO.</th>
				    <th>MLI NAME</th>
					<th>PORTFOLIO NAME</th>
					<!-- <th>FILE ID</th> -->
					<th>Validity Start Dt</th>
					<th>Validity End Dt</th>
					<th>DOWNLOAD</th> 
				</tr>
				</thead>
				<tbody>
				<c:if test="${!empty danDetailList}">
	  <% int counter=0;%>
				<c:forEach items="${danDetailList}" var="mliList" varStatus="loopStatus">
					<tr <% if(counter%2==0){%>bgcolor="silver" <%}%>>
					<td><%=counter+1%></td>
					<td><c:out value="${mliList.mli_Name}" /></td>
						<td><c:out value="${mliList.portfolioName}" /></td>
				<%-- 		<td><c:out value="${mliList.fileId}" /></td> --%>
						<td><c:out value="${mliList.from_date}" /></td>
						<td><c:out value="${mliList.to_date}" /></td>
						
						<td><a href="exportModifyOutstandingAmountDetails.html?PORTFOLIO_NAME=${mliList.portfolioName}">Download Excel File </a></td>
					   	<%  counter+=1;%></tr>
				</c:forEach>
				</c:if>
			    													
			</tbody>				
		</table>	
		 
			</div>	
</div>
  
		</div>
</div>	
</div>
		
<!-- <div id="feature-sec">
		<div class="container">
		 <div class="row">		 
				<div class="col-md-12">
					
					<div class="feature-sec">
						<h3>Silent Features of Credit Guarantee Scheme For NBFCs (CGS-II)</h3>
				</div>
				   </div>				
		<div class="col-md-6">
		<div class="accordion mt-25" id="myAccordion">
			<div class="panel cus-panel mb-0">
			 <div class="faq-header" id="faqHeading-1">     
                         <h5 class="faq-title" style="text-align:center;     font-size: 20px;">Eligibility Criteria</h5>
                   </div>
               </div>
    <div class="panel cus-panel mb-0">
        <button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">Types of NBFCs</button>        
        <div class="faq-header" id="faqHeading-1">             
                 <h5 class="faq-title" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">
                     <span class="badge">1</span>Types of NBFCs	
                 </h5>             
         </div>        
        <div id="collapsible-1" class="collapse">
       	 <div class="cus-panelbody">
            <p>-	Systemically Important NBFCs. Profit making for last three years.
				Credit Rating: BBB& above 	</p>
			</div>		
        </div>
    </div>
     <div class="panel cus-panel mb-0">
        <button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">Types of NBFCs</button>        
        <div class="faq-header" id="faqHeading-1">             
                 <h5 class="faq-title" data-toggle="collapse" data-target="#collapsible-2" data-parent="#myAccordion">
                     <span class="badge">2</span>Credit Rating:
                 </h5>             
         </div>        
        <div id="collapsible-2" class="collapse">
       	 <div class="cus-panelbody">
            <p>- BBB & above 	</p>
			</div>		
        </div>
    </div>
    <div class="panel cus-panel mb-0">
        <button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">Types of NBFCs</button>        
        <div class="faq-header" id="faqHeading-1">             
                 <h5 class="faq-title" data-toggle="collapse" data-target="#collapsible-3" data-parent="#myAccordion">
                     <span class="badge">3</span>Lendding to MSEs :
                 </h5>             
         </div>        
        <div id="collapsible-3" class="collapse">
       	 <div class="cus-panelbody">
            <p>- Experence of minimum 5 years having MSE loan portfolio of at least 100 crore	</p>
			</div>		
        </div>
    </div>
    <div class="panel cus-panel mb-0">
        <button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">Types of NBFCs</button>        
        <div class="faq-header" id="faqHeading-1">             
                 <h5 class="faq-title" data-toggle="collapse" data-target="#collapsible-4" data-parent="#myAccordion">
                     <span class="badge">4</span>Net NPA Level :
                 </h5>             
         </div>        
        <div id="collapsible-4" class="collapse">
       	 <div class="cus-panelbody">
            <p>- less then 5%	</p>
			</div>		
        </div>
    </div>
    <div class="panel cus-panel mb-0">
        <button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">Types of NBFCs</button>        
        <div class="faq-header" id="faqHeading-1">             
                 <h5 class="faq-title" data-toggle="collapse" data-target="#collapsible-5" data-parent="#myAccordion">
                     <span class="badge">5</span>Average Recovery Ratio :
                 </h5>             
         </div>        
        <div id="collapsible-5" class="collapse">
       	 <div class="cus-panelbody">
            <p>- Over 90% for the MSE portfolio</p>
			</div>		
        </div>
    </div>
   
</div>
</div>

<div class="col-md-6">
		<div class="accordion mt-25" id="myAccordion2">
			<div class="panel cus-panel mb-0">
			 <div class="faq-header" id="faqHeading-2">     
                         <h5 class="faq-title" style="text-align:center;     font-size: 20px;">Feaatures of CGS-II</h5>
                   </div>
               </div>
    <div class="panel cus-panel mb-0">
        <button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">Types of NBFCs</button>        
        <div class="faq-header" id="faqHeading-2">             
                 <h5 class="faq-title" data-toggle="collapse" data-target="#collapsible-6" data-parent="#myAccordion2">
                     <span class="badge">1</span>Guarantee Type :
                 </h5>             
         </div>        
        <div id="collapsible-6" class="collapse">
       	 <div class="cus-panelbody">
            <p>- portFolio based Guarantee
				- Each portfolio build-up would be on quarterly basis	</p>
			</div>		
        </div>
    </div>
     <div class="panel cus-panel mb-0">
        <button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">Types of NBFCs</button>        
        <div class="faq-header" id="faqHeading-2">             
                 <h5 class="faq-title" data-toggle="collapse" data-target="#collapsible-7" data-parent="#myAccordion2">
                     <span class="badge">2</span>Eligible borrowers for coverage under CGS II :
                 </h5>             
         </div>        
        <div id="collapsible-7" class="collapse">
       	 <div class="cus-panelbody">
            <p>- New and existing Micro and Small Enterprises (MSE) in manufacturing, service and retail trade segment. 	</p>
			</div>		
        </div>
    </div>
    <div class="panel cus-panel mb-0">
        <button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">Types of NBFCs</button>        
        <div class="faq-header" id="faqHeading-2">             
                 <h5 class="faq-title" data-toggle="collapse" data-target="#collapsible-8" data-parent="#myAccordion2">
                     <span class="badge">3</span>Coverage :
                 </h5>             
         </div>        
        <div id="collapsible-8" class="collapse">
       	 <div class="cus-panelbody">
             <p>- collaterial free and third party guarantee free loans.
				- Partially collateralised loans
				- Unsecured bussiness loans	</p>
			</div>		
        </div>
    </div>
    <div class="panel cus-panel mb-0">
        <button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">Types of NBFCs</button>        
        <div class="faq-header" id="faqHeading-2">             
                 <h5 class="faq-title" data-toggle="collapse" data-target="#collapsible-9" data-parent="#myAccordion2">
                     <span class="badge">4</span>Loan amount :
                 </h5>             
         </div>        
        <div id="collapsible-9" class="collapse">
       	 <div class="cus-panelbody">
           <p>- Upto 200 lakh extended to the MSEs (For retails trade it is upto 100lakh)</p>
			</div>		
        </div>
    </div>
    <div class="panel cus-panel mb-0">
        <button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">Types of NBFCs</button>        
        <div class="faq-header" id="faqHeading-2">             
                 <h5 class="faq-title" data-toggle="collapse" data-target="#collapsible-10" data-parent="#myAccordion2">
                     <span class="badge">5</span>Extent of coverage :
                 </h5>             
         </div>        
        <div id="collapsible-10" class="collapse">
       	 <div class="cus-panelbody">
           <p>- Upto 75% (option of 50% (or) 60% (or) 75% available) of the amount in default.</p>
			</div>		
        </div>
    </div>
    <div class="panel cus-panel mb-0">
        <button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">Types of NBFCs</button>        
        <div class="faq-header" id="faqHeading-2">             
                 <h5 class="faq-title" data-toggle="collapse" data-target="#collapsible-11" data-parent="#myAccordion2">
                     <span class="badge">6</span>Interest Rate Cap on Loans :
                 </h5>             
         </div>        
        <div id="collapsible-11" class="collapse">
       	 <div class="cus-panelbody">
              <p>- Fullfilling RBI guidleines or any other rate as prescribed by the Trust frin time to time.</p>
			</div>		
        </div>
    </div>
   
</div>
</div>		col-6 


   																		

					
					
				</div>
				
		</div>	row 
	</div>		feature-section
	-->	
</div> 	<!-- main-section -->
						
					<!--  <div class="col-md-6 prl-5">
                        <div class="faq-title text-center pb-3">
                            <h2>Eligibility Criteria</h2>
                        </div>
                   
                    <div class="col-md-12 prl-0">
                        <div class="faq" id="accordion">
                         <div class="panel">
						        <button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#collapsible-1" data-parent="#myAccordion">Question 1?</button>
						        <div id="collapsible-1" class="collapse">
						            ..
						        </div>
						    </div>
    
                            <div class="card">
                                <div class="card-header" id="faqHeading-1">
                                    <div class="mb-0">
                                        <h5 class="faq-title" data-toggle="collapse" data-target="#faqCollapse-1" data-aria-expanded="true" data-aria-controls="faqCollapse-1">
                                            <span class="badge">1</span>Types of NBFCs	
                                        </h5>
                                    </div>
                                </div>
                                <div id="faqCollapse-1" class="collapse" aria-labelledby="faqHeading-1" data-parent="#accordion">
                                    <div class="card-body">
                                        <p>-	Systemically Important NBFCs. Profit making for last three years.
Credit Rating: BBB& above 	</p>
                                    </div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-header" id="faqHeading-2">
                                    <div class="mb-0">
                                        <h5 class="faq-title" data-toggle="collapse" data-target="#faqCollapse-2" data-aria-expanded="false" data-aria-controls="faqCollapse-2">
                                            <span class="badge">2</span> Credit Rating:
                                        </h5>
                                    </div>
                                </div>
                                <div id="faqCollapse-2" class="collapse" aria-labelledby="faqHeading-2" data-parent="#accordion">
                                    <div class="card-body">
                                        <p>-BBB & above</p>
                                    </div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-header" id="faqHeading-3">
                                    <div class="mb-0">
                                        <h5 class="faq-title" data-toggle="collapse" data-target="#faqCollapse-3" data-aria-expanded="false" data-aria-controls="faqCollapse-3">
                                            <span class="badge">3</span>Lendding to MSEs :
                                        </h5>
                                    </div>
                                </div>
                                <div id="faqCollapse-3" class="collapse" aria-labelledby="faqHeading-3" data-parent="#accordion">
                                    <div class="card-body">
                                        <p>-Experence of minimum 5 years having MSE loan portfolio of at least 100 crore</p>
                                    </div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-header" id="faqHeading-4">
                                    <div class="mb-0">
                                        <h5 class="faq-title" data-toggle="collapse" data-target="#faqCollapse-4" data-aria-expanded="false" data-aria-controls="faqCollapse-4">
                                            <span class="badge">4</span> Net NPA Level :
                                        </h5>
                                    </div>
                                </div>
                                <div id="faqCollapse-4" class="collapse" aria-labelledby="faqHeading-4" data-parent="#accordion">
                                    <div class="card-body">
                                        <p>-less then 5%</p>
                                    </div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-header" id="faqHeading-4">
                                    <div class="mb-0">
                                        <h5 class="faq-title" data-toggle="collapse" data-target="#faqCollapse-4" data-aria-expanded="false" data-aria-controls="faqCollapse-4">
                                            <span class="badge">5</span> Average Recovery Ratio :
                                        </h5>
                                    </div>
                                </div>
                                <div id="faqCollapse-4" class="collapse" aria-labelledby="faqHeading-4" data-parent="#accordion">
                                    <div class="card-body">
                                        <p>-Over 90% for the MSE portfolio</p>
                                    </div>
                                </div>
                            </div>
                            
                           
                            </div>
                        </div>
                         </div>	col-6   -->
                         
                        <!--  <div class="col-md-6 prl-5">
                        <div class="faq-title text-center pb-3">
                            <h2>Features of CGS-II</h2>
                        </div>
                   
                    <div class="col-md-12 prl-0">
                        <div class="faq" id="accordion">
                            <div class="card">
                                <div class="card-header" id="faqHeading-5">
                                    <div class="mb-0">
                                        <h5 class="faq-title" data-toggle="collapse" data-target="#faqCollapse-5" data-aria-expanded="true" data-aria-controls="faqCollapse-1">
                                            <span class="badge">1</span>Guarantee Type :
                                        </h5>
                                    </div>
                                </div>
                                <div id="faqCollapse-5" class="collapse" aria-labelledby="faqHeading-5" data-parent="#accordion">
                                    <div class="card-body">
                                        <p>-	portFolio based Guarantee</p>
                                        <p>-Each portfolio build-up would be on quarterly basis</p>
                                    </div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-header" id="faqHeading-6">
                                    <div class="mb-0">
                                        <h5 class="faq-title" data-toggle="collapse" data-target="#faqCollapse-6" data-aria-expanded="false" data-aria-controls="faqCollapse-2">
                                            <span class="badge">2</span> Eligible borrowers for coverage under CGS II :
                                        </h5>
                                    </div>
                                </div>
                                <div id="faqCollapse-6" class="collapse" aria-labelledby="faqHeading-2" data-parent="#accordion">
                                    <div class="card-body">
                                        <p>-New and existing Micro and Small
		Enterprises (MSE) in manufacturing, service and retail trade segment.</p>
                                    </div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-header" id="faqHeading-7">
                                    <div class="mb-0">
                                        <h5 class="faq-title" data-toggle="collapse" data-target="#faqCollapse-7" data-aria-expanded="false" data-aria-controls="faqCollapse-3">
                                            <span class="badge">3</span>Coverage :
                                        </h5>
                                    </div>
                                </div>
                                <div id="faqCollapse-7" class="collapse" aria-labelledby="faqHeading-3" data-parent="#accordion">
                                    <div class="card-body">
                                        <p>- collaterial free and third party guarantee free loans.</p>
                                        <p>- Partially collateralised loans</p>
                                        <p>- Unsecured bussiness loans</p>
                                    </div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-header" id="faqHeading-8">
                                    <div class="mb-0">
                                        <h5 class="faq-title" data-toggle="collapse" data-target="#faqCollapse-8" data-aria-expanded="false" data-aria-controls="faqCollapse-4">
                                            <span class="badge">4</span> Loan amount :
                                        </h5>
                                    </div>
                                </div>
                                <div id="faqCollapse-8" class="collapse" aria-labelledby="faqHeading-4" data-parent="#accordion">
                                    <div class="card-body">
                                        <p>-Upto 200 lakh extended to the MSEs (For retails trade it is upto 100lakh)</p>
                                    </div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-header" id="faqHeading-9">
                                    <div class="mb-0">
                                        <h5 class="faq-title" data-toggle="collapse" data-target="#faqCollapse-9" data-aria-expanded="false" data-aria-controls="faqCollapse-4">
                                            <span class="badge">5</span> Extent of coverage :
                                        </h5>
                                    </div>
                                </div>
                                <div id="faqCollapse-9" class="collapse" aria-labelledby="faqHeading-9" data-parent="#accordion">
                                    <div class="card-body">
                                        <p>-Upto 75% (option of 50% (or) 60% (or) 75% available) of the amount
			in default.</p>
                                    </div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-header" id="faqHeading-10">
                                    <div class="mb-0">
                                        <h5 class="faq-title" data-toggle="collapse" data-target="#faqCollapse-10" data-aria-expanded="false" data-aria-controls="faqCollapse-4">
                                            <span class="badge">5</span> Interest Rate Cap on  Loans :
                                        </h5>
                                    </div>
                                </div>
                                <div id="faqCollapse-10" class="collapse" aria-labelledby="faqHeading-10" data-parent="#accordion">
                                    <div class="card-body">
                                        <p>-Fullfilling RBI guidleines or any other rate as prescribed
		by the Trust frin time to time.</p>
                                    </div>
                                </div>
                            </div>
                            
                           
                            </div>
                        </div>
                         </div>	 --><!-- col-6   -->
                         
 
		
		
	<%-- <form:form  method="POST" action="" id="A">
	<div class="col-md-3 col-sm-4 col-xs-12">
		<div class="small-box bg-blue">
			<div class="inner">
				<h3>01</h3>
				<p>No. of Batch Uploads pending for approval</p>
			</div>
			<div class="icon"><i class="fa fa-upload" aria-hidden="true"></i></div>
			<a href="cgtmseCheckerBatchUploadsPendingForApprovalRM.html?paramID=${statusCMAKey}" class="small-box-footer">More Details	<i class="fa fa-arrow-circle-right"></i></a>			
		</div>
	</div>
	
	<div class="col-md-3 col-sm-4 col-xs-12">
		<div class="small-box bg-green">
			<div class="inner">
				<h3>00</h3>
				<p>	No. of Gurantee fee generation pending for approval</p>
			</div>
			<div class="icon"><i class="fa fa-inr" aria-hidden="true"></i></div>
			<a href="#" class="small-box-footer">More Details	<i class="fa fa-arrow-circle-right"></i></a>			
		</div>
	</div>
	
	<div class="col-md-3 col-sm-4 col-xs-12">
		<div class="small-box bg-orange">
			<div class="inner">
				<h3>00</h3>
				<p>No. of Gurantee fee aoorioruation pending for approval</p>
			</div>
			<div class="icon"><i class="fa fa-inr" aria-hidden="true"></i></div>
			<a href="#" class="small-box-footer">More Details	<i class="fa fa-arrow-circle-right"></i></a>			
		</div>
	</div>
	
	<div class="col-md-3 col-sm-4 col-xs-12">
		<div class="small-box bg-purple">
			<div class="inner">
				<h3>00</h3>
				<p>No. of Update files pending for approval</p>
			</div>
			<div class="icon"><i class="fa fa-file-text-o" aria-hidden="true"></i></div>
			<a href="#" class="small-box-footer">More Details	<i class="fa fa-arrow-circle-right"></i></a>			
		</div>
	</div>
	
	<div class="col-md-3 col-sm-4 col-xs-12">
		<div class="small-box bg-red">
			<div class="inner">
				<h3>00</h3>
				<p>No. of Claim Files pending for approval</p>
			</div>
			<div class="icon"><i class="fa fa-file-text-o" aria-hidden="true"></i></div>
			<a href="#" class="small-box-footer">More Details	<i class="fa fa-arrow-circle-right"></i></a>			
		</div>
	</div>	
	
	
	</form:form> --%>	
	


<!-- 
	<table style="width: 50%; float: left" border=1 bordercolor=black>
		<tr>
			<td> 
			<img src="images/HomeNote.jpg" style="margin: top" hight="100%"	width="100%">
			</td>			
		</tr>
	</table>
	  -->


	<%-- <table style="width: 40%; float: right" border="1" bordercolor="black">

		<thead>
			<tr>
			<td colspan=5 align=center><font size=4><b><u>User Dashboard</u></b></font></td>
			</tr>
			<tr>
				<c:forEach items="${rows[0]}" var="column">
					<b> </b>
					<td class="tableHeader1"><c:out value="${column.key}" /></td>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${rows}" var="columns">
				<tr>
					<c:forEach items="${columns}" var="column">
						<td align="center"><c:out value="${column.value}" /></td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>

	</table> --%>

</body>
</html>