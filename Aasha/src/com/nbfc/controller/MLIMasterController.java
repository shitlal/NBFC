package com.nbfc.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nbfc.bean.JsonResponse2;
import com.nbfc.bean.MLIRegBean;

import com.nbfc.bean.StateBean;
import com.nbfc.bean.UserBean;
import com.nbfc.model.BankDetails;
import com.nbfc.model.District;
import com.nbfc.model.MLIRegistration;
import com.nbfc.model.NBFCPrivilegeMaster;
import com.nbfc.model.State;
import com.nbfc.model.StateMaster;
import com.nbfc.model.User;
import com.nbfc.model.UserPerivilegeDetails;
import com.nbfc.service.DistrictService;
import com.nbfc.service.LoginService;
import com.nbfc.service.MLIRegService;
import com.nbfc.service.StateService;
import com.nbfc.validation.EmployeeValidator;
import com.raistudies.domain.CustomExceptionHandler;

@Controller
public class MLIMasterController {

	@Autowired
	LoginService lofinService;
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
	private List<District> userList = new ArrayList<District>();
	static Logger log = Logger.getLogger(NBFCController.class.getName());
	List<BankDetails> bankDetails = new ArrayList<BankDetails>();
	List<Integer> bankId = new ArrayList<Integer>();
	List<Integer> bankBranchId = new ArrayList<Integer>();
	List<Integer> bankZonId = new ArrayList<Integer>();

	@RequestMapping(value = "/mliRegistration", method = RequestMethod.GET)
	public ModelAndView mliRegistration(
			@ModelAttribute("command") MLIRegBean mliRegistrationBean,
			HttpServletRequest request, HttpSession session, Model modelMsg) {

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("stateList", "");
		model.put("reatingAGNList", "");
		String userId = (String) session.getAttribute("userId");
		if (userId.equals("CGTMSE ADMIN")) {

		} else {
			if (userId != null) {
				userPri = lofinService.getUserPrivlageDtl(userId, "A");
				userPrvMst = lofinService
						.getPrivlageMstDtl(userPri.getPrv_id());
				if (userPrvMst.getPrvCreatedModifiedBy().equals("CMAKER")) {

					model.put("stateList", prepareStateListofBean(stateService
							.listStateMaster()));
					model.put("ratingAgencyList", stateService.mliRatingList());

					return new ModelAndView("mliRegistration", model);
				} else {
					return new ModelAndView("redirect:/nbfcLogin.html");
				}
			}
		}
		model.put("ratingAgencyList", stateService.mliRatingList());
		model.put("stateList",
				prepareStateListofBean(stateService.listStateMaster()));
		return new ModelAndView("mliRegistration", model);
		// return new ModelAndView("redirect:/nbfcLogin.html");

	}

	@RequestMapping(value = "/saveMLIDetails", method = RequestMethod.GET)
	public ModelAndView saveMLIDetails(
			@ModelAttribute("command") MLIRegBean mliRegistrationBean,
			BindingResult result, Model modelMsg, HttpSession session) {
		Map<String, Object> model = new HashMap<String, Object>();
		bankDetails.clear();
		// System.out.println(mliRegistrationBean.getDistrict()+""+mliRegistrationBean.getMobileNUmber());
		String userId = (String) session.getAttribute("userId");
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
					prepareStateListofBean(stateService.listStateMaster()));

			return new ModelAndView("mliRegistration", model);

		}

		bankDetails = mliRegService.getBankDetails();
		System.out.println("" + bankDetails);

		if (bankDetails.size() >= 0) {
			for (BankDetails bankList : bankDetails) {
				bankId.add(Integer.parseInt(bankList.getMEM_BNK_ID()));
				bankBranchId.add(Integer.parseInt(bankList.getMEM_BRN_ID()));
				bankZonId.add(Integer.parseInt(bankList.getMEM_ZNE_ID()));

			}
			for (BankDetails list : bankDetails) {
				System.out.println(list.getLongName());
				System.out.println(mliRegistrationBean.getLongName());
				if (list.getLongName().equalsIgnoreCase(mliRegistrationBean.getLongName())) {
                    modelMsg.addAttribute("message", ""+list.getLongName()+" MLI already Registred.");
					model.put("ratingAgencyList", stateService.mliRatingList());
					model.put("stateList", prepareStateListofBean(stateService.listStateMaster()));
					return new ModelAndView("mliRegistration", model);
                 }else if (list.getShortName().equalsIgnoreCase(mliRegistrationBean.getShortName())) {
                    modelMsg.addAttribute("message", "MLI Short name "+list.getShortName()+" already Registred.");
					model.put("ratingAgencyList", stateService.mliRatingList());
					model.put("stateList", prepareStateListofBean(stateService.listStateMaster()));
					return new ModelAndView("mliRegistration", model);
                 } else if (list.getRBI_REGISTRATION_NO().equalsIgnoreCase(mliRegistrationBean.getRbiReggistrationNumber())) {
                    modelMsg.addAttribute("message", "RBI REGISTRATION NO "+list.getRBI_REGISTRATION_NO()+" already Registred.");
					model.put("ratingAgencyList", stateService.mliRatingList());
					model.put("stateList", prepareStateListofBean(stateService.listStateMaster()));
					return new ModelAndView("mliRegistration", model);
                 }else if (list.getCOMPANY_CIN().equalsIgnoreCase(mliRegistrationBean.getCompanyCIN())) {
                    modelMsg.addAttribute("message", "Company CIN NO "+list.getCOMPANY_CIN()+" already Registred.");
					model.put("ratingAgencyList", stateService.mliRatingList());
					model.put("stateList", prepareStateListofBean(stateService.listStateMaster()));
					return new ModelAndView("mliRegistration", model);
                 }else if (list.getCOMPANY_PAN().equalsIgnoreCase(mliRegistrationBean.getCompanyPAN())) {
                    modelMsg.addAttribute("message", "COMPANY PAN "+list.getCOMPANY_PAN()+" already Registred.");
					model.put("ratingAgencyList", stateService.mliRatingList());
					model.put("stateList", prepareStateListofBean(stateService.listStateMaster()));
					return new ModelAndView("mliRegistration", model);
                 } else if (list.getGSTIN_NO().equalsIgnoreCase(mliRegistrationBean.getGstinNumber())) {
                    modelMsg.addAttribute("message", "GSTIN NO "+list.getGSTIN_NO()+" already Registred.");
					model.put("ratingAgencyList", stateService.mliRatingList());
					model.put("stateList", prepareStateListofBean(stateService.listStateMaster()));
					return new ModelAndView("mliRegistration", model);
}
			}
			Integer i = Collections.max(bankId);
			Integer b = Collections.max(bankBranchId);
			Integer c = Collections.max(bankZonId);
			i = i + 1;
			b = b + 1;
			c = c + 1;
			System.out.println("Hello");
			System.out.println(i + " " + b + " " + c);
			MLIRegistration mliRegistration = prepareModelForMLIReg(
					mliRegistrationBean, Integer.toString(i),
					Integer.toString(b), Integer.toString(c), userId);
			mliRegService.addMLIDetails(mliRegistration);
			System.out.println("insert....");
		} else {

			MLIRegistration mliRegistration = prepareModelForMLIReg(
					mliRegistrationBean, "1000", "2000", "3000", userId);
			mliRegService.addMLIDetails(mliRegistration);

		}
		model.put("ratingAgencyList", stateService.mliRatingList());
		model.put("stateList",
				prepareStateListofBean(stateService.listStateMaster()));
		return new ModelAndView("redirect:/addMLIRegistration.html");
	}

	@RequestMapping(value = "/addMLIRegistration", method = RequestMethod.GET)
	public ModelAndView addMLIRegistration(
			@ModelAttribute("command") MLIRegBean mliRegistrationBean,
			HttpServletRequest request, HttpSession session, Model modelMsg) {

		Map<String, Object> model = new HashMap<String, Object>();
		modelMsg.addAttribute("message", "MLI Successfully Registered.");
		model.put("ratingAgencyList", stateService.mliRatingList());
		model.put("stateList",
				prepareStateListofBean(stateService.listStateMaster()));
		return new ModelAndView("mliRegistration", model);
		// return new ModelAndView("redirect:/nbfcLogin.html");

	}

	@RequestMapping(value = "/getDistrict", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse2 addUser(@ModelAttribute(value = "state") District district,
			BindingResult result, String state) {
		JsonResponse2 res = new JsonResponse2();
		Map<String, String> mapLongNameObj1 = districtService
				.listDistricts(state);
		userList.clear();
		for (Map.Entry<String, String> entry : mapLongNameObj1.entrySet()) {
			District s1 = new District();
			String value = entry.getValue();
			s1.setDst_name(value);
			userList.add(s1);
		}
		mapLongNameObj1.clear();
		res.setStatus("SUCCESS");
		res.setResult(userList);

		return res;
	}

	private List<StateBean> prepareStateListofBean(List<StateMaster> employees) {
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

	private MLIRegistration prepareModelForMLIReg(MLIRegBean mliRegNean,
			String mem_bnk_id, String mem_brn_id, String mem_zne_id,
			String userId) {
		MLIRegistration mliReg = new MLIRegistration();
		mliReg.setCity(mliRegNean.getCity());
		mliReg.setCompanyAddress(mliRegNean.getCompanyAddress());
		mliReg.setCompanyCIN(mliRegNean.getCompanyCIN());
		mliReg.setCompanyPAN(mliRegNean.getCompanyPAN());
		mliReg.setContactPerson(mliRegNean.getContactPerson());
		mliReg.setDistrict(mliRegNean.getDistrict());
		mliReg.setEmailId(mliRegNean.getEmailId());
		mliReg.setFaxNumber(mliRegNean.getFaxNumber());
		mliReg.setGstinNumber(mliRegNean.getGstinNumber());
		mliReg.setLandlineNumber(mliRegNean.getLandlineNumber());
		mliReg.setLongName(mliRegNean.getLongName());
		mliReg.setMem_bnk_id(mem_bnk_id);
		mliReg.setMem_brn_id(mem_brn_id);
		mliReg.setMem_zne_id(mem_zne_id);
		mliReg.setMobileNUmber(mliRegNean.getMobileNUmber());
		mliReg.setPincode(mliRegNean.getPincode());
		mliReg.setRating(mliRegNean.getRating());
		mliReg.setRatingAgency(mliRegNean.getRatingAgency());
		mliReg.setRatingDate(mliRegNean.getRatingDate());
		mliReg.setRbiReggistrationNumber(mliRegNean.getRbiReggistrationNumber());
		mliReg.setUserID(userId);
		mliReg.setStatus("CMR");
		mliReg.setMem_bnk_name(mliRegNean.getLongName());
		mliReg.setShortName(mliRegNean.getShortName());
		mliReg.setState(mliRegNean.getState());
		mliReg.setPhone_code(mliRegNean.getPhoneCode());
		mliReg.setFaxCode(mliRegNean.getFaxCode());
		mliReg.setMem_status("A");
		mliReg.setMem_mcgf("B");

		return mliReg;
	}
	 @ExceptionHandler(CustomExceptionHandler.class)
		public ModelAndView handleCustomException(CustomExceptionHandler ex) {
			Map<String, Object> model1 = new HashMap<String, Object>();		

			 //model1.put("actList",						userActivityService.getActivity("NBFCCHECKER", "User_Activity"));
			// model1.put("homePage","nbfcCheckerHome");
			ModelAndView model = new ModelAndView("customException",model1);
			model.addObject("customException", ex.getMessage());
			return model;

		}

		@ExceptionHandler(Exception.class)
		public ModelAndView handleAllException(Exception ex) {
			Map<String, Object> model1 = new HashMap<String, Object>();		

			// model1.put("actList",						userActivityService.getActivity("NBFCCHECKER", "User_Activity"));
			// model1.put("homePage","nbfcCheckerHome");
			ModelAndView model = new ModelAndView("exception",model1);
			model.addObject("exception", ex.getCause());
			return model;

		}

		@ExceptionHandler(ArithmeticException.class)
		public ModelAndView handleArithException(ArithmeticException ex) {
			Map<String, Object> model1 = new HashMap<String, Object>();		

			 //model1.put("actList",						userActivityService.getActivity("NBFCCHECKER", "User_Activity"));
			// model1.put("homePage","nbfcCheckerHome");
			ModelAndView model = new ModelAndView("exception",model1);
			model.addObject("exception", ex.getMessage());
			return model;

		}

		@ExceptionHandler(NullPointerException.class)
		public ModelAndView handleNullException(NullPointerException ex) {
			Map<String, Object> model1 = new HashMap<String, Object>();		

			// model1.put("actList",						userActivityService.getActivity("NBFCCHECKER", "User_Activity"));
			// model1.put("homePage","nbfcCheckerHome");
			ModelAndView model = new ModelAndView("exception",model1);
			model.addObject("exception", "Data is null");
			return model;

		}

}
