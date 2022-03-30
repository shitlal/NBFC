package com.nbfc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;

import com.nbfc.bean.CGPANDetailsReportBean;
import com.nbfc.model.ProformaInvoiceDetails;
import com.nbfc.model.ProformaInvoiceDetailsASF;
import com.nbfc.model.TaxDetailMaster;
import com.nbfc.model.TaxInvoiceDetails;
import com.nbfc.model.TaxInvoiceDetailsASF;


public interface NBFCInvoiceDao {

	public List<TaxDetailMaster> getTaxInvoiceDeails();
	
	public void callTaxInvoiceReport(String memid,HttpServletResponse response,HttpServletRequest request,CGPANDetailsReportBean bean) throws JRException;

	public List<Object> getTaxReportDetails(String dan_id, String uid);
	public CGPANDetailsReportBean getTaxReportDetailsForGFAndTM(String dan_id, String uid,String danType);
	
	public void insertTaxInvoiceDetails(TaxInvoiceDetails taxDetails);

	public Integer getMaxTaxInvoiceIdCount();

	public Integer getTaxInvoiceIdCount(String dan_id);

	public String getTaxInvoiceNo(String dan_id);	
	
	public String getTaxInvoiceNoForGFAndTM(String portfolioName,String danType);
	
	public List<Object> getTaxInvoiceData(String mliId,HttpSession session);

	public List<Object> proformaInvoiceData(String mliId, HttpSession session);

	public Integer getProformaInvoiceIdCount(String dan_id);

	public Integer getMaxProformaInvoiceIdCount();

	public String getProformaInvoiceNo(String dan_id);
// mdofied by shashi on date 03-08-2021
	public void callProformaInvoiceReport(String dan_id,
			HttpServletResponse response, HttpServletRequest request,
			CGPANDetailsReportBean bean) throws Exception;

	public void insertProformaInvoiceDetails(ProformaInvoiceDetails proformaObj1);

	public List<Object> getTaxInvoiceDataASF(String mliId, HttpSession session);

	public Integer getTaxInvoiceIdCountASF(String danId);

	public List<Object> getTaxReportDetailsASF(String danId, String year);
	
	public List<Object> getProformaReportDetailsASF(String danId, String year);

	public void callTaxInvoiceReportASF(String danId, HttpServletResponse response, HttpServletRequest request,
			CGPANDetailsReportBean bean);

	public Integer getMaxTaxInvoiceIdCountASF();

	public void insertTaxInvoiceDetailsASF(TaxInvoiceDetailsASF taxDetailsObj);

	public String getTaxInvoiceNoASF(String danId);

	public List<Object> proformaInvoiceDataASF(String mliId, HttpSession session);

	public Integer getProformaInvoiceIdCountASF(String danId);

	public Integer getMaxProformaInvoiceIdCountASF();

	public void callProformaInvoiceReportASF(String danId, HttpServletResponse response, HttpServletRequest request,
			CGPANDetailsReportBean bean);

	public void insertProformaInvoiceDetailsASF(ProformaInvoiceDetailsASF proformaObj1);

	public String getProformaInvoiceNoASF(String danId);
	
	public List<CGPANDetailsReportBean> proformaInvoiceDatatTENURE(String mliId,
			HttpSession session);

	public List<Object> getDanDetailTenure(String danId, String userId);

	public void callProformaInvoiceReportTenure(String danId,
			HttpServletResponse response, HttpServletRequest request,
			CGPANDetailsReportBean bean);

	public Integer getMaxProformaInvoiceIdCountTENURE();

	public String genratePdfWithInvoiceNo(HttpServletResponse response, CGPANDetailsReportBean bean, String contextPath, String strDate);

	public List<Object> getTaxQRCode(String taxInvoiceId);
	
	public String genPdfTaxInvoiceForASF(HttpServletResponse response, CGPANDetailsReportBean bean, String contextPath,	String strDate);
	
//	public String getDanTypeBasedOnPortfolioNo(String dan_id);
	





	

	
}
