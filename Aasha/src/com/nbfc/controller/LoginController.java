package com.nbfc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.math.BigDecimal;
import com.nbfc.bean.ASFGenerationDetailBean;
import com.nbfc.bean.DANAllocationBean;
import com.nbfc.bean.GetMLINameBean;
import com.nbfc.bean.LoginBean;
import com.nbfc.bean.PortfolioDetailsBean;
import com.nbfc.bean.UserDashboardBean;
import com.nbfc.model.CGTMSECheckerForBatchApprovalGetStatus;
import com.nbfc.model.CGTMSEMakerForBatchApprovalGetStatus;
import com.nbfc.model.DanAllocationForNbfcMakerUsingVWModel;
import com.nbfc.model.Login;
import com.nbfc.model.UserDashboardVmodel;
import com.nbfc.service.ASFGenerationBulkUploadService;
import com.nbfc.service.CGTMSECheckerForBatchApprovalService;
import com.nbfc.service.CGTMSEMakerForBatchApprovalGetStatusService;
import com.nbfc.service.LoginService;
import com.nbfc.service.NBFCUserReportService;
import com.nbfc.service.NPAService;
import com.nbfc.service.UserActivityService;
import com.nbfc.service.UserService;
import com.raistudies.domain.CustomExceptionHandler;
import com.vaannila.validator.UserValidator;

@Controller
public class LoginController {

	String usr_id = null;
	String usr_password = null;
	String User = null;
	Login login = null;
	String userName = null;
	static Logger log = Logger.getLogger(NBFCController.class.getName());
	@Autowired
	ASFGenerationBulkUploadService ASFGenerationService;
	@Autowired
	private LoginService loginService;
	@Autowired
	UserService userService;
	@Autowired
	UserActivityService userActivityService;
	@Autowired
	NBFCUserReportService nbfcUserReportService;
	@Autowired
	NPAService npaService;
	@Autowired
	private CGTMSEMakerForBatchApprovalGetStatusService cgtmseMakerForBatchApprovalGetStatusService;
	@Autowired
	private CGTMSECheckerForBatchApprovalService cgtmseCheckerForBatchApprovalService;
	UserDashboardBean userDashboardBean;
	GetMLINameBean getMLINameBean;
	UserValidator userValidator = new UserValidator();
	String userIdAndPasswordIsCorrect = null;
	List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

	public LoginController() {
		log.info("LoginController class default called===");
	}

	public LoginController(UserValidator userValidator) {
		log.info("LoginController class constructor called===");
		this.userValidator = userValidator;
	}

