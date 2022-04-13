package com.nbfc.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.text.DecimalFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.impl.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nbfc.bean.CGPANDetailsReportBean;
import com.nbfc.common.utility.method.NumberToWordsFormatMethod;
import com.nbfc.helper.NBFCHelper;
import com.nbfc.helper.PropertyLoader;
import com.nbfc.model.ProformaInvoiceDetails;
import com.nbfc.model.ProformaInvoiceDetailsASF;
import com.nbfc.model.TaxDetailMaster;
import com.nbfc.model.TaxInvoiceDetails;
import com.nbfc.model.TaxInvoiceDetailsASF;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import oracle.jdbc.OracleTypes;

@Repository("NBFCInvoiceDao")
public class NBFCInvoiceDaoImpl implements NBFCInvoiceDao {

	@Autowired
	SessionFactory sessionFactory;
	ResultSet resultset = null;
	public List<TaxDetailMaster> getTaxInvoiceDeails() {
		System.out.println("Hello");
		return (List<TaxDetailMaster>) sessionFactory.getCurrentSession().createCriteria(TaxDetailMaster.class).list();
	}

	// Modified By Parmanand 09-Jan-2019
	public void callTaxInvoiceReport(String portfolioName, HttpServletResponse response, HttpServletRequest request,
			CGPANDetailsReportBean bean) throws JRException {
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		NBFCHelper nbfcHelper = new NBFCHelper();
		Connection connection = ((SessionImpl) session).connection();
		JasperPrint jasperPrint1 = null;
		if (connection == null) {
			System.out.println("connection is null.........");
		}
		String State = bean.getState();
		String DAN = bean.getDanId();
		String userId = bean.getUserId();
		// TaxInvoice No
		String date = bean.getDciAppropriationDate();
		String TaxNo = bean.getTaxid();
		// String id = date.replace("/", "");

		// -------------------------------------------------------------------------------------
		double dci_base_amt = bean.getDci_base_amt();
		BigDecimal dci_base_amt1 = new BigDecimal(dci_base_amt);
		BigDecimal dci_base_amt2 = dci_base_amt1.setScale(2, RoundingMode.HALF_UP);
		// String taxBaseAmountInRupes = dci_base_amt2.toPlainString();

		double dci_total_amt = Math.round(bean.getDci_total_amt());
		BigDecimal dci_total_amt1 = new BigDecimal(dci_total_amt);
		BigDecimal dci_total_amt2 = dci_total_amt1.setScale(2, RoundingMode.HALF_UP);
		//-----------------------------------------------------------------------------------------	

		double IGSTAMT = Math.round(bean.getIgstAmt());
		double CGSTAMT = Math.round(bean.getCgstAmt());
		double SGSTAMT = Math.round(bean.getSgstAmt());

		/*
		 * String fyBasedOnStartAndEndDate = nbfcHelper.getCurrentYear(); StringBuilder
		 * sb = new StringBuilder(fyBasedOnStartAndEndDate); sb.delete(5, 7); String fy
		 * = sb.toString();
		 * 
		 * String TaxInvoiceNo = fy + "N0000000" + TaxNo;
		 */

		double outSandingAndGuran = ((bean.getOutstandingAmount1() * bean.getGuaranteeFee() / 100));
		double outSandingAndGuranRound = Math.round(outSandingAndGuran);
		BigDecimal taxAmt = new BigDecimal(outSandingAndGuranRound);
		BigDecimal taxAmountInRupes2 = taxAmt.setScale(2, RoundingMode.HALF_UP);
		String taxAmountInRupes = taxAmountInRupes2.toPlainString();
		double objTotalAmt2 = bean.getIgstAmt() + bean.getCgstAmt() + bean.getSgstAmt() + outSandingAndGuran;
		BigDecimal objTotalAmt = new BigDecimal(Math.round(objTotalAmt2));
		BigDecimal objFinalToatlAmt1 = objTotalAmt.setScale(2, RoundingMode.HALF_UP);
		// -----------------------------------------------------------
		String fianlTotalAmountInRupes = dci_total_amt2.toPlainString();
		long fianlTotalAmountInRupes1 = dci_total_amt2.longValue();
		String sim2 = NumberToWordsFormatMethod.inWordFormat(fianlTotalAmountInRupes1);
		// --------------------------------------------------------------
		//		String fianlTotalAmountInRupes = objFinalToatlAmt1.toPlainString();
		//		
		//		long fianlTotalAmountInRupes1 = dci_total_amt2.longValue();
		//		String sim2 = NumberToWordsFormatMethod
		//				.inWordFormat(fianlTotalAmountInRupes1);
		//		//-----------------------------------------------------

		BigDecimal IGST_AMT = new BigDecimal(IGSTAMT);
		BigDecimal IGST_AMT1 = IGST_AMT.setScale(2, RoundingMode.HALF_UP);

		BigDecimal CGST_AMT = new BigDecimal(CGSTAMT);
		BigDecimal CGST_AMT1 = CGST_AMT.setScale(2, RoundingMode.HALF_UP);

		BigDecimal SGST_AMT = new BigDecimal(SGSTAMT);
		BigDecimal SGST_AMT1 = SGST_AMT.setScale(2, RoundingMode.HALF_UP);

		HashMap map = new HashMap();
		map.put("TotalIgstAmt", IGST_AMT1);
		map.put("TotalCgstAmt", CGST_AMT1);
		map.put("TotalSgstAmt", SGST_AMT1);
		map.put("TaxInvoiceNo", TaxNo);
		map.put("TaxAmount", taxAmountInRupes2);
		map.put("TotalAmount", dci_total_amt2);
		map.put("TotalAmountInWords", sim2);
		map.put("StateName", State);
		map.put("portfolioName", portfolioName);
		map.put("userId", userId);
		try {
			String filename = "TaxInvoiceReport1";
			String reportfileName = "Taxreport" + portfolioName + ".pdf";
			String tempFolderPath = "/WEB-INF/JReport/" + filename;
			System.out.println("tempFolderPath.............................................." + tempFolderPath);
			File tempFolder = new File(tempFolderPath);
			if (!tempFolder.exists()) {
				tempFolder.mkdirs();
			}
			String jasperFilePath = tempFolderPath + ".jasper";
			System.out.println("jasperFilePath......................" + jasperFilePath);
			File reportFile = new File(jasperFilePath);
			if (!reportFile.exists()) {
				InputStream jRXmlStream = request.getSession().getServletContext()
						.getResourceAsStream("/WEB-INF/JReport/" + filename + ".jrxml");
				JasperDesign jasperDesign = JRXmlLoader.load(jRXmlStream);
				JasperCompileManager.compileReportToFile(jasperDesign, jasperFilePath);
				System.out.println(jasperFilePath);
			}
			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, connection);
			JRAbstractExporter exporterPDF = new JRPdfExporter();
			exporterPDF.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			try {
				exporterPDF.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			response.setHeader("Content-Disposition", "attachment;filename=" + reportfileName);
			response.setContentType("application/pdf");
			exporterPDF.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tn.commit();

		}

	}

	@Override
	public List<Object> getTaxReportDetails(String portfolioName, String uid) {
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		Connection connection = ((SessionImpl) session).connection();
		String sql = "SELECT MAX(MEM_BANK_NAME) MEM_BANK_NAME,MAX(MEM_ADDRESS) MEM_ADDRESS,MAX(TAX_DATE) TAX_DATE, MAX(GSTIN_NO) GSTIN_NO,"
				+ "MAX(MEM_STATE_NAME) AS STE_CODE,MAX(STE_NAME) STE_NAME,MAX(MLI_ID) MLI_ID,MAX(PORTFOLIO_NAME) PORTFOLIO_NAME,"
				+ "SUM(SANCTIONED_AMOUNT) SANCTIONED_AMOUNT,SUM(GUARANTEE_AMOUNT) GUARANTEE_AMOUNT,"
				+ "MAX(TENOR_IN_MONTHS) TENOR_IN_MONTHS,MAX((NBFC_UPLOADED_DATE)) NBFC_UPLOADED_DATE,MAX(DCI_AMOUNT_RAISED) DCI_AMOUNT_RAISED,MAX(DCI_BASE_AMT) DCI_BASE_AMT,"
				+ "MAX(IGST_RATE) IGST_RATE,MAX(IGST_AMT) IGST_AMT,MAX(CGST_RATE) CGST_RATE,MAX(CGST_AMT) CGST_AMT,MAX(SGST_RATE) SGST_RATE,MAX(SGST_AMT) SGST_AMT, "
				+ "MAX (DCI_STANDARD_RATE) DCI_STANDARD_RATE , MAX(TAX_APP_DATE) TAX_APP_DATE, MAX (DAN_ID) DAN_ID  "
				+ "FROM(SELECT NM.MEM_BANK_NAME,NM.MEM_ADDRESS,(SELECT TO_CHAR(DI.DCI_GUARANTEE_START_DT,'DD/MM/YYYY') FROM NBFC_DAN_CGPAN_INFO DI WHERE DI.DAN_ID = NI.DAN_ID  ) TAX_DATE,(SELECT TO_CHAR(DI.DCI_APPROPRIATION_DT,'DD/MM/YYYY') FROM NBFC_DAN_CGPAN_INFO DI WHERE DI.DAN_ID = NI.DAN_ID  ) TAX_APP_DATE, DAN_ID,"
				+ "NM.GSTIN_NO,NM.MEM_STATE_NAME,S.STE_NAME,NM.MEM_BNK_ID||NM.MEM_ZNE_ID||NM.MEM_BRN_ID MLI_ID,"
				+ "NI.PORTFOLIO_NAME,NI.SANCTIONED_AMOUNT,NI.GUARANTEE_AMOUNT,NI.TENOR_IN_MONTHS,"
				+ "TO_CHAR(NI.NBFC_UPLOADED_DATE,'DD/MM/YYYY') NBFC_UPLOADED_DATE,"
				+ "(SELECT  SUM(DCI_AMOUNT_RAISED) FROM NBFC_DAN_CGPAN_INFO DI WHERE DI.DAN_ID IN(SELECT DISTINCT (C.DAN_ID) FROM NBFC_INTERFACE_UPLOAD c WHERE c.PORTFOLIO_NAME='"
				+ portfolioName + "'  ))  AS DCI_AMOUNT_RAISED,"
				+ "(SELECT  SUM(DI.DCI_BASE_AMT) FROM NBFC_DAN_CGPAN_INFO DI WHERE DI.DAN_ID IN(SELECT DISTINCT (C.DAN_ID) FROM NBFC_INTERFACE_UPLOAD c WHERE c.PORTFOLIO_NAME='"
				+ portfolioName + "') )  AS DCI_BASE_AMT,"
				+ "(SELECT  MAX(DI.IGST_RATE) FROM NBFC_DAN_CGPAN_INFO DI WHERE DI.DAN_ID IN(SELECT DISTINCT (C.DAN_ID) FROM NBFC_INTERFACE_UPLOAD c WHERE c.PORTFOLIO_NAME='"
				+ portfolioName + "') )  AS IGST_RATE,"
				+ "(SELECT  SUM(DI.IGST_AMT) FROM NBFC_DAN_CGPAN_INFO DI WHERE DI.DAN_ID IN(SELECT DISTINCT (C.DAN_ID) FROM NBFC_INTERFACE_UPLOAD c WHERE c.PORTFOLIO_NAME='"
				+ portfolioName
				+ "'  ) )  AS IGST_AMT,(SELECT  MAX(DI.CGST_RATE) FROM NBFC_DAN_CGPAN_INFO DI WHERE DI.DAN_ID IN(SELECT DISTINCT (C.DAN_ID) FROM NBFC_INTERFACE_UPLOAD c WHERE c.PORTFOLIO_NAME='"
				+ portfolioName + "')) "
				+ " AS CGST_RATE,(SELECT  SUM(DI.CGST_AMT) FROM NBFC_DAN_CGPAN_INFO DI WHERE DI.DAN_ID IN(SELECT DISTINCT (C.DAN_ID) FROM NBFC_INTERFACE_UPLOAD c WHERE c.PORTFOLIO_NAME='"
				+ portfolioName
				+ "' ) )  AS CGST_AMT,(SELECT  MAX(DI.SGST_RATE) FROM NBFC_DAN_CGPAN_INFO DI WHERE DI.DAN_ID IN(SELECT DISTINCT (C.DAN_ID) FROM NBFC_INTERFACE_UPLOAD c WHERE c.PORTFOLIO_NAME='"
				+ portfolioName + "') )  AS SGST_RATE,"
				+ "(SELECT  SUM(DI.SGST_AMT) FROM NBFC_DAN_CGPAN_INFO DI WHERE DI.DAN_ID IN(SELECT DISTINCT (C.DAN_ID) FROM NBFC_INTERFACE_UPLOAD c WHERE c.PORTFOLIO_NAME='"
				+ portfolioName
				+ "' ) )  AS SGST_AMT, (SELECT SUM (DI.DCI_STANDARD_RATE) FROM NBFC_DAN_CGPAN_INFO DI WHERE DI.DAN_ID = ni.dan_id ) AS DCI_STANDARD_RATE "
				+ "FROM NBFC_INTERFACE_UPLOAD NI,NBFC_MEMBER_INFO NM,CGTSITEMPUSER.STATE_MASTER S WHERE NI.MEM_BNK_ID||NI.MEM_ZNE_ID||NI.MEM_BRN_ID=NM.MEM_BNK_ID||NM.MEM_ZNE_ID||NM.MEM_BRN_ID AND S.STE_CODE =MEM_STATE_NAME AND PORTFOLIO_NAME='"
				+ portfolioName + "' AND NI.STATUS='CCA')";
		System.out.println("Query-------" + sql);
		SQLQuery query = session.createSQLQuery(sql);
		List<Object> list = query.list();
		return list;
	}


