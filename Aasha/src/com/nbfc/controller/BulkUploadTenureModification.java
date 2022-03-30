package com.nbfc.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.nbfc.bean.MLIMakerInterfaceUploadedBean;
import com.nbfc.bean.TenureBulkUploadBean;
import com.nbfc.helper.ExcelDeatailsBean;
import com.nbfc.helper.FileExportHelper;
import com.nbfc.helper.NPABulkUpload;
import com.nbfc.helper.TableDetailBean;
import com.nbfc.helper.TenureBulkUpload;
import com.nbfc.model.UserInfo;
import com.nbfc.service.TenureBulkUploadService;
import com.nbfc.service.UserActivityService;

@Controller
public class BulkUploadTenureModification {

	@Autowired
	UserActivityService userActivityService;
	@Autowired
	TenureBulkUploadService TenureBulkUploadService;
	@Autowired
	SessionFactory sessionFactory;
	String memberId = null;

	@RequestMapping(value = "/BulkUploadTenureModification", method = RequestMethod.GET)
	public ModelAndView FirstRedirectPageforBulkTenureModification(@ModelAttribute("command") TenureBulkUploadBean bean,
			BindingResult result, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		System.out.println("The login id is" + userId);
		Map<String, Object> Tenure_model = new HashMap<String, Object>();
		List<TenureBulkUploadBean> TenureBulkUploadBean = null;
		if (userId == null) {
			return new ModelAndView("redirect:/nbfcLogin.html");
		}
		Tenure_model.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
		Tenure_model.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
		Tenure_model.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
		Tenure_model.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
		Tenure_model.put("actName", userActivityService.getActivityName("NBFCMAKER", "NPADetails"));// Added by Say 31
																									// Jan19
		Tenure_model.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
		Tenure_model.put("GMaintainlist", userActivityService.getActivity("NBFCMAKER", "Guarantee_Maintenance"));
		Tenure_model.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
		Tenure_model.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
		Tenure_model.put("memberId", memberId);
		Tenure_model.put("homePage", "nbfcMakerHome");
		return new ModelAndView("BulkUploadinputForm", Tenure_model);
	}

