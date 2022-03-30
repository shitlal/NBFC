package com.nbfc.service;

import java.util.Date;
import java.util.List;
import com.nbfc.bean.TenureModificationRepBean;

public interface TenureModificationRepService {
	
	public List<TenureModificationRepBean> getTenureModificationReport(String userId, Date toDate, Date fromDate, String role,String mliIdOrName);
		
}
