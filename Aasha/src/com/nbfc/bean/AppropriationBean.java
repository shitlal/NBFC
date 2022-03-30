package com.nbfc.bean;

import java.util.List;

public class AppropriationBean {
	
	private String fromDate;
	private String toDate;
	private String bankName;
	private String paymentStatus;
	private String shbutton;
	private String danId;
	private String MLIID;	
	private String CGPAN;
	private String status;
	private String memberId;
	private String memberName;
	private String virtualAccountNumber;
	private String rpNumber;
	private boolean selectAll;
	private List<String> chcktbl;
	private Integer allocatedAmount;	
	private Integer inwardAmount;
	private String message;
	private String payment_date;	
	private Integer amount;	
			
	private List<String> instrumentNo;
	private List<Integer> instrumentAmount;
	private List<String> instrumentDate;
	private String dan_type;
	
	
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
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getDanId() {
		return danId;
	}
	public void setDanId(String danId) {
		this.danId = danId;
	}
	public String getMLIID() {
		return MLIID;
	}
	public void setMLIID(String mLIID) {
		MLIID = mLIID;
	}
	public String getCGPAN() {
		return CGPAN;
	}
	public void setCGPAN(String cGPAN) {
		CGPAN = cGPAN;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getVirtualAccountNumber() {
		return virtualAccountNumber;
	}
	public void setVirtualAccountNumber(String virtualAccountNumber) {
		this.virtualAccountNumber = virtualAccountNumber;
	}
	public String getRpNumber() {
		return rpNumber;
	}
	public void setRpNumber(String rpNumber) {
		this.rpNumber = rpNumber;
	}
	public boolean isSelectAll() {
		return selectAll;
	}
	public void setSelectAll(boolean selectAll) {
		this.selectAll = selectAll;
	}
	public List<String> getChcktbl() {
		return chcktbl;
	}
	public void setChcktbl(List<String> chcktbl) {
		this.chcktbl = chcktbl;
	}
	public Integer getAllocatedAmount() {
		return allocatedAmount;
	}
	public void setAllocatedAmount(Integer allocatedAmount) {
		this.allocatedAmount = allocatedAmount;
	}
	public Integer getInwardAmount() {
		return inwardAmount;
	}
	public void setInwardAmount(Integer inwardAmount) {
		this.inwardAmount = inwardAmount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPayment_date() {
		return payment_date;
	}
	public void setPayment_date(String payment_date) {
		this.payment_date = payment_date;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public List<String> getInstrumentNo() {
		return instrumentNo;
	}
	public void setInstrumentNo(List<String> instrumentNo) {
		this.instrumentNo = instrumentNo;
	}
	public List<Integer> getInstrumentAmount() {
		return instrumentAmount;
	}
	public void setInstrumentAmount(List<Integer> instrumentAmount) {
		this.instrumentAmount = instrumentAmount;
	}
	public List<String> getInstrumentDate() {
		return instrumentDate;
	}
	public void setInstrumentDate(List<String> instrumentDate) {
		this.instrumentDate = instrumentDate;
	}
	public String getShbutton() {
		return shbutton;
	}
	public void setShbutton(String shbutton) {
		this.shbutton = shbutton;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDan_type() {
		return dan_type;
	}
	public void setDan_type(String dan_type) {
		this.dan_type = dan_type;
	}	
}
