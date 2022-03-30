package com.nbfc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.*;
import com.ibm.icu.text.SimpleDateFormat;
import com.nbfc.bean.ApplicationStatusDetailsBean;
import com.nbfc.bean.GeneralReport;
import com.nbfc.bean.MliWiseReportDetailBean;
import com.nbfc.bean.NPADetailsBean;
import com.nbfc.helper.DateFormate;
import com.nbfc.model.MLIName;
import com.nbfc.model.State;
import com.nbfc.service.CGTMSECreateExposureLimitMakerService;
import com.nbfc.service.NPAService;
import com.nbfc.service.StateService;
import com.nbfc.service.StateWiseReportService;
import com.nbfc.service.StatutsWiseReportService;
import com.nbfc.service.UserActivityService;
import com.nbfc.validation.EmployeeValidator;

/**MliWiseReport
 * @author ajeet
 *f
 */
@Controller
public class MliWiseReportDetailController {
	
	@Autowired
	UserActivityService userActivityService;
	@Autowired
	NPAService npaService;
	@Autowired
	EmployeeValidator validator;
	
	@Autowired
	StateWiseReportService stateWiseReportService;
	
	@Autowired
	StatutsWiseReportService statutsWiseReportService;
	@Value("${downloadFileDirPath}")
	private String downloadFileDirPath;
	
	@Value("${MLIWisedownloadFileName}")
	private String downloadFileName;
	
	@Value("${StatutsWisedownloadFileName}")
	private String downloadFileName1;
	
	@Value("${ASfReportdownloadFileName}")
	private String ASfReportdownloadFileName;
	
	
	String memberId=null;
	String userId = null;
	@Autowired
	StateService stateService;
	@Autowired
	private CGTMSECreateExposureLimitMakerService cgtmseExposureMasterMakerService;
	MLIName mem_id=null;
	public static final int BUFFER_SIZE = 4096;
	@Autowired
    List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
	static Logger log = Logger.getLogger(NBFCController.class.getName());
	
	@RequestMapping(value = "/StatusWiseReportDetail", method = RequestMethod.GET)
	public ModelAndView StatutsDetailReport( @ModelAttribute("command") GeneralReport bean,
			BindingResult result, HttpSession session, Model model) throws JRException {
		    Map<String, Object> model1 = new HashMap<String, Object>();
		    String Role = (String) session.getAttribute("uRole");
		   userId = (String) session.getAttribute("userId");
		  // String loginUserMemId = npaService.getMemberId(userId);
		   // session.removeAttribute("row1");
		if (Role.equals("CMAKER")) {
			model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
			model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
			model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model1.put("stateList", prepareStateListofBean(stateService.listStates("CCA")));
			//model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "cgtmseMakerHome");
		} else if (Role.equals("CCHECKER")) {
			model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
			model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
			model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
			//model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "cgtmseCheckerHome");
		 } else if (Role.equals("NMAKER")) {
			model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
			//model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "nbfcMakerHome");

		} else if (Role.equals("NCHECKER")) {
			model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
			//model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "nbfcCheckerHome");
		}
		return new ModelAndView("StatutsWiseReportInput", model1);
	 }
	
