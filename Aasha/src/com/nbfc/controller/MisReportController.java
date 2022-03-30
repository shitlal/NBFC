package com.nbfc.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.nbfc.bean.ClaimLodgementBean;
import com.nbfc.bean.MISReportbanforClaim;
import com.nbfc.bean.MisReportbean;
import com.nbfc.bean.NPADetailsBean;
import com.nbfc.bean.OtherMisRepor;
import com.nbfc.bean.PortfolioDetailsBean;
import com.nbfc.helper.PropertyLoader;
import com.nbfc.model.MLIName;
import com.nbfc.model.State;
import com.nbfc.service.CGTMSECreateExposureLimitMakerService;
import com.nbfc.service.MISService;
import com.nbfc.service.MISServiceImpl;
import com.nbfc.service.NPAService;
import com.nbfc.service.StateService;
import com.nbfc.service.UserActivityService;
import com.nbfc.validation.EmployeeValidator;

import net.sf.jasperreports.engine.JRException;

@Controller
public class MisReportController {

	@Autowired
	UserActivityService userActivityService;
	@Autowired
	MISService mISService;
	@Autowired
	EmployeeValidator validator;
	String memberId = null;
	String userId = null;
	@Autowired
	StateService stateService;
	@Value("${downloadFileDirPath}")
	private String downloadFileDirPath;
	@Value("${NPAReportdownloadFileName}")
	private String downloadFileName;
	MLIName mem_id = null;
	public static final int BUFFER_SIZE = 4096;
	@Autowired
	private CGTMSECreateExposureLimitMakerService cgtmseExposureMasterMakerService;
	@Autowired
	static Logger log = Logger.getLogger(NBFCController.class.getName());

