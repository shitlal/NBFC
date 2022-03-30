package com.nbfc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nbfc.bean.MISReportbanforClaim;
import com.nbfc.bean.MisReportbean;
import com.nbfc.bean.OtherMisRepor;
import com.nbfc.bean.PortfolioDetailsBean;
import com.nbfc.dao.MisReportdao;

@Service("MISService")
public class MISServiceImpl implements MISService {
	@Autowired
	MisReportdao MisReportDao;

	public String getMemberId(String userId) {
		return MisReportDao.getMemberId(userId);
	}

	@Override
	public List<MisReportbean> getReportdataforMis(String userId, Date toDate, Date fromDate) {
		return MisReportDao.getReportdataforMis(userId, toDate, fromDate);
	}

	@Override
	public List<MISReportbanforClaim> getReportdataforClaimMis(String userId, Date toDate, Date fromDate) {
		return MisReportDao.getReportdataforClaimMis(userId, toDate, fromDate);
	}
	
	@Override
	public List<OtherMisRepor> getOverAllReportdataforClaimMis(String userId, Date toDate, Date fromDate) {
		return MisReportDao.getOverAllReportdataforClaimMis(userId, toDate, fromDate);
	}


}
