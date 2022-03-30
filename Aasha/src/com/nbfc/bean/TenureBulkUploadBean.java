package com.nbfc.bean;

import org.springframework.web.multipart.MultipartFile;

public class TenureBulkUploadBean {
	
	
	private MultipartFile file;
	private String MSE_Name;
	private String cgpan;
	private String revisedTenure;
	private String Modification_Reason;
	
	
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getMSE_Name() {
		return MSE_Name;
	}
	public void setMSE_Name(String mSE_Name) {
		MSE_Name = mSE_Name;
	}
	public String getCgpan() {
		return cgpan;
	}
	public void setCgpan(String cgpan) {
		this.cgpan = cgpan;
	}
	public String getRevisedTenure() {
		return revisedTenure;
	}
	public void setRevisedTenure(String revisedTenure) {
		this.revisedTenure = revisedTenure;
	}
	public String getModification_Reason() {
		return Modification_Reason;
	}
	public void setModification_Reason(String modification_Reason) {
		Modification_Reason = modification_Reason;
	}
//	@Override
//	public String toString() {
//		return "TenureBulkUploadBean [MSE_Name=" + MSE_Name + ", cgpan=" + cgpan + ", revisedTenure=" + revisedTenure
//				+ ", Modification_Reason=" + Modification_Reason + "]";
//	}
//	
	@Override
	public String toString() {
		return "TenureBulkUploadBean [file=" + file + ", MSE_Name=" + MSE_Name + ", cgpan=" + cgpan + ", revisedTenure="
				+ revisedTenure + ", Modification_Reason=" + Modification_Reason + "]";
	}
	
	
	
	

}
