package com.nbfc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "TAXINVOICE_DETAILS")
public class TaxDetailMaster {

	private static final long serialVersionUID = -723583058586873479L;

	@Id
	@Column(name = "T_ID")
	private Integer t_id;

	@Column(name = "TAXINVOICE_NO")
	private String Tax_Invoice_No;

	@Column(name = "MLI_NAME")
	private String mli_name;

	@Column(name = "ADDRESS_PRINCIPALPLACE")
	private String address;
	
	@Column(name = "BRANCH_NAME")
	private String branch_Name;

	@Column(name = "TAX_DATE")
	private Date taxDate = new Date();

	@Column(name = "PORTFOLIO_NO")
	private String portfoliono;

	

	@Column(name = "GSTIN")
	private String gstin;

	@Column(name = "MEMBER_ID")
	private String mem_id;

	@Column(name = "NBFC_NAME")
	private String nbfc_name;

	@Column(name = "SANCTION_AMOUNT")
	private Integer sanction_amt;

	@Column(name = "APPOVED_AMOUNT")
	private Integer approve_amt;

	@Column(name = "GUARANTEE_STDATE")
	private Date guarantee_sdate = new Date();
	
	@Column(name = "REAPPROVED_AMT")
	private Integer reapproved_amt;
	
	@Column(name = "REAPPROVED_DATE")
	private Date reapproved_date=new Date();
	
	@Column(name = "APPLICATION_NO")
	private String application_no;

	@Column(name = "APPLICATION_DATE")
	private Date application_date=new Date();
	
	@Column(name = "TAXABLE_AMT")
	private Integer taxable_amt;
	
	@Column(name = "CGST_RATE")
	private Integer cgst_rate;
	
	@Column(name = "CGST_AMT")
	private Integer cgst_amt;
	
	@Column(name = "IGST_RATE")
	private Integer igst_rate;
	
	@Column(name = "IGST_AMT")
	private Integer igst_amt;
	
	@Column(name = "SGST_RATE")
	private Integer sgst_rate;
	
	@Column(name = "SGST_AMT")
	private Integer SGST_AMT;
	
	@Column(name = "TOTAL_AMT")
	private Integer totalamt;
	
	@Column(name = "BANK_NAME")
	private String bank_name;
	    
	
	
	@Column(name = "BANKBRANCH_NAME")
	private String bankbranch_name;
	
	@Column(name = "ACCOUNT_NO")
	private String acc_no;

	@Column(name = "PAN_ID")
	private String pan;

	


	public Integer getT_id() {
		return t_id;
	}



	public void setT_id(Integer t_id) {
		this.t_id = t_id;
	}



	public String getTax_Invoice_No() {
		return Tax_Invoice_No;
	}



	public void setTax_Invoice_No(String tax_Invoice_No) {
		Tax_Invoice_No = tax_Invoice_No;
	}



	public String getMli_name() {
		return mli_name;
	}



	public void setMli_name(String mli_name) {
		this.mli_name = mli_name;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getBranch_Name() {
		return branch_Name;
	}



	public void setBranch_Name(String branch_Name) {
		this.branch_Name = branch_Name;
	}



	public Date getTaxDate() {
		return taxDate;
	}



	public void setTaxDate(Date taxDate) {
		this.taxDate = taxDate;
	}



	public String getPortfoliono() {
		return portfoliono;
	}



	public void setPortfoliono(String portfoliono) {
		this.portfoliono = portfoliono;
	}



	public String getGstin() {
		return gstin;
	}



	public void setGstin(String gstin) {
		this.gstin = gstin;
	}



	public String getMem_id() {
		return mem_id;
	}



	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}



	public String getNbfc_name() {
		return nbfc_name;
	}



	public void setNbfc_name(String nbfc_name) {
		this.nbfc_name = nbfc_name;
	}



	public Integer getSanction_amt() {
		return sanction_amt;
	}



	public void setSanction_amt(Integer sanction_amt) {
		this.sanction_amt = sanction_amt;
	}



	public Integer getApprove_amt() {
		return approve_amt;
	}



	public void setApprove_amt(Integer approve_amt) {
		this.approve_amt = approve_amt;
	}



	public Date getGuarantee_sdate() {
		return guarantee_sdate;
	}



	public void setGuarantee_sdate(Date guarantee_sdate) {
		this.guarantee_sdate = guarantee_sdate;
	}



	public Integer getReapproved_amt() {
		return reapproved_amt;
	}



	public void setReapproved_amt(Integer reapproved_amt) {
		this.reapproved_amt = reapproved_amt;
	}



	public Date getReapproved_date() {
		return reapproved_date;
	}



	public void setReapproved_date(Date reapproved_date) {
		this.reapproved_date = reapproved_date;
	}



	public String getApplication_no() {
		return application_no;
	}



	public void setApplication_no(String application_no) {
		this.application_no = application_no;
	}



	public Date getApplication_date() {
		return application_date;
	}



	public void setApplication_date(Date application_date) {
		this.application_date = application_date;
	}



	public Integer getTaxable_amt() {
		return taxable_amt;
	}



	public void setTaxable_amt(Integer taxable_amt) {
		this.taxable_amt = taxable_amt;
	}



	public Integer getCgst_rate() {
		return cgst_rate;
	}



	public void setCgst_rate(Integer cgst_rate) {
		this.cgst_rate = cgst_rate;
	}



	public Integer getCgst_amt() {
		return cgst_amt;
	}



	public void setCgst_amt(Integer cgst_amt) {
		this.cgst_amt = cgst_amt;
	}



	public Integer getIgst_rate() {
		return igst_rate;
	}



	public void setIgst_rate(Integer igst_rate) {
		this.igst_rate = igst_rate;
	}



	public Integer getIgst_amt() {
		return igst_amt;
	}



	public void setIgst_amt(Integer igst_amt) {
		this.igst_amt = igst_amt;
	}



	public Integer getSgst_rate() {
		return sgst_rate;
	}



	public void setSgst_rate(Integer sgst_rate) {
		this.sgst_rate = sgst_rate;
	}



	public Integer getSGST_AMT() {
		return SGST_AMT;
	}



	public void setSGST_AMT(Integer sGST_AMT) {
		SGST_AMT = sGST_AMT;
	}



	public Integer getTotalamt() {
		return totalamt;
	}



	public void setTotalamt(Integer totalamt) {
		this.totalamt = totalamt;
	}



	public String getBank_name() {
		return bank_name;
	}



	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}



	public String getBankbranch_name() {
		return bankbranch_name;
	}



	public void setBankbranch_name(String bankbranch_name) {
		this.bankbranch_name = bankbranch_name;
	}



	public String getAcc_no() {
		return acc_no;
	}



	public void setAcc_no(String acc_no) {
		this.acc_no = acc_no;
	}



	public String getPan() {
		return pan;
	}



	public void setPan(String pan) {
		this.pan = pan;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
