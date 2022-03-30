package com.nbfc.service;

import java.util.Date;
import java.util.List;

import com.nbfc.bean.AppropriationBean;

public interface AppropriationService {
	
	public List<AppropriationBean> getAppropriationDetails(String userId, Date toDate, Date fromDate, String role,String mliIdOrName);
	
	public String updatePaymentStatus(AppropriationBean appropriationBean);
	
	
}
