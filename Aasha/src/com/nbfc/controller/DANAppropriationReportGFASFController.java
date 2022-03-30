package com.nbfc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.nbfc.bean.NPADetailsBean;
import com.nbfc.helper.DateFormate;
import com.nbfc.model.MLIName;
import com.nbfc.service.CGTMSECreateExposureLimitMakerService;
import com.nbfc.service.NPAService;
import com.nbfc.service.StateService;
import com.nbfc.service.StateWiseReportService;
import com.nbfc.service.StatutsWiseReportService;
import com.nbfc.service.UserActivityService;
import com.nbfc.validation.EmployeeValidator;

import net.sf.jasperreports.engine.JRException;
@Controller
public class DANAppropriationReportGFASFController {
	
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
	
	
	@RequestMapping(value = "/DANAppropriationReportGFASF", method = RequestMethod.GET)
	public ModelAndView DANAppropriationReportGFASF(
			@ModelAttribute("command") NPADetailsBean bean,
			BindingResult result, HttpSession session, Model model)
			throws JRException {
		Map<String, Object> model1 = new HashMap<String, Object>();
		String Role = (String) session.getAttribute("uRole");
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
			model1.put("homePage", "nbfcCheckerHome");

		}
		return new ModelAndView("DANAppropriationReportGFASF", model1);
		// return null;
	}
	
	
	@RequestMapping(value = "/DANAppropriationReportDetailGFASF", method = RequestMethod.POST)
	public ModelAndView DANGFReportDetailList(
			@ModelAttribute("command") NPADetailsBean bean,
			BindingResult result, HttpSession session, Model model)
			throws JRException, ParseException {
		Map<String, Object> model1 = new HashMap<String, Object>();
		List<NPADetailsBean> NPADetailList=null;
		List<Object[]> rows1=null;
		session.setAttribute("NPADetailList",null);
		session.removeAttribute("NPADetailList");
		String Role = (String) session.getAttribute("uRole");
		String danType=bean.getDanType();
		System.out.println("mliLongName==194==="+danType);
		validator.DanAppropriationReportDetailsValidate(bean, result);
	
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
			}

	

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
		session.setAttribute("dan_type", danType); 

		
		
		
			rows1=statutsWiseReportService.getDANAppropriationASFGFDetails(Role, userId, fromDate, toDate, danType);
			// rows = statutsWiseReportService.getDanASFDetail(Role, userId, fromDate, toDate, Mem_Id,Mem_Id);
		
		if (!rows1.isEmpty()) {
			model1.put("rows1", rows1);
		 } else {
			model1.put("message", "NO Data Found !!");
		} 
		return new ModelAndView("DANAppropriationReportGFASF", model1);
	} 
	 @RequestMapping(value = "/DANAppropriationGFASFReportDetailDownload", method = RequestMethod.GET)
		public ModelAndView DANAppropriationGFASFReportDetailDownload(
				@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
				HttpServletRequest request, HttpServletResponse response,
				HttpSession session) throws IOException {
			try {
             String ASFSummaryDetailsFileName1="DANAppropriationGFASFReportDetail.xlsx";
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
				XSSFSheet sheet = hwb.createSheet("DANAppropriationGFASFReportDetail");
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
				cell1.setCellValue("Name Of the NBFC");// Done 3

				XSSFCell cell2 = rowhead.createCell(2);
				cell2.setCellStyle(style);
				cell2.setCellValue("Demand Advice Number"); 

				XSSFCell cell3 = rowhead.createCell(3);
				cell3.setCellStyle(style);
				cell3.setCellValue("Guaranteed Amount"); 
				
				XSSFCell cell4 = rowhead.createCell(4);
				cell4.setCellStyle(style);
				cell4.setCellValue("Dan Amount"); 

				/*XSSFCell cell5 = rowhead.createCell(5);
				cell5.setCellStyle(style);
				cell5.setCellValue("Ssi Name"); */

				XSSFCell cell5 = rowhead.createCell(5);
				cell5.setCellStyle(style);
				cell5.setCellValue("Appropriation Status ( Y / N )"); 
				
				
			

				String Role= (String) session.getAttribute("Role"); 
				String userId=(String) session.getAttribute("userId"); 
				String fromDate=(String) session.getAttribute("fromDate");  
				String toDate= (String) session.getAttribute("toDate");  
				String Mem_Id= (String) session.getAttribute("Mem_Id"); 
				String ssiName= (String) session.getAttribute("ssiName"); 
				String danType= (String) session.getAttribute("dan_type"); 
			   //List<MliWiseReportDetailBean> report=statutsWiseReportService.getDanGfReportDowanload(Role, userId, fromDate, toDate, Mem_Id,ssiName);
				List<Object[]> report1=new ArrayList<Object[]>();
			

				
				
				
				report1=statutsWiseReportService.getDANAppropriationASFGFDetails(Role, userId, fromDate, toDate, danType);
				//report1=statutsWiseReportService.getDANAppropriationASFGFDetails(Role, userId, fromDate, toDate, Mem_Id, ssiName);
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
                    row.createCell((short) 5).setCellValue(obj1[4].toString()==null ? "":obj1[4].toString());
                     //row.createCell((short) 5).setCellValue(obj1[5].toString()==null ? "":obj1[5].toString());
                   
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
				e.printStackTrace();
				System.out.println(""+e.getMessage());
			}
			return null;
		}
	 
}
