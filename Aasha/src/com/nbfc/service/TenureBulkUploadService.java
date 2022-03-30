package com.nbfc.service;

import java.util.List;

import com.nbfc.bean.TenureBulkUploadBean;

public interface TenureBulkUploadService {
	
	public String getMemberId(String userId);

	public List<TenureBulkUploadBean> getNPADetailsForApproval(String cgpan, String usrId);

}
