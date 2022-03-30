package com.nbfc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.nbfc.bean.TenureModificationRepBean;
import com.nbfc.model.MLIName;
import com.nbfc.service.CGTMSECreateExposureLimitMakerService;
import com.nbfc.service.NPAService;
import com.nbfc.service.TenureModificationRepService;
import com.nbfc.service.UserActivityService;
import com.nbfc.validation.EmployeeValidator;

@Controller
public class TenureModificationRepController {

	@Autowired
	UserActivityService userActivityService;
	@Autowired
	NPAService npaService;
	@Autowired
	TenureModificationRepService tenureModificationService;
	@Autowired
	EmployeeValidator validator;
	@Autowired
	private CGTMSECreateExposureLimitMakerService cgtmseExposureMasterMakerService;
	@Autowired
	static Logger log = Logger.getLogger(TenureModificationRepController.class.getName());	

	@Value("${downloadFileDirPath}")
	private String downloadFileDirPath;
	@Value("${TenureModRepFile}")
	private String downloadFileName;

	public static final int BUFFER_SIZE = 4096;
	String memberId = null;
	String userId = null;
	MLIName mem_id = null;

	/*START Add by VinodSingh For Tenure Report on 12-DEC-2020*/
	@RequestMapping(value = "/TenureModificationReport", method = RequestMethod.GET)
	public ModelAndView TenureModificationReport(@ModelAttribute("command") TenureModificationRepBean bean, BindingResult result,HttpSession session, Model model) throws JRException {
		Map<String, Object> model1 = new HashMap<String, Object>();
		String Role = (String) session.getAttribute("uRole");
		userId = (String) session.getAttribute("userId");
		String loginUserMemId = npaService.getMemberId(userId);
		System.out.println("Role::"+Role+" ,userId::"+userId);
		if (Role.equals("CMAKER")) {
			model1.put("guaranteelist", userActivityService.getActivity("CGTMSEMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("CGTMSEMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("CGTMSEMAKER", "Receipt_Payments"));
			model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
			model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "cgtmseMakerHome");

		} else if (Role.equals("CCHECKER")) {
			model1.put("adminlist", userActivityService.getActivity("CGTMSECHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity("CGTMSECHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("CGTMSECHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("CGTMSECHECKER", "Receipt_Payments"));
			model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
			model1.put("repList", userActivityService.getReport("CGTMSECHECKER", "User_Report"));
			model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "cgtmseCheckerHome");
		} else if (Role.equals("NMAKER")) {
			model1.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
			model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "nbfcMakerHome");

		} else if (Role.equals("NCHECKER")) {
			model1.put("adminlist", userActivityService.getActivity("NBFCCHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity("NBFCCHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("NBFCCHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("NBFCCHECKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
			model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "nbfcCheckerHome");
		}
		return new ModelAndView("TenureModificationReport", model1);
	}

	/*Add by VinodSingh Fetch Data For Tenure Report on 12-DEC-2020*/
	@RequestMapping(value = "/TenureModRepData", method = RequestMethod.POST)
	public ModelAndView TenureModificationReportData(@ModelAttribute("command") TenureModificationRepBean bean,BindingResult result, HttpSession session, Model model) throws JRException, ParseException {
		Map<String, Object> model1 = new HashMap<String, Object>();
		List<TenureModificationRepBean> tenureModiReport = null;

		String Role = (String) session.getAttribute("uRole");
		String userId = (String) session.getAttribute("userId");
		memberId = npaService.getMemberId(userId);
		String mliLongName = bean.getBankName();
		System.out.println("mliLongName:"+mliLongName+" ,memberId:"+memberId);
		validator.TenureModificationReportValidate(bean, result);
		if (result.hasErrors()) {
			if (Role.equals("CMAKER")) {
				model1.put("guaranteelist", userActivityService.getActivity("CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity("CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity("CGTMSEMAKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
				model1.put("homePage", "cgtmseMakerHome");

			} else if (Role.equals("CCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity("CGTMSECHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity("CGTMSECHECKER", "Registration"));
				model1.put("applicationList",
						userActivityService.getActivity("CGTMSECHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity("CGTMSECHECKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport("CGTMSECHECKER", "User_Report"));
				model1.put("homePage", "cgtmseCheckerHome");
			} else if (Role.equals("NMAKER")) {
				model1.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcMakerHome");

			} else if (Role.equals("NCHECKER")) {
				model1.put("adminlist", userActivityService.getActivity("NBFCCHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity("NBFCCHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity("NBFCCHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity("NBFCCHECKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
				model1.put("homePage", "nbfcCheckerHome");
			}
			return new ModelAndView("TenureModificationReport", model1);
		}

		if (Role.equals("CMAKER")) {
			userId = "ADMIN";
			model1.put("guaranteelist", userActivityService.getActivity("CGTMSEMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("CGTMSEMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("CGTMSEMAKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
			model1.put("homePage", "cgtmseMakerHome");

		} else if (Role.equals("CCHECKER")) {
			userId = "ADMIN";
			model1.put("adminlist", userActivityService.getActivity("CGTMSECHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity("CGTMSECHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("CGTMSECHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("CGTMSECHECKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("CGTMSECHECKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
			model1.put("homePage", "cgtmseCheckerHome");
		} else if (Role.equals("NMAKER")) {
			userId = (String) session.getAttribute("userId");
			model1.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
			model1.put("homePage", "nbfcMakerHome");
		} else if (Role.equals("NCHECKER")) {
			userId = (String) session.getAttribute("userId");
			model1.put("adminlist", userActivityService.getActivity("NBFCCHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity("NBFCCHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("NBFCCHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("NBFCCHECKER", "Receipt_Payments"));
			model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
			model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
			model1.put("homePage", "nbfcCheckerHome");
		}

		Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getToDate());
		Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getFromDate());							
			
		if (Role.equals("CCHECKER") || Role.equals("CMAKER")) {
			if (mliLongName.equals("All")) {
				
				tenureModiReport = tenureModificationService.getTenureModificationReport(userId, toDate, fromDate, Role ,mliLongName.toUpperCase());
			} else {
				mem_id = userActivityService.getBankID(mliLongName);
				String Mem_Id = mem_id.getBnkId() + mem_id.getZneID() + mem_id.getBrnName();
				System.out.println("Mem_Id::"+Mem_Id);
				tenureModiReport = tenureModificationService.getTenureModificationReport(userId, toDate, fromDate, Role, Mem_Id);
			}
		} else {
			tenureModiReport = tenureModificationService.getTenureModificationReport(userId, toDate, fromDate, Role, memberId);;
		}
		
		
		if (!tenureModiReport.isEmpty()) {
			model1.put("tenureModiReport", tenureModiReport);
			session.setAttribute("tenureModiReport", tenureModiReport);
		} else {
			session.removeAttribute("tenureModiReport");
			model1.put("message", "NO Data Found !!");
		}
		return new ModelAndView("TenureModificationReportData", model1);
	}

	// Get the MLI Long Name in Drop Down on Page on Load
	@ModelAttribute("bankName")
	public Map<String, String> getMlilongName() {
		Map<String, String> mapMliLongNameObj = new HashMap<String, String>();
		mapMliLongNameObj = cgtmseExposureMasterMakerService.getMliLongNameInDropDown();
		return mapMliLongNameObj;
	}

	@RequestMapping(value = "/TenureModifyRepDownload", method = RequestMethod.GET)
	public ModelAndView NPAUpgradationReportDownload(@ModelAttribute("command") TenureModificationRepBean bean,BindingResult result, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		try {
			String filePath = downloadFileDirPath + File.separator + downloadFileName;
			System.out.println("File Path===" + filePath);
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
			List<TenureModificationRepBean> list = (List<TenureModificationRepBean>) session.getAttribute("tenureModiReport");
			log.info("list size==" + list.size());
			log.info("list Data==" + list);

			// Writing and Downlaoding Excel File
			System.out.println("Create Sheet=");
			XSSFWorkbook hwb = new XSSFWorkbook();
			XSSFSheet sheet = hwb.createSheet("TenureModiReportExcelFile");

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
			cell0.setCellValue("Bank Name");

			XSSFCell cell1 = rowhead.createCell(1);
			cell1.setCellStyle(style);
			cell1.setCellValue("MLIID");
			
			XSSFCell cell2 = rowhead.createCell(2);
			cell2.setCellStyle(style);
			cell2.setCellValue("MSE Name");

			XSSFCell cell3 = rowhead.createCell(3);
			cell3.setCellStyle(style);
			cell3.setCellValue("CGPAN");

			XSSFCell cell4 = rowhead.createCell(4);
			cell4.setCellStyle(style);
			cell4.setCellValue("Outstanding Amount");

			XSSFCell cell5 = rowhead.createCell(5);
			cell5.setCellStyle(style);
			cell5.setCellValue("Disbursement Date");

		//	XSSFCell cell6 = rowhead.createCell(6);
		//	cell6.setCellStyle(style);
		//	cell6.setCellValue("Old Tenure");

		//	XSSFCell cell7 = rowhead.createCell(7);
		//	cell7.setCellStyle(style);
		//	cell7.setCellValue("Old Expiry Date");

			XSSFCell cell8 = rowhead.createCell(6);
			cell8.setCellStyle(style);
			cell8.setCellValue("Revised Tenure(In months)");

			XSSFCell cell9 = rowhead.createCell(7);
			cell9.setCellStyle(style);
			cell9.setCellValue("Modification Remark");

			XSSFCell cell10 = rowhead.createCell(8);
			cell10.setCellStyle(style);
			cell10.setCellValue("Revised Expiry date");

			XSSFCell cell11 = rowhead.createCell(9);
			cell11.setCellStyle(style);
			cell11.setCellValue("Dan Amount");
			
			XSSFCell cell12 = rowhead.createCell(10);
			cell12.setCellStyle(style);
			cell12.setCellValue("Modification Date");

			int index = 1;
			int sno = 0;
			Iterator<TenureModificationRepBean> itr2 = list.iterator();
			while (itr2.hasNext()) {
				TenureModificationRepBean obj1 = (TenureModificationRepBean) itr2.next();
				sno++;
				XSSFRow row = sheet.createRow((short) index);
				
				row.createCell((short) 0).setCellValue(obj1.getBankName());

				row.createCell((short) 1).setCellValue(obj1.getMLIID());

				row.createCell((short) 2).setCellValue(obj1.getMse_name());

				row.createCell((short) 3).setCellValue(obj1.getCGPAN());

				row.createCell((short) 4).setCellValue(obj1.getGuarantee_amount());

				row.createCell((short) 5).setCellValue(obj1.getFirst_disbursement_date());

				/*	row.createCell((short) 6).setCellValue(obj1.getTenor_in_months());   

				row.createCell((short) 5).setCellValue(obj1.getOld_exp_date());*/

				row.createCell((short) 6).setCellValue(obj1.getRevised_tenure());

				row.createCell((short) 7).setCellValue(obj1.getRevised_expiry_date());

				row.createCell((short) 8).setCellValue(obj1.getModification_remarks());

				row.createCell((short) 9).setCellValue(obj1.getDan_amt());
				
				row.createCell((short) 10).setCellValue(obj1.getTenure_approval_date());

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