	@RequestMapping(value = "/TenureBulkUploadProcess", method = RequestMethod.POST)
	public ModelAndView FinalPageSubmitDataBulkTenure(@ModelAttribute("command") TenureBulkUploadBean bean,
			BindingResult result, HttpServletRequest request, HttpSession session) {
		Map<String, Object> Tenure_model = new HashMap<String, Object>();
		try {
			String fileName = bean.getFile().getOriginalFilename();
			if (!fileName.endsWith(".xls")) {
				System.out.println("The input file Name is" + fileName);
				Tenure_model.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
				Tenure_model.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
				Tenure_model.put("applicationList",
						userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
				Tenure_model.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
				Tenure_model.put("actName", userActivityService.getActivityName("NBFCMAKER", "NPADetails"));// Added by
																											// Say 31
																											// Jan19
				Tenure_model.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
				Tenure_model.put("GMaintainlist",
						userActivityService.getActivity("NBFCMAKER", "Guarantee_Maintenance"));
				Tenure_model.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
				Tenure_model.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
				Tenure_model.put("memberId", memberId);
				Tenure_model.put("homePage", "nbfcMakerHome");
				Tenure_model.put("message", "Batch File Should be .xls");
				return new ModelAndView("BulkUploadinputForm", Tenure_model);

			}
			// excel logic part here added by shashi -17-11-2020\

			UserInfo user = (UserInfo) session.getAttribute("userInfo");
			Session connSess = sessionFactory.openSession();
			Transaction tn = connSess.beginTransaction();
			Connection conn = connSess.connection();
			System.out.println("conn :" + conn);

			TenureBulkUpload TenureBulkUpload = new TenureBulkUpload();
			// String TableName="NBFC_BULK_TENURE_UPLOAD_DETAIL";
			String TableName = "NBFC_TENURE_UPLOAD_STAG";
			// String TableName="NBFC_BULK_TENURE_UPLOAD_DETAIL";
			// String TableName="NBFC_NPA_UPLOAD_DETAIL";
			String BulkName = "Tenure";
			LinkedHashMap<String, ExcelDeatailsBean> headerMap;
			// add logic here for table header.
			headerMap = TenureBulkUpload.getTableHeaderData(conn, TableName, BulkName);
			// ADDED FOR MAIN VALIDATION
			HashMap<String, ArrayList<String>> UploadedStatus = new HashMap<String, ArrayList<String>>();
			UploadedStatus = TenureBulkUpload.CheckExcelData(bean.getFile(), headerMap, TableName, conn, user, BulkName,
					conn);
			// check status here
			if (UploadedStatus != null) {
				ArrayList SuccessDataList = (ArrayList) UploadedStatus.get("successRecord");
				ArrayList UnSuccessDataList = (ArrayList) UploadedStatus.get("unsuccessRecord");
				ArrayList allerrors = (ArrayList) UploadedStatus.get("allerror");/**/
				System.out.println("allerrors:" + allerrors);
				System.out.println("UnSuccessDataList:" + UnSuccessDataList);
				System.out.println("SuccessDataList:" + SuccessDataList);
				HttpSession sess = request.getSession(false);
				request.setAttribute("UploadedStatus", UploadedStatus);
				sess.setAttribute("SuccessDataList", SuccessDataList);
				sess.setAttribute("UnSuccessDataList", UnSuccessDataList);
				sess.setAttribute("Allerrors", allerrors);
			}
			Tenure_model.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
			Tenure_model.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
			Tenure_model.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
			Tenure_model.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
			Tenure_model.put("actName", userActivityService.getActivityName("NBFCMAKER", "NPADetails"));
			Tenure_model.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
			Tenure_model.put("GMaintainlist", userActivityService.getActivity("NBFCMAKER", "Guarantee_Maintenance"));
			Tenure_model.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
			Tenure_model.put("CBMFList", userActivityService.getActivity("NBFCMAKER", "Claim_Bank_Mandate"));
			Tenure_model.put("homePage", "nbfcMakerHome");

		} catch (Exception ex) {
			ex.printStackTrace();
			ex.getMessage();
		}

		// add return page
		return new ModelAndView("BulkUploadedData", Tenure_model);

	}
	
	@RequestMapping(value = "exportToTenureFile", method = RequestMethod.GET)
	public ModelAndView exportToNpaFile(
			@ModelAttribute("command") MLIMakerInterfaceUploadedBean excelFile,
			BindingResult result, ModelMap modelMap, Model modFr,
			HttpServletRequest request,HttpServletResponse response, Model modelMsg, HttpSession session) throws IOException {
		//System.out.println("exportToFile.............START");
		
		//OutputStream os = response.getOutputStream();
		Map<String, Object> model = new HashMap<String, Object>();
    	Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String strDate = sdf.format(cal.getTime());		
    	System.out.println("ExportToFile Calling..");
		
    	//String contextPath1 = request.getSession(false).getServletContext().getRealPath("");
    	//String contextPath1 = request.getSession(false).getServletContext().getRealPath("");
    	//String contextPath1 = "C:\\Tenure_Model\\";
    	String contextPath1 = "C://NBFCDownload";
    	
    	
    	System.out.println("exportToFile@contextPath1 :"+contextPath1);
        String contextPath = contextPath1.replace('\\','/');
        System.out.println("exportToFile@contextPath1 :"+contextPath1);
        //System.out.println("contextPath1 :"+contextPath1);
        //System.out.println("contextPath :"+contextPath);
    	HttpSession sess = request.getSession(false);		
    	String fileType = request.getParameter("fileType");
    	String FlowLevel = request.getParameter("FlowLevel");    	
    	System.out.println("@@@@@@@@@@@@FlowLevel :"+FlowLevel);		
		//ArrayList ClmDataList = (ArrayList)sess.getAttribute("ClaimSettledDatalist");
		
		ArrayList ClmDataList = (ArrayList)sess.getAttribute(FlowLevel);
		
		//System.out.println("@@@@@@@@@@@@ClmDataList:"+ClmDataList);
		ArrayList HeaderArrLst = (ArrayList)ClmDataList.get(0);
		//System.out.println("@@@@@@@@@@@@HeaderArrLst:"+HeaderArrLst);
		int NoColumn = HeaderArrLst.size();		
		System.out.println("fileType:"+fileType);
		
		
		
		if(fileType.equals("XLSType")){
			System.out.println("####################inside if#####################");
			FileExportHelper fileExportHelper = new FileExportHelper();
			byte[] b = fileExportHelper.generateEXL(ClmDataList,NoColumn,contextPath);			
			if (response != null)
			    response.setContentType("APPLICATION/OCTET-STREAM");
			    response.setHeader("Content-Disposition","attachment; filename=ExcelData"+strDate+".xls");
			    OutputStream os = response.getOutputStream();
	            os.write(b);
	            //os.flush();			
	            os.close();
		}		 /**/
		//System.out.println("exportToFile.............END");
		return null;
    }

}
