package com.nbfc.dao;

import java.util.Date;
import java.util.List;

import com.nbfc.bean.AppropriationBean;

public interface AppropriationDao {
	
	public List<AppropriationBean> getAppropriationDetails(String userId, Date toDate, Date fromDate, String role,String mliLongName);
		
	public String updatePaymentStatus(AppropriationBean appropriationBean);
}