	@RequestMapping(value = "/Statutswisereport", method = RequestMethod.POST)
	public ModelAndView Statutswisereport( @ModelAttribute("command") GeneralReport bean, BindingResult result, HttpSession session, Model model,HttpServletRequest request)
			 throws JRException, ParseException {
		     Map<String, Object> model1 = new HashMap<String, Object>();
		     String Role = (String) session.getAttribute("uRole");
		      userId = (String) session.getAttribute("userId");
		    // session.removeAttribute("row1");stateInputDetailsValidate
			 memberId = npaService.getMemberId(userId);
		     validator.StatutsWiseValidate(bean, result);
		if (result.hasErrors()) {
			if (Role.equals("CMAKER")) {
				model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
		        model1.put("homePage", "cgtmseMakerHome");

			} else if (Role.equals("CCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
			    model1.put("homePage", "cgtmseCheckerHome");
				// return null;
			} else if (Role.equals("NMAKER")) {
				model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
				model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcMakerHome");

			} else if (Role.equals("NCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
				model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcCheckerHome");
            }
			return new ModelAndView("StatutsWiseReportInput", model1);
		 }

		if (Role.equals("CMAKER")) {
			userId = "ADMIN";
			model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			 model1.put("homePage", "cgtmseMakerHome");

		} else if (Role.equals("CCHECKER")) {
			userId = "ADMIN";
			model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
			model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
			model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
			 model1.put("homePage", "cgtmseCheckerHome");
		 } else if (Role.equals("NMAKER")) {
			userId = (String) session.getAttribute("userId");
			model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
			model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
			model1.put("homePage", "nbfcMakerHome");

		} else if (Role.equals("NCHECKER")) {
			userId = (String) session.getAttribute("userId");
			model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
			model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
			model1.put("homePage", "nbfcCheckerHome");

		}
		String toDateF = bean.getToDate();
		String fromDateF = bean.getFromDate();
		session.setAttribute("FDate", fromDateF);
		session.setAttribute("TDate", toDateF);
		session.setAttribute("status", bean.getGuaranteeStatus());
        //Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(bean.getToDate());
		//Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean .getFromDate());
		List<Object[]> row1=null;
		//ArrayList row1=null;
		//row1= statutsWiseReportService.getStatutsDetails(userId,Role, toDate, fromDate,bean.getGuaranteeStatus());
		//String userId, String toDate, String fromDate, String status
		String toDate= DateFormate.dateformate(bean.getToDate());
		String fromDate=DateFormate.dateformate(bean .getFromDate());
		session.setAttribute("memberId", memberId);
		session.setAttribute("Role", Role);
		session.setAttribute("toDate", toDate);
		session.setAttribute("fromDate", fromDate);
		session.setAttribute("statuts", bean.getGuaranteeStatus());
	    row1=statutsWiseReportService.getStatutsWiseData(memberId, Role, fromDate, toDate, bean.getGuaranteeStatus());
		//public List<Object[]> getStatutsWiseData(String userId,String role, Date toDate, Date fromDate, String statuts);
		 if (!row1.isEmpty()) {
			model1.put("row1", row1);
			//session.setAttribute("row1", row1);
		 } else {
			model1.put("message", "NO Data Found !!");
		}
       return new ModelAndView("StatutsWiseReportInput", model1);
	 }
	
	@RequestMapping(value = "/StatutsbackButton", method = RequestMethod.POST)
	public ModelAndView StatutsbackButton( @ModelAttribute("command") GeneralReport bean, BindingResult result, 
			HttpSession session, Model model,HttpServletRequest request) throws JRException, ParseException {
		        Map<String, Object> model1 = new HashMap<String, Object>();
		        String Role = (String) session.getAttribute("uRole");
		if (result.hasErrors()) {
			if (Role.equals("CMAKER")) {
				model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
		        model1.put("homePage", "cgtmseMakerHome");
             } else if (Role.equals("CCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
				model1.put("homePage", "cgtmseCheckerHome");
			 } else if (Role.equals("NMAKER")) {
				model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
				model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcMakerHome");

			} else if (Role.equals("NCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
				model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcCheckerHome");

			}
			return new ModelAndView("StatutsWiseReportInput", model1);
		}

		if (Role.equals("CMAKER")) {
			userId = "ADMIN";
			model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model1.put("homePage", "cgtmseMakerHome");

		} else if (Role.equals("CCHECKER")) {
			userId = "ADMIN";
			model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
			model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
			model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
			model1.put("homePage", "cgtmseCheckerHome");
		 } else if (Role.equals("NMAKER")) {
			userId = (String) session.getAttribute("userId");
			model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
			model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
			model1.put("homePage", "nbfcMakerHome");

		} else if (Role.equals("NCHECKER")) {
			userId = (String) session.getAttribute("userId");
			model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
			model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
			model1.put("homePage", "nbfcCheckerHome");

		}
		
		/*String gStatus=(String) session.getAttribute("status");
		String toDateF=(String) session.getAttribute("TDate");
		String fromDateF=(String) session.getAttribute("FDate");
		Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(toDateF);
		Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDateF);
		rows = stateWiseReportService.getStateDetails(userId,Role,toDate, fromDate,gStatus);
		if (!rows.isEmpty()) {
			model1.put("rows", rows);
		} else {
			model1.put("message", "NO Data Found !!");
		}*/

		return new ModelAndView("StatutsWiseReportInput", model1);
		// return null;
	}
	
	@RequestMapping(value = "/MLIWisebackButton", method = RequestMethod.POST)
	public ModelAndView MLIWisebackButton( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
			Model model,HttpServletRequest request) throws JRException, ParseException {
		        Map<String, Object> model1 = new HashMap<String, Object>();
		        String Role = (String) session.getAttribute("uRole");
		if (result.hasErrors()) {
			if (Role.equals("CMAKER")) {
				model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
		        model1.put("homePage", "cgtmseMakerHome");
             } else if (Role.equals("CCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
				model1.put("homePage", "cgtmseCheckerHome");
			 } else if (Role.equals("NMAKER")) {
				model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
				model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcMakerHome");

			} else if (Role.equals("NCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
				model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcCheckerHome");

			}
			return new ModelAndView("MliWiseReportInputForm", model1);
		}

		if (Role.equals("CMAKER")) {
			userId = "ADMIN";
			model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model1.put("homePage", "cgtmseMakerHome");

		} else if (Role.equals("CCHECKER")) {
			userId = "ADMIN";
			model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
			model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
			model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
			model1.put("homePage", "cgtmseCheckerHome");
		 } else if (Role.equals("NMAKER")) {
			userId = (String) session.getAttribute("userId");
			model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
			model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
			model1.put("homePage", "nbfcMakerHome");

		} else if (Role.equals("NCHECKER")) {
			userId = (String) session.getAttribute("userId");
			model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
			model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
			model1.put("homePage", "nbfcCheckerHome");

		}
		
		/*String gStatus=(String) session.getAttribute("status");
		String toDateF=(String) session.getAttribute("TDate");
		String fromDateF=(String) session.getAttribute("FDate");
		Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(toDateF);
		Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDateF);
		rows = stateWiseReportService.getStateDetails(userId,Role,toDate, fromDate,gStatus);
		if (!rows.isEmpty()) {
			model1.put("rows", rows);
		} else {
			model1.put("message", "NO Data Found !!");
		}*/

		return new ModelAndView("MliWiseReportInputForm", model1);
		// return null;
	}
	
	
	
	
	
	
	 
	 
 
	private List<NPADetailsBean> prepareStateListofBean(List<State> employees) {
		// TODO Auto-generated method stub
		List<NPADetailsBean> beans = null;
		if (employees != null && !employees.isEmpty()) {
			beans = new ArrayList<NPADetailsBean>();
			NPADetailsBean bean = null;
			for (State employee : employees) {
				bean = new NPADetailsBean();
				bean.setSte_code(employee.getSte_code());
				bean.setSte_name(employee.getSte_name());
				beans.add(bean);
			}
		}
		return beans;
	}
	
	
	//this method call for MliWiseReport Report Detail Page
	@RequestMapping(value = "/MliWiseReport", method = RequestMethod.GET)
	public ModelAndView MliWiseReport(
			@ModelAttribute("command") NPADetailsBean bean,
			BindingResult result, HttpSession session, Model model)
			throws JRException {
		Map<String, Object> model1 = new HashMap<String, Object>();
		String Role = (String) session.getAttribute("uRole");
		 //userId = (String) session.getAttribute("userId");
		//String loginUserMemId = npaService.getMemberId(userId);
		// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
		if (Role.equals("CMAKER")) {
			model1.put("guaranteelist", userActivityService.getActivity(
					"CGTMSEMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity(
					"CGTMSEMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity(
					"CGTMSEMAKER", "Receipt_Payments"));
			model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
			model1.put("repList",
					userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model1.put("stateList", prepareStateListofBean(stateService.listStates("CCA")));
			// model1.put("actNameHome",
			// userActivityService.getActivityName("CGTMSEMAKER",
			// "cgpanDetailReport"));// Added by Say 31 Jan19
			//model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "cgtmseMakerHome");

		} else if (Role.equals("CCHECKER")) {
			model1.put("adminlist", userActivityService.getActivity(
					"CGTMSECHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity(
					"CGTMSECHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity(
					"CGTMSECHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity(
					"CGTMSECHECKER", "Receipt_Payments"));
			model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
			model1.put("repList", userActivityService.getReport(
					"CGTMSECHECKER", "User_Report"));
			//model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "cgtmseCheckerHome");
			// return null;
		} else if (Role.equals("NMAKER")) {
			// added by say 6 feb-----------------------
			model1.put("adminlist", userActivityService.getActivity(
					"NBFCMAKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity(
					"NBFCMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity(
					"NBFCMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity(
					"NBFCMAKER", "Receipt_Payments"));
			model1.put("repList",
					userActivityService.getReport("NBFCMAKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
			//model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "nbfcMakerHome");

		} else if (Role.equals("NCHECKER")) {
			model1.put("adminlist", userActivityService.getActivity(
					"NBFCCHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity(
					"NBFCCHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity(
					"NBFCCHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity(
					"NBFCCHECKER", "Receipt_Payments"));
			model1.put("repList",
					userActivityService.getReport("NBFCCHECKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
			//model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "nbfcCheckerHome");

		}
		return new ModelAndView("MliWiseReportInputForm", model1);
		// return null;
	}
	
	
	  //Modified By Parmanand
	@RequestMapping(value = "/MliWiseReportDetailList", method = RequestMethod.POST)
	public ModelAndView MliWiseReportDetailList(
			@ModelAttribute("command") NPADetailsBean bean,
			BindingResult result, HttpSession session, Model model,HttpServletRequest request)
			throws JRException, ParseException {
		Map<String, Object> model1 = new HashMap<String, Object>();
		List<NPADetailsBean> NPADetailList=null;
		
		session.setAttribute("NPADetailList",null);
		session.removeAttribute("NPADetailList");
		
		String Role = (String) session.getAttribute("uRole");
		//String userId = (String) session.getAttribute("userId");
		//memberId = npaService.getMemberId(userId);
		String mliLongName=bean.getMliLongName();
		 
		System.out.println("mliLongName==194==="+mliLongName);
		validator.MliReportDetailsValidate(bean, result);
		if (result.hasErrors()) {
			if (Role.equals("CMAKER")) {
				// userId = "ADMIN";
				model1.put("guaranteelist", userActivityService.getActivity(
						"CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"CGTMSEMAKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport(
						"CGTMSEMAKER", "User_Report"));
				model1.put("homePage", "cgtmseMakerHome");

			} else if (Role.equals("CCHECKER")) {
				// userId = "ADMIN";
				model1.put("adminlist", userActivityService.getActivity(
						"CGTMSECHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"CGTMSECHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"CGTMSECHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"CGTMSECHECKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport(
						"CGTMSECHECKER", "User_Report"));
				model1.put("homePage", "cgtmseCheckerHome");
				// return null;
			} else if (Role.equals("NMAKER")) {
				model1.put("adminlist", userActivityService.getActivity(
						"NBFCMAKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"NBFCMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"NBFCMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"NBFCMAKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport(
						"NBFCMAKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcMakerHome");

			} else if (Role.equals("NCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity(
						"NBFCCHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"NBFCCHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"NBFCCHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"NBFCCHECKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport(
						"NBFCCHECKER", "User_Report"));
				 model1.put("homePage", "nbfcCheckerHome");

			}
			return new ModelAndView("MliWiseReportInputForm", model1);
		}

		if (Role.equals("CMAKER")) {
			userId = "ADMIN";
			model1.put("guaranteelist", userActivityService.getActivity(
					"CGTMSEMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity(
					"CGTMSEMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity(
					"CGTMSEMAKER", "Receipt_Payments"));

			model1.put("repList",
					userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
			model1.put("homePage", "cgtmseMakerHome");

		} else if (Role.equals("CCHECKER")) {
			userId = "ADMIN";
			model1.put("adminlist", userActivityService.getActivity(
					"CGTMSECHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity(
					"CGTMSECHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity(
					"CGTMSECHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity(
					"CGTMSECHECKER", "Receipt_Payments"));

			model1.put("repList", userActivityService.getReport(
					"CGTMSECHECKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
			model1.put("homePage", "cgtmseCheckerHome");
			// return null;
		} else if (Role.equals("NMAKER")) {
			userId = (String) session.getAttribute("userId");
			model1.put("adminlist", userActivityService.getActivity(
					"NBFCMAKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity(
					"NBFCMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity(
					"NBFCMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity(
					"NBFCMAKER", "Receipt_Payments"));
			model1.put("repList",
					userActivityService.getReport("NBFCMAKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
			model1.put("homePage", "nbfcMakerHome");

		} else if (Role.equals("NCHECKER")) {
			userId = (String) session.getAttribute("userId");
			model1.put("adminlist", userActivityService.getActivity(
					"NBFCCHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity(
					"NBFCCHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity(
					"NBFCCHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity(
					"NBFCCHECKER", "Receipt_Payments"));
			model1.put("repList",
					userActivityService.getReport("NBFCCHECKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
			model1.put("homePage", "nbfcCheckerHome");

		}
		String Mem_Id ="";
		if(!mliLongName.equals("ALL")){
			mem_id = userActivityService.getBankID(mliLongName);
			Mem_Id= mem_id.getBnkId() + mem_id.getBrnName()+ mem_id.getZneID() ;
			System.out.println("Mem_Id FOR MLI WIse For Specific==="+Mem_Id);
		}
		else{
			Mem_Id="ALL";	
			System.out.println("Mem_Id FOR MLI WIse FOR ALL==="+Mem_Id);
		}
		Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(bean.getToDate());
		Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean .getFromDate());
		session.setAttribute("Role", Role); 
		session.setAttribute("userId", userId); 
		session.setAttribute("fromDate", fromDate); 
		session.setAttribute("toDate", toDate); 
		session.setAttribute("Mem_Id", Mem_Id); 
		if(Role.equals("CCHECKER") || Role.equals("CMAKER")){
			 rows = statutsWiseReportService.getMliWeise(Role, userId, fromDate, toDate, Mem_Id, request);
		}else{
			 rows = statutsWiseReportService.getMliWeise(Role, userId, fromDate, toDate, Mem_Id,request);
		 }
		if (!rows.isEmpty()) {
			model1.put("rows", rows);
		 } else {
			model1.put("message", "NO Data Found !!");
		} 
		return new ModelAndView("MliWiseReportInputForm", model1);
	}
	
	//Get the MLI Long Name in Drop Down on Page on Load
		/*@ModelAttribute("mliLongName")
		public Map<String, String> getMlilongName() {
			Map<String, String> mapMliLongNameObj = new HashMap<String, String>();
			//mapMliLongNameObj.put("ALL", "all Mil wise");
			mapMliLongNameObj = cgtmseExposureMasterMakerService.getMliLongNameInDropDown();
			return   mapMliLongNameObj;
		}*/
		
		 
		
		 @ModelAttribute("mliLongName")
		public Map<String, String> getMlilongName1(HttpSession session) {
			 Map<String, String> mapMliLongNameObj = new HashMap<String, String>();
			 String Role=(String) session.getAttribute("uRole");
			 String Userid= (String) session.getAttribute("userId");
			 if(Role.equals("CMAKER") || Role.equals("CCHECKER")){
				  mapMliLongNameObj = statutsWiseReportService.getAllMliLongName();
			 }
			 else{
			      mapMliLongNameObj=statutsWiseReportService.getBankName(Userid);
			 
			 }
			return   mapMliLongNameObj;
		} 
		
		  
		
		
		
		 @RequestMapping(value = "/MliWiseReportDetailDownload", method = RequestMethod.GET)
				public ModelAndView MliWiseReportDetailDownload(
						@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
						HttpServletRequest request, HttpServletResponse response,
						HttpSession session) throws IOException {
					try {

						String filePath = downloadFileDirPath + File.separator + downloadFileName;
						System.out.println(filePath);
						File file = new File(downloadFileDirPath);
						boolean isCreated = file.mkdir();
		                if (isCreated) {
							File file1 = new File(filePath);
							boolean isExists = file1.exists();
							if (isExists) {

							} else {
								file1.createNewFile();
							}

						}
		                
		                XSSFWorkbook hwb = new XSSFWorkbook();
						XSSFSheet sheet = hwb.createSheet("MliWiseReport");
                        CellStyle style = hwb.createCellStyle();
						Font font = hwb.createFont();
						font.setFontHeightInPoints((short) 11);
						font.setFontName(HSSFFont.FONT_ARIAL);
						font.setBoldweight(HSSFFont.COLOR_NORMAL);
						font.setBold(true);
						font.setColor(HSSFColor.DARK_BLUE.index);
						style.setFont(font);
						style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
						style.setFillPattern(CellStyle.SOLID_FOREGROUND);
						sheet.createFreezePane(0,1 ); // Freeze 1st Row
														// sheet.createFreezePane(int
														// colSplit, int rowSplit, int
														// leftmostColumn, int topRow)
                       XSSFRow rowhead = sheet.createRow((short) 0);

						XSSFCell cell0 = rowhead.createCell(0);
						cell0.setCellStyle(style);
						cell0.setCellValue("SR.NO.");// Done 1

						XSSFCell cell1 = rowhead.createCell(1);
						cell1.setCellStyle(style);
						cell1.setCellValue("NAME OF THE NBFC");// Done 3

						XSSFCell cell2 = rowhead.createCell(2);
						cell2.setCellStyle(style);
						cell2.setCellValue("MLI ID");// Done 4

						XSSFCell cell3 = rowhead.createCell(3);
						cell3.setCellStyle(style);
						cell3.setCellValue("GUARANTEED COUNT");// Done 5

						XSSFCell cell4 = rowhead.createCell(4);
						cell4.setCellStyle(style);
						cell4.setCellValue("GUARANTEED AMOUNT");// Done 6
						//String userName = (String) session.getAttribute("uName");
                        String Role= (String) session.getAttribute("Role"); 
						String userId=(String) session.getAttribute("userId"); 
						Date fromDate=(Date) session.getAttribute("fromDate");  
						Date toDate= (Date) session.getAttribute("toDate");  
						String Mem_Id= (String) session.getAttribute("Mem_Id"); 
						List<MliWiseReportDetailBean> report=statutsWiseReportService.getMliWeiseReport(Role, userId, fromDate, toDate, Mem_Id);
						if(!report.isEmpty()){ 
						int index = 1;
						for(MliWiseReportDetailBean reportBean:report){
							 XSSFRow row = sheet.createRow((short) index);	
	                         row.createCell((short) 0).setCellValue(reportBean.getSrNo()==null ? "":reportBean.getSrNo());
	                         row.createCell((short) 1).setCellValue(reportBean.getMliName()==null ? "":reportBean.getMliName());
	                         row.createCell((short) 2).setCellValue( reportBean.getMliId()==null ? "" :reportBean.getMliId());
	                         row.createCell((short) 3).setCellValue(reportBean.getMliCount()==null ? "":reportBean.getMliCount());
	                         row.createCell((short) 4).setCellValue(reportBean.getTolalApprovedAmt()==null ? "":reportBean.getTolalApprovedAmt());
	                         index++; 
						 }
						}
						FileOutputStream fileOut = new FileOutputStream(filePath);
						hwb.write(fileOut);
						fileOut.close();

						ModelAndView model = new ModelAndView("newRolePage");

						model.addObject( "excelFileDownLoadSuccessfully", "File DownLoaded Successfully in this location F:/ExcelReports/nbfcNPAReportExcelFile.xls.");

						File downloadFile = new File(filePath);
						log.info("downloadFile =" + downloadFile);
						FileInputStream inputStream = new FileInputStream(downloadFile);

						response.setContentLength((int) downloadFile.length());

						// set headers for the response
						String headerKey = "Content-Disposition";
						String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
						response.setHeader(headerKey, headerValue);

						// get output stream of the response
						OutputStream outStream = response.getOutputStream();

						byte[] buffer = new byte[BUFFER_SIZE];
						int bytesRead = -1;

						// write bytes read from the input stream into the output stream
						while ((bytesRead = inputStream.read(buffer)) != -1) {
							outStream.write(buffer, 0, bytesRead);
						}
						inputStream.close();
						outStream.close();
						return model;
					} catch (Exception e) {
						log.info("Exception == " + e);
						System.out.println("Exception == " + e);
					}
					return null;
				}
		 @RequestMapping(value = "/StatutsWiseReportDownload", method = RequestMethod.GET)
			public ModelAndView StatutsReportdownLoad(
					@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
					HttpServletRequest request, HttpServletResponse response,
					HttpSession session) throws IOException {
				try {
                    String filePath = downloadFileDirPath + File.separator + downloadFileName1;
					System.out.println(filePath);
					File file = new File(downloadFileDirPath);
					boolean isCreated = file.mkdir();
	                if (isCreated) {
						File file1 = new File(filePath);
						boolean isExists = file1.exists();
						if (isExists) {

						} else {
							file1.createNewFile();
						}

					}
					//List<NPADetailsBean> list = (List<NPADetailsBean>) session.getAttribute("NPADetailList");
				   //log.info("list size==" + list.size());
					//log.info("list Data==" + list);

					// Writing and Downlaoding Excel File
                 XSSFWorkbook hwb = new XSSFWorkbook();
					XSSFSheet sheet = hwb.createSheet("StatutsReportDownLoadedFile");

					// Making bold and color to excel column heading
					CellStyle style = hwb.createCellStyle();
					Font font = hwb.createFont();
					font.setFontHeightInPoints((short) 11);
					font.setFontName(HSSFFont.FONT_ARIAL);
					font.setBoldweight(HSSFFont.COLOR_NORMAL);
					font.setBold(true);
					font.setColor(HSSFColor.DARK_BLUE.index);
					style.setFont(font);
					style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					sheet.createFreezePane(0, 1); // Freeze 1st Row
													// sheet.createFreezePane(int
													// colSplit, int rowSplit, int
													// leftmostColumn, int topRow)

					// Creating First rows for excel heading
					XSSFRow rowhead = sheet.createRow((short) 0);

					XSSFCell cell0 = rowhead.createCell(0);
					cell0.setCellStyle(style);
					cell0.setCellValue("SNo");// Done 1<th>SNo.</th>
					
					XSSFCell cell1 = rowhead.createCell(1);
					cell1.setCellStyle(style);
					cell1.setCellValue("CGPAN");// Done 3

					
					XSSFCell cell3 = rowhead.createCell(2);
					cell3.setCellStyle(style);
					cell3.setCellValue("MLIID");// Done 5
					
					XSSFCell cell6 = rowhead.createCell(3);
					cell6.setCellStyle(style);
					cell6.setCellValue("LOAN ACCOUNT NO");// Done 7
					
					XSSFCell cell8 = rowhead.createCell(4);
					cell8.setCellStyle(style);
					cell8.setCellValue("MSE NAME");// 
					
					XSSFCell cell2 = rowhead.createCell(5);
					cell2.setCellStyle(style);
					cell2.setCellValue("STATE");// Done 4

					

					XSSFCell cell4 = rowhead.createCell(6);
					cell4.setCellStyle(style);
					cell4.setCellValue("DISTRICT");// Done 6
 
					
					XSSFCell cell5 = rowhead.createCell(7);
					cell5.setCellStyle(style);
					cell5.setCellValue("STATUS");// Done 7

					
					
					XSSFCell cell7 = rowhead.createCell(8);
					cell7.setCellStyle(style);
					cell7.setCellValue("GUARANTEE AMOUNT");// Done 7
					
					
					 
					XSSFCell cell9 = rowhead.createCell(9);
					cell9.setCellStyle(style);
					cell9.setCellValue("NBFC UPLOADED DATE");// 
					
					XSSFCell cell10 = rowhead.createCell(10);
					cell10.setCellStyle(style);
					cell10.setCellValue("DCI GUARANTEE START DT");// 
					
					XSSFCell cell11 = rowhead.createCell(11);
					cell11.setCellStyle(style);
					cell11.setCellValue("EXPAIRY DATE");// 
					 
					List<Object[]> result1=new ArrayList<Object[]>();
					String memberId=(String) session.getAttribute("memberId");
					String Role=(String) session.getAttribute("Role");
					String toDate=(String) session.getAttribute("toDate");
					String fromDate=(String) session.getAttribute("fromDate");
					String statuts=(String) session.getAttribute("statuts");
					result1=statutsWiseReportService.getStatutsWiseData(memberId, Role,fromDate , toDate, statuts);
					//result1=(List<Object[]>) session.getAttribute("row1");
					if(!result1.isEmpty()){
					int index = 1;
					int sno = 1;
					for(Object[] obj1:result1){
						XSSFRow row = sheet.createRow((short) index);	
						row.createCell((short) 0).setCellValue(sno); 
                        row.createCell((short) 1).setCellValue(obj1[0].toString()==null ? "":obj1[0].toString()); 
                        row.createCell((short) 2).setCellValue(obj1[2].toString()==null ? "":obj1[2].toString());
                        row.createCell((short) 3).setCellValue(obj1[5].toString()==null ? "":obj1[5].toString());
                        row.createCell((short) 4).setCellValue(obj1[7].toString().equals("null") ? "":obj1[7].toString());
                        row.createCell((short) 5).setCellValue(obj1[1].toString()==null ? "":obj1[1].toString());
                        row.createCell((short) 6).setCellValue(obj1[3].toString()==null ? "":obj1[3].toString());
                        row.createCell((short) 7).setCellValue(obj1[4].toString()==null ? "":obj1[4].toString());
                        String abbb = String.valueOf(obj1[6]);
                        if((abbb.equals("null")) || (abbb==null)){
                        	row.createCell((short) 8).setCellValue(""); 
                        }
                        else{
                        	row.createCell((short)8).setCellValue(obj1[6].toString().equals("null") ? "":obj1[6].toString()); 
                        }
                        row.createCell((short) 9).setCellValue(obj1[8].toString()==null ? "":obj1[8].toString()); 
                        row.createCell((short) 10).setCellValue(obj1[9].toString()==null ?"":obj1[9].toString()); 
                        row.createCell((short) 11).setCellValue(obj1[10].toString()==null ?"":obj1[10].toString()); 
					sno++;
					index++;
					}
					}
					FileOutputStream fileOut = new FileOutputStream(filePath);
					hwb.write(fileOut);
					fileOut.close();

					ModelAndView model = new ModelAndView("newRolePage");
                    model.addObject( "excelFileDownLoadSuccessfully", "File DownLoaded Successfully in this location F:/ExcelReports/StatutsReportExcelFile.xls.");
                    File downloadFile = new File(filePath);
					log.info("downloadFile =" + downloadFile);
					FileInputStream inputStream = new FileInputStream(downloadFile);

					response.setContentLength((int) downloadFile.length());

					// set headers for the response
					String headerKey = "Content-Disposition";
					String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
					response.setHeader(headerKey, headerValue);

					// get output stream of the response
					OutputStream outStream = response.getOutputStream();

					byte[] buffer = new byte[BUFFER_SIZE];
					int bytesRead = -1;

					// write bytes read from the input stream into the output stream
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, bytesRead);
					}
					inputStream.close();
					outStream.close();
					return model;
				} catch (Exception e) {
					log.info("Exception == " + e);
					e.printStackTrace(); 
				}
				return null;
			}
		 
		 
		//this method call for MliWiseReport Report Detail Page
			@RequestMapping(value = "/ASFSummaryDetails", method = RequestMethod.GET)
			public ModelAndView ASFSummaryDetails(
					@ModelAttribute("command") NPADetailsBean bean,
					BindingResult result, HttpSession session, Model model)
					throws JRException {
				Map<String, Object> model1 = new HashMap<String, Object>();
				String Role = (String) session.getAttribute("uRole");
				 //userId = (String) session.getAttribute("userId");
				//String loginUserMemId = npaService.getMemberId(userId);
				// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
				if (Role.equals("CMAKER")) {
					model1.put("guaranteelist", userActivityService.getActivity(
							"CGTMSEMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"CGTMSEMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"CGTMSEMAKER", "Receipt_Payments"));
					model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
					model1.put("repList",
							userActivityService.getReport("CGTMSEMAKER", "User_Report"));
					model1.put("stateList", prepareStateListofBean(stateService.listStates("CCA")));
					// model1.put("actNameHome",
					// userActivityService.getActivityName("CGTMSEMAKER",
					// "cgpanDetailReport"));// Added by Say 31 Jan19
					//model1.put("MLIID", loginUserMemId);
					model1.put("homePage", "cgtmseMakerHome");

				} else if (Role.equals("CCHECKER")) {
					model1.put("adminlist", userActivityService.getActivity(
							"CGTMSECHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"CGTMSECHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"CGTMSECHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"CGTMSECHECKER", "Receipt_Payments"));
					model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
					model1.put("repList", userActivityService.getReport(
							"CGTMSECHECKER", "User_Report"));
					//model1.put("MLIID", loginUserMemId);
					model1.put("homePage", "cgtmseCheckerHome");
					// return null;
				} else if (Role.equals("NMAKER")) {
					// added by say 6 feb-----------------------
					model1.put("adminlist", userActivityService.getActivity(
							"NBFCMAKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"NBFCMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"NBFCMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"NBFCMAKER", "Receipt_Payments"));
					model1.put("repList",
							userActivityService.getReport("NBFCMAKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
					//model1.put("MLIID", loginUserMemId);
					model1.put("homePage", "nbfcMakerHome");

				} else if (Role.equals("NCHECKER")) {
					model1.put("adminlist", userActivityService.getActivity(
							"NBFCCHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"NBFCCHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"NBFCCHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"NBFCCHECKER", "Receipt_Payments"));
					model1.put("repList",
							userActivityService.getReport("NBFCCHECKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
					//model1.put("MLIID", loginUserMemId);
					model1.put("homePage", "nbfcCheckerHome");

				}
				return new ModelAndView("ASFReportInputForm", model1);
				// return null;
			}	 

			  
			@RequestMapping(value = "/AsfReportDetailList", method = RequestMethod.POST)
			public ModelAndView AsfReportDetailList(
					@ModelAttribute("command") NPADetailsBean bean,
					BindingResult result, HttpSession session, Model model)
					throws JRException, ParseException {
				Map<String, Object> model1 = new HashMap<String, Object>();
				List<NPADetailsBean> NPADetailList=null;
				
				session.setAttribute("NPADetailList",null);
				session.removeAttribute("NPADetailList");
				
				String Role = (String) session.getAttribute("uRole");
				//String userId = (String) session.getAttribute("userId");
				//memberId = npaService.getMemberId(userId);
				String mliLongName=bean.getMliLongName();
				 
				System.out.println("mliLongName==194==="+mliLongName);
				validator.MliReportDetailsValidate(bean, result);
				if (result.hasErrors()) {
					if (Role.equals("CMAKER")) {
						// userId = "ADMIN";
						model1.put("guaranteelist", userActivityService.getActivity(
								"CGTMSEMAKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"CGTMSEMAKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"CGTMSEMAKER", "Receipt_Payments"));
						model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
						model1.put("repList", userActivityService.getReport(
								"CGTMSEMAKER", "User_Report"));
						model1.put("homePage", "cgtmseMakerHome");

					} else if (Role.equals("CCHECKER")) {
						// userId = "ADMIN";
						model1.put("adminlist", userActivityService.getActivity(
								"CGTMSECHECKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity(
								"CGTMSECHECKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"CGTMSECHECKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"CGTMSECHECKER", "Receipt_Payments"));
						model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
						model1.put("repList", userActivityService.getReport(
								"CGTMSECHECKER", "User_Report"));
						model1.put("homePage", "cgtmseCheckerHome");
						// return null;
					} else if (Role.equals("NMAKER")) {
						model1.put("adminlist", userActivityService.getActivity(
								"NBFCMAKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity(
								"NBFCMAKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"NBFCMAKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"NBFCMAKER", "Receipt_Payments"));
						model1.put("repList", userActivityService.getReport(
								"NBFCMAKER", "User_Report"));
						model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
						model1.put("homePage", "nbfcMakerHome");

					} else if (Role.equals("NCHECKER")) {
						model1.put("adminlist", userActivityService.getActivity(
								"NBFCCHECKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity(
								"NBFCCHECKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"NBFCCHECKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"NBFCCHECKER", "Receipt_Payments"));
						model1.put("repList", userActivityService.getReport(
								"NBFCCHECKER", "User_Report"));
						 model1.put("homePage", "nbfcCheckerHome");

					}
					return new ModelAndView("ASFReportInputForm", model1);
				}

				if (Role.equals("CMAKER")) {
					userId = "ADMIN";
					model1.put("guaranteelist", userActivityService.getActivity(
							"CGTMSEMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"CGTMSEMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"CGTMSEMAKER", "Receipt_Payments"));

					model1.put("repList",
							userActivityService.getReport("CGTMSEMAKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
					model1.put("homePage", "cgtmseMakerHome");

				} else if (Role.equals("CCHECKER")) {
					userId = "ADMIN";
					model1.put("adminlist", userActivityService.getActivity(
							"CGTMSECHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"CGTMSECHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"CGTMSECHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"CGTMSECHECKER", "Receipt_Payments"));

					model1.put("repList", userActivityService.getReport(
							"CGTMSECHECKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
					model1.put("homePage", "cgtmseCheckerHome");
					// return null;
				} else if (Role.equals("NMAKER")) {
					userId = (String) session.getAttribute("userId");
					model1.put("adminlist", userActivityService.getActivity(
							"NBFCMAKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"NBFCMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"NBFCMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"NBFCMAKER", "Receipt_Payments"));
					model1.put("repList",
							userActivityService.getReport("NBFCMAKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
					model1.put("homePage", "nbfcMakerHome");

				} else if (Role.equals("NCHECKER")) {
					userId = (String) session.getAttribute("userId");
					model1.put("adminlist", userActivityService.getActivity(
							"NBFCCHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"NBFCCHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"NBFCCHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"NBFCCHECKER", "Receipt_Payments"));
					model1.put("repList",
							userActivityService.getReport("NBFCCHECKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
					model1.put("homePage", "nbfcCheckerHome");

				}
				String Mem_Id ="";
				if(!mliLongName.equals("ALL")){
					mem_id = userActivityService.getBankID(mliLongName);
					System.out.println("mem_id.getBnkId()===="+mem_id.getBnkId());
					System.out.println("mem_id.getBrnName()===="+mem_id.getBrnName());
					System.out.println("mem_id.getZneID()===="+mem_id.getZneID());
					Mem_Id= mem_id.getBnkId() + mem_id.getBrnName()+ mem_id.getZneID() ;
				}
				else{
					Mem_Id="ALL";	
				}
				Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(bean.getToDate());
				Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean .getFromDate());
				session.setAttribute("Role", Role); 
				session.setAttribute("userId", userId); 
				session.setAttribute("fromDate", fromDate); 
				session.setAttribute("toDate", toDate); 
				session.setAttribute("Mem_Id", Mem_Id); 
				if(Role.equals("CCHECKER") || Role.equals("CMAKER")){
					 rows = statutsWiseReportService.getAsfReportDetail(Role, userId, fromDate, toDate, Mem_Id);
				}else{
					 rows = statutsWiseReportService.getAsfReportDetail(Role, userId, fromDate, toDate, Mem_Id);
				 }
				if (!rows.isEmpty()) {
					model1.put("rows", rows);
				 } else {
					model1.put("message", "NO Data Found !!");
				} 
				return new ModelAndView("ASFReportInputForm", model1);
			}
			 @RequestMapping(value = "/ASFReportDetailDownload", method = RequestMethod.GET)
				public ModelAndView ASFReportDetailDownload(
						@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
						HttpServletRequest request, HttpServletResponse response,
						HttpSession session) throws IOException {
					try {
                        String ASFSummaryDetailsFileName1="ASFSummaryDetailsFileName.xlsx";
						String filePath = downloadFileDirPath + File.separator + ASFSummaryDetailsFileName1;
						System.out.println(filePath);
						File file = new File(downloadFileDirPath);
						boolean isCreated = file.mkdir();
		                if (isCreated) {
							File file1 = new File(filePath);
							boolean isExists = file1.exists();
							if (isExists) {

							} else {
								file1.createNewFile();
							}

						}
		                
		                XSSFWorkbook hwb = new XSSFWorkbook();
						XSSFSheet sheet = hwb.createSheet("ASFDownLoadedFile");
                        CellStyle style = hwb.createCellStyle();
						Font font = hwb.createFont();
						font.setFontHeightInPoints((short) 11);
						font.setFontName(HSSFFont.FONT_ARIAL);
						font.setBoldweight(HSSFFont.COLOR_NORMAL);
						font.setBold(true);
						font.setColor(HSSFColor.DARK_BLUE.index);
						style.setFont(font);
						style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
						style.setFillPattern(CellStyle.SOLID_FOREGROUND);
						sheet.createFreezePane(0,1 ); // Freeze 1st Row
														// sheet.createFreezePane(int
														// colSplit, int rowSplit, int
														// leftmostColumn, int topRow)
						XSSFRow rowhead = sheet.createRow((short) 0);

						XSSFCell cell0 = rowhead.createCell(0);
						cell0.setCellStyle(style);
						cell0.setCellValue("SR.NO.");// Done 1

						XSSFCell cell1 = rowhead.createCell(1);
						cell1.setCellStyle(style);
						cell1.setCellValue("NAME OF THE NBFC");// Done 3

						XSSFCell cell2 = rowhead.createCell(2);
						cell2.setCellStyle(style);
						cell2.setCellValue("MLI ID"); 

						XSSFCell cell3 = rowhead.createCell(3);
						cell3.setCellStyle(style);
						cell3.setCellValue("DAN ID"); 
						
						XSSFCell cell4 = rowhead.createCell(4);
						cell4.setCellStyle(style);
						cell4.setCellValue("NO.OF ASF DUE"); 

						XSSFCell cell5 = rowhead.createCell(5);
						cell5.setCellStyle(style);
						cell5.setCellValue("ASF AMOUNT DUE IN RS."); 

						XSSFCell cell6 = rowhead.createCell(6);
						cell6.setCellStyle(style);
						cell6.setCellValue("ASFAppropriationflag"); 
						
					    String Role= (String) session.getAttribute("Role"); 
						String userId=(String) session.getAttribute("userId"); 
						Date fromDate=(Date) session.getAttribute("fromDate");  
						Date toDate= (Date) session.getAttribute("toDate");  
						String Mem_Id= (String) session.getAttribute("Mem_Id"); 
						List<MliWiseReportDetailBean> report=statutsWiseReportService.getASFfReportDetail(Role, userId, fromDate, toDate, Mem_Id);
						if(!report.isEmpty()){ 
						int index = 1;
						for(MliWiseReportDetailBean reportBean:report){
							 XSSFRow row = sheet.createRow((short) index);	
	                         row.createCell((short) 0).setCellValue(reportBean.getSrNo()==null ? "":reportBean.getSrNo());
	                         row.createCell((short) 1).setCellValue(reportBean.getNameoftheNBFC()==null ? "":reportBean.getNameoftheNBFC());
	                         row.createCell((short) 2).setCellValue( reportBean.getMliId()==null ? "" :reportBean.getMliId());
	                         row.createCell((short) 3).setCellValue(reportBean.getDANID()==null ? "":reportBean.getDANID());
	                         row.createCell((short) 4).setCellValue(reportBean.getNOOFASFDUE()==null ? "":reportBean.getNOOFASFDUE());
	                         row.createCell((short) 5).setCellValue(reportBean.getASFAMNTDUEINRS()==null ? "":reportBean.getASFAMNTDUEINRS());
	                         row.createCell((short) 6).setCellValue(reportBean.getASFAppropriationflag()==null ? "":reportBean.getASFAppropriationflag());
	                         index++; 
						 }
						}
						FileOutputStream fileOut = new FileOutputStream(filePath);
						hwb.write(fileOut);
						fileOut.close();

						ModelAndView model = new ModelAndView("newRolePage");

						model.addObject( "excelFileDownLoadSuccessfully", "File DownLoaded Successfully in this location F:/ExcelReports/nbfcASFReportExcelFile.xls.");

						File downloadFile = new File(filePath);
						log.info("downloadFile =" + downloadFile);
						FileInputStream inputStream = new FileInputStream(downloadFile);

						response.setContentLength((int) downloadFile.length());

						// set headers for the response
						String headerKey = "Content-Disposition";
						String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
						response.setHeader(headerKey, headerValue);

						// get output stream of the response
						OutputStream outStream = response.getOutputStream();

						byte[] buffer = new byte[BUFFER_SIZE];
						int bytesRead = -1;

						// write bytes read from the input stream into the output stream
						while ((bytesRead = inputStream.read(buffer)) != -1) {
							outStream.write(buffer, 0, bytesRead);
						}
						inputStream.close();
						outStream.close();
						return model;
					} catch (Exception e) {
						log.info("Exception == " + e);
						System.out.println("Exception == " + e);
					}
					return null;
				}
			 
			 
			 @RequestMapping(value = "/ASFbackButton", method = RequestMethod.POST)
				public ModelAndView ASFbackButton( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
						Model model,HttpServletRequest request) throws JRException, ParseException {
					        Map<String, Object> model1 = new HashMap<String, Object>();
					        String Role = (String) session.getAttribute("uRole");
					if (result.hasErrors()) {
						if (Role.equals("CMAKER")) {
							model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
			                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
					        model1.put("homePage", "cgtmseMakerHome");
			             } else if (Role.equals("CCHECKER")) {
							model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
							model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
							model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
							model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
							model1.put("homePage", "cgtmseCheckerHome");
						 } else if (Role.equals("NMAKER")) {
							model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
							model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
							model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
							model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
							model1.put("homePage", "nbfcMakerHome");

						} else if (Role.equals("NCHECKER")) {
							model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
							model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
							model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
							model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
							model1.put("homePage", "nbfcCheckerHome");

						}
						return new ModelAndView("ASFReportInputForm", model1);
					}

					if (Role.equals("CMAKER")) {
						userId = "ADMIN";
						model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
			            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
						model1.put("homePage", "cgtmseMakerHome");

					} else if (Role.equals("CCHECKER")) {
						userId = "ADMIN";
						model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
						model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
						model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
						model1.put("homePage", "cgtmseCheckerHome");
					 } else if (Role.equals("NMAKER")) {
						userId = (String) session.getAttribute("userId");
						model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
						model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
						model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
						model1.put("homePage", "nbfcMakerHome");

					} else if (Role.equals("NCHECKER")) {
						userId = (String) session.getAttribute("userId");
						model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
						model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
						model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
						model1.put("homePage", "nbfcCheckerHome");

					}
					
					/*String gStatus=(String) session.getAttribute("status");
					String toDateF=(String) session.getAttribute("TDate");
					String fromDateF=(String) session.getAttribute("FDate");
					Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(toDateF);
					Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDateF);
					rows = stateWiseReportService.getStateDetails(userId,Role,toDate, fromDate,gStatus);
					if (!rows.isEmpty()) {
						model1.put("rows", rows);
					} else {
						model1.put("message", "NO Data Found !!");
					}*/

					return new ModelAndView("ASFReportInputForm", model1);
					// return null;
				}
			 

			//this method call for DANReportGF Report Detail Page
				@RequestMapping(value = "/DANReportGF", method = RequestMethod.GET)
				public ModelAndView DANReportGF(
						@ModelAttribute("command") NPADetailsBean bean,
						BindingResult result, HttpSession session, Model model)
						throws JRException {
					Map<String, Object> model1 = new HashMap<String, Object>();
					String Role = (String) session.getAttribute("uRole");
					 //userId = (String) session.getAttribute("userId");
					//String loginUserMemId = npaService.getMemberId(userId);
					// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
					if (Role.equals("CMAKER")) {
						model1.put("guaranteelist", userActivityService.getActivity(
								"CGTMSEMAKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"CGTMSEMAKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"CGTMSEMAKER", "Receipt_Payments"));
						model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
						model1.put("repList",
								userActivityService.getReport("CGTMSEMAKER", "User_Report"));
						model1.put("stateList", prepareStateListofBean(stateService.listStates("CCA")));
						// model1.put("actNameHome",
						// userActivityService.getActivityName("CGTMSEMAKER",
						// "cgpanDetailReport"));// Added by Say 31 Jan19
						//model1.put("MLIID", loginUserMemId);
						model1.put("homePage", "cgtmseMakerHome");

					} else if (Role.equals("CCHECKER")) {
						model1.put("adminlist", userActivityService.getActivity(
								"CGTMSECHECKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity(
								"CGTMSECHECKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"CGTMSECHECKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"CGTMSECHECKER", "Receipt_Payments"));
						model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
						model1.put("repList", userActivityService.getReport(
								"CGTMSECHECKER", "User_Report"));
						//model1.put("MLIID", loginUserMemId);
						model1.put("homePage", "cgtmseCheckerHome");
						// return null;
					} else if (Role.equals("NMAKER")) {
						// added by say 6 feb-----------------------
						model1.put("adminlist", userActivityService.getActivity(
								"NBFCMAKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity(
								"NBFCMAKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"NBFCMAKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"NBFCMAKER", "Receipt_Payments"));
						model1.put("repList",
								userActivityService.getReport("NBFCMAKER", "User_Report"));
						model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
						//model1.put("MLIID", loginUserMemId);
						model1.put("homePage", "nbfcMakerHome");

					} else if (Role.equals("NCHECKER")) {
						model1.put("adminlist", userActivityService.getActivity(
								"NBFCCHECKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity(
								"NBFCCHECKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"NBFCCHECKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"NBFCCHECKER", "Receipt_Payments"));
						model1.put("repList",
								userActivityService.getReport("NBFCCHECKER", "User_Report"));
						model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
						//model1.put("MLIID", loginUserMemId);
						model1.put("homePage", "nbfcCheckerHome");

					}
					return new ModelAndView("DANGFReportInputForm", model1);
					// return null;
				}		
			 
				@RequestMapping(value = "/DANGFReportDetailList", method = RequestMethod.POST)
				public ModelAndView DANGFReportDetailList(
						@ModelAttribute("command") NPADetailsBean bean,
						BindingResult result, HttpSession session, Model model)
						throws JRException, ParseException {
					Map<String, Object> model1 = new HashMap<String, Object>();
					List<NPADetailsBean> NPADetailList=null;
					
					session.setAttribute("NPADetailList",null);
					session.removeAttribute("NPADetailList");
					
					String Role = (String) session.getAttribute("uRole");
					//String userId = (String) session.getAttribute("userId");
					//memberId = npaService.getMemberId(userId);
					String mliLongName=bean.getMliLongName();
					 
					System.out.println("mliLongName==194==="+mliLongName);
					validator.MliReportDetailsValidate(bean, result);
					if (result.hasErrors()) {
						if (Role.equals("CMAKER")) {
							// userId = "ADMIN";
							model1.put("guaranteelist", userActivityService.getActivity(
									"CGTMSEMAKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity(
									"CGTMSEMAKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity(
									"CGTMSEMAKER", "Receipt_Payments"));
							model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
							model1.put("repList", userActivityService.getReport(
									"CGTMSEMAKER", "User_Report"));
							model1.put("homePage", "cgtmseMakerHome");

						} else if (Role.equals("CCHECKER")) {
							// userId = "ADMIN";
							model1.put("adminlist", userActivityService.getActivity(
									"CGTMSECHECKER", "System_Admin"));
							model1.put("guaranteelist", userActivityService.getActivity(
									"CGTMSECHECKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity(
									"CGTMSECHECKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity(
									"CGTMSECHECKER", "Receipt_Payments"));
							model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
							model1.put("repList", userActivityService.getReport(
									"CGTMSECHECKER", "User_Report"));
							model1.put("homePage", "cgtmseCheckerHome");
							// return null;
						} else if (Role.equals("NMAKER")) {
							model1.put("adminlist", userActivityService.getActivity(
									"NBFCMAKER", "System_Admin"));
							model1.put("guaranteelist", userActivityService.getActivity(
									"NBFCMAKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity(
									"NBFCMAKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity(
									"NBFCMAKER", "Receipt_Payments"));
							model1.put("repList", userActivityService.getReport(
									"NBFCMAKER", "User_Report"));
							model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
							model1.put("homePage", "nbfcMakerHome");

						} else if (Role.equals("NCHECKER")) {
							model1.put("adminlist", userActivityService.getActivity(
									"NBFCCHECKER", "System_Admin"));
							model1.put("guaranteelist", userActivityService.getActivity(
									"NBFCCHECKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity(
									"NBFCCHECKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity(
									"NBFCCHECKER", "Receipt_Payments"));
							model1.put("repList", userActivityService.getReport(
									"NBFCCHECKER", "User_Report"));
							 model1.put("homePage", "nbfcCheckerHome");

						}
						return new ModelAndView("DANGFReportInputForm", model1);
					}

					if (Role.equals("CMAKER")) {
						userId = "ADMIN";
						model1.put("guaranteelist", userActivityService.getActivity(
								"CGTMSEMAKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"CGTMSEMAKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"CGTMSEMAKER", "Receipt_Payments"));

						model1.put("repList",
								userActivityService.getReport("CGTMSEMAKER", "User_Report"));
						model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
						model1.put("homePage", "cgtmseMakerHome");

					} else if (Role.equals("CCHECKER")) {
						userId = "ADMIN";
						model1.put("adminlist", userActivityService.getActivity(
								"CGTMSECHECKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity(
								"CGTMSECHECKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"CGTMSECHECKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"CGTMSECHECKER", "Receipt_Payments"));

						model1.put("repList", userActivityService.getReport(
								"CGTMSECHECKER", "User_Report"));
						model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
						model1.put("homePage", "cgtmseCheckerHome");
						// return null;
					} else if (Role.equals("NMAKER")) {
						userId = (String) session.getAttribute("userId");
						model1.put("adminlist", userActivityService.getActivity(
								"NBFCMAKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity(
								"NBFCMAKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"NBFCMAKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"NBFCMAKER", "Receipt_Payments"));
						model1.put("repList",
								userActivityService.getReport("NBFCMAKER", "User_Report"));
						model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
						model1.put("homePage", "nbfcMakerHome");

					} else if (Role.equals("NCHECKER")) {
						userId = (String) session.getAttribute("userId");
						model1.put("adminlist", userActivityService.getActivity(
								"NBFCCHECKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity(
								"NBFCCHECKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"NBFCCHECKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"NBFCCHECKER", "Receipt_Payments"));
						model1.put("repList",
								userActivityService.getReport("NBFCCHECKER", "User_Report"));
						model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
						model1.put("homePage", "nbfcCheckerHome");

					}
					String Mem_Id ="";
					if(!mliLongName.equals("ALL")){
					mem_id = userActivityService.getBankID(mliLongName);
					System.out.println("mem_id.getBnkId()=="+mem_id.getBnkId());
					System.out.println("mem_id.getBrnName()=="+mem_id.getBrnName());
					System.out.println("mem_id.getZneID()=="+mem_id.getZneID());
					
					Mem_Id= mem_id.getBnkId() + mem_id.getBrnName()+mem_id.getZneID() ;
					
					}
					else{
						Mem_Id="ALL";	
					}
					List<Object[]> rows1=null;

					//Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(bean.getToDate());
					//Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean .getFromDate());
					
					String toDate= DateFormate.dateformate(bean.getToDate());
					String fromDate=DateFormate.dateformate(bean .getFromDate());
					
					//String ssiName="ALL";
					String ssiName=bean.getNameOffSsiUnit();
					session.setAttribute("Role", Role); 
					session.setAttribute("userId", userId); 
					session.setAttribute("fromDate", fromDate); 
					session.setAttribute("toDate", toDate); 
					session.setAttribute("Mem_Id", Mem_Id); 
					session.setAttribute("ssiName", ssiName); 
					
					
					if(Role.equals("CCHECKER") || Role.equals("CMAKER")){
						rows1=statutsWiseReportService.getDANGFDetails(Role, userId, fromDate, toDate, Mem_Id, ssiName);
						// rows = statutsWiseReportService.getDanASFDetail(Role, userId, fromDate, toDate, Mem_Id,ssiName);
					}else{
						rows1=statutsWiseReportService.getDANGFDetails(Role, userId, fromDate, toDate, Mem_Id, ssiName);
						// rows = statutsWiseReportService.getDanASFDetail(Role, userId, fromDate, toDate, Mem_Id,Mem_Id);
					 }
					if (!rows1.isEmpty()) {
						model1.put("rows1", rows1);
					 } else {
						model1.put("message", "NO Data Found !!");
					} 
					return new ModelAndView("DANGFReportInputForm", model1);
				} 
				
				
				
				 @RequestMapping(value = "/DANGFReportDetailDownload", method = RequestMethod.GET)
					public ModelAndView DANGFReportDetailDownload(
							@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
							HttpServletRequest request, HttpServletResponse response,
							HttpSession session) throws IOException {
						try {
	                        String ASFSummaryDetailsFileName1="DANGFFileName.xlsx";
							String filePath = downloadFileDirPath + File.separator + ASFSummaryDetailsFileName1;
							System.out.println(filePath);
							File file = new File(downloadFileDirPath);
							boolean isCreated = file.mkdir();
			                if (isCreated) {
								File file1 = new File(filePath);
								boolean isExists = file1.exists();
								if (isExists) {

								} else {
									file1.createNewFile();
								}

							}
			                
			                XSSFWorkbook hwb = new XSSFWorkbook();
							XSSFSheet sheet = hwb.createSheet("DANGFDownLoadedFile");
	                        CellStyle style = hwb.createCellStyle();
							Font font = hwb.createFont();
							font.setFontHeightInPoints((short) 11);
							font.setFontName(HSSFFont.FONT_ARIAL);
							font.setBoldweight(HSSFFont.COLOR_NORMAL);
							font.setBold(true);
							font.setColor(HSSFColor.DARK_BLUE.index);
							style.setFont(font);
							style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
							style.setFillPattern(CellStyle.SOLID_FOREGROUND);
							sheet.createFreezePane(0,1 ); // Freeze 1st Row
															// sheet.createFreezePane(int
															// colSplit, int rowSplit, int
															// leftmostColumn, int topRow)
	                    XSSFRow rowhead = sheet.createRow((short) 0);

							XSSFCell cell0 = rowhead.createCell(0);
							cell0.setCellStyle(style);
							cell0.setCellValue("SrNo");// Done 1

							XSSFCell cell1 = rowhead.createCell(1);
							cell1.setCellStyle(style);
							cell1.setCellValue("Demand Advice Number");// Done 3

							XSSFCell cell2 = rowhead.createCell(2);
							cell2.setCellStyle(style);
							cell2.setCellValue("Generated On Date"); 

							XSSFCell cell3 = rowhead.createCell(3);
							cell3.setCellStyle(style);
							cell3.setCellValue("No Of Application"); 
							
							XSSFCell cell4 = rowhead.createCell(4);
							cell4.setCellStyle(style);
							cell4.setCellValue("Member Id"); 

							/*XSSFCell cell5 = rowhead.createCell(5);
							cell5.setCellStyle(style);
							cell5.setCellValue("Ssi Name"); */

							XSSFCell cell5 = rowhead.createCell(5);
							cell5.setCellStyle(style);
							cell5.setCellValue("Base Amount"); 
							
							
							XSSFCell cell6 = rowhead.createCell(6);
							cell6.setCellStyle(style);
							cell6.setCellValue("Dan Amount"); 

							XSSFCell cell7 = rowhead.createCell(7);
							cell7.setCellStyle(style);
							cell7.setCellValue("IGST Amount"); 
							
							XSSFCell cell8 = rowhead.createCell(8);
							cell8.setCellStyle(style);
							cell8.setCellValue("CGST Amount"); 

							XSSFCell cell9 = rowhead.createCell(9);
							cell9.setCellStyle(style);
							cell9.setCellValue("SGST Amount"); 

							String Role= (String) session.getAttribute("Role"); 
							String userId=(String) session.getAttribute("userId"); 
							String fromDate=(String) session.getAttribute("fromDate");  
							String toDate= (String) session.getAttribute("toDate");  
							String Mem_Id= (String) session.getAttribute("Mem_Id"); 
							String ssiName= (String) session.getAttribute("ssiName"); 
						   //List<MliWiseReportDetailBean> report=statutsWiseReportService.getDanGfReportDowanload(Role, userId, fromDate, toDate, Mem_Id,ssiName);
							List<Object[]> report1=new ArrayList<Object[]>();
							
							report1=statutsWiseReportService.getDANGFDetails(Role, userId, fromDate, toDate, Mem_Id, ssiName);
							if(!report1.isEmpty()){ 
							int index = 1;
							int srno=1;
							for(Object[] obj1:report1){
								 XSSFRow row = sheet.createRow((short) index);	
		                         row.createCell((short) 0).setCellValue(srno);
		                         row.createCell((short) 1).setCellValue(obj1[0].toString()==null ? "":obj1[0].toString());
		                         row.createCell((short) 2).setCellValue( obj1[1].toString()==null ?"":obj1[1].toString());
		                         row.createCell((short) 3).setCellValue(obj1[2].toString()==null ? "":obj1[2].toString());
		                         row.createCell((short) 4).setCellValue(obj1[3].toString()==null ? "":obj1[3].toString());
		                       /*  row.createCell((short) 5).setCellValue(obj1[4].toString()==null ? "":obj1[4].toString());*/
		                         row.createCell((short) 5).setCellValue(obj1[5].toString()==null ? "":obj1[5].toString());
		                         row.createCell((short) 6).setCellValue(obj1[6].toString()==null ? "":obj1[6].toString());
		                         row.createCell((short) 7).setCellValue(obj1[7].toString()==null ? "":obj1[7].toString());
		                         row.createCell((short) 8).setCellValue(obj1[8].toString()==null ? "":obj1[8].toString());
		                         row.createCell((short) 9).setCellValue(obj1[9].toString()==null ? "":obj1[9].toString());
		                         srno++;
		                         index++; 
							 }
							}
							FileOutputStream fileOut = new FileOutputStream(filePath);
							hwb.write(fileOut);
							fileOut.close();

							ModelAndView model = new ModelAndView("newRolePage");

							model.addObject( "excelFileDownLoadSuccessfully", "File DownLoaded Successfully in this location F:/ExcelReports/nbfcDANGFReportExcelFile.xls.");

							File downloadFile = new File(filePath);
							log.info("downloadFile =" + downloadFile);
							FileInputStream inputStream = new FileInputStream(downloadFile);

							response.setContentLength((int) downloadFile.length());

							// set headers for the response
							String headerKey = "Content-Disposition";
							String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
							response.setHeader(headerKey, headerValue);

							// get output stream of the response
							OutputStream outStream = response.getOutputStream();

							byte[] buffer = new byte[BUFFER_SIZE];
							int bytesRead = -1;

							// write bytes read from the input stream into the output stream
							while ((bytesRead = inputStream.read(buffer)) != -1) {
								outStream.write(buffer, 0, bytesRead);
							}
							inputStream.close();
							outStream.close();
							return model;
						} catch (Exception e) {
							log.info("Exception == " + e);
							System.out.println("Exception == " + e);
						}
						return null;
					}
				 
				 @RequestMapping(value = "/DANGFbackButton", method = RequestMethod.POST)
					public ModelAndView DANGFbackButtonbackButton( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
							Model model,HttpServletRequest request) throws JRException, ParseException {
						        Map<String, Object> model1 = new HashMap<String, Object>();
						        String Role = (String) session.getAttribute("uRole");
						if (result.hasErrors()) {
							if (Role.equals("CMAKER")) {
								model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
				                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
						        model1.put("homePage", "cgtmseMakerHome");
				             } else if (Role.equals("CCHECKER")) {
								model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
								model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
								model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
								model1.put("homePage", "cgtmseCheckerHome");
							 } else if (Role.equals("NMAKER")) {
								model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcMakerHome");

							} else if (Role.equals("NCHECKER")) {
								model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcCheckerHome");

							}
							return new ModelAndView("ASFReportInputForm", model1);
						}

						if (Role.equals("CMAKER")) {
							userId = "ADMIN";
							model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
				            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
							model1.put("homePage", "cgtmseMakerHome");

						} else if (Role.equals("CCHECKER")) {
							userId = "ADMIN";
							model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
							model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
							model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
							model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
							model1.put("homePage", "cgtmseCheckerHome");
						 } else if (Role.equals("NMAKER")) {
							userId = (String) session.getAttribute("userId");
							model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
							model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
							model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
							model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
							model1.put("homePage", "nbfcMakerHome");

						} else if (Role.equals("NCHECKER")) {
							userId = (String) session.getAttribute("userId");
							model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
							model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
							model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
							model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
							model1.put("homePage", "nbfcCheckerHome");

						}
						
						/*String gStatus=(String) session.getAttribute("status");
						String toDateF=(String) session.getAttribute("TDate");
						String fromDateF=(String) session.getAttribute("FDate");
						Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(toDateF);
						Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDateF);
						rows = stateWiseReportService.getStateDetails(userId,Role,toDate, fromDate,gStatus);
						if (!rows.isEmpty()) {
							model1.put("rows", rows);
						} else {
							model1.put("message", "NO Data Found !!");
						}*/

						return new ModelAndView("DANGFReportInputForm", model1);
						// return null;
					}
				 
				 
					@RequestMapping(value = "/DANIDwise", method = RequestMethod.GET)
					public ModelAndView searchStateMliWiseReport(
							@ModelAttribute("command") NPADetailsBean bean,
							BindingResult result, HttpSession session, Model model,HttpServletRequest request)
							throws JRException, ParseException {
						Map<String, Object> model1 = new HashMap<String, Object>();
						String Role = (String) session.getAttribute("uRole");
						String DanId=request.getParameter("danId");
						session.setAttribute("DanId", DanId);
						//bean.setToDate(request.getParameter("p_todate"));
						//bean.setFromDate(request.getParameter("p_fromdate"));
						// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
				       
						/*validator.stateInputDetailsValidate(bean, result);*/
						if (result.hasErrors()) {
							if (Role.equals("CMAKER")) {
								// userId = "ADMIN";
								model1.put("guaranteelist", userActivityService.getActivity(
										"CGTMSEMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"CGTMSEMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"CGTMSEMAKER", "Receipt_Payments"));

								model1.put("repList", userActivityService.getReport(
										"CGTMSEMAKER", "User_Report"));
						
								// model1.put("actNameHome",
								// userActivityService.getActivityName("CGTMSEMAKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "cgtmseMakerHome");

							} else if (Role.equals("CCHECKER")) {
								// userId = "ADMIN";
								model1.put("adminlist", userActivityService.getActivity(
										"CGTMSECHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"CGTMSECHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"CGTMSECHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"CGTMSECHECKER", "Receipt_Payments"));
								model1.put("CList",
										userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
								model1.put("repList", userActivityService.getReport(
										"CGTMSECHECKER", "User_Report"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("CGTMSECHECKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "cgtmseCheckerHome");
								// return null;
							} else if (Role.equals("NMAKER")) {
								// added by say 6 feb-----------------------
								// userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity(
										"NBFCMAKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"NBFCMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"NBFCMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"NBFCMAKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport(
										"NBFCMAKER", "User_Report"));
								model1.put("CList",
										userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("NBFCMAKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "nbfcMakerHome");

							} else if (Role.equals("NCHECKER")) {
								// userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity(
										"NBFCCHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"NBFCCHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"NBFCCHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"NBFCCHECKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport(
										"NBFCCHECKER", "User_Report"));
								model1.put("CList",
										userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("NBFCCHECKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "nbfcCheckerHome");

							}
							return new ModelAndView("DANGFReportInputForm", model1);
						}

						if (Role.equals("CMAKER")) {
							userId = (String) session.getAttribute("userId");
							model1.put("guaranteelist", userActivityService.getActivity(
									"CGTMSEMAKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity(
									"CGTMSEMAKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity(
									"CGTMSEMAKER", "Receipt_Payments"));

							model1.put("repList",
									userActivityService.getReport("CGTMSEMAKER", "User_Report"));
							// model1.put("actNameHome",
							// userActivityService.getActivityName("CGTMSEMAKER",
							// "cgpanDetailReport"));// Added by Say 31 Jan19
							model1.put("homePage", "cgtmseMakerHome");

						} else if (Role.equals("CCHECKER")) {
							userId = (String) session.getAttribute("userId");
							model1.put("adminlist", userActivityService.getActivity(
									"CGTMSECHECKER", "System_Admin"));
							model1.put("guaranteelist", userActivityService.getActivity(
									"CGTMSECHECKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity(
									"CGTMSECHECKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity(
									"CGTMSECHECKER", "Receipt_Payments"));
							model1.put("CList",
									userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
							model1.put("repList", userActivityService.getReport(
									"CGTMSECHECKER", "User_Report"));
							// model1.put("actNameHome",
							// userActivityService.getActivityName("CGTMSECHECKER",
							// "cgpanDetailReport"));// Added by Say 31 Jan19
							model1.put("homePage", "cgtmseCheckerHome");
							// return null;
						} else if (Role.equals("NMAKER")) {
							// added by say 6 feb-----------------------
							userId = (String) session.getAttribute("userId");
							model1.put("adminlist", userActivityService.getActivity(
									"NBFCMAKER", "System_Admin"));
							model1.put("guaranteelist", userActivityService.getActivity(
									"NBFCMAKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity(
									"NBFCMAKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity(
									"NBFCMAKER", "Receipt_Payments"));
							model1.put("repList",
									userActivityService.getReport("NBFCMAKER", "User_Report"));
							model1.put("CList",
									userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
							// model1.put("actNameHome",
							// userActivityService.getActivityName("NBFCMAKER",
							// "cgpanDetailReport"));// Added by Say 31 Jan19
							model1.put("homePage", "nbfcMakerHome");

						} else if (Role.equals("NCHECKER")) {
							userId = (String) session.getAttribute("userId");
							model1.put("adminlist", userActivityService.getActivity(
									"NBFCCHECKER", "System_Admin"));
							model1.put("guaranteelist", userActivityService.getActivity(
									"NBFCCHECKER", "Registration"));
							model1.put("applicationList", userActivityService.getActivity(
									"NBFCCHECKER", "Application_Processing"));
							model1.put("RPaymentList", userActivityService.getActivity(
									"NBFCCHECKER", "Receipt_Payments"));
							model1.put("repList",
									userActivityService.getReport("NBFCCHECKER", "User_Report"));
							model1.put("CList",
									userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
							// model1.put("actNameHome",
							// userActivityService.getActivityName("NBFCCHECKER",
							// "cgpanDetailReport"));// Added by Say 31 Jan19
							model1.put("homePage", "nbfcCheckerHome");

						}
						String Role1= (String) session.getAttribute("Role"); 
						String userId=(String) session.getAttribute("userId"); 
						String fromDate=(String) session.getAttribute("fromDate");  
						String toDate= (String) session.getAttribute("toDate");  
						String Mem_Id= (String) session.getAttribute("Mem_Id"); 
						String ssiName= (String) session.getAttribute("ssiName"); 
						//List<MliWiseReportDetailBean> report=statutsWiseReportService.getDanGfReportDowanload(Role, userId, fromDate, toDate, Mem_Id,ssiName);
						List<Object[]> report1=new ArrayList<Object[]>();
						/*report1=statutsWiseReportService.getDANGFDetails(Role1, userId, fromDate, toDate, Mem_Id, ssiName);
						if(!report1.isEmpty()){ 
							model1.put("rows1", report1);
							
						}
						else{
							model1.put("message", "NO Data Found !!");
						}*/
						
					   List<Object[]> rows3=null;
					   rows3 = statutsWiseReportService.getDanId(DanId);
					   if (!rows3.isEmpty()) {
							model1.put("rows3", rows3);
						} else {
							model1.put("message", "NO Data Found !!");
						}
                        return new ModelAndView("DANGFReportInputForm", model1);
						// return null;
					}
					
					
					 @RequestMapping(value = "/DemandAdviceNumberDetailDownload", method = RequestMethod.GET)
						public ModelAndView DemandAdviceNumberDetailDownload(
								@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
								HttpServletRequest request, HttpServletResponse response,
								HttpSession session) throws IOException {
							try {
		                        String ASFSummaryDetailsFileName1="DemandAdvaceFileName.xlsx";
								String filePath = downloadFileDirPath + File.separator + ASFSummaryDetailsFileName1;
								System.out.println(filePath);
								File file = new File(downloadFileDirPath);
								boolean isCreated = file.mkdir();
				                if (isCreated) {
									File file1 = new File(filePath);
									boolean isExists = file1.exists();
									if (isExists) {

									} else {
										file1.createNewFile();
									}

								}
				                
				                XSSFWorkbook hwb = new XSSFWorkbook();
								XSSFSheet sheet = hwb.createSheet("DemandAdvaceDownLoadedFile");
		                        CellStyle style = hwb.createCellStyle();
								Font font = hwb.createFont();
								font.setFontHeightInPoints((short) 11);
								font.setFontName(HSSFFont.FONT_ARIAL);
								font.setBoldweight(HSSFFont.COLOR_NORMAL);
								font.setBold(true);
								font.setColor(HSSFColor.DARK_BLUE.index);
								style.setFont(font);
								style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
								style.setFillPattern(CellStyle.SOLID_FOREGROUND);
								sheet.createFreezePane(0,1 ); // Freeze 1st Row
																// sheet.createFreezePane(int
																// colSplit, int rowSplit, int
																// leftmostColumn, int topRow)
		                    XSSFRow rowhead = sheet.createRow((short) 0);

								XSSFCell cell0 = rowhead.createCell(0);
								cell0.setCellStyle(style);
								cell0.setCellValue("SrNo");// Done 1

								 
								XSSFCell cell1 = rowhead.createCell(1);
								cell1.setCellStyle(style);
								cell1.setCellValue("CGPAN");// Done 3

								XSSFCell cell2 = rowhead.createCell(2);
								cell2.setCellStyle(style);
								cell2.setCellValue("MSE NAME"); 

								XSSFCell cell3 = rowhead.createCell(3);
								cell3.setCellStyle(style);
								cell3.setCellValue("GUARANTEE AMOUNT"); 
								
								XSSFCell cell4 = rowhead.createCell(4);
								cell4.setCellStyle(style);
								cell4.setCellValue("NBFC UPLOADED DATE"); 
								 

								XSSFCell cell5 = rowhead.createCell(5);
								cell5.setCellStyle(style);
								cell5.setCellValue("PORTFOLIO RATE"); 

								XSSFCell cell6 = rowhead.createCell(6);
								cell6.setCellStyle(style);
								cell6.setCellValue("LOAN ACCOUNT NO"); 
								
								
								XSSFCell cell7 = rowhead.createCell(7);
								cell7.setCellStyle(style);
								cell7.setCellValue("STATE"); 

								XSSFCell cell8 = rowhead.createCell(8);
								cell8.setCellStyle(style);
								cell8.setCellValue("BASE AMOUNT"); 
								 
								
								XSSFCell cell9 = rowhead.createCell(9);
								cell9.setCellStyle(style);
								cell9.setCellValue("IGST RATE"); 

								XSSFCell cell10 = rowhead.createCell(10);
								cell10.setCellStyle(style);
								cell10.setCellValue("IGST AMT"); 
								
								XSSFCell cell11 = rowhead.createCell(11);
								cell11.setCellStyle(style);
								cell11.setCellValue("CGST RATE"); 

								XSSFCell cell12 = rowhead.createCell(12);
								cell12.setCellStyle(style);
								cell12.setCellValue("CGST AMT"); 
								 
								
								XSSFCell cell13 = rowhead.createCell(13);
								cell13.setCellStyle(style);
								cell13.setCellValue("SGST RATE"); 
								
								XSSFCell cell14 = rowhead.createCell(14);
								cell14.setCellStyle(style);
								cell14.setCellValue("SGST AMT"); 

								XSSFCell cell15 = rowhead.createCell(15);
								cell15.setCellStyle(style);
								cell15.setCellValue("DAN AMOUNT");

								String DanId= (String) session.getAttribute("DanId");
							    List<Object[]> report1=new ArrayList<Object[]>();
								report1=statutsWiseReportService.getDanId(DanId);
								if(!report1.isEmpty()){ 
								int index = 1;
								int srno=1;
								for(Object[] obj1:report1){
									 XSSFRow row = sheet.createRow((short) index);	
			                         row.createCell((short) 0).setCellValue(srno);
			                         row.createCell((short) 1).setCellValue(obj1[0].toString()==null ? "":obj1[0].toString());
			                         row.createCell((short) 2).setCellValue( obj1[1].toString()==null ?"":obj1[1].toString());
			                         String a = String.valueOf(obj1[2]);
			                         if((a.equals("null")) || (a==null)){
			                         	row.createCell((short) 3).setCellValue(""); 
			                         }
			                         else{
			                         	row.createCell((short)3).setCellValue(obj1[2].toString().equals("null") ? "":obj1[2].toString()); 
			                         }
			                         
			                         
			                        // row.createCell((short) 3).setCellValue(String.valueOf(obj1[2]).toString()==null ? "":String.valueOf(obj1[2]));
			                         row.createCell((short) 4).setCellValue(obj1[3].toString()==null ? "":obj1[3].toString());
			                         row.createCell((short) 5).setCellValue(obj1[4].toString()==null ? "":obj1[4].toString());
			                         row.createCell((short) 6).setCellValue(obj1[5].toString()==null ? "":obj1[5].toString());
			                         row.createCell((short) 7).setCellValue(obj1[6].toString()==null ? "":obj1[6].toString());
			                         
			                         if((String.valueOf(obj1[7]).equals("null")) || (String.valueOf(obj1[7])==null)){
			                         	row.createCell((short) 8).setCellValue(""); 
			                         }
			                         else{
			                         	row.createCell((short)8).setCellValue(obj1[7].toString().equals("null") ? "":obj1[7].toString()); 
			                         }
			                         
			                         if((String.valueOf(obj1[8]).equals("null")) || (String.valueOf(obj1[8])==null)){
				                         	row.createCell((short) 9).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)9).setCellValue(obj1[8].toString().equals("null") ? "":obj1[8].toString()); 
				                         }
			                         
			                         if((String.valueOf(obj1[9]).equals("null")) || (String.valueOf(obj1[9])==null)){
				                         	row.createCell((short) 10).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)10).setCellValue(obj1[9].toString().equals("null") ? "":obj1[9].toString()); 
				                         }
			                         
			                         if((String.valueOf(obj1[10]).equals("null")) || (String.valueOf(obj1[10])==null)){
				                         	row.createCell((short) 11).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)11).setCellValue(obj1[10].toString().equals("null") ? "":obj1[10].toString()); 
				                         }
			                         
			                         if((String.valueOf(obj1[11]).equals("null")) || (String.valueOf(obj1[11])==null)){
				                         	row.createCell((short) 12).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)12).setCellValue(obj1[11].toString().equals("null") ? "":obj1[11].toString()); 
				                         }
			                         
			                         if((String.valueOf(obj1[12]).equals("null")) || (String.valueOf(obj1[12])==null)){
				                         	row.createCell((short) 13).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)13).setCellValue(obj1[12].toString().equals("null") ? "":obj1[12].toString()); 
				                         }
			                         
			                         if((String.valueOf(obj1[13]).equals("null")) || (String.valueOf(obj1[13])==null)){
				                         	row.createCell((short) 14).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)14).setCellValue(obj1[13].toString().equals("null") ? "":obj1[13].toString()); 
				                         }
			                         
			                         if((String.valueOf(obj1[14]).equals("null")) || (String.valueOf(obj1[14])==null)){
				                         	row.createCell((short) 15).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)15).setCellValue(obj1[14].toString().equals("null") ? "":obj1[14].toString()); 
				                         }
			                         
			                         
			                       //  row.createCell((short) 8).setCellValue( String.valueOf(obj1[7])==null ? "":String.valueOf(obj1[7]));
			                        // row.createCell((short) 9).setCellValue(String.valueOf(obj1[8])==null ? "":String.valueOf(obj1[8]));
			                        // row.createCell((short) 10).setCellValue(String.valueOf(obj1[9])==null ? "":String.valueOf(obj1[9]));
			                        // row.createCell((short) 11).setCellValue(String.valueOf(obj1[10])==null ? "":String.valueOf(obj1[10]));
			                        // row.createCell((short) 12).setCellValue(String.valueOf(obj1[11])==null ? "":String.valueOf(obj1[11]));
			                        // row.createCell((short) 13).setCellValue(String.valueOf(obj1[12])==null ? "":String.valueOf(obj1[12]));
			                         //row.createCell((short) 14).setCellValue(String.valueOf(obj1[13])==null ? "":String.valueOf(obj1[13]));
			                        // row.createCell((short) 15).setCellValue(String.valueOf(obj1[14])==null ? "":String.valueOf(obj1[14]));
			                         srno++;
			                         index++; 
								 }
								}
								FileOutputStream fileOut = new FileOutputStream(filePath);
								hwb.write(fileOut);
								fileOut.close();

								ModelAndView model = new ModelAndView("newRolePage");

								model.addObject( "excelFileDownLoadSuccessfully", "File DownLoaded Successfully in this location F:/ExcelReports/nbfcDANGFReportExcelFile.xls.");

								File downloadFile = new File(filePath);
								log.info("downloadFile =" + downloadFile);
								FileInputStream inputStream = new FileInputStream(downloadFile);

								response.setContentLength((int) downloadFile.length());

								// set headers for the response
								String headerKey = "Content-Disposition";
								String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
								response.setHeader(headerKey, headerValue);

								// get output stream of the response
								OutputStream outStream = response.getOutputStream();

								byte[] buffer = new byte[BUFFER_SIZE];
								int bytesRead = -1;

								// write bytes read from the input stream into the output stream
								while ((bytesRead = inputStream.read(buffer)) != -1) {
									outStream.write(buffer, 0, bytesRead);
								}
								inputStream.close();
								outStream.close();
								return model;
							} catch (Exception e) {
								e.printStackTrace();
								//log.info("Exception == " + e);
								//System.out.println("Exception == " + e);
							}
							return null;
						}
					
					
					 @RequestMapping(value = "/CGPNDetails", method = RequestMethod.GET)
						public ModelAndView getBorworDetails(
								@ModelAttribute("command") ApplicationStatusDetailsBean bean,
								BindingResult result, String fileId, HttpSession session,HttpServletRequest request,
								Model model) throws JRException, ParseException {
							Map<String, Object> model1 = new HashMap<String, Object>();
							String Role = (String) session.getAttribute("uRole");
							ApplicationStatusDetailsBean appBean;
							// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
							String cgpnNo=request.getParameter("cgpnNo");
							session.setAttribute("cgpnNo", cgpnNo);
							if (Role.equals("CMAKER")) {
								userId = "ADMIN";
								model1.put("guaranteelist", userActivityService.getActivity(
										"CGTMSEMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"CGTMSEMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"CGTMSEMAKER", "Receipt_Payments"));

								model1.put("repList",
										userActivityService.getReport("CGTMSEMAKER", "User_Report"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("CGTMSEMAKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "cgtmseMakerHome");

							} else if (Role.equals("CCHECKER")) {
								userId = "ADMIN";
								model1.put("adminlist", userActivityService.getActivity(
										"CGTMSECHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"CGTMSECHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"CGTMSECHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"CGTMSECHECKER", "Receipt_Payments"));
								model1.put("CList", userActivityService.getReport("CGTMSECHECKER",
										"Claim_Lodgement"));
								model1.put("repList", userActivityService.getReport(
										"CGTMSECHECKER", "User_Report"));
								model1.put("CBMFList", userActivityService.getReport(
										"CGTMSECHECKER", "Claim_Bank_Mandate"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("CGTMSECHECKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "cgtmseCheckerHome");
								// return null;
							} else if (Role.equals("NMAKER")) {
								// added by say 6 feb-----------------------
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity(
										"NBFCMAKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"NBFCMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"NBFCMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"NBFCMAKER", "Receipt_Payments"));
								model1.put("repList",
										userActivityService.getReport("NBFCMAKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCMAKER",
										"Claim_Lodgement"));
								model1.put("CBMFList", userActivityService.getReport("NBFCMAKER",
										"Claim_Bank_Mandate"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("NBFCMAKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "nbfcMakerHome");

							} else if (Role.equals("NCHECKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity(
										"NBFCCHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"NBFCCHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"NBFCCHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"NBFCCHECKER", "Receipt_Payments"));
								model1.put("repList",
										userActivityService.getReport("NBFCCHECKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCCHECKER",
										"Claim_Lodgement"));
								model1.put("CBMFList", userActivityService.getReport("NBFCCHECKER",
										"Claim_Bank_Mandate"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("NBFCCHECKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "nbfcCheckerHome");

							}

							// Date toDate=new
							// SimpleDateFormat("dd/MM/yyyy").parse(bean.getToDate());
							// Date fromDate=new
							// SimpleDateFormat("dd/MM/yyyy").parse(bean.getFromDate());
							appBean = statutsWiseReportService.getapplicationDetails(cgpnNo);
							model1.put("applicationDetails", appBean);
							return new ModelAndView("DANGFReportInputForm", model1);
							// return null;
						}
					
					 @RequestMapping(value = "/DANGFbackButton2", method = RequestMethod.POST)
						public ModelAndView DANGFbackButtonbackButton2( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
								Model model,HttpServletRequest request) throws JRException, ParseException {
							        Map<String, Object> model1 = new HashMap<String, Object>();
							        String Role = (String) session.getAttribute("uRole");
							if (result.hasErrors()) {
								if (Role.equals("CMAKER")) {
									model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
					                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
							        model1.put("homePage", "cgtmseMakerHome");
					             } else if (Role.equals("CCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
									model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
									model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
									model1.put("homePage", "cgtmseCheckerHome");
								 } else if (Role.equals("NMAKER")) {
									model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
									model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcMakerHome");

								} else if (Role.equals("NCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
									model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcCheckerHome");

								}
								return new ModelAndView("ASFReportInputForm", model1);
							}

							if (Role.equals("CMAKER")) {
								userId = "ADMIN";
								model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
					            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
								model1.put("homePage", "cgtmseMakerHome");

							} else if (Role.equals("CCHECKER")) {
								userId = "ADMIN";
								model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
								model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
								model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
								model1.put("homePage", "cgtmseCheckerHome");
							 } else if (Role.equals("NMAKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcMakerHome");

							} else if (Role.equals("NCHECKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcCheckerHome");

							}
							
							/*String gStatus=(String) session.getAttribute("status");
							String toDateF=(String) session.getAttribute("TDate");
							String fromDateF=(String) session.getAttribute("FDate");
							Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(toDateF);
							Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDateF);
							rows = stateWiseReportService.getStateDetails(userId,Role,toDate, fromDate,gStatus);
							if (!rows.isEmpty()) {
								model1.put("rows", rows);
							} else {
								model1.put("message", "NO Data Found !!");
							}*/
							String DanId=(String) session.getAttribute("DanId");
							List<Object[]> rows3=null;
							   rows3 = statutsWiseReportService.getDanId(DanId);
							   if (!rows3.isEmpty()) {
									model1.put("rows3", rows3);
								} else {
									model1.put("message", "NO Data Found !!");
								}

							return new ModelAndView("DANGFReportInputForm", model1);
							// return null;
						}
					 
					 @RequestMapping(value = "/DANGFbackButton1", method = RequestMethod.POST)
						public ModelAndView DANGFbackButtonbackButton1( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
								Model model,HttpServletRequest request) throws JRException, ParseException {
							        Map<String, Object> model1 = new HashMap<String, Object>();
							        String Role = (String) session.getAttribute("uRole");
							if (result.hasErrors()) {
								if (Role.equals("CMAKER")) {
									model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
					                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
							        model1.put("homePage", "cgtmseMakerHome");
					             } else if (Role.equals("CCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
									model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
									model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
									model1.put("homePage", "cgtmseCheckerHome");
								 } else if (Role.equals("NMAKER")) {
									model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
									model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcMakerHome");

								} else if (Role.equals("NCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
									model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcCheckerHome");

								}
								return new ModelAndView("ASFReportInputForm", model1);
							}

							if (Role.equals("CMAKER")) {
								userId = "ADMIN";
								model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
					            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
								model1.put("homePage", "cgtmseMakerHome");

							} else if (Role.equals("CCHECKER")) {
								userId = "ADMIN";
								model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
								model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
								model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
								model1.put("homePage", "cgtmseCheckerHome");
							 } else if (Role.equals("NMAKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcMakerHome");

							} else if (Role.equals("NCHECKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcCheckerHome");

							}
							
							 
							String Role1=(String) session.getAttribute("Role");
							String userId=(String) session.getAttribute("userId");
							String fromDate=(String) session.getAttribute("fromDate");
							String toDate=(String) session.getAttribute("toDate");
							String Mem_Id=(String) session.getAttribute("Mem_Id");
							String ssiName=(String) session.getAttribute("ssiName");
							List<Object[]> rows1=null;
						  if(Role.equals("CCHECKER") || Role.equals("CMAKER")){
								rows1=statutsWiseReportService.getDANGFDetails(Role1, userId, fromDate, toDate, Mem_Id, ssiName);
								 
							}else{
								rows1=statutsWiseReportService.getDANGFDetails(Role1, userId, fromDate, toDate, Mem_Id, ssiName);
							 }
							if (!rows1.isEmpty()) {
								model1.put("rows1", rows1);
							 } else {
								model1.put("message", "NO Data Found !!");
							} 

							return new ModelAndView("DANGFReportInputForm", model1);
							// return null;
						}
					 
					 
					 @RequestMapping(value = "/DANASFReportDetailList", method = RequestMethod.POST)
						public ModelAndView DANASFReportDetailList(
								@ModelAttribute("command") NPADetailsBean bean,
								BindingResult result, HttpSession session, Model model)
								throws JRException, ParseException {
							Map<String, Object> model1 = new HashMap<String, Object>();
							List<NPADetailsBean> NPADetailList=null;
							
							session.setAttribute("NPADetailList",null);
							session.removeAttribute("NPADetailList");
							
							String Role = (String) session.getAttribute("uRole");
							//String userId = (String) session.getAttribute("userId");
							//memberId = npaService.getMemberId(userId);
							String mliLongName=bean.getMliLongName();
							 
							System.out.println("mliLongName==194==="+mliLongName);
							validator.MliReportDetailsValidate(bean, result);
							if (result.hasErrors()) {
								if (Role.equals("CMAKER")) {
									// userId = "ADMIN";
									model1.put("guaranteelist", userActivityService.getActivity(
											"CGTMSEMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"CGTMSEMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"CGTMSEMAKER", "Receipt_Payments"));
									model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
									model1.put("repList", userActivityService.getReport(
											"CGTMSEMAKER", "User_Report"));
									model1.put("homePage", "cgtmseMakerHome");

								} else if (Role.equals("CCHECKER")) {
									// userId = "ADMIN";
									model1.put("adminlist", userActivityService.getActivity(
											"CGTMSECHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity(
											"CGTMSECHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"CGTMSECHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"CGTMSECHECKER", "Receipt_Payments"));
									model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
									model1.put("repList", userActivityService.getReport(
											"CGTMSECHECKER", "User_Report"));
									model1.put("homePage", "cgtmseCheckerHome");
									// return null;
								} else if (Role.equals("NMAKER")) {
									model1.put("adminlist", userActivityService.getActivity(
											"NBFCMAKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity(
											"NBFCMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"NBFCMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"NBFCMAKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport(
											"NBFCMAKER", "User_Report"));
									model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcMakerHome");

								} else if (Role.equals("NCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity(
											"NBFCCHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity(
											"NBFCCHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"NBFCCHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"NBFCCHECKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport(
											"NBFCCHECKER", "User_Report"));
									 model1.put("homePage", "nbfcCheckerHome");

								}
								return new ModelAndView("DANASFReportInputForm", model1);
							}

							if (Role.equals("CMAKER")) {
								userId = "ADMIN";
								model1.put("guaranteelist", userActivityService.getActivity(
										"CGTMSEMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"CGTMSEMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"CGTMSEMAKER", "Receipt_Payments"));

								model1.put("repList",
										userActivityService.getReport("CGTMSEMAKER", "User_Report"));
								model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
								model1.put("homePage", "cgtmseMakerHome");

							} else if (Role.equals("CCHECKER")) {
								userId = "ADMIN";
								model1.put("adminlist", userActivityService.getActivity(
										"CGTMSECHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"CGTMSECHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"CGTMSECHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"CGTMSECHECKER", "Receipt_Payments"));

								model1.put("repList", userActivityService.getReport(
										"CGTMSECHECKER", "User_Report"));
								model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
								model1.put("homePage", "cgtmseCheckerHome");
								// return null;
							} else if (Role.equals("NMAKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity(
										"NBFCMAKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"NBFCMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"NBFCMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"NBFCMAKER", "Receipt_Payments"));
								model1.put("repList",
										userActivityService.getReport("NBFCMAKER", "User_Report"));
								model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcMakerHome");

							} else if (Role.equals("NCHECKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity(
										"NBFCCHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"NBFCCHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"NBFCCHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"NBFCCHECKER", "Receipt_Payments"));
								model1.put("repList",
										userActivityService.getReport("NBFCCHECKER", "User_Report"));
								model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcCheckerHome");

							}
							String Mem_Id ="";
							if(!mliLongName.equals("ALL")){
								mem_id = userActivityService.getBankID(mliLongName);
								Mem_Id= mem_id.getBnkId()+ mem_id.getBrnName()+ mem_id.getZneID();
								System.out.println("DAN ASF Report For Specific=="+Mem_Id);
							}
							else{
								Mem_Id="ALL";
								System.out.println("DAN ASF Report For ALL=="+Mem_Id);
							}
							List<Object[]> rows1=null;

							//Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(bean.getToDate());
							//Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean .getFromDate());
							
							String toDate= DateFormate.dateformate(bean.getToDate());
							String fromDate=DateFormate.dateformate(bean .getFromDate());
							
							//String ssiName="ALL";
							String ssiName=bean.getNameOffSsiUnit();
							session.setAttribute("Role", Role); 
							session.setAttribute("userId", userId); 
							session.setAttribute("fromDate", fromDate); 
							session.setAttribute("toDate", toDate); 
							session.setAttribute("Mem_Id", Mem_Id); 
							session.setAttribute("ssiName", ssiName); 
							
							
							if(Role.equals("CCHECKER") || Role.equals("CMAKER")){
								rows1=statutsWiseReportService.getDANASFDetails(Role, userId, fromDate, toDate, Mem_Id, ssiName);
								// rows = statutsWiseReportService.getDanASFDetail(Role, userId, fromDate, toDate, Mem_Id,ssiName);
							}else{
								rows1=statutsWiseReportService.getDANASFDetails(Role, userId, fromDate, toDate, Mem_Id, ssiName);
								// rows = statutsWiseReportService.getDanASFDetail(Role, userId, fromDate, toDate, Mem_Id,Mem_Id);
							 }
							if (!rows1.isEmpty()) {
								model1.put("rows1", rows1);
							 } else {
								model1.put("message", "NO Data Found !!");
							} 
							return new ModelAndView("DANASFReportInputForm", model1);
						} 
					 @RequestMapping(value = "/DANReportASF", method = RequestMethod.GET)
						public ModelAndView DANReportASF(
								@ModelAttribute("command") NPADetailsBean bean,
								BindingResult result, HttpSession session, Model model)
								throws JRException {
							Map<String, Object> model1 = new HashMap<String, Object>();
							String Role = (String) session.getAttribute("uRole");
							 //userId = (String) session.getAttribute("userId");
							//String loginUserMemId = npaService.getMemberId(userId);
							// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
							if (Role.equals("CMAKER")) {
								model1.put("guaranteelist", userActivityService.getActivity(
										"CGTMSEMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"CGTMSEMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"CGTMSEMAKER", "Receipt_Payments"));
								model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
								model1.put("repList",
										userActivityService.getReport("CGTMSEMAKER", "User_Report"));
								model1.put("stateList", prepareStateListofBean(stateService.listStates("CCA")));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("CGTMSEMAKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								//model1.put("MLIID", loginUserMemId);
								model1.put("homePage", "cgtmseMakerHome");

							} else if (Role.equals("CCHECKER")) {
								model1.put("adminlist", userActivityService.getActivity(
										"CGTMSECHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"CGTMSECHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"CGTMSECHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"CGTMSECHECKER", "Receipt_Payments"));
								model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
								model1.put("repList", userActivityService.getReport(
										"CGTMSECHECKER", "User_Report"));
								//model1.put("MLIID", loginUserMemId);
								model1.put("homePage", "cgtmseCheckerHome");
								// return null;
							} else if (Role.equals("NMAKER")) {
								// added by say 6 feb-----------------------
								model1.put("adminlist", userActivityService.getActivity(
										"NBFCMAKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"NBFCMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"NBFCMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"NBFCMAKER", "Receipt_Payments"));
								model1.put("repList",
										userActivityService.getReport("NBFCMAKER", "User_Report"));
								model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
								//model1.put("MLIID", loginUserMemId);
								model1.put("homePage", "nbfcMakerHome");

							} else if (Role.equals("NCHECKER")) {
								model1.put("adminlist", userActivityService.getActivity(
										"NBFCCHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"NBFCCHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"NBFCCHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"NBFCCHECKER", "Receipt_Payments"));
								model1.put("repList",
										userActivityService.getReport("NBFCCHECKER", "User_Report"));
								model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
								//model1.put("MLIID", loginUserMemId);
								model1.put("homePage", "nbfcCheckerHome");

							}
							return new ModelAndView("DANASFReportInputForm", model1);
							// return null;
						}	
					 
					 
					 @RequestMapping(value = "/DimandAdvaceNo", method = RequestMethod.GET)
						public ModelAndView DimandAdvaceNo(
								@ModelAttribute("command") NPADetailsBean bean,
								BindingResult result, HttpSession session, Model model,HttpServletRequest request)
								throws JRException, ParseException {
							Map<String, Object> model1 = new HashMap<String, Object>();
							String Role = (String) session.getAttribute("uRole");
							String DanId=request.getParameter("DemandAdvaceNO");
							session.setAttribute("DanId", DanId);
							//bean.setToDate(request.getParameter("p_todate"));
							//bean.setFromDate(request.getParameter("p_fromdate"));
							// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
					       
							/*validator.stateInputDetailsValidate(bean, result);*/
							if (result.hasErrors()) {
								if (Role.equals("CMAKER")) {
									// userId = "ADMIN";
									model1.put("guaranteelist", userActivityService.getActivity(
											"CGTMSEMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"CGTMSEMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"CGTMSEMAKER", "Receipt_Payments"));

									model1.put("repList", userActivityService.getReport(
											"CGTMSEMAKER", "User_Report"));
							
									// model1.put("actNameHome",
									// userActivityService.getActivityName("CGTMSEMAKER",
									// "cgpanDetailReport"));// Added by Say 31 Jan19
									model1.put("homePage", "cgtmseMakerHome");

								} else if (Role.equals("CCHECKER")) {
									// userId = "ADMIN";
									model1.put("adminlist", userActivityService.getActivity(
											"CGTMSECHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity(
											"CGTMSECHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"CGTMSECHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"CGTMSECHECKER", "Receipt_Payments"));
									model1.put("CList",
											userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
									model1.put("repList", userActivityService.getReport(
											"CGTMSECHECKER", "User_Report"));
									// model1.put("actNameHome",
									// userActivityService.getActivityName("CGTMSECHECKER",
									// "cgpanDetailReport"));// Added by Say 31 Jan19
									model1.put("homePage", "cgtmseCheckerHome");
									// return null;
								} else if (Role.equals("NMAKER")) {
									// added by say 6 feb-----------------------
									// userId = (String) session.getAttribute("userId");
									model1.put("adminlist", userActivityService.getActivity(
											"NBFCMAKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity(
											"NBFCMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"NBFCMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"NBFCMAKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport(
											"NBFCMAKER", "User_Report"));
									model1.put("CList",
											userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
									// model1.put("actNameHome",
									// userActivityService.getActivityName("NBFCMAKER",
									// "cgpanDetailReport"));// Added by Say 31 Jan19
									model1.put("homePage", "nbfcMakerHome");

								} else if (Role.equals("NCHECKER")) {
									// userId = (String) session.getAttribute("userId");
									model1.put("adminlist", userActivityService.getActivity(
											"NBFCCHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity(
											"NBFCCHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"NBFCCHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"NBFCCHECKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport(
											"NBFCCHECKER", "User_Report"));
									model1.put("CList",
											userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
									// model1.put("actNameHome",
									// userActivityService.getActivityName("NBFCCHECKER",
									// "cgpanDetailReport"));// Added by Say 31 Jan19
									model1.put("homePage", "nbfcCheckerHome");

								}
								return new ModelAndView("DANASFReportInputForm", model1);
							}

							if (Role.equals("CMAKER")) {
								userId = "ADMIN";
								model1.put("guaranteelist", userActivityService.getActivity(
										"CGTMSEMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"CGTMSEMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"CGTMSEMAKER", "Receipt_Payments"));

								model1.put("repList",
										userActivityService.getReport("CGTMSEMAKER", "User_Report"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("CGTMSEMAKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "cgtmseMakerHome");

							} else if (Role.equals("CCHECKER")) {
								userId = "ADMIN";
								model1.put("adminlist", userActivityService.getActivity(
										"CGTMSECHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"CGTMSECHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"CGTMSECHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"CGTMSECHECKER", "Receipt_Payments"));
								model1.put("CList",
										userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
								model1.put("repList", userActivityService.getReport(
										"CGTMSECHECKER", "User_Report"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("CGTMSECHECKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "cgtmseCheckerHome");
								// return null;
							} else if (Role.equals("NMAKER")) {
								// added by say 6 feb-----------------------
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity(
										"NBFCMAKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"NBFCMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"NBFCMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"NBFCMAKER", "Receipt_Payments"));
								model1.put("repList",
										userActivityService.getReport("NBFCMAKER", "User_Report"));
								model1.put("CList",
										userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("NBFCMAKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "nbfcMakerHome");

							} else if (Role.equals("NCHECKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity(
										"NBFCCHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"NBFCCHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"NBFCCHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"NBFCCHECKER", "Receipt_Payments"));
								model1.put("repList",
										userActivityService.getReport("NBFCCHECKER", "User_Report"));
								model1.put("CList",
										userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("NBFCCHECKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "nbfcCheckerHome");

							}
							String Role1= (String) session.getAttribute("Role"); 
							String userId=(String) session.getAttribute("userId"); 
							String fromDate=(String) session.getAttribute("fromDate");  
							String toDate= (String) session.getAttribute("toDate");  
							String Mem_Id= (String) session.getAttribute("Mem_Id"); 
							String ssiName= (String) session.getAttribute("ssiName"); 
							//List<MliWiseReportDetailBean> report=statutsWiseReportService.getDanGfReportDowanload(Role, userId, fromDate, toDate, Mem_Id,ssiName);
							List<Object[]> report1=new ArrayList<Object[]>();
							/*report1=statutsWiseReportService.getDANGFDetails(Role1, userId, fromDate, toDate, Mem_Id, ssiName);
							if(!report1.isEmpty()){ 
								model1.put("rows1", report1);
								
							}
							else{
								model1.put("message", "NO Data Found !!");
							}*/
							
						   List<Object[]> rows3=null;
						   rows3 = statutsWiseReportService.getDanIdForASF(DanId);
						   if (!rows3.isEmpty()) {
								model1.put("rows3", rows3);
							} else {
								model1.put("message", "NO Data Found !!");
							}
	                        return new ModelAndView("DANASFReportInputForm", model1);
							// return null;
						}
						
					 
					 @RequestMapping(value = "/ASFCGPNDetails", method = RequestMethod.GET)
						public ModelAndView ASFCGPNDetails(
								@ModelAttribute("command") ApplicationStatusDetailsBean bean,
								BindingResult result, String fileId, HttpSession session,HttpServletRequest request,
								Model model) throws JRException, ParseException {
							Map<String, Object> model1 = new HashMap<String, Object>();
							String Role = (String) session.getAttribute("uRole");
							ApplicationStatusDetailsBean appBean;
							// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
							String cgpnNo=request.getParameter("ASFcgpnNo");
							session.setAttribute("cgpnNo", cgpnNo);
							if (Role.equals("CMAKER")) {
								userId = "ADMIN";
								model1.put("guaranteelist", userActivityService.getActivity(
										"CGTMSEMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"CGTMSEMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"CGTMSEMAKER", "Receipt_Payments"));

								model1.put("repList",
										userActivityService.getReport("CGTMSEMAKER", "User_Report"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("CGTMSEMAKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "cgtmseMakerHome");

							} else if (Role.equals("CCHECKER")) {
								userId = "ADMIN";
								model1.put("adminlist", userActivityService.getActivity(
										"CGTMSECHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"CGTMSECHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"CGTMSECHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"CGTMSECHECKER", "Receipt_Payments"));
								model1.put("CList", userActivityService.getReport("CGTMSECHECKER",
										"Claim_Lodgement"));
								model1.put("repList", userActivityService.getReport(
										"CGTMSECHECKER", "User_Report"));
								model1.put("CBMFList", userActivityService.getReport(
										"CGTMSECHECKER", "Claim_Bank_Mandate"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("CGTMSECHECKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "cgtmseCheckerHome");
								// return null;
							} else if (Role.equals("NMAKER")) {
								// added by say 6 feb-----------------------
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity(
										"NBFCMAKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"NBFCMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"NBFCMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"NBFCMAKER", "Receipt_Payments"));
								model1.put("repList",
										userActivityService.getReport("NBFCMAKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCMAKER",
										"Claim_Lodgement"));
								model1.put("CBMFList", userActivityService.getReport("NBFCMAKER",
										"Claim_Bank_Mandate"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("NBFCMAKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "nbfcMakerHome");

							} else if (Role.equals("NCHECKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity(
										"NBFCCHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity(
										"NBFCCHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity(
										"NBFCCHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity(
										"NBFCCHECKER", "Receipt_Payments"));
								model1.put("repList",
										userActivityService.getReport("NBFCCHECKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCCHECKER",
										"Claim_Lodgement"));
								model1.put("CBMFList", userActivityService.getReport("NBFCCHECKER",
										"Claim_Bank_Mandate"));
								// model1.put("actNameHome",
								// userActivityService.getActivityName("NBFCCHECKER",
								// "cgpanDetailReport"));// Added by Say 31 Jan19
								model1.put("homePage", "nbfcCheckerHome");

							}

							// Date toDate=new
							// SimpleDateFormat("dd/MM/yyyy").parse(bean.getToDate());
							// Date fromDate=new
							// SimpleDateFormat("dd/MM/yyyy").parse(bean.getFromDate());
							appBean = statutsWiseReportService.getapplicationDetails(cgpnNo);
							model1.put("applicationDetails", appBean);
							return new ModelAndView("DANASFReportInputForm", model1);
							// return null;
						}
					 
					 @RequestMapping(value = "/DANASFbackButton", method = RequestMethod.POST)
						public ModelAndView DANASFbackButton( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
								Model model,HttpServletRequest request) throws JRException, ParseException {
							        Map<String, Object> model1 = new HashMap<String, Object>();
							        String Role = (String) session.getAttribute("uRole");
							if (result.hasErrors()) {
								if (Role.equals("CMAKER")) {
									model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
					                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
							        model1.put("homePage", "cgtmseMakerHome");
					             } else if (Role.equals("CCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
									model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
									model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
									model1.put("homePage", "cgtmseCheckerHome");
								 } else if (Role.equals("NMAKER")) {
									model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
									model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcMakerHome");

								} else if (Role.equals("NCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
									model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcCheckerHome");

								}
								return new ModelAndView("DANASFReportInputForm", model1);
							}

							if (Role.equals("CMAKER")) {
								userId = "ADMIN";
								model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
					            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
								model1.put("homePage", "cgtmseMakerHome");

							} else if (Role.equals("CCHECKER")) {
								userId = "ADMIN";
								model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
								model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
								model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
								model1.put("homePage", "cgtmseCheckerHome");
							 } else if (Role.equals("NMAKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcMakerHome");

							} else if (Role.equals("NCHECKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcCheckerHome");

							}
							
							/*String gStatus=(String) session.getAttribute("status");
							String toDateF=(String) session.getAttribute("TDate");
							String fromDateF=(String) session.getAttribute("FDate");
							Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(toDateF);
							Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDateF);
							rows = stateWiseReportService.getStateDetails(userId,Role,toDate, fromDate,gStatus);
							if (!rows.isEmpty()) {
								model1.put("rows", rows);
							} else {
								model1.put("message", "NO Data Found !!");
							}*/

							return new ModelAndView("DANASFReportInputForm", model1);
							// return null;
						}
					 
					 @RequestMapping(value = "/DANASFbackButton1", method = RequestMethod.POST)
						public ModelAndView DANASFbackButton1( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
								Model model,HttpServletRequest request) throws JRException, ParseException {
							        Map<String, Object> model1 = new HashMap<String, Object>();
							        String Role = (String) session.getAttribute("uRole");
							if (result.hasErrors()) {
								if (Role.equals("CMAKER")) {
									model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
					                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
							        model1.put("homePage", "cgtmseMakerHome");
					             } else if (Role.equals("CCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
									model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
									model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
									model1.put("homePage", "cgtmseCheckerHome");
								 } else if (Role.equals("NMAKER")) {
									model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
									model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcMakerHome");

								} else if (Role.equals("NCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
									model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcCheckerHome");

								}
								return new ModelAndView("DANASFReportInputForm", model1);
							}

							if (Role.equals("CMAKER")) {
								userId = "ADMIN";
								model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
					            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
								model1.put("homePage", "cgtmseMakerHome");

							} else if (Role.equals("CCHECKER")) {
								userId = "ADMIN";
								model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
								model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
								model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
								model1.put("homePage", "cgtmseCheckerHome");
							 } else if (Role.equals("NMAKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcMakerHome");

							} else if (Role.equals("NCHECKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcCheckerHome");

							}
							
							 
							String Role1=(String) session.getAttribute("Role");
							String userId=(String) session.getAttribute("userId");
							String fromDate=(String) session.getAttribute("fromDate");
							String toDate=(String) session.getAttribute("toDate");
							String Mem_Id=(String) session.getAttribute("Mem_Id");
							String ssiName=(String) session.getAttribute("ssiName");
							List<Object[]> rows1=null;
						  if(Role.equals("CCHECKER") || Role.equals("CMAKER")){
								rows1=statutsWiseReportService.getDANASFDetails(Role1, userId, fromDate, toDate, Mem_Id, ssiName);
								 
							}else{
								rows1=statutsWiseReportService.getDANASFDetails(Role1, userId, fromDate, toDate, Mem_Id, ssiName);
							 }
							if (!rows1.isEmpty()) {
								model1.put("rows1", rows1);
							 } else {
								model1.put("message", "NO Data Found !!");
							} 

							return new ModelAndView("DANASFReportInputForm", model1);
							// return null;
						}
					 @RequestMapping(value = "/DANASFbackButton2", method = RequestMethod.POST)
						public ModelAndView DANASFbackButton2( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
								Model model,HttpServletRequest request) throws JRException, ParseException {
							        Map<String, Object> model1 = new HashMap<String, Object>();
							        String Role = (String) session.getAttribute("uRole");
							if (result.hasErrors()) {
								if (Role.equals("CMAKER")) {
									model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
					                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
							        model1.put("homePage", "cgtmseMakerHome");
					             } else if (Role.equals("CCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
									model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
									model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
									model1.put("homePage", "cgtmseCheckerHome");
								 } else if (Role.equals("NMAKER")) {
									model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
									model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcMakerHome");

								} else if (Role.equals("NCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
									model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
									model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcCheckerHome");

								}
								return new ModelAndView("DANASFReportInputForm", model1);
							}

							if (Role.equals("CMAKER")) {
								userId = "ADMIN";
								model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
					            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
								model1.put("homePage", "cgtmseMakerHome");

							} else if (Role.equals("CCHECKER")) {
								userId = "ADMIN";
								model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
								model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
								model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
								model1.put("homePage", "cgtmseCheckerHome");
							 } else if (Role.equals("NMAKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcMakerHome");

							} else if (Role.equals("NCHECKER")) {
								userId = (String) session.getAttribute("userId");
								model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
								model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
								model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
								model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
								model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
								model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
								model1.put("homePage", "nbfcCheckerHome");

							}
							
							/*String gStatus=(String) session.getAttribute("status");
							String toDateF=(String) session.getAttribute("TDate");
							String fromDateF=(String) session.getAttribute("FDate");
							Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(toDateF);
							Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDateF);
							rows = stateWiseReportService.getStateDetails(userId,Role,toDate, fromDate,gStatus);
							if (!rows.isEmpty()) {
								model1.put("rows", rows);
							} else {
								model1.put("message", "NO Data Found !!");
							}*/
							String DanId=(String) session.getAttribute("DanId");
							List<Object[]> rows3=null;
							   rows3 = statutsWiseReportService.getDanIdForASF(DanId);
							   if (!rows3.isEmpty()) {
									model1.put("rows3", rows3);
								} else {
									model1.put("message", "NO Data Found !!");
								}

							return new ModelAndView("DANASFReportInputForm", model1);
							// return null;
						}
					 @RequestMapping(value = "/DemandAdviceNumberASFDetailDownload", method = RequestMethod.GET)
						public ModelAndView DemandAdviceNumberASFDetailDownload(
								@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
								HttpServletRequest request, HttpServletResponse response,
								HttpSession session) throws IOException {
							try {
		                        String ASFSummaryDetailsFileName1="DemandAdvaceASFFileName.xlsx";
								String filePath = downloadFileDirPath + File.separator + ASFSummaryDetailsFileName1;
								System.out.println(filePath);
								File file = new File(downloadFileDirPath);
								boolean isCreated = file.mkdir();
				                if (isCreated) {
									File file1 = new File(filePath);
									boolean isExists = file1.exists();
									if (isExists) {

									} else {
										file1.createNewFile();
									}

								}
				                
				                XSSFWorkbook hwb = new XSSFWorkbook();
								XSSFSheet sheet = hwb.createSheet("DemandAdvaceASFDownLoadedFile");
		                        CellStyle style = hwb.createCellStyle();
								Font font = hwb.createFont();
								font.setFontHeightInPoints((short) 11);
								font.setFontName(HSSFFont.FONT_ARIAL);
								font.setBoldweight(HSSFFont.COLOR_NORMAL);
								font.setBold(true);
								font.setColor(HSSFColor.DARK_BLUE.index);
								style.setFont(font);
								style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
								style.setFillPattern(CellStyle.SOLID_FOREGROUND);
								sheet.createFreezePane(0,1 ); // Freeze 1st Row
																// sheet.createFreezePane(int
																// colSplit, int rowSplit, int
																// leftmostColumn, int topRow)
		                    XSSFRow rowhead = sheet.createRow((short) 0);

								XSSFCell cell0 = rowhead.createCell(0);
								cell0.setCellStyle(style);
								cell0.setCellValue("SrNo");// Done 1

								 
								XSSFCell cell1 = rowhead.createCell(1);
								cell1.setCellStyle(style);
								cell1.setCellValue("CGPAN");// Done 3

								XSSFCell cell2 = rowhead.createCell(2);
								cell2.setCellStyle(style);
								cell2.setCellValue("MSE NAME"); 

								XSSFCell cell3 = rowhead.createCell(3);
								cell3.setCellStyle(style);
								cell3.setCellValue("GUARANTEE AMOUNT"); 
								
								XSSFCell cell4 = rowhead.createCell(4);
								cell4.setCellStyle(style);
								cell4.setCellValue("OutStanding AMOUNT"); 
								
								XSSFCell cell5 = rowhead.createCell(5);
								cell5.setCellStyle(style);
								cell5.setCellValue("NBFC UPLOADED DATE"); 
								 

								XSSFCell cell6 = rowhead.createCell(6);
								cell6.setCellStyle(style);
								cell6.setCellValue("PORTFOLIO RATE"); 

								XSSFCell cell7 = rowhead.createCell(7);
								cell7.setCellStyle(style);
								cell7.setCellValue("LOAN ACCOUNT NO"); 
								
								
								XSSFCell cell8 = rowhead.createCell(8);
								cell8.setCellStyle(style);
								cell8.setCellValue("STATE"); 

								XSSFCell cell9 = rowhead.createCell(9);
								cell9.setCellStyle(style);
								cell9.setCellValue("BASE AMOUNT"); 
								 
								
								XSSFCell cell10 = rowhead.createCell(10);
								cell10.setCellStyle(style);
								cell10.setCellValue("IGST RATE"); 

								XSSFCell cell11 = rowhead.createCell(11);
								cell11.setCellStyle(style);
								cell11.setCellValue("IGST AMT"); 
								
								XSSFCell cell12 = rowhead.createCell(12);
								cell12.setCellStyle(style);
								cell12.setCellValue("CGST RATE"); 

								XSSFCell cell13 = rowhead.createCell(13);
								cell13.setCellStyle(style);
								cell13.setCellValue("CGST AMT"); 
								 
								
								XSSFCell cell14 = rowhead.createCell(14);
								cell14.setCellStyle(style);
								cell14.setCellValue("SGST RATE"); 
								
								XSSFCell cell15 = rowhead.createCell(15);
								cell15.setCellStyle(style);
								cell15.setCellValue("SGST AMT"); 

								XSSFCell cell16 = rowhead.createCell(16);
								cell16.setCellStyle(style);
								cell16.setCellValue("DAN AMOUNT");

								String DanId= (String) session.getAttribute("DanId");
							    List<Object[]> report1=new ArrayList<Object[]>();
								report1=statutsWiseReportService.getDanIdForASF(DanId);
								if(!report1.isEmpty()){ 
								int index = 1;
								int srno=1;
								for(Object[] obj1:report1){
									 XSSFRow row = sheet.createRow((short) index);	
			                         row.createCell((short) 0).setCellValue(srno);
			                         row.createCell((short) 1).setCellValue(obj1[0].toString()==null ? "":obj1[0].toString());
			                         row.createCell((short) 2).setCellValue( obj1[1].toString()==null ?"":obj1[1].toString());
			                         String a = String.valueOf(obj1[2]);
			                         if((a.equals("null")) || (a==null)){
			                         	row.createCell((short) 3).setCellValue(""); 
			                         }
			                         else{
			                         	row.createCell((short)3).setCellValue(obj1[2].toString().equals("null") ? "":obj1[2].toString()); 
			                         }
			                         
			                         
			                        // row.createCell((short) 3).setCellValue(String.valueOf(obj1[2]).toString()==null ? "":String.valueOf(obj1[2]));
			                        // row.createCell((short) 4).setCellValue(obj1[3].toString()==null ? "":obj1[3].toString());
			                         
			                         if((String.valueOf(obj1[3]).equals("null")) || (String.valueOf(obj1[3])==null)){
				                         	row.createCell((short) 4).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)4).setCellValue(obj1[3].toString().equals("null") ? "":obj1[3].toString()); 
				                         }
			                         
			                         
			                         
			                         row.createCell((short) 5).setCellValue(obj1[4].toString()==null ? "":obj1[4].toString());
			                         row.createCell((short) 6).setCellValue(obj1[5].toString()==null ? "":obj1[5].toString());
			                         row.createCell((short) 7).setCellValue(obj1[6].toString()==null ? "":obj1[6].toString());
			                         
			                         if((String.valueOf(obj1[7]).equals("null")) || (String.valueOf(obj1[7])==null)){
			                         	row.createCell((short) 8).setCellValue(""); 
			                         }
			                         else{
			                         	row.createCell((short)8).setCellValue(obj1[7].toString().equals("null") ? "":obj1[7].toString()); 
			                         }
			                         
			                         if((String.valueOf(obj1[8]).equals("null")) || (String.valueOf(obj1[8])==null)){
				                         	row.createCell((short) 9).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)9).setCellValue(obj1[8].toString().equals("null") ? "":obj1[8].toString()); 
				                         }
			                         
			                         if((String.valueOf(obj1[9]).equals("null")) || (String.valueOf(obj1[9])==null)){
				                         	row.createCell((short) 10).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)10).setCellValue(obj1[9].toString().equals("null") ? "":obj1[9].toString()); 
				                         }
			                         
			                         if((String.valueOf(obj1[10]).equals("null")) || (String.valueOf(obj1[10])==null)){
				                         	row.createCell((short) 11).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)11).setCellValue(obj1[10].toString().equals("null") ? "":obj1[10].toString()); 
				                         }
			                         
			                         if((String.valueOf(obj1[11]).equals("null")) || (String.valueOf(obj1[11])==null)){
				                         	row.createCell((short) 12).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)12).setCellValue(obj1[11].toString().equals("null") ? "":obj1[11].toString()); 
				                         }
			                         
			                         if((String.valueOf(obj1[12]).equals("null")) || (String.valueOf(obj1[12])==null)){
				                         	row.createCell((short) 13).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)13).setCellValue(obj1[12].toString().equals("null") ? "":obj1[12].toString()); 
				                         }
			                         
			                         if((String.valueOf(obj1[13]).equals("null")) || (String.valueOf(obj1[13])==null)){
				                         	row.createCell((short) 14).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)14).setCellValue(obj1[13].toString().equals("null") ? "":obj1[13].toString()); 
				                         }
			                         
			                         if((String.valueOf(obj1[14]).equals("null")) || (String.valueOf(obj1[14])==null)){
				                         	row.createCell((short) 15).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)15).setCellValue(obj1[14].toString().equals("null") ? "":obj1[14].toString()); 
				                         }
			                         
			                         
			                         if((String.valueOf(obj1[15]).equals("null")) || (String.valueOf(obj1[15])==null)){
				                         	row.createCell((short) 16).setCellValue(""); 
				                         }
				                         else{
				                         	row.createCell((short)16).setCellValue(obj1[15].toString().equals("null") ? "":obj1[15].toString()); 
				                         }
			                         
			                       //  row.createCell((short) 8).setCellValue( String.valueOf(obj1[7])==null ? "":String.valueOf(obj1[7]));
			                        // row.createCell((short) 9).setCellValue(String.valueOf(obj1[8])==null ? "":String.valueOf(obj1[8]));
			                        // row.createCell((short) 10).setCellValue(String.valueOf(obj1[9])==null ? "":String.valueOf(obj1[9]));
			                        // row.createCell((short) 11).setCellValue(String.valueOf(obj1[10])==null ? "":String.valueOf(obj1[10]));
			                        // row.createCell((short) 12).setCellValue(String.valueOf(obj1[11])==null ? "":String.valueOf(obj1[11]));
			                        // row.createCell((short) 13).setCellValue(String.valueOf(obj1[12])==null ? "":String.valueOf(obj1[12]));
			                         //row.createCell((short) 14).setCellValue(String.valueOf(obj1[13])==null ? "":String.valueOf(obj1[13]));
			                        // row.createCell((short) 15).setCellValue(String.valueOf(obj1[14])==null ? "":String.valueOf(obj1[14]));
			                         srno++;
			                         index++; 
								 }
								}
								FileOutputStream fileOut = new FileOutputStream(filePath);
								hwb.write(fileOut);
								fileOut.close();

								ModelAndView model = new ModelAndView("newRolePage");

								model.addObject( "excelFileDownLoadSuccessfully", "File DownLoaded Successfully in this location F:/ExcelReports/nbfcDANGFReportExcelFile.xls.");

								File downloadFile = new File(filePath);
								log.info("downloadFile =" + downloadFile);
								FileInputStream inputStream = new FileInputStream(downloadFile);

								response.setContentLength((int) downloadFile.length());

								// set headers for the response
								String headerKey = "Content-Disposition";
								String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
								response.setHeader(headerKey, headerValue);

								// get output stream of the response
								OutputStream outStream = response.getOutputStream();

								byte[] buffer = new byte[BUFFER_SIZE];
								int bytesRead = -1;

								// write bytes read from the input stream into the output stream
								while ((bytesRead = inputStream.read(buffer)) != -1) {
									outStream.write(buffer, 0, bytesRead);
								}
								inputStream.close();
								outStream.close();
								return model;
							} catch (Exception e) {
								e.printStackTrace();
								//log.info("Exception == " + e);
								//System.out.println("Exception == " + e);
							}
							return null;
						}
					 @RequestMapping(value = "/DANASFReportDetailDownload", method = RequestMethod.GET)
						public ModelAndView DANASFReportDetailDownload(
								@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
								HttpServletRequest request, HttpServletResponse response,
								HttpSession session) throws IOException {
							try {
		                        String ASFSummaryDetailsFileName1="DANASFFileName.xlsx";
								String filePath = downloadFileDirPath + File.separator + ASFSummaryDetailsFileName1;
								System.out.println(filePath);
								File file = new File(downloadFileDirPath);
								boolean isCreated = file.mkdir();
				                if (isCreated) {
									File file1 = new File(filePath);
									boolean isExists = file1.exists();
									if (isExists) {

									} else {
										file1.createNewFile();
									}

								}
				                
				                XSSFWorkbook hwb = new XSSFWorkbook();
								XSSFSheet sheet = hwb.createSheet("DANASFDownLoadedFile");
		                        CellStyle style = hwb.createCellStyle();
								Font font = hwb.createFont();
								font.setFontHeightInPoints((short) 11);
								font.setFontName(HSSFFont.FONT_ARIAL);
								font.setBoldweight(HSSFFont.COLOR_NORMAL);
								font.setBold(true);
								font.setColor(HSSFColor.DARK_BLUE.index);
								style.setFont(font);
								style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
								style.setFillPattern(CellStyle.SOLID_FOREGROUND);
								sheet.createFreezePane(0,1 ); // Freeze 1st Row
																// sheet.createFreezePane(int
																// colSplit, int rowSplit, int
																// leftmostColumn, int topRow)
		                    XSSFRow rowhead = sheet.createRow((short) 0);

								XSSFCell cell0 = rowhead.createCell(0);
								cell0.setCellStyle(style);
								cell0.setCellValue("SrNo");// Done 1

								XSSFCell cell1 = rowhead.createCell(1);
								cell1.setCellStyle(style);
								cell1.setCellValue("Demand Advice Number");// Done 3

								
								
								
								
								
								/*XSSFCell cell2 = rowhead.createCell(2);
								cell2.setCellStyle(style);
								cell2.setCellValue("Ssi Name"); */
								
								
							 

								XSSFCell cell2 = rowhead.createCell(2);
								cell2.setCellStyle(style);
								cell2.setCellValue("Member Id"); 
								
								XSSFCell cell3 = rowhead.createCell(3);
								cell3.setCellStyle(style);
								cell3.setCellValue("Generated On Date"); 

								XSSFCell cell4 = rowhead.createCell(4);
								cell4.setCellStyle(style);
								cell4.setCellValue("No Of Application");
								 
								XSSFCell cell5 = rowhead.createCell(5);
								cell5.setCellStyle(style);
								cell5.setCellValue("Base Amount"); 
								
								
								XSSFCell cell6 = rowhead.createCell(6);
								cell6.setCellStyle(style);
								cell6.setCellValue("Dan Amount"); 

								XSSFCell cell7 = rowhead.createCell(7);
								cell7.setCellStyle(style);
								cell7.setCellValue("IGST Amount"); 
								
								XSSFCell cell8 = rowhead.createCell(8);
								cell8.setCellStyle(style);
								cell8.setCellValue("CGST Amount"); 

								XSSFCell cell9 = rowhead.createCell(9);
								cell9.setCellStyle(style);
								cell9.setCellValue("SGST Amount"); 

								String Role= (String) session.getAttribute("Role"); 
								String userId=(String) session.getAttribute("userId"); 
								String fromDate=(String) session.getAttribute("fromDate");  
								String toDate= (String) session.getAttribute("toDate");  
								String Mem_Id= (String) session.getAttribute("Mem_Id"); 
								String ssiName= (String) session.getAttribute("ssiName"); 
							   //List<MliWiseReportDetailBean> report=statutsWiseReportService.getDanGfReportDowanload(Role, userId, fromDate, toDate, Mem_Id,ssiName);
								List<Object[]> report1=new ArrayList<Object[]>();
								
								report1=statutsWiseReportService.getDANASFDetails(Role, userId, fromDate, toDate, Mem_Id, ssiName);
								if(!report1.isEmpty()){ 
								int index = 1;
								int srno=1;
								for(Object[] obj1:report1){
									 XSSFRow row = sheet.createRow((short) index);	
									 
									  
									 
									 
									 
			                         row.createCell((short) 0).setCellValue(srno);
			                         row.createCell((short) 1).setCellValue(obj1[2].toString()==null ? "":obj1[2].toString());
			                       /*  row.createCell((short) 2).setCellValue( obj1[1].toString()==null ?"":obj1[1].toString());*/
			                         row.createCell((short) 2).setCellValue(obj1[0].toString()==null ? "":obj1[0].toString());
			                         row.createCell((short) 3).setCellValue(obj1[3].toString()==null ? "":obj1[3].toString());
			                         row.createCell((short) 4).setCellValue(obj1[4].toString()==null ? "":obj1[4].toString());
			                         row.createCell((short) 5).setCellValue(obj1[5].toString()==null ? "":obj1[5].toString());
			                         row.createCell((short) 6).setCellValue(obj1[6].toString()==null ? "":obj1[6].toString());
			                         row.createCell((short) 7).setCellValue(obj1[7].toString()==null ? "":obj1[7].toString());
			                         row.createCell((short) 8).setCellValue(obj1[8].toString()==null ? "":obj1[8].toString());
			                         row.createCell((short) 9).setCellValue(obj1[9].toString()==null ? "":obj1[9].toString());
			                         srno++;
			                         index++; 
								 }
								}
								FileOutputStream fileOut = new FileOutputStream(filePath);
								hwb.write(fileOut);
								fileOut.close();

								ModelAndView model = new ModelAndView("newRolePage");

								model.addObject( "excelFileDownLoadSuccessfully", "File DownLoaded Successfully in this location F:/ExcelReports/nbfcDANGFReportExcelFile.xls.");

								File downloadFile = new File(filePath);
								log.info("downloadFile =" + downloadFile);
								FileInputStream inputStream = new FileInputStream(downloadFile);

								response.setContentLength((int) downloadFile.length());

								// set headers for the response
								String headerKey = "Content-Disposition";
								String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
								response.setHeader(headerKey, headerValue);

								// get output stream of the response
								OutputStream outStream = response.getOutputStream();

								byte[] buffer = new byte[BUFFER_SIZE];
								int bytesRead = -1;

								// write bytes read from the input stream into the output stream
								while ((bytesRead = inputStream.read(buffer)) != -1) {
									outStream.write(buffer, 0, bytesRead);
								}
								inputStream.close();
								outStream.close();
								return model;
							} catch (Exception e) {
								log.info("Exception == " + e);
								System.out.println("Exception == " + e);
							}
							return null;
						}
					 
					///List of Mlis Report
						//this method call for DANReportGF Report Detail Page
							@RequestMapping(value = "/ListofMLIsReport", method = RequestMethod.GET)
							public ModelAndView ListofMLIsReport(
									@ModelAttribute("command") NPADetailsBean bean,
									BindingResult result, HttpSession session, Model model)
									throws JRException {
								Map<String, Object> model1 = new HashMap<String, Object>();
								String Role = (String) session.getAttribute("uRole");
								 //userId = (String) session.getAttribute("userId");
								//String loginUserMemId = npaService.getMemberId(userId);
								// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
								if (Role.equals("CMAKER")) {
									model1.put("guaranteelist", userActivityService.getActivity(
											"CGTMSEMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"CGTMSEMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"CGTMSEMAKER", "Receipt_Payments"));
									model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
									model1.put("repList",
											userActivityService.getReport("CGTMSEMAKER", "User_Report"));
									model1.put("stateList", prepareStateListofBean(stateService.listStates("CCA")));
									// model1.put("actNameHome",
									// userActivityService.getActivityName("CGTMSEMAKER",
									// "cgpanDetailReport"));// Added by Say 31 Jan19
									//model1.put("MLIID", loginUserMemId);
									model1.put("homePage", "cgtmseMakerHome");

								} else if (Role.equals("CCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity(
											"CGTMSECHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity(
											"CGTMSECHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"CGTMSECHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"CGTMSECHECKER", "Receipt_Payments"));
									model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
									model1.put("repList", userActivityService.getReport(
											"CGTMSECHECKER", "User_Report"));
									//model1.put("MLIID", loginUserMemId);
									model1.put("homePage", "cgtmseCheckerHome");
									// return null;
								} else if (Role.equals("NMAKER")) {
									// added by say 6 feb-----------------------
									model1.put("adminlist", userActivityService.getActivity(
											"NBFCMAKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity(
											"NBFCMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"NBFCMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"NBFCMAKER", "Receipt_Payments"));
									model1.put("repList",
											userActivityService.getReport("NBFCMAKER", "User_Report"));
									model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
									//model1.put("MLIID", loginUserMemId);
									model1.put("homePage", "nbfcMakerHome");

								} else if (Role.equals("NCHECKER")) {
									model1.put("adminlist", userActivityService.getActivity(
											"NBFCCHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity(
											"NBFCCHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"NBFCCHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"NBFCCHECKER", "Receipt_Payments"));
									model1.put("repList",
											userActivityService.getReport("NBFCCHECKER", "User_Report"));
									model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
									//model1.put("MLIID", loginUserMemId);
									model1.put("homePage", "nbfcCheckerHome");

								}
								return new ModelAndView("ListOfMLIsReportInputForm", model1);
								// return null;
							} 
							
							
							@RequestMapping(value = "/ListofMLIsReportData", method = RequestMethod.POST)
							public ModelAndView ListofMLIsReportData(
									@ModelAttribute("command") NPADetailsBean bean,
									BindingResult result, HttpSession session, Model model)
									throws JRException, ParseException {
								Map<String, Object> model1 = new HashMap<String, Object>();
								List<NPADetailsBean> NPADetailList=null;
								 String Role = (String) session.getAttribute("uRole");
								//String userId = (String) session.getAttribute("userId");
								//memberId = npaService.getMemberId(userId);
								String mliLongName=bean.getMliLongName();
								 
								System.out.println("mliLongName==194==="+mliLongName);
								validator.MliLIstReportDetailsValidate(bean, result);
								if (result.hasErrors()) {
									if (Role.equals("CMAKER")) {
										// userId = "ADMIN";
										model1.put("guaranteelist", userActivityService.getActivity(
												"CGTMSEMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"CGTMSEMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"CGTMSEMAKER", "Receipt_Payments"));
										model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
										model1.put("repList", userActivityService.getReport(
												"CGTMSEMAKER", "User_Report"));
										model1.put("homePage", "cgtmseMakerHome");

									} else if (Role.equals("CCHECKER")) {
										// userId = "ADMIN";
										model1.put("adminlist", userActivityService.getActivity(
												"CGTMSECHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity(
												"CGTMSECHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"CGTMSECHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"CGTMSECHECKER", "Receipt_Payments"));
										model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
										model1.put("repList", userActivityService.getReport(
												"CGTMSECHECKER", "User_Report"));
										model1.put("homePage", "cgtmseCheckerHome");
										// return null;
									} else if (Role.equals("NMAKER")) {
										model1.put("adminlist", userActivityService.getActivity(
												"NBFCMAKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity(
												"NBFCMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"NBFCMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"NBFCMAKER", "Receipt_Payments"));
										model1.put("repList", userActivityService.getReport(
												"NBFCMAKER", "User_Report"));
										model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
										model1.put("homePage", "nbfcMakerHome");

									} else if (Role.equals("NCHECKER")) {
										model1.put("adminlist", userActivityService.getActivity(
												"NBFCCHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity(
												"NBFCCHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"NBFCCHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"NBFCCHECKER", "Receipt_Payments"));
										model1.put("repList", userActivityService.getReport(
												"NBFCCHECKER", "User_Report"));
										 model1.put("homePage", "nbfcCheckerHome");

									}
									return new ModelAndView("ListOfMLIsReportInputForm", model1);
								}

								if (Role.equals("CMAKER")) {
									userId = "ADMIN";
									model1.put("guaranteelist", userActivityService.getActivity(
											"CGTMSEMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"CGTMSEMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"CGTMSEMAKER", "Receipt_Payments"));

									model1.put("repList",
											userActivityService.getReport("CGTMSEMAKER", "User_Report"));
									model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
									model1.put("homePage", "cgtmseMakerHome");

								} else if (Role.equals("CCHECKER")) {
									userId = "ADMIN";
									model1.put("adminlist", userActivityService.getActivity(
											"CGTMSECHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity(
											"CGTMSECHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"CGTMSECHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"CGTMSECHECKER", "Receipt_Payments"));

									model1.put("repList", userActivityService.getReport(
											"CGTMSECHECKER", "User_Report"));
									model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
									model1.put("homePage", "cgtmseCheckerHome");
									// return null;
								} else if (Role.equals("NMAKER")) {
									userId = (String) session.getAttribute("userId");
									model1.put("adminlist", userActivityService.getActivity(
											"NBFCMAKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity(
											"NBFCMAKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"NBFCMAKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"NBFCMAKER", "Receipt_Payments"));
									model1.put("repList",
											userActivityService.getReport("NBFCMAKER", "User_Report"));
									model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcMakerHome");

								} else if (Role.equals("NCHECKER")) {
									userId = (String) session.getAttribute("userId");
									model1.put("adminlist", userActivityService.getActivity(
											"NBFCCHECKER", "System_Admin"));
									model1.put("guaranteelist", userActivityService.getActivity(
											"NBFCCHECKER", "Registration"));
									model1.put("applicationList", userActivityService.getActivity(
											"NBFCCHECKER", "Application_Processing"));
									model1.put("RPaymentList", userActivityService.getActivity(
											"NBFCCHECKER", "Receipt_Payments"));
									model1.put("repList",
											userActivityService.getReport("NBFCCHECKER", "User_Report"));
									model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
									model1.put("homePage", "nbfcCheckerHome");

								}
								String Mem_Id12 ="";
								if(!mliLongName.equals("ALL")){
								mem_id = userActivityService.getBankID(mliLongName);
								Mem_Id12= mem_id.getBnkId() + mem_id.getZneID() + mem_id.getBrnName();
								}
								else{
									Mem_Id12="ALL";	
								}
								List<Object[]> rows1=null;
							 session.setAttribute("Mem_Id12", Mem_Id12);
							 if(Role.equals("CCHECKER") || Role.equals("CMAKER")){
									rows1=statutsWiseReportService.getListOfMlis(Mem_Id12);
									//session.setAttribute("Mem_Id12", Mem_Id12);  
								}else{
									rows1=statutsWiseReportService.getListOfMlis(Mem_Id12);
									//session.setAttribute("rows1", rows1);   
								 }
								if (!rows1.isEmpty()) {
									model1.put("rows1", rows1);
								 } else {
									model1.put("message", "NO Data Found !!");
								} 
								return new ModelAndView("ListOfMLIsReportInputForm", model1);
							} 
							
							
							
							
							 @RequestMapping(value = "/ListofMLIsReportDataDownload", method = RequestMethod.GET)
								public ModelAndView ListofMLIsReportDataDownload(
										@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
										HttpServletRequest request, HttpServletResponse response,
										HttpSession session) throws IOException {
									try {
				                        String ASFSummaryDetailsFileName1="ListOfMlisName.xlsx";
										String filePath = downloadFileDirPath + File.separator + ASFSummaryDetailsFileName1;
										System.out.println(filePath);
										File file = new File(downloadFileDirPath);
										boolean isCreated = file.mkdir();
						                if (isCreated) {
											File file1 = new File(filePath);
											boolean isExists = file1.exists();
											if (isExists) {

											} else {
												file1.createNewFile();
											}

										}
						                
						                XSSFWorkbook hwb = new XSSFWorkbook();
										XSSFSheet sheet = hwb.createSheet("ListOfMlisNameDownLoadedFile");
				                        CellStyle style = hwb.createCellStyle();
										Font font = hwb.createFont();
										font.setFontHeightInPoints((short) 11);
										font.setFontName(HSSFFont.FONT_ARIAL);
										font.setBoldweight(HSSFFont.COLOR_NORMAL);
										font.setBold(true);
										font.setColor(HSSFColor.DARK_BLUE.index);
										style.setFont(font);
										style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
										style.setFillPattern(CellStyle.SOLID_FOREGROUND);
										sheet.createFreezePane(0,1 ); // Freeze 1st Row
																		// sheet.createFreezePane(int
																		// colSplit, int rowSplit, int
																		// leftmostColumn, int topRow)
				                    XSSFRow rowhead = sheet.createRow((short) 0);

										XSSFCell cell0 = rowhead.createCell(0);
										cell0.setCellStyle(style);
										cell0.setCellValue("SrNo");// Done 1

										XSSFCell cell1 = rowhead.createCell(1);
										cell1.setCellStyle(style);
										cell1.setCellValue("MLI NAME");// Done 3

										XSSFCell cell2 = rowhead.createCell(2);
										cell2.setCellStyle(style);
										cell2.setCellValue("OPERATING OFFICES"); 

										 
										List<Object[]> report1=new ArrayList<Object[]>();
										String Mem_Id12= (String) session.getAttribute("Mem_Id12");
										session.setAttribute("Mem_Id12", Mem_Id12); 
										report1= statutsWiseReportService.getListOfMlis(Mem_Id12);
										if(!report1.isEmpty()){ 
										int index = 1;
										int srno=1;
										for(Object[] obj1:report1){
											 XSSFRow row = sheet.createRow((short) index);	
					                         row.createCell((short) 0).setCellValue(srno);
					                         row.createCell((short) 1).setCellValue(obj1[0].toString()==null ? "":obj1[0].toString());
					                         row.createCell((short) 2).setCellValue( obj1[1].toString()==null ?"":obj1[1].toString());
					                         srno++;
					                         index++; 
										 }
										}
										FileOutputStream fileOut = new FileOutputStream(filePath);
										hwb.write(fileOut);
										fileOut.close();

										ModelAndView model = new ModelAndView("newRolePage");

										model.addObject( "excelFileDownLoadSuccessfully", "File DownLoaded Successfully in this location F:/ExcelReports/nbfcDANGFReportExcelFile.xls.");

										File downloadFile = new File(filePath);
										log.info("downloadFile =" + downloadFile);
										FileInputStream inputStream = new FileInputStream(downloadFile);

										response.setContentLength((int) downloadFile.length());

										// set headers for the response
										String headerKey = "Content-Disposition";
										String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
										response.setHeader(headerKey, headerValue);

										// get output stream of the response
										OutputStream outStream = response.getOutputStream();

										byte[] buffer = new byte[BUFFER_SIZE];
										int bytesRead = -1;

										// write bytes read from the input stream into the output stream
										while ((bytesRead = inputStream.read(buffer)) != -1) {
											outStream.write(buffer, 0, bytesRead);
										}
										inputStream.close();
										outStream.close();
										return model;
									} catch (Exception e) {
										log.info("Exception == " + e);
										System.out.println("Exception == " + e);
									}
									return null;
								}
							 
							
							 
							 @RequestMapping(value = "/GetMlisDetelsDownload", method = RequestMethod.GET)
								public ModelAndView GetMlisDetelsDownload(
										@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
										HttpServletRequest request, HttpServletResponse response,
										HttpSession session) throws IOException {
									try {
				                        String ASFSummaryDetailsFileName1="GetMlisDetelsDownload.xlsx";
										String filePath = downloadFileDirPath + File.separator + ASFSummaryDetailsFileName1;
										System.out.println(filePath);
										File file = new File(downloadFileDirPath);
										boolean isCreated = file.mkdir();
						                if (isCreated) {
											File file1 = new File(filePath);
											boolean isExists = file1.exists();
											if (isExists) {

											} else {
												file1.createNewFile();
											}

										}
						                
						                XSSFWorkbook hwb = new XSSFWorkbook();
										XSSFSheet sheet = hwb.createSheet("GetMlisDetelsDownload");
				                        CellStyle style = hwb.createCellStyle();
										Font font = hwb.createFont();
										font.setFontHeightInPoints((short) 11);
										font.setFontName(HSSFFont.FONT_ARIAL);
										font.setBoldweight(HSSFFont.COLOR_NORMAL);
										font.setBold(true);
										font.setColor(HSSFColor.DARK_BLUE.index);
										style.setFont(font);
										style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
										style.setFillPattern(CellStyle.SOLID_FOREGROUND);
										sheet.createFreezePane(0,1 ); // Freeze 1st Row
																		// sheet.createFreezePane(int
																		// colSplit, int rowSplit, int
																		// leftmostColumn, int topRow)
				                    XSSFRow rowhead = sheet.createRow((short) 0);

										XSSFCell cell0 = rowhead.createCell(0);
										cell0.setCellStyle(style);
										cell0.setCellValue("SrNo");// Done 1

										XSSFCell cell1 = rowhead.createCell(1);
										cell1.setCellStyle(style);
										cell1.setCellValue("OPERATING OFFICES");// Done 3

										XSSFCell cell2 = rowhead.createCell(2);
										cell2.setCellStyle(style);
										cell2.setCellValue("ADDRESS"); 

										XSSFCell cell3 = rowhead.createCell(3);
										cell3.setCellStyle(style);
										cell3.setCellValue("Phone No"); 
										
										List<Object[]> report1=new ArrayList<Object[]>();
										String bankMemberId=(String) session.getAttribute("bankMemberId");
										report1 = statutsWiseReportService.getMLISDetails(bankMemberId);
										if(!report1.isEmpty()){ 
										int index = 1;
										int srno=1;
										for(Object[] obj1:report1){
											 XSSFRow row = sheet.createRow((short) index);	
					                         row.createCell((short) 0).setCellValue(srno);
					                         row.createCell((short) 1).setCellValue(obj1[2].toString()==null ? "":obj1[2].toString());
					                         row.createCell((short) 2).setCellValue( obj1[0].toString()==null ?"":obj1[0].toString());
					                         row.createCell((short) 3).setCellValue(obj1[1].toString()==null ? "":obj1[1].toString());
					                         srno++;
					                         index++; 
										 }
										}
										FileOutputStream fileOut = new FileOutputStream(filePath);
										hwb.write(fileOut);
										fileOut.close();

										ModelAndView model = new ModelAndView("newRolePage");

										model.addObject( "excelFileDownLoadSuccessfully", "File DownLoaded Successfully in this location F:/ExcelReports/nbfcDANGFReportExcelFile.xls.");

										File downloadFile = new File(filePath);
										log.info("downloadFile =" + downloadFile);
										FileInputStream inputStream = new FileInputStream(downloadFile);

										response.setContentLength((int) downloadFile.length());

										// set headers for the response
										String headerKey = "Content-Disposition";
										String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
										response.setHeader(headerKey, headerValue);

										// get output stream of the response
										OutputStream outStream = response.getOutputStream();

										byte[] buffer = new byte[BUFFER_SIZE];
										int bytesRead = -1;

										// write bytes read from the input stream into the output stream
										while ((bytesRead = inputStream.read(buffer)) != -1) {
											outStream.write(buffer, 0, bytesRead);
										}
										inputStream.close();
										outStream.close();
										return model;
									} catch (Exception e) {
										log.info("Exception == " + e);
										System.out.println("Exception == " + e);
									}
									return null;
								}
							 	 
						
							 @RequestMapping(value = "/ListofMLIsReportDataBackButton", method = RequestMethod.POST)
								public ModelAndView ListofMLIsReportDataBackButton( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
										Model model,HttpServletRequest request) throws JRException, ParseException {
									        Map<String, Object> model1 = new HashMap<String, Object>();
									        String Role = (String) session.getAttribute("uRole");
									if (result.hasErrors()) {
										if (Role.equals("CMAKER")) {
											model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
							                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
									        model1.put("homePage", "cgtmseMakerHome");
							             } else if (Role.equals("CCHECKER")) {
											model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
											model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
											model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
											model1.put("homePage", "cgtmseCheckerHome");
										 } else if (Role.equals("NMAKER")) {
											model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
											model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
											model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
											model1.put("homePage", "nbfcMakerHome");

										} else if (Role.equals("NCHECKER")) {
											model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
											model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
											model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
											model1.put("homePage", "nbfcCheckerHome");

										}
										return new ModelAndView("ListOfMLIsReportInputForm", model1);
									}

									if (Role.equals("CMAKER")) {
										userId = "ADMIN";
										model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
							            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
										model1.put("homePage", "cgtmseMakerHome");

									} else if (Role.equals("CCHECKER")) {
										userId = "ADMIN";
										model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
										model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
										model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
										model1.put("homePage", "cgtmseCheckerHome");
									 } else if (Role.equals("NMAKER")) {
										userId = (String) session.getAttribute("userId");
										model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
										model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
										model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
										model1.put("homePage", "nbfcMakerHome");

									} else if (Role.equals("NCHECKER")) {
										userId = (String) session.getAttribute("userId");
										model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
										model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
										model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
										model1.put("homePage", "nbfcCheckerHome");

									}
									String Mem_Id12=(String) session.getAttribute("Mem_Id12");
									List<Object[]> rows1=null;
									rows1=statutsWiseReportService.getListOfMlis(Mem_Id12);
									   if (!rows1.isEmpty()) {
											model1.put("rows1", rows1);
										} else {
											model1.put("message", "NO Data Found !!");
										}

									return new ModelAndView("ListOfMLIsReportInputForm", model1);
									// return null;
								}
							 
							 
							 @RequestMapping(value = "/MLIListsbackButton", method = RequestMethod.POST)
								public ModelAndView MLIListsbackButton( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
										Model model,HttpServletRequest request) throws JRException, ParseException {
									        Map<String, Object> model1 = new HashMap<String, Object>();
									        String Role = (String) session.getAttribute("uRole");
									if (result.hasErrors()) {
										if (Role.equals("CMAKER")) {
											model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
							                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
									        model1.put("homePage", "cgtmseMakerHome");
							             } else if (Role.equals("CCHECKER")) {
											model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
											model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
											model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
											model1.put("homePage", "cgtmseCheckerHome");
										 } else if (Role.equals("NMAKER")) {
											model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
											model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
											model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
											model1.put("homePage", "nbfcMakerHome");

										} else if (Role.equals("NCHECKER")) {
											model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
											model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
											model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
											model1.put("homePage", "nbfcCheckerHome");

										}
										return new ModelAndView("ListOfMLIsReportInputForm", model1);
									}

									if (Role.equals("CMAKER")) {
										userId = "ADMIN";
										model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
							            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
										model1.put("homePage", "cgtmseMakerHome");

									} else if (Role.equals("CCHECKER")) {
										userId = "ADMIN";
										model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
										model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
										model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
										model1.put("homePage", "cgtmseCheckerHome");
									 } else if (Role.equals("NMAKER")) {
										userId = (String) session.getAttribute("userId");
										model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
										model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
										model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
										model1.put("homePage", "nbfcMakerHome");

									} else if (Role.equals("NCHECKER")) {
										userId = (String) session.getAttribute("userId");
										model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
										model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
										model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
										model1.put("homePage", "nbfcCheckerHome");

									}
									 
		                           return new ModelAndView("ListOfMLIsReportInputForm", model1);
									// return null;
								}
							///Women Entrepreneur Report
							 
							 @RequestMapping(value = "/WomenEntrepreneurReport", method = RequestMethod.GET)
								public ModelAndView WomenEntrepreneurReport(
										@ModelAttribute("command") NPADetailsBean bean,
										BindingResult result, HttpSession session, Model model)
										throws JRException {
									Map<String, Object> model1 = new HashMap<String, Object>();
									String Role = (String) session.getAttribute("uRole");
									 //userId = (String) session.getAttribute("userId");
									//String loginUserMemId = npaService.getMemberId(userId);
									// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
									if (Role.equals("CMAKER")) {
										model1.put("guaranteelist", userActivityService.getActivity(
												"CGTMSEMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"CGTMSEMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"CGTMSEMAKER", "Receipt_Payments"));
										model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
										model1.put("repList",
												userActivityService.getReport("CGTMSEMAKER", "User_Report"));
										model1.put("stateList", prepareStateListofBean(stateService.listStates("CCA")));
										// model1.put("actNameHome",
										// userActivityService.getActivityName("CGTMSEMAKER",
										// "cgpanDetailReport"));// Added by Say 31 Jan19
										//model1.put("MLIID", loginUserMemId);
										model1.put("homePage", "cgtmseMakerHome");

									} else if (Role.equals("CCHECKER")) {
										model1.put("adminlist", userActivityService.getActivity(
												"CGTMSECHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity(
												"CGTMSECHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"CGTMSECHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"CGTMSECHECKER", "Receipt_Payments"));
										model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
										model1.put("repList", userActivityService.getReport(
												"CGTMSECHECKER", "User_Report"));
										//model1.put("MLIID", loginUserMemId);
										model1.put("homePage", "cgtmseCheckerHome");
										// return null;
									} else if (Role.equals("NMAKER")) {
										// added by say 6 feb-----------------------
										model1.put("adminlist", userActivityService.getActivity(
												"NBFCMAKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity(
												"NBFCMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"NBFCMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"NBFCMAKER", "Receipt_Payments"));
										model1.put("repList",
												userActivityService.getReport("NBFCMAKER", "User_Report"));
										model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
										//model1.put("MLIID", loginUserMemId);
										model1.put("homePage", "nbfcMakerHome");

									} else if (Role.equals("NCHECKER")) {
										model1.put("adminlist", userActivityService.getActivity(
												"NBFCCHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity(
												"NBFCCHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"NBFCCHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"NBFCCHECKER", "Receipt_Payments"));
										model1.put("repList",
												userActivityService.getReport("NBFCCHECKER", "User_Report"));
										model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
										//model1.put("MLIID", loginUserMemId);
										model1.put("homePage", "nbfcCheckerHome");

									}
									return new ModelAndView("WomenEntrepreneurReportInput", model1);
									// return null;
								}
							 @RequestMapping(value = "/WomenEntrepreneurReportList", method = RequestMethod.POST)
								public ModelAndView WomenEntrepreneurReportList(
										@ModelAttribute("command") NPADetailsBean bean,
										BindingResult result, HttpSession session, Model model)
										throws JRException, ParseException {
									Map<String, Object> model1 = new HashMap<String, Object>();
									List<NPADetailsBean> NPADetailList=null;
									String Role = (String) session.getAttribute("uRole");
									String userId = (String) session.getAttribute("userId");
									//memberId = npaService.getMemberId(userId);
									String mliLongName=bean.getMliLongName();
									System.out.println("mliLongName==194==="+mliLongName);
									validator.WomenEntrepreneurValidate(bean, result);
									if (result.hasErrors()) {
										if (Role.equals("CMAKER")) {
											// userId = "ADMIN";
											model1.put("guaranteelist", userActivityService.getActivity(
													"CGTMSEMAKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity(
													"CGTMSEMAKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity(
													"CGTMSEMAKER", "Receipt_Payments"));
											model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
											model1.put("repList", userActivityService.getReport(
													"CGTMSEMAKER", "User_Report"));
											model1.put("homePage", "cgtmseMakerHome");

										} else if (Role.equals("CCHECKER")) {
											// userId = "ADMIN";
											model1.put("adminlist", userActivityService.getActivity(
													"CGTMSECHECKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity(
													"CGTMSECHECKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity(
													"CGTMSECHECKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity(
													"CGTMSECHECKER", "Receipt_Payments"));
											model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
											model1.put("repList", userActivityService.getReport(
													"CGTMSECHECKER", "User_Report"));
											model1.put("homePage", "cgtmseCheckerHome");
											// return null;
										} else if (Role.equals("NMAKER")) {
											model1.put("adminlist", userActivityService.getActivity(
													"NBFCMAKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity(
													"NBFCMAKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity(
													"NBFCMAKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity(
													"NBFCMAKER", "Receipt_Payments"));
											model1.put("repList", userActivityService.getReport(
													"NBFCMAKER", "User_Report"));
											model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
											model1.put("homePage", "nbfcMakerHome");

										} else if (Role.equals("NCHECKER")) {
											model1.put("adminlist", userActivityService.getActivity(
													"NBFCCHECKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity(
													"NBFCCHECKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity(
													"NBFCCHECKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity(
													"NBFCCHECKER", "Receipt_Payments"));
											model1.put("repList", userActivityService.getReport(
													"NBFCCHECKER", "User_Report"));
											 model1.put("homePage", "nbfcCheckerHome");

										}
										return new ModelAndView("WomenEntrepreneurReportInput", model1);
									}

									if (Role.equals("CMAKER")) {
										userId = "ADMIN";
										model1.put("guaranteelist", userActivityService.getActivity(
												"CGTMSEMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"CGTMSEMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"CGTMSEMAKER", "Receipt_Payments"));

										model1.put("repList",
												userActivityService.getReport("CGTMSEMAKER", "User_Report"));
										model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
										model1.put("homePage", "cgtmseMakerHome");

									} else if (Role.equals("CCHECKER")) {
										userId = "ADMIN";
										model1.put("adminlist", userActivityService.getActivity(
												"CGTMSECHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity(
												"CGTMSECHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"CGTMSECHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"CGTMSECHECKER", "Receipt_Payments"));

										model1.put("repList", userActivityService.getReport(
												"CGTMSECHECKER", "User_Report"));
										model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
										model1.put("homePage", "cgtmseCheckerHome");
										// return null;
									} else if (Role.equals("NMAKER")) {
										userId = (String) session.getAttribute("userId");
										model1.put("adminlist", userActivityService.getActivity(
												"NBFCMAKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity(
												"NBFCMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"NBFCMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"NBFCMAKER", "Receipt_Payments"));
										model1.put("repList",
												userActivityService.getReport("NBFCMAKER", "User_Report"));
										model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
										model1.put("homePage", "nbfcMakerHome");

									} else if (Role.equals("NCHECKER")) {
										userId = (String) session.getAttribute("userId");
										model1.put("adminlist", userActivityService.getActivity(
												"NBFCCHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity(
												"NBFCCHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"NBFCCHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"NBFCCHECKER", "Receipt_Payments"));
										model1.put("repList",
												userActivityService.getReport("NBFCCHECKER", "User_Report"));
										model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
										model1.put("homePage", "nbfcCheckerHome");

									}
									String Mem_Id ="";
									if(!mliLongName.equals("ALL")){
									mem_id = userActivityService.getBankID(mliLongName);
									Mem_Id= mem_id.getBnkId() + mem_id.getZneID() + mem_id.getBrnName();
									}
									else{
										Mem_Id="ALL";	
									}
									List<Object[]> rows1=null;
									//Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(bean.getToDate());
									//Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean .getFromDate());
									String toDate= DateFormate.dateformate(bean.getToDate());
									String fromDate=DateFormate.dateformate(bean .getFromDate());
									String state=statutsWiseReportService.getsateNme(bean.getState());
									session.setAttribute("Role", Role); 
									//session.setAttribute("userId", userId); 
									session.setAttribute("fromDate", fromDate); 
									session.setAttribute("toDate", toDate); 
									session.setAttribute("Mem_Id", Mem_Id); 
									session.setAttribute("state", state); 
									session.setAttribute("distict", bean.getDistict());
									//rows= statutsWiseReportService.getWomenEntrepreneurReportDetails(Role, userId, fromDate, toDate, state, bean.getDistict(), Mem_Id);
										rows1=statutsWiseReportService.getWomenEntrepreneurDetailList(Role, userId, toDate, fromDate, state, bean.getDistict(),Mem_Id);
										// rows = statutsWiseReportService.getDanASFDetail(Role, userId, fromDate, toDate, Mem_Id,ssiName);
									 
									if (!rows1.isEmpty()) {
										model1.put("rows1", rows1);
									 } else {
										model1.put("message", "NO Data Found !!");
									} 
									return new ModelAndView("WomenEntrepreneurReportInput", model1);
								}  
							 
							 
							 @RequestMapping(value = "/WomenEntrepreneurReportDownload", method = RequestMethod.GET)
								public ModelAndView WomenEntrepreneurReportDownload(
										@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
										HttpServletRequest request, HttpServletResponse response,
										HttpSession session) throws IOException {
									try {
				                        String ASFSummaryDetailsFileName1="WomenEntrepreneur.xlsx";
										String filePath = downloadFileDirPath + File.separator + ASFSummaryDetailsFileName1;
										System.out.println(filePath);
										File file = new File(downloadFileDirPath);
										boolean isCreated = file.mkdir();
						                if (isCreated) {
											File file1 = new File(filePath);
											boolean isExists = file1.exists();
											if (isExists) {

											} else {
												file1.createNewFile();
											}

										}
						                
						                XSSFWorkbook hwb = new XSSFWorkbook();
										XSSFSheet sheet = hwb.createSheet("WomenEntrepreneur");
				                        CellStyle style = hwb.createCellStyle();
										Font font = hwb.createFont();
										font.setFontHeightInPoints((short) 11);
										font.setFontName(HSSFFont.FONT_ARIAL);
										font.setBoldweight(HSSFFont.COLOR_NORMAL);
										font.setBold(true);
										font.setColor(HSSFColor.DARK_BLUE.index);
										style.setFont(font);
										style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
										style.setFillPattern(CellStyle.SOLID_FOREGROUND);
										sheet.createFreezePane(0,1 ); // Freeze 1st Row
																		// sheet.createFreezePane(int
																		// colSplit, int rowSplit, int
																		// leftmostColumn, int topRow)
				                    XSSFRow rowhead = sheet.createRow((short) 0);

										XSSFCell cell0 = rowhead.createCell(0);
										cell0.setCellStyle(style);
										cell0.setCellValue("SrNo");// Done 1

										XSSFCell cell1 = rowhead.createCell(1);
										cell1.setCellStyle(style);
										cell1.setCellValue("STATE");// Done 3

										XSSFCell cell2 = rowhead.createCell(2);
										cell2.setCellStyle(style);
										cell2.setCellValue("DISTRICT"); 
										
										
										XSSFCell cell3 = rowhead.createCell(3);
										cell3.setCellStyle(style);
										cell3.setCellValue("NO OF GRAUNTEE COUNT");// Done 3

										XSSFCell cell4 = rowhead.createCell(4);
										cell4.setCellStyle(style);
										cell4.setCellValue("GRAUNTEE APPROVED AMOUNT(IN LAKH)"); 
										
										
										

										 

										String Role= (String) session.getAttribute("Role"); 
										String userId=(String) session.getAttribute("userId"); 
										String fromDate=(String) session.getAttribute("fromDate");  
										String toDate= (String) session.getAttribute("toDate"); 
										String state= (String) session.getAttribute("state");
										String dist= (String) session.getAttribute("distict");
										String Mem_Id= (String) session.getAttribute("Mem_Id"); 
										 
									   //List<MliWiseReportDetailBean> report=statutsWiseReportService.getDanGfReportDowanload(Role, userId, fromDate, toDate, Mem_Id,ssiName);
										List<Object[]> report1= statutsWiseReportService .getWomenEntrepreneurDetailList(Role, userId, toDate, fromDate, state, dist,Mem_Id);
										//public List<MliWiseReportDetailBean> getWomenEntrepreneurDetails
										//List<MliWiseReportDetailBean> report1=statutsWiseReportService.getWomenEntrepreneurDetails(Role, userId, fromDate, toDate,state,dist, Mem_Id);
										if(!report1.isEmpty()){ 
										int index = 1;
										int srno=1;
										/*for(MliWiseReportDetailBean obj1:report1){*/
										for(Object obj[]:report1){
											 XSSFRow row = sheet.createRow((short) index);	
											 row.createCell((short) 0).setCellValue(srno);
					                         row.createCell((short) 1).setCellValue(obj[0].toString()==null ? "":obj[0].toString());
					                         row.createCell((short) 2).setCellValue(obj[1].toString()==null ? "":obj[1].toString());
					                         row.createCell((short) 3).setCellValue(obj[2].toString()==null ? "":obj[2].toString());
					                         row.createCell((short) 4).setCellValue(obj[3].toString()==null ? "":obj[3].toString());
					                         srno++;
					                         index++; 
										 }
										}
										FileOutputStream fileOut = new FileOutputStream(filePath);
										hwb.write(fileOut);
										fileOut.close();

										ModelAndView model = new ModelAndView("newRolePage");

										model.addObject( "excelFileDownLoadSuccessfully", "File DownLoaded Successfully in this location F:/ExcelReports/nbfcDANGFReportExcelFile.xls.");

										File downloadFile = new File(filePath);
										log.info("downloadFile =" + downloadFile);
										FileInputStream inputStream = new FileInputStream(downloadFile);

										response.setContentLength((int) downloadFile.length());

										// set headers for the response
										String headerKey = "Content-Disposition";
										String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
										response.setHeader(headerKey, headerValue);

										// get output stream of the response
										OutputStream outStream = response.getOutputStream();

										byte[] buffer = new byte[BUFFER_SIZE];
										int bytesRead = -1;

										// write bytes read from the input stream into the output stream
										while ((bytesRead = inputStream.read(buffer)) != -1) {
											outStream.write(buffer, 0, bytesRead);
										}
										inputStream.close();
										outStream.close();
										return model;
									} catch (Exception e) {
										log.info("Exception == " + e);
										System.out.println("Exception == " + e);
									}
									return null;
								}
							 
							 
							 @RequestMapping(value = "/WomenEntrepreneurbackButton", method = RequestMethod.POST)
								public ModelAndView WomenEntrepreneurbackButton( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
										Model model,HttpServletRequest request) throws JRException, ParseException {
									        Map<String, Object> model1 = new HashMap<String, Object>();
									        String Role = (String) session.getAttribute("uRole");
									if (result.hasErrors()) {
										if (Role.equals("CMAKER")) {
											model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
							                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
									        model1.put("homePage", "cgtmseMakerHome");
							             } else if (Role.equals("CCHECKER")) {
											model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
											model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
											model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
											model1.put("homePage", "cgtmseCheckerHome");
										 } else if (Role.equals("NMAKER")) {
											model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
											model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
											model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
											model1.put("homePage", "nbfcMakerHome");

										} else if (Role.equals("NCHECKER")) {
											model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
											model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
											model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
											model1.put("homePage", "nbfcCheckerHome");

										}
										return new ModelAndView("WomenEntrepreneurReportInput", model1);
									}

									if (Role.equals("CMAKER")) {
										userId = "ADMIN";
										model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
							            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
										model1.put("homePage", "cgtmseMakerHome");

									} else if (Role.equals("CCHECKER")) {
										userId = "ADMIN";
										model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
										model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
										model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
										model1.put("homePage", "cgtmseCheckerHome");
									 } else if (Role.equals("NMAKER")) {
										userId = (String) session.getAttribute("userId");
										model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
										model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
										model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
										model1.put("homePage", "nbfcMakerHome");

									} else if (Role.equals("NCHECKER")) {
										userId = (String) session.getAttribute("userId");
										model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
										model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
										model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
										model1.put("homePage", "nbfcCheckerHome");

									}
									
									/*String Role1= (String) session.getAttribute("Role"); 
									String userId=(String) session.getAttribute("userId"); 
									Date fromDate=(Date) session.getAttribute("fromDate");  
									Date toDate= (Date) session.getAttribute("toDate"); 
									String state= (String) session.getAttribute("state");
									String dist= (String) session.getAttribute("distict");
									String Mem_Id= (String) session.getAttribute("Mem_Id"); 
									rows= statutsWiseReportService.getWomenEntrepreneurReportDetails(Role1, userId, fromDate, toDate, state, dist, Mem_Id);
										//rows1=statutsWiseReportService.getWomenEntrepreneurReportDetails(Role, userId, fromDate, toDate, bean.getState(), bean.getDistict(),Mem_Id);
										// rows = statutsWiseReportService.getDanASFDetail(Role, userId, fromDate, toDate, Mem_Id,ssiName);
									 
									if (!rows.isEmpty()) {
										model1.put("rows", rows);
									 } else {
										model1.put("message", "NO Data Found !!");
									} */
									return new ModelAndView("WomenEntrepreneurReportInput", model1);
									// return null;
								} 
							 	@ModelAttribute("stateName")
								public Map<String, String> getstateName(HttpSession session) {
									 Map<String, String> mapMliLongNameObj = new LinkedHashMap<String, String>();
									 mapMliLongNameObj.put("ALL", "All State");
									 String Role=(String) session.getAttribute("uRole");
									 String Userid= (String) session.getAttribute("userId");
									 if(Userid!=null) {
										 String loginUserMemId = npaService.getMemberId(Userid);
										 ArrayList<Object[]> result=new ArrayList<Object[]>();
										 result = (ArrayList<Object[]>) statutsWiseReportService.getStateName(Role,loginUserMemId);
										 for(Object[] obj:result){
											// String statename=obj[0];
											 mapMliLongNameObj.put(obj[0].toString(), obj[1].toString()) ;
										 }
									
										 
								}
									 return   mapMliLongNameObj;
							 	}
								
								/* @ModelAttribute("distictName")
								public Map<String, String> getdistictName(HttpSession session) {
									 Map<String, String> mapMliLongNameObj = new HashMap<String, String>();
									 String Role=(String) session.getAttribute("uRole");
									 String Userid= (String) session.getAttribute("userId");
									 String loginUserMemId = npaService.getMemberId(Userid);
									 ArrayList<Object[]> result=new ArrayList<Object[]>();
									 result = (ArrayList<Object[]>) statutsWiseReportService.getDistictName(Role,loginUserMemId);
									 if(!result.isEmpty()){
									 for(Object[] obj:result){
										 //String distname=obj[0];
										 mapMliLongNameObj.put(obj[0].toString().equals("null") ? "": obj[0].toString(), obj[1].toString().equals("null") ? "":obj[1].toString()) ;
									 }
									 }
									 return   mapMliLongNameObj;
								}  */
	 
							 @RequestMapping(value = "/GetMlisDetels", method = RequestMethod.GET)
								public ModelAndView GetMlisDetels(
										@ModelAttribute("command") NPADetailsBean bean,
										BindingResult result, HttpSession session, Model model,HttpServletRequest request)
										throws JRException, ParseException {
									Map<String, Object> model1 = new HashMap<String, Object>();
									String Role = (String) session.getAttribute("uRole");
									String bankMemberId=request.getParameter("bankMemberId");
									
									//session.setAttribute("rows2",null);
									//session.removeAttribute("rows2");
									
									if (result.hasErrors()) {
										if (Role.equals("CMAKER")) {
											// userId = "ADMIN";
											model1.put("guaranteelist", userActivityService.getActivity(
													"CGTMSEMAKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity(
													"CGTMSEMAKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity(
													"CGTMSEMAKER", "Receipt_Payments"));

											model1.put("repList", userActivityService.getReport(
													"CGTMSEMAKER", "User_Report"));
									
											// model1.put("actNameHome",
											// userActivityService.getActivityName("CGTMSEMAKER",
											// "cgpanDetailReport"));// Added by Say 31 Jan19
											model1.put("homePage", "cgtmseMakerHome");

										} else if (Role.equals("CCHECKER")) {
											// userId = "ADMIN";
											model1.put("adminlist", userActivityService.getActivity(
													"CGTMSECHECKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity(
													"CGTMSECHECKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity(
													"CGTMSECHECKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity(
													"CGTMSECHECKER", "Receipt_Payments"));
											model1.put("CList",
													userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
											model1.put("repList", userActivityService.getReport(
													"CGTMSECHECKER", "User_Report"));
											// model1.put("actNameHome",
											// userActivityService.getActivityName("CGTMSECHECKER",
											// "cgpanDetailReport"));// Added by Say 31 Jan19
											model1.put("homePage", "cgtmseCheckerHome");
											// return null;
										} else if (Role.equals("NMAKER")) {
											// added by say 6 feb-----------------------
											// userId = (String) session.getAttribute("userId");
											model1.put("adminlist", userActivityService.getActivity(
													"NBFCMAKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity(
													"NBFCMAKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity(
													"NBFCMAKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity(
													"NBFCMAKER", "Receipt_Payments"));
											model1.put("repList", userActivityService.getReport(
													"NBFCMAKER", "User_Report"));
											model1.put("CList",
													userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
											// model1.put("actNameHome",
											// userActivityService.getActivityName("NBFCMAKER",
											// "cgpanDetailReport"));// Added by Say 31 Jan19
											model1.put("homePage", "nbfcMakerHome");

										} else if (Role.equals("NCHECKER")) {
											// userId = (String) session.getAttribute("userId");
											model1.put("adminlist", userActivityService.getActivity(
													"NBFCCHECKER", "System_Admin"));
											model1.put("guaranteelist", userActivityService.getActivity(
													"NBFCCHECKER", "Registration"));
											model1.put("applicationList", userActivityService.getActivity(
													"NBFCCHECKER", "Application_Processing"));
											model1.put("RPaymentList", userActivityService.getActivity(
													"NBFCCHECKER", "Receipt_Payments"));
											model1.put("repList", userActivityService.getReport(
													"NBFCCHECKER", "User_Report"));
											model1.put("CList",
													userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
											// model1.put("actNameHome",
											// userActivityService.getActivityName("NBFCCHECKER",
											// "cgpanDetailReport"));// Added by Say 31 Jan19
											model1.put("homePage", "nbfcCheckerHome");

										}
										return new ModelAndView("ListOfMLIsReportInputForm", model1);
									}

									if (Role.equals("CMAKER")) {
										userId = "ADMIN";
										model1.put("guaranteelist", userActivityService.getActivity(
												"CGTMSEMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"CGTMSEMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"CGTMSEMAKER", "Receipt_Payments"));

										model1.put("repList",
												userActivityService.getReport("CGTMSEMAKER", "User_Report"));
										// model1.put("actNameHome",
										// userActivityService.getActivityName("CGTMSEMAKER",
										// "cgpanDetailReport"));// Added by Say 31 Jan19
										model1.put("homePage", "cgtmseMakerHome");

									} else if (Role.equals("CCHECKER")) {
										userId = "ADMIN";
										model1.put("adminlist", userActivityService.getActivity(
												"CGTMSECHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity(
												"CGTMSECHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"CGTMSECHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"CGTMSECHECKER", "Receipt_Payments"));
										model1.put("CList",
												userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
										model1.put("repList", userActivityService.getReport(
												"CGTMSECHECKER", "User_Report"));
										// model1.put("actNameHome",
										// userActivityService.getActivityName("CGTMSECHECKER",
										// "cgpanDetailReport"));// Added by Say 31 Jan19
										model1.put("homePage", "cgtmseCheckerHome");
										// return null;
									} else if (Role.equals("NMAKER")) {
										// added by say 6 feb-----------------------
										userId = (String) session.getAttribute("userId");
										model1.put("adminlist", userActivityService.getActivity(
												"NBFCMAKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity(
												"NBFCMAKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"NBFCMAKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"NBFCMAKER", "Receipt_Payments"));
										model1.put("repList",
												userActivityService.getReport("NBFCMAKER", "User_Report"));
										model1.put("CList",
												userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
										// model1.put("actNameHome",
										// userActivityService.getActivityName("NBFCMAKER",
										// "cgpanDetailReport"));// Added by Say 31 Jan19
										model1.put("homePage", "nbfcMakerHome");

									} else if (Role.equals("NCHECKER")) {
										userId = (String) session.getAttribute("userId");
										model1.put("adminlist", userActivityService.getActivity(
												"NBFCCHECKER", "System_Admin"));
										model1.put("guaranteelist", userActivityService.getActivity(
												"NBFCCHECKER", "Registration"));
										model1.put("applicationList", userActivityService.getActivity(
												"NBFCCHECKER", "Application_Processing"));
										model1.put("RPaymentList", userActivityService.getActivity(
												"NBFCCHECKER", "Receipt_Payments"));
										model1.put("repList",
												userActivityService.getReport("NBFCCHECKER", "User_Report"));
										model1.put("CList",
												userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
										// model1.put("actNameHome",
										// userActivityService.getActivityName("NBFCCHECKER",
										// "cgpanDetailReport"));// Added by Say 31 Jan19
										model1.put("homePage", "nbfcCheckerHome");

									}
								   List<Object[]> rows2=null;
								   session.setAttribute("bankMemberId", bankMemberId);
								   rows2 = statutsWiseReportService.getMLISDetails(bankMemberId);
								   if (!rows2.isEmpty()) {
									  // session.setAttribute("rows2", rows2);
									   model1.put("rows2", rows2);
										
									} else {
										model1.put("message", "NO Data Found !!");
									}
			                        return new ModelAndView("ListOfMLIsReportInputForm", model1);
									// return null;
								}	
							 
							 //ajext
							 @RequestMapping(value = "/getDistictName", method = RequestMethod.GET)
								public @ResponseBody
								String getDistictName(HttpServletRequest request, HttpSession session) {
								 TreeMap map =new TreeMap<String, String>();
								 map.put("ALL", "All Distict");
								 String state = request.getParameter("hoCd");
										///TreeMap<String, String> map = null;
										String json = null;
										 List<Object[]> rows2=null;
										 rows2 = statutsWiseReportService.getDistictName("AA","BB",state);
										 for(Object obj[]:rows2){
											map.put(obj[0].toString(), obj[1].toString()); 
										 }
										 Gson gson = new Gson();
										 json = gson.toJson(map);
										 return json;
									}
								


//// start DAN TAX(GST) Report For ASF 
	 @RequestMapping(value = "/DANTAXGSTReportForASFList", method = RequestMethod.POST)
		public ModelAndView DANTAXGSTReportForASFList(
				@ModelAttribute("command") NPADetailsBean bean,
				BindingResult result, HttpSession session, Model model)
				throws JRException, ParseException {
			Map<String, Object> model1 = new HashMap<String, Object>();
			String Role = (String) session.getAttribute("uRole");
			//String userId = (String) session.getAttribute("userId");
			//memberId = npaService.getMemberId(userId);
			String mliLongName=bean.getMliLongName();
			System.out.println("mliLongName==194==="+mliLongName);
			validator.DantextReportDetailsValidate(bean, result);
			if (result.hasErrors()) {
				if (Role.equals("CMAKER")) {
					// userId = "ADMIN";
					model1.put("guaranteelist", userActivityService.getActivity(
							"CGTMSEMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"CGTMSEMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"CGTMSEMAKER", "Receipt_Payments"));
					model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
					model1.put("repList", userActivityService.getReport(
							"CGTMSEMAKER", "User_Report"));
					model1.put("homePage", "cgtmseMakerHome");

				} else if (Role.equals("CCHECKER")) {
					// userId = "ADMIN";
					model1.put("adminlist", userActivityService.getActivity(
							"CGTMSECHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"CGTMSECHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"CGTMSECHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"CGTMSECHECKER", "Receipt_Payments"));
					model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
					model1.put("repList", userActivityService.getReport(
							"CGTMSECHECKER", "User_Report"));
					model1.put("homePage", "cgtmseCheckerHome");
					// return null;
				} else if (Role.equals("NMAKER")) {
					model1.put("adminlist", userActivityService.getActivity(
							"NBFCMAKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"NBFCMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"NBFCMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"NBFCMAKER", "Receipt_Payments"));
					model1.put("repList", userActivityService.getReport(
							"NBFCMAKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
					model1.put("homePage", "nbfcMakerHome");

				} else if (Role.equals("NCHECKER")) {
					model1.put("adminlist", userActivityService.getActivity(
							"NBFCCHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"NBFCCHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"NBFCCHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"NBFCCHECKER", "Receipt_Payments"));
					model1.put("repList", userActivityService.getReport(
							"NBFCCHECKER", "User_Report"));
					 model1.put("homePage", "nbfcCheckerHome");

				}
				return new ModelAndView("DANTAXGSTReportForASFInputForm", model1);
			}

			if (Role.equals("CMAKER")) {
				userId = "ADMIN";
				model1.put("guaranteelist", userActivityService.getActivity(
						"CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"CGTMSEMAKER", "Receipt_Payments"));

				model1.put("repList",
						userActivityService.getReport("CGTMSEMAKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
				model1.put("homePage", "cgtmseMakerHome");

			} else if (Role.equals("CCHECKER")) {
				userId = "ADMIN";
				model1.put("adminlist", userActivityService.getActivity(
						"CGTMSECHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"CGTMSECHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"CGTMSECHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"CGTMSECHECKER", "Receipt_Payments"));

				model1.put("repList", userActivityService.getReport(
						"CGTMSECHECKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
				model1.put("homePage", "cgtmseCheckerHome");
				// return null;
			} else if (Role.equals("NMAKER")) {
				userId = (String) session.getAttribute("userId");
				model1.put("adminlist", userActivityService.getActivity(
						"NBFCMAKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"NBFCMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"NBFCMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"NBFCMAKER", "Receipt_Payments"));
				model1.put("repList",
						userActivityService.getReport("NBFCMAKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcMakerHome");

			} else if (Role.equals("NCHECKER")) {
				userId = (String) session.getAttribute("userId");
				model1.put("adminlist", userActivityService.getActivity(
						"NBFCCHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"NBFCCHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"NBFCCHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"NBFCCHECKER", "Receipt_Payments"));
				model1.put("repList",
						userActivityService.getReport("NBFCCHECKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcCheckerHome");

			}
			/*String Mem_Id ="";
			if(!mliLongName.equals("ALL")){
			mem_id = userActivityService.getBankID(mliLongName);
			Mem_Id= mem_id.getBnkId() + mem_id.getZneID() + mem_id.getBrnName();
			}
			else{
				Mem_Id="ALL";	
			}*/
			List<Object[]> rows1=null;

			//Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(bean.getToDate());
			//Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean .getFromDate());
			
			String toDate= DateFormate.dateformate(bean.getToDate());
			String fromDate=DateFormate.dateformate(bean .getFromDate());
			
			//System.out.println(fromDate);
			session.setAttribute("fromDate", fromDate); 
			session.setAttribute("toDate", toDate); 
			if(Role.equals("CCHECKER") || Role.equals("CMAKER")){
				//rows=statutsWiseReportService.getDANTAXGSTFList(fromDate, toDate);
				rows1 = statutsWiseReportService.getDanTeaxtAsfGstReportList(fromDate, toDate);
			}else{
				//rows=statutsWiseReportService.getDANTAXGSTFList(fromDate, toDate);
				rows1 = statutsWiseReportService.getDanTeaxtAsfGstReportList( fromDate, toDate);
			 }
			if (!rows1.isEmpty()) {
				model1.put("rows1", rows1);
			 } else {
				model1.put("message", "NO Data Found !!");
			} 
			return new ModelAndView("DANTAXGSTReportForASFInputForm", model1);
		} 
	 
	 
	 @RequestMapping(value = "/DANTAXGSTReportForASFListDownload", method = RequestMethod.GET)
		public ModelAndView DANTAXGSTReportForASFListDownload(
				@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
				HttpServletRequest request, HttpServletResponse response,
				HttpSession session) throws IOException {
			try {
             String ASFSummaryDetailsFileName1="DANGstASFReport.xlsx";
				String filePath = downloadFileDirPath + File.separator + ASFSummaryDetailsFileName1;
				System.out.println(filePath);
				File file = new File(downloadFileDirPath);
				boolean isCreated = file.mkdir();
             if (isCreated) {
					File file1 = new File(filePath);
					boolean isExists = file1.exists();
					if (isExists) {

					} else {
						file1.createNewFile();
					}

				}
             
             XSSFWorkbook hwb = new XSSFWorkbook();
				XSSFSheet sheet = hwb.createSheet("DANGstAsfDownLoadedFile");
             CellStyle style = hwb.createCellStyle();
				Font font = hwb.createFont();
				font.setFontHeightInPoints((short) 11);
				font.setFontName(HSSFFont.FONT_ARIAL);
				font.setBoldweight(HSSFFont.COLOR_NORMAL);
				font.setBold(true);
				font.setColor(HSSFColor.DARK_BLUE.index);
				style.setFont(font);
				style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				sheet.createFreezePane(0,1 ); // Freeze 1st Row
												// sheet.createFreezePane(int
												// colSplit, int rowSplit, int
												// leftmostColumn, int topRow)
         XSSFRow rowhead = sheet.createRow((short) 0);
         
			
			 
				XSSFCell cell0 = rowhead.createCell(0);
				cell0.setCellStyle(style);
				cell0.setCellValue("SrNo");// Done 1
             
				XSSFCell cell1 = rowhead.createCell(1);
				cell1.setCellStyle(style);
				cell1.setCellValue("DAN_ID");// Done 3

				XSSFCell cell2 = rowhead.createCell(2);
				cell2.setCellStyle(style);
				cell2.setCellValue("MEM_BANK_NAME"); 

				XSSFCell cell3 = rowhead.createCell(3);
				cell3.setCellStyle(style);
				cell3.setCellValue("MEM_ZONE_NAME"); 
				
				XSSFCell cell4 = rowhead.createCell(4);
				cell4.setCellStyle(style);
				cell4.setCellValue("MLIID"); 

				
				XSSFCell cell5 = rowhead.createCell(5);
				cell5.setCellStyle(style);
				cell5.setCellValue("GST No"); 

				XSSFCell cell6 = rowhead.createCell(6);
				cell6.setCellStyle(style);
				cell6.setCellValue("Portfolio Number"); 
				 
				
				XSSFCell cell7 = rowhead.createCell(7);
				cell7.setCellStyle(style);
				cell7.setCellValue("TAX_INV_ID"); 
				
				 
				XSSFCell cell8 = rowhead.createCell(8);
				cell8.setCellStyle(style);
				cell8.setCellValue("DANAMT"); 
				
				XSSFCell cell9 = rowhead.createCell(9);
				cell9.setCellStyle(style);
				cell9.setCellValue("BASEAMT"); 
				
				 

				XSSFCell cell10 = rowhead.createCell(10);
				cell10.setCellStyle(style);
				cell10.setCellValue("IGST_AMT"); 
				
				
				
				XSSFCell cell11 = rowhead.createCell(11);
				cell11.setCellStyle(style);
				cell11.setCellValue("IGST_RATE"); 
				
				XSSFCell cell12 = rowhead.createCell(12);
				cell12.setCellStyle(style);
				cell12.setCellValue("CGST_AMT"); 
				
				 
				XSSFCell cell13 = rowhead.createCell(13);
				cell13.setCellStyle(style);
				cell13.setCellValue("CGST_RATE"); 
				
			 
				

				XSSFCell cell14 = rowhead.createCell(14);
				cell14.setCellStyle(style);
				cell14.setCellValue("SGST_AMT"); 
				
				XSSFCell cell15 = rowhead.createCell(15);
				cell15.setCellStyle(style);
				cell15.setCellValue("SGST_RATE"); 
				 

				XSSFCell cell16 = rowhead.createCell(16);
				cell16.setCellStyle(style);
				cell16.setCellValue("DCI_APPROPRIATION_FLAG"); 
				
				
				XSSFCell cell17 = rowhead.createCell(17);
				cell17.setCellStyle(style);
				cell17.setCellValue("DCI_APPROPRIATION_DT"); 
				
				XSSFCell cell18 = rowhead.createCell(18);
				cell18.setCellStyle(style);
				cell18.setCellValue("DCI_GUARANTEE_START_DT"); 

				XSSFCell cell19 = rowhead.createCell(19);
				cell19.setCellStyle(style);
				cell19.setCellValue("MEM_STATE_NAME"); 
				
				 
				String fromDate=(String) session.getAttribute("fromDate");  
				String toDate= (String) session.getAttribute("toDate");  
				List<Object[]> report1=new ArrayList<Object[]>();
				report1=statutsWiseReportService.getDanTeaxtAsfGstReportList(fromDate, toDate);
				if(!report1.isEmpty()){ 
				int index = 1;
				int srno=1;
				for(Object[] obj1:report1){
					 XSSFRow row = sheet.createRow((short) index);	
                  row.createCell((short) 0).setCellValue(srno);
                 
                  if((String.valueOf(obj1[0]).equals("null")) || (String.valueOf(obj1[0])==null)){
                      	row.createCell((short) 1).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 1).setCellValue(obj1[0].toString()==null ? "":obj1[0].toString());
                      }
                   
                   if((String.valueOf(obj1[1]).equals("null")) || (String.valueOf(obj1[1])==null)){
                      	row.createCell((short) 2).setCellValue(""); 
                      }
                      else{
                      	row.createCell((short)2).setCellValue(obj1[1].toString().equals("null") ? "":obj1[1].toString()); 
                      }
                 
                  if((String.valueOf(obj1[2]).equals("null")) || (String.valueOf(obj1[2])==null)){
                      	row.createCell((short) 3).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 3).setCellValue(obj1[2].toString()==null ? "":obj1[2].toString());
                      }
                 
                  if((String.valueOf(obj1[3]).equals("null")) || (String.valueOf(obj1[3])==null)){
                      	row.createCell((short) 4).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 4).setCellValue(obj1[3].toString()==null ? "":obj1[3].toString());
                      }
                 
                  if((String.valueOf(obj1[4]).equals("null")) || (String.valueOf(obj1[4])==null)){
                      	row.createCell((short) 5).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 5).setCellValue(obj1[4].toString()==null ? "":obj1[4].toString());
                      }
                  
                  if((String.valueOf(obj1[5]).equals("null")) || (String.valueOf(obj1[5])==null)){
                      	row.createCell((short) 6).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 6).setCellValue(obj1[5].toString()==null ? "":obj1[5].toString());
                      }
                  
                  if((String.valueOf(obj1[6]).equals("null")) || (String.valueOf(obj1[6])==null)){
                      	row.createCell((short) 7).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 7).setCellValue(obj1[6].toString()==null ? "":obj1[6].toString());
                      }
                 
                  if((String.valueOf(obj1[7]).equals("null")) || (String.valueOf(obj1[7])==null)){
                      	row.createCell((short) 8).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 8).setCellValue(obj1[7].toString()==null ? "":obj1[7].toString());
                      }
                  
                  if((String.valueOf(obj1[8]).equals("null")) || (String.valueOf(obj1[8])==null)){
                      	row.createCell((short) 9).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 9).setCellValue(obj1[8].toString()==null ? "":obj1[8].toString());
                      }
                 
                  if((String.valueOf(obj1[9]).equals("null")) || (String.valueOf(obj1[9])==null)){
                      	row.createCell((short) 10).setCellValue(""); 
                      }
                      else{ 
                     	 row.createCell((short) 10).setCellValue(obj1[9].toString()==null ? "":obj1[9].toString());
                      }

                 
                  if((String.valueOf(obj1[10]).equals("null")) || (String.valueOf(obj1[10])==null)){
                      	row.createCell((short) 11).setCellValue(""); 
                      }
                      else{ 
                     	 row.createCell((short) 11).setCellValue(obj1[10].toString()==null ? "":obj1[10].toString());
                      }
                  
                  if((String.valueOf(obj1[11]).equals("null")) || (String.valueOf(obj1[11])==null)){
                      	row.createCell((short) 12).setCellValue(""); 
                      }
                      else{ 
                     	 row.createCell((short) 12).setCellValue( obj1[11].toString()==null ?"":obj1[11].toString());
                      }
                  
                  if((String.valueOf(obj1[12]).equals("null")) || (String.valueOf(obj1[12])==null)){
                      	row.createCell((short) 13).setCellValue(""); 
                      }
                      else{ 
                     	 row.createCell((short) 13).setCellValue(obj1[12].toString()==null ? "":obj1[12].toString());
                      }
                 
                  if((String.valueOf(obj1[13]).equals("null")) || (String.valueOf(obj1[13])==null)){
                      	row.createCell((short) 14).setCellValue(""); 
                      }
                      else{ 
                     	 row.createCell((short) 14).setCellValue(obj1[13].toString()==null ? "":obj1[13].toString());
                      }
                 
                  if((String.valueOf(obj1[14]).equals("null")) || (String.valueOf(obj1[14])==null)){
                      	row.createCell((short) 15).setCellValue(""); 
                      }
                      else{ 
                     	 row.createCell((short) 15).setCellValue(obj1[14].toString()==null ? "":obj1[14].toString());
                      }
                 
                  if((String.valueOf(obj1[15]).equals("null")) || (String.valueOf(obj1[15])==null)){
                      	row.createCell((short) 16).setCellValue(""); 
                      }
                      else{ 
                     	 row.createCell((short) 16).setCellValue(obj1[15].toString()==null ? "":obj1[15].toString());
                      }
                  
                  if((String.valueOf(obj1[16]).equals("null")) || (String.valueOf(obj1[16])==null)){
                      	row.createCell((short) 17).setCellValue(""); 
                      }
                      else{ 
                     	 row.createCell((short) 17).setCellValue(obj1[16].toString()==null ? "":obj1[16].toString());
                      }
                 
                  if((String.valueOf(obj1[17]).equals("null")) || (String.valueOf(obj1[17])==null)){
                      	row.createCell((short) 18).setCellValue(""); 
                      }
                      else{ 
                     	 row.createCell((short) 18).setCellValue(obj1[17].toString()==null ? "":obj1[17].toString());
                      }
                  
                  
                  if((String.valueOf(obj1[18]).equals("null")) || (String.valueOf(obj1[18])==null)){
                      	row.createCell((short) 19).setCellValue(""); 
                      }
                      else{ 
                     	 row.createCell((short) 19).setCellValue(obj1[18].toString()==null ? "":obj1[18].toString());
                      }
                  srno++;
                  index++; 
				 }
				}
				FileOutputStream fileOut = new FileOutputStream(filePath);
				hwb.write(fileOut);
				fileOut.close();

				ModelAndView model = new ModelAndView("newRolePage");

				model.addObject( "excelFileDownLoadSuccessfully", "File DownLoaded Successfully in this location F:/ExcelReports/nbfcDANGFReportExcelFile.xls.");

				File downloadFile = new File(filePath);
				log.info("downloadFile =" + downloadFile);
				FileInputStream inputStream = new FileInputStream(downloadFile);

				response.setContentLength((int) downloadFile.length());

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
				response.setHeader(headerKey, headerValue);

				// get output stream of the response
				OutputStream outStream = response.getOutputStream();

				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;

				// write bytes read from the input stream into the output stream
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
				inputStream.close();
				outStream.close();
				return model;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception == " + e);
				System.out.println("Exception == " + e);
			}
			return null;
		}
	 @RequestMapping(value = "/DANGstASFbackButton", method = RequestMethod.POST)
		public ModelAndView DANGstASFbackButton( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
				Model model,HttpServletRequest request) throws JRException, ParseException {
			        Map<String, Object> model1 = new HashMap<String, Object>();
			        String Role = (String) session.getAttribute("uRole");
			if (result.hasErrors()) {
				if (Role.equals("CMAKER")) {
					model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
	                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
			        model1.put("homePage", "cgtmseMakerHome");
	             } else if (Role.equals("CCHECKER")) {
					model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
					model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
					model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
					model1.put("homePage", "cgtmseCheckerHome");
				 } else if (Role.equals("NMAKER")) {
					model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
					model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
					model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
					model1.put("homePage", "nbfcMakerHome");

				} else if (Role.equals("NCHECKER")) {
					model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
					model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
					model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
					model1.put("homePage", "nbfcCheckerHome");

				}
				return new ModelAndView("DANTAXGSTReportForASFInputForm", model1);
			}

			if (Role.equals("CMAKER")) {
				userId = "ADMIN";
				model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
	            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
				model1.put("homePage", "cgtmseMakerHome");

			} else if (Role.equals("CCHECKER")) {
				userId = "ADMIN";
				model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
				model1.put("homePage", "cgtmseCheckerHome");
			 } else if (Role.equals("NMAKER")) {
				userId = (String) session.getAttribute("userId");
				model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
				model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcMakerHome");

			} else if (Role.equals("NCHECKER")) {
				userId = (String) session.getAttribute("userId");
				model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
				model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcCheckerHome");

			}
			 return new ModelAndView("DANTAXGSTReportForASFInputForm", model1);
			 
		}
	 
	 
	 //// start DAN TAX(GST) Report For GF 
	 @RequestMapping(value = "/DANTAXGSTReportForGFList", method = RequestMethod.POST)
		public ModelAndView DANTAXGSTReportForGFList(
				@ModelAttribute("command") NPADetailsBean bean,
				BindingResult result, HttpSession session, Model model)
				throws JRException, ParseException {
			Map<String, Object> model1 = new HashMap<String, Object>();
			 
			String Role = (String) session.getAttribute("uRole");
			//String userId = (String) session.getAttribute("userId");
			//memberId = npaService.getMemberId(userId);
			String mliLongName=bean.getMliLongName();
			 
			System.out.println("mliLongName==194==="+mliLongName);
			validator.DantextReportDetailsValidate(bean, result);
			if (result.hasErrors()) {
				if (Role.equals("CMAKER")) {
					// userId = "ADMIN";
					model1.put("guaranteelist", userActivityService.getActivity(
							"CGTMSEMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"CGTMSEMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"CGTMSEMAKER", "Receipt_Payments"));
					model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
					model1.put("repList", userActivityService.getReport(
							"CGTMSEMAKER", "User_Report"));
					model1.put("homePage", "cgtmseMakerHome");

				} else if (Role.equals("CCHECKER")) {
					// userId = "ADMIN";
					model1.put("adminlist", userActivityService.getActivity(
							"CGTMSECHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"CGTMSECHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"CGTMSECHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"CGTMSECHECKER", "Receipt_Payments"));
					model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
					model1.put("repList", userActivityService.getReport(
							"CGTMSECHECKER", "User_Report"));
					model1.put("homePage", "cgtmseCheckerHome");
					// return null;
				} else if (Role.equals("NMAKER")) {
					model1.put("adminlist", userActivityService.getActivity(
							"NBFCMAKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"NBFCMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"NBFCMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"NBFCMAKER", "Receipt_Payments"));
					model1.put("repList", userActivityService.getReport(
							"NBFCMAKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
					model1.put("homePage", "nbfcMakerHome");

				} else if (Role.equals("NCHECKER")) {
					model1.put("adminlist", userActivityService.getActivity(
							"NBFCCHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"NBFCCHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"NBFCCHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"NBFCCHECKER", "Receipt_Payments"));
					model1.put("repList", userActivityService.getReport(
							"NBFCCHECKER", "User_Report"));
					 model1.put("homePage", "nbfcCheckerHome");

				}
				return new ModelAndView("DANTAXGSTReportForGFInputForm", model1);
			}

			if (Role.equals("CMAKER")) {
				userId = "ADMIN";
				model1.put("guaranteelist", userActivityService.getActivity(
						"CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"CGTMSEMAKER", "Receipt_Payments"));

				model1.put("repList",
						userActivityService.getReport("CGTMSEMAKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
				model1.put("homePage", "cgtmseMakerHome");

			} else if (Role.equals("CCHECKER")) {
				userId = "ADMIN";
				model1.put("adminlist", userActivityService.getActivity(
						"CGTMSECHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"CGTMSECHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"CGTMSECHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"CGTMSECHECKER", "Receipt_Payments"));

				model1.put("repList", userActivityService.getReport(
						"CGTMSECHECKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
				model1.put("homePage", "cgtmseCheckerHome");
				// return null;
			} else if (Role.equals("NMAKER")) {
				userId = (String) session.getAttribute("userId");
				model1.put("adminlist", userActivityService.getActivity(
						"NBFCMAKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"NBFCMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"NBFCMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"NBFCMAKER", "Receipt_Payments"));
				model1.put("repList",
						userActivityService.getReport("NBFCMAKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcMakerHome");

			} else if (Role.equals("NCHECKER")) {
				userId = (String) session.getAttribute("userId");
				model1.put("adminlist", userActivityService.getActivity(
						"NBFCCHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"NBFCCHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"NBFCCHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"NBFCCHECKER", "Receipt_Payments"));
				model1.put("repList",
						userActivityService.getReport("NBFCCHECKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcCheckerHome");

			}
			/*String Mem_Id ="";
			if(!mliLongName.equals("ALL")){
			mem_id = userActivityService.getBankID(mliLongName);
			Mem_Id= mem_id.getBnkId() + mem_id.getZneID() + mem_id.getBrnName();
			}
			else{
				Mem_Id="ALL";	
			}*/
			List<Object[]> rows1=null;

			//Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(bean.getToDate());
			//Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean .getFromDate());
			
			String toDate= DateFormate.dateformate(bean.getToDate());
			String fromDate=DateFormate.dateformate(bean .getFromDate());
			
			System.out.println(fromDate);
			session.setAttribute("fromDate", fromDate); 
			session.setAttribute("toDate", toDate); 
			
			//String ssiName="ALL";
			/*String ssiName=bean.getNameOffSsiUnit();
			session.setAttribute("Role", Role); 
			session.setAttribute("userId", userId); 
			session.setAttribute("fromDate", fromDate); 
			session.setAttribute("toDate", toDate); 
			//session.setAttribute("Mem_Id", Mem_Id); 
			session.setAttribute("ssiName", ssiName); 
			*/
			
			if(Role.equals("CCHECKER") || Role.equals("CMAKER")){
				//rows=statutsWiseReportService.getDANTAXGSTFList(fromDate, toDate);
				rows1 = statutsWiseReportService.getDanTeaxtGstReportList(fromDate, toDate);
			}else{
				//rows=statutsWiseReportService.getDANTAXGSTFList(fromDate, toDate);
				rows1 = statutsWiseReportService.getDanTeaxtGstReportList( fromDate, toDate);
			 }
			if (!rows1.isEmpty()) {
				model1.put("rows1", rows1);
			 } else {
				model1.put("message", "NO Data Found !!");
			} 
			return new ModelAndView("DANTAXGSTReportForGFInputForm", model1);
		} 
	 
	 
	 @RequestMapping(value = "/DANTAXGSTReportForGFListDownload", method = RequestMethod.GET)
		public ModelAndView DANTAXGSTReportForGFListDownload(
				@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
				HttpServletRequest request, HttpServletResponse response,
				HttpSession session) throws IOException {
			try {
             String ASFSummaryDetailsFileName1="DANGstGFReport.xlsx";
				String filePath = downloadFileDirPath + File.separator + ASFSummaryDetailsFileName1;
				System.out.println(filePath);
				File file = new File(downloadFileDirPath);
				boolean isCreated = file.mkdir();
             if (isCreated) {
					File file1 = new File(filePath);
					boolean isExists = file1.exists();
					if (isExists) {

					} else {
						file1.createNewFile();
					}

				}
             
             XSSFWorkbook hwb = new XSSFWorkbook();
				XSSFSheet sheet = hwb.createSheet("DANGstGFDownLoadedFile");
             CellStyle style = hwb.createCellStyle();
				Font font = hwb.createFont();
				font.setFontHeightInPoints((short) 11);
				font.setFontName(HSSFFont.FONT_ARIAL);
				font.setBoldweight(HSSFFont.COLOR_NORMAL);
				font.setBold(true);
				font.setColor(HSSFColor.DARK_BLUE.index);
				style.setFont(font);
				style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				sheet.createFreezePane(0,1 ); // Freeze 1st Row
												// sheet.createFreezePane(int
												// colSplit, int rowSplit, int
												// leftmostColumn, int topRow)
         XSSFRow rowhead = sheet.createRow((short) 0);
         
			
			 
				XSSFCell cell0 = rowhead.createCell(0);
				cell0.setCellStyle(style);
				cell0.setCellValue("SrNo");// Done 1

				XSSFCell cell1 = rowhead.createCell(1);
				cell1.setCellStyle(style);
				cell1.setCellValue("DAN_ID");// Done 3

				XSSFCell cell2 = rowhead.createCell(2);
				cell2.setCellStyle(style);
				cell2.setCellValue("BANK NAME"); 

				XSSFCell cell3 = rowhead.createCell(3);
				cell3.setCellStyle(style);
				cell3.setCellValue("ZONE"); 
				
				XSSFCell cell4 = rowhead.createCell(4);
				cell4.setCellStyle(style);
				cell4.setCellValue("MLIID"); 

				
				XSSFCell cell5 = rowhead.createCell(5);
				cell5.setCellStyle(style);
				cell5.setCellValue("GST No"); 

				XSSFCell cell6 = rowhead.createCell(6);
				cell6.setCellStyle(style);
				cell6.setCellValue("Portfolio Number"); 
				
				
				XSSFCell cell7 = rowhead.createCell(7);
				cell7.setCellStyle(style);
				cell7.setCellValue("DAN_ID"); 
				
				 
				XSSFCell cell8 = rowhead.createCell(8);
				cell8.setCellStyle(style);
				cell8.setCellValue("TAX_INV_ID"); 
				
				XSSFCell cell9 = rowhead.createCell(9);
				cell9.setCellStyle(style);
				cell9.setCellValue("DANAMT"); 

				XSSFCell cell10 = rowhead.createCell(10);
				cell10.setCellStyle(style);
				cell10.setCellValue("BASEAMT"); 
				
				
				
				XSSFCell cell11 = rowhead.createCell(11);
				cell11.setCellStyle(style);
				cell11.setCellValue("IGST_AMT"); 
				
				XSSFCell cell12 = rowhead.createCell(12);
				cell12.setCellStyle(style);
				cell12.setCellValue("IGST_RATE"); 

				XSSFCell cell13 = rowhead.createCell(13);
				cell13.setCellStyle(style);
				cell13.setCellValue("CGST_AMT"); 
				
			 
				

				XSSFCell cell14 = rowhead.createCell(14);
				cell14.setCellStyle(style);
				cell14.setCellValue("CGST_RATE"); 
				
				XSSFCell cell15 = rowhead.createCell(15);
				cell15.setCellStyle(style);
				cell15.setCellValue("SGST_AMT"); 

				XSSFCell cell16 = rowhead.createCell(16);
				cell16.setCellStyle(style);
				cell16.setCellValue("SGST_RATE"); 
				
				
				XSSFCell cell17 = rowhead.createCell(17);
				cell17.setCellStyle(style);
				cell17.setCellValue("DCI_APPROPRIATION_FLAG"); 
				
				XSSFCell cell18 = rowhead.createCell(18);
				cell18.setCellStyle(style);
				cell18.setCellValue("DCI_APPROPRIATION_DT"); 

				XSSFCell cell19 = rowhead.createCell(19);
				cell19.setCellStyle(style);
				cell19.setCellValue("DCI_GUARANTEE_START_DT "); 
				
				XSSFCell cell20 = rowhead.createCell(20);
				cell20.setCellStyle(style);
				cell20.setCellValue("MEM_STATE_NAME"); 
				String fromDate=(String) session.getAttribute("fromDate");  
				String toDate= (String) session.getAttribute("toDate");  
				List<Object[]> report1=new ArrayList<Object[]>();
				
				report1=statutsWiseReportService.getDanTeaxtGstReportList(fromDate, toDate);
				if(!report1.isEmpty()){ 
				int index = 1;
				int srno=1;
				for(Object[] obj1:report1){
					 XSSFRow row = sheet.createRow((short) index);	
                  row.createCell((short) 0).setCellValue(srno);
                  row.createCell((short) 1).setCellValue(obj1[0].toString()==null ? "":obj1[0].toString());
                  //row.createCell((short) 2).setCellValue(""); 
                  String  a=String.valueOf(obj1[1]);
                  System.out.println(a);
                  
                  if((String.valueOf(obj1[1]).equals("null")) || (String.valueOf(obj1[1])==null)){
                      	row.createCell((short) 2).setCellValue(""); 
                      }
                      else{
                      	row.createCell((short)2).setCellValue(obj1[1].toString().equals("null") ? "":obj1[1].toString()); 
                      }
                  
                  
                  /*String  zone= obj1[1].toString().equals("null") ? "0":obj1[1].toString();
                  System.out.println("zone"+zone);
                  if(!zone.equals("null")){
                  row.createCell((short) 2).setCellValue( obj1[1].toString()==null ?"":obj1[1].toString());
                  }
                  else{
                 	 row.createCell((short) 2).setCellValue(""); 
                  }*/
                 
                  if((String.valueOf(obj1[2]).equals("null")) || (String.valueOf(obj1[2])==null)){
                      	row.createCell((short) 3).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 3).setCellValue(obj1[2].toString()==null ? "":obj1[2].toString());
                      }
                 
                  if((String.valueOf(obj1[3]).equals("null")) || (String.valueOf(obj1[3])==null)){
                      	row.createCell((short) 4).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 4).setCellValue(obj1[3].toString()==null ? "":obj1[3].toString());
                      }
                  
                  
                  if((String.valueOf(obj1[4]).equals("null")) || (String.valueOf(obj1[4])==null)){
                      	row.createCell((short) 5).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 5).setCellValue(obj1[4].toString()==null ? "":obj1[4].toString());
                      }
                  
                  if((String.valueOf(obj1[5]).equals("null")) || (String.valueOf(obj1[5])==null)){
                      	row.createCell((short) 6).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 6).setCellValue(obj1[5].toString()==null ? "":obj1[5].toString());
                      }
                 
                  if((String.valueOf(obj1[6]).equals("null")) || (String.valueOf(obj1[6])==null)){
                      	row.createCell((short) 7).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 7).setCellValue(obj1[6].toString()==null ? "":obj1[6].toString());
                      }
                 
                  if((String.valueOf(obj1[7]).equals("null")) || (String.valueOf(obj1[7])==null)){
                      	row.createCell((short) 8).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 8).setCellValue(obj1[7].toString()==null ? "":obj1[7].toString());
                      }
                 
                  if((String.valueOf(obj1[8]).equals("null")) || (String.valueOf(obj1[8])==null)){
                      	row.createCell((short) 9).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 9).setCellValue(obj1[8].toString()==null ? "":obj1[8].toString());
                      }
                 
                  if((String.valueOf(obj1[9]).equals("null")) || (String.valueOf(obj1[9])==null)){
                      	row.createCell((short)10).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 10).setCellValue(obj1[9].toString()==null ? "":obj1[9].toString());
                      }
                 
                  if((String.valueOf(obj1[10]).equals("null")) || (String.valueOf(obj1[10])==null)){
                      	row.createCell((short)11).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 11).setCellValue(obj1[10].toString()==null ? "":obj1[10].toString());
                      }
                  
                  if((String.valueOf(obj1[11]).equals("null")) || (String.valueOf(obj1[11])==null)){
                      	row.createCell((short)12).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 12).setCellValue( obj1[11].toString()==null ?"":obj1[11].toString());
                      }
                  
                  if((String.valueOf(obj1[12]).equals("null")) || (String.valueOf(obj1[12])==null)){
                      	row.createCell((short)13).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 13).setCellValue(obj1[12].toString()==null ? "":obj1[12].toString());
                      }
                  
                  if((String.valueOf(obj1[13]).equals("null")) || (String.valueOf(obj1[13])==null)){
                      	row.createCell((short)14).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 14).setCellValue(obj1[13].toString()==null ? "":obj1[13].toString());
                      }
                 
                  if((String.valueOf(obj1[14]).equals("null")) || (String.valueOf(obj1[14])==null)){
                      	row.createCell((short)15).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 15).setCellValue(obj1[14].toString()==null ? "":obj1[14].toString());
                      }
                  
                  if((String.valueOf(obj1[15]).equals("null")) || (String.valueOf(obj1[15])==null)){
                      	row.createCell((short)16).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 16).setCellValue(obj1[15].toString()==null ? "":obj1[15].toString());
                      }
                 
                  if((String.valueOf(obj1[16]).equals("null")) || (String.valueOf(obj1[16])==null)){
                      	row.createCell((short)17).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 17).setCellValue(obj1[16].toString()==null ? "":obj1[16].toString());
                      }
                 
                  if((String.valueOf(obj1[17]).equals("null")) || (String.valueOf(obj1[17])==null)){
                      	row.createCell((short)18).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 18).setCellValue(obj1[17].toString()==null ? "":obj1[17].toString());
                      }
                 
                  if((String.valueOf(obj1[18]).equals("null")) || (String.valueOf(obj1[18])==null)){
                      	row.createCell((short)19).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 19).setCellValue(obj1[18].toString()==null ? "":obj1[18].toString());
                      }
                 /*
                  if((String.valueOf(obj1[19]).equals("null")) || (String.valueOf(obj1[19])==null)){
                      	row.createCell((short)20).setCellValue(""); 
                      }
                      else{
                     	 row.createCell((short) 20).setCellValue(obj1[19].toString()==null ? "":obj1[19].toString());
                      }*/
                  
                  srno++;
                  index++; 
				 }
				}
				FileOutputStream fileOut = new FileOutputStream(filePath);
				hwb.write(fileOut);
				fileOut.close();

				ModelAndView model = new ModelAndView("newRolePage");

				model.addObject( "excelFileDownLoadSuccessfully", "File DownLoaded Successfully in this location F:/ExcelReports/nbfcDANGFReportExcelFile.xls.");

				File downloadFile = new File(filePath);
				log.info("downloadFile =" + downloadFile);
				FileInputStream inputStream = new FileInputStream(downloadFile);

				response.setContentLength((int) downloadFile.length());

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
				response.setHeader(headerKey, headerValue);

				// get output stream of the response
				OutputStream outStream = response.getOutputStream();

				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;

				// write bytes read from the input stream into the output stream
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
				inputStream.close();
				outStream.close();
				return model;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception == " + e);
				System.out.println("Exception == " + e);
			}
			return null;
		}
	 
	 @RequestMapping(value = "/DANGstGfbackButton", method = RequestMethod.POST)
		public ModelAndView DANGstGfbackButton( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
				Model model,HttpServletRequest request) throws JRException, ParseException {
			        Map<String, Object> model1 = new HashMap<String, Object>();
			        String Role = (String) session.getAttribute("uRole");
			if (result.hasErrors()) {
				if (Role.equals("CMAKER")) {
					model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
	                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
			        model1.put("homePage", "cgtmseMakerHome");
	             } else if (Role.equals("CCHECKER")) {
					model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
					model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
					model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
					model1.put("homePage", "cgtmseCheckerHome");
				 } else if (Role.equals("NMAKER")) {
					model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
					model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
					model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
					model1.put("homePage", "nbfcMakerHome");

				} else if (Role.equals("NCHECKER")) {
					model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
					model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
					model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
					model1.put("homePage", "nbfcCheckerHome");

				}
				return new ModelAndView("DANTAXGSTReportForGFInputForm", model1);
			}

			if (Role.equals("CMAKER")) {
				userId = "ADMIN";
				model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
	            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
				model1.put("homePage", "cgtmseMakerHome");

			} else if (Role.equals("CCHECKER")) {
				userId = "ADMIN";
				model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
				model1.put("homePage", "cgtmseCheckerHome");
			 } else if (Role.equals("NMAKER")) {
				userId = (String) session.getAttribute("userId");
				model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
				model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcMakerHome");

			} else if (Role.equals("NCHECKER")) {
				userId = (String) session.getAttribute("userId");
				model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
				model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcCheckerHome");

			}
			 return new ModelAndView("DANTAXGSTReportForGFInputForm", model1);
			// return null;
		}
	 @RequestMapping(value = "/DANTAXGSTReportForASF", method = RequestMethod.GET)
		public ModelAndView DANTAXGSTReportForASF(
				@ModelAttribute("command") NPADetailsBean bean,
				BindingResult result, HttpSession session, Model model)
				throws JRException {
			Map<String, Object> model1 = new HashMap<String, Object>();
			String Role = (String) session.getAttribute("uRole");
			 //userId = (String) session.getAttribute("userId");
			//String loginUserMemId = npaService.getMemberId(userId);
			// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
			if (Role.equals("CMAKER")) {
				model1.put("guaranteelist", userActivityService.getActivity(
						"CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"CGTMSEMAKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
				model1.put("repList",
						userActivityService.getReport("CGTMSEMAKER", "User_Report"));
				model1.put("stateList", prepareStateListofBean(stateService.listStates("CCA")));
				// model1.put("actNameHome",
				// userActivityService.getActivityName("CGTMSEMAKER",
				// "cgpanDetailReport"));// Added by Say 31 Jan19
				//model1.put("MLIID", loginUserMemId);
				model1.put("homePage", "cgtmseMakerHome");

			} else if (Role.equals("CCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity(
						"CGTMSECHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"CGTMSECHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"CGTMSECHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"CGTMSECHECKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport(
						"CGTMSECHECKER", "User_Report"));
				//model1.put("MLIID", loginUserMemId);
				model1.put("homePage", "cgtmseCheckerHome");
				// return null;
			} else if (Role.equals("NMAKER")) {
				// added by say 6 feb-----------------------
				model1.put("adminlist", userActivityService.getActivity(
						"NBFCMAKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"NBFCMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"NBFCMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"NBFCMAKER", "Receipt_Payments"));
				model1.put("repList",
						userActivityService.getReport("NBFCMAKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
				//model1.put("MLIID", loginUserMemId);
				model1.put("homePage", "nbfcMakerHome");

			} else if (Role.equals("NCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity(
						"NBFCCHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"NBFCCHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"NBFCCHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"NBFCCHECKER", "Receipt_Payments"));
				model1.put("repList",
						userActivityService.getReport("NBFCCHECKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
				//model1.put("MLIID", loginUserMemId);
				model1.put("homePage", "nbfcCheckerHome");

			}
			return new ModelAndView("DANTAXGSTReportForASFInputForm", model1);
			// return null;
		}
	 
	 @RequestMapping(value = "/DANTAXGSTReportForGF", method = RequestMethod.GET)
		public ModelAndView DANTAXGSTReportForGF(
				@ModelAttribute("command") NPADetailsBean bean,
				BindingResult result, HttpSession session, Model model)
				throws JRException {
			Map<String, Object> model1 = new HashMap<String, Object>();
			String Role = (String) session.getAttribute("uRole");
			 userId = (String) session.getAttribute("userId");
			//String loginUserMemId = npaService.getMemberId(userId);
			// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
			if (Role.equals("CMAKER")) {
				model1.put("guaranteelist", userActivityService.getActivity(
						"CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"CGTMSEMAKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
				model1.put("repList",
						userActivityService.getReport("CGTMSEMAKER", "User_Report"));
				model1.put("stateList", prepareStateListofBean(stateService.listStates("CCA")));
				// model1.put("actNameHome",
				// userActivityService.getActivityName("CGTMSEMAKER",
				// "cgpanDetailReport"));// Added by Say 31 Jan19
				//model1.put("MLIID", loginUserMemId);
				model1.put("homePage", "cgtmseMakerHome");

			} else if (Role.equals("CCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity(
						"CGTMSECHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"CGTMSECHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"CGTMSECHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"CGTMSECHECKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport(
						"CGTMSECHECKER", "User_Report"));
				//model1.put("MLIID", loginUserMemId);
				model1.put("homePage", "cgtmseCheckerHome");
				// return null;
			} else if (Role.equals("NMAKER")) {
				// added by say 6 feb-----------------------
				model1.put("adminlist", userActivityService.getActivity(
						"NBFCMAKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"NBFCMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"NBFCMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"NBFCMAKER", "Receipt_Payments"));
				model1.put("repList",
						userActivityService.getReport("NBFCMAKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
				//model1.put("MLIID", loginUserMemId);
				model1.put("homePage", "nbfcMakerHome");

			} else if (Role.equals("NCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity(
						"NBFCCHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity(
						"NBFCCHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity(
						"NBFCCHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity(
						"NBFCCHECKER", "Receipt_Payments"));
				model1.put("repList",
						userActivityService.getReport("NBFCCHECKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
				//model1.put("MLIID", loginUserMemId);
				model1.put("homePage", "nbfcCheckerHome");

			}
			model1.put("userId", "userId");
			return new ModelAndView("DANTAXGSTReportForGFInputForm", model1);
			// return null;
		}	
	 
	//Diksha Guarantee Approval Report
		 @RequestMapping(value = "/GuaranteeApprovalReport", method = RequestMethod.GET) 
		 public ModelAndView GuaranteeApprovalReport(
					@ModelAttribute("command") NPADetailsBean bean,
					BindingResult result, HttpSession session, Model model)
					throws JRException {
				Map<String, Object> model1 = new HashMap<String, Object>();
				String Role = (String) session.getAttribute("uRole");
				 //userId = (String) session.getAttribute("userId");
				//String loginUserMemId = npaService.getMemberId(userId);
				// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
				if (Role.equals("CMAKER")) {
					model1.put("guaranteelist", userActivityService.getActivity(
							"CGTMSEMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"CGTMSEMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"CGTMSEMAKER", "Receipt_Payments"));
					model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
					model1.put("repList",
							userActivityService.getReport("CGTMSEMAKER", "User_Report"));
					model1.put("stateList", prepareStateListofBean(stateService.listStates("CCA")));
					// model1.put("actNameHome",
					// userActivityService.getActivityName("CGTMSEMAKER",
					// "cgpanDetailReport"));// Added by Say 31 Jan19
					//model1.put("MLIID", loginUserMemId);
					model1.put("homePage", "cgtmseMakerHome");

				} else if (Role.equals("CCHECKER")) {
					model1.put("adminlist", userActivityService.getActivity(
							"CGTMSECHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"CGTMSECHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"CGTMSECHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"CGTMSECHECKER", "Receipt_Payments"));
					model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
					model1.put("repList", userActivityService.getReport(
							"CGTMSECHECKER", "User_Report"));
					//model1.put("MLIID", loginUserMemId);
					model1.put("homePage", "cgtmseCheckerHome");
					// return null;
				} else if (Role.equals("NMAKER")) {
					// added by say 6 feb-----------------------
					model1.put("adminlist", userActivityService.getActivity(
							"NBFCMAKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"NBFCMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"NBFCMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"NBFCMAKER", "Receipt_Payments"));
					model1.put("repList",
							userActivityService.getReport("NBFCMAKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
					//model1.put("MLIID", loginUserMemId);
					model1.put("homePage", "nbfcMakerHome");

				} else if (Role.equals("NCHECKER")) {
					model1.put("adminlist", userActivityService.getActivity(
							"NBFCCHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"NBFCCHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"NBFCCHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"NBFCCHECKER", "Receipt_Payments"));
					model1.put("repList",
							userActivityService.getReport("NBFCCHECKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
					//model1.put("MLIID", loginUserMemId);
					model1.put("homePage", "nbfcCheckerHome");

				}
				return new ModelAndView("GuaranteeApprovalReport", model1);
				// return null;
			}
			
			
			 
			@RequestMapping(value = "/GuaranteeApprovalReportDataList", method = RequestMethod.POST)
			public ModelAndView GuaranteeApprovalReportDataList(
					@ModelAttribute("command") NPADetailsBean bean,
					BindingResult result, HttpSession session, Model model,HttpServletRequest request)
					throws JRException, ParseException {
				Map<String, Object> model1 = new HashMap<String, Object>();
				List<NPADetailsBean> NPADetailList=null;
				
				session.setAttribute("NPADetailList",null);
				session.removeAttribute("NPADetailList");
				
				String Role = (String) session.getAttribute("uRole");
				//String userId = (String) session.getAttribute("userId");
				//memberId = npaService.getMemberId(userId);
				
				
				//String mliLongName=bean.getMliLongName();
				 
				//System.out.println("mliLongName==194==="+mliLongName);
				validator.MliReportDetailsValidate(bean, result);
				if (result.hasErrors()) {
					if (Role.equals("CMAKER")) {
						// userId = "ADMIN";
						model1.put("guaranteelist", userActivityService.getActivity(
								"CGTMSEMAKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"CGTMSEMAKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"CGTMSEMAKER", "Receipt_Payments"));
						model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
						model1.put("repList", userActivityService.getReport(
								"CGTMSEMAKER", "User_Report"));
						model1.put("homePage", "cgtmseMakerHome");

					} else if (Role.equals("CCHECKER")) {
						// userId = "ADMIN";
						model1.put("adminlist", userActivityService.getActivity(
								"CGTMSECHECKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity(
								"CGTMSECHECKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"CGTMSECHECKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"CGTMSECHECKER", "Receipt_Payments"));
						model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
						model1.put("repList", userActivityService.getReport(
								"CGTMSECHECKER", "User_Report"));
						model1.put("homePage", "cgtmseCheckerHome");
						// return null;
					} else if (Role.equals("NMAKER")) {
						model1.put("adminlist", userActivityService.getActivity(
								"NBFCMAKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity(
								"NBFCMAKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"NBFCMAKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"NBFCMAKER", "Receipt_Payments"));
						model1.put("repList", userActivityService.getReport(
								"NBFCMAKER", "User_Report"));
						model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
						model1.put("homePage", "nbfcMakerHome");

					} else if (Role.equals("NCHECKER")) {
						model1.put("adminlist", userActivityService.getActivity(
								"NBFCCHECKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity(
								"NBFCCHECKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity(
								"NBFCCHECKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity(
								"NBFCCHECKER", "Receipt_Payments"));
						model1.put("repList", userActivityService.getReport(
								"NBFCCHECKER", "User_Report"));
						 model1.put("homePage", "nbfcCheckerHome");

					}
					//return new ModelAndView("GuaranteeApprovalReport", model1); //GuaranteeApprovalReportDetails
					return new ModelAndView("GuaranteeApprovalReportDetails", model1);
				}

				if (Role.equals("CMAKER")) {
					userId = "ADMIN";
					model1.put("guaranteelist", userActivityService.getActivity(
							"CGTMSEMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"CGTMSEMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"CGTMSEMAKER", "Receipt_Payments"));

					model1.put("repList",
							userActivityService.getReport("CGTMSEMAKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
					model1.put("homePage", "cgtmseMakerHome");

				} else if (Role.equals("CCHECKER")) {
					userId = "ADMIN";
					model1.put("adminlist", userActivityService.getActivity(
							"CGTMSECHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"CGTMSECHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"CGTMSECHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"CGTMSECHECKER", "Receipt_Payments"));

					model1.put("repList", userActivityService.getReport(
							"CGTMSECHECKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
					model1.put("homePage", "cgtmseCheckerHome");
					// return null;
				} else if (Role.equals("NMAKER")) {
					userId = (String) session.getAttribute("userId");
					model1.put("adminlist", userActivityService.getActivity(
							"NBFCMAKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"NBFCMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"NBFCMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"NBFCMAKER", "Receipt_Payments"));
					model1.put("repList",
							userActivityService.getReport("NBFCMAKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
					model1.put("homePage", "nbfcMakerHome");

				} else if (Role.equals("NCHECKER")) {
					userId = (String) session.getAttribute("userId");
					model1.put("adminlist", userActivityService.getActivity(
							"NBFCCHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity(
							"NBFCCHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity(
							"NBFCCHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity(
							"NBFCCHECKER", "Receipt_Payments"));
					model1.put("repList",
							userActivityService.getReport("NBFCCHECKER", "User_Report"));
					model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
					model1.put("homePage", "nbfcCheckerHome");

				}
//				String Mem_Id ="";
//				if(!mliLongName.equals("ALL")){
//					mem_id = userActivityService.getBankID(mliLongName);
//					Mem_Id= mem_id.getBnkId() + mem_id.getBrnName()+ mem_id.getZneID() ;
//					System.out.println("Mem_Id FOR MLI WIse For Specific==="+Mem_Id);
//				}
//				else{
//					Mem_Id="ALL";	
//					System.out.println("Mem_Id FOR MLI WIse FOR ALL==="+Mem_Id);
//				}
				Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(bean.getToDate());
				Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean .getFromDate());
				session.setAttribute("Role", Role); 
				session.setAttribute("userId", userId); 
				session.setAttribute("fromDate", fromDate); 
				session.setAttribute("toDate", toDate); 
				//session.setAttribute("Mem_Id", Mem_Id); 
				if(Role.equals("CCHECKER") || Role.equals("CMAKER")){
					// rows = statutsWiseReportService.getMliWeise(Role, userId, fromDate, toDate, Mem_Id, request);
					rows = statutsWiseReportService.getGuaranteeWise(Role, userId, fromDate, toDate, request);
				}else{
					 //rows = statutsWiseReportService.getMliWeise(Role, userId, fromDate, toDate, Mem_Id,request);
					 rows = statutsWiseReportService.getGuaranteeWise(Role, userId, fromDate, toDate,request);
				 }
				if (!rows.isEmpty()) {
					model1.put("rows", rows);
				 } else {
					model1.put("message", "NO Data Found !!");
				} 
				return new ModelAndView("GuaranteeApprovalReportDetails", model1);
			}
		 
			@RequestMapping(value = "/GuaranteeWisebackButton", method = RequestMethod.POST)
			public ModelAndView GuaranteeWisebackButton( @ModelAttribute("command") NPADetailsBean bean, BindingResult result, HttpSession session, 
					Model model,HttpServletRequest request) throws JRException, ParseException {
				        Map<String, Object> model1 = new HashMap<String, Object>();
				        String Role = (String) session.getAttribute("uRole");
				if (result.hasErrors()) {
					if (Role.equals("CMAKER")) {
						model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
		                model1.put("repList", userActivityService.getReport( "CGTMSEMAKER", "User_Report"));
				        model1.put("homePage", "cgtmseMakerHome");
		             } else if (Role.equals("CCHECKER")) {
						model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
						model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
						model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
						model1.put("homePage", "cgtmseCheckerHome");
					 } else if (Role.equals("NMAKER")) {
						model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
						model1.put("repList", userActivityService.getReport( "NBFCMAKER", "User_Report"));
						model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
						model1.put("homePage", "nbfcMakerHome");

					} else if (Role.equals("NCHECKER")) {
						model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
						model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
						model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
						model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
						model1.put("repList", userActivityService.getReport( "NBFCCHECKER", "User_Report"));
						model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
						model1.put("homePage", "nbfcCheckerHome");

					}
					return new ModelAndView("GuaranteeApprovalReport", model1);
				}

				if (Role.equals("CMAKER")) {
					userId = "ADMIN";
					model1.put("guaranteelist", userActivityService.getActivity( "CGTMSEMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity( "CGTMSEMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity( "CGTMSEMAKER", "Receipt_Payments"));
		            model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
					model1.put("homePage", "cgtmseMakerHome");

				} else if (Role.equals("CCHECKER")) {
					userId = "ADMIN";
					model1.put("adminlist", userActivityService.getActivity( "CGTMSECHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity( "CGTMSECHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity( "CGTMSECHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity( "CGTMSECHECKER", "Receipt_Payments"));
					model1.put("CList", userActivityService.getReport("CGTMSECHECKER", "Claim_Lodgement"));
					model1.put("repList", userActivityService.getReport( "CGTMSECHECKER", "User_Report"));
					model1.put("homePage", "cgtmseCheckerHome");
				 } else if (Role.equals("NMAKER")) {
					userId = (String) session.getAttribute("userId");
					model1.put("adminlist", userActivityService.getActivity( "NBFCMAKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity( "NBFCMAKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity( "NBFCMAKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity( "NBFCMAKER", "Receipt_Payments"));
					model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
					model1.put("CList", userActivityService.getReport("NBFCMAKER", "Claim_Lodgement"));
					model1.put("homePage", "nbfcMakerHome");

				} else if (Role.equals("NCHECKER")) {
					userId = (String) session.getAttribute("userId");
					model1.put("adminlist", userActivityService.getActivity( "NBFCCHECKER", "System_Admin"));
					model1.put("guaranteelist", userActivityService.getActivity( "NBFCCHECKER", "Registration"));
					model1.put("applicationList", userActivityService.getActivity( "NBFCCHECKER", "Application_Processing"));
					model1.put("RPaymentList", userActivityService.getActivity( "NBFCCHECKER", "Receipt_Payments"));
					model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
					model1.put("CList", userActivityService.getReport("NBFCCHECKER", "Claim_Lodgement"));
					model1.put("homePage", "nbfcCheckerHome");

				}
				
				/*String gStatus=(String) session.getAttribute("status");
				String toDateF=(String) session.getAttribute("TDate");
				String fromDateF=(String) session.getAttribute("FDate");
				Date toDate = new SimpleDateFormat("dd/MM/yyyy") .parse(toDateF);
				Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDateF);
				rows = stateWiseReportService.getStateDetails(userId,Role,toDate, fromDate,gStatus);
				if (!rows.isEmpty()) {
					model1.put("rows", rows);
				} else {
					model1.put("message", "NO Data Found !!");
				}*/

				return new ModelAndView("GuaranteeApprovalReport", model1);
				// return null;
			}

	 
	 
	 
	 }
 