	@Override
	public CGPANDetailsReportBean getTaxReportDetailsForGFAndTM(String portfolioName, String taxInvId,String danType) {
		ResultSet resultset = null;
		CGPANDetailsReportBean bean = null;		
		try {
			Session session4 = sessionFactory.openSession();			
			Connection conn = session4.connection();
			CallableStatement callableStatement = (CallableStatement) conn.prepareCall("{ call NBFC.PROC_NBFCAPPROPRIATIONBYDANTYP(?,?,?) } ");
			callableStatement.setString(1, taxInvId);
			callableStatement.setString(2, danType);
			callableStatement.registerOutParameter(3, OracleTypes.CURSOR);
			callableStatement.execute();
			resultset = (ResultSet) callableStatement.getObject(3);				
			while (resultset.next()) {

				bean = new CGPANDetailsReportBean();
				bean.setPortfolioName(portfolioName);
				
				//System.out.println("MEM_BANK_NAME ::"+resultset.getString("MEM_BANK_NAME"));
				bean.setBankname(resultset.getString("MEM_BANK_NAME"));
				
				//System.out.println("MEM_ADDRESS::"+resultset.getString("MEM_ADDRESS"));
				bean.setAddress(resultset.getString("MEM_ADDRESS"));
				
				//System.out.println("Tax Date::"+resultset.getString("Tax_Date"));				
				
			//	System.out.println("DCI_APPROPRIATION_DT Date::"+resultset.getString("DCI_APPROPRIATION_DT"));
				bean.setDciAppropriationDate(resultset.getString("DCI_APPROPRIATION_DT"));

				//System.out.println("GSTIN_NO::"+resultset.getString("GSTIN_NO"));
				bean.setGstNo(resultset.getString("GSTIN_NO"));
				
				//System.out.println("State Code::"+resultset.getString("MEM_STATE_CODE"));
				bean.setStateCode(resultset.getString("MEM_STATE_CODE"));

				//System.out.println("State Name::"+resultset.getString("MEM_STATE_NAME"));
				bean.setState(resultset.getString("MEM_STATE_NAME"));

				//System.out.println("MLI_ID::"+resultset.getString("MLI_ID"));
				bean.setMliId(resultset.getString("MLI_ID"));

				//System.out.println("PORTFOLIO_NAME::"+resultset.getString("PORTFOLIO_NAME"));
				bean.setPortfolioName(resultset.getString("PORTFOLIO_NAME"));				
				
				//System.out.println("SANCTIONED_AMOUNT::"+resultset.getBigDecimal("SANCTIONED_AMOUNT"));
				bean.setSanctionAMt1(resultset.getBigDecimal("SANCTIONED_AMOUNT").setScale(2, RoundingMode.HALF_UP));

				//System.out.println("GUARANTEE_AMOUNT::"+resultset.getString("GUARANTEE_AMOUNT"));
				bean.setGuaranteeAmt1(resultset.getBigDecimal("GUARANTEE_AMOUNT").setScale(2, RoundingMode.HALF_UP));

				//System.out.println("TENOR_IN_MONTHS::"+resultset.getString("TENOR_IN_MONTHS"));				
				//System.out.println("NBFC_UPLOADED_DATE::"+resultset.getString("NBFC_UPLOADED_DATE"));				

				//System.out.println("DCI_AMOUNT_RAISED::"+resultset.getBigDecimal("DCI_AMOUNT_RAISED"));				
				bean.setTotalAmount(new BigDecimal(roundUP(""+resultset.getBigDecimal("DCI_AMOUNT_RAISED"))));//new BigDecimal(roundUP(""+totalAmt))
				
				String sim2 = NumberToWordsFormatMethod.inWordFormat(bean.getTotalAmount().doubleValue());
				bean.setTotalAmt(sim2);

				//System.out.println("DCI_BASE_AMT::"+resultset.getBigDecimal("DCI_BASE_AMT"));
				bean.setBaseAmount(new BigDecimal(roundUP(""+resultset.getBigDecimal("DCI_BASE_AMT"))));

			//	System.out.println("IGST_RATE::"+resultset.getString(17));				

				//System.out.println("IGST_AMT::"+resultset.getBigDecimal("IGST_AMT"));
				bean.setIgstAmount(new BigDecimal(roundUP(""+resultset.getBigDecimal("IGST_AMT"))));

			//	System.out.println("CGST_RATE::"+resultset.getBigDecimal(19));
			
				//System.out.println("CGST_AMT::"+resultset.getBigDecimal("CGST_AMT"));
				bean.setCgstAmount(new BigDecimal(roundUP(""+resultset.getBigDecimal("CGST_AMT"))));

			//	System.out.println("SGST_RATE::"+resultset.getString(21));				

				//System.out.println("SGST_AMT::"+resultset.getBigDecimal("SGST_AMT"));
				bean.setSgstAmount(new BigDecimal(roundUP(""+resultset.getBigDecimal("SGST_AMT"))));

				System.out.println("DCI_STANDARD_RATE::"+resultset.getString("DCI_STANDARD_RATE"));
				bean.setDci_standard_rate(resultset.getString("DCI_STANDARD_RATE"));

				//System.out.println("dan_id::"+resultset.getString("DAN_ID"));
				bean.setDanId(resultset.getString("DAN_ID"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		
		return bean;
	}


	public void insertTaxInvoiceDetails(TaxInvoiceDetails taxDetailsObj) {
		sessionFactory.getCurrentSession().save(taxDetailsObj);
	}

	@Override
	public Integer getMaxTaxInvoiceIdCount() {
		try {
			String hql = "SELECT MAX(TAX_INV_ID)+1 FROM TaxInvoiceDetails ";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			System.out.println("Exception===" + e);
		}
		return 0;
	}

	@Override
	public Integer getTaxInvoiceIdCount(String portfolioName) {
		try {
			String hql = "SELECT count(*) FROM TaxInvoiceDetails where PORTFOLIO_NAME=:PORTFOLIO ";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("PORTFOLIO", portfolioName);
			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			System.out.println("Exception===" + e);
		}
		return 0;
	}

	@Override
	public String getTaxInvoiceNo(String portfolioName) {
		String Taxid = "";
		try {
			Session session = sessionFactory.openSession();
			Transaction tn = session.beginTransaction();
			Connection connection = ((SessionImpl) session).connection();
//			SQLQuery slqQuery = session.createSQLQuery("SELECT TAX_INV_ID FROM NBFC_GST_TAX_INVOICE WHERE PORTFOLIO_NAME = '" + portfolioName + "' and DAN_TYPE='AF' " ); //Add by VinodSingh For GF 10-APR-2021
		//Code changed by Shital on 17-Jan-2022
			SQLQuery slqQuery = session.createSQLQuery("SELECT max(TAX_INV_ID) FROM NBFC_GST_TAX_INVOICE WHERE (PORTFOLIO_NAME = '" + portfolioName + "' OR PORTFOLIO_NAME = (SELECT MAX(ASF_DAN_ID) FROM nbfc_asf_detail WHERE portfolio_name = '" + portfolioName + "' AND ASF_YEAR = (SELECT MAX(ASF_YEAR) FROM nbfc_asf_detail WHERE portfolio_name = '" + portfolioName + "'))) " );  
			System.out.println("slqQuery::: "+slqQuery);			
			ArrayList fyList = (ArrayList) slqQuery.list();			
			Iterator itr = fyList.iterator();
			while (itr.hasNext()) {
				Taxid = (String) itr.next();
			}

			return Taxid;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getTaxInvoiceNoForGFAndTM(String portfolioName,String danType) {
		String Taxid = "";
		try {
			Session session = sessionFactory.openSession();
			Transaction tn = session.beginTransaction();
			Connection connection = ((SessionImpl) session).connection();
			SQLQuery slqQuery = session.createSQLQuery("SELECT TAX_INV_ID FROM NBFC_GST_TAX_INVOICE WHERE PORTFOLIO_NAME = '" + portfolioName + "' and DAN_TYPE='" + danType + "' " ); //Add by VinodSingh For GF 10-APR-2021
			System.out.println("slqQuery::: "+slqQuery);
			ArrayList fyList = (ArrayList) slqQuery.list();			
			Iterator itr = fyList.iterator();
			while (itr.hasNext()) {
				Taxid = (String) itr.next();
			}

			return Taxid;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<Object> getTaxInvoiceData(String mliId, HttpSession session) {
		String mliId1=mliId;
		String Role = (String) session.getAttribute("uRole");
		List<Object> list;		
		Session session1 = sessionFactory.openSession();
		Transaction tn = session1.beginTransaction();
		Connection connection = ((SessionImpl) session1).connection();
		
		if (Role.equals("CMAKER") || Role.equals("CCHECKER")) {
		//	String hql = "SELECT PM.PortFolioName,MAX(MR.mem_bnk_name),MAX(UI.USR_ID) FROM FileUploadModel IU,PortFolioDetailsInParentTBL PM,DanCgpanDetailsModel DC,MLIRegistration MR,UserInfo UI WHERE PM.portfolio_no = IU.portfolioNo AND DC.DCI_APPROPRIATION_FLAG='Y' AND IU.danId = DC.danId AND concat(concat(IU.mem_bank_id,IU.mem_zne_id),IU.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) AND UI.MEM_BNK_ID = MR.mem_bnk_id AND DC.Dci_appropriationDate >= '1-OCT-2020' and UI.MEM_BNK_ID not in ('1026','1027') GROUP BY PM.PortFolioName,MR.mem_bnk_name";
		//	SQLQuery slqQuery = session1.createSQLQuery("Select * from (SELECT a.PAY_ID,null PORTFOLIO_NAME,B.MEM_BANK_NAME,a.DAN_TYPE,a.tax_inv_id FROM NBFC_GST_TAX_INVOICE a, nbfc_member_info b, nbfc_dan_cgpan_info d,nbfc_demand_advice_info e WHERE a.pay_id = d.pay_id AND e.mem_bnk_id || e.mem_zne_id || e.mem_zne_id = b.mem_bnk_id || b.mem_zne_id || b.mem_zne_id AND E.DAN_ID = D.DAN_iD AND DCI_APPROPRIATION_FLAG = 'Y' AND DCI_APPROPRIATION_DT IS NOT NULL AND B.MEM_BNK_ID NOT IN ('1026', '1027') AND E.dan_type = 'TM' GROUP BY MEM_BANK_NAME, a.DAN_TYPE,a.PAY_ID,a.tax_inv_id union all select d.PAY_ID,C.PORTFOLIO_NAME, B.MEM_BANK_NAME ,A.DAN_TYPE,a.tax_inv_id from NBFC_GST_TAX_INVOICE a, nbfc_member_info b, nbfc_interface_upload c, nbfc_dan_cgpan_info d where a.portfolio_name=c.portfolio_name and c.mem_bnk_id||c.mem_zne_id||c.mem_zne_id=b.mem_bnk_id||b.mem_zne_id||b.mem_zne_id and d.dan_id=c.dan_id and DCI_APPROPRIATION_FLAG = 'Y' and DCI_APPROPRIATION_DT is not null and B.MEM_BNK_ID NOT IN ('1026', '1027') and A.dan_type='GF' and A.TAX_INV_ID not in ('2019-20N00000023','2019-20N00000031','2020-21N00000027') GROUP BY C.PORTFOLIO_NAME,MEM_BANK_NAME,A.DAN_TYPE,d.PAY_ID,a.tax_inv_id) order by mem_bank_name ");
			SQLQuery slqQuery = session1.createSQLQuery("select * from (SELECT a.PAY_ID,null PORTFOLIO_NAME,B.MEM_BANK_NAME,a.DAN_TYPE,a.tax_inv_id FROM NBFC_GST_TAX_INVOICE a, nbfc_member_info b, nbfc_dan_cgpan_info d,nbfc_demand_advice_info e WHERE a.pay_id = d.pay_id AND e.mem_bnk_id || e.mem_zne_id || e.mem_zne_id = b.mem_bnk_id || b.mem_zne_id || b.mem_zne_id AND E.DAN_ID = D.DAN_iD AND DCI_APPROPRIATION_FLAG = 'Y' AND DCI_APPROPRIATION_DT IS NOT NULL AND B.MEM_BNK_ID NOT IN ('1026', '1027') AND E.dan_type = 'TM' GROUP BY MEM_BANK_NAME,a.DAN_TYPE,a.PAY_ID,a.tax_inv_id union all select d.PAY_ID,C.PORTFOLIO_NAME, B.MEM_BANK_NAME ,A.DAN_TYPE,a.tax_inv_id from NBFC_GST_TAX_INVOICE a, nbfc_member_info b , nbfc_interface_upload c, nbfc_dan_cgpan_info d where a.portfolio_name=c.portfolio_name and c.mem_bnk_id||c.mem_zne_id||c.mem_zne_id=b.mem_bnk_id||b.mem_zne_id||b.mem_zne_id and d.dan_id=c.dan_id and DCI_APPROPRIATION_FLAG = 'Y' and DCI_APPROPRIATION_DT is not null and B.MEM_BNK_ID NOT IN ('1026', '1027') and A.dan_type='GF' and A.TAX_INV_ID not in ('2019-20N00000023','2019-20N00000031','2020-21N00000027') GROUP BY C.PORTFOLIO_NAME,MEM_BANK_NAME,A.DAN_TYPE,d.PAY_ID,a.tax_inv_id) order by mem_bank_name");
			list = (ArrayList) slqQuery.list();			
		} else {
			//String hql = "SELECT (PM.PortFolioName),MAX(MR.mem_bnk_name),MAX(UI.USR_ID) FROM FileUploadModel IU,PortFolioDetailsInParentTBL PM,DanCgpanDetailsModel DC,MLIRegistration MR,UserInfo UI  WHERE PM.portfolio_no = IU.portfolioNo AND DC.DCI_APPROPRIATION_FLAG='Y' AND IU.danId = DC.danId AND concat(concat(IU.mem_bank_id,IU.mem_zne_id),IU.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) AND concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) = :MLIID AND UI.MEM_BNK_ID = MR.mem_bnk_id AND DC.Dci_appropriationDate >= '1-OCT-2020' and UI.MEM_BNK_ID not in ('1026','1027') GROUP BY PM.PortFolioName,MR.mem_bnk_name";
			//SQLQuery slqQuery = session1.createSQLQuery("Select * from (SELECT a.PAY_ID,null PORTFOLIO_NAME,B.MEM_BANK_NAME,a.DAN_TYPE,a.tax_inv_id FROM NBFC_GST_TAX_INVOICE a, nbfc_member_info b, nbfc_dan_cgpan_info d,nbfc_demand_advice_info e WHERE a.pay_id = d.pay_id AND e.mem_bnk_id || e.mem_zne_id || e.mem_zne_id = b.mem_bnk_id || b.mem_zne_id || b.mem_zne_id AND E.DAN_ID = D.DAN_iD AND DCI_APPROPRIATION_FLAG = 'Y' AND DCI_APPROPRIATION_DT IS NOT NULL AND B.MEM_BNK_ID NOT IN ('1026', '1027') AND b.mem_bnk_id || b.mem_zne_id || b.mem_zne_id = '" + mliId + "'  AND E.dan_type = 'TM' GROUP BY MEM_BANK_NAME, a.DAN_TYPE,a.PAY_ID,a.tax_inv_id union all select d.PAY_ID,C.PORTFOLIO_NAME, B.MEM_BANK_NAME ,A.DAN_TYPE,a.tax_inv_id from NBFC_GST_TAX_INVOICE a, nbfc_member_info b, nbfc_interface_upload c, nbfc_dan_cgpan_info d where a.portfolio_name=c.portfolio_name and c.mem_bnk_id||c.mem_zne_id||c.mem_zne_id=b.mem_bnk_id||b.mem_zne_id||b.mem_zne_id and d.dan_id=c.dan_id and DCI_APPROPRIATION_FLAG = 'Y' and DCI_APPROPRIATION_DT is not null and B.MEM_BNK_ID NOT IN ('1026', '1027') and A.dan_type='GF' and A.TAX_INV_ID not in ('2019-20N00000023','2019-20N00000031','2020-21N00000027') AND b.mem_bnk_id || b.mem_zne_id || b.mem_zne_id =  '" + mliId1 + "' GROUP BY C.PORTFOLIO_NAME,MEM_BANK_NAME,A.DAN_TYPE,d.PAY_ID,a.tax_inv_id) order by mem_bank_name");
			//Commented by keshav on25-11-2021 and change in query for tax invoice show according to nbfc cheker and maker
			//SQLQuery slqQuery = session1.createSQLQuery("select * from (SELECT a.PAY_ID,null PORTFOLIO_NAME,B.MEM_BANK_NAME,a.DAN_TYPE,a.tax_inv_id FROM NBFC_GST_TAX_INVOICE a, nbfc_member_info b, nbfc_dan_cgpan_info d,nbfc_demand_advice_info e WHERE a.pay_id = d.pay_id AND e.mem_bnk_id || e.mem_zne_id || e.mem_zne_id = b.mem_bnk_id || b.mem_zne_id || b.mem_zne_id AND E.DAN_ID = D.DAN_iD AND DCI_APPROPRIATION_FLAG = 'Y' AND DCI_APPROPRIATION_DT IS NOT NULL AND B.MEM_BNK_ID NOT IN ('1026', '1027') AND E.dan_type = 'TM' and b.mem_bnk_id||b.mem_zne_id||b.mem_zne_id= '" + mliId + "'  GROUP BY MEM_BANK_NAME,a.DAN_TYPE,a.PAY_ID,a.tax_inv_id union all select d.PAY_ID,C.PORTFOLIO_NAME, B.MEM_BANK_NAME ,A.DAN_TYPE,a.tax_inv_id from NBFC_GST_TAX_INVOICE a, nbfc_member_info b , nbfc_interface_upload c, nbfc_dan_cgpan_info d where a.portfolio_name=c.portfolio_name and c.mem_bnk_id||c.mem_zne_id||c.mem_zne_id=b.mem_bnk_id||b.mem_zne_id||b.mem_zne_id and d.dan_id=c.dan_id and DCI_APPROPRIATION_FLAG = 'Y' and DCI_APPROPRIATION_DT is not null and B.MEM_BNK_ID NOT IN ('1026', '1027') and A.dan_type='GF' and b.mem_bnk_id||b.mem_zne_id||b.mem_zne_id= '" + mliId1 + "' and A.TAX_INV_ID not in ('2019-20N00000023','2019-20N00000031','2020-21N00000027') GROUP BY C.PORTFOLIO_NAME,MEM_BANK_NAME,A.DAN_TYPE,d.PAY_ID,a.tax_inv_id) order by mem_bank_name");
			SQLQuery slqQuery = session1.createSQLQuery("select * from (SELECT a.PAY_ID,null PORTFOLIO_NAME,B.MEM_BANK_NAME,a.DAN_TYPE,a.tax_inv_id FROM NBFC_GST_TAX_INVOICE a, nbfc_member_info b, nbfc_dan_cgpan_info d,nbfc_demand_advice_info e WHERE a.pay_id = d.pay_id AND e.mem_bnk_id || e.mem_zne_id || e.mem_brn_id = b.mem_bnk_id || b.mem_zne_id || b.mem_brn_id AND E.DAN_ID = D.DAN_iD AND DCI_APPROPRIATION_FLAG = 'Y' AND DCI_APPROPRIATION_DT IS NOT NULL AND B.MEM_BNK_ID NOT IN ('1026', '1027') AND E.dan_type = 'TM' and b.mem_bnk_id||b.mem_zne_id||b.mem_brn_id= '" + mliId + "'  GROUP BY MEM_BANK_NAME,a.DAN_TYPE,a.PAY_ID,a.tax_inv_id union all select d.PAY_ID,C.PORTFOLIO_NAME, B.MEM_BANK_NAME ,A.DAN_TYPE,a.tax_inv_id from NBFC_GST_TAX_INVOICE a, nbfc_member_info b , nbfc_interface_upload c, nbfc_dan_cgpan_info d where a.portfolio_name=c.portfolio_name and c.mem_bnk_id||c.mem_zne_id||c.mem_brn_id=b.mem_bnk_id||b.mem_zne_id||b.mem_brn_id and d.dan_id=c.dan_id and DCI_APPROPRIATION_FLAG = 'Y' and DCI_APPROPRIATION_DT is not null and B.MEM_BNK_ID NOT IN ('1026', '1027') and A.dan_type='GF' and b.mem_bnk_id||b.mem_zne_id||b.mem_brn_id= '" + mliId1 + "' and A.TAX_INV_ID not in ('2019-20N00000023','2019-20N00000031','2020-21N00000027')GROUP BY C.PORTFOLIO_NAME,MEM_BANK_NAME,A.DAN_TYPE,d.PAY_ID,a.tax_inv_id) order by mem_bank_name");
			list = (ArrayList) slqQuery.list();	
			
		}

		return list;
	}

	@Override
	public List<Object> proformaInvoiceData(String mliId, HttpSession session) {
		String Role = (String) session.getAttribute("uRole");
		List<Object> list;
		if (Role.equals("CMAKER") || Role.equals("CCHECKER")) {
			String hql = "SELECT (PM.PortFolioName),max(MR.mem_bnk_name),MAX(UI.USR_ID) FROM FileUploadModel IU,PortFolioDetailsInParentTBL PM,DanCgpanDetailsModel DC,MLIRegistration MR,UserInfo UI WHERE PM.portfolio_no = IU.portfolioNo AND DC.DCI_APPROPRIATION_FLAG='N' AND IU.danId = DC.danId AND concat(concat(IU.mem_bank_id,IU.mem_zne_id),IU.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) AND UI.MEM_BNK_ID = MR.mem_bnk_id GROUP BY PM.PortFolioName,MR.mem_bnk_name";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			list = query.list();
		} else {
			String hql = "SELECT (PM.PortFolioName),max(MR.mem_bnk_name),MAX(UI.USR_ID) FROM FileUploadModel IU,PortFolioDetailsInParentTBL PM,DanCgpanDetailsModel DC,MLIRegistration MR,UserInfo UI WHERE PM.portfolio_no = IU.portfolioNo AND DC.DCI_APPROPRIATION_FLAG='N' AND IU.danId = DC.danId AND concat(concat(IU.mem_bank_id,IU.mem_zne_id),IU.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) AND concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) = :MLIID AND UI.MEM_BNK_ID = MR.mem_bnk_id GROUP BY PM.PortFolioName,MR.mem_bnk_name";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("MLIID", mliId);
			list = query.list();
		}

		return list;
	}

	@Override
	public Integer getProformaInvoiceIdCount(String portfolioName) {
		try {
			String hql = "SELECT count(*) FROM ProformaInvoiceDetails where PORTFOLIO_NAME=:PORTFOLIO ";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("PORTFOLIO", portfolioName);
			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			System.out.println("Exception===" + e);
		}
		return 0;
	}

	@Override
	public Integer getMaxProformaInvoiceIdCount() {
		try {
			String hql = "SELECT MAX(PROFORMA_INV_ID)+1 FROM ProformaInvoiceDetails ";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			System.out.println("Exception===" + e);
		}
		return 0;
	}

	@Override
	public String getProformaInvoiceNo(String portfolioName) {
		String Proformaid = "";
		try {
			Session session = sessionFactory.openSession();
			Transaction tn = session.beginTransaction();
			Connection connection = ((SessionImpl) session).connection();

			Query query = session.createQuery(
					"SELECT PROFORMA_INV_ID FROM ProformaInvoiceDetails WHERE PORTFOLIO_NAME = :PORTFOLIO");
			query.setParameter("PORTFOLIO", portfolioName);

			List<Long> rows = query.list();
			System.out.println("current row>>>>>>  " + rows.toString());

			for (Long val : rows) {
				Proformaid = val.toString();
			}

			return Proformaid;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Modified By Parmanand 09-Jan-2019 by shashi on date 03-08-2021
	@Override
	public void callProformaInvoiceReport(String portfolioName, HttpServletResponse response,HttpServletRequest request, CGPANDetailsReportBean bean) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		NBFCHelper nbfcHelper = new NBFCHelper();
		Connection connection = ((SessionImpl) session).connection();
		JasperPrint jasperPrint1 = null;
		if (connection == null) {
			System.out.println("connection is null.........");
		}
		String pdfMsg="";
		String date = bean.getDciGuaranteeeSDate();
		String appDate = bean.getAppsubmittedDate();
		String ProformaNo = bean.getTaxInvoiceId().toString();
		System.out.println("ProformaNo===" + ProformaNo);
		Date myDate = new Date();
		System.out.println(myDate);
		SimpleDateFormat mdyFormat1 = new SimpleDateFormat("MM/dd/yy");

		// String mdy1 = mdyFormat1.format(appDate);
		// System.out.println("sysDate==="+mdy1);
		String[] formatedDate = appDate.split("/");
		String finalDate = formatedDate[0] + formatedDate[1] + formatedDate[2];
		/*
		 * try { System.out.println(mdyFormat1.parse(mdy1)); } catch (ParseException e1)
		 * { e1.printStackTrace(); }
		 */

		String ProformaInvoiceNo = finalDate + "0000" + ProformaNo;
		String State = bean.getState();
		String userId = bean.getUserId();
		//-------------------------------------------------------------------------------------
		double dci_base_amt = bean.getDci_base_amt();
		BigDecimal dci_base_amt1 = new BigDecimal(dci_base_amt);
		BigDecimal dci_base_amt2 = dci_base_amt1.setScale(2, RoundingMode.HALF_UP);
		// String taxBaseAmountInRupes = dci_base_amt2.toPlainString();

		double dci_total_amt = Math.round(bean.getDci_total_amt());
		BigDecimal dci_total_amt1 = new BigDecimal(dci_total_amt);
		BigDecimal dci_total_amt2 = dci_total_amt1.setScale(2, RoundingMode.HALF_UP);// Total Amount(Rs)
		//-----------------------------------------------------------------------------------------		
	
		double outSandingAndGuran = ((bean.getOutstandingAmount1()) * (bean.getGuaranteeFee() / 100));
		
		BigDecimal outstandingAmount=new BigDecimal(bean.getOutstandingAmount1());
		
		System.out.println("The outSandingAndGuran Amount is"+outstandingAmount);		
		
		System.out.println("The outSandingAndGuran Amount is"+outSandingAndGuran);
		double outSandingAndGuranRound = Math.round(outSandingAndGuran);
		String outSandingAndGuranRound1=outSandingAndGuranRound+"";
		System.out.println("The outSandingAndGuranRound1 Amount is"+outSandingAndGuranRound1);
		

		BigDecimal taxAmt = new BigDecimal(bean.getDci_base_amt());// Taxable Amount
		BigDecimal taxAmountInRupes2 = taxAmt.setScale(2, RoundingMode.HALF_UP);

		// String taxAmountInRupes = taxAmountInRupes2.toPlainString();

		double objTotalAmt2 = Math.round(bean.getIgstAmt()) + Math.round(bean.getCgstAmt())	+ Math.round(bean.getSgstAmt()) + outSandingAndGuran;
		BigDecimal objTotalAmt = new BigDecimal(Math.round(objTotalAmt2));
		System.out.println("The Total Amount is"+objTotalAmt);
		BigDecimal objFinalToatlAmt1 = objTotalAmt.setScale(2, RoundingMode.HALF_UP);
		// --------------------------------------------------------------------
		String fianlTotalAmountInRupes = dci_total_amt2.toPlainString();
		long fianlTotalAmountInRupes1 = dci_total_amt2.longValue();// Total Amount(Rs)
		// Added By Parmanand Start
		BigDecimal fianlTotalAmountInRupes11 = new BigDecimal(fianlTotalAmountInRupes1);
		BigDecimal fianlTotalAmountInRupes2 = fianlTotalAmountInRupes11.setScale(2, RoundingMode.HALF_UP);
		String fianlTotalAmountInRupesFinal=fianlTotalAmountInRupes2+"";
		System.out.println("The fianlTotalAmountInRupesFinal Amount is"+fianlTotalAmountInRupesFinal);
		// Added By Parmanand End
		String sim2 = NumberToWordsFormatMethod.inWordFormat(fianlTotalAmountInRupes1);
		//-----------------------------------------------------------------------------------
		/*
		 * long fianlTotalAmountInRupes1 = objFinalToatlAmt1.longValue(); String sim2 =
		 * NumberToWordsFormatMethod .inWordFormat(fianlTotalAmountInRupes1);
		 */
		//----------------------------------------------------------------------
		double IGSTAMT = Math.round(bean.getIgstAmt());
		double CGSTAMT = Math.round(bean.getCgstAmt());
		double SGSTAMT = Math.round(bean.getSgstAmt());

		BigDecimal IGST_AMT = new BigDecimal(IGSTAMT);
		BigDecimal IGST_AMT1 = IGST_AMT.setScale(2, RoundingMode.HALF_UP);

		BigDecimal CGST_AMT = new BigDecimal(CGSTAMT);
		BigDecimal CGST_AMT1 = CGST_AMT.setScale(2, RoundingMode.HALF_UP);

		BigDecimal SGST_AMT = new BigDecimal(SGSTAMT);
		BigDecimal SGST_AMT1 = SGST_AMT.setScale(2, RoundingMode.HALF_UP);

		HashMap map1 = new HashMap();
		map1.put("TotalIgstAmt", IGST_AMT1);
		map1.put("TotalCgstAmt", CGST_AMT1);
		map1.put("TotalSgstAmt", SGST_AMT1);
//		map1.put("TaxInvoiceNo", ProformaInvoiceNo);
		map1.put("TaxAmount", taxAmountInRupes2);// taxAmt tTax
		map1.put("finalAmt", fianlTotalAmountInRupes2);// totalAmt tAmt
		// map1.put("finalAmt", fianlTotalAmountInRupes1);// totalAmt tAmt
		map1.put("TotalAmount", dci_total_amt2);// totalAmt tAmt
		map1.put("TotalAmountInWords", sim2);
		map1.put("StateName", State);
		map1.put("portfolioName", portfolioName);
		map1.put("userId", userId);
		
		// added by shashi on date 03-08-2021  create error here
		//--;
		List<Object> list;	
		// get the data from database.
		String hql = "SELECT MAX(dcit.dan_id) AS dan_id, MAX(UI.USR_ID) AS user_id, MAX(MEM_BANK_NAME) AS user_NAME, MAX(UI.MEM_BNK_ID||UI.MEM_ZNE_ID||UI.MEM_BRN_ID) AS BANKID, MAX(IU.MEM_BNK_ID||IU.MEM_ZNE_ID||IU.MEM_BRN_ID) AS BANKID_IU,MAX(MI.MEM_BNK_ID||MI.MEM_ZNE_ID||MI.MEM_BRN_ID) AS BANKID_MI, ROUND(SUM(DCI_AMOUNT_RAISED),2) AS DCI_AMOUNT_RAISED,SUM(DCI_BASE_AMT) AS DCI_BASE_AMT, MAX(IGST_RATE) AS IGST_RATE, TO_CHAR(ROUND(SUM(IGST_AMT),2),'99999999999999999.99') AS IGST_AMT, MAX(CGST_RATE) AS CGST_RATE,TO_CHAR(ROUND(SUM(CGST_AMT),2),'99999999999999999.99') AS CGST_AMT, MAX(SGST_RATE) AS SGST_RATE, TO_CHAR(ROUND(SUM(SGST_AMT),2),'99999999999999999.99') AS SGST_AMT, MAX(MEM_BANK_NAME) AS MEM_BANK_NAME,MAX(MEM_ADDRESS) AS MEM_ADDRESS, MAX(PORTFOLIO_NAME) AS portfoliono, TO_CHAR(ROUND(SUM(SANCTIONED_AMOUNT)),'99999999999999999.99') AS SANCTIONED_AMOUNT,TO_CHAR(ROUND(SUM(GUARANTEE_AMOUNT)),'99999999999999999.99') AS Approved_AMOUNT, MAX(TO_CHAR(DCI_GUARANTEE_START_DT,'dd/mm/yyyy')) AS Gurantee_StartDate,MAX(TENOR_IN_MONTHS) AS Tenure, MAX(TO_CHAR(NBFC_UPLOADED_DATE,'dd/mm/yyyy')) AS Application_SubmittedDate, MAX(mi.COMPANY_PAN) AS PAN_ID, MAX(mi.GSTIN_NO) AS GSTIN, MAX(mi.MEM_STATE_NAME) AS State,  MAX(TO_CHAR(dcit.DCI_GUARANTEE_START_DT,'dd/mm/yyyy')) AS Tax_Date FROM  NBFC_INTERFACE_UPLOAD iu, NBFC_USER_INFO UI, NBFC_MEMBER_INFO MI, NBFC_DAN_CGPAN_INFO dcit WHERE  iu.dan_id = dcit.dan_id AND UI.MEM_BNK_ID||UI.MEM_ZNE_ID||UI.MEM_BRN_ID = IU.MEM_BNK_ID||IU.MEM_ZNE_ID||IU.MEM_BRN_ID AND UI.MEM_BNK_ID||UI.MEM_ZNE_ID||UI.MEM_BRN_ID = MI.MEM_BNK_ID||MI.MEM_ZNE_ID||MI.MEM_BRN_ID AND IU.MEM_BNK_ID||IU.MEM_ZNE_ID||IU.MEM_BRN_ID = MI.MEM_BNK_ID||MI.MEM_ZNE_ID||MI.MEM_BRN_ID AND IU.PORTFOLIO_NAME ='"+portfolioName+"'";
		System.out.println(hql);
		SQLQuery query=session.createSQLQuery(hql);
		list = (ArrayList) query.list();	
		//Query query = sessionFactory.getCurrentSession().createQuery(hql);
		//list = query.list();
		Iterator<Object>  DataOFUser=list.iterator();
		CGPANDetailsReportBean bean11=null;
		while(DataOFUser.hasNext()) {
			 bean11=new CGPANDetailsReportBean();
			Object[] obj1 = (Object[]) DataOFUser.next();
			// new value adding \
			String MLI_NAME=((String)obj1[2]);
			System.out.println("MLI_NAME is"+MLI_NAME);
			bean11.setMLINAME(MLI_NAME);
			//bean11.set
			// loan sanction amount is pending.
			
			bean11.setTaxDate((String)obj1[25]);
			bean11.setGstNo((String)obj1[23]);
			bean11.setAddress((String)obj1[15]);
			bean11.setStateCode((String)obj1[24]);
		}
		String taxDate = bean11.getTaxDate();
		String[] formatedDategf = taxDate.split("/");
		String finalDategf = formatedDategf[0] + formatedDategf[1] + formatedDategf[2];
		/*
		 * try { System.out.println(mdyFormat1.parse(mdy1)); } catch (ParseException e1)
		 * { e1.printStackTrace(); }
		 */

		String ProformaInvoiceNogf = finalDategf + "0000" + ProformaNo;
		map1.put("TaxInvoiceNo", ProformaInvoiceNogf);
		System.out.println("ProformaInvoiceNogf=== "+ ProformaInvoiceNogf);
		
		String contextPath_dkr = request.getSession(false).getServletContext().getRealPath("");	
		String contextPath = PropertyLoader.changeToOSpath(contextPath_dkr);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String strDate = "Invoice".concat(sdf.format(cal.getTime()));
		
		
		try {
			Document document = new Document(PageSize.A4, 50, 50, 30, 20);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			String returnOnPage = "";

			FileOutputStream out = new FileOutputStream(new File(contextPath + "\\Download\\DataCSVFile" + strDate + ".pdf"));
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();
		
			Font drfont10 = FontFactory.getFont(FontFactory.TIMES, 9f);
			Font drfont8 = FontFactory.getFont(FontFactory.TIMES, 6f);
			Font drfont6d = FontFactory.getFont(FontFactory.TIMES, 5f, Font.BOLD);
			Font drfont7d = FontFactory.getFont(FontFactory.TIMES, 7f, Font.BOLD);
			Font drfont6 = FontFactory.getFont(FontFactory.TIMES, 5f);
			Paragraph centerAlignedParagraph1 = new Paragraph(" Proforma Invoice", drfont10);
			
			centerAlignedParagraph1.setAlignment(Element.ALIGN_CENTER);
			
			Paragraph centerAlignedParagraph2 = new Paragraph("  Credit Guarantee Fund Trust For Micro and Small Enterprises(CGTMSE)", drfont10);
			centerAlignedParagraph2.setAlignment(Element.ALIGN_CENTER);
			Paragraph centerAlignedParagraphd = new Paragraph(
					"Credit Guarantee Fund Trust For Micro and Small Enterprises(CGTMSE)", drfont8);
			centerAlignedParagraphd.setAlignment(Element.ALIGN_CENTER);
			Paragraph centerAlignedParagraph3 = new Paragraph("Set up of Govt.Of India SIDBI", drfont6);
			centerAlignedParagraph3.setAlignment(Element.ALIGN_CENTER);
			Paragraph centerAlignedParagraph4 = new Paragraph("GSTN:27AAATC2613D1ZC STATE:Maharastra-MH", drfont8);
			centerAlignedParagraph4.setAlignment(Element.ALIGN_CENTER);
			Paragraph centerAlignedParagraph5 = new Paragraph("SIDBI Swavalamban Bhavan, 1st Floor, C-11,G-Block,Bandra Kurla Compledx,Bandra(East), Mumbai 400 051"	+ new Chunk("www.cgtmse.in").setUnderline(0.1f, -2f),	drfont6);
			centerAlignedParagraph5.setAlignment(Element.ALIGN_CENTER);			
			Paragraph centerAlignedParagraph6 = new Paragraph("Proforma Invoice No:" + ProformaInvoiceNogf+""
					+ "\t\t\t\t\t     \t   \t                        "
					+ "               "
					+ ""
					+ "                                                                      Date:"+bean11.getTaxDate(),	drfont7d);
			centerAlignedParagraph6.setAlignment(Element.ALIGN_LEFT);
			
 
			
			Paragraph centerAlignedParagraph8 = new Paragraph("MLI Name:" + bean11.getMLINAME()+"                                        "
					+ "                                                GSTIN: "+bean11.getGstNo(),	drfont7d);
			centerAlignedParagraph8.setAlignment(Element.ALIGN_LEFT);
			
			
			Paragraph centerAlignedParagraph10 = new Paragraph("Address:" + bean11.getAddress(),drfont7d);
			centerAlignedParagraph10.setAlignment(Element.ALIGN_LEFT);
			
			Paragraph centerAlignedParagraph11 = new Paragraph(" State:"+bean.getState(),	drfont7d);
			centerAlignedParagraph11.setAlignment(Element.ALIGN_MIDDLE);
			
			Paragraph centerAlignedParagraph12 = new Paragraph("State Code:" + bean11.getStateCode(),	drfont7d);
			centerAlignedParagraph12.setAlignment(Element.ALIGN_MIDDLE);				
			

			document.add(centerAlignedParagraph1);
			//document.add(centerAlignedParagraph11);
			document.add(centerAlignedParagraph2);
			document.add(centerAlignedParagraphd);
			document.add(centerAlignedParagraph3);
			document.add(centerAlignedParagraph4);
			document.add(centerAlignedParagraph5);
			document.add(centerAlignedParagraph6);
			document.add(centerAlignedParagraph8);
			document.add(centerAlignedParagraph10);
			document.add(centerAlignedParagraph11);
			document.add(centerAlignedParagraph12);

            // now adding the tabel in the docment 
			document.add(new Paragraph("\n"));
			
			
			PdfPTable table = new PdfPTable(13); // No Of columns.
			table.setWidthPercentage(100); // Width 100%
			float[] columnWidths = { 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f,1f};
			table.setWidths(columnWidths);
			
			
			PdfPCell c1 = new PdfPCell(new Phrase("Portfolio No.", drfont6d));
			c1.setBorder(Rectangle.BOX);

			table.addCell(c1);
			c1 = new PdfPCell(new Phrase("Name Of NBFC", drfont6d));
			c1.setBorder(Rectangle.BOX);

			table.addCell(c1);
	    	c1 = new PdfPCell(new Phrase("Loan Sanction" + "Amount(Rs.)", drfont6d));
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1); 

			
			c1 = new PdfPCell(new Phrase("Guarantee Approved" + "Amount(Rs.)", drfont6d));
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Application Submitted Date", drfont6d));
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Phrase("Taxable" + "Amount(Rs.)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("IGST Rate(%)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("IGST Amount(Rs.)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("CGST Rate(%)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("CGST Amount(Rs.)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("SGST Rate(%)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("SGST Amount(Rs.)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Total Amount(Rs.) ", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			table.setHeaderRows(1);
			PdfPCell cell1 = new PdfPCell(new Paragraph(bean.getPortfolioName(), drfont6)); // column 1

			cell1.setBorder(Rectangle.BOX);

			PdfPCell cell2 = new PdfPCell(new Paragraph(bean11.getMLINAME(), drfont6));// column 2
			cell2.setBorder(Rectangle.BOX);
			
			BigDecimal value=bean.getSanctionAMt();
			String LoanSanctionAmount=value+"";
			PdfPCell cell14 = new PdfPCell(new Paragraph(LoanSanctionAmount, drfont6));// column 2
			cell14.setBorder(Rectangle.BOX);
			
			float GuaranteeApprovedAmount=(float) bean.getOutstandingAmount1();
			//BigDecimal GApproveAmount=new BigDecimal(GuaranteeApprovedAmount);
			
			BigDecimal GApproveAmount1 = new BigDecimal(Math.round(GuaranteeApprovedAmount));
			System.out.println("The GApproveAmount1 Amount is"+GApproveAmount1);
			BigDecimal GApproveAmount11 = objTotalAmt.setScale(2, RoundingMode.HALF_UP);
			
			System.out.println("-------------ghfjhgjgjhghgjghgj------"+GApproveAmount11);
			
			//PdfPCell cell15 = new PdfPCell(new Paragraph(GApproveAmount1.toString(), drfont6));// column 2   outstandingAmount
			
			String outstandingAMT=outstandingAmount+"";
			
			PdfPCell cell15 = new PdfPCell(new Paragraph(outstandingAMT.toString(),drfont6));// column 2 
			cell15.setBorder(Rectangle.BOX);
			

			//PdfPCell cell3 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getOutStandingAmount().toString()), drfont6)); // column 3
			PdfPCell cell3 = new PdfPCell(new Paragraph(bean.getAppsubmittedDate(), drfont6)); // column 3
			
			// outstanding
			// amount
			cell3.setBorder(Rectangle.BOX);

		//	PdfPCell cell5 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getBaseAmount().toString()), drfont6)); // column
			PdfPCell cell5 = new PdfPCell(new Paragraph(taxAmountInRupes2.toString(), drfont6)); // column
			// 5
			// Taxable
			// Amount
			cell5.setBorder(Rectangle.BOX);
			
			// done till here

			// System.out.println("```````````````````2`````````````````````````````````");
			PdfPCell cell7;
			cell7 = new PdfPCell(new Paragraph("18", drfont6)); // column 7 invoiceObj.getCity()
			cell7.setBorder(Rectangle.BOX);
			// System.out.println("```````````````````3`````````````````````````````````");
			PdfPCell cell8;
			if(bean.getIgstAmt() != null) {
			 cell8 = new PdfPCell(new Paragraph(String.valueOf(IGST_AMT1), drfont6));// column
			cell8.setBorder(Rectangle.BOX);
			//PdfPCell cell8 = new PdfPCell(new Paragraph(amountTrilWithZeros("12345.67"), drfont6));// column
			}else {
			cell8 = new PdfPCell(new Paragraph(String.valueOf("0"), drfont6));
			cell8.setBorder(Rectangle.BOX);
			}
			// 8
			// invoiceObj.getCity()
			//cell8.setBorder(Rectangle.BOX);

			PdfPCell cell9 = new PdfPCell(new Paragraph("9", drfont6));// column 9 invoiceObj.getCity()
			cell9.setBorder(Rectangle.BOX);
			// System.out.println("````````````````````4``````````````````````````````````");
			PdfPCell cell10;
			String cgst_AmountRate=CGST_AMT1+"";
			if(cgst_AmountRate !=null ) {
			
			//cell10 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getCgstAmount().toString()), drfont6));// column
			cell10 = new PdfPCell(new Paragraph(String.valueOf(CGST_AMT1), drfont6));// column
			}else {
				cell10 = new PdfPCell(new Paragraph(String.valueOf("0.0"), drfont6));//
			}
			
			// 10
			// invoiceObj.getCity()
			cell10.setBorder(Rectangle.BOX);

			// System.out.println("````````````````````5``````````````````````````````````");
			PdfPCell cell11 = new PdfPCell(new Paragraph("9", drfont6));// column 11
			// invoiceObj.getCity() //
			cell11.setBorder(Rectangle.BOX);

			// System.out.println("````````````````````6`````````````````````````````````");
			PdfPCell cell12 = null;
			String Sgst_Amount=SGST_AMT1+"";
			if(Sgst_Amount !=null) {
			//cell12 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getSgstAmount().toString()), drfont6));// column
			cell12 = new PdfPCell(new Paragraph(String.valueOf(SGST_AMT1),drfont6));// column
			}else {
				cell12 = new PdfPCell(new Paragraph(String.valueOf("0.0"),drfont6));// column
			}
			// 12
			// invoiceObj.getCity()
			cell12.setBorder(Rectangle.BOX);

			//PdfPCell cell13 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getDanAmount().toString()), drfont6));
			PdfPCell cell13 = new PdfPCell(new Paragraph(amountTrilWithZeros(fianlTotalAmountInRupesFinal), drfont6));
			cell13.setBorder(Rectangle.BOX);

			table.addCell(cell1);
			table.addCell(cell2);
			table.addCell(cell14);
			table.addCell(cell15);
			table.addCell(cell3);
			table.addCell(cell5);
			table.addCell(cell7);
			table.addCell(cell8);
			table.addCell(cell9);
			table.addCell(cell10);
			table.addCell(cell11);
			table.addCell(cell12);
			table.addCell(cell13);
			document.left(100f);
			document.top(150f);
			document.add(table);
			
			
			document.add(new Paragraph("\n"));
			//String totAmountInWords = NumberToWordsFormatMethod.inWordFormat(sim2); // Total
			// Amount(Rs.)
			// System.out.println("````````````````````9``````````````````````````````````");
			Paragraph totalPayAmt = new Paragraph(" Total Amount Payable RS. In Words:" + sim2, drfont7d);
			totalPayAmt.setAlignment(Element.ALIGN_LEFT);
			document.add(totalPayAmt);
			document.add(new Paragraph("\n\n"));
			Paragraph termCondition = new Paragraph("  Terms and conditions:", drfont7d);
			termCondition.setAlignment(Element.ALIGN_LEFT);

			document.add(termCondition);
			
			Paragraph termCondetail = new Paragraph(
					" 1) The Courts in Mumbai shall have exclusive jurisdiction in respect of any dispute arising in connection with the above matter.",
					drfont7d);
			document.add(termCondetail);

			Paragraph termCondition2 = new Paragraph(
					"2) Please revert hack to us within 7 days of receipt of the invoice in case of discreapancy in state code and/or GSTIN as mentioned herein the please of supply or your actual GSTIN by email on",
					drfont7d);

			// SAC code/Service Category:
			Paragraph sacServiceCat = new Paragraph("\r \n SAC code/Service Category:", drfont7d);
			sacServiceCat.setAlignment(Element.ALIGN_LEFT);
			document.add(sacServiceCat);

			Paragraph toatalCond3 = new Paragraph(
					"Code under GST:997113 - Credit - granting services including stand - by commitment, guarantees & securities.",
					drfont7d);
			toatalCond3.setAlignment(Element.ALIGN_LEFT);

			document.add(toatalCond3);
			// *** This is computer generated document, no signature required******
			Paragraph totalCond4 = new Paragraph(
					"\r \n *** This is computer generated document, no signature required******", drfont7d);
			totalCond4.setAlignment(Element.ALIGN_LEFT);
			document.add(totalCond4);

			Paragraph autCgt = new Paragraph(" For CGTMSE", drfont7d);
			autCgt.setAlignment(Element.ALIGN_RIGHT);
			document.add(autCgt);

			Paragraph sd = new Paragraph(" Sd/-", drfont7d);
			sd.setAlignment(Element.ALIGN_RIGHT);
			document.add(sd);

			Paragraph sigCgt = new Paragraph("  Autorised Signatory", drfont7d);
			sigCgt.setAlignment(Element.ALIGN_RIGHT);
			document.add(sigCgt);
			document.add(new Paragraph("   "));
			
			
			pdfMsg = "PDF Successfully Genreated";
			System.out.println("::::::::::::PDF Generation Message:::::::::::" + pdfMsg);
			document.close();
			OutputStream os = response.getOutputStream();
			FileInputStream fis = new FileInputStream(
					new File(contextPath + "\\Download\\DataCSVFile" + strDate + ".pdf"));
			int x = fis.available();
			byte b[] = new byte[x];
			System.out.println(" byte obj size~~~~~~~~~~~~~~~~~~~" + b.length);
			fis.read(b);
			if (response != null)
				response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", (new StringBuilder("attachment; filename=ProformaInvoiceGF"))
					.append(bean.getDanId()).append(".pdf").toString());
			os.write(b);
			os.flush(); 
		} catch (FileNotFoundException e) {
			pdfMsg = "Error in pdf download." + e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			pdfMsg = "Error in pdf download.." + e.getMessage();
			e.printStackTrace();
		} catch (Exception e) {
			pdfMsg = "Error in pdf download..." + e.getMessage();
			e.printStackTrace();
			System.out.println("pdfMsg------------" + pdfMsg);

		}
			
			
		 
		
		
		
	/*	
		
		try {
			String filename = "ParaformaInvoiceReport1";
			String reportfileName = "ProformaReport" + portfolioName + ".pdf";
			String tempFolderPath = "/WEB-INF/JReport/" + filename;
			System.out.println("path:" + tempFolderPath);
			File tempFolder = new File(tempFolderPath);
			if (!tempFolder.exists()) {
				tempFolder.mkdirs();
			}
			String jasperFilePath = tempFolderPath + ".jasper";
			File reportFile = new File(jasperFilePath);
			if (!reportFile.exists()) {
				InputStream jRXmlStream = request.getSession().getServletContext()
						.getResourceAsStream("/WEB-INF/JReport/" + filename + ".jrxml");
				JasperDesign jasperDesign = JRXmlLoader.load(jRXmlStream);
				JasperCompileManager.compileReportToFile(jasperDesign, jasperFilePath);
				System.out.println(jasperFilePath);
			}
			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map1, connection);
			JRAbstractExporter exporterPDF = new JRPdfExporter();
			exporterPDF.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			try {
				exporterPDF.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			response.setHeader("Content-Disposition", "attachment;filename=" + reportfileName);
			response.setContentType("application/pdf");
			exporterPDF.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tn.commit();

		}
		
		*/

	}

	@Override
	public void insertProformaInvoiceDetails(ProformaInvoiceDetails proformaObj1) {
		sessionFactory.getCurrentSession().save(proformaObj1);
	}

	@Override
	public List<Object> getTaxInvoiceDataASF(String mliId, HttpSession session) {
		String Role = (String) session.getAttribute("uRole");
		List<Object> list;
		if (Role.equals("CMAKER") || Role.equals("CCHECKER")) {
			//			String hql = "SELECT  DISTINCT(IU.PORTFOLIO_NAME),(MR.mem_bnk_name),MAX(AD.ASF_YEAR) FROM FileUploadModel IU,DanCgpanDetailsModel DC,MLIRegistration MR,DemandDeviceModel DM,OutStandingAmtModel AM, ASFDetailModel AD WHERE  DC.DCI_APPROPRIATION_FLAG='Y' AND concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) AND DM.danId=DC.danId AND DM.Dan_type='AF' AND AM.DAN_ID = DC.danId AND AM.CGPAN = IU.CGPAN AND DC.Dci_appropriationDate >= '1-OCT-2020' and AD.CGPAN = AM.CGPAN GROUP BY IU.PORTFOLIO_NAME,MR.mem_bnk_name";
			//			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			//			list = query.list();

			String hql = "SELECT DISTINCT(IU.PORTFOLIO_NAME),(MR.mem_bnk_name),MAX(AD.ASF_YEAR) FROM FileUploadModel IU,DanCgpanDetailsModel DC,MLIRegistration MR,DemandDeviceModel DM,OutStandingAmtModel AM ,ASFDetailModel AD WHERE  DC.DCI_APPROPRIATION_FLAG='Y' AND concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) AND DM.danId=DC.danId AND DM.Dan_type='AF' AND AM.DAN_ID = DC.danId AND AM.CGPAN = IU.CGPAN AND AD.CGPAN = AM.CGPAN AND  DC.Dci_appropriationDate >= '1-OCT-2020' GROUP BY IU.PORTFOLIO_NAME,MR.mem_bnk_name";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			System.out.println("--------query-----------" + query);
			list = query.list();
		} else {
			//			String hql = "SELECT  DISTINCT(IU.PORTFOLIO_NAME),(MR.mem_bnk_name),MAX(AD.ASF_YEAR)_YEAR) FROM FileUploadModel IU,DanCgpanDetailsModel DC,MLIRegistration MR,DemandDeviceModel DM,OutStandingAmtModel AM,ASFDetailModel AD WHERE  DC.DCI_APPROPRIATION_FLAG='Y' AND concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) and concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = :MLIID AND DM.danId=DC.danId AND DM.Dan_type='AF' AND AM.DAN_ID = DC.danId AND AM.CGPAN = IU.CGPAN AND DC.Dci_appropriationDate >= '1-OCT-2020' and AD.CGPAN = AM.CGPAN GROUP BY IU.PORTFOLIO_NAME,MR.mem_bnk_name";
			//			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			//			query.setParameter("MLIID", mliId);
			//			list = query.list();

			String hql = "SELECT DISTINCT(IU.PORTFOLIO_NAME),(MR.mem_bnk_name),MAX(AD.ASF_YEAR) FROM FileUploadModel IU,DanCgpanDetailsModel DC,MLIRegistration MR,DemandDeviceModel DM,OutStandingAmtModel AM ,ASFDetailModel AD WHERE  DC.DCI_APPROPRIATION_FLAG='Y' AND concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) and  concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = :MLIID AND DM.danId=DC.danId AND DM.Dan_type='AF' AND AM.DAN_ID = DC.danId AND AM.CGPAN = IU.CGPAN AND AD.CGPAN = AM.CGPAN AND DC.Dci_appropriationDate >= '1-OCT-2020' GROUP BY IU.PORTFOLIO_NAME,MR.mem_bnk_name";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("MLIID", mliId);
			System.out.println("--------query-----------" + query);
			list = query.list();
		}

		return list;

	}

	@Override
	public Integer getTaxInvoiceIdCountASF(String danId) {
		try {
			String hql = "SELECT count(*) FROM TaxInvoiceDetailsASF where DAN_ID=:DANID ";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("DANID", danId);
			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			System.out.println("Exception===" + e);
		}
		return 0;
	}

	@Override
	public List<Object> getTaxReportDetailsASF(String danId, String year) {
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		Connection connection = ((SessionImpl) session).connection();
 
 
		String sql="SELECT (DI.DAN_id) DAN_ID, ROUND ((di.IGST_AMT)) IGST_AMT, (ROUND (di.CGST_AMT)) CGST_AMT,  (ROUND (di.SGST_AMT)) SGST_AMT, ROUND (SUM (IU.OUTSTANDING_AMOUNT)) OUTSTANDING_AMT, (di.DCI_APPROPRIATION_DT) AS APPR_DT, (di.DCI_STANDARD_RATE) GUARANTEE_FEE,  (di.DCI_GUARANTEE_START_DT) AS GSD, MI.MEM_STATE_NAME STATE_CODE, SM.STE_NAME STATE_NAME, MAX (iu.NBFC_UPLOADED_DATE) AS Application_SubmittedDate,ROUND ((di.DCI_AMOUNT_RAISED)) AS dan_amount, ROUND ( (di.DCI_BASE_AMT)) AS BASE_AMT, (GSTIN_NO) GST_NO, (MEM_ADDRESS) MEM_ADDRESS FROM nbfc_interface_upload iu, nbfc_dan_cgpan_info di, nbfc_asf_detail ad, nbfc_member_info mi, NBFC_STATE_MASTER SM  WHERE ad.ASF_DAN_ID = DI.DAN_ID AND ad.cgpan = iu.cgpan AND mi.mem_bnk_id = iu.mem_bnk_id AND mi.mem_zne_id = iu.mem_zne_id AND mi.mem_brn_id = iu.mem_brn_id AND MI.MEM_STATE_NAME = SM.STE_CODE AND ad.ASF_YEAR = '" + year + "' AND iu.portfolio_name = '" + danId + "' AND di.DCI_APPROPRIATION_DT IS NOT NULL GROUP BY (DI.DAN_id), (di.DCI_APPROPRIATION_DT), (di.DCI_STANDARD_RATE),  (di.DCI_GUARANTEE_START_DT),MI.MEM_STATE_NAME,SM.STE_NAME,(GSTIN_NO), (MEM_ADDRESS), ROUND ((di.DCI_AMOUNT_RAISED)),ROUND ( (di.DCI_BASE_AMT)),ROUND ( (di.IGST_AMT)) ,(ROUND (di.CGST_AMT)) , (ROUND (di.SGST_AMT))";
 
		System.out.println("Query-------" + sql);
		SQLQuery query = session.createSQLQuery(sql);
		List<Object> list = query.list();
		return list;
	}
	// for proforma invoice asf created by shashi on date - 22-10-2021
	@Override
	public List<Object> getProformaReportDetailsASF(String danId, String year) {
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		Connection connection = ((SessionImpl) session).connection();
 
 
		String sql="SELECT (DI.DAN_id) DAN_ID, ROUND ((di.IGST_AMT)) IGST_AMT, (ROUND (di.CGST_AMT)) CGST_AMT,  (ROUND (di.SGST_AMT)) SGST_AMT, ROUND (SUM (IU.OUTSTANDING_AMOUNT)) OUTSTANDING_AMT, (di.DCI_APPROPRIATION_DT) AS APPR_DT, (di.DCI_STANDARD_RATE) GUARANTEE_FEE,  (di.DCI_GUARANTEE_START_DT) AS GSD, MI.MEM_STATE_NAME STATE_CODE, SM.STE_NAME STATE_NAME, MAX (iu.NBFC_UPLOADED_DATE) AS Application_SubmittedDate,ROUND ((di.DCI_AMOUNT_RAISED)) AS dan_amount, ROUND ( (di.DCI_BASE_AMT)) AS BASE_AMT, (GSTIN_NO) GST_NO, (MEM_ADDRESS) MEM_ADDRESS FROM nbfc_interface_upload iu, nbfc_dan_cgpan_info di, nbfc_asf_detail ad, nbfc_member_info mi, NBFC_STATE_MASTER SM  WHERE ad.ASF_DAN_ID = DI.DAN_ID AND ad.cgpan = iu.cgpan AND mi.mem_bnk_id = iu.mem_bnk_id AND mi.mem_zne_id = iu.mem_zne_id AND mi.mem_brn_id = iu.mem_brn_id AND MI.MEM_STATE_NAME = SM.STE_CODE AND ad.ASF_YEAR = '" + year + "' AND iu.portfolio_name = '" + danId + "' AND di.DCI_APPROPRIATION_DT IS NULL GROUP BY (DI.DAN_id), (di.DCI_APPROPRIATION_DT), (di.DCI_STANDARD_RATE),  (di.DCI_GUARANTEE_START_DT),MI.MEM_STATE_NAME,SM.STE_NAME,(GSTIN_NO), (MEM_ADDRESS), ROUND ((di.DCI_AMOUNT_RAISED)),ROUND ( (di.DCI_BASE_AMT)),ROUND ( (di.IGST_AMT)) ,(ROUND (di.CGST_AMT)) , (ROUND (di.SGST_AMT))";
 
		System.out.println("Query-------" + sql);
		SQLQuery query = session.createSQLQuery(sql);
		List<Object> list = query.list();
		return list;
	}


	// Modified By Parmanand 09-Jan-2019
	@Override
	public void callTaxInvoiceReportASF(String danId, HttpServletResponse response, HttpServletRequest request,
			CGPANDetailsReportBean bean) {
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		Connection connection = ((SessionImpl) session).connection();
		JasperPrint jasperPrint1 = null;

		NBFCHelper nbfcHelper = new NBFCHelper();
		String State = bean.getState();
		String DAN = bean.getDanId();
		String userId = bean.getUserId();
		String date = bean.getDciAppropriationDate();
		String TaxNo = bean.getTaxid();
		String YEAR = bean.getAsfYear();
		//		String fyBasedOnStartAndEndDate = nbfcHelper.getCurrentYear();
		//		StringBuilder sb = new StringBuilder(fyBasedOnStartAndEndDate);
		//		sb.delete(5, 7);
		//		String fy = sb.toString();

		// String TaxInvoiceNo = fy + "N0000000" + TaxNo;

		// ---------------------TAX AMOUNT------------------------------------------
		double outSandingAndGuran = bean.getDci_base_amt();
		double outSandingAndGuranRound = Math.round(outSandingAndGuran);
		BigDecimal taxAmt = new BigDecimal(outSandingAndGuranRound);
		BigDecimal taxAmountInRupes2 = taxAmt.setScale(2, RoundingMode.HALF_UP);
		String taxAmountInRupes = taxAmountInRupes2.toPlainString();
		// ------------------TOTAL DAN
		// AMOUNT---------------------------------------------
		double objTotalAmt2 = bean.getDan_Amt();
		BigDecimal objTotalAmt = new BigDecimal(Math.round(objTotalAmt2));
		BigDecimal objFinalToatlAmt1 = objTotalAmt.setScale(2, RoundingMode.HALF_UP);
		String fianlTotalAmountInRupes = objFinalToatlAmt1.toPlainString();
		long fianlTotalAmountInRupes1 = objFinalToatlAmt1.longValue();
		String sim2 = NumberToWordsFormatMethod.inWordFormat(fianlTotalAmountInRupes1);
		// --------------------------------------------------------------------------------------

		/*
		 * double outSandingAndGuran = ((bean.getOutstandingAmount1()
		 * bean.getGuaranteeFee() / 100)); double outSandingAndGuranRound =
		 * Math.round(outSandingAndGuran); BigDecimal taxAmt = new
		 * BigDecimal(outSandingAndGuranRound); BigDecimal taxAmountInRupes2 =
		 * taxAmt.setScale(2, RoundingMode.HALF_UP);
		 */

		/*
		 * 
		 * String taxAmountInRupes = taxAmountInRupes2.toPlainString(); double
		 * objTotalAmt2 = bean.getIgstAmt() + bean.getCgstAmt() + bean.getSgstAmt() +
		 * outSandingAndGuran; BigDecimal objTotalAmt = new
		 * BigDecimal(Math.round(objTotalAmt2)); BigDecimal objFinalToatlAmt1 =
		 * objTotalAmt.setScale(2, RoundingMode.HALF_UP); String fianlTotalAmountInRupes
		 * = objFinalToatlAmt1.toPlainString(); long fianlTotalAmountInRupes1 =
		 * objFinalToatlAmt1.longValue(); String sim2 = NumberToWordsFormatMethod
		 * .inWordFormat(fianlTotalAmountInRupes1);
		 */

		double IGSTAMT = Math.round(bean.getIgstAmt());
		double CGSTAMT = Math.round(bean.getCgstAmt());
		double SGSTAMT = Math.round(bean.getSgstAmt());

		BigDecimal IGST_AMT = new BigDecimal(IGSTAMT);
		BigDecimal IGST_AMT1 = IGST_AMT.setScale(2, RoundingMode.HALF_UP);

		BigDecimal CGST_AMT = new BigDecimal(CGSTAMT);
		BigDecimal CGST_AMT1 = CGST_AMT.setScale(2, RoundingMode.HALF_UP);

		BigDecimal SGST_AMT = new BigDecimal(SGSTAMT);
		BigDecimal SGST_AMT1 = SGST_AMT.setScale(2, RoundingMode.HALF_UP);

		HashMap map = new HashMap();
		map.put("TotalIgstAmt", IGST_AMT1);
		map.put("TotalCgstAmt", CGST_AMT1);
		map.put("TotalSgstAmt", SGST_AMT1);
		map.put("TaxInvoiceNo", TaxNo);
		map.put("TaxAmount", taxAmountInRupes2);
		map.put("TotalAmount", objFinalToatlAmt1);
		map.put("TotalAmountInWords", sim2);
		map.put("StateName", State);
		map.put("danId", danId);
		map.put("userId", userId);
		map.put("ASFYEAR", YEAR);
		try {
			String filename = "TaxInvoiceReportASF1";
			String reportfileName = "TaxreportASF" + danId + ".pdf";
			String tempFolderPath = "/WEB-INF/JReport/" + filename;
			System.out.println("path:" + tempFolderPath);
			File tempFolder = new File(tempFolderPath);
			if (!tempFolder.exists()) {
				tempFolder.mkdirs();
			}
			String jasperFilePath = tempFolderPath + ".jasper";
			File reportFile = new File(jasperFilePath);
			if (!reportFile.exists()) {
				InputStream jRXmlStream = request.getSession().getServletContext()
						.getResourceAsStream("/WEB-INF/JReport/" + filename + ".jrxml");
				JasperDesign jasperDesign = JRXmlLoader.load(jRXmlStream);
				JasperCompileManager.compileReportToFile(jasperDesign, jasperFilePath);
				System.out.println(jasperFilePath);
			}
			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, connection);
			JRAbstractExporter exporterPDF = new JRPdfExporter();
			exporterPDF.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			try {
				exporterPDF.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			response.setHeader("Content-Disposition", "attachment;filename=" + reportfileName);
			response.setContentType("application/pdf");
			exporterPDF.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tn.commit();

		}

	}

	@Override
	public Integer getMaxTaxInvoiceIdCountASF() {
		try {
			String hql = "SELECT MAX(TAX_INV_ID)+1 FROM TaxInvoiceDetailsASF ";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			System.out.println("Exception===" + e);
		}
		return 0;
	}

	@Override
	public void insertTaxInvoiceDetailsASF(TaxInvoiceDetailsASF taxDetailsObj) {
		sessionFactory.getCurrentSession().save(taxDetailsObj);
	}

	@Override
	public String getTaxInvoiceNoASF(String danId) {
		String Taxid = "";
		try {
			Session session = sessionFactory.openSession();
			Transaction tn = session.beginTransaction();
			Connection connection = ((SessionImpl) session).connection();
			SQLQuery slqQuery = session
					.createSQLQuery("SELECT TAX_INV_ID FROM NBFC_GST_TAX_INVOICE WHERE PORTFOLIO_NAME = '" + danId
							+ "' AND DAN_TYPE='AF'");
			// SQLQuery q = session.reateSQLQuery("select
			// To_char(FYSTARTDATE('01-Apr-2018'),'RRRR') ||--'
			// ||(To_char(FYSTARTDATE('01-Mar-2019'),'RRRR')+1) as fyyear from dual");
			ArrayList fyList = (ArrayList) slqQuery.list();
			/*
			 * Query query = session
			 * .createQuery("SELECT TAX_INV_ID FROM TaxInvoiceDetails WHERE PORTFOLIO_NAME = :PORTFOLIO"
			 * ); query.setParameter("PORTFOLIO", portfolioName); List<String> rows =
			 * query.list(); System.out.println("current row>>>>>>  " + rows.toString());
			 */
			Iterator itr = fyList.iterator();
			while (itr.hasNext()) {
				Taxid = (String) itr.next();
			}

			return Taxid;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object> proformaInvoiceDataASF(String mliId, HttpSession session) {
		String Role = (String) session.getAttribute("uRole");
		List<Object> list;
		if (Role.equals("CMAKER") || Role.equals("CCHECKER")) {
			String hql = "SELECT DISTINCT(IU.PORTFOLIO_NAME),(MR.mem_bnk_name),MAX(AD.ASF_YEAR) FROM FileUploadModel IU,DanCgpanDetailsModel DC,MLIRegistration MR,DemandDeviceModel DM,OutStandingAmtModel AM ,ASFDetailModel AD WHERE  DC.DCI_APPROPRIATION_FLAG='N' AND concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) AND DM.danId=DC.danId AND DM.Dan_type='AF' AND AM.DAN_ID = DC.danId AND AM.CGPAN = IU.CGPAN AND AD.CGPAN = AM.CGPAN  GROUP BY IU.PORTFOLIO_NAME,MR.mem_bnk_name";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			System.out.println("--------query-----------" + query);
			list = query.list();
		} else {
			String hql = "SELECT DISTINCT(IU.PORTFOLIO_NAME),(MR.mem_bnk_name),MAX(AD.ASF_YEAR) FROM FileUploadModel IU,DanCgpanDetailsModel DC,MLIRegistration MR,DemandDeviceModel DM,OutStandingAmtModel AM ,ASFDetailModel AD WHERE  DC.DCI_APPROPRIATION_FLAG='N' AND concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) and  concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = :MLIID AND DM.danId=DC.danId AND DM.Dan_type='AF' AND AM.DAN_ID = DC.danId AND AM.CGPAN = IU.CGPAN AND AD.CGPAN = AM.CGPAN GROUP BY IU.PORTFOLIO_NAME,MR.mem_bnk_name";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("MLIID", mliId);
			System.out.println("--------query-----------" + query);
			list = query.list();
		}

		return list;
	}

	@Override
	public Integer getProformaInvoiceIdCountASF(String danId) {
		try {
			String hql = "SELECT count(*) FROM ProformaInvoiceDetailsASF where PORTFOLIO_NAME=:DAN ";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("DAN", danId);
			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			System.out.println("Exception===" + e);
		}
		return 0;
	}

	@Override
	public Integer getMaxProformaInvoiceIdCountASF() {
		try {
			String hql = "SELECT MAX(PROFORMA_INV_ID)+1 FROM ProformaInvoiceDetailsASF ";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			System.out.println("Exception===" + e);
		}
		return 0;
	}

	// Modified By Parmanand 09-Jan-2019
	@Override
	public void callProformaInvoiceReportASF(String danId, HttpServletResponse response, HttpServletRequest request,
			CGPANDetailsReportBean bean) {
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		Connection connection = ((SessionImpl) session).connection();
		JasperPrint jasperPrint1 = null;
		if (connection == null) {
			System.out.println("connection is null.........");
		}

		String State = bean.getState();
		String YEAR = bean.getAsfYear();
		String DAN = bean.getDanId();
		String userId = bean.getUserId();
		String date = bean.getDciGuaranteeeSDate();
		String TaxNo = bean.getTaxid();
		String id = date.replace("/", "");
		String TaxInvoiceNo = id + TaxNo;
		System.out.println("taxno--------------" + TaxInvoiceNo);

		/*
		 * double outSandingAndGuran = ((bean.getOutstandingAmount1()
		 * bean.getGuaranteeFee() / 100)); double outSandingAndGuranRound =
		 * Math.round(outSandingAndGuran); BigDecimal taxAmt = new
		 * BigDecimal(outSandingAndGuranRound); BigDecimal taxAmountInRupes2 =
		 * taxAmt.setScale(2, RoundingMode.HALF_UP); String taxAmountInRupes =
		 * taxAmountInRupes2.toPlainString(); double objTotalAmt2 = bean.getIgstAmt() +
		 * bean.getCgstAmt() + bean.getSgstAmt() + outSandingAndGuran; BigDecimal
		 * objTotalAmt = new BigDecimal(Math.round(objTotalAmt2)); BigDecimal
		 * objFinalToatlAmt1 = objTotalAmt.setScale(2, RoundingMode.HALF_UP); String
		 * fianlTotalAmountInRupes = objFinalToatlAmt1.toPlainString(); long
		 * fianlTotalAmountInRupes1 = objFinalToatlAmt1.longValue(); String sim2 =
		 * NumberToWordsFormatMethod .inWordFormat(fianlTotalAmountInRupes1);
		 */

		// ---------------------TAX AMOUNT------------------------------------------
		double outSandingAndGuran = bean.getDci_base_amt();
		double outSandingAndGuranRound = Math.round(outSandingAndGuran);
		BigDecimal taxAmt = new BigDecimal(outSandingAndGuranRound);
		BigDecimal taxAmountInRupes2 = taxAmt.setScale(2, RoundingMode.HALF_UP);
		String taxAmountInRupes = taxAmountInRupes2.toPlainString();
		// ------------------TOTAL DAN
		// AMOUNT---------------------------------------------
		double objTotalAmt2 = bean.getDan_Amt();
		BigDecimal objTotalAmt = new BigDecimal(Math.round(objTotalAmt2));
		BigDecimal objFinalToatlAmt1 = objTotalAmt.setScale(2, RoundingMode.HALF_UP);
		String fianlTotalAmountInRupes = objFinalToatlAmt1.toPlainString();
		long fianlTotalAmountInRupes1 = objFinalToatlAmt1.longValue();
		String sim2 = NumberToWordsFormatMethod.inWordFormat(fianlTotalAmountInRupes1);
		// --------------------------------------------------------------------------------------

		double IGSTAMT = Math.round(bean.getIgstAmt());
		double CGSTAMT = Math.round(bean.getCgstAmt());
		double SGSTAMT = Math.round(bean.getSgstAmt());

		BigDecimal IGST_AMT = new BigDecimal(IGSTAMT);
		BigDecimal IGST_AMT1 = IGST_AMT.setScale(2, RoundingMode.HALF_UP);

		BigDecimal CGST_AMT = new BigDecimal(CGSTAMT);
		BigDecimal CGST_AMT1 = CGST_AMT.setScale(2, RoundingMode.HALF_UP);

		BigDecimal SGST_AMT = new BigDecimal(SGSTAMT);
		BigDecimal SGST_AMT1 = SGST_AMT.setScale(2, RoundingMode.HALF_UP);

		HashMap map = new HashMap();
		map.put("TotalIgstAmt", IGST_AMT1);
		map.put("TotalCgstAmt", CGST_AMT1);
		map.put("TotalSgstAmt", SGST_AMT1);
		map.put("TaxInvoiceNo", TaxInvoiceNo);
		map.put("TaxAmount", taxAmountInRupes2);
		map.put("TotalAmount", objFinalToatlAmt1);
		map.put("TotalAmountInWords", sim2);
		map.put("StateName", State);
		map.put("danId", danId);
		map.put("userId", userId);
		map.put("ASFYEAR", YEAR);
		try {
			String filename = "ParaformaInvoiceReportASF1";
			String reportfileName = "ProformaReportASF" + danId + ".pdf";
			String tempFolderPath = "/WEB-INF/JReport/" + filename;
			System.out.println("path:" + tempFolderPath);
			File tempFolder = new File(tempFolderPath);
			if (!tempFolder.exists()) {
				tempFolder.mkdirs();
			}
			String jasperFilePath = tempFolderPath + ".jasper";
			File reportFile = new File(jasperFilePath);
			if (!reportFile.exists()) {
				InputStream jRXmlStream = request.getSession().getServletContext()
						.getResourceAsStream("/WEB-INF/JReport/" + filename + ".jrxml");
				JasperDesign jasperDesign = JRXmlLoader.load(jRXmlStream);
				JasperCompileManager.compileReportToFile(jasperDesign, jasperFilePath);
				System.out.println(jasperFilePath);
			}
			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, connection);
			JRAbstractExporter exporterPDF = new JRPdfExporter();
			exporterPDF.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			try {
				exporterPDF.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			response.setHeader("Content-Disposition", "attachment;filename=" + reportfileName);
			response.setContentType("application/pdf");
			exporterPDF.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		} finally {
			tn.commit();

		}

	}

	@Override
	public void insertProformaInvoiceDetailsASF(ProformaInvoiceDetailsASF proformaObj1) {
		sessionFactory.getCurrentSession().save(proformaObj1);
	}

	@Override
	public String getProformaInvoiceNoASF(String danId) {
		String Proformaid = "";
		try {
			Session session = sessionFactory.openSession();
			Transaction tn = session.beginTransaction();
			Connection connection = ((SessionImpl) session).connection();

			Query query = session
					.createQuery("SELECT PROFORMA_INV_ID FROM ProformaInvoiceDetailsASF WHERE PORTFOLIO_NAME = :DAN");
			query.setParameter("DAN", danId);

			List<Long> rows = query.list();
			System.out.println("current row>>>>>>  " + rows.toString());

			for (Long val : rows) {
				Proformaid = val.toString();
			}

			return Proformaid;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//	@Override
	//	public List<Object> proformaInvoiceDatatTENURE(String mliId, HttpSession session) {
	//		// TODO Auto-generated method stub
	//		String Role = (String) session.getAttribute("uRole");
	//		List<Object> list;
	//		if (Role.equals("CMAKER") || Role.equals("CCHECKER")) {
	//			String hql = "SELECT DISTINCT(DC.payId),(MR.mem_bnk_name) FROM FileUploadModel IU,DanCgpanDetailsModel DC,MLIRegistration MR,DemandDeviceModel DM WHERE DC.DCI_APPROPRIATION_FLAG='N' AND concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) AND DM.danId=DC.danId AND DM.Dan_type='TM'";
	//			Query query = sessionFactory.getCurrentSession().createQuery(hql);
	//			list = query.list();
	//		} else {
	//			String hql = "SELECT DISTINCT(DC.payId),(MR.mem_bnk_name) FROM FileUploadModel IU,DanCgpanDetailsModel DC,MLIRegistration MR, DemandDeviceModel DM  WHERE DC.DCI_APPROPRIATION_FLAG='N' AND  concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) AND concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) = :MLIID AND DM.danId=DC.danId AND DM.Dan_type='TM'";
	//			Query query = sessionFactory.getCurrentSession().createQuery(hql);
	//			query.setParameter("MLIID", mliId);
	//			list = query.list();
	//		}
	//
	//		return list;
	//	}

	@Override
	public List<CGPANDetailsReportBean> proformaInvoiceDatatTENURE(String userId, HttpSession session) {
		// TODO Auto-generated method stub
		String Role = (String) session.getAttribute("uRole");
		/*
		 * if (Role.equals("CMAKER") || Role.equals("CCHECKER")) { String hql =
		 * "SELECT DISTINCT(DC.payId),(MR.mem_bnk_name) FROM FileUploadModel IU,DanCgpanDetailsModel DC,MLIRegistration MR,DemandDeviceModel DM WHERE DC.DCI_APPROPRIATION_FLAG='N' AND concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) AND DM.danId=DC.danId AND DM.Dan_type='TM'"
		 * ; Query query = sessionFactory.getCurrentSession().createQuery(hql); list =
		 * query.list(); } else { String hql =
		 * "SELECT DISTINCT(DC.payId),(MR.mem_bnk_name) FROM FileUploadModel IU,DanCgpanDetailsModel DC,MLIRegistration MR, DemandDeviceModel DM  WHERE DC.DCI_APPROPRIATION_FLAG='N' AND  concat(concat(DM.mem_bank_id,DM.mem_zne_id),DM.mem_brn_id) = concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) AND concat(concat(MR.mem_bnk_id,MR.mem_zne_id),MR.mem_brn_id) = :MLIID AND DM.danId=DC.danId AND DM.Dan_type='TM'"
		 * ; Query query = sessionFactory.getCurrentSession().createQuery(hql);
		 * query.setParameter("MLIID", mliId); list = query.list(); }
		 */

		Session session3 = sessionFactory.openSession();
		Transaction tn = session3.beginTransaction();
		Connection con = session3.connection();
		CallableStatement callStmt = null;
		ResultSet resultset = null;
		boolean flag = false;
		System.out.println("userId::" + userId);

		CGPANDetailsReportBean tenureModiBean = null;
		List<CGPANDetailsReportBean> payBankList = new ArrayList<CGPANDetailsReportBean>();
		;
		try {
			callStmt = (CallableStatement) con.prepareCall("{ call proc_tm_proforma_inv(?,?) } ");
			callStmt.setString(1, userId);
			callStmt.registerOutParameter(2, OracleTypes.CURSOR);
			callStmt.execute();
			resultset = (ResultSet) callStmt.getObject(2);
			System.out.println("resultset" + resultset);
			while (resultset.next()) {
				tenureModiBean = new CGPANDetailsReportBean();
				tenureModiBean.setDanId(resultset.getString("col_0_0_") != null ? resultset.getString("col_0_0_") : "");
				tenureModiBean
				.setBankname(resultset.getString("col_1_0_") != null ? resultset.getString("col_1_0_") : "");
				payBankList.add(tenureModiBean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return payBankList;
	}

	@Override
	public List<Object> getDanDetailTenure(String danId, String userId) {
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		Connection connection = ((SessionImpl) session).connection();

	//	String sql = "SELECT DISTINCT t.pay_id, total_amount,base_amt,igst,sgst,cgst, gua_start_dt, T2.MEM_BNK_ID||T2.MEM_ZNE_ID||t2.mem_brn_id mli_id, MEM_BANK_NAME, MEM_ADDRESS, sum(IU.OUTSTANDING_AMOUNT) outstanding_amount,MI.COMPANY_PAN , MI.GSTIN_NO,t1.DCI_STANDARD_RATE, MI.MEM_STATE_NAME FROM (SELECT SUM (A.DCI_AMOUNT_RAISED) total_amount,SUM (A.DCI_BASE_AMT) base_amt,SUM (A.igst_amt) igst, SUM (A.SGST_AMT) sgst, SUM (A.CGST_AMT) cgst, pay_id, TRUNC (A.DCI_GUARANTEE_START_DT) gua_start_dt FROM NBFC_DAN_CGPAN_INFO a WHERE pay_id='"+danId+"' GROUP BY pay_id, TRUNC (A.DCI_GUARANTEE_START_DT)) t, nbfc_dan_cgpan_info t1, nbfc_demand_advice_info t2,nbfc_member_info mi, nbfc_interface_upload iu WHERE t1.dan_id = t2.dan_id AND t1.pay_id = t.pay_id and t1.dan_id like 'TM%' and IU.CGPAN=t1.cgpan and MI.MEM_BNK_ID||mi.mem_zne_id||mi.mem_brn_id=T2.MEM_BNK_ID||T2.MEM_ZNE_ID||t2.mem_brn_id group by t.pay_id, total_amount, base_amt, igst, sgst, cgst,gua_start_dt,T2.MEM_BNK_ID||T2.MEM_ZNE_ID||t2.mem_brn_id,MEM_BANK_NAME, MEM_ADDRESS, MI.COMPANY_PAN ,MI.GSTIN_NO, t1.DCI_STANDARD_RATE, MI.MEM_STATE_NAME";

		
	//	String sql="SELECT DISTINCT t.pay_id,total_amount,base_amt,igst,sgst,cgst,gua_start_dt,T2.MEM_BNK_ID || T2.MEM_ZNE_ID || t2.mem_brn_id mli_id,MEM_BANK_NAME,MEM_ADDRESS,SUM (IU.OUTSTANDING_AMOUNT) outstanding_amount,MI.COMPANY_PAN,MI.GSTIN_NO,t1.DCI_STANDARD_RATE,MI.MEM_STATE_NAME FROM (  SELECT SUM (A.DCI_AMOUNT_RAISED) total_amount,SUM (A.DCI_BASE_AMT) base_amt,SUM (A.igst_amt) igst,SUM (A.SGST_AMT) sgst,SUM (A.CGST_AMT) cgst,pay_id,TRUNC (A.DCI_GUARANTEE_START_DT) gua_start_dt,COUNT (a.dan_id) FROM NBFC_DAN_CGPAN_INFO a WHERE pay_id = '"+danId+"' AND a.cgpan NOT IN (SELECT * FROM tbl_rfdancases) GROUP BY pay_id, TRUNC (A.DCI_GUARANTEE_START_DT)) t,nbfc_dan_cgpan_info t1,nbfc_demand_advice_info t2,nbfc_member_info mi,nbfc_interface_upload iu WHERE t1.dan_id = t2.dan_id AND t1.pay_id = t.pay_id AND t1.dan_id LIKE 'TM%' AND IU.CGPAN = t1.cgpan AND MI.MEM_BNK_ID || mi.mem_zne_id || mi.mem_brn_id =T2.MEM_BNK_ID || T2.MEM_ZNE_ID || t2.mem_brn_id       AND  t1.cgpan NOT IN (SELECT * FROM tbl_rfdancases)  GROUP BY t.pay_id,total_amount,base_amt,igst,sgst,cgst,gua_start_dt,T2.MEM_BNK_ID|| T2.MEM_ZNE_ID || t2.mem_brn_id,MEM_BANK_NAME,MEM_ADDRESS,MI.COMPANY_PAN,MI.GSTIN_NO,t1.DCI_STANDARD_RATE,MI.MEM_STATE_NAME";
		
		String sql ="SELECT DISTINCT t.pay_id, "
				+ "                  total_amount,"
				+ "                  base_amt,"
				+ "                  igst,"
				+ "                  sgst,"
				+ "                  cgst,"
				+ "                  gua_start_dt,"
				+ "                  T2.MEM_BNK_ID || T2.MEM_ZNE_ID || t2.mem_brn_id mli_id,"
				+ "                  MEM_BANK_NAME,"
				+ "                  MEM_ADDRESS,"
				+ "                  SUM (IU.OUTSTANDING_AMOUNT) outstanding_amount,"
				+ "                  MI.COMPANY_PAN,"
				+ "                  MI.GSTIN_NO,"
				+ "                  t1.DCI_STANDARD_RATE,"
				+ "                  MI.MEM_STATE_NAME"
				+ "    FROM (  SELECT SUM (A.DCI_AMOUNT_RAISED) total_amount,"
				+ "                   SUM (A.DCI_BASE_AMT) base_amt,"
				+ "                   SUM (A.igst_amt) igst,"
				+ "                   SUM (A.SGST_AMT) sgst,"
				+ "                   SUM (A.CGST_AMT) cgst,"
				+ "                   pay_id,"
				+ "                   TRUNC (A.DCI_GUARANTEE_START_DT) gua_start_dt,"
				+ "                   COUNT (a.dan_id)"
				+ "              FROM NBFC_DAN_CGPAN_INFO a"
				+ "             WHERE pay_id in('"+danId+"')"
				+ "                 AND a.cgpan NOT IN (SELECT * FROM tbl_rfdancases)"
				+ "          GROUP BY pay_id, TRUNC (A.DCI_GUARANTEE_START_DT)) t,"
				+ "         nbfc_dan_cgpan_info t1,"
				+ "         nbfc_demand_advice_info t2,"
				+ "         nbfc_member_info mi,"
				+ "         nbfc_interface_upload iu"
				+ "   WHERE     t1.dan_id = t2.dan_id"
				+ "         AND t1.pay_id = t.pay_id"
				+ "         AND t1.dan_id LIKE 'TM%'"
				+ "         AND IU.CGPAN = t1.cgpan"
				+ "         AND MI.MEM_BNK_ID || mi.mem_zne_id || mi.mem_brn_id ="
				+ "                T2.MEM_BNK_ID || T2.MEM_ZNE_ID || t2.mem_brn_id"
				+ "                AND  t1.cgpan NOT IN (SELECT * FROM tbl_rfdancases)"
				+ "GROUP BY t.pay_id,"
				+ "         total_amount,"
				+ "         base_amt,"
				+ "         igst,"
				+ "         sgst,"
				+ "         cgst,"
				+ "         gua_start_dt,"
				+ "         T2.MEM_BNK_ID || T2.MEM_ZNE_ID || t2.mem_brn_id,"
				+ "         MEM_BANK_NAME,"
				+ "         MEM_ADDRESS,"
				+ "         MI.COMPANY_PAN,"
				+ "         MI.GSTIN_NO,"
				+ "         t1.DCI_STANDARD_RATE,"
				+ "         MI.MEM_STATE_NAME"
				+ "";
		
		
		System.out.println("Query-------" + sql);
		SQLQuery query = session.createSQLQuery(sql);
		List<Object> list = query.list();
		return list;
	}

	@Override
	public void callProformaInvoiceReportTenure(String danId, HttpServletResponse response, HttpServletRequest request,
			CGPANDetailsReportBean bean) {
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		Connection connection = ((SessionImpl) session).connection();
		JasperPrint jasperPrint1 = null;
		if (connection == null) {
			System.out.println("connection is null.........");
		}

		String State = bean.getState();
		String DAN = bean.getDanId();
		String userId = bean.getUserId();
		String date = bean.getDciGuaranteeeSDate();
		String TaxNo = bean.getTaxInvoiceId().toString();
		String id = date.replace("/", "");
		String TaxInvoiceNo = id + TaxNo;
		System.out.println("taxno--------------" + TaxInvoiceNo);

		double objTotalAmt2 = bean.getDci_total_amt();
		double outSandingAndGuranRound = bean.getDci_base_amt();
		/*
		 * double outSandingAndGuran = ((bean.getOutstandingAmount1()
		 * bean.getGuaranteeFee() / 100)); double outSandingAndGuranRound =
		 * Math.round(outSandingAndGuran);
		 */
		BigDecimal taxAmt = new BigDecimal(outSandingAndGuranRound);
		BigDecimal taxAmountInRupes2 = taxAmt.setScale(2, RoundingMode.HALF_UP);
		String taxAmountInRupes = taxAmountInRupes2.toPlainString();

		/*
		 * double objTotalAmt2 = bean.getIgstAmt() + bean.getCgstAmt() +
		 * bean.getSgstAmt() + outSandingAndGuran;
		 */
		BigDecimal objTotalAmt = new BigDecimal(Math.round(objTotalAmt2));
		BigDecimal objFinalToatlAmt1 = objTotalAmt.setScale(2, RoundingMode.HALF_UP);
		String fianlTotalAmountInRupes = objFinalToatlAmt1.toPlainString();
		long fianlTotalAmountInRupes1 = objFinalToatlAmt1.longValue();
		String sim2 = NumberToWordsFormatMethod.inWordFormat(fianlTotalAmountInRupes1);

		double IGSTAMT = Math.round(bean.getIgstAmt());
		double CGSTAMT = Math.round(bean.getCgstAmt());
		double SGSTAMT = Math.round(bean.getSgstAmt());

		BigDecimal IGST_AMT = new BigDecimal(IGSTAMT);
		BigDecimal IGST_AMT1 = IGST_AMT.setScale(2, RoundingMode.HALF_UP);

		BigDecimal CGST_AMT = new BigDecimal(CGSTAMT);
		BigDecimal CGST_AMT1 = CGST_AMT.setScale(2, RoundingMode.HALF_UP);

		BigDecimal SGST_AMT = new BigDecimal(SGSTAMT);
		BigDecimal SGST_AMT1 = SGST_AMT.setScale(2, RoundingMode.HALF_UP);

		HashMap map = new HashMap();
		map.put("TotalIgstAmt", IGST_AMT1);
		map.put("TotalCgstAmt", CGST_AMT1);
		map.put("TotalSgstAmt", SGST_AMT1);
		map.put("TaxInvoiceNo", TaxInvoiceNo);
		map.put("TaxAmount", taxAmountInRupes);
		map.put("TotalAmount", objTotalAmt);
		map.put("TotalAmountInWords", sim2);
		map.put("StateName", State);
		map.put("danId", danId);
		map.put("userId", userId);
		try {
			String filename = "ParaformaInvoiceReportTenure";
			String reportfileName = "ProformaReportTenure" + danId + ".pdf";
			String tempFolderPath = "/WEB-INF/JReport/" + filename;
			System.out.println("path:" + tempFolderPath);
			File tempFolder = new File(tempFolderPath);
			if (!tempFolder.exists()) {
				tempFolder.mkdirs();
			}
			String jasperFilePath = tempFolderPath + ".jasper";
			File reportFile = new File(jasperFilePath);
			if (!reportFile.exists()) {
				InputStream jRXmlStream = request.getSession().getServletContext()
						.getResourceAsStream("/WEB-INF/JReport/" + filename + ".jrxml");
				JasperDesign jasperDesign = JRXmlLoader.load(jRXmlStream);
				JasperCompileManager.compileReportToFile(jasperDesign, jasperFilePath);
				System.out.println(jasperFilePath);
			}
			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, connection);
			JRAbstractExporter exporterPDF = new JRPdfExporter();
			exporterPDF.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			try {
				exporterPDF.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			response.setHeader("Content-Disposition", "attachment;filename=" + reportfileName);
			response.setContentType("application/pdf");
			exporterPDF.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tn.commit();

		}

	}

	@Override
	public Integer getMaxProformaInvoiceIdCountTENURE() {
		try {
			String hql = "SELECT MAX(PROFORMA_INV_ID)+1 FROM ProformaInvoiceDetailsASF WHERE DAN_TYPE ='TM'";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			System.out.println("Exception===" + e);
		}
		return 0;
	}

	@Override
	public String genratePdfWithInvoiceNo(HttpServletResponse response, CGPANDetailsReportBean bean, String contextPath,
			String strDate) {
		String pdfMsg = "";
		try {
			// String pdfFilePath = "C:\\dkr_lib\\DKR INVOICE.pdf";
			Document document = new Document(PageSize.A4, 50, 50, 30, 20);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			// ========================================================
			String returnOnPage = "";
			System.out.println("downloadPDfForSuccessGeneratedIRN==DKR==");
			FileOutputStream out = new FileOutputStream(new File(contextPath + "\\Download\\DataCSVFile" + strDate + ".pdf"));
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();
			// ====================================================
			// System.out.println("````````````````````sayali`````````````````````````````````");
			Font drfont10 = FontFactory.getFont(FontFactory.TIMES, 9f);
			Font drfont8 = FontFactory.getFont(FontFactory.TIMES, 6f);
			Font drfont6d = FontFactory.getFont(FontFactory.TIMES, 5f, Font.BOLD);
			Font drfont7b = FontFactory.getFont(FontFactory.TIMES, 7f, Font.BOLD);
			Font drfont6 = FontFactory.getFont(FontFactory.TIMES, 5f);
			Paragraph centerAlignedParagraph1 = new Paragraph("  Credit Guarantee Fund Trust For Micro and Small Enterprises(CGTMSE)", drfont10);
			centerAlignedParagraph1.setAlignment(Element.ALIGN_CENTER);
			System.out.println("qr---" + bean.getIrnNO());
			BarcodeQRCode barcodeQRCode = new BarcodeQRCode((bean.getIrnNO()), 1000, 1000, null);
			Image dkrQrImage = barcodeQRCode.getImage();
			dkrQrImage.scaleAbsolute(50, 50);
			Paragraph centerAlignedParagraph11 = new Paragraph(new Chunk(dkrQrImage, 0, 0, true));
			centerAlignedParagraph11.setAlignment(Element.ALIGN_LEFT);
			Paragraph centerAlignedParagraph2 = new Paragraph("Tax Invoice", drfont10);
			// centerAlignedParagraph2.setBorder(Rectangle.NO_BORDER);
			centerAlignedParagraph2.setAlignment(Element.ALIGN_CENTER);
			Paragraph centerAlignedParagraphd = new Paragraph("Credit Guarantee Fund Trust For Micro and Small Enterprises(CGTMSE)", drfont8);
			centerAlignedParagraphd.setAlignment(Element.ALIGN_CENTER);
			Paragraph centerAlignedParagraph3 = new Paragraph("Set up of Govt.Of India SIDBI", drfont6);
			centerAlignedParagraph3.setAlignment(Element.ALIGN_CENTER);
			Paragraph centerAlignedParagraph4 = new Paragraph("GSTN:27AAATC2613D1ZC STATE:Maharastra-MH", drfont8);
			centerAlignedParagraph4.setAlignment(Element.ALIGN_CENTER);
			Paragraph centerAlignedParagraph5 = new Paragraph("SIDBI Swavalamban Bhavan, 7th Floor, C-11,G-Block,Bandra Kurla Compledx,Bandra(East), Mumbai 400 051"+ new Chunk("www.cgtmse.in").setUnderline(0.1f, -2f),drfont6);
			centerAlignedParagraph5.setAlignment(Element.ALIGN_CENTER);
			// Paragraph centerAlignedParagraph6 = new Paragraph("Date:" +
			// dateFormat.format(new java.util.Date().getTime()), drfont7b);
			Paragraph centerAlignedParagraph6 = new Paragraph("\n Date Of Payment:" + bean.getDciAppropriationDate(),drfont7b);
			centerAlignedParagraph6.setAlignment(Element.ALIGN_RIGHT);
			// centerAlignedParagraph6.setSpacingAfter(20f);
			document.add(centerAlignedParagraph1);
			document.add(centerAlignedParagraph11);
			document.add(centerAlignedParagraph2);
			document.add(centerAlignedParagraphd);
			document.add(centerAlignedParagraph3);
			document.add(centerAlignedParagraph4);
			document.add(centerAlignedParagraph5);
			document.add(centerAlignedParagraph6);
			document.add(new Paragraph("\r \n"));

			PdfPTable myTable = new PdfPTable(3);
			myTable.setWidthPercentage(100);
			PdfPCell cellOne = new PdfPCell(new Paragraph("Tax Invoice No.:" + bean.getTaxid(), drfont7b));
			cellOne.setColspan(3);
			cellOne.setBorder(Rectangle.BOTTOM);
			cellOne.setHorizontalAlignment(Element.ALIGN_LEFT);
			cellOne.setBorder(Rectangle.NO_BORDER);
			myTable.addCell(cellOne);
			PdfPCell cellMli = new PdfPCell(new Paragraph("\n MLI Name:" + bean.getBankname(), drfont7b));
			cellMli.setColspan(3);
			cellMli.setBorder(Rectangle.BOTTOM);
			cellMli.setHorizontalAlignment(Element.ALIGN_LEFT);
			cellMli.setBorder(Rectangle.NO_BORDER);
			myTable.addCell(cellMli);

			PdfPCell cellTwo = new PdfPCell(new Paragraph("\n Address:" + bean.getAddress(), drfont7b));
			cellTwo.setBorder(Rectangle.NO_BORDER);
			cellTwo.setHorizontalAlignment(Element.ALIGN_LEFT);
			myTable.addCell(cellTwo);

			PdfPCell cellThree = new PdfPCell(new Paragraph("GSTIN:" + bean.getGstNo(), drfont7b));
			cellThree.setColspan(3);
			cellThree.setBorder(Rectangle.ALIGN_CENTER);
			cellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellThree.setBorder(Rectangle.NO_BORDER);
			myTable.addCell(cellThree);

			document.add(myTable);

			Paragraph parState = new Paragraph(" State:" + bean.getState(), drfont7b);
			parState.setAlignment(Element.ALIGN_RIGHT);
			document.add(parState);

			Paragraph stateCode = new Paragraph(" State Code :-" + bean.getStateCode(), drfont7b);
			stateCode.setAlignment(Element.ALIGN_RIGHT);
			document.add(stateCode);

			Paragraph midParaDanHeder = new Paragraph("  DAN Report:-", drfont7b);
			midParaDanHeder.setAlignment(Element.ALIGN_LEFT);
			// midParaDanHeder.setSpacingBefore(10);
			document.add(midParaDanHeder);
			document.add(new Paragraph("\n"));
			PdfPTable table = new PdfPTable(12);
			table.setWidthPercentage(100); // Width 100%
			// float[] columnWidths = { 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f
			// };
			float[] columnWidths = { 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f };
			table.setWidths(columnWidths);
			PdfPCell c1 = new PdfPCell(new Phrase("Portfolio Name", drfont6d));
			c1.setBorder(Rectangle.BOX);
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);
			c1 = new PdfPCell(new Phrase("Name Of NBFC", drfont6d));
			c1.setBorder(Rectangle.BOX);
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);
			c1 = new PdfPCell(new Phrase("Loan Sanction" + "Amount(Rs.)", drfont6d));
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Guarantee" + "Approved" + "Amount(Rs.)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);
			// c1 = new PdfPCell(new Phrase("Application" + "Submitted Date" + "",
			// drfont6d));
			// c1.setBorder(Rectangle.BOX);
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			// table.addCell(c1);
			c1 = new PdfPCell(new Phrase("Taxable" + "Amount(Rs.)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);
			c1 = new PdfPCell(new Phrase("IGST Rate(%)", drfont6d));
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("IGST Amount(Rs.)", drfont6d));
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("CGST Rate(%)", drfont6d));
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("CGST Amount(Rs.)", drfont6d));
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("SGST Rate(%)", drfont6d));
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("SGST Amount(Rs.)", drfont6d));
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Total Amount(Rs.) ", drfont6d));
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);
			
			table.setHeaderRows(1);

			PdfPCell cell1 = new PdfPCell(new Paragraph(bean.getPortfolioName(), drfont6)); // 1
			// cell1.setBorderColor(BaseColor.BLUE);
			cell1.setBorder(Rectangle.BOX);

			PdfPCell cell2 = new PdfPCell(new Paragraph(bean.getBankname(), drfont6));// invoiceObj.getCgpan()
			
			cell2.setBorder(Rectangle.BOX);
			
			PdfPCell cell3 = new PdfPCell(new Paragraph(bean.getSanctionAMt1().toString(), drfont6));// invoiceObj.getCity()
			
			cell3.setBorder(Rectangle.BOX);

			PdfPCell cell4 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getGuaranteeAmt1().toString()), drfont6));// SSI NAME 4
			cell4.setBorder(Rectangle.BOX);
			
			PdfPCell cell6 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getBaseAmount().toString()), drfont6));// invoiceObj.getCity()
			
			cell6.setBorder(Rectangle.BOX);
			
			System.out.println("```````````````````2`````````````````````````````````");
			PdfPCell cell7;
			cell7 = new PdfPCell(new Paragraph(String.valueOf("18"), drfont6)); // invoiceObj.getCity()
			

			cell7.setBorder(Rectangle.BOX);
		
			System.out.println("```````````````````3`````````````````````````````````");
			PdfPCell cell8 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getIgstAmount().toString()), drfont6));// invoiceObj.getCity()
			
			cell8.setBorder(Rectangle.BOX);
			
			PdfPCell cell9 = new PdfPCell(new Paragraph(String.valueOf("9"), drfont6));// invoiceObj.getCity()
			
			cell9.setBorder(Rectangle.BOX);
		
			System.out.println("````````````````````4``````````````````````````````````");
			PdfPCell cell10;

			cell10 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getCgstAmount().toString()), drfont6));// invoiceObj.getCity()
			cell10.setBorder(Rectangle.BOX);
			
			System.out.println("````````````````````5``````````````````````````````````");
			PdfPCell cell11 = new PdfPCell(new Paragraph(String.valueOf("9"), drfont6));// invoiceObj.getCity() //
		
			cell11.setBorder(Rectangle.BOX);
		
			System.out.println("````````````````````6`````````````````````````````````");
			PdfPCell cell12;
			cell12 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getSgstAmount().toString()), drfont6));// invoiceObj.getCity()		
			cell12.setBorder(Rectangle.BOX);
			
			PdfPCell cell13 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getTotalAmount().toString()), drfont6));// invoiceObj.getCity()
			cell13.setBorder(Rectangle.BOX); 

			table.addCell(cell1);
			table.addCell(cell2);
			table.addCell(cell3);
			table.addCell(cell4);
			table.addCell(cell6);
			table.addCell(cell7);
			table.addCell(cell8);
			table.addCell(cell9);
			table.addCell(cell10);
			table.addCell(cell11);
			table.addCell(cell12);
			table.addCell(cell13);

			document.left(100f);
			document.top(150f);
			document.add(table);
		    System.out.println("````````````````````9``````````````````````````````````");
			Paragraph totalPayAmt = new Paragraph("   Total Amount Paid RS. In Words:" + String.valueOf(bean.getTotalAmt()), drfont7b);
			totalPayAmt.setAlignment(Element.ALIGN_LEFT);			
			document.add(totalPayAmt);

			Paragraph genIrrnPara = new Paragraph(" Genrated IRRN:" + bean.getIrnNO(), drfont8);
			genIrrnPara.setAlignment(Element.ALIGN_LEFT);			
			document.add(genIrrnPara);
			System.out.println("```````````````````10````````````````````````````````");
			Paragraph termCondition = new Paragraph("  Terms and conditions:", drfont6);
			termCondition.setAlignment(Element.ALIGN_LEFT);			
			document.add(termCondition);
			Paragraph termCondetail = new Paragraph(" 1) The Courts in Mumbai shall have exclusive jurisdiction in respect of any dispute arising in connection with the above matter.",drfont6);
			document.add(termCondetail);
			
			Paragraph termCondition2 = new Paragraph("2) Please revert hack to us within 7 days of receipt of the invoice in case of discreapancy in state code and/or GSTIN as mentioned herein the please of supply or your actual GSTIN by email on",drfont6);
		
			Paragraph sacServiceCat = new Paragraph("\r \n SAC code/Service Category:", drfont8);
			sacServiceCat.setAlignment(Element.ALIGN_LEFT);			
			document.add(sacServiceCat);
		
			Paragraph toatalCond3 = new Paragraph("Code under GST:997113 - Credit - granting services including stand - by commitment, guarantees & securities.",drfont8);
			toatalCond3.setAlignment(Element.ALIGN_LEFT);
			
			document.add(toatalCond3);
			// *** This is computer generated document, no signature required******
			Paragraph totalCond4 = new Paragraph("\r \n *** This is computer generated document, no signature required******", drfont8);
			totalCond4.setAlignment(Element.ALIGN_LEFT);
			document.add(totalCond4);
			document.add(new Paragraph("\r\n \r\n "));
			Paragraph autCgt = new Paragraph(" For CGTMSE", drfont8);
			autCgt.setAlignment(Element.ALIGN_RIGHT);
			document.add(autCgt);

			Paragraph sd = new Paragraph(" Sd/-", drfont8);
			sd.setAlignment(Element.ALIGN_RIGHT);
			document.add(sd);
			//Image signImage = Image.getInstance("C:\\NBFC_Template\\Signature.jpg");
			 //*/
			//Add By VinodSingh Signature Image
			
			try{
				Image signImage = Image.getInstance("/images/Signature.jpg");
				Paragraph signImageParagraph= new Paragraph(new Chunk(signImage,  0, 0, true));		  
				signImageParagraph.setAlignment(Element.ALIGN_RIGHT); 
				signImage.scaleAbsolute(30, 30);
				document.add(signImageParagraph);

			} catch (Exception e) {
				System.out.println("Signature.jpg:::--" + e.getMessage());
			}

			Paragraph autorisedName= new Paragraph(" Jigar Shah, COO ",drfont8);
			autorisedName.setAlignment(Element.ALIGN_RIGHT);			
			document.add(autorisedName);
			Paragraph sigCgt = new Paragraph("  Autorised Signatory", drfont8);
			sigCgt.setAlignment(Element.ALIGN_RIGHT);
			document.add(sigCgt);
			document.add(new Paragraph("   "));
		
			pdfMsg = "PDF Successfully Genreated";
			// ==================================================
			document.close();
			OutputStream os = response.getOutputStream();
			FileInputStream fis = new FileInputStream(new File(contextPath + "\\Download\\DataCSVFile" + strDate + ".pdf"));
			int x = fis.available();
			byte b[] = new byte[x];
			System.out.println(" byte obj size~~~~~~~~~~~~~~~~~~~" + b.length);
			fis.read(b);
			if (response != null)
				response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition",(new StringBuilder("attachment; filename=GFTax")).append(strDate).append(".pdf").toString());
			os.write(b);
			os.flush();
		} catch (FileNotFoundException e) {
			pdfMsg = "Error in pdf download." + e.getMessage();
			// new DatabaseException("Error in pdf download." + e.getMessage());
		} catch (IOException e) {
			pdfMsg = "Error in pdf download.." + e.getMessage();
			// new DatabaseException("Error in pdf download.." + e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			pdfMsg = "Error in pdf download..." + e.getMessage();
			System.out.println("pdfMsg------------" + pdfMsg);
			// DatabaseException("Error in pdf download..." + e.getMessage());
		}
		return pdfMsg;
	}

	@Override
	public List<Object> getTaxQRCode(String taxInvoiceId) {
		List<Object> list = null;
		try {
			Session session = sessionFactory.openSession();
			Transaction tn = session.beginTransaction();
			Connection connection = ((SessionImpl) session).connection();
			SQLQuery slqQuery = session.createSQLQuery(
					"SELECT GENERATED_IRRN,RES_SIGNEDINVOICE FROM NBFC_GST_TAX_INVOICE WHERE TAX_INV_ID = '"
							+ taxInvoiceId + "'");
			list = slqQuery.list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/* START Add By VinodSingh on 01-Jan-2021 For ASF Tax invoice Report */
	@Override
	public String genPdfTaxInvoiceForASF(HttpServletResponse response, CGPANDetailsReportBean bean, String contextPath,
			String strDate) {
		String pdfMsg = "";

		try {
			Document document = new Document(PageSize.A4, 50, 50, 30, 20);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			String returnOnPage = "";

			FileOutputStream out = new FileOutputStream(
					new File(contextPath + "\\Download\\DataCSVFile" + strDate + ".pdf"));
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();

			Font drfont10 = FontFactory.getFont(FontFactory.TIMES, 9f);
			Font drfont8 = FontFactory.getFont(FontFactory.TIMES, 6f);
			Font drfont6d = FontFactory.getFont(FontFactory.TIMES, 5f, Font.BOLD);
			Font drfont7d = FontFactory.getFont(FontFactory.TIMES, 7f, Font.BOLD);
			Font drfont6 = FontFactory.getFont(FontFactory.TIMES, 5f);
			Paragraph centerAlignedParagraph1 = new Paragraph(
					"  Credit Guarantee Fund Trust For Micro and Small Enterprises(CGTMSE)", drfont10);
			centerAlignedParagraph1.setAlignment(Element.ALIGN_CENTER);
			System.out.println("qr---" + bean.getIrnNO());
			BarcodeQRCode barcodeQRCode = new BarcodeQRCode((bean.getIrnNO()), 1000, 1000, null);
			Image dkrQrImage = barcodeQRCode.getImage();
			dkrQrImage.scaleAbsolute(50, 50);
			Paragraph centerAlignedParagraph11 = new Paragraph(new Chunk(dkrQrImage, 0, 0, true));
			centerAlignedParagraph11.setAlignment(Element.ALIGN_LEFT);
			Paragraph centerAlignedParagraph2 = new Paragraph("Tax Invoice", drfont10);

			centerAlignedParagraph2.setAlignment(Element.ALIGN_CENTER);
			Paragraph centerAlignedParagraphd = new Paragraph(
					"Credit Guarantee Fund Trust For Micro and Small Enterprises(CGTMSE)", drfont8);
			centerAlignedParagraphd.setAlignment(Element.ALIGN_CENTER);
			Paragraph centerAlignedParagraph3 = new Paragraph("Set up of Govt.Of India SIDBI", drfont6);
			centerAlignedParagraph3.setAlignment(Element.ALIGN_CENTER);
			Paragraph centerAlignedParagraph4 = new Paragraph("GSTN:27AAATC2613D1ZC STATE:Maharastra-MH", drfont8);
			centerAlignedParagraph4.setAlignment(Element.ALIGN_CENTER);
			Paragraph centerAlignedParagraph5 = new Paragraph(
					"SIDBI Swavalamban Bhavan, 7th Floor, C-11,G-Block,Bandra Kurla Compledx,Bandra(East), Mumbai 400 051"
							+ new Chunk("www.cgtmse.in").setUnderline(0.1f, -2f),
							drfont6);
			centerAlignedParagraph5.setAlignment(Element.ALIGN_CENTER);
			// Paragraph centerAlignedParagraph6 = new Paragraph("Date:" +
			// dateFormat.format(new java.util.Date().getTime()), drfont7d);
			Paragraph centerAlignedParagraph6 = new Paragraph("Date Of Payment:" + bean.getDciAppropriationDate(),
					drfont7d);
			centerAlignedParagraph6.setAlignment(Element.ALIGN_RIGHT);

			document.add(centerAlignedParagraph1);
			document.add(centerAlignedParagraph11);
			document.add(centerAlignedParagraph2);
			document.add(centerAlignedParagraphd);
			document.add(centerAlignedParagraph3);
			document.add(centerAlignedParagraph4);
			document.add(centerAlignedParagraph5);
			document.add(centerAlignedParagraph6);
			document.add(new Paragraph("\r \n"));

			PdfPTable myTable = new PdfPTable(3);
			myTable.setWidthPercentage(100);
			PdfPCell cellOne = new PdfPCell(new Paragraph("Tax Invoice No.:" + bean.getTaxid(), drfont7d));
			cellOne.setColspan(3);
			cellOne.setBorder(Rectangle.BOTTOM);
			cellOne.setHorizontalAlignment(Element.ALIGN_LEFT);
			cellOne.setBorder(Rectangle.NO_BORDER);
			myTable.addCell(cellOne);
			PdfPCell cellMli = new PdfPCell(new Paragraph("\n MLI Name:" + bean.getBankname(), drfont7d));
			cellMli.setColspan(3);
			cellMli.setBorder(Rectangle.BOTTOM);
			cellMli.setHorizontalAlignment(Element.ALIGN_LEFT);
			cellMli.setBorder(Rectangle.NO_BORDER);
			myTable.addCell(cellMli);

			PdfPCell cellTwo = new PdfPCell(new Paragraph("\n Address:" + bean.getAddress(), drfont7d));
			cellTwo.setBorder(Rectangle.NO_BORDER);
			cellTwo.setHorizontalAlignment(Element.ALIGN_LEFT);
			myTable.addCell(cellTwo);

			PdfPCell cellThree = new PdfPCell(new Paragraph("GSTIN:" + bean.getGstNo(), drfont7d));
			cellThree.setColspan(3);
			cellThree.setBorder(Rectangle.ALIGN_CENTER);
			cellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellThree.setBorder(Rectangle.NO_BORDER);
			myTable.addCell(cellThree);

			/*
			 * PdfPCell cellAddressPrinc = new PdfPCell(new Paragraph("\n Date Of Payment:"
			 * + bean.getDciAppropriationDate(), drfont7d)); cellAddressPrinc.setColspan(2);
			 * cellAddressPrinc.setHorizontalAlignment(Element.ALIGN_LEFT);
			 * cellAddressPrinc.setBorder(Rectangle.NO_BORDER);
			 * myTable.addCell(cellAddressPrinc);
			 */

			/*
			 * PdfPCell cellState = new PdfPCell(new Paragraph("State:" + bean.getState(),
			 * drfont7d)); cellState.setBorder(Rectangle.LEFT);
			 * cellState.setHorizontalAlignment(Element.ALIGN_RIGHT);
			 * cellState.setBorder(Rectangle.NO_BORDER); myTable.addCell(cellState);
			 */

			document.add(myTable);

			Paragraph cellState1 = new Paragraph(" State:" + bean.getState(), drfont7d);
			cellState1.setAlignment(Element.ALIGN_RIGHT);
			document.add(cellState1);

			Paragraph stateCodeHeder = new Paragraph(" State Code:" + " " + bean.getStateCode() + "  " + "  ",
					drfont7d);
			stateCodeHeder.setAlignment(Element.ALIGN_RIGHT);
			document.add(stateCodeHeder);

			Paragraph midParaDanHeder = new Paragraph("  DAN Report:-", drfont7d);
			midParaDanHeder.setAlignment(Element.ALIGN_LEFT);

			document.add(midParaDanHeder);
			document.add(new Paragraph("\n"));
			PdfPTable table = new PdfPTable(11); // No Of columns.
			table.setWidthPercentage(100); // Width 100%
			float[] columnWidths = { 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f };
			table.setWidths(columnWidths);
			PdfPCell c1 = new PdfPCell(new Phrase("Portfolio No.", drfont6d));
			c1.setBorder(Rectangle.BOX);

			table.addCell(c1);
			c1 = new PdfPCell(new Phrase("Name Of NBFC", drfont6d));
			c1.setBorder(Rectangle.BOX);

			table.addCell(c1);
			c1 = new PdfPCell(new Phrase("Total Outstanding" + "Amount(Rs.)", drfont6d));
			// c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Taxable" + "Amount(Rs.)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("IGST Rate(%)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("IGST Amount(Rs.)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("CGST Rate(%)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("CGST Amount(Rs.)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("SGST Rate(%)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("SGST Amount(Rs.)", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Total Amount(Rs.) ", drfont6d));
			c1.setBorder(Rectangle.BOX);
			table.addCell(c1);

			table.setHeaderRows(1);
			PdfPCell cell1 = new PdfPCell(new Paragraph(bean.getPortfolioName(), drfont6)); // column 1

			cell1.setBorder(Rectangle.BOX);

			PdfPCell cell2 = new PdfPCell(new Paragraph(bean.getBankname(), drfont6));// column 2
			cell2.setBorder(Rectangle.BOX);

			PdfPCell cell3 = new PdfPCell(
					new Paragraph(amountTrilWithZeros(bean.getOutStandingAmount().toString()), drfont6)); // column 3
			// outstanding
			// amount
			cell3.setBorder(Rectangle.BOX);

			PdfPCell cell5 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getBaseAmount().toString()), drfont6)); // column
			// 5
			// Taxable
			// Amount
			cell5.setBorder(Rectangle.BOX);

			// System.out.println("```````````````````2`````````````````````````````````");
			PdfPCell cell7;
			cell7 = new PdfPCell(new Paragraph(String.valueOf("18"), drfont6)); // column 7 invoiceObj.getCity()
			cell7.setBorder(Rectangle.BOX);
			// System.out.println("```````````````````3`````````````````````````````````");
			PdfPCell cell8 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getIgstAmount().toString()), drfont6));// column
			// 8
			// invoiceObj.getCity()
			cell8.setBorder(Rectangle.BOX);

			PdfPCell cell9 = new PdfPCell(new Paragraph(String.valueOf("9"), drfont6));// column 9 invoiceObj.getCity()
			cell9.setBorder(Rectangle.BOX);
			// System.out.println("````````````````````4``````````````````````````````````");
			PdfPCell cell10;
			cell10 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getCgstAmount().toString()), drfont6));// column
			// 10
			// invoiceObj.getCity()
			cell10.setBorder(Rectangle.BOX);

			// System.out.println("````````````````````5``````````````````````````````````");
			PdfPCell cell11 = new PdfPCell(new Paragraph(String.valueOf("9"), drfont6));// column 11
			// invoiceObj.getCity() //
			cell11.setBorder(Rectangle.BOX);

			// System.out.println("````````````````````6`````````````````````````````````");
			PdfPCell cell12;

			cell12 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getSgstAmount().toString()), drfont6));// column
			// 12
			// invoiceObj.getCity()
			cell12.setBorder(Rectangle.BOX);

			PdfPCell cell13 = new PdfPCell(new Paragraph(amountTrilWithZeros(bean.getDanAmount().toString()), drfont6));
			cell13.setBorder(Rectangle.BOX);

			table.addCell(cell1);
			table.addCell(cell2);
			table.addCell(cell3);
			table.addCell(cell5);
			table.addCell(cell7);
			table.addCell(cell8);
			table.addCell(cell9);
			table.addCell(cell10);
			table.addCell(cell11);
			table.addCell(cell12);
			table.addCell(cell13);

			document.left(100f);
			document.top(150f);
			document.add(table);
			String totAmountInWords = NumberToWordsFormatMethod.inWordFormat(bean.getDanAmount().doubleValue()); // Total
			// Amount(Rs.)
			// System.out.println("````````````````````9``````````````````````````````````");
			Paragraph totalPayAmt = new Paragraph("   Total Amount Paid RS. In Words:" + totAmountInWords, drfont7d);
			totalPayAmt.setAlignment(Element.ALIGN_LEFT);
			document.add(totalPayAmt);

			Paragraph genIrrnPara = new Paragraph(" Genrated IRRN:" + bean.getIrnNO(), drfont8);
			genIrrnPara.setAlignment(Element.ALIGN_LEFT);
			document.add(genIrrnPara);
			// System.out.println("```````````````````10````````````````````````````````");
			Paragraph termCondition = new Paragraph("  Terms and conditions:", drfont6);
			termCondition.setAlignment(Element.ALIGN_LEFT);

			document.add(termCondition);
			Paragraph termCondetail = new Paragraph(
					" 1) The Courts in Mumbai shall have exclusive jurisdiction in respect of any dispute arising in connection with the above matter.",
					drfont6);
			document.add(termCondetail);

			Paragraph termCondition2 = new Paragraph(
					"2) Please revert hack to us within 7 days of receipt of the invoice in case of discreapancy in state code and/or GSTIN as mentioned herein the please of supply or your actual GSTIN by email on",
					drfont6);

			// SAC code/Service Category:
			Paragraph sacServiceCat = new Paragraph("\r \n SAC code/Service Category:", drfont8);
			sacServiceCat.setAlignment(Element.ALIGN_LEFT);
			document.add(sacServiceCat);

			Paragraph toatalCond3 = new Paragraph(
					"Code under GST:997113 - Credit - granting services including stand - by commitment, guarantees & securities.",
					drfont8);
			toatalCond3.setAlignment(Element.ALIGN_LEFT);

			document.add(toatalCond3);
			// *** This is computer generated document, no signature required******
			Paragraph totalCond4 = new Paragraph(
					"\r \n *** This is computer generated document, no signature required******", drfont8);
			totalCond4.setAlignment(Element.ALIGN_LEFT);
			document.add(totalCond4);

			Paragraph autCgt = new Paragraph(" For CGTMSE", drfont8);
			autCgt.setAlignment(Element.ALIGN_RIGHT);
			document.add(autCgt);

			Paragraph sd = new Paragraph(" Sd/-", drfont8);
			sd.setAlignment(Element.ALIGN_RIGHT);
			document.add(sd);

			Paragraph sigCgt = new Paragraph("  Autorised Signatory", drfont8);
			sigCgt.setAlignment(Element.ALIGN_RIGHT);
			document.add(sigCgt);
			document.add(new Paragraph("   "));
			/*
			 * BarcodeQRCode barcodeQRCode = new
			 * BarcodeQRCode(String.valueOf(invoiceObj.getTaxableAmt()), 1000, 1000, null);
			 * Image dkrQrImage = barcodeQRCode.getImage(); dkrQrImage.scaleAbsolute(100,
			 * 100); document.add(dkrQrImage);
			 */
			pdfMsg = "PDF Successfully Genreated";
			System.out.println("::::::::::::PDF Generation Message:::::::::::" + pdfMsg);
			document.close();
			OutputStream os = response.getOutputStream();
			FileInputStream fis = new FileInputStream(
					new File(contextPath + "\\Download\\DataCSVFile" + strDate + ".pdf"));
			int x = fis.available();
			byte b[] = new byte[x];
			System.out.println(" byte obj size~~~~~~~~~~~~~~~~~~~" + b.length);
			fis.read(b);
			if (response != null)
				response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", (new StringBuilder("attachment; filename=TaxInvoiceASF"))
					.append(bean.getDanId()).append(".pdf").toString());
			os.write(b);
			os.flush();
		} catch (FileNotFoundException e) {
			pdfMsg = "Error in pdf download." + e.getMessage();
		} catch (IOException e) {
			pdfMsg = "Error in pdf download.." + e.getMessage();
		} catch (Exception e) {
			pdfMsg = "Error in pdf download..." + e.getMessage();
			System.out.println("pdfMsg------------" + pdfMsg);

		}
		return pdfMsg;
	}

	/* END Add By VinodSingh on 01-Jan-2021 For ASF Tax invoice Report */
	static String amountTrilWithZeros(String amount) {
		int decimalPlaces = 2;
		String amtInString = "0.00";
		if (amount != null && !amount.equals("")) {
			BigDecimal amountInBigDecimal = new BigDecimal(amount);
			amountInBigDecimal = amountInBigDecimal.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
			amtInString = amountInBigDecimal.toString();
			System.out.println("amountInBigDecimal:::" + amountInBigDecimal + " ,amtInString::" + amtInString);
		}
		return amtInString;
	}


	String roundUP(String strAMT) {
		DecimalFormat df = new DecimalFormat("0");
		String amount="0.00";
		if(!strAMT.equals("")){				
			BigDecimal input = new BigDecimal(strAMT);
			df.setRoundingMode(RoundingMode.HALF_UP);
			amount = df.format(input);
		}
		return amount;
	}
	
	
}
