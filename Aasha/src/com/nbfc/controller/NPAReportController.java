package com.nbfc.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import net.sf.jasperreports.engine.JRException;

//import org.apache.commons.math.util.MathUtils;
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
//import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.nbfc.bean.ApplicationStatusDetailsBean;
import com.nbfc.bean.NPADetailsBean;
import com.nbfc.bean.StateBean;
import com.nbfc.helper.PropertyLoader;
import com.nbfc.model.MLIName;
import com.nbfc.model.MLIRegistration;
import com.nbfc.model.State;
import com.nbfc.model.User;
import com.nbfc.service.CGTMSECreateExposureLimitMakerService;
import com.nbfc.service.NPAService;
import com.nbfc.service.StateService;
import com.nbfc.service.UserActivityService;
import com.nbfc.validation.EmployeeValidator;

/**
 * @author ajeet
 *
 */
@Controller
public class NPAReportController {

	@Autowired
	UserActivityService userActivityService;
	@Autowired
	NPAService npaService;
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

	// this method call for NPA Report Detail Page
	@RequestMapping(value = "/NPAReport", method = RequestMethod.GET)
	public ModelAndView cgpanDetailReport(@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
			HttpSession session, Model model) throws JRException {
		Map<String, Object> model1 = new HashMap<String, Object>();
		String Role = (String) session.getAttribute("uRole");
		userId = (String) session.getAttribute("userId");
		String loginUserMemId = npaService.getMemberId(userId);
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
		return new ModelAndView("npaReportInputForm", model1);
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

//	public static void GetDouble(double m) {
//		double sum1 = 0;
//		for (int i = 0; i < m.size(); i++)
//			sum1 += m.get(i);
//		return sum1;
//
//	}

	// this method call for NPA Report Detail
	@RequestMapping(value = "/npaReportDetailList", method = RequestMethod.POST)
	public ModelAndView searchAppStatus(@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
			HttpSession session, Model model) throws JRException, ParseException {
		Map<String, Object> model1 = new HashMap<String, Object>();
		List<Double> Latest_oS_amount = new ArrayList<Double>();
		List<Double> Total_Outstanding_Amount = new ArrayList<Double>();
		List<Double> Total_Guaranteed_Amount = new ArrayList<Double>();
		List<Map<String, Object>> NPADetailList = new ArrayList<Map<String, Object>>();
		// List<NPADetailsBean> NPADetailList = null;
		// declaring variable
		String TotalGuaranteedAmountStr = null;
		String TotalOutstandingAmountStr = null;
		String LatestOutStandingAmountStr = null;
		session.setAttribute("NPADetailList", null);
		session.removeAttribute("NPADetailList");

		String Role = (String) session.getAttribute("uRole");

		String userId = (String) session.getAttribute("userId");
		memberId = npaService.getMemberId(userId);

		String mliLongName = bean.getMliLongName();

		System.out.println("mliLongName==194===" + mliLongName);

		validator.npaReportDetailsValidate(bean, result);
		if (result.hasErrors()) {
			if (Role.equals("CMAKER")) {
				// userId = "ADMIN";
				model1.put("guaranteelist", userActivityService.getActivity("CGTMSEMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity("CGTMSEMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity("CGTMSEMAKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getActivity("CGTMSEMAKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport("CGTMSEMAKER", "User_Report"));
				// model1.put("actNameHome",
				// userActivityService.getActivityName("CGTMSEMAKER",
				// "cgpanDetailReport"));// Added by Say 31 Jan19
				model1.put("homePage", "cgtmseMakerHome");

			} else if (Role.equals("CCHECKER")) {
				// userId = "ADMIN";
				model1.put("adminlist", userActivityService.getActivity("CGTMSECHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity("CGTMSECHECKER", "Registration"));
				model1.put("applicationList",
						userActivityService.getActivity("CGTMSECHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity("CGTMSECHECKER", "Receipt_Payments"));
				model1.put("CList", userActivityService.getActivity("CGTMSECHECKER", "Claim_Lodgement"));
				model1.put("repList", userActivityService.getReport("CGTMSECHECKER", "User_Report"));
				// model1.put("actNameHome",
				// userActivityService.getActivityName("CGTMSECHECKER",
				// "cgpanDetailReport"));// Added by Say 31 Jan19
				model1.put("homePage", "cgtmseCheckerHome");
				// return null;
			} else if (Role.equals("NMAKER")) {
				// added by say 6 feb-----------------------
				// userId = (String) session.getAttribute("userId");
				model1.put("adminlist", userActivityService.getActivity("NBFCMAKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity("NBFCMAKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity("NBFCMAKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity("NBFCMAKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport("NBFCMAKER", "User_Report"));
				model1.put("CList", userActivityService.getActivity("NBFCMAKER", "Claim_Lodgement"));
				model1.put("homePage", "nbfcMakerHome");

			} else if (Role.equals("NCHECKER")) {
				// userId = (String) session.getAttribute("userId");
				model1.put("adminlist", userActivityService.getActivity("NBFCCHECKER", "System_Admin"));
				model1.put("guaranteelist", userActivityService.getActivity("NBFCCHECKER", "Registration"));
				model1.put("applicationList", userActivityService.getActivity("NBFCCHECKER", "Application_Processing"));
				model1.put("RPaymentList", userActivityService.getActivity("NBFCCHECKER", "Receipt_Payments"));
				model1.put("repList", userActivityService.getReport("NBFCCHECKER", "User_Report"));
				// model1.put("actNameHome",
				// userActivityService.getActivityName("NBFCCHECKER",
				// "cgpanDetailReport"));// Added by Say 31 Jan19
				model1.put("homePage", "nbfcCheckerHome");

			}
			return new ModelAndView("npaReportInputForm", model1);
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
			// return null;
		} else if (Role.equals("NMAKER")) {
			// added by say 6 feb-----------------------
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
			// model1.put("actNameHome",
			model1.put("CList", userActivityService.getActivity("NBFCCHECKER", "Claim_Lodgement"));
			model1.put("homePage", "nbfcCheckerHome");

		}

		/*
		 * String toDateF = bean.getToDate(); String fromDateF = bean.getFromDate();
		 * session.setAttribute("FDate", fromDateF); session.setAttribute("TDate",
		 * toDateF);
		 */

		Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getToDate());
		Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(bean.getFromDate());

		if (Role.equals("CCHECKER") || Role.equals("CMAKER")) {
			// String mliname=bean.getMliName();
			// mem_id = userActivityService.getBankID(mliname);
			// String Mem_Id= mem_id.getBnkId() + mem_id.getZneID()
			// + mem_id.getBrnName();
			if (mliLongName.equals("All")) {
				NPADetailList = npaService.getNPAReportDetail(userId, toDate, fromDate, mliLongName, Role);
			} else {
				mem_id = userActivityService.getBankID(mliLongName);
				// mem_id = userActivityService.getBankID(mliLongName);
				// mem_id = userActivityService.getBankID(mliname);
				String branchName = mem_id.getBrnName();
				String Mem_Id = mem_id.getBnkId() + mem_id.getZneID() + mem_id.getBrnName();
				NPADetailList = npaService.getNPAReportDetail(userId, toDate, fromDate, Mem_Id, Role);
			}
		} else {

			NPADetailList = npaService.getNPAReportDetail(userId, toDate, fromDate, memberId, Role);
		}
		// nbfcInvoiceService.callTaxInvoiceReport(dan_id, response,request);
		if (!NPADetailList.isEmpty()) {
			model1.put("NPADetailList", NPADetailList);
			session.setAttribute("NPADetailList", NPADetailList);
		} else {
			model1.put("message", "NO Data Found !!");
		}
		return new ModelAndView("npaReportListDetails", model1);
	}

	// Get the MLI Long Name in Drop Down on Page on Load
	@ModelAttribute("mliLongName")
	public Map<String, String> getMlilongName() {
		Map<String, String> mapMliLongNameObj = new HashMap<String, String>();
		mapMliLongNameObj = cgtmseExposureMasterMakerService.getMliLongNameInDropDown();
		return mapMliLongNameObj;
	}
	// For DownLoadExcel File Added by ajeet
	@RequestMapping(value = "/NPAReportDetailDownload", method = RequestMethod.GET)
	public ModelAndView npaReportdownLoad(@ModelAttribute("command") NPADetailsBean bean, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
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
			List<Map<String, Object>> list = (List<Map<String, Object>>) session.getAttribute("NPADetailList");
			// List<MLIRegistration> list = mliDetailsService.getMLIAllDetails();

			// List<UserPerivilegeDetails>
			// list=employeeService.getUserPrivlageDetails();
			log.info("list size==" + list.size());
			log.info("list Data==" + list);
			OutputStream os = response.getOutputStream();

			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
			String strDate = sdf.format(cal.getTime());

			// System.out.println("ExportToFile Calling..");

			String contextPath1 = request.getSession(false).getServletContext().getRealPath("");
			String contextPath = PropertyLoader.changeToOSpath(contextPath1);

			System.out.println("contextPath1 :" + contextPath1);
			System.out.println("contextPath :" + contextPath);

			// Writing and Downlaoding Excel File

			ArrayList<Object> HeaderArrLst = new ArrayList<Object>();

			String key = null;
			for (Map<String, Object> ClmDataList : list) {
				for (Map.Entry<String, Object> entry : ClmDataList.entrySet()) {
					// rowhead = sheet.createRow((short) 0);
					key = entry.getKey();
					// Object value = entry.getValue();
					System.out.println("ClmDataList key==" + key);
					HeaderArrLst.add(key);
				}
				break;
			}

			// System.out.println("@@@@@@@@@@@@HeaderArrLst:" + HeaderArrLst);
			int NoColumn = HeaderArrLst.size();

			//System.out.println("NoColumn:" + NoColumn);

			// if (fileType.equals("CSVType")) {
			byte[] b = NpaReportMethod(list, NoColumn, contextPath);

			if (response != null)
				response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename=ExcelData" + strDate + ".csv");
			os.write(b);
			os.flush();

		} catch (Exception e) {
			log.info("Exception == " + e);
			System.out.println("Exception == " + e);
		}
		return null;

	}

	public byte[] NpaReportMethod(List<Map<String, Object>> list, int No_Column, String contextPath)
			throws IOException {

		System.out.println("---generateCSV()---");
		StringBuffer strbuff = new StringBuffer();
		// System.out.println("ParamDataList:" + ParamDataList);
		// System.out.println("contextPath :" + contextPath);
		// ArrayList<String> rowDataLst = new ArrayList<String>();
		// ArrayList<String> HeaderLst = (ArrayList) list.get(0);
		// ArrayList<ArrayList> RecordWiseLst = (ArrayList) list.get(1);

		// List<Map<String, Object>> rowDataLst= new ArrayList<Map<String, Object>>();
		ArrayList<Object> rowDataLst = new ArrayList<Object>();

		// List<Map<String, Object>> HeaderLst= new ArrayList<Map<String, Object>>();
		String key = null;
		for (Map<String, Object> HeaderLst : list) {
			for (Map.Entry<String, Object> entry : HeaderLst.entrySet()) {
				// rowhead = sheet.createRow((short) 0);
				key = entry.getKey();
				// Object value = entry.getValue();
				//System.out.println("ClmDataList key==" + key);
				rowDataLst.add(key);
			}
			// rowDataLst.add(key);
			//System.out.println("HeaderLst" + HeaderLst);
			break;
		}

		// List<Map<String, Object>> RecordWiseLst= new ArrayList<Map<String,
		// Object>>();
		System.out.println("rowDataLst" + rowDataLst);

		// System.out.println("HeaderLst" + HeaderLst);
		// System.out.println("RecordWiseLst" + RecordWiseLst);
		// #### Header List

		// System.out.println("Loop--headerdata:" + headerdata);

		// System.out.println("rowDataLst:" + rowDataLst);
		// #### Header List

		// #### Data List

		for (Map<String, Object> RecordWiseLst : list) {
			for (Map.Entry<String, Object> entry : RecordWiseLst.entrySet()) {
				// rowhead = sheet.createRow((short) 0);
				// key = entry.getKey();
				Object value = entry.getValue();
				System.out.println("ClmDataList key==" + value);
				rowDataLst.add(value);

			}
			// rowDataLst.add(RecordWiseLst);
			System.out.println("RecordWiseLst" + RecordWiseLst);
		}
		System.out.println("rowDataLst" + rowDataLst);
		// System.out.println("rowDataLst::" + rowDataLst);
		// #### Data List

		ArrayList<Object> FinalrowDatalist = new ArrayList<Object>();
		// System.out.println("1");
		int y = 0;
		System.out.println("2==" + No_Column);
		for (int n = 0; n < rowDataLst.size(); n++) {

			if (n % No_Column == 0 && n != 0) {
				FinalrowDatalist.add(rowDataLst.get(n));
				FinalrowDatalist.add(n + y, "\n");
				System.out.println("2n value inside if:" + n);
				System.out.println("n:" + n);
				y++;
			} else {
				System.out.println("2n inside else:" + n);
				if (null != rowDataLst.get(n)) {
					// if (( rowDataLst.get(n)).contains(",")) {
					if (((Object) rowDataLst.get(n)).equals(",")) {
						rowDataLst.set(n, ((String) rowDataLst.get(n)).replace(",", ";"));
					}
				}
				FinalrowDatalist.add(rowDataLst.get(n));
			}
		//	System.out.println("rowDataLst.get " + rowDataLst.get(n) + "    " + n % 3);
		}
		// System.out.println("rowDataLst :"+rowDataLst.toString().replace("\n,","\n"));
		// String tempStr = rowDataLst.toString().replace("\n,", "\n");
		// System.out.println("3");

		String tempStr = FinalrowDatalist.toString().replace("\n,", "\n").replace(" ,", ",").replace(", ", ",");
		// String tempStr = FinalrowDatalist.toString().replace("\n,", "\n");

		// System.out.println("4");
		// strbuff.append(ParamDataList.toString().substring(2,
		// ParamDataList.toString().length() - 2).replace("endrow,", "\n"));
		strbuff.append(tempStr.substring(1, tempStr.length() - 1));
		// System.out.println("strbuff :"+strbuff);
		/// System.out.println("5");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String strDate = sdf.format(cal.getTime());
		BufferedWriter output = null;
		OutputStream outStrm;
		// File genfile = new File("D:\\GenerateFiles\\SampleFile" + strDate+
		// ".csv");
		File genfile = new File(contextPath + "\\Download\\nbfcNPAReportExcelFile" + strDate + ".csv");

		// System.out.println("6");
		output = new BufferedWriter(new FileWriter(genfile));
		output.write(strbuff.toString());
		// System.out.println("7");
		output.flush();
		output.close();
		// System.out.println("8");

		// ##
		// FileInputStream fis = new
		// FileInputStream("D:\\GenerateFiles\\SampleFile" + strDate+ ".csv");
		FileInputStream fis = new FileInputStream(contextPath + "\\Download\\nbfcNPAReportExcelFile" + strDate + ".csv");

		// System.out.println("9");
		byte b[];
		int x = fis.available();
		b = new byte[x];
		// System.out.println(" b size"+b.length);

		fis.read(b);
		// ##
		return b;
		// genfile.setReadOnly();

	}

}
