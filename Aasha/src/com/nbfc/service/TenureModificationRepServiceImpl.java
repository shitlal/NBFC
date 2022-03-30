package com.nbfc.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nbfc.bean.TenureModificationRepBean;
import com.nbfc.dao.TenureModificationRepDao;

@Service("TenureModificationRepService")
public class TenureModificationRepServiceImpl implements TenureModificationRepService{
	
	@Autowired
	TenureModificationRepDao tenureModificationRepDao;
	
	
	public List<TenureModificationRepBean> getTenureModificationReport(String userId, Date toDate,	Date fromDate, String role, String mliIdOrName) {
		// TODO Auto-generated method stub
		return tenureModificationRepDao.getTenureModificationReport(userId, toDate, fromDate, role, mliIdOrName);
	}

}