	// add here the name of the NBFC_ACTIVITY_INFO_TEST Class name
	@RequestMapping(value = "/MISReportControllerMonthly", method = RequestMethod.GET)
	public ModelAndView cgpanDetailReport(@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
			HttpSession session, Model model) throws JRException {
		Map<String, Object> model1 = new HashMap<String, Object>();
		String Role = (String) session.getAttribute("uRole");
		userId = (String) session.getAttribute("userId");
		String loginUserMemId = mISService.getMemberId(userId);
		// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
		if (Role.equals("CMAKER")) {
			model1.put("guaranteelist", userActivityService.getActivity("CGTMSEMAKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("CGTMSEMAKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("CGTMSEMAKER", "Receipt_Payments"));
			model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
			model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
			model1.put("stateList", prepareStateListofBean(stateService.listStates("CCA")));
			// model1.put("actNameHome",
			// userActivityService.getActivityName("CGTMSEMAKER",
			// "cgpanDetailReport"));// Added by Say 31 Jan19
			model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "cgtmseMakerHome");

		} else if (Role.equals("CCHECKER")) {
			model1.put("adminlist", userActivityService.getActivity("CGTMSECHECKER", "System_Admin"));
			model1.put("guaranteelist", userActivityService.getActivity("CGTMSECHECKER", "Registration"));
			model1.put("applicationList", userActivityService.getActivity("CGTMSECHECKER", "Application_Processing"));
			model1.put("RPaymentList", userActivityService.getActivity("CGTMSECHECKER", "Receipt_Payments"));
			model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
			model1.put("repList", userActivityService.getReport("CGTMSECHECKER", "User_Report"));
			// model1.put("actNameHome",
			// userActivityService.getActivityName("CGTMSECHECKER",
			// "cgpanDetailReport"));// Added by Say 31 Jan19
			model1.put("MLIID", loginUserMemId);
			model1.put("homePage", "cgtmseCheckerHome");
			// return null;
		} else if (Role.equals("NMAKER")) {
			// added by say 6 feb-----------------------
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
		return new ModelAndView("MISReportInputForm", model1);
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

	
	
	@RequestMapping(value = "/MISReportInputForm", method = { RequestMethod.POST })
	public ModelAndView claimSettledReportExcel(@ModelAttribute("command") MisReportbean bean, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		//List<Map<String, Object>> TotalCases = new ArrayList<Map<String, Object>>();
		List<MisReportbean> TotalCases = new ArrayList<MisReportbean>();
		try {
			Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getToDate());
			Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getFromDate());
			TotalCases = mISService.getReportdataforMis(userId, toDate, fromDate);
			if (TotalCases != null) {
				session.setAttribute("MisReportbean", TotalCases);
			} else {
				System.out.println("No Data found from DB");

			}
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
			List<MisReportbean> list = (List<MisReportbean>) session.getAttribute("MisReportbean");
			String Role = (String) session.getAttribute("uRole");
			log.info("list size==" + list.size());
			log.info("list Data==" + list);
			// creating and Downlaoding Excel File
			System.out.println("Create Sheet=");
			Workbook hwb = new XSSFWorkbook();
			Sheet sheet = hwb.createSheet("GUARANTEEReportExcelFile");

			// Making bold and color to excel column heading
			//CellStyle style = hwb.createCellStyle();
			//--Font font = hwb.createFont();
			//--font.setFontHeightInPoints((short) 11);
			//font.setFontName(HSSFFont.FONT_ARIAL);
			//font.setBoldweight(HSSFFont.COLOR_NORMAL);
			//font.setBold(true);
			//font.setColor(HSSFColor.DARK_BLUE.index);
			//style.setFont(font);
		//	style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		//	style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			sheet.createFreezePane(0, 1); // Freeze 1st Row
											// sheet.createFreezePane(int
											// colSplit, int rowSplit, int
											// leftmostColumn, int topRow)

			// Creating First rows for excel heading
			Row rowhead = sheet.createRow((short) 0);
			int ronnn = 0;
			Cell cell0 = rowhead.createCell(ronnn);
			//cell0.setCellStyle(style);
			cell0.setCellValue("CGPAN");// Done 1
			ronnn++;

			Cell cell1 = rowhead.createCell(ronnn);
		//	cell1.setCellStyle(style);
			cell1.setCellValue("MLI_NAME");// Done 3
			ronnn++;

			Cell cell2 = rowhead.createCell(ronnn);
		//	cell2.setCellStyle(style);
			cell2.setCellValue("GUARANTEE_AMOUNT");// Done 4
			ronnn++;

			Cell cell3 = rowhead.createCell(ronnn);
			//cell3.setCellStyle(style);
			cell3.setCellValue("RANGE_DATA");// Done 5
			ronnn++;

			Cell cell112 = rowhead.createCell(ronnn);
			//cell112.setCellStyle(style);
			cell112.setCellValue("WOMEN");// Done 4
			ronnn++;

			Cell cell113 = rowhead.createCell(ronnn);
			//cell113.setCellStyle(style);
			cell113.setCellValue("STATE");// Done 5
			ronnn++;

			Cell cell4 = rowhead.createCell(ronnn);
			//cell4.setCellStyle(style);
			cell4.setCellValue("STATUS");// Done 6
			ronnn++;

			Cell cell5 = rowhead.createCell(ronnn);
			//cell5.setCellStyle(style);
			cell5.setCellValue("DISTRICT");// Done 7
			ronnn++;

			Cell cell6 = rowhead.createCell(ronnn);
			//cell6.setCellStyle(style);
			cell6.setCellValue("CITY");// Done 7
			ronnn++;

			Cell cell7 = rowhead.createCell(ronnn);
			//cell7.setCellStyle(style);
			cell7.setCellValue("CATEGORY");// Done 7
			ronnn++;

			Cell cell8 = rowhead.createCell(ronnn);
			//cell8.setCellStyle(style);
			cell8.setCellValue("PARTIAL_SECURITY_FLAG");//
			ronnn++;

			Cell cell9 = rowhead.createCell(ronnn);
			//cell9.setCellStyle(style);
			cell9.setCellValue("MICRO_SMALL");//
			ronnn++;

			Cell cell10 = rowhead.createCell(ronnn);
			//cell10.setCellStyle(style);
			cell10.setCellValue("INDUSTRY_SECTOR");//
			ronnn++;
			
			Cell cell11 = rowhead.createCell(ronnn);
			//cell10.setCellStyle(style);
			cell11.setCellValue("INDUSTRY_NATURE");//
			ronnn++;
			
			Cell cell12 = rowhead.createCell(ronnn);
			//cell10.setCellStyle(style);
			cell12.setCellValue("MSE_ADDRESS");//
			ronnn++;
			
			Cell cell13 = rowhead.createCell(ronnn);
			//cell10.setCellStyle(style);
			cell13.setCellValue("NPA_CREATED_MODIFIED_DT");//
			ronnn++;
			
			Cell cell14 = rowhead.createCell(ronnn);
			//cell10.setCellStyle(style);
			cell14.setCellValue("NPA_OUTSTANDING_AMT");//
			ronnn++;

			Cell cell15 = rowhead.createCell(ronnn);
			//cell10.setCellStyle(style);
			cell15.setCellValue("MINORITY_COMMUNITY");//
			ronnn++;

			Cell cell16 = rowhead.createCell(ronnn);
			//cell10.setCellStyle(style);
			cell16.setCellValue("RETAIL_TRADE");//
			ronnn++;
			
			Cell cell17 = rowhead.createCell(ronnn);
			//cell10.setCellStyle(style);
			cell17.setCellValue("CGTMSC_CHECKER_DATE");//
			ronnn++;
			
			int index = 1;
			int sno = 0;
			Iterator<MisReportbean> itr2 = list.iterator();
			
			while (itr2.hasNext()) {
				MisReportbean obj1 = (MisReportbean) itr2.next();
				sno++;
				Row row = sheet.createRow((int) index);
				//
				int row1 = 0;
				row.createCell((short) row1).setCellValue(obj1.getCgpan());// Done
				row1++; // 1
				row.createCell((short) row1).setCellValue(obj1.getMEM_BANK_NAME());// Done
				row1++; // 2
				row.createCell((short) row1).setCellValue(obj1.getGUARANTEE_AMOUNT());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getRANGE_DATA());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getWomen());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getState());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getStatus());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getDistrict());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getCity());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getCategory());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getPartialSecurityFlag());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getMicroAndSmall());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getINDUSTRY_SECTOR());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getINDUSTRY_NATURE());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getMSE_ADDRESS());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getNpaCreatedModDate());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getNpaOutstadAmt());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getMINORITY_COMMUNITY());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getRetail_trade());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getCgtmsc_checker_date());// Done
				index++;
				//System.gc();
			}
			FileOutputStream fileOut = new FileOutputStream(filePath);
			hwb.write(fileOut);
			fileOut.close();
			ModelAndView model = new ModelAndView("newRolePage");
			model.addObject("excelFileDownLoadSuccessfully",
					"File DownLoaded Successfully in this location F:/ExcelReports/nbfcClaimSettledExcel.csv");
			File downloadFile = new File(filePath);
			log.info("downloadFile =" + downloadFile);
			FileInputStream inputStream = new FileInputStream(downloadFile);
			response.setContentLength((int) downloadFile.length());
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);
			OutputStream outStream = response.getOutputStream();
			System.out.println("DDDDD==");
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			outStream.close();
			return model;
		} catch (Exception e) {
			log.info("Exception == " + e);
			System.out.println("Exception == " );
			e.printStackTrace();
			System.out.println("EXCEPTION =="+e.getMessage());
		}
		return null;
	}
	
	// second report for claim
	

	@RequestMapping(value = "/ClaimreportInputForm", method = { RequestMethod.POST })
	public ModelAndView ClaimReportFormReportExcel(@ModelAttribute("command") MISReportbanforClaim bean, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		//List<Map<String, Object>> TotalCases = new ArrayList<Map<String, Object>>();
		List<MISReportbanforClaim> ClaimReprotMIS = new ArrayList<MISReportbanforClaim>();
		try {
			Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getToDate());
			Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getFromDate());
			ClaimReprotMIS = mISService.getReportdataforClaimMis(userId, toDate, fromDate);
			if (ClaimReprotMIS != null) {
				session.setAttribute("MisReportbean", ClaimReprotMIS);
			} else {
				System.out.println("No Data found from DB");

			}
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
			List<MISReportbanforClaim> list = (List<MISReportbanforClaim>) session.getAttribute("MisReportbean");
			String Role = (String) session.getAttribute("uRole");
			log.info("list size==" + list.size());
			log.info("list Data==" + list);
			// creating and Downlaoding Excel File
			System.out.println("Create Sheet=");
			XSSFWorkbook hwb = new XSSFWorkbook();
			XSSFSheet sheet = hwb.createSheet("ClaimSettledReportExcelFile");

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
			int ronnn = 0;
			XSSFCell cell0 = rowhead.createCell(ronnn);
		cell0.setCellStyle(style);
			cell0.setCellValue("MLI_NAME");// Done 1
			ronnn++;

			XSSFCell cell1 = rowhead.createCell(ronnn);
			cell1.setCellStyle(style);
			cell1.setCellValue("STATE");// Done 3
			ronnn++;

			XSSFCell cell2 = rowhead.createCell(ronnn);
			cell2.setCellStyle(style);
			cell2.setCellValue("WOMEN");// Done 4
			ronnn++;

			XSSFCell cell3 = rowhead.createCell(ronnn);
			cell3.setCellStyle(style);
			cell3.setCellValue("CATEGORY");// Done 5
			ronnn++;

			XSSFCell cell112 = rowhead.createCell(ronnn);
			cell112.setCellStyle(style);
			cell112.setCellValue("INDUSTRY_SECTOR");// Done 4
			ronnn++;

			XSSFCell cell113 = rowhead.createCell(ronnn);
			cell113.setCellStyle(style);
			cell113.setCellValue("INDUSTRY_NATURE");// Done 5
			ronnn++;

			XSSFCell cell4 = rowhead.createCell(ronnn);
			cell4.setCellStyle(style);
			cell4.setCellValue("FIRST_INSTALLMENT_CLAIM");// Done 6
			ronnn++;

			XSSFCell cell5 = rowhead.createCell(ronnn);
			cell5.setCellStyle(style);
			cell5.setCellValue("FINAL_CLAIM_SETTELE_AMT");// Done 7
			ronnn++;

			XSSFCell cell6 = rowhead.createCell(ronnn);
			cell6.setCellStyle(style);
			cell6.setCellValue("CGPAN");// Done 7
			ronnn++;

			XSSFCell cell7 = rowhead.createCell(ronnn);
			cell7.setCellStyle(style);
			cell7.setCellValue("RANGE_OUTSTANDING_AMOUNT");// Done 7
			ronnn++;

			XSSFCell cell8 = rowhead.createCell(ronnn);
			cell8.setCellStyle(style);
			cell8.setCellValue("SUM(OUTSTANDING_AMOUNT)");//
			ronnn++;
			
			XSSFCell cell11 = rowhead.createCell(ronnn);
			cell11.setCellStyle(style);
			cell11.setCellValue("COUNT1");//
			ronnn++;

			XSSFCell cell9 = rowhead.createCell(ronnn);
			cell9.setCellStyle(style);
			cell9.setCellValue("RANGE_GUARANTEE_AMOUNT");//
			ronnn++;

			XSSFCell cell10 = rowhead.createCell(ronnn);
			cell10.setCellStyle(style);
			cell10.setCellValue("SUM(GUARANTEE_AMOUNT)");//
			ronnn++;
			
			XSSFCell cell12 = rowhead.createCell(ronnn);
			cell12.setCellStyle(style);
			cell12.setCellValue("COUNT2");//
			ronnn++;
			
			
			
			/*
			XSSFCell cell13 = rowhead.createCell(ronnn);
			//cell10.setCellStyle(style);
			cell10.setCellValue("NPA_CREATED_MODIFIED_DT");//
			ronnn++;
			
			XSSFCell cell14 = rowhead.createCell(ronnn);
			//cell10.setCellStyle(style);
			cell10.setCellValue("NPA_OUTSTANDING_AMT");//
			ronnn++;
*/
			int index = 1;
			int sno = 0;
			Iterator<MISReportbanforClaim> itr2 = list.iterator();
			
			while (itr2.hasNext()) {
				MISReportbanforClaim obj1 = (MISReportbanforClaim) itr2.next();
				sno++;
				XSSFRow row = sheet.createRow((int) index);
				//
				int row1 = 0;
				row.createCell((short) row1).setCellValue(obj1.getMEM_BANK_NAME());// Done
				row1++; // 1
				row.createCell((short) row1).setCellValue(obj1.getState());// Done
				row1++; // 2
				row.createCell((short) row1).setCellValue(obj1.getWomen());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getCategory());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getIndustrySector());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getIndustryNature());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getFirstInstallMentClaim());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getFirstClaimSettle());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getCgpan());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getRangeOutstandingAmount());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getSumOutstandingAmout());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getCount1());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getRangeGuaranteeAmount());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getSumGuaranteeAmount());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getCount2());// Done
				row1++;
				
				/*
				row.createCell((short) row1).setCellValue(obj1.getMSE_ADDRESS());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getNpaCreatedModDate());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getNpaOutstadAmt());// Done
				*/
				index++;
				//System.gc();
			}
			FileOutputStream fileOut = new FileOutputStream(filePath);
			hwb.write(fileOut);
			fileOut.close();
			ModelAndView model = new ModelAndView("newRolePage");
			model.addObject("excelFileDownLoadSuccessfully",
					"File DownLoaded Successfully in this location F:/ExcelReports/nbfcClaimSettledExcel.csv");
			File downloadFile = new File(filePath);
			log.info("downloadFile =" + downloadFile);
			FileInputStream inputStream = new FileInputStream(downloadFile);
			response.setContentLength((int) downloadFile.length());
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);
			OutputStream outStream = response.getOutputStream();
			System.out.println("DDDDD==");
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			outStream.close();
			return model;
		} catch (Exception e) {
			log.info("Exception == " + e);
			System.out.println("Exception == " );
			e.printStackTrace();
			System.out.println("EXCEPTION =="+e.getMessage());
		}
		return null;
	}
	
	// third report for Other Data 
	

	@RequestMapping(value = "/OtherMISInputForm", method = { RequestMethod.POST })
	public ModelAndView OtherMISInputFormReportExcel(@ModelAttribute("command") OtherMisRepor bean, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		//List<Map<String, Object>> TotalCases = new ArrayList<Map<String, Object>>();
		List<OtherMisRepor> TotalCases = new ArrayList<OtherMisRepor>();
		try {
			Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getToDate());
			Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getFromDate());
			TotalCases = mISService.getOverAllReportdataforClaimMis(userId, toDate, fromDate);
			if (TotalCases != null) {
				session.setAttribute("MisReportbean", TotalCases);
			} else {
				System.out.println("No Data found from DB");

			}
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
			List<OtherMisRepor> list = (List<OtherMisRepor>) session.getAttribute("MisReportbean");
			String Role = (String) session.getAttribute("uRole");
			log.info("list size==" + list.size());
			log.info("list Data==" + list);
			// creating and Downlaoding Excel File
			System.out.println("Create Sheet=");
			XSSFWorkbook hwb = new XSSFWorkbook();
			XSSFSheet sheet = hwb.createSheet("MISREportData");

			// Making bold and color to excel column heading
			//CellStyle style = hwb.createCellStyle();
			//--Font font = hwb.createFont();
			//--font.setFontHeightInPoints((short) 11);
			//font.setFontName(HSSFFont.FONT_ARIAL);
			//font.setBoldweight(HSSFFont.COLOR_NORMAL);
			//font.setBold(true);
			//font.setColor(HSSFColor.DARK_BLUE.index);
			//style.setFont(font);
		//	style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		//	style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			sheet.createFreezePane(0, 1); // Freeze 1st Row
											// sheet.createFreezePane(int
											// colSplit, int rowSplit, int
											// leftmostColumn, int topRow)

			// Creating First rows for excel heading
			XSSFRow rowhead = sheet.createRow((short) 0);
			int ronnn = 0;
			XSSFCell cell0 = rowhead.createCell(ronnn);
			//cell0.setCellStyle(style);
			cell0.setCellValue("MEM_BANK_NAME");// Done 1
			ronnn++;

			XSSFCell cell1 = rowhead.createCell(ronnn);
		//	cell1.setCellStyle(style);
			cell1.setCellValue("STATUS");// Done 3
			ronnn++;

			XSSFCell cell2 = rowhead.createCell(ronnn);
		//	cell2.setCellStyle(style);
			cell2.setCellValue("CGPAN");// Done 4
			ronnn++;

			XSSFCell cell3 = rowhead.createCell(ronnn);
			//cell3.setCellStyle(style);
			cell3.setCellValue("STATE");// Done 5
			ronnn++;

			XSSFCell cell112 = rowhead.createCell(ronnn);
			//cell112.setCellStyle(style);
			cell112.setCellValue("DISTRICT");// Done 4
			ronnn++;

			XSSFCell cell113 = rowhead.createCell(ronnn);
			//cell113.setCellStyle(style);
			cell113.setCellValue("CITY");// Done 5
			ronnn++;

			XSSFCell cell4 = rowhead.createCell(ronnn);
			//cell4.setCellStyle(style);
			cell4.setCellValue("WOMEN");// Done 6
			ronnn++;

			XSSFCell cell5 = rowhead.createCell(ronnn);
			//cell5.setCellStyle(style);
			cell5.setCellValue("CATEGORY");// Done 7
			ronnn++;

			XSSFCell cell6 = rowhead.createCell(ronnn);
			//cell6.setCellStyle(style);
			cell6.setCellValue("INDUSTRY_SECTOR");// Done 7
			ronnn++;

			XSSFCell cell7 = rowhead.createCell(ronnn);
			//cell7.setCellStyle(style);
			cell7.setCellValue("INDUSTRY_NATURE");// Done 7
			ronnn++;

			XSSFCell cell8 = rowhead.createCell(ronnn);
			//cell8.setCellStyle(style);
			cell8.setCellValue("MSE_ADDRESS");//
			ronnn++;

			XSSFCell cell9 = rowhead.createCell(ronnn);
			//cell9.setCellStyle(style);
			cell9.setCellValue("RANGE_DATA");//
			ronnn++;

			XSSFCell cell10 = rowhead.createCell(ronnn);
			//cell10.setCellStyle(style);
			cell10.setCellValue("SUM(GUARANTEE_AMOUNT)");//
			ronnn++;
			int index = 1;
			int sno = 0;
			Iterator<OtherMisRepor> itr2 = list.iterator();
			
			while (itr2.hasNext()) {
				OtherMisRepor obj1 = (OtherMisRepor) itr2.next();
				sno++;
				XSSFRow row = sheet.createRow((int) index);
				//
				int row1 = 0;
				row.createCell((short) row1).setCellValue(obj1.getMSE_BANK_NAME());// Done
				row1++; // 1
				row.createCell((short) row1).setCellValue(obj1.getStatus());// Done
				row1++; // 2
				row.createCell((short) row1).setCellValue(obj1.getCgpan());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getState());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getDistrict());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getCity());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getWomen());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getCategory());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getIndustrySector());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getIndustry_nature());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getMse_address());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getRange_data());// Done
				row1++;
				row.createCell((short) row1).setCellValue(obj1.getSumGuaranteeAmount());// Done
				index++;
				//System.gc();
			}
			FileOutputStream fileOut = new FileOutputStream(filePath);
			hwb.write(fileOut);
			fileOut.close();
			ModelAndView model = new ModelAndView("newRolePage");
			model.addObject("excelFileDownLoadSuccessfully",
					"File DownLoaded Successfully in this location F:/ExcelReports/nbfcClaimSettledExcel.xls.");
			File downloadFile = new File(filePath);
			log.info("downloadFile =" + downloadFile);
			FileInputStream inputStream = new FileInputStream(downloadFile);
			response.setContentLength((int) downloadFile.length());
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);
			OutputStream outStream = response.getOutputStream();
			System.out.println("Output==");
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			outStream.close();
			return model;
		} catch (Exception e) {
			log.info("Exception == " + e);
			System.out.println("Exception == " );
			e.printStackTrace();
			System.out.println("EXCEPTION =="+e.getMessage());
		}
		return null;
	}
	
	
	
	

}
