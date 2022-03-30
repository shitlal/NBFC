package com.nbfc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nbfc.bean.MISReportbanforClaim;
import com.nbfc.bean.MisReportbean;
import com.nbfc.bean.OtherMisRepor;
import com.nbfc.bean.PortfolioDetailsBean;

public interface MISService {
	public String getMemberId(String userId);
	public List<MisReportbean> getReportdataforMis(String userId, Date toDate, Date fromDate);
	public List<MISReportbanforClaim> getReportdataforClaimMis(String userId, Date toDate, Date fromDate);
	public List<OtherMisRepor> getOverAllReportdataforClaimMis(String userId, Date toDate, Date fromDate);



}