	@RequestMapping(value = "nbfcLogin", method = RequestMethod.GET)
	public ModelAndView showLoginBeanInputForm(
			@ModelAttribute("command") LoginBean loginBean, BindingResult result) {
		log.info("showLoginBeanInputForm method call as part of LoginController=====");
		return new ModelAndView("login");
		// return "login";
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public ModelAndView logOut(@ModelAttribute("command") LoginBean loginBean,
			BindingResult result, HttpSession session) {
		log.info("logout method call as part of LoginController=====");
		session.removeAttribute("uName");
		session.removeAttribute("userId");

		return new ModelAndView("login");
	}

	@RequestMapping(value = "changePassword", method = RequestMethod.POST)
	public ModelAndView changePassword(
			@ModelAttribute("command") LoginBean loginBean,
			BindingResult result, Model model, HttpSession session) {
		log.info("logout method call as part of LoginController=====");
		Map<String, Object> modelAct = new HashMap<String, Object>();
		String Password=loginBean.getNewPassword();
		char array1[] = Password.toCharArray();
		String invalids = "!@#$%";
		int specialCharactersCount = 0;
		int digitCount = 0;
		int letterCount = 0;
          
		ModelAndView modelAndView = new ModelAndView();
		if (Password.equals(loginBean.getReEnterPassword())) {
			
			for (int i = 0; i < invalids.length(); i++) {
				if (Password.indexOf(invalids.charAt(i)) >= 0) {
					specialCharactersCount = specialCharactersCount + 1;
					// System.out.println("Given PasswordContains Invalide characters");
					// break;
				}
			}
			// if(array1.length==10){

			for (int j = 0; j < Password.length(); j++) {
				if (Character.isLetter(array1[j])) {
					letterCount = letterCount + 1;
				} else if (Character.isDigit(array1[j])) {
					digitCount = digitCount + 1;
				}
			}
			// }
	
			if (digitCount == 0) {
				modelAct.put("message",
						"Password should comprise of atleast one digit, one special characer and one letter");
				modelAndView = new ModelAndView("firstTimeLogin", modelAct);
					
			} else if (specialCharactersCount == 0) {
				modelAct.put("message",
						"Password should comprise of atleast one digit, one special characer and one letter");
				modelAndView = new ModelAndView("firstTimeLogin", modelAct);
			} else if (letterCount == 0) {
				modelAct.put("message",
						"Password should comprise of atleast one digit, one special characer and one letter");
				modelAndView = new ModelAndView("firstTimeLogin", modelAct);
			}else{
			boolean flag = loginService
					.changePassword(preChangePasswordModal(loginBean,
							loginService.userDetails(loginBean.getUsr_id())));
			if (flag) {
				modelAct.put("message",
						"Password has been successfully change.. ");
				modelAndView = new ModelAndView("login", modelAct);
			}
			}
			
		/*	if (Password.length() >= 6) {
				//if (strSpecialChars.contains(Password)){
				//	if (regularExpression.contains(Password)){
					
				
				boolean flag = loginService
						.changePassword(preChangePasswordModal(loginBean,
								loginService.userDetails(loginBean.getUsr_id())));
				if (flag) {
					modelAct.put("message",
							"Password has been successfully change ");
					modelAndView = new ModelAndView("login", modelAct);
				}
//			} else{
//				modelAct.put("message",
//						"Password should be Contain  Alphabate and Numeric values  !!");
//						
//				modelAndView = new ModelAndView("firstTimeLogin", modelAct);
//			}
//				}else{
//					modelAct.put("message",
//							"Password should be Contain at least one special character !!");
//					modelAndView = new ModelAndView("firstTimeLogin", modelAct);
//				}
			}else {

				modelAct.put("message",
						"Password should be menimum six digits !!");
				modelAndView = new ModelAndView("firstTimeLogin", modelAct);
			}*/
		} else {
			modelAct.put("message", "Password does not match !!");
			modelAndView = new ModelAndView("firstTimeLogin", modelAct);
		}
		// loginService.userDetails(loginBean.getUsr_id());
		return modelAndView;
	}

	@RequestMapping(value = "nbfcLoginSubmitForm", method = RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command") LoginBean loginBean,	BindingResult result,HttpServletRequest request, Model model, HttpSession session) {
		session.setAttribute("userId", loginBean.getUsr_id());
		String userId=(String)session.getAttribute("userId");
		PortfolioDetailsBean bean = new PortfolioDetailsBean();
		userValidator.validate(loginBean, result);
		 Object TotalASFDUECount = 0;
		 
			// checking the session at URL Hitting
		HttpSession LoginSession=request.getSession(false);
		if(LoginSession ==null || userId==null) {
				return new ModelAndView("redirect:/nbfcLogin.html");
		}
		 
		if (result.hasErrors()) {
			return new ModelAndView("login");
		}
		log.info("onSubmit method call as part of LoginController=====");
		Map<String, Object> modelAct = new HashMap<String, Object>();
		Integer UserPerivilegeId = 0;
		String loginUsrId = loginBean.getUsr_id();
		String usrFlag = "A";

		ModelAndView modelAndViewObj = new ModelAndView();
		userIdAndPasswordIsCorrect = loginService.verifyUserLogin(loginBean);
		log.info("isUserExists===" + userIdAndPasswordIsCorrect);


		if(userIdAndPasswordIsCorrect!=null && !userIdAndPasswordIsCorrect.equals("invalidUser")) {
			List<Object> listObj2 = loginService.gerLoginUserPrivilege(loginUsrId,usrFlag);
			log.info("listObj2==" + listObj2);

			java.util.Iterator<Object> itr2 = listObj2.iterator();

			while (itr2.hasNext()) {
				Object[] obj1 = (Object[]) itr2.next();
				log.info("PRVId===" + obj1[0] + " " + "Flag==" + obj1[1] + " "+ "USERID==" + obj1[2] + " " + "PageName==" + obj1[4]+ "Page Descrition==" + obj1[5] + "TypeOfUser==" + obj1[6]);
				User = (String) obj1[6];
				log.info("Role===" + User);
			}
			//--------------------------ASF DUE LIST-------------------------------
			List<ASFGenerationDetailBean> list = ASFGenerationService.getASFDetailsByFileWise(userId);
			if (!list.isEmpty()) {
				modelAct.put("danDetailList", list);
			} else {
				modelAct.put("noDataFound", "NO Data Found !!");
			}
			//------------------COunt ASF DUE------------------------------------------------

			//TotalASFDUECount = ASFGenerationService.getASFDueCount(userId);
			// modelAct.put("TotalASFDueCountKey", TotalASFDUECount);

			//------------------------------------------------------------------
			
		}

		if(userIdAndPasswordIsCorrect!=null && userIdAndPasswordIsCorrect.equals("inactiveuser")) {
			System.out.println("User is InActive"+userIdAndPasswordIsCorrect);
			model.addAttribute("message", "this User has been deactivated ....");			
			return new ModelAndView("login");
		}
		else if (userIdAndPasswordIsCorrect.equals("invalidUser")) {
			log.info("userExists not Exit===" + userIdAndPasswordIsCorrect);
			model.addAttribute("InvalidCredencialKey", "Invalid Credentials");
			log.info("Invalid Credentials===");
			return new ModelAndView("login");
		} else if (userIdAndPasswordIsCorrect.equals("firstTimeLogin")) {

			log.info("userExists not Exit===" + userIdAndPasswordIsCorrect);
			model.addAttribute("InvalidCredencialKey",
					"Please Change the Password !!");

			log.info("First time login,User Change Password");
			return new ModelAndView("firstTimeLogin");

		} else {
			if (userIdAndPasswordIsCorrect.equals("success") && User.equals("ADMIN")) {
				log.info("ROLE IS ADMIN==" + session.getAttribute("userId"));
				login = loginService.userDetails(loginBean.getUsr_id());
				session.setAttribute("uName", login.getUSR_FIRST_NAME() + " "+ login.getUSR_LAST_NAME());
				session.setAttribute("uRole", "ADMIN");// added by say
				getMLINameBean = nbfcUserReportService.getMliDetails(loginBean.getUsr_id());

				session.setAttribute("mliName", getMLINameBean.getMliName());


				// Added by say 6 Feb
				modelAct.put("adminlist", userActivityService.getActivity("ADMIN", "System_Admin"));
				modelAct.put("actNameHome", "CGTMSE");// Added by Say 31 Jan19
				// modelAct.put("actList", userActivityService.getActivity(
				// "ADMIN", "User_Activity"));//commented by Say 6 Feb
				modelAct.put("repList",userActivityService.getReport("ADMIN", "User_Report"));
				modelAct.put("homePage", "adminHome");
				return new ModelAndView("adminPage", modelAct);
			}

			if (userIdAndPasswordIsCorrect.equals("success") && User.equals("CMAKER")) {
				log.info("ROLE IS CMAKER==" + session.getAttribute("userId"));
				login = loginService.userDetails(loginBean.getUsr_id());
				session.setAttribute("uName", login.getUSR_FIRST_NAME() + " "+ login.getUSR_LAST_NAME());
				session.setAttribute("userId", loginBean.getUsr_id());
				session.setAttribute("uRole", "CMAKER");// added by say
				getMLINameBean = nbfcUserReportService.getMliDetails(loginBean.getUsr_id());
				session.setAttribute("mliName", getMLINameBean.getMliName());
				// Added by sayali
				// 18march19------------------------------------------------------------------------------------------------------------------
				CGTMSEMakerForBatchApprovalGetStatus cgtmseMakerForBatchApprovalGetStatus = new CGTMSEMakerForBatchApprovalGetStatus();

				String statusNCA = null;
				int statusNCACount = 0;
				Integer subPortfolioDtlNo = 0;
				Object totalNoOfUploadedFile = 0;
				String status = null;

				// Get Record Based On NCA status

				List<Object> list1 = new ArrayList<Object>();
				String status1 = "NCA";
				log.info("Status===" + status1);
				cgtmseMakerForBatchApprovalGetStatus.setStatus(status1);
				totalNoOfUploadedFile = cgtmseMakerForBatchApprovalGetStatusService.getNCAStatusCountBasedOnNCAStatus(cgtmseMakerForBatchApprovalGetStatus);
				modelAct.put("countTotalNoOfUploadedFileKey",totalNoOfUploadedFile);
				modelAct.put("statusKey", status1);

				// end----------------------------------------------------------------------------------------------------------------------
				/*---------------------------------------------------------------
				 List<PortfolioDetailsBean> list12 = npaService.getExposureDetailsDASHBOARD(userId);
				 if (!list12.isEmpty()) {
						modelAct.put("ExpoDashboard", list12);
					} else {
						modelAct.put("noDataFound", "NO Data Found !!");
					}
//			//---------------------------------------------------------------------------------------------
				 Object TotalExpiredExpoCount = 0;
//			
					TotalExpiredExpoCount = npaService.getExposureExpiredCount(userId);
					modelAct.put("countTotalNoExpiredExpo",
							TotalExpiredExpoCount);

				//------------------------------------------------------------------

				 */

				modelAct.put("guaranteelist", userActivityService.getActivity("CGTMSEMAKER", "Registration"));
				modelAct.put("applicationList", userActivityService.getActivity("CGTMSEMAKER", "Application_Processing"));
				modelAct.put("RPaymentList", userActivityService.getActivity("CGTMSEMAKER", "Receipt_Payments"));
				modelAct.put("GMaintainlist", userActivityService.getActivity("CGTMSEMAKER", "Guarantee_Maintenance"));
				modelAct.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));

				modelAct.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
				//modelAct.put("CBMFList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Bank_Mandate"));
				modelAct.put("actNameHome", "CGTMSE");// Added by Say 31 Jan19
				modelAct.put("homePage", "cgtmseMakerHome");

				return new ModelAndView("successCGTMSEMAKERPage", modelAct);
			}
			if (userIdAndPasswordIsCorrect.equals("success") && User.equals("CCHECKER")) {
				log.info("ROLE IS CCHECKER==" + session.getAttribute("userId"));
				login = loginService.userDetails(loginBean.getUsr_id());
				getMLINameBean = nbfcUserReportService.getMliDetails(loginBean.getUsr_id());
				session.setAttribute("mliName", getMLINameBean.getMliName());
				session.setAttribute("uName", login.getUSR_FIRST_NAME() + " "+ login.getUSR_LAST_NAME());
				CGTMSECheckerForBatchApprovalGetStatus cgtmseCheckerForBatchApprovalGetStatus = new CGTMSECheckerForBatchApprovalGetStatus();
				session.setAttribute("userId", loginBean.getUsr_id());
				session.setAttribute("uRole", "CCHECKER");// added by say
				// ---------------Added by sayali
				// 18march19---------------------------------------------
				// Get All the status
				List<Object> list1 = new ArrayList<Object>();
				String statusCMA1 = "CMA";
				Long noOfUploadedExcelFile = (long) 0;
				Integer counterNOfUploadedExcelFile = 0;
				Long noOfUploadedExcelFileCount = (long) 0;
				cgtmseCheckerForBatchApprovalGetStatus.setStatus(statusCMA1);
				int counter = 0;
				Long noOfFiles = (long) 0;
				List<Object> listObj = cgtmseCheckerForBatchApprovalService.getCMAStatusCount(cgtmseCheckerForBatchApprovalGetStatus);
				if (listObj.size() != 0) {
					Iterator<Object> itr1 = listObj.iterator();
					while (itr1.hasNext()) {
						Object[] obj1 = (Object[]) itr1.next();
						noOfFiles = (Long) obj1[1];
						noOfUploadedExcelFile = (Long) obj1[3];
						counterNOfUploadedExcelFile++;
					}
				}


				modelAct.put("statusCMACountKey", counterNOfUploadedExcelFile);
				modelAct.put("statusCMAKey", statusCMA1);
				//---------------------------------------------------------------

				/*
				 List<PortfolioDetailsBean> list12 = npaService.getExposureDetailsDASHBOARD(userId);
				 if (!list12.isEmpty()) {
						modelAct.put("ExpoDashboard", list12);
					} else {
						modelAct.put("noDataFound", "NO Data Found !!");
					}
//				//---------------------------------------------------------------------------------------------
				 Object TotalExpiredExpoCount = 0;
//			
					TotalExpiredExpoCount = npaService.getExposureExpiredCount(userId);
					modelAct.put("countTotalNoExpiredExpo",
							TotalExpiredExpoCount);

				//------------------------------------------------------------------
				 */
				modelAct.put("adminlist", userActivityService.getActivity("CGTMSECHECKER", "System_Admin"));
				modelAct.put("guaranteelist", userActivityService.getActivity("CGTMSECHECKER", "Registration"));
				modelAct.put("applicationList", userActivityService.getActivity("CGTMSECHECKER", "Application_Processing"));
				modelAct.put("RPaymentList", userActivityService.getActivity("CGTMSECHECKER", "Receipt_Payments"));
				modelAct.put("GMaintainlist", userActivityService.getActivity("CGTMSECHECKER", "Guarantee_Maintenance"));
				// end---------------------------------------------------------------------------------------
				// modelAct.put("actList", userActivityService.getActivity(
				// "CGTMSECHECKER", "User_Activity"));
				modelAct.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
				modelAct.put("CBMFList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Bank_Mandate"));
				modelAct.put("repList", userActivityService.getReport("CGTMSECHECKER", "User_Report"));
				modelAct.put("actNameHome", "CGTMSE");// Added by Say 31 Jan19
				modelAct.put("homePage", "cgtmseCheckerHome");
				return new ModelAndView("successCGTMSECHECKERPage", modelAct);
			}
			if (userIdAndPasswordIsCorrect.equals("success") && User.equals("NMAKER")) {
				log.info("ROLE IS NMAKER==" + session.getAttribute("userId"));
				login = loginService.userDetails(loginBean.getUsr_id());
				session.setAttribute("uName", login.getUSR_FIRST_NAME() + " "+ login.getUSR_LAST_NAME());
				session.setAttribute("userId", loginBean.getUsr_id());
				session.setAttribute("uRole", "NMAKER");// added by say
				getMLINameBean = nbfcUserReportService.getMliDetails(loginBean.getUsr_id());
				session.setAttribute("userInfo",userService.getUserInfo(loginBean.getUsr_id()));

				session.setAttribute("mliName", getMLINameBean.getMliName());

				rows = nbfcUserReportService.getUserDashboardDetails(loginBean.getUsr_id());
				userDashboardBean = new UserDashboardBean();
				userDashboardBean = createUserDashboardBean(userActivityService.getUserDashboardDetails(loginBean.getUsr_id()));
				if (userDashboardBean != null) {
					modelAct.put("userDashboardBean", userDashboardBean);
				}
				modelAct.put("rows", rows);
				// added by say 6 feb-----------------------
				modelAct.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
				modelAct.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
				modelAct.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
				modelAct.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
				modelAct.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
				modelAct.put("GMaintainlist", userActivityService.getActivity("NBFCMAKER", "Guarantee_Maintenance"));
				modelAct.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
				modelAct.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
				// end---------------------------------------------------------------------------------------

				// modelAct.put("actList", userActivityService.getActivity(
				// "NBFCMAKER", "User_Activity"));

				modelAct.put("actNameHome", "CGTMSE");// Added by Say 31 Jan19
				modelAct.put("homePage", "nbfcMakerHome");
				return new ModelAndView("successMliMakerScreen", modelAct);
			}
			if (userIdAndPasswordIsCorrect.equalsIgnoreCase("success") && User.equals("NCHECKER")) {
				log.info("ROLE IS NCHECKER==" + session.getAttribute("userId"));
				login = loginService.userDetails(loginBean.getUsr_id());
				session.setAttribute("uName", login.getUSR_FIRST_NAME() + " "+ login.getUSR_LAST_NAME());
				session.setAttribute("userId", loginBean.getUsr_id());
				session.setAttribute("uRole", "NCHECKER");// added by say
				rows = nbfcUserReportService.getUserDashboardDetails(loginBean.getUsr_id());
				getMLINameBean = nbfcUserReportService.getMliDetails(loginBean.getUsr_id());

				session.setAttribute("mliName", getMLINameBean.getMliName());
				userDashboardBean = createUserDashboardBean(userActivityService.getUserDashboardDetails(loginBean.getUsr_id()));
				if (userDashboardBean != null) {
					modelAct.put("userDashboardBean", userDashboardBean);
				}
				modelAct.put("rows", rows);
				// added by say 6
				// feb--------------------------------------------------------------
				modelAct.put("adminlist", userActivityService.getActivity("NBFCCHECKER", "System_Admin"));
				modelAct.put("guaranteelist", userActivityService.getActivity("NBFCCHECKER", "Registration"));
				modelAct.put("applicationList", userActivityService.getActivity("NBFCCHECKER", "Application_Processing"));
				modelAct.put("RPaymentList", userActivityService.getActivity("NBFCCHECKER", "Receipt_Payments"));
				modelAct.put("GMaintainlist", userActivityService.getActivity("NBFCCHECKER", "Guarantee_Maintenance"));

				modelAct.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
				modelAct.put("CBMFList", userActivityService.getActivity("NBFCCHECKER", "Claim_Bank_Mandate"));
				// end---------------------------------------------------------------------------------------

				//
				// modelAct.put("actList", userActivityService.getActivity(
				// "NBFCCHECKER", "User_Activity"));
				modelAct.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
				modelAct.put("actNameHome", "CGTMSE");// Added by Say 31 Jan19
				modelAct.put("homePage", "nbfcCheckerHome");
				return new ModelAndView("successMliCheckerScreen", modelAct);
			} else {
				return new ModelAndView("login");
			}

		}
	}

	@RequestMapping(value = "adminHome", method = RequestMethod.GET)
	public ModelAndView adminHome(
			@ModelAttribute("command") LoginBean loginBean,	BindingResult result, Model model, HttpSession session,HttpServletRequest request) {
		log.info("adminHome*************************"+ session.getAttribute("userId"));
		Map<String, Object> modelAct = new HashMap<String, Object>();
	
		HttpSession LoginSession=request.getSession(false);
		if(LoginSession ==null) {
				return new ModelAndView("redirect:/nbfcLogin.html");
		}

		modelAct.put("adminlist",
				userActivityService.getActivity("ADMIN", "System_Admin"));
		modelAct.put("repList",
				userActivityService.getReport("ADMIN", "User_Report"));
		modelAct.put("actNameHome", "HOME");// Added by Say 31 Jan19
		modelAct.put("homePage", "adminHome");
		return new ModelAndView("adminPage", modelAct);
	}

	@RequestMapping(value = "cgtmseCheckerHome", method = RequestMethod.GET)
	public ModelAndView cgtmseCheckerHome(
			@ModelAttribute("command") LoginBean loginBean,
			BindingResult result, Model model,HttpServletRequest request, HttpSession session) {
		log.info("cgtmseCheckerHome***************************************************************************************************************"
				+ session.getAttribute("userId"));
		Map<String, Object> modelAct = new HashMap<String, Object>();
		String userId = (String) session.getAttribute("userId");
		HttpSession LoginSession=request.getSession(false);
		if(LoginSession ==null || userId==null) {
				return new ModelAndView("redirect:/nbfcLogin.html");
		}

		
		CGTMSECheckerForBatchApprovalGetStatus cgtmseCheckerForBatchApprovalGetStatus = new CGTMSECheckerForBatchApprovalGetStatus();
		// session.setAttribute("userId", loginBean.getUsr_id());
		// userDashboardBean = createUserDashboardBean(userActivityService
		// .getUserDashboardDetails(userId));
		// if (userDashboardBean != null) {
		// modelAct.put("userDashboardBean", userDashboardBean);
		// }

		// ---------------Added by sayali
		// 18march19---------------------------------------------
		// Get All the status
		List<Object> list1 = new ArrayList<Object>();
		String statusCMA1 = "CMA";
		Long noOfUploadedExcelFile = (long) 0;
		Integer counterNOfUploadedExcelFile = 0;
		Long noOfUploadedExcelFileCount = (long) 0;
		log.info("statusCMA1==" + statusCMA1);
		cgtmseCheckerForBatchApprovalGetStatus.setStatus(statusCMA1);
		int counter = 0;
		Long noOfFiles = (long) 0;
		List<Object> listObj = cgtmseCheckerForBatchApprovalService
				.getCMAStatusCount(cgtmseCheckerForBatchApprovalGetStatus);
		log.info("listObj size==" + listObj.size());
		log.info("listObj Data==" + listObj);
		if (listObj.size() != 0) {
			Iterator<Object> itr1 = listObj.iterator();
			while (itr1.hasNext()) {
				Object[] obj1 = (Object[]) itr1.next();
				noOfFiles = (Long) obj1[1];
				noOfUploadedExcelFile = (Long) obj1[3];
				counterNOfUploadedExcelFile++;
			}
		}
		
		//---------------------------------------------------------------
		  List<ASFGenerationDetailBean> list = ASFGenerationService.getASFDetailsByFileWise(userId);
			if (!list.isEmpty()) {
				modelAct.put("danDetailList", list);
			} else {
				modelAct.put("noDataFound", "NO Data Found !!");
			}
		//------------------------------------------------------------------
		modelAct.put("statusCMACountKey", counterNOfUploadedExcelFile);
		modelAct.put("statusCMAKey", statusCMA1);
		// added by say 6 feb-----------------------
		
		 List<PortfolioDetailsBean> list12 = npaService.getExposureDetailsDASHBOARD(userId);
		 if (!list12.isEmpty()) {
				modelAct.put("ExpoDashboard", list12);
			} else {
				modelAct.put("noDataFound", "NO Data Found !!");
			}
		modelAct.put("adminlist", userActivityService.getActivity(
				"CGTMSECHECKER", "System_Admin"));
		modelAct.put("guaranteelist", userActivityService.getActivity(
				"CGTMSECHECKER", "Registration"));
		modelAct.put("applicationList", userActivityService.getActivity(
				"CGTMSECHECKER", "Application_Processing"));
		modelAct.put("RPaymentList", userActivityService.getActivity(
				"CGTMSECHECKER", "Receipt_Payments"));
		modelAct.put("GMaintainlist", userActivityService.getActivity(
				"CGTMSECHECKER", "Guarantee_Maintenance"));
		// end---------------------------------------------------------------------------------------

		// modelAct.put("actList", userActivityService.getActivity(
		// "CGTMSECHECKER", "User_Activity"));
		modelAct.put("repList",
				userActivityService.getReport("CGTMSECHECKER", "User_Report"));
		modelAct.put("actNameHome", "HOME");// Added by Say 31 Jan19
		modelAct.put("homePage", "cgtmseCheckerHome");
		return new ModelAndView("successCGTMSECHECKERPage", modelAct);
	}

	@RequestMapping(value = "cgtmseMakerHome", method = RequestMethod.GET)
	public ModelAndView cgtmseMakerHome(
			@ModelAttribute("command") LoginBean loginBean,
			BindingResult result, Model model, HttpSession session,HttpServletRequest request) {
		log.info("cgtmseMakerHome***************************************************************************************************************"
				+ session.getAttribute("userId"));
		
		String userId =(String)session.getAttribute("userId");
		
		HttpSession LoginSession=request.getSession(false);
		if(LoginSession ==null || userId==null) {
				return new ModelAndView("redirect:/nbfcLogin.html");
		}

		Map<String, Object> modelAct = new HashMap<String, Object>();
		// String userId = (String) session.getAttribute("userId");
		// Added by sayali
		// 18march19------------------------------------------------------------------------------------------------------------------
		// userDashboardBean = createUserDashboardBean(userActivityService
		// .getUserDashboardDetails(userId));
		// if (userDashboardBean != null) {
		// modelAct.put("userDashboardBean", userDashboardBean);
		// }
		//
		CGTMSEMakerForBatchApprovalGetStatus cgtmseMakerForBatchApprovalGetStatus = new CGTMSEMakerForBatchApprovalGetStatus();
		String statusNCA = null;
		int statusNCACount = 0;
		Integer subPortfolioDtlNo = 0;
		Object totalNoOfUploadedFile = 0;
		String status = null;
		List<Object> list1 = new ArrayList<Object>();
		String status1 = "NCA";
		cgtmseMakerForBatchApprovalGetStatus.setStatus(status1);
		Integer listObj5 = cgtmseMakerForBatchApprovalGetStatusService
				.getNCAStatusCountBasedOnNCAStatus(cgtmseMakerForBatchApprovalGetStatus);
		int countTotalNoOfUploadedFile = 0;
		/*
		 * Iterator<Object> itr2 = listObj5.iterator(); while (itr2.hasNext()) {
		 * Object[] obj1 = (Object[]) itr2.next(); subPortfolioDtlNo = (Integer)
		 * obj1[0]; totalNoOfUploadedFile = obj1[1]; status = (String) obj1[2];
		 * countTotalNoOfUploadedFile++; }
		 */
		List<Object> list3 = new ArrayList<Object>();
		String status3 = "CCR";
		log.info("Status==" + status3);
		cgtmseMakerForBatchApprovalGetStatus.setStatus(status3);
		List<Object> listObj6 = cgtmseMakerForBatchApprovalGetStatusService
				.getNoOfBatchRejectedByCGTMSE(cgtmseMakerForBatchApprovalGetStatus);
		log.info("listObj6 size==" + listObj6.size());
		log.info("listObj6 ==" + listObj6);
		Iterator<Object> itr4 = listObj6.iterator();

		int rejectedFileCount = 0;
		while (itr4.hasNext()) {
			int countNoOfBatchRejected = 0;
			Object[] obj4 = (Object[]) itr4.next();
			rejectedFileCount++;
		}
		//---------------------------------------------------------------
		  List<ASFGenerationDetailBean> list = ASFGenerationService.getASFDetailsByFileWise(userId);
			if (!list.isEmpty()) {
				modelAct.put("danDetailList", list);
			} else {
				modelAct.put("noDataFound", "NO Data Found !!");
			}
		//------------------------------------------------------------------
		modelAct.put("countTotalNoOfUploadedFileKey", listObj5);
		modelAct.put("rejectedNoOfFileByCGTMSEKey", rejectedFileCount);
		// end------------------------------------------------------------
		
		
		 List<PortfolioDetailsBean> list12 = npaService.getExposureDetailsDASHBOARD(userId);
		 if (!list12.isEmpty()) {
				modelAct.put("ExpoDashboard", list12);
			} else {
				modelAct.put("noDataFound", "NO Data Found !!");
			}
		 //----------------------------------------------------------------------------
		// added by say 6 feb-----------------------
		// modelAct.put("adminlist",
		// userActivityService.getActivity("CGTMSEMAKER", "System_Admin"));
		modelAct.put("guaranteelist",
				userActivityService.getActivity("CGTMSEMAKER", "Registration"));
		modelAct.put("applicationList", userActivityService.getActivity(
				"CGTMSEMAKER", "Application_Processing"));
		modelAct.put("RPaymentList", userActivityService.getActivity(
				"CGTMSEMAKER", "Receipt_Payments"));
		modelAct.put("GMaintainlist", userActivityService.getActivity(
				"CGTMSEMAKER", "Guarantee_Maintenance"));
		// end---------------------------------------------------------------------------------------

		// modelAct.put("actList",
		// userActivityService.getActivity("CGTMSEMAKER", "User_Activity"));
		modelAct.put("repList",
				userActivityService.getReport("CGTMSEMAKER", "User_Report"));
		modelAct.put("actNameHome", "HOME");// Added by Say 31 Jan19
		modelAct.put("homePage", "cgtmseMakerHome");
		return new ModelAndView("successCGTMSEMAKERPage", modelAct);
	}

	@RequestMapping(value = "cgtmseGetMakerHome", method = RequestMethod.POST)
	public ModelAndView cgtmseGetMakerHome(
			@ModelAttribute("command") LoginBean loginBean,
			BindingResult result, Model model, HttpSession session) {
		log.info("cgtmseMakerHome***************************************************************************************************************"
				+ session.getAttribute("userId"));
		Map<String, Object> modelAct = new HashMap<String, Object>();
		// added by say 6 feb-----------------------
		modelAct.put("adminlist",
				userActivityService.getActivity("CGTMSEMAKER", "Registration"));
		modelAct.put("guaranteelist",
				userActivityService.getActivity("CGTMSEMAKER", "Registration"));
		modelAct.put("applicationList", userActivityService.getActivity(
				"CGTMSEMAKER", "Application_Processing"));
		modelAct.put("RPaymentList", userActivityService.getActivity(
				"CGTMSEMAKER", "Receipt_Payments"));
		modelAct.put("GMaintainlist", userActivityService.getActivity(
				"CGTMSEMAKER", "Guarantee_Maintenance"));
		// end---------------------------------------------------------------------------------------
		// modelAct.put("actList",
		// userActivityService.getActivity("CGTMSEMAKER", "User_Activity"));
		modelAct.put("repList",
				userActivityService.getReport("CGTMSEMAKER", "User_Report"));
		modelAct.put("actNameHome", "HOME");// Added by Say 31 Jan19
		modelAct.put("homePage", "cgtmseMakerHome");
		return new ModelAndView("successCGTMSEMAKERPage", modelAct);
	}

	@RequestMapping(value = "nbfcMakerHome", method = RequestMethod.GET)
	public ModelAndView nbfcMakerHome(
			@ModelAttribute("command") LoginBean loginBean,
			BindingResult result, Model model, HttpSession session) {
		log.info("nbfcMakerHome***************************************************************************************************************"
				+ session.getAttribute("userId"));
		String userId = (String) session.getAttribute("userId");
		Map<String, Object> modelAct = new HashMap<String, Object>();

		getMLINameBean = nbfcUserReportService.getMliDetails(userId);

		session.setAttribute("mliName", getMLINameBean.getMliName());
		userDashboardBean = new UserDashboardBean();
		userDashboardBean = createUserDashboardBean(userActivityService
				.getUserDashboardDetails(userId));
		if (userDashboardBean != null) {
			modelAct.put("userDashboardBean", userDashboardBean);
		}
		//---------------------------------------------------------------
		  List<ASFGenerationDetailBean> list = ASFGenerationService.getASFDetailsByFileWise(userId);
			if (!list.isEmpty()) {
				modelAct.put("danDetailList", list);
			} else {
				modelAct.put("noDataFound", "NO Data Found !!");
			}
		//------------------------------------------------------------------
		rows = nbfcUserReportService.getUserDashboardDetails(userId);
		modelAct.put("rows", rows);
		// added by say 6 feb-----------------------
		modelAct.put("adminlist",
				userActivityService.getActivity("NBFCMAKER", "System_Admin"));
		modelAct.put("guaranteelist",
				userActivityService.getActivity("NBFCMAKER", "Registration"));
		modelAct.put("applicationList", userActivityService.getActivity(
				"NBFCMAKER", "Application_Processing"));
		modelAct.put("RPaymentList", userActivityService.getActivity(
				"NBFCMAKER", "Receipt_Payments"));
		modelAct.put("GMaintainlist", userActivityService.getActivity("NBFCMAKER", "Guarantee_Maintenance"));
		modelAct.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
		// end---------------------------------------------------------------------------------------
		// modelAct.put("actList",
		// userActivityService.getActivity("NBFCMAKER", "User_Activity"));
		modelAct.put("repList",
				userActivityService.getReport("NBFCMAKER", "User_Report"));
		modelAct.put("actNameHome", "HOME");// Added by Say 31 Jan19
		modelAct.put("homePage", "nbfcMakerHome");
		return new ModelAndView("successMliMakerScreen", modelAct);
	}

	@RequestMapping(value = "nbfcCheckerHome", method = RequestMethod.GET)
	public ModelAndView nbfcCheckerHome(
			@ModelAttribute("command") LoginBean loginBean,
			BindingResult result, Model model, HttpSession session) {
		log.info("nbfcCheckerHome***************************************************************************************************************"
				+ session.getAttribute("userId"));
		String userId = (String) session.getAttribute("userId");
		getMLINameBean = nbfcUserReportService.getMliDetails(userId);

		session.setAttribute("mliName", getMLINameBean.getMliName());

		Map<String, Object> modelAct = new HashMap<String, Object>();
		userDashboardBean = new UserDashboardBean();
		userDashboardBean = createUserDashboardBean(userActivityService
				.getUserDashboardDetails(userId));
		rows = nbfcUserReportService.getUserDashboardDetails(userId);
		userDashboardBean = createUserDashboardBean(userActivityService
				.getUserDashboardDetails(loginBean.getUsr_id()));

		modelAct.put("rows", rows);
		if (userDashboardBean != null) {
			modelAct.put("userDashboardBean", userDashboardBean);
		}
		//---------------------------------------------------------------
		  List<ASFGenerationDetailBean> list = ASFGenerationService.getASFDetailsByFileWise(userId);
			if (!list.isEmpty()) {
				modelAct.put("danDetailList", list);
			} else {
				modelAct.put("noDataFound", "NO Data Found !!");
			}
		//------------------------------------------------------------------
		// added by say 6 feb-----------------------
		modelAct.put("adminlist",
				userActivityService.getActivity("NBFCCHECKER", "System_Admin"));
		modelAct.put("guaranteelist",
				userActivityService.getActivity("NBFCCHECKER", "Registration"));
		modelAct.put("applicationList", userActivityService.getActivity(
				"NBFCCHECKER", "Application_Processing"));
		modelAct.put("RPaymentList", userActivityService.getActivity(
				"NBFCCHECKER", "Receipt_Payments"));
		modelAct.put("GMaintainlist", userActivityService.getActivity(
				"NBFCCHECKER", "Guarantee_Maintenance"));
		// end---------------------------------------------------------------------------------------
		// modelAct.put("actList",
		// userActivityService.getActivity("NBFCCHECKER", "User_Activity"));
		modelAct.put("repList",
				userActivityService.getActivity("NBFCCHECKER", "User_Report"));
		modelAct.put("actNameHome", "HOME");// Added by Say 31 Jan19
		modelAct.put("homePage", "nbfcCheckerHome");
		return new ModelAndView("successMliCheckerScreen", modelAct);
	}

	@RequestMapping(value = "nbfcCheckerBackHome", method = RequestMethod.POST)
	public ModelAndView nbfcCheckerBackHome(
			@ModelAttribute("command") LoginBean loginBean,
			BindingResult result, Model model, HttpSession session) {

		String userId = (String) session.getAttribute("userId");
		log.info("nbfcCheckerHome***************************************************************************************************************"
				+ userId);
		getMLINameBean = nbfcUserReportService.getMliDetails(userId);

		session.setAttribute("mliName", getMLINameBean.getMliName());
		Map<String, Object> modelAct = new HashMap<String, Object>();

		rows = nbfcUserReportService.getUserDashboardDetails(userId);
		modelAct.put("rows", rows);
		//---------------------------------------------------------------
		  List<ASFGenerationDetailBean> list = ASFGenerationService.getASFDetailsByFileWise(userId);
			if (!list.isEmpty()) {
				modelAct.put("danDetailList", list);
			} else {
				modelAct.put("noDataFound", "NO Data Found !!");
			}
		//------------------------------------------------------------------
		// added by say 6 feb-----------------------
		modelAct.put("adminlist",
				userActivityService.getActivity("NBFCCHECKER", "System_Admin"));
		modelAct.put("guaranteelist",
				userActivityService.getActivity("NBFCCHECKER", "Registration"));
		modelAct.put("applicationList", userActivityService.getActivity(
				"NBFCCHECKER", "Application_Processing"));
		modelAct.put("RPaymentList", userActivityService.getActivity(
				"NBFCCHECKER", "Receipt_Payments"));
		modelAct.put("GMaintainlist", userActivityService.getActivity(
				"NBFCCHECKER", "Guarantee_Maintenance"));
		// end------------------------------------------------------------------------------------
		// modelAct.put("actList",
		// userActivityService.getActivity("NBFCCHECKER", "User_Activity"));
		modelAct.put("repList",
				userActivityService.getActivity("NBFCCHECKER", "User_Report"));
		modelAct.put("actNameHome", "HOME");// Added by Say 31 Jan19
		modelAct.put("homePage", "nbfcCheckerHome");
		return new ModelAndView("successMliCheckerScreen", modelAct);
	}

	@RequestMapping(value = "nbfcCheckerHomeBack", method = RequestMethod.POST)
	public ModelAndView nbfcBackCheckerHome(
			@ModelAttribute("command") LoginBean loginBean,
			BindingResult result, Model model, HttpSession session) {
		log.info("nbfcCheckerHome***************************************************************************************************************"
				+ session.getAttribute("userId"));
		String userId = (String) session.getAttribute("userId");
		getMLINameBean = nbfcUserReportService.getMliDetails(userId);

		session.setAttribute("mliName", getMLINameBean.getMliName());
		Map<String, Object> modelAct = new HashMap<String, Object>();

		rows = nbfcUserReportService.getUserDashboardDetails(userId);
		modelAct.put("rows", rows);
		// added by say 6 feb-----------------------
		modelAct.put("adminlist",
				userActivityService.getActivity("NBFCCHECKER", "System_Admin"));
		modelAct.put("guaranteelist",
				userActivityService.getActivity("NBFCCHECKER", "Registration"));
		modelAct.put("applicationList", userActivityService.getActivity(
				"NBFCCHECKER", "Application_Processing"));
		modelAct.put("RPaymentList", userActivityService.getActivity(
				"NBFCCHECKER", "Receipt_Payments"));
		modelAct.put("GMaintainlist", userActivityService.getActivity(
				"NBFCCHECKER", "Guarantee_Maintenance"));
		// end------------------------------------------------------------------------------------
		// modelAct.put("actList",
		// userActivityService.getActivity("NBFCCHECKER", "User_Activity"));
		modelAct.put("repList",
				userActivityService.getActivity("NBFCCHECKER", "User_Report"));
		modelAct.put("homePage", "nbfcCheckerHome");
		modelAct.put("actNameHome", "HOME");// Added by Say 31 Jan19
		return new ModelAndView("successMliCheckerScreen", modelAct);
	}

	@RequestMapping(value = "nbfcMakerHomeBack", method = RequestMethod.POST)
	public ModelAndView nbfcMLIMakerHome(
			@ModelAttribute("command") LoginBean loginBean,
			BindingResult result, Model model, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		log.info("nbfcMakerHome***************************************************************************************************************"
				+ userId);

		getMLINameBean = nbfcUserReportService.getMliDetails(userId);

		session.setAttribute("mliName", getMLINameBean.getMliName());
		rows = nbfcUserReportService.getUserDashboardDetails(userId);
		Map<String, Object> modelAct = new HashMap<String, Object>();
		modelAct.put("rows", rows);
		modelAct.put("repList",
				userActivityService.getReport("NBFCMAKER", "User_Report"));
		// added by say 6 feb-----------------------
		modelAct.put("adminlist",
				userActivityService.getActivity("NBFCMAKER", "System_Admin"));
		modelAct.put("guaranteelist",
				userActivityService.getActivity("NBFCMAKER", "Registration"));
		modelAct.put("applicationList", userActivityService.getActivity(
				"NBFCMAKER", "Application_Processing"));
		modelAct.put("RPaymentList", userActivityService.getActivity(
				"NBFCMAKER", "Receipt_Payments"));
		modelAct.put("GMaintainlist", userActivityService.getActivity(
				"NBFCMAKER", "Guarantee_Maintenance"));
		modelAct.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
		// end------------------------------------------------------------------------------------
		// modelAct.put("actList",
		// userActivityService.getActivity("NBFCMAKER", "User_Activity"));
		modelAct.put("actNameHome", "HOME");// Added by Say 31 Jan19
		modelAct.put("homePage", "nbfcMakerHome");
		return new ModelAndView("successMliMakerScreen", modelAct);
	}

	public Login preChangePasswordModal(LoginBean loginBean, Login login) {
		Login loginEntity = new Login();

		loginEntity.setADDRESS(login.getADDRESS());
		loginEntity.setCITY(login.getCITY());
		loginEntity.setDISTRICT(login.getDISTRICT());
		loginEntity.setFAX_NO(login.getFAX_NO());
		loginEntity.setHINT_ANS(login.getHINT_ANS());
		loginEntity.setHINT_QUESTION(login.getHINT_QUESTION());
		loginEntity.setLOGIN_STATUS("Y");
		loginEntity.setMEM_BNK_ID(login.getMEM_BNK_ID());
		loginEntity.setMEM_BRN_ID(login.getMEM_BRN_ID());
		loginEntity.setMEM_ZNE_ID(login.getMEM_ZNE_ID());
		loginEntity.setMOBILE_NO(login.getMOBILE_NO());
		// loginEntity.setPasswordChangeDate(login.getPasswordChangeDate());
		loginEntity.setPHONE_CODE(login.getPHONE_CODE());
		loginEntity.setPHONE_NO(login.getPHONE_NO());
		loginEntity.setPIN_CODE(login.getPIN_CODE());
		loginEntity.setSTATE(login.getSTATE());
		loginEntity.setUSR_CREATED_BY(login.getUSR_CREATED_BY());
		loginEntity.setUSR_CREATED_DT(login.getUSR_CREATED_DT());
		loginEntity.setUSR_DSG_NAME(login.getUSR_DSG_NAME());
		loginEntity.setUSR_EMAIL_ID(login.getUSR_EMAIL_ID());
		loginEntity.setUSR_FIRST_NAME(login.getUSR_FIRST_NAME());
		loginEntity.setUSR_HINT_ANSWER(login.getUSR_HINT_ANSWER());
		loginEntity.setUsr_id(login.getUsr_id());
		loginEntity.setUSR_LAST_NAME(login.getUSR_LAST_NAME());
		loginEntity.setUSR_MIDDLE_NAME(login.getUSR_MIDDLE_NAME());
		loginEntity.setUSR_MODIFIED_BY(login.getUSR_MODIFIED_BY());
		loginEntity.setUSR_MODIFIED_DT(login.getUSR_MODIFIED_DT());
		loginEntity.setUSR_OLD_USR_ID(login.getUSR_OLD_USR_ID());
		loginEntity.setUsr_password(loginBean.getUsr_password());
		loginEntity.setUSR_STATUS(login.getUSR_STATUS());
		loginEntity.setUSR_TYPE(login.getUSR_TYPE());

		loginEntity.setUsr_id(login.getUsr_id());

		loginEntity.setUsr_password(loginBean.getNewPassword());

		return loginEntity;

	}

	@ExceptionHandler(CustomExceptionHandler.class)
	public ModelAndView handleCustomException(CustomExceptionHandler ex) {
		Map<String, Object> model1 = new HashMap<String, Object>();

		// model1.put("actList", userActivityService.getActivity("NBFCCHECKER",
		// "User_Activity"));
		// model1.put("homePage","nbfcCheckerHome");
		ModelAndView model = new ModelAndView("customException", model1);
		model.addObject("customException", ex.getMessage());
		return model;

	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(Exception ex) {
		Map<String, Object> model1 = new HashMap<String, Object>();

		// model1.put("actList", userActivityService.getActivity("NBFCCHECKER",
		// "User_Activity"));
		// model1.put("homePage","nbfcCheckerHome");
		ModelAndView model = new ModelAndView("exception", model1);
		model.addObject("exception", ex.getCause());
		return model;

	}

	@ExceptionHandler(ArithmeticException.class)
	public ModelAndView handleArithException(ArithmeticException ex) {
		Map<String, Object> model1 = new HashMap<String, Object>();

		// model1.put("actList", userActivityService.getActivity("NBFCCHECKER",
		// "User_Activity"));
		// model1.put("homePage","nbfcCheckerHome");
		ModelAndView model = new ModelAndView("exception", model1);
		model.addObject("exception", ex.getMessage());
		return model;

	}

	@ExceptionHandler(NullPointerException.class)
	public ModelAndView handleNullException(NullPointerException ex) {
		Map<String, Object> model1 = new HashMap<String, Object>();

		// model1.put("actList", userActivityService.getActivity("NBFCCHECKER",
		// "User_Activity"));
		// model1.put("homePage","nbfcCheckerHome");
		ModelAndView model = new ModelAndView("exception", model1);
		model.addObject("exception", "Data is null");
		return model;

	}

	private UserDashboardBean createUserDashboardBean(
			UserDashboardVmodel userDashboardVmodel) {
		// TODO Auto-generated method stub

		UserDashboardBean udb = new UserDashboardBean();
		if (userDashboardVmodel != null) {
			udb.setCck_t_dan(userDashboardVmodel.getCck_t_dan());
			udb.setCck_t_dan_all_approval(userDashboardVmodel
					.getCck_t_dan_all_approval());
			udb.setCmk_t_batch_approval(userDashboardVmodel
					.getCmk_t_batch_approval());
			udb.setCmk_t_batch_approved(userDashboardVmodel
					.getCmk_t_batch_approved());
			udb.setCmk_t_batch_pending(userDashboardVmodel
					.getCmk_t_batch_pending());
			udb.setCmk_t_batch_reject(userDashboardVmodel
					.getCmk_t_batch_reject());
			udb.setCmk_t_exposure(userDashboardVmodel.getCmk_t_exposure());
			udb.setCmk_t_exposure_approved(userDashboardVmodel
					.getCmk_t_exposure_approved());
			udb.setCmk_t_exposure_count(userDashboardVmodel
					.getCmk_t_exposure_count());
			udb.setCmk_t_exposure_pending(userDashboardVmodel
					.getCmk_t_exposure_pending());
			udb.setCmk_t_exposure_reject(userDashboardVmodel
					.getCmk_t_exposure_reject());
			udb.setCmk_t_fridge_portfolio(userDashboardVmodel
					.getCmk_t_fridge_portfolio());
			udb.setCmk_t_gurantee_fee(userDashboardVmodel
					.getCmk_t_gurantee_fee());
			udb.setCmk_t_mli_approve(userDashboardVmodel.getCmk_t_mli_approve());
			udb.setCmk_t_mli_pending(userDashboardVmodel.getCmk_t_mli_pending());
			udb.setCmk_t_mli_reg(userDashboardVmodel.getCmk_t_mli_reg());
			udb.setCmk_t_mli_reject(userDashboardVmodel.getCmk_t_mli_reject());
			udb.setCmk_t_portfolio(userDashboardVmodel.getCmk_t_portfolio());
			udb.setCmk_t_used_exposure(userDashboardVmodel
					.getCmk_t_used_exposure());
			udb.setMli_id(userDashboardVmodel.getMli_id());
			udb.setNck_dan_allocation_approval(userDashboardVmodel
					.getNck_dan_allocation_approval());
			udb.setNck_dan_payment_approval(userDashboardVmodel
					.getNck_dan_payment_approval());
			udb.setNck_file_approval(userDashboardVmodel.getNck_file_approval());
			udb.setNkm_total_upload_file(userDashboardVmodel
					.getNkm_total_upload_file());
			udb.setNmk_dan_pay_approval(userDashboardVmodel
					.getNmk_dan_pay_approval());
			udb.setNmk_exposure_lmt(userDashboardVmodel.getNmk_exposure_lmt());
			udb.setNmk_fridge_portfolio(userDashboardVmodel
					.getNmk_fridge_portfolio());
			udb.setNmk_running_portfolio(userDashboardVmodel
					.getNmk_running_portfolio());
			udb.setNmk_t_gurantee_fee(userDashboardVmodel
					.getNmk_t_gurantee_fee());
			udb.setNmk_total_approv(userDashboardVmodel.getNmk_total_approv());
			udb.setNmk_total_pending(userDashboardVmodel.getNmk_total_pending());
			udb.setNmk_total_portfolio(userDashboardVmodel
					.getNmk_total_portfolio());
			udb.setNmk_total_reject(userDashboardVmodel.getNmk_total_reject());
			udb.setNmk_unused_exposure(userDashboardVmodel
					.getNmk_unused_exposure());
			udb.setNmk_used_exposure(userDashboardVmodel.getNmk_used_exposure());

		}

		return udb;
	}

}
