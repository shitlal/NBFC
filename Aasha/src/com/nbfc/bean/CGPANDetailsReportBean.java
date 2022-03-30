package com.nbfc.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;


public class CGPANDetailsReportBean {

	private BigDecimal dan_amount1;
	private String Cgpan;
	private String danId;
	private String totalAmt;
	private String irnNO;
	private String expiryDate;
	private String qRcode;
	private BigDecimal outstandingAmtStr12;
	private BigDecimal guaranteeAmtStr12;
	private String address;
	private String gstNo;
	private String payId;
	private String outstandingAmtStr;
	private String guaranteeAmtStr;
	private String dan_AmtStr;
	private String base_AmtStr;
	private String asfYear;
	private double outstandingAmt;
	private BigDecimal sanctionAMt;
	private BigDecimal sanctionAMt1;
	private double guaranteeAmt;
	private BigDecimal guaranteeAmt1;
	private double dci_base_amt;
	private double dci_total_amt;
	private String dciGuaranteeeSDate;
	private String appsubmittedDate;
	private String dciAppropriationDate;
	private String crystalizationDate;
	private String ssiName;
	private String mliId;
	private String displayCgpen;
	private String fYYear;
	private double outstandingAmount1;	
	private double dan_Amt;
	private String status;
	private String Taxid;
	private Double igstAmt;
	private Double cgstAmt;
	private Double sgstAmt;
	private Float guaranteeFee;
	private Long taxInvoiceId;
	private String portfolioName;
	private String bankname;
	private String state;
	private String date;
	private String userId;
	private String appropriation_by;
	private String stateCode;
	
	private BigDecimal danAmount;
	private BigDecimal baseAmount;
	private BigDecimal igstAmount;
	private BigDecimal cgstAmount;
	private BigDecimal sgstAmount;
	private BigDecimal outStandingAmount;
	private BigDecimal totalAmount;	
	private String danType;
	private String dci_standard_rate;
	private String taxInvId;
	private String MLINAME;
	private String TaxDate;
	private String igstRate;
	private String cgstRate;
	private String sgstRate;
	
	
	
