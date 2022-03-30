package com.nbfc.bean;

public class TenureModificationRepBean {
	
	private String MLIID;
	private String fromDate;
	private String toDate;	
	private String CGPAN;
	private String bankName;
	private String mse_name;
	private String guarantee_amount;
	private String first_disbursement_date;
	private String tenor_in_months;
	private String old_exp_date;
	private String revised_tenure;
	private String revised_expiry_date;
	private String modification_remarks;
	private String dan_amt;
	private String tenure_approval_date;
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getCGPAN() {
		return CGPAN;
	}
	public void setCGPAN(String cGPAN) {
		CGPAN = cGPAN;
	}
	public String getMse_name() {
		return mse_name;
	}
	public void setMse_name(String mse_name) {
		this.mse_name = mse_name;
	}
	public String getGuarantee_amount() {
		return guarantee_amount;
	}
	public void setGuarantee_amount(String guarantee_amount) {
		this.guarantee_amount = guarantee_amount;
	}
	public String getFirst_disbursement_date() {
		return first_disbursement_date;
	}
	public void setFirst_disbursement_date(String first_disbursement_date) {
		this.first_disbursement_date = first_disbursement_date;
	}
	public String getTenor_in_months() {
		return tenor_in_months;
	}
	public void setTenor_in_months(String tenor_in_months) {
		this.tenor_in_months = tenor_in_months;
	}
	public String getOld_exp_date() {
		return old_exp_date;
	}
	public void setOld_exp_date(String old_exp_date) {
		this.old_exp_date = old_exp_date;
	}
	public String getRevised_tenure() {
		return revised_tenure;
	}
	public void setRevised_tenure(String revised_tenure) {
		this.revised_tenure = revised_tenure;
	}
	public String getRevised_expiry_date() {
		return revised_expiry_date;
	}
	public void setRevised_expiry_date(String revised_expiry_date) {
		this.revised_expiry_date = revised_expiry_date;
	}
	public String getModification_remarks() {
		return modification_remarks;
	}
	public void setModification_remarks(String modification_remarks) {
		this.modification_remarks = modification_remarks;
	}
	public String getDan_amt() {
		return dan_amt;
	}
	public void setDan_amt(String dan_amt) {
		this.dan_amt = dan_amt;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getMLIID() {
		return MLIID;
	}
	public void setMLIID(String mLIID) {
		MLIID = mLIID;
	}
	public String getTenure_approval_date() {
		return tenure_approval_date;
	}
	public void setTenure_approval_date(String tenure_approval_date) {
		this.tenure_approval_date = tenure_approval_date;
	}	
}
