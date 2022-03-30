package com.nbfc.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.text.SimpleDateFormat;
import com.nbfc.bean.JsonResponse2;
import com.nbfc.bean.MLIDEtailsBean;
import com.nbfc.bean.MLIRegBean;
import com.nbfc.bean.StateBean;
import com.nbfc.bean.UserBean;
import com.nbfc.common.utility.method.NBFCCommonUtility;
import com.nbfc.exception.NBFCException;
import com.nbfc.model.BankDetails;
import com.nbfc.model.District;
import com.nbfc.model.MLIRegistration;
import com.nbfc.model.NBFCPrivilegeMaster;
import com.nbfc.model.NewMLIRegistration;
import com.nbfc.model.StateMaster;
import com.nbfc.model.UserPerivilegeDetails;
import com.nbfc.service.DistrictService;
import com.nbfc.service.LoginService;
import com.nbfc.service.MLIDetailsService;
import com.nbfc.service.MLIRegService;
import com.nbfc.service.StateService;
import com.nbfc.service.UserActivityService;
import com.nbfc.validation.EmployeeValidator;
import com.raistudies.domain.CustomExceptionHandler;

@Controller
public class NewMLIRegistrationController {

	@Autowired
	LoginService lofinService;

	@Autowired
	MLIDetailsService mliDetailsService;
	@Autowired
	DistrictService districtService;
	@Autowired
	MLIRegService mliRegService;
	@Autowired
	StateService stateService;
	@Autowired
	EmployeeValidator validator;
	UserPerivilegeDetails userPri;
	NBFCPrivilegeMaster userPrvMst;
	@Autowired
	UserActivityService userActivityService;
	private List<District> userList = new ArrayList<District>();
	static Logger log = Logger.getLogger(NBFCController.class.getName());
	List<BankDetails> bankDetails = new ArrayList<BankDetails>();
	List<Integer> bankId = new ArrayList<Integer>();
	List<Integer> bankBranchId = new ArrayList<Integer>();
	List<Integer> bankZonId = new ArrayList<Integer>();
	String bankBRID = null;
	String bankBRIDTest = null;
	String mliID = null;
	String mliName="";

	@RequestMapping(value = "/newMLIRegistration", method = RequestMethod.GET)
	public ModelAndView newMLIRegistration(
			@ModelAttribute("command") MLIRegBean mliRegistrationBean,
			HttpServletRequest request, HttpSession session, Model modelMsg)
			throws NBFCException {

		Map<String, Object> model = new HashMap<String, Object>();
		// model.put("stateList", "");
		// model.put("reatingAGNList", "");
		String userId = (String) session.getAttribute("userId");
		// if (userId.equals("CGTMSE ADMIN")) { 26-03-2019
		//
		// } else {
		if (userId != null) {
			userPri = lofinService.getUserPrivlageDtl(userId, "A");
			userPrvMst = lofinService.getPrivlageMstDtl(userPri.getPrv_id());
			if (userPrvMst.getPrvCreatedModifiedBy().equals("CMAKER")) {

				model.put("stateList", prepareNewStateListofBean(stateService
						.listStateMaster()));
				model.put("ratingAgencyList", stateService.mliRatingList());

				// added by say 6 feb-----------------------
				model.put("adminlist", userActivityService.getActivity(
						"CGTMSEMAKER", "System_Admin"));
				model.put("guaranteelist", userActivityService.getActivity(
						"CGTMSEMAKER", "Registration"));
				model.put("applicationList", userActivityService.getActivity(
						"CGTMSEMAKER", "Application_Processing"));
				model.put("RPaymentList", userActivityService.getActivity(
						"CGTMSEMAKER", "Receipt_Payments"));
				model.put("repList", userActivityService.getReport(
						"CGTMSEMAKER", "User_Report"));
				model.put("GMaintainlist", userActivityService.getActivity(
						"CGTMSEMAKER", "Guarantee_Maintenance"));
				// end---------------------------------------------------------------------------------------

				// model.put("actList", userActivityService.getActivity(
				// "CGTMSEMAKER", "User_Activity"));
				model.put("actName", userActivityService.getActivityName(
						"CGTMSEMAKER", "mliRegistrationPage"));// Added by Say
																// 31 Jan19
				model.put("district", "--------Select District--------");
				model.put("homePage", "cgtmseMakerHome");
				return new ModelAndView("newMLIRegistration", model);
			} else {
				return new ModelAndView("redirect:/nbfcLogin.html");
			}
		}
		// }
		model.put("ratingAgencyList", stateService.mliRatingList());
		model.put("stateList",
				prepareNewStateListofBean(stateService.listStateMaster()));

		// added by say 6 feb-----------------------
		model.put("adminlist",
				userActivityService.getActivity("CGTMSEMAKER", "System_Admin"));
		model.put("guaranteelist",
				userActivityService.getActivity("CGTMSEMAKER", "Registration"));
		model.put("applicationList", userActivityService.getActivity(
				"CGTMSEMAKER", "Application_Processing"));
		model.put("RPaymentList", userActivityService.getActivity(
				"CGTMSEMAKER", "Receipt_Payments"));
		model.put("repList",
				userActivityService.getReport("CGTMSEMAKER", "User_Report"));
		model.put("GMaintainlist", userActivityService.getActivity(
				"CGTMSEMAKER", "Guarantee_Maintenance"));
		// end------------
		// model.put("actList",
		// userActivityService.getActivity("CGTMSEMAKER", "User_Activity"));
		model.put("actName", userActivityService.getActivityName("CGTMSEMAKER",
				"mliRegistrationPage"));// Added by Say 31 Jan19
		model.put("district", "--------Select District--------");
		model.put("homePage", "cgtmseMakerHome");
		return new ModelAndView("newMLIRegistration", model);

	}