	public String getIgstRate() {
		return igstRate;
	}
	public void setIgstRate(String igstRate) {
		this.igstRate = igstRate;
	}
	public String getCgstRate() {
		return cgstRate;
	}
	public void setCgstRate(String cgstRate) {
		this.cgstRate = cgstRate;
	}
	public String getSgstRate() {
		return sgstRate;
	}
	public void setSgstRate(String sgstRate) {
		this.sgstRate = sgstRate;
	}
	public String getTaxDate() {
		return TaxDate;
	}
	public void setTaxDate(String taxDate) {
		TaxDate = taxDate;
	}
	public String getMLINAME() {
		return MLINAME;
	}
	public void setMLINAME(String mLINAME) {
		MLINAME = mLINAME;
	}
	public BigDecimal getDan_amount1() {
		return dan_amount1;
	}
	public void setDan_amount1(BigDecimal dan_amount1) {
		this.dan_amount1 = dan_amount1;
	}
	public String getCgpan() {
		return Cgpan;
	}
	public void setCgpan(String cgpan) {
		Cgpan = cgpan;
	}
	public String getDanId() {
		return danId;
	}
	public void setDanId(String danId) {
		this.danId = danId;
	}
	public String getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getIrnNO() {
		return irnNO;
	}
	public void setIrnNO(String irnNO) {
		this.irnNO = irnNO;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getqRcode() {
		return qRcode;
	}
	public void setqRcode(String qRcode) {
		this.qRcode = qRcode;
	}
	public BigDecimal getOutstandingAmtStr12() {
		return outstandingAmtStr12;
	}
	public void setOutstandingAmtStr12(BigDecimal outstandingAmtStr12) {
		this.outstandingAmtStr12 = outstandingAmtStr12;
	}
	public BigDecimal getGuaranteeAmtStr12() {
		return guaranteeAmtStr12;
	}
	public void setGuaranteeAmtStr12(BigDecimal guaranteeAmtStr12) {
		this.guaranteeAmtStr12 = guaranteeAmtStr12;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGstNo() {
		return gstNo;
	}
	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public String getOutstandingAmtStr() {
		return outstandingAmtStr;
	}
	public void setOutstandingAmtStr(String outstandingAmtStr) {
		this.outstandingAmtStr = outstandingAmtStr;
	}
	public String getGuaranteeAmtStr() {
		return guaranteeAmtStr;
	}
	public void setGuaranteeAmtStr(String guaranteeAmtStr) {
		this.guaranteeAmtStr = guaranteeAmtStr;
	}
	public String getDan_AmtStr() {
		return dan_AmtStr;
	}
	public void setDan_AmtStr(String dan_AmtStr) {
		this.dan_AmtStr = dan_AmtStr;
	}
	public String getBase_AmtStr() {
		return base_AmtStr;
	}
	public void setBase_AmtStr(String base_AmtStr) {
		this.base_AmtStr = base_AmtStr;
	}
	public String getAsfYear() {
		return asfYear;
	}
	public void setAsfYear(String asfYear) {
		this.asfYear = asfYear;
	}
	public double getOutstandingAmt() {
		return outstandingAmt;
	}
	public void setOutstandingAmt(double outstandingAmt) {
		this.outstandingAmt = outstandingAmt;
	}
	public BigDecimal getSanctionAMt() {
		return sanctionAMt;
	}
	public void setSanctionAMt(BigDecimal obj1) {
		this.sanctionAMt = obj1;
	}
	public BigDecimal getSanctionAMt1() {
		return sanctionAMt1;
	}
	public void setSanctionAMt1(BigDecimal sanctionAMt1) {
		this.sanctionAMt1 = sanctionAMt1;
	}
	public double getGuaranteeAmt() {
		return guaranteeAmt;
	}
	public void setGuaranteeAmt(double guaranteeAmt) {
		this.guaranteeAmt = guaranteeAmt;
	}
	public BigDecimal getGuaranteeAmt1() {
		return guaranteeAmt1;
	}
	public void setGuaranteeAmt1(BigDecimal guaranteeAmt1) {
		this.guaranteeAmt1 = guaranteeAmt1;
	}
	public double getDci_base_amt() {
		return dci_base_amt;
	}
	public void setDci_base_amt(double dci_base_amt) {
		this.dci_base_amt = dci_base_amt;
	}
	public double getDci_total_amt() {
		return dci_total_amt;
	}
	public void setDci_total_amt(double dci_total_amt) {
		this.dci_total_amt = dci_total_amt;
	}
	public String getDciGuaranteeeSDate() {
		return dciGuaranteeeSDate;
	}
	public void setDciGuaranteeeSDate(String dciGuaranteeeSDate) {
		this.dciGuaranteeeSDate = dciGuaranteeeSDate;
	}
	public String getAppsubmittedDate() {
		return appsubmittedDate;
	}
	public void setAppsubmittedDate(String appropriationDate1) {
		this.appsubmittedDate = appropriationDate1;
	}
	public String getDciAppropriationDate() {
		return dciAppropriationDate;
	}
	public void setDciAppropriationDate(String dciAppropriationDate) {
		this.dciAppropriationDate = dciAppropriationDate;
	}
	public String getCrystalizationDate() {
		return crystalizationDate;
	}
	public void setCrystalizationDate(String crystalizationDate) {
		this.crystalizationDate = crystalizationDate;
	}
	public String getSsiName() {
		return ssiName;
	}
	public void setSsiName(String ssiName) {
		this.ssiName = ssiName;
	}
	public String getMliId() {
		return mliId;
	}
	public void setMliId(String mliId) {
		this.mliId = mliId;
	}
	public String getDisplayCgpen() {
		return displayCgpen;
	}
	public void setDisplayCgpen(String displayCgpen) {
		this.displayCgpen = displayCgpen;
	}
	public String getfYYear() {
		return fYYear;
	}
	public void setfYYear(String fYYear) {
		this.fYYear = fYYear;
	}
	public double getOutstandingAmount1() {
		return outstandingAmount1;
	}
	public void setOutstandingAmount1(double outstandingAmount1) {
		this.outstandingAmount1 = outstandingAmount1;
	}
	public double getDan_Amt() {
		return dan_Amt;
	}
	public void setDan_Amt(double dan_Amt) {
		this.dan_Amt = dan_Amt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTaxid() {
		return Taxid;
	}
	public void setTaxid(String taxid) {
		Taxid = taxid;
	}
	public Double getIgstAmt() {
		return igstAmt;
	}
	public void setIgstAmt(Double igstAmt2) {
		this.igstAmt = igstAmt2;
	}
	public Double getCgstAmt() {
		return cgstAmt;
	}
	public void setCgstAmt(Double cgstAmt) {
		this.cgstAmt = cgstAmt;
	}
	public Double getSgstAmt() {
		return sgstAmt;
	}
	public void setSgstAmt(Double sgstAmt) {
		this.sgstAmt = sgstAmt;
	}
	public Float getGuaranteeFee() {
		return guaranteeFee;
	}
	public void setGuaranteeFee(Float guaranteeFee) {
		this.guaranteeFee = guaranteeFee;
	}
	public Long getTaxInvoiceId() {
		return taxInvoiceId;
	}
	public void setTaxInvoiceId(Long taxInvoiceId) {
		this.taxInvoiceId = taxInvoiceId;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAppropriation_by() {
		return appropriation_by;
	}
	public void setAppropriation_by(String appropriation_by) {
		this.appropriation_by = appropriation_by;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public BigDecimal getDanAmount() {
		return danAmount;
	}
	public void setDanAmount(BigDecimal danAmount) {
		this.danAmount = danAmount;
	}
	public BigDecimal getBaseAmount() {
		return baseAmount;
	}
	public void setBaseAmount(BigDecimal baseAmount) {
		this.baseAmount = baseAmount;
	}
	public BigDecimal getIgstAmount() {
		return igstAmount;
	}
	public void setIgstAmount(BigDecimal igstAmount) {
		this.igstAmount = igstAmount;
	}
	public BigDecimal getCgstAmount() {
		return cgstAmount;
	}
	public void setCgstAmount(BigDecimal cgstAmount) {
		this.cgstAmount = cgstAmount;
	}
	public BigDecimal getSgstAmount() {
		return sgstAmount;
	}
	public void setSgstAmount(BigDecimal sgstAmount) {
		this.sgstAmount = sgstAmount;
	}
	public BigDecimal getOutStandingAmount() {
		return outStandingAmount;
	}
	public void setOutStandingAmount(BigDecimal outStandingAmount) {
		this.outStandingAmount = outStandingAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getDanType() {
		return danType;
	}
	public void setDanType(String danType) {
		this.danType = danType;
	}
	public String getDci_standard_rate() {
		return dci_standard_rate;
	}
	public void setDci_standard_rate(String dci_standard_rate) {
		this.dci_standard_rate = dci_standard_rate;
	}
	public String getTaxInvId() {
		return taxInvId;
	}
	public void setTaxInvId(String taxInvId) {
		this.taxInvId = taxInvId;
	}

}
