package com.nbfc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nbfc.bean.UpdMemberStatusBean;
import com.nbfc.model.NBFCPrivilegeMaster;
import com.nbfc.model.UserPerivilegeDetails;
import com.nbfc.service.LoginService;
import com.nbfc.service.NPAService;
import com.nbfc.service.UpdMemberStatusService;
import com.nbfc.service.UserActivityService;
import com.nbfc.validation.EmployeeValidator;


@Controller
public class UpdMemberStatusController {

	@Autowired
	LoginService lofinService;
	@Autowired
	UserActivityService userActivityService;
	NBFCPrivilegeMaster userPrvMst;
	ModelAndView modelView;
	String userId=null;
	UserPerivilegeDetails userPri;
	String loginUserMemId=null;
	String memberId=null;	
	@Autowired
	NPAService npaService;
	@Autowired
	UpdMemberStatusService updMemStatusService;
	@Autowired
	EmployeeValidator validator;
	static Logger log = Logger.getLogger(UpdMemberStatusController.class.getName());

	@RequestMapping(value = "getMemberStatus", method ={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView showMemberStatusForm(@ModelAttribute("command") UpdMemberStatusBean updMemberStatusBean,BindingResult result, HttpSession session, Model modelMsg) {
		try{
			String loginUserId = (String) session.getAttribute("userId");
			String meberId=loginUserId;
			if(loginUserId!=null){
				userPri=lofinService.getUserPrivlageDtl(loginUserId, "A");
				userPrvMst=lofinService.getPrivlageMstDtl(userPri.getPrv_id());
				memberId= npaService.getMemberId(loginUserId);
				log.info("meberId==="+meberId+" Login User's:"+userPrvMst.getPrvCreatedModifiedBy());
				if(userPrvMst.getPrvCreatedModifiedBy().equals("CCHECKER")){
					Map<String, Object> mapObj = new HashMap<String, Object>();
					String memId=memberId;
					List<UpdMemberStatusBean> listObj=updMemStatusService.getBankMandateDetailsFromCGTMSE();
					if (!listObj.isEmpty()) {
						mapObj.put("bankmanDateList", listObj);
					} else {

						mapObj.put("recordNotFound", "NO Data Found !!");
					}

					mapObj.put("adminlist", userActivityService.getActivity("CGTMSECHECKER", "System_Admin"));
					mapObj.put("guaranteelist", userActivityService.getActivity("CGTMSECHECKER", "Registration"));
					mapObj.put("applicationList", userActivityService.getActivity("CGTMSECHECKER", "Application_Processing"));
					mapObj.put("RPaymentList", userActivityService.getActivity("CGTMSECHECKER", "Receipt_Payments"));
					mapObj.put("repList", userActivityService.getReport("CGTMSECHECKER", "User_Report"));
					mapObj.put("GMaintainlist", userActivityService.getActivity("CGTMSECHECKER", "Guarantee_Maintenance"));
					mapObj.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
					mapObj.put("CBMFList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Bank_Mandate"));
					mapObj.put("actNameHome", "CGTMSE");
					mapObj.put("homePage", "nbfcMakerHome");

					modelView = new ModelAndView("UpdMemberStatus", mapObj);
					return modelView;
				}
			}else{
				modelView = new ModelAndView("redirect:/nbfcLogin.html");
			}

		}catch(Exception e){
			System.out.println("Exception =="+e);
		}

		return modelView;

	}




	@RequestMapping(value = "updBankMandateStatusFromCGTMSE", method ={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getBankMandateDataForApprovalOrRejectionFromCGTMSE(HttpServletRequest request,
			@ModelAttribute("command") UpdMemberStatusBean updMemberStatusBean, HttpSession session, @RequestParam(value = "memberId")String mliMemeberId,@RequestParam(value = "cgtmseCheckerRemarks")String cgtmseCheckerRemarks) {
		log.info("mliMemeberId==="+mliMemeberId);
		String loginUserId = (String) session.getAttribute("userId");
		System.out.println("loginUserId:::"+loginUserId);	

		String remarks = request.getParameter("cgtmseCheckerRemarks") != null ? request.getParameter("cgtmseCheckerRemarks") : "";
		System.out.println("remarks:::"+remarks);
		
		String updStatus = updMemStatusService.updBankMandateStausFromCGTMSE(mliMemeberId,loginUserId,remarks.trim());		
		log.info("updStatus==="+updStatus);
		modelView = new ModelAndView("redirect:/getMemberStatus.html");	
		
		return modelView;
	}
}