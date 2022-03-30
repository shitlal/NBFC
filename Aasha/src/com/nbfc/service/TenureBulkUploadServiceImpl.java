package com.nbfc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nbfc.bean.TenureBulkUploadBean;
import com.nbfc.dao.TenureBulkUploadDao;

@Service("TenureBulkUploadService")
public class TenureBulkUploadServiceImpl implements TenureBulkUploadService {

	//@Autowired
	TenureBulkUploadDao tenureBulkUploadDao;

	public String getMemberId(String userId) {
		return tenureBulkUploadDao.getMemberId(userId);
	}
	public List<TenureBulkUploadBean> getNPADetailsForApproval(String cgpan, String usrId) {
		return tenureBulkUploadDao.getNPADetailsForApproval(cgpan, usrId);

	}

}
