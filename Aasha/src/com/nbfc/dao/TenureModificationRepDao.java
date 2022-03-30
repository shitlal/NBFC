package com.nbfc.dao;

import java.util.Date;
import java.util.List;

import com.nbfc.bean.TenureModificationRepBean;

public interface TenureModificationRepDao {
	
	public List<TenureModificationRepBean> getTenureModificationReport(String userId, Date toDate, Date fromDate, String role,String mliLongName);
		
}

