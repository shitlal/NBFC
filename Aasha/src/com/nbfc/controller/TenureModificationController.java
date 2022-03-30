package com.nbfc.controller;

import java.io.IOException;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.nbfc.bean.ClaimApprovalBean;
import com.nbfc.bean.ClaimLodgementBean;

import com.nbfc.bean.TenureModificationDetailsBean;
import com.nbfc.model.ClaimLodgeBlobModel;
import com.nbfc.model.NBFCPrivilegeMaster;

import com.nbfc.model.UserPerivilegeDetails;
import com.nbfc.service.ClaimLodgementService;

import com.nbfc.service.LoginService;
import com.nbfc.service.NPAService;
import com.nbfc.service.TenureModificationService;
import com.nbfc.service.UserActivityService;
import com.nbfc.validation.EmployeeValidator;
import com.nbfc.validator.DataValidation;

@Controller


public class TenureModificationController {
	
	@Autowired
	LoginService lofinService;
	@Autowired
	UserActivityService userActivityService;
	NBFCPrivilegeMaster userPrvMst;
	ModelAndView modelView;
	String userId=null;
	UserPerivilegeDetails userPri;
	String loginUserMemId=null;
	@Autowired
	TenureModificationService tenureModificationService;

	@Autowired
	NPAService npaService;
	@Autowired
	EmployeeValidator validator;
	String claimRefNoValue;
	static double totalOfAmount=0;
	DataValidation dataValidation = new DataValidation();
	private static final DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	//List<RecoveryDetailModel> allocationList= new ArrayList<RecoveryDetailModel>();
	
