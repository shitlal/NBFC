package com.nbfc.bean;

import java.sql.Date;
import java.util.List;

public class TenureModificationDetailsBean {
	private String mliID;
	private String bankName;
	private String returnRemark;
	private String expiryDate;
	private String firstDisburseDate;
	private String tenure;
	private String mseName;
	private String state;
	private String reviseTenure;
	private String cgpan;
	private String gstNo;
	private String AcceptReturn;
	private String MLI_STATUS;
	private String noIntrest;
	private String 	ckbankAuthority;
	private String msg;
	private Integer Cnt;
	private Integer tId;
	private String CGPANReturnStatus;
	private List<String> statusList;
	
	
	
	
	
	
	
	
	
	public String getNoIntrest() {
		return noIntrest;
	}
	public String getCGPANReturnStatus() {
		return CGPANReturnStatus;
	}
	public void setCGPANReturnStatus(String cGPANReturnStatus) {
		CGPANReturnStatus = cGPANReturnStatus;
	}
	public void setNoIntrest(String noIntrest) {
		this.noIntrest = noIntrest;
	}
	public String getFirstDisburseDate() {
		return firstDisburseDate;
	}
	public void setFirstDisburseDate(String firstDisburseDate) {
		this.firstDisburseDate = firstDisburseDate;
	}
	public String getCkbankAuthority() {
		return ckbankAuthority;
	}
	public void setCkbankAuthority(String ckbankAuthority) {
		ckbankAuthority = ckbankAuthority;
	}
	public Integer gettId() {
		return tId;
	}
	public void settId(Integer tId) {
		this.tId = tId;
	}
	public String getReturnRemark() {
		return returnRemark;
	}
	public void setReturnRemark(String returnRemark) {
		this.returnRemark = returnRemark;
	}
	public Integer getCnt() {
		return Cnt;
	}
	public void setCnt(Integer cnt) {
		Cnt = cnt;
	}
	private String rmsg;
	
	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getRmsg() {
		return rmsg;
	}
	public void setRmsg(String rmsg) {
		this.rmsg = rmsg;
	}
	public String getMLI_STATUS() {
		return MLI_STATUS;
	}
	public void setMLI_STATUS(String mLI_STATUS) {
		MLI_STATUS = mLI_STATUS;
	}
	public String getAcceptReturn() {
		return AcceptReturn;
	}
	public void setAcceptReturn(String acceptReturn) {
		AcceptReturn = acceptReturn;
	}
	public String getGstNo() {
		return gstNo;
	}
	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}
	public String getCgpan() {
		return cgpan;
	}
	public void setCgpan(String cgpan) {
		this.cgpan = cgpan;
	}
	private String reviseExpirydate;
	private String modificationRemark;
	private String accStandard;
	private String bankAuthority;
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGuaranteeStartDate() {
		return guaranteeStartDate;
	}
	public void setGuaranteeStartDate(String guaranteeStartDate) {
		this.guaranteeStartDate = guaranteeStartDate;
	}
	private String guaranteeStartDate;
	public String getMliID() {
		return mliID;
	}
	public void setMliID(String mliID) {
		this.mliID = mliID;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getTenure() {
		return tenure;
	}
	public void setTenure(String tenure) {
		this.tenure = tenure;
	}
	public String getMseName() {
		return mseName;
	}
	public void setMseName(String mseName) {
		this.mseName = mseName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getReviseTenure() {
		return reviseTenure;
	}
	public void setReviseTenure(String reviseTenure) {
		this.reviseTenure = reviseTenure;
	}
	public String getReviseExpirydate() {
		return reviseExpirydate;
	}
	public void setReviseExpirydate(String reviseExpirydate) {
		this.reviseExpirydate = reviseExpirydate;
	}
	public String getModificationRemark() {
		return modificationRemark;
	}
	public void setModificationRemark(String modificationRemark) {
		this.modificationRemark = modificationRemark;
	}
	public String getAccStandard() {
		return accStandard;
	}
	public void setAccStandard(String accStandard) {
		this.accStandard = accStandard;
	}
	public String getBankAuthority() {
		return bankAuthority;
	}
	public void setBankAuthority(String bankAuthority) {
		this.bankAuthority = bankAuthority;
	}
	public List<String> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}
	
	
	
}