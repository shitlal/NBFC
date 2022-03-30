package com.nbfc.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nbfc.bean.AppropriationBean;
import com.nbfc.dao.AppropriationDao;

@Service("AppropriationService")
public class AppropriationServiceImpl implements AppropriationService{
	
	@Autowired
	AppropriationDao appropriationDao;
	
	
	public List<AppropriationBean> getAppropriationDetails(String paymentStatus, Date toDate,Date fromDate, String role, String mliIdOrName) {
		// TODO Auto-generated method
		return appropriationDao.getAppropriationDetails(paymentStatus, toDate, fromDate, role, mliIdOrName);
	}
	
	public String updatePaymentStatus(AppropriationBean aappropriationBean) {
		// TODO Auto-generated method stub
	return appropriationDao.updatePaymentStatus(aappropriationBean);
	}
}
