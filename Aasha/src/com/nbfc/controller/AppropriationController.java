package com.nbfc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.ModelAndView;

import com.nbfc.bean.AppropriationBean;
import com.nbfc.model.MLIName;
import com.nbfc.service.AppropriationService;
import com.nbfc.service.CGTMSECreateExposureLimitMakerService;
import com.nbfc.service.NPAService;
import com.nbfc.service.UserActivityService;
import com.nbfc.validation.EmployeeValidator;

@Controller
public class AppropriationController {

	@Autowired
	UserActivityService userActivityService;
	@Autowired
	NPAService npaService;
	@Autowired
	AppropriationService appropriationService;
	@Autowired
	EmployeeValidator validator;
	@Autowired
	private CGTMSECreateExposureLimitMakerService cgtmseExposureMasterMakerService;
	@Autowired
	static Logger log = Logger.getLogger(AppropriationController.class.getName());	

	@Value("${downloadFileDirPath}")
	private String downloadFileDirPath;
	@Value("${TenureModRepFile}")
	private String downloadFileName;

	public static final int BUFFER_SIZE = 4096;
	String memberId = null;
	String userId = null;
	MLIName mem_id = null;

	/*START Add by VinodSingh For Appropriation on 28 Feb-2021*/
	@RequestMapping(value = "/getAppropriation", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getGFASFAppropriation(@ModelAttribute("command") AppropriationBean bean, BindingResult result,HttpSession session, Model model,HttpServletRequest request) throws JRException {
		Map<String, Object> model1 = new HashMap<String, Object>();
		String Role = (String) session.getAttribute("uRole");
		userId = (String) session.getAttribute("userId");
		HttpSession LoginSession=request.getSession(false);
		if(LoginSession ==null) {
				return new ModelAndView("redirect:/nbfcLogin.html");
		}
		String loginUserMemId = npaService.getMemberId(userId);
		System.out.println("Role::"+Role+" ,userId::"+userId);
		model1.put("shbutton", "hide");
		session.setAttribute("shbutton", "hide");
		//Nullpointer issue handled by Shital on 27-Dec-2021 
		if (Role != null && Role.equals("CMAKER")) {
			model1.put("guaranteelist", userActivityService.getActivity("CGTMSEMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("CGTMSEMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("CGTMSEMAKER", "Receipt_Payments"));
			model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
			model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "cgtmseMakerHome");

		} 
		return new ModelAndView("Appropriation", model1);
	}

	/*Add by VinodSingh Fetch Data For Tenure Report on 12-DEC-2020*/
	@RequestMapping(value = "/getDataForEdit", method ={ RequestMethod.POST, RequestMethod.GET})
	public ModelAndView editAppropriationData(@ModelAttribute("command") AppropriationBean bean,BindingResult result, HttpSession session, Model model,HttpServletRequest request) throws JRException, ParseException {
		Map<String, Object> model1 = new HashMap<String, Object>();
		List<AppropriationBean> appropriationList = null;
		String Role = (String) session.getAttribute("uRole");
		String userId = (String) session.getAttribute("userId");
		HttpSession LoginSession=request.getSession(false);
		if(LoginSession ==null || userId==null) {
				return new ModelAndView("redirect:/nbfcLogin.html");
		}

		memberId = npaService.getMemberId(userId);
		
//		session.setAttribute("paymentStatus", paymentStatus);
//		session.setAttribute("Role", Role);
//		session.setAttribute("mliLongName", mliLongName);
		
		String mliLongName = bean.getBankName() != null ? bean.getBankName() : (session.getAttribute("mliLongName") != null ?session.getAttribute("mliLongName").toString() : "" );
		String paymentStatus = bean.getPaymentStatus() != null ? bean.getPaymentStatus() : (session.getAttribute("paymentStatus") != null ?session.getAttribute("paymentStatus").toString() : "" );
		System.out.println("mliLongName:"+mliLongName+" ,memberId:"+memberId+" ,paymentStatus::"+paymentStatus);
		
		Date toDate =  null;
		Date fromDate = null;
		if(bean.getToDate() == null ){
			bean.setToDate(session.getAttribute("toDate") != null ? session.getAttribute("toDate").toString() : null);
		}
		if(bean.getFromDate() == null ){
			bean.setFromDate(session.getAttribute("fromDate") != null ? session.getAttribute("fromDate").toString() : null);
		}
		validator.appropriationDateValidate(bean, result);
		if (result.hasErrors()) {
			if (Role.equals("CMAKER")) {
				model1.put("guaranteelist", userActivityService.getActivity("CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity("CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity("CGTMSEMAKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
				model1.put("homePage","cgtmseMakerHome");

			} 
			return new ModelAndView("Appropriation", model1);
		}

		if (Role.equals("CMAKER")) {
			userId = "ADMIN";
			model1.put("guaranteelist", userActivityService.getActivity("CGTMSEMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("CGTMSEMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("CGTMSEMAKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
			model1.put("homePage", "cgtmseMakerHome");
		} 

		if(bean.getToDate() != null ){
			toDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getToDate());
		}
		if(bean.getFromDate() != null ){
			fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getFromDate());
		}
		
			System.out.println("Status:"+paymentStatus+", mliLongName::"+mliLongName);
		if (Role.equals("CMAKER") ) {
			appropriationList=new ArrayList<AppropriationBean>();
			appropriationList = appropriationService.getAppropriationDetails(paymentStatus, toDate, fromDate, Role, mliLongName);
		}		
		
		if (null != appropriationList && !appropriationList.isEmpty()) {
			model1.put("appropriationList", appropriationList);
			model1.put("shbutton", "show");
			session.setAttribute("shbutton", "show");
		} else {
			model1.put("shbutton", "hide");
			session.setAttribute("shbutton", "hide");
			model1.put("message", "NO Data Found !!");
		}
		return new ModelAndView("Appropriation", model1);
	}

	// Get the MLI Long Name in Drop Down on Page on Load
	@ModelAttribute("bankName")
	public Map<String, String> getMlilongName() {
		Map<String, String> mapMliLongNameObj = new HashMap<String, String>();
		mapMliLongNameObj = cgtmseExposureMasterMakerService.getMliLongNameInDropDown();
		return mapMliLongNameObj;
	}

	
	
	@RequestMapping(value = "/updatePaymentStatus", method = RequestMethod.POST)
	public ModelAndView updatePaymentStatus(@ModelAttribute("command") AppropriationBean bean,BindingResult result, HttpSession session, Model model,HttpServletRequest request) throws JRException, ParseException {
		Map<String, Object> model1 = new HashMap<String, Object>();
		String Role = (String) session.getAttribute("uRole");
		String userId = (String) session.getAttribute("userId");
		HttpSession LoginSession=request.getSession(false);
		if(LoginSession ==null || userId==null) {
				return new ModelAndView("redirect:/nbfcLogin.html");
		}
		memberId = npaService.getMemberId(userId);
		String mliLongName = bean.getBankName();
		String paymentStatus = bean.getPaymentStatus();
		System.out.println("mliLongName:"+mliLongName+" ,memberId:"+memberId+" ,paymentStatus::"+paymentStatus);
		
		validator.appropriationDateValidate(bean, result);
		if (result.hasErrors()) {
			if (Role.equals("CMAKER")) {
				model1.put("guaranteelist", userActivityService.getActivity("CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity("CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity("CGTMSEMAKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
				model1.put("homePage", "cgtmseMakerHome");

			} 
			return new ModelAndView("Appropriation", model1);
//			return "redirect";
		}

		if (Role.equals("CMAKER")) {
			userId = "ADMIN";
			model1.put("guaranteelist", userActivityService.getActivity("CGTMSEMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("CGTMSEMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("CGTMSEMAKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
			model1.put("homePage", "cgtmseMakerHome");

		} 

		Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getToDate());
		Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getFromDate());							
			
		String message =  "";
		if (Role.equals("CMAKER") ) {			
			message = appropriationService.updatePaymentStatus(bean);
			if(message.trim().equalsIgnoreCase("Y")){
				model1.put("message", "Record updated..");
			}else if(!message.trim().equalsIgnoreCase("Y")){
				model1.put("message", "Record updatedation fail..");
			}
		}
		/*
		List<String> instrumentNo =null;
		 List<Integer> instrumentAmount =null;
		 List<String> instrumentDate =null;
		List<AppropriationBean> dataList = null;
		List<AppropriationBean> dataList2 = new ArrayList<AppropriationBean>();
		if (Role.equals("CMAKER") ) {
			dataList=new ArrayList<AppropriationBean>();
			dataList = appropriationService.getAppropriationDetails(paymentStatus, toDate, fromDate, Role, mliLongName);;
			System.out.println("==========>>>>>>"+dataList.size());
			for (AppropriationBean apbean : dataList) {
				System.out.println("date::"+apbean.getInstrumentDate()+",Amount:: "+apbean.getInstrumentAmount()+" ,"+apbean.getInstrumentNo());
				AppropriationBean beanData = new AppropriationBean();
				instrumentNo=new ArrayList<String>();
				instrumentAmount=new ArrayList<Integer>();
				instrumentDate=new ArrayList<String>();
				apbean.setChcktbl(null);
				apbean.setInstrumentDate(null);
				apbean.setInstrumentAmount(null);
				apbean.setInstrumentNo(null);
				dataList2.add(apbean);
			}			
			System.out.println(dataList.get(0).getInstrumentAmount());
		}
		
		if (!dataList.isEmpty() && !dataList2.isEmpty()) {
			model1.put("appropriationList", dataList2);			
			session.setAttribute("shbutton", "show");
		} else{
			model1.put("shbutton", "hide");
			session.setAttribute("shbutton", "hide");
		}
		*/
//paymentStatus, toDate, fromDate, Role, mliLongName
		session.setAttribute("paymentStatus", paymentStatus);
		session.setAttribute("toDate", bean.getToDate());
		session.setAttribute("fromDate", bean.getFromDate());
//		session.setAttribute("Role", Role);
		session.setAttribute("mliLongName", mliLongName);
//		return new ModelAndView("Appropriation", model1);
		return new ModelAndView("redirect:getDataForEdit.html", model1);
//		return new ModelAndView("forward:getDataForEdit.html", model1);
//		return "redirect:getDataForEdit.html";
		//return "forward:/newpage";
	}


	
	
	@RequestMapping(value = "/AppropriationDownload", method = RequestMethod.POST)
	public ModelAndView appropriationDownload(@ModelAttribute("command") AppropriationBean bean,BindingResult result, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		try {
			String Role = (String) session.getAttribute("uRole");
			String userId = (String) session.getAttribute("userId");
			memberId = npaService.getMemberId(userId);
			String mliLongName = bean.getBankName();
			String paymentStatus = bean.getPaymentStatus();
			Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getToDate());
			Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getFromDate());
			
			String filePath = downloadFileDirPath + File.separator + downloadFileName;
			System.out.println("Appropriation File Path===" + filePath);
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
						
			List<AppropriationBean> list = null;
			if (Role.equals("CMAKER") ) {
				list=new ArrayList<AppropriationBean>();
				list = appropriationService.getAppropriationDetails(paymentStatus, toDate, fromDate, Role, mliLongName);;
			}
			if (null != list && !list.isEmpty()) {
				session.setAttribute("shbutton", "show");
			} else {				
				session.setAttribute("shbutton", "hide");
			}
			log.info("list size==" + list.size()+"  "+"list Data==" + list);

			// Writing and Downlaoding Excel File
			System.out.println("Create Sheet=");
			XSSFWorkbook hwb = new XSSFWorkbook();
			XSSFSheet sheet = hwb.createSheet("appropriationListExcelFile");

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
			sheet.createFreezePane(0, 1);			

			// Creating First rows for excel heading
			XSSFRow rowhead = sheet.createRow((short) 0);
			
			XSSFCell cell0 = rowhead.createCell(0);
			cell0.setCellStyle(style);
			cell0.setCellValue("MEMBER ID");

			XSSFCell cell1 = rowhead.createCell(1);
			cell1.setCellStyle(style);
			cell1.setCellValue("NAME OF NBFC");
			
			XSSFCell cell2 = rowhead.createCell(2);
			cell2.setCellStyle(style);
			cell2.setCellValue("PAYMENT ID");

			XSSFCell cell3 = rowhead.createCell(3);
			cell3.setCellStyle(style);
			cell3.setCellValue("INWARD AMOUNT IN RS.");

			XSSFCell cell4 = rowhead.createCell(4);
			cell4.setCellStyle(style);
			cell4.setCellValue("ALOCATED AMOUNT IN RS.");

			XSSFCell cell5 = rowhead.createCell(5);
			cell5.setCellStyle(style);
			cell5.setCellValue("INSTRUMENT NO");

			XSSFCell cell6 = rowhead.createCell(6);
			cell6.setCellStyle(style);
			cell6.setCellValue("INSTRUMENT AMOUNT IN RS.)");

			XSSFCell cell7 = rowhead.createCell(7);
			cell7.setCellStyle(style);
			cell7.setCellValue("INSTRUMENT DATE ");

			XSSFCell cell8 = rowhead.createCell(8);
			cell8.setCellStyle(style);
			cell8.setCellValue("Payment Status");
			
			int index = 1;
			int sno = 0;
			Iterator<AppropriationBean> itr2 = list.iterator();
			while (itr2.hasNext()) {
				AppropriationBean obj1 = (AppropriationBean) itr2.next();
				sno++;
				XSSFRow row = sheet.createRow((short) index);
				
				row.createCell((short) 0).setCellValue(obj1.getMemberId());

				row.createCell((short) 1).setCellValue(obj1.getMemberName());
					
				row.createCell((short) 2).setCellValue(obj1.getRpNumber());

				row.createCell((short) 3).setCellValue(obj1.getInwardAmount());

				row.createCell((short) 4).setCellValue(obj1.getInwardAmount());

				row.createCell((short) 5).setCellValue(obj1.getVirtualAccountNumber());

				row.createCell((short) 6).setCellValue(obj1.getInwardAmount());   

				row.createCell((short) 7).setCellValue(obj1.getPayment_date());

				row.createCell((short) 8).setCellValue(obj1.getStatus());
				index++;
			}
			FileOutputStream fileOut = new FileOutputStream(filePath);
			hwb.write(fileOut);
			fileOut.close();

			ModelAndView model = new ModelAndView("newRolePage");
		//	model.addObject("excelFileDownLoadSuccessfully","File DownLoaded Successfully in this location F:/ExcelReports/TenureModificationReportFile.xls.");
			model.addObject("excelFileDownLoadSuccessfully","File DownLoaded Successfully in this location C:/ExcelReports/TenureModificationReportFile.xls.");

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
			System.out.println("DDDDD==");
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;

			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			outStream.close();
			return null;
		} catch (Exception e) {
			log.info("Exception == " + e);
			System.out.println("Exception == " + e);
		}
		return null;
	}		
	/*END Add by VinodSingh For Tenure Report on 12-DEC-2020*/
}
