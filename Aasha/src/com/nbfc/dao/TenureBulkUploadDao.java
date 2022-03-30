package com.nbfc.dao;

import java.util.List;

import com.nbfc.bean.TenureBulkUploadBean;

public interface TenureBulkUploadDao {

	String getMemberId(String userId);
	List<TenureBulkUploadBean> getNPADetailsForApproval(String cgpan, String usrId);
	

}