	@ExceptionHandler(NBFCException.class)
	public ModelAndView handleStudentNotFoundException(NBFCException ex) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("exception", ex);
		return new ModelAndView("exceptionPage", model);
	}

	@RequestMapping(value = "/mliRegistrationPageBack", method = RequestMethod.GET)
	public ModelAndView getMLIDetails(
			@ModelAttribute("command") MLIDEtailsBean mliDetails,
			BindingResult result, Model modelMsg, HttpSession session) {

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("mlisList", prepareMLIListofBean(mliDetailsService
				.getMLIDetails(null, "CCA")));
		/*
		 * model.put( "mlisList",
		 * prepareMLIListofBean(mliDetailsService.getMLIAllDetails()));
		 */

		// added by say 6 feb-----------------------
		model.put("adminlist",
				userActivityService.getActivity("CGTMSEMAKER", "System_Admin"));
		model.put("guaranteelist",
				userActivityService.getActivity("CGTMSEMAKER", "Registration"));
		model.put("applicationList", userActivityService.getActivity(
				"CGTMSEMAKER", "Application_Processing"));
		model.put("RPaymentList", userActivityService.getActivity(
				"CGTMSEMAKER", "Receipt_Payments"));
		model.put("repList",
				userActivityService.getReport("CGTMSEMAKER", "User_Report"));
		model.put("GMaintainlist", userActivityService.getActivity(
				"CGTMSEMAKER", "Guarantee_Maintenance"));
		// end------------
		// model.put("actList",
		// userActivityService.getActivity("CGTMSEMAKER", "User_Activity"));
		model.put("homePage", "cgtmseMakerHome");
		model.put("actName", userActivityService.getActivityName("CGTMSEMAKER",
				"mliRegistrationPage"));// Added by Say 31 Jan19
		return new ModelAndView("mliRegistrationPage", model);
	}

	private List<MLIDEtailsBean> prepareMLIListofBean(
			List<MLIRegistration> employees) {

		List<MLIDEtailsBean> beans = null;
		if (employees != null && !employees.isEmpty()) {
			beans = new ArrayList<MLIDEtailsBean>();
			MLIDEtailsBean bean = null;
			for (MLIRegistration employee : employees) {
				bean = new MLIDEtailsBean();
				bean.setMliLongName(employee.getLongName());
				bean.setShortName(employee.getShortName());
				bean.setCompanyAddress(employee.getCompanyAddress());
				bean.setCity(employee.getCity());
				bean.setCompanyPAN(employee.getCompanyPAN());
				bean.setContactPerson(employee.getContactPerson());
				bean.setEmailId(employee.getEmailId());
				bean.setMobileNUmber(employee.getMobileNUmber());

				if (employee.getStatus().equals("CCA")) {
					bean.setStatus("Approved");
				} else if (employee.getStatus().equals("CME")) {
					bean.setStatus("Pending for Approval(Edit)");
				} else if (employee.getStatus().equals("CCR")) {
					bean.setStatus("Rejected");
				} else if (employee.getStatus().equals("CEMMA")) {
					bean.setStatus("CEMMA");
				} else if (employee.getStatus().equals("CMR")) {
					bean.setStatus("Pending For Approval(New)");
				}

				beans.add(bean);

			}
			Collections.sort(beans);
		}

		return beans;
	}

	@RequestMapping(value = "/ResetMli", method = RequestMethod.GET)
	public ModelAndView resetMliDetails(
			@ModelAttribute("command") MLIRegBean mliRegistrationBean,
			BindingResult result, HttpServletRequest request,
			HttpSession session) throws Exception {
		System.out.println("reset the value......................");
		// List<State> stateList = stateService.listStates();
		Map<String, Object> model = new HashMap<String, Object>();
		log.info("Reset MLI Registration Page***********************************************************************************************************");
		String userId = (String) session.getAttribute("userId");
		System.out
				.println("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
		if (userId.equals("CGTMSE ADMIN")) {

		} else {
			if (userId != null) {
				userPri = lofinService.getUserPrivlageDtl(userId, "A");
				userPrvMst = lofinService
						.getPrivlageMstDtl(userPri.getPrv_id());
				if (userPrvMst.getPrvCreatedModifiedBy().equals("CMAKER")) {

				} else {
					return new ModelAndView("redirect:/nbfcLogin.html");
				}
			}
		}
		model.put("ratingAgencyList", stateService.mliRatingList());
		model.put("stateList",
				prepareNewStateListofBean(stateService.listStateMaster()));
		return new ModelAndView("newMLIRegistration", model);

	}

	@RequestMapping(value = "/updateNewMLIDetails", method = RequestMethod.GET)
	public ModelAndView saveNewMLIDetails(
			@ModelAttribute("command") MLIRegBean mliRegistrationBean,
			BindingResult result, Model modelMsg, HttpSession session)
			throws ParseException {
		Map<String, Object> model = new HashMap<String, Object>();
		bankDetails.clear();
		// System.out.println(mliRegistrationBean.getDistrict()+""+mliRegistrationBean.getMobileNUmber());
		String userId = (String) session.getAttribute("userId");

		System.out
				.println("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
		if (userId.equals("CGTMSE ADMIN")) {

		} else {
			if (userId != null) {
				userPri = lofinService.getUserPrivlageDtl(userId, "A");
				userPrvMst = lofinService
						.getPrivlageMstDtl(userPri.getPrv_id());
				if (userPrvMst.getPrvCreatedModifiedBy().equals("CMAKER")) {

				} else {
					return new ModelAndView("redirect:/nbfcLogin.html");
				}
			}
		}
		validator.mliRegistrationValidator(mliRegistrationBean, result);
		if (result.hasErrors()) {
			log.info("Error in field*******************************************************************************************************************");
			model.put("ratingAgencyList", stateService.mliRatingList());
			model.put("stateList",
					prepareNewStateListofBean(stateService.listStateMaster()));
			model.put("district", mliRegistrationBean.getDistrict());

			// added by say 6 feb-----------------------
			model.put("adminlist", userActivityService.getActivity(
					"CGTMSEMAKER", "System_Admin"));
			model.put("guaranteelist", userActivityService.getActivity(
					"CGTMSEMAKER", "Registration"));
			model.put("applicationList", userActivityService.getActivity(
					"CGTMSEMAKER", "Application_Processing"));
			model.put("RPaymentList", userActivityService.getActivity(
					"CGTMSEMAKER", "Receipt_Payments"));
			model.put("repList",
					userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model.put("GMaintainlist", userActivityService.getActivity(
					"CGTMSEMAKER", "Guarantee_Maintenance"));
			// end------------
			// model.put("actList",
			// userActivityService.getActivity("CGTMSEMAKER",
			// "User_Activity"));
			model.put("actName", userActivityService.getActivityName(
					"CGTMSEMAKER", "mliRegistrationPage"));// Added by Say 31
															// Jan19
			model.put("homePage", "cgtmseMakerHome");
			return new ModelAndView("newMLIRegistration", model);

		}
		// #####
		if (!result.hasErrors()) {
			String A = mliRegistrationBean.getRatingDate();
			Calendar cal1 = Calendar.getInstance(); 
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(A);
			cal1.setTime(date1);
			Date RateDate=cal1.getTime();
//			String currentDate = sdf.format(new Date());
//			Date sysDate = sdf.parse(currentDate);

			System.out.println("RateDate: " + RateDate);
			System.out.println("Current Date " + new Date());

			if (RateDate.compareTo(new Date()) > 0) {
				System.out.println("Future Date not allowed");
				model.put("ratingAgencyList", stateService.mliRatingList());
				model.put("stateList", prepareNewStateListofBean(stateService
						.listStateMaster()));
				model.put("district", mliRegistrationBean.getDistrict());

				// added by say 6 feb-----------------------
				model.put("adminlist", userActivityService.getActivity(
						"CGTMSEMAKER", "System_Admin"));
				model.put("guaranteelist", userActivityService.getActivity(
						"CGTMSEMAKER", "Registration"));
				model.put("applicationList", userActivityService.getActivity(
						"CGTMSEMAKER", "Application_Processing"));
				model.put("RPaymentList", userActivityService.getActivity(
						"CGTMSEMAKER", "Receipt_Payments"));
				model.put("repList", userActivityService.getReport(
						"CGTMSEMAKER", "User_Report"));
				model.put("GMaintainlist", userActivityService.getActivity(
						"CGTMSEMAKER", "Guarantee_Maintenance"));
				// end------------
				// model.put("actList", userActivityService.getActivity(
				// "CGTMSEMAKER", "User_Activity"));
				model.put("actName", userActivityService.getActivityName(
						"CGTMSEMAKER", "mliRegistrationPage"));// Added by Say
																// 31 Jan19
				modelMsg.addAttribute("ratDateMsg",
						"Future Date not allowed !!");
				return new ModelAndView("newMLIRegistration", model);
			} else {
				System.out.println(" Correct date entered");
			}
		}
		// #####
		String str = mliRegistrationBean.getGstinNumber();
		System.out.println(str);
		if (!Character.toString(
				mliRegistrationBean.getGstinNumber().charAt(
						(mliRegistrationBean.getGstinNumber().length()) - 2))
				.equalsIgnoreCase("Z")) {
			model.put("ratingAgencyList", stateService.mliRatingList());
			model.put("stateList",
					prepareNewStateListofBean(stateService.listStateMaster()));
			model.put("district", mliRegistrationBean.getDistrict());

			// added by say 6 feb-----------------------
			model.put("adminlist", userActivityService.getActivity(
					"CGTMSEMAKER", "System_Admin"));
			model.put("guaranteelist", userActivityService.getActivity(
					"CGTMSEMAKER", "Registration"));
			model.put("applicationList", userActivityService.getActivity(
					"CGTMSEMAKER", "Application_Processing"));
			model.put("RPaymentList", userActivityService.getActivity(
					"CGTMSEMAKER", "Receipt_Payments"));
			model.put("repList",
					userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model.put("GMaintainlist", userActivityService.getActivity(
					"CGTMSEMAKER", "Guarantee_Maintenance"));
			// end------------
			// model.put("actList",
			// userActivityService.getActivity("CGTMSEMAKER",
			// "User_Activity"));
			model.put("actName", userActivityService.getActivityName(
					"CGTMSEMAKER", "mliRegistrationPage"));// Added by Say 31
															// Jan19
			modelMsg.addAttribute("gstinNOerror",
					"GSTIN NO is Wrong Formate !!");
			model.put("homePage", "cgtmseMakerHome");

			return new ModelAndView("newMLIRegistration", model);
		}
		bankDetails = mliRegService.getBankDetails();
		System.out.println("" + bankDetails);
		for (BankDetails bankID : bankDetails) {
			bankBRID = bankID.getMEM_BNK_ID();

		}
		System.out.println("bankBRID " + bankBRID);
		if (bankBRID != null) {
			for (BankDetails bankList : bankDetails) {
				bankId.add(Integer.parseInt(bankList.getMEM_BNK_ID()));
				bankBranchId.add(Integer.parseInt(bankList.getMEM_BRN_ID()));
				bankZonId.add(Integer.parseInt(bankList.getMEM_ZNE_ID()));

			}
			for (BankDetails list : bankDetails) {
				System.out.println(list.getLongName());
				System.out.println(mliRegistrationBean.getLongName());
				if (list.getLongName().equalsIgnoreCase(
						mliRegistrationBean.getLongName())) {
					modelMsg.addAttribute("longNameerror",
							" MLI Name already Registred.");
					model.put("ratingAgencyList", stateService.mliRatingList());
					model.put("district", mliRegistrationBean.getDistrict());
					model.put("stateList",
							prepareNewStateListofBean(stateService
									.listStateMaster()));

					// added by say 6 feb-----------------------
					model.put("adminlist", userActivityService.getActivity(
							"CGTMSEMAKER", "System_Admin"));
					model.put("guaranteelist", userActivityService.getActivity(
							"CGTMSEMAKER", "Registration"));
					model.put("applicationList", userActivityService
							.getActivity("CGTMSEMAKER",
									"Application_Processing"));
					model.put("RPaymentList", userActivityService.getActivity(
							"CGTMSEMAKER", "Receipt_Payments"));
					model.put("repList", userActivityService.getReport(
							"CGTMSEMAKER", "User_Report"));
					model.put("GMaintainlist", userActivityService.getActivity(
							"CGTMSEMAKER", "Guarantee_Maintenance"));
					// end------------
					// model.put("actList", userActivityService.getActivity(
					// "CGTMSEMAKER", "User_Activity"));
					model.put("actName", userActivityService.getActivityName(
							"CGTMSEMAKER", "mliRegistrationPage"));// Added by
																	// Say 31
																	// Jan19
					model.put("homePage", "cgtmseMakerHome");
					return new ModelAndView("newMLIRegistration", model);
				} else if (list.getShortName().equalsIgnoreCase(
						mliRegistrationBean.getShortName())) {
					modelMsg.addAttribute("shortNameerror",
							"MLI Short name already Registred.");
					model.put("district", mliRegistrationBean.getDistrict());
					model.put("ratingAgencyList", stateService.mliRatingList());
					model.put("stateList",
							prepareNewStateListofBean(stateService
									.listStateMaster()));

					// added by say 6 feb-----------------------
					model.put("adminlist", userActivityService.getActivity(
							"CGTMSEMAKER", "System_Admin"));
					model.put("guaranteelist", userActivityService.getActivity(
							"CGTMSEMAKER", "Registration"));
					model.put("applicationList", userActivityService
							.getActivity("CGTMSEMAKER",
									"Application_Processing"));
					model.put("RPaymentList", userActivityService.getActivity(
							"CGTMSEMAKER", "Receipt_Payments"));
					model.put("repList", userActivityService.getReport(
							"CGTMSEMAKER", "User_Report"));
					model.put("GMaintainlist", userActivityService.getActivity(
							"CGTMSEMAKER", "Guarantee_Maintenance"));
					// end------------
					// model.put("actList", userActivityService.getActivity(
					// "CGTMSEMAKER", "User_Activity"));
					model.put("actName", userActivityService.getActivityName(
							"CGTMSEMAKER", "mliRegistrationPage"));// Added by
																	// Say 31
																	// Jan19
					model.put("homePage", "cgtmseMakerHome");
					return new ModelAndView("newMLIRegistration", model);
				} else if (list.getRBI_REGISTRATION_NO().equalsIgnoreCase(
						mliRegistrationBean.getRbiReggistrationNumber())) {
					modelMsg.addAttribute("rbiREGerror",
							"RBI REGISTRATION NO already Registred.");
					model.put("district", mliRegistrationBean.getDistrict());
					model.put("ratingAgencyList", stateService.mliRatingList());
					model.put("stateList",
							prepareNewStateListofBean(stateService
									.listStateMaster()));

					// added by say 6 feb-----------------------
					model.put("adminlist", userActivityService.getActivity(
							"CGTMSEMAKER", "System_Admin"));
					model.put("guaranteelist", userActivityService.getActivity(
							"CGTMSEMAKER", "Registration"));
					model.put("applicationList", userActivityService
							.getActivity("CGTMSEMAKER",
									"Application_Processing"));
					model.put("RPaymentList", userActivityService.getActivity(
							"CGTMSEMAKER", "Receipt_Payments"));
					model.put("repList", userActivityService.getReport(
							"CGTMSEMAKER", "User_Report"));
					model.put("GMaintainlist", userActivityService.getActivity(
							"CGTMSEMAKER", "Guarantee_Maintenance"));
					// end------------
					// model.put("actList", userActivityService.getActivity(
					// "CGTMSEMAKER", "User_Activity"));
					model.put("actName", userActivityService.getActivityName(
							"CGTMSEMAKER", "mliRegistrationPage"));// Added by
																	// Say 31
																	// Jan19
					model.put("homePage", "cgtmseMakerHome");
					return new ModelAndView("newMLIRegistration", model);
				} else if (list.getCOMPANY_CIN().equalsIgnoreCase(
						mliRegistrationBean.getCompanyCIN())) {
					modelMsg.addAttribute("companyCINNoGerror",
							"Company CIN NO already Registred.");
					model.put("district", mliRegistrationBean.getDistrict());
					model.put("ratingAgencyList", stateService.mliRatingList());
					model.put("stateList",
							prepareNewStateListofBean(stateService
									.listStateMaster()));

					// added by say 6 feb-----------------------
					model.put("adminlist", userActivityService.getActivity(
							"CGTMSEMAKER", "System_Admin"));
					model.put("guaranteelist", userActivityService.getActivity(
							"CGTMSEMAKER", "Registration"));
					model.put("applicationList", userActivityService
							.getActivity("CGTMSEMAKER",
									"Application_Processing"));
					model.put("RPaymentList", userActivityService.getActivity(
							"CGTMSEMAKER", "Receipt_Payments"));
					model.put("repList", userActivityService.getReport(
							"CGTMSEMAKER", "User_Report"));
					model.put("GMaintainlist", userActivityService.getActivity(
							"CGTMSEMAKER", "Guarantee_Maintenance"));
					// end------------
					// model.put("actList", userActivityService.getActivity(
					// "CGTMSEMAKER", "User_Activity"));
					model.put("actName", userActivityService.getActivityName(
							"CGTMSEMAKER", "mliRegistrationPage"));// Added by
																	// Say 31
																	// Jan19
					model.put("homePage", "cgtmseMakerHome");
					return new ModelAndView("newMLIRegistration", model);
				} else if (list.getCOMPANY_PAN().equalsIgnoreCase(
						mliRegistrationBean.getCompanyPAN())) {
					modelMsg.addAttribute("panOFCompanyerror",
							"COMPANY PAN already Registred.");
					model.put("ratingAgencyList", stateService.mliRatingList());
					model.put("district", mliRegistrationBean.getDistrict());
					model.put("stateList",
							prepareNewStateListofBean(stateService
									.listStateMaster()));

					// added by say 6 feb-----------------------
					model.put("adminlist", userActivityService.getActivity(
							"CGTMSEMAKER", "System_Admin"));
					model.put("guaranteelist", userActivityService.getActivity(
							"CGTMSEMAKER", "Registration"));
					model.put("applicationList", userActivityService
							.getActivity("CGTMSEMAKER",
									"Application_Processing"));
					model.put("RPaymentList", userActivityService.getActivity(
							"CGTMSEMAKER", "Receipt_Payments"));
					model.put("repList", userActivityService.getReport(
							"CGTMSEMAKER", "User_Report"));
					model.put("GMaintainlist", userActivityService.getActivity(
							"CGTMSEMAKER", "Guarantee_Maintenance"));
					// end------------
					// model.put("actList", userActivityService.getActivity(
					// "CGTMSEMAKER", "User_Activity"));
					model.put("actName", userActivityService.getActivityName(
							"CGTMSEMAKER", "mliRegistrationPage"));// Added by
																	// Say 31
																	// Jan19
					model.put("homePage", "cgtmseMakerHome");
					return new ModelAndView("newMLIRegistration", model);
				} else if (list.getGSTIN_NO().equalsIgnoreCase(
						mliRegistrationBean.getGstinNumber())) {
					modelMsg.addAttribute("gstinNOerror",
							"GSTIN NO already Registred.");
					model.put("ratingAgencyList", stateService.mliRatingList());
					model.put("stateList",
							prepareNewStateListofBean(stateService
									.listStateMaster()));
					model.put("district", mliRegistrationBean.getDistrict());

					// added by say 6 feb-----------------------
					model.put("adminlist", userActivityService.getActivity(
							"CGTMSEMAKER", "System_Admin"));
					model.put("guaranteelist", userActivityService.getActivity(
							"CGTMSEMAKER", "Registration"));
					model.put("applicationList", userActivityService
							.getActivity("CGTMSEMAKER",
									"Application_Processing"));
					model.put("RPaymentList", userActivityService.getActivity(
							"CGTMSEMAKER", "Receipt_Payments"));
					model.put("repList", userActivityService.getReport(
							"CGTMSEMAKER", "User_Report"));
					model.put("GMaintainlist", userActivityService.getActivity(
							"CGTMSEMAKER", "Guarantee_Maintenance"));
					// end------------
					// model.put("actList", userActivityService.getActivity(
					// "CGTMSEMAKER", "User_Activity"));
					model.put("actName", userActivityService.getActivityName(
							"CGTMSEMAKER", "mliRegistrationPage"));// Added by
																	// Say 31
																	// Jan19
					model.put("homePage", "cgtmseMakerHome");
					return new ModelAndView("newMLIRegistration", model);
				}
			}
			Integer i = Collections.max(bankId);
			Integer b = 0000;// Collections.max(bankBranchId);
			Integer c = 0000;// Collections.max(bankZonId);
			i = i + 1;
			// b = b + 1;
			// c = c + 1;
			System.out.println("Hello");
			System.out.println(i + " " + b + " " + c);
			// NewMLIRegistration mliRegistration =
			// prepareNewModelForMLIReg(mliRegistrationBean,
			// Integer.toString(i),Integer.toString(b), Integer.toString(c),
			// userId);
			NewMLIRegistration mliRegistration = prepareNewModelForMLIReg(mliRegistrationBean, Integer.toString(i), "0000", "0000",userId);

			mliRegService.addNEWMLIDetails(mliRegistration);
			/*
			 * mliID = mliRegistration.getShortName() + Integer.toString(i) +
			 * Integer.toString(b) + Integer.toString(c);
			 */
			mliID = mliRegistration.getShortName() + Integer.toString(i)
					+ "0000" + "0000";
			System.out.println("insert....if");
		} else {
			System.out.println("Insert Else........");
			/*
			 * NewMLIRegistration mliRegistration = prepareNewModelForMLIReg(
			 * mliRegistrationBean, "1000", "2000", "3000", userId);
			 */
			NewMLIRegistration mliRegistration = prepareNewModelForMLIReg(
					mliRegistrationBean, "5001", "0000", "0000", userId);
			mliName = mliRegistration.getMem_bnk_name();
			mliID = mliRegistration.getShortName() + "5001" + "0000" + "0000";
			mliRegService.addNEWMLIDetails(mliRegistration);
			System.out.println("Insert Complete........");

		}
		modelMsg.addAttribute("message", mliName
				+ " MLI Successfully Registered:"+mliID);
		model.put("ratingAgencyList", stateService.mliRatingList());
		model.put("stateList",
				prepareNewStateListofBean(stateService.listStateMaster()));
		// return new ModelAndView("redirect:/addNewMLIRegistration.html");
		return new ModelAndView("redirect:/addNewMLIRegistration.html?mliID="
				+ mliID);
	}

	@RequestMapping(value = "/addNewMLIRegistration", method = RequestMethod.GET)
	public ModelAndView addNewMLIRegistration(
			@ModelAttribute("command") MLIRegBean mliRegistrationBean,
			HttpServletRequest request, HttpSession session, Model modelMsg,
			String mliID) {
		
		System.out
				.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
		Map<String, Object> model = new HashMap<String, Object>();
		modelMsg.addAttribute("message",mliID
				+ " : MLI has been Successfully Registered:");
		model.put("ratingAgencyList", stateService.mliRatingList());
		model.put("stateList",
				prepareNewStateListofBean(stateService.listStateMaster()));

		// added by say 6 feb-----------------------
		model.put("adminlist",
				userActivityService.getActivity("CGTMSEMAKER", "System_Admin"));
		model.put("guaranteelist",
				userActivityService.getActivity("CGTMSEMAKER", "Registration"));
		model.put("applicationList", userActivityService.getActivity(
				"CGTMSEMAKER", "Application_Processing"));
		model.put("RPaymentList", userActivityService.getActivity(
				"CGTMSEMAKER", "Receipt_Payments"));
		model.put("repList",
				userActivityService.getReport("CGTMSEMAKER", "User_Report"));
		model.put("GMaintainlist", userActivityService.getActivity(
				"CGTMSEMAKER", "Guarantee_Maintenance"));
		// end------------
		// model.put("actList",
		// userActivityService.getActivity("CGTMSEMAKER", "User_Activity"));
		model.put("actName", userActivityService.getActivityName("CGTMSEMAKER",
				"mliRegistrationPage"));// Added by Say 31 Jan19
		model.put("homePage", "cgtmseMakerHome");
		return new ModelAndView("newMLIRegistration", model);
		// return new ModelAndView("redirect:/nbfcLogin.html");

	}

	@RequestMapping(value = "/getNewDistrict", method = RequestMethod.POST)
	public @ResponseBody JsonResponse2 addaaUser(@ModelAttribute(value = "state") District district,BindingResult result, String state) {
		JsonResponse2 res = new JsonResponse2();
		Map<String, String> mapLongNameObj1 = districtService.listDistricts(state);
		userList.clear();
	
		for (Map.Entry<String, String> entry : mapLongNameObj1.entrySet()) {
			District s1 = new District();
			System.out.println("Before Sorting==="+entry.getValue());
			String value = entry.getValue();
			s1.setDst_name(value);
			userList.add(s1);
		}
		
		
		mapLongNameObj1.clear();
		res.setStatus("SUCCESS");
		//Collections.sort(userList);   
		res.setResult(userList);

		return res;
	}
	
	

	// Added by Say 22-nov-2018
	private NewMLIRegistration prepareNewModelForMLIReg(MLIRegBean mliRegNean,String mem_bnk_id, String mem_brn_id, String mem_zne_id,String userId) throws ParseException {
		NewMLIRegistration mliReg = new NewMLIRegistration();
		mliReg.setCity(mliRegNean.getCity().toUpperCase().trim());
		mliReg.setCompanyAddress(mliRegNean.getCompanyAddress().toUpperCase().trim());
		mliReg.setCompanyCIN(mliRegNean.getCompanyCIN().toUpperCase().trim());
		mliReg.setCompanyPAN(mliRegNean.getCompanyPAN().toUpperCase().trim());
		mliReg.setContactPerson(mliRegNean.getContactPerson().toUpperCase().trim());
		mliReg.setDistrict(mliRegNean.getDistrict().toUpperCase().trim());
		mliReg.setEmailId(mliRegNean.getEmailId().trim());
		/*if(mliRegNean.getFaxCode()!=null) {
			mliReg.setFaxCode(mliRegNean.getFaxCode().trim());
		}else {
			mliReg.setFaxCode("");
		}
		
		if(mliRegNean.getFaxNumber()!=null) {
			mliReg.setFaxNumber(mliRegNean.getFaxNumber().trim());
		}else {
			mliReg.setFaxNumber("");
		}*/
	
		mliReg.setGstinNumber(mliRegNean.getGstinNumber().toUpperCase());
		
		
		if(mliRegNean.getPhoneCode().trim()!=null) {
			mliReg.setPhone_code(mliRegNean.getPhoneCode().trim());
		}else {
			mliReg.setPhone_code("");
		}
		if(mliRegNean.getLandlineNumber().trim()!=null) {
			mliReg.setLandlineNumber(mliRegNean.getLandlineNumber().trim());
		}else {
			mliReg.setLandlineNumber("");
		}
		
		mliReg.setLongName(mliRegNean.getLongName().toUpperCase().trim());
		mliReg.setMem_bnk_id(mem_bnk_id);
		mliReg.setMem_brn_id(mem_brn_id);
		mliReg.setMem_zne_id(mem_zne_id);
		mliReg.setMobileNUmber(mliRegNean.getMobileNUmber().trim());
		mliReg.setPincode(mliRegNean.getPincode().trim());
		mliReg.setRating(mliRegNean.getRating().toUpperCase());
		mliReg.setRatingAgency(mliRegNean.getRatingAgency().toUpperCase());
		// mliReg.setRatingDate(mliRegNean.getRatingDate());

		// System.out.println("employeeBean.getPortfolio_start_date()  :"
		// + mliRegNean.getRatingDate());
		// String date = mliRegNean.getRatingDate().substring(0, 10);
		// String dateSplit[] = date.split("-");
		// String formatedSanctionDate = dateSplit[1] + "-" + dateSplit[0] + "-"
		// + dateSplit[2];
		// System.out.println("Reting Date.........." + formatedSanctionDate);
		// String startDateString = "06-27-2007";
		// DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
		// Date startDate = df.parse(startDateString);
		// mliReg.setRatingDate(startDate);
		// System.out.println("mliRegNean.getRatingDate()  :"
		// + mliRegNean.getRatingDate());
		String ff = mliRegNean.getRatingDate();

		System.out.println("mliRegNean.getRatingDate() 1---- :" + ff);

		NBFCCommonUtility nbfcCommonUtility = new NBFCCommonUtility();
		Date tt = nbfcCommonUtility.convertStringDateInToDateFormatMMddyyyy(ff);
		System.out.println("mliRegNean.getRatingDate() DAte---- :" + tt);
		mliReg.setRatingDate(tt);

		System.out.println("Rating Date..........."+ mliRegNean.getRatingDate());
		mliReg.setRbiReggistrationNumber(mliRegNean.getRbiReggistrationNumber().trim());
		mliReg.setUserID(userId);
		mliReg.setStatus("CMR");
		mliReg.setMem_bnk_name(mliRegNean.getLongName().toUpperCase().trim());
		mliReg.setShortName(mliRegNean.getShortName().trim());
		mliReg.setState(mliRegNean.getState().trim());
		
		mliReg.setMem_status("A");
		mliReg.setMem_mcgf("B");
		// ended---------------------------------------------------------------------------------------
		return mliReg;
	}

	private List<StateBean> prepareNewStateListofBean(
			List<StateMaster> employees) {
		List<StateBean> beans = null;
		if (employees != null && !employees.isEmpty()) {
			beans = new ArrayList<StateBean>();
			StateBean bean = null;
			for (StateMaster employee : employees) {
				bean = new StateBean();
				bean.setSte_code(employee.getSte_code());
				bean.setSte_name(employee.getSte_name());
				beans.add(bean);
			}
		}
		return beans;
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

}