	@RequestMapping(value="/TenureModification", method=RequestMethod.GET)
	public ModelAndView RecoveryInput(@ModelAttribute("command") TenureModificationDetailsBean bean,
			BindingResult result,String message, HttpSession session, Model model,HttpServletRequest request){
		Map<String, Object> model1 = new HashMap<String, Object>();
		Map<String, Object> mapObj = new HashMap<String, Object>();
		String userId = (String) session.getAttribute("userId");
		HttpSession LoginSession=request.getSession(false);
		if(LoginSession ==null || userId==null) {
				return new ModelAndView("redirect:/nbfcLogin.html");
		}
		List<TenureModificationDetailsBean> claimLodgList = null;
		String loginUserMemId = npaService.getMemberId(userId);
       	System.out.println(loginUserMemId);
			 userId=(String)session.getAttribute("userId");
			 if (message != null) {
				 mapObj.put("message", message);
				} else {
					mapObj.put("message", "");
				}
			 if(userId!=null){
					userPri=lofinService.getUserPrivlageDtl(userId, "A");
					userPrvMst=lofinService.getPrivlageMstDtl(userPri.getPrv_id());
					if(userPrvMst.getPrvCreatedModifiedBy().equals("NMAKER")){
						List<TenureModificationDetailsBean> BeanObjList=tenureModificationService.getTenureReturnedRecordsByNBFCChecker(loginUserMemId);
						if (!BeanObjList.isEmpty()) {
							mapObj.put("TenureReturnedRecordsList", BeanObjList);
						} else {
							mapObj.put("noDataFound", "NO Data Found !!");
						}
						mapObj.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
						mapObj.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
						mapObj.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
						mapObj.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
						mapObj.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
						mapObj.put("GMaintainlist", userActivityService.getActivity("NBFCMAKER", "Guarantee_Maintenance"));
						mapObj.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
						mapObj.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
						mapObj.put("actNameHome", "CGTMSE");
						mapObj.put("homePage", "nbfcMakerHome");
						mapObj.put("memberId", loginUserMemId);
						modelView = new ModelAndView("displayTenureInputForm", mapObj);
						return modelView;
					}else{
						modelView = new ModelAndView("redirect:/nbfcLogin.html");
					}
				 }else{
					 modelView = new ModelAndView("redirect:/nbfcLogin.html");
				 }
			
		
			 return modelView;
		
	}
	@RequestMapping(value = "TenureModificationInputForm", method ={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView claimLodgementInputForm(@ModelAttribute("command") TenureModificationDetailsBean tenureBean,BindingResult result,String message, HttpSession session, Model modelObj) {
	try{
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> mapObj5 = new HashMap<String, Object>();
		String userId = (String) session.getAttribute("userId");
		 userId=(String)session.getAttribute("userId");
		
		 if(userId!=null){
				userPri=lofinService.getUserPrivlageDtl(userId, "A");
				userPrvMst=lofinService.getPrivlageMstDtl(userPri.getPrv_id());
		loginUserMemId = npaService.getMemberId(userId);
		String cgpan = tenureBean.getCgpan();
		System.out.println("-----------"+cgpan);
		validator.cgpancheck(cgpan, result);
		if (result.hasErrors()) {
			//Added by say 6 FEb-------------------------------------------------------------
			mapObj5.put("message", "CGPAN is Mandatory");
			mapObj5.put("adminlist",
						userActivityService.getActivity("NBFCMAKER", "System_Admin"));
			mapObj5.put("guaranteelist",
						userActivityService.getActivity("NBFCMAKER", "Registration"));
			mapObj5.put("applicationList",
						userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
			mapObj5.put("RPaymentList",
						userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
			mapObj5.put("actName", userActivityService.getActivityName("NBFCMAKER",
						"NPADetails"));// Added by Say 31 Jan19
			mapObj5.put("repList",
						userActivityService.getReport("NBFCMAKER", "User_Report"));
			mapObj5.put("GMaintainlist", userActivityService.getActivity(
						"NBFCMAKER", "Guarantee_Maintenance"));
			mapObj5.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
			mapObj5.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
//				modelAct.put("actList",
//						userActivityService.getActivity("NBFCMAKER", "User_Activity"));
			 mapObj5.put("homePage", "nbfcMakerHome");
			 mapObj5.put("memberId", loginUserMemId);
			
			return new ModelAndView("displayTenureInputForm", mapObj5);
		}
					tenureBean=tenureModificationService.getValidateCgpanForTenureMod(cgpan,userId);
		    if(tenureBean.getCnt() ==0){
			mapObj5.put("message", "Case not eligible for tenure modification.");
		
						mapObj5.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
						mapObj5.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
						mapObj5.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
						mapObj5.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
						mapObj5.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
						mapObj5.put("GMaintainlist", userActivityService.getActivity("NBFCMAKER", "Guarantee_Maintenance"));
						mapObj5.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
						mapObj5.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
						mapObj5.put("actNameHome", "CGTMSE");
						mapObj5.put("homePage", "nbfcMakerHome");
						mapObj5.put("memberId", loginUserMemId);
						modelView = new ModelAndView("displayTenureInputForm", mapObj5);
						return modelView;
					
		}
		
		    TenureModificationDetailsBean List=tenureModificationService.getTenureModificationDetails(cgpan);
			mapObj5.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
						mapObj5.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
						mapObj5.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
						mapObj5.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
						mapObj5.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
						mapObj5.put("GMaintainlist", userActivityService.getActivity("NBFCMAKER", "Guarantee_Maintenance"));
						mapObj5.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
						mapObj5.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
						mapObj5.put("actNameHome", "CGTMSE");
						mapObj5.put("homePage", "nbfcMakerHome");
						//mapObj5.put("memberId", loginUserMemId);
						mapObj5.put("formData", List);
						modelView = new ModelAndView("tenureModDetails", mapObj5);
						return modelView;	
			 
			 }else{
					modelView = new ModelAndView("redirect:/nbfcLogin.html");
				}
				return modelView;
			}catch(Exception e){
				System.out.println("Exception =="+e);
			}
				return modelView;
		}
	@RequestMapping(value = "submitTenureModificationDetails", method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView submitTenureModificationDetails(
			@ModelAttribute("command") TenureModificationDetailsBean tenureBean,
			BindingResult result, Model model, HttpSession session,String CGPAN, HttpServletRequest request) {
		      int count=0;
				String usr_id=(String)session.getAttribute("userId");
				HttpSession LoginSession=request.getSession(false);
				if(LoginSession ==null || usr_id==null) {
						return new ModelAndView("redirect:/nbfcLogin.html");
				}

				String loginUserMemId = npaService.getMemberId(usr_id);
				Map<String, Object> modelAct = new HashMap<String, Object>();
				tenureBean.setCgpan(CGPAN);
				System.out.println("cgpan----------"+CGPAN);
				count=tenureModificationService.submitTenureModificationDetails(tenureBean,loginUserMemId,usr_id);
				System.out.println("Tenure save successfully =="+count);
		
				String message = "";
				if (count > 0) {
					message = "Tenure Modification successfully submitted";
		
				}
			//	modelAct.put("totalOfAmount", BigDecimal.valueOf(totalOfAmount).toBigDecimal().toPlainString());
			//	modelAct.put("danDetailList", danDetailList);
				//Added by say 6 FEb-------------------------------------------------------------
				modelAct.put("adminlist",
						userActivityService.getActivity("NBFCMAKER", "System_Admin"));
				modelAct.put("guaranteelist",
						userActivityService.getActivity("NBFCMAKER", "Registration"));
				modelAct.put("applicationList",
						userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
				modelAct.put("RPaymentList",
						userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
				modelAct.put("actName", userActivityService.getActivityName("CGTMSECHECKER",
						"danAllocation"));// Added by Say 31 Jan19
				modelAct.put("repList",
						userActivityService.getReport("NBFCMAKER", "User_Report"));
				modelAct.put("GMaintainlist", userActivityService.getActivity(
						"NBFCMAKER", "Guarantee_Maintenance"));
				modelAct.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
				modelAct.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
				modelAct.put("homePage", "nbfcMakerHome");
				modelAct.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
				return new ModelAndView(
						"redirect:/TenureModification.html?message=" + message);
	}
	
	@RequestMapping(value = "TenureModificationApproval", method ={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView TenureModificationApproval(@ModelAttribute("command") TenureModificationDetailsBean tenureBean,BindingResult result, HttpSession session, Model modelObj,HttpServletRequest request) {
	try{
		String userId = (String) session.getAttribute("userId");
		HttpSession LoginSession=request.getSession(false);
		if(LoginSession ==null || userId==null) {
				return new ModelAndView("redirect:/nbfcLogin.html");
		}
		Map<String, Object> mapObj1 = new HashMap<String, Object>();
		List<TenureModificationDetailsBean> claimLodgList = null;
		loginUserMemId = npaService.getMemberId(userId);
		TenureModificationDetailsBean list2 = null;
		String Claim_ref_no="";
			 userId=(String)session.getAttribute("userId");
			 List<TenureModificationDetailsBean> list = tenureModificationService.getTenureModDetailsApproval(loginUserMemId);
			
		/*	 for (ClaimLodgementBean bean: list) {
				   System.out.println(bean.getClaimRefNo());
				   Claim_ref_no=bean.getClaimRefNo();
				  list2 = claimLodgementService.getClaimChecklistRecommndation(Claim_ref_no);
				}
			*/
				if (!list.isEmpty()) {
					mapObj1.put("danDetailList", list);
				} else {
					mapObj1.put("noDataFound", "NO Data Found !!");
				}
				
			
			 if(userId!=null){
					userPri=lofinService.getUserPrivlageDtl(userId, "A");
					userPrvMst=lofinService.getPrivlageMstDtl(userPri.getPrv_id());
				//	if(userPrvMst.getPrvCreatedModifiedBy().equals("NCHECKER")){
						
						mapObj1.put("adminlist", userActivityService.getActivity("NBFCCHECKER", "System_Admin"));
						mapObj1.put("guaranteelist", userActivityService.getActivity("NBFCCHECKER", "Registration"));
						mapObj1.put("applicationList", userActivityService.getActivity("NBFCCHECKER", "Application_Processing"));
						mapObj1.put("RPaymentList", userActivityService.getActivity("NBFCCHECKER", "Receipt_Payments"));
						mapObj1.put("GMaintainlist", userActivityService.getActivity("NBFCCHECKER", "Guarantee_Maintenance"));
						mapObj1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
						mapObj1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
						mapObj1.put("CBMFList", userActivityService.getActivity("NBFCCHECKER", "Claim_Bank_Mandate"));
						mapObj1.put("actNameHome", "CGTMSE");
						mapObj1.put("homePage", "nbfcMakerHome");
						mapObj1.put("memberId", loginUserMemId);
						mapObj1.put("MLIID", loginUserMemId);
						modelView = new ModelAndView("tenureModDetailsApproval", mapObj1);
						return modelView;
				}else{
					 modelView = new ModelAndView("redirect:/nbfcLogin.html");
				 }
			
			return modelView;
		}catch(Exception e){
			System.out.println("Exception =="+e);
		}
			return modelView;
	}
	//method for Claim Approve Or Reject By Checker
	@RequestMapping(value = "mliApproveOrRejectTenure", method ={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView mliApproveOrRejectTenure(@ModelAttribute("command") TenureModificationDetailsBean tenureBean,BindingResult result, HttpSession session, Model modelObj,HttpServletRequest req,String CHK,HttpServletRequest request){
		System.out.println("Inside MLI CHeckar Approve Or Reject Tenure Method"+CHK);
		// checking the session at the approve or reject time
		String userId = (String) session.getAttribute("userId");
		HttpSession LoginSession=request.getSession(false);
		if(LoginSession ==null || userId==null) {
				return new ModelAndView("redirect:/nbfcLogin.html");
		}
		String Status="";
		String claimRemarks="";
 		String cgpan="";
		 String Final_settelement_Amt = "";
		 String Claim_eligibility_Amt = "";
		String msg="Claim Updated successfully";
		//String hidden=claimbean.getClaimStatusRemarkHidden();
		//System.out.println("hidden--------------"+hidden);
		Map<String, Object> mapObj1 = new HashMap<String, Object>();
		
		String userRole = (String) session.getAttribute("uRole");
		System.out.println(tenureBean.getAcceptReturn());
		System.out.println(tenureBean.getCgpan());
          String par=req.getParameter("AcceptReturn");
          System.out.println("yyyyy0------"+par);
		String status=tenureBean.getAcceptReturn();
		cgpan=tenureBean.getCgpan();
		String Remarks=tenureBean.getReturnRemark();
		tenureBean.setCkbankAuthority(CHK);
		System.out.println(tenureBean.gettId());
		Integer tId=tenureBean.gettId();
		tenureBean.settId(tId);
	//	System.out.println("IsDeclared---------"+IsDeclared);
		//List<String> List4 =null;
		List<String> List5 =null;
	 
		System.out.println("Return remarks=="+claimRemarks);
	//	System.out.println("final_amt=="+Final_settelement_Amt);
		
		System.out.println(claimRemarks);
		TenureModificationDetailsBean bean;
		List<String> List2 = new ArrayList<String>(Arrays.asList(status.split(",")));
		List<String> List1 = new ArrayList<String>(Arrays.asList(cgpan.split(",")));
		List<String> List3 = new ArrayList<String>(Arrays.asList(Remarks.split(",")));
	//	List<String> List4 = new ArrayList<String>(Arrays.asList(IsDeclared.split(",")));
	
			
		Map<String, Object> claimStatusMapObj = new HashMap<String, Object>();
		
		
			
		for(int i=0;i<List1.size();i++){
			bean=new TenureModificationDetailsBean();	
			if(!List1.isEmpty()){
				  List1.get(i); 
				  bean.setMLI_STATUS(List2.get(i));
				  
			  }	else{
				  bean.setMLI_STATUS("");  
			  }
			
		  if(!List2.isEmpty()){
			  List2.get(i); 
			  
			  bean.setMLI_STATUS(List2.get(i));
			//  claimLodgementService.validateClaimAccptReturn(userId, claimStatusMapObj,userRole); 
		  }	else{
			  bean.setMLI_STATUS("");  
		  }
		  if(!List3.isEmpty()){	
			  	if(List3.get(i).length()>0){
				  	if(!List3.get(i).equals("")||!List3.get(i).equals(null)){
					  bean.setReturnRemark(List3.get(i));  
			  		}else{
							bean.setReturnRemark("Enter Remarks");  
					}
			  	}
		  }else {
				bean.setReturnRemark("Enter Remarks");  
		  }
		/*  if(!List4.isEmpty()){	
				if(List4.get(i).length()>0){
				  	if(!List4.get(i).equals("")||!List4.get(i).equals(null)){
					  bean.setCkbankAuthority(List4.get(i));  
			  		}else{
							bean.setCkbankAuthority("");  
					}
			  	}
			  	}*/
		  
		  
		  
		  
			claimStatusMapObj.put(List1.get(i), bean);
		}
		
		
		TenureModificationDetailsBean bean1=tenureModificationService.updateStatusTenureApprovedReturn(userId, claimStatusMapObj,userRole,CHK);
		
		String MSG=bean1.getMsg();
		String RMSG=bean1.getRmsg();
		loginUserMemId = npaService.getMemberId(userId);
		
		 if(userId!=null){
				userPri=lofinService.getUserPrivlageDtl(userId, "A");
				userPrvMst=lofinService.getPrivlageMstDtl(userPri.getPrv_id());
				if(userPrvMst.getPrvCreatedModifiedBy().equals("NCHECKER")){
					mapObj1.put("adminlist", userActivityService.getActivity("NBFCCHECKER", "System_Admin"));
					mapObj1.put("guaranteelist", userActivityService.getActivity("NBFCCHECKER", "Registration"));
					mapObj1.put("applicationList", userActivityService.getActivity("NBFCCHECKER", "Application_Processing"));
					mapObj1.put("RPaymentList", userActivityService.getActivity("NBFCCHECKER", "Receipt_Payments"));
					mapObj1.put("GMaintainlist", userActivityService.getActivity("NBFCCHECKER", "Guarantee_Maintenance"));
					mapObj1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
					mapObj1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
					mapObj1.put("CBMFList", userActivityService.getActivity("NBFCCHECKER", "Claim_Bank_Mandate"));
					mapObj1.put("actNameHome", "CGTMSE");
					mapObj1.put("homePage", "nbfcMakerHome");
					mapObj1.put("memberId", loginUserMemId);
					mapObj1.put("messageR", RMSG);
			        mapObj1.put("message", MSG);
					/* START Added by VinodSingh on 12-Mar-2021 */
			        mapObj1.put("resultList", bean1.getStatusList());
			        modelView = new ModelAndView("successclaimLoadgementApprovalMLI",mapObj1);
					return modelView;
				}
			 }else{
				 modelView = new ModelAndView("redirect:/nbfcLogin.html");
			 }
		
		return modelView;
		
	}
	@RequestMapping(value = "TenureDataForModification", method ={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getclaimLodgedDataForModification(@ModelAttribute("command") TenureModificationDetailsBean Bean,BindingResult result, HttpSession session,Integer TenId,HttpServletRequest request) {
		String userId = (String) session.getAttribute("userId");
		HttpSession LoginSession=request.getSession(false);
		if(LoginSession ==null || userId==null) {
				return new ModelAndView("redirect:/nbfcLogin.html");
		}

		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> mapObj5 = new HashMap<String, Object>();
		
		userId=(String)session.getAttribute("userId");
		 if(userId!=null){
			userPri=lofinService.getUserPrivlageDtl(userId, "A");
			userPrvMst=lofinService.getPrivlageMstDtl(userPri.getPrv_id());
			loginUserMemId = npaService.getMemberId(userId);
		//	int T_ID = Bean.gettId();
			//String cgpan="CGN2019000000025";
			System.out.println("-----cgpan 222222222222------"+TenId);
			 TenureModificationDetailsBean List=tenureModificationService.getTenureDetailsForUpdate(TenId);
			mapObj5.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
			mapObj5.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
			mapObj5.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
			mapObj5.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
			mapObj5.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
			mapObj5.put("GMaintainlist", userActivityService.getActivity("NBFCMAKER", "Guarantee_Maintenance"));
			mapObj5.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
			mapObj5.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
			mapObj5.put("actNameHome", "CGTMSE");
			mapObj5.put("homePage", "nbfcMakerHome");
			//mapObj5.put("memberId", loginUserMemId);
			mapObj5.put("formData", List);
			
			
			modelView = new ModelAndView("TenureModificationFormForMLIMaker", mapObj5);
		}
		return modelView;
	}	
	//editClaimDataReturnByNBFCMake
	@RequestMapping(value = "editTenureDataReturnByNBFCMaker", method ={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView editTenureDataReturnByNBFCMaker(@ModelAttribute("command") TenureModificationDetailsBean Bean,BindingResult result, HttpSession session,Integer TenId) {
		Map<String, Object> mapObj5 = new HashMap<String, Object>();
		String userId = (String) session.getAttribute("userId");
		userId=(String)session.getAttribute("userId");
		String MSG ="";
		if(userId!=null){
			userPri=lofinService.getUserPrivlageDtl(userId, "A");
			userPrvMst=lofinService.getPrivlageMstDtl(userPri.getPrv_id());
			loginUserMemId = npaService.getMemberId(userId);
			String memberId=loginUserMemId;
			String loginUserId=userId;
			String claimRefNo = claimRefNoValue;
			int updatedCheckList=0;
			System.out.println("ClaimRefNoValue==="+claimRefNo);
		//	String cgpan = Bean.getCgpan();
			int n=tenureModificationService.updateTenureDataDetails(Bean,TenId, userId);
	
			mapObj5.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
			mapObj5.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
			mapObj5.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
			mapObj5.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
			mapObj5.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
			mapObj5.put("GMaintainlist", userActivityService.getActivity("NBFCMAKER", "Guarantee_Maintenance"));
			mapObj5.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
			mapObj5.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
			mapObj5.put("actNameHome", "CGTMSE");
			mapObj5.put("homePage", "nbfcMakerHome");
			mapObj5.put("memberId", loginUserMemId);
			mapObj5.put("message", "TENURE Details Modify Successfully");
			modelView = new ModelAndView("successclaimLoadgementApprovalMLI", mapObj5);
		}
		return modelView;
	
	}	
	@RequestMapping(value = "TenureDataModificationDetails", method ={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView TenureDataModificationDetails(@ModelAttribute("command") TenureModificationDetailsBean Bean,BindingResult result, HttpSession session,Integer TenId) {
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> mapObj5 = new HashMap<String, Object>();
		String userId = (String) session.getAttribute("userId");
		userId=(String)session.getAttribute("userId");
		 if(userId!=null){
			userPri=lofinService.getUserPrivlageDtl(userId, "A");
			userPrvMst=lofinService.getPrivlageMstDtl(userPri.getPrv_id());
			loginUserMemId = npaService.getMemberId(userId);
		//	int T_ID = Bean.gettId();
			//String cgpan="CGN2019000000025";
			System.out.println("-----cgpan 222222222222------"+TenId);
			 TenureModificationDetailsBean List=tenureModificationService.getTenureDetailsForUpdate(TenId);
			mapObj5.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
			mapObj5.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
			mapObj5.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
			mapObj5.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
			mapObj5.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
			mapObj5.put("GMaintainlist", userActivityService.getActivity("NBFCMAKER", "Guarantee_Maintenance"));
			mapObj5.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
			mapObj5.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
			mapObj5.put("actNameHome", "CGTMSE");
			mapObj5.put("homePage", "nbfcMakerHome");
			//mapObj5.put("memberId", loginUserMemId);
			mapObj5.put("formData", List);
			
			
			modelView = new ModelAndView("TenureDataDetails", mapObj5);
		}
	
	return new ModelAndView("redirect:/nbfcLogin.html");
	

	}	
	
	
}
