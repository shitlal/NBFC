package com.nbfc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nbfc.bean.ITPANSearchHistoryBean;
import com.nbfc.bean.NPADetailsBean;
import com.nbfc.bean.NPAMarkBean;
import com.nbfc.bean.NPATCDetailsBean;
import com.nbfc.bean.NPAWCDetailsBean;

import com.nbfc.bean.PortfolioBatchBean;
import com.nbfc.bean.PortfolioDetailsBean;
import com.nbfc.model.StateMaster;

public interface NPAService {
	
	public String getMemberId(String userId);
	public String getCGPAN();
	public NPADetailsBean getCGPANDetails(String cgpan,String usrId);
	public int saveNPADetails(NPAMarkBean npaMarkBean,NPATCDetailsBean npaTCDetailsBean,NPAWCDetailsBean npaWCDetailsBean,String usrId);
	public List<NPADetailsBean> getNPADetails(String cgpan);
	public List<NPADetailsBean> getNPADetailsForCGTMSE(String status,String usrId, String cgpan,String sDate,String eDate);
	public List<NPADetailsBean> getNPADetailsForApproval(String useId,String status);
	public int updateNPAApproveReject(String cgpanList, String useId,String status,String userType,String ReturnRemark);
	public NPADetailsBean getModifyNPADetails(String cgpan,String userId);
	public int updateNPADetails(NPADetailsBean bean, String cgpan, String userId);
	
	public PortfolioDetailsBean getExposureDetails(String userId);
	
	public List<PortfolioBatchBean> getPortfolioName(String userId);
	public List<ITPANSearchHistoryBean> getITpanSearchDetails(String itPan);
	public int NPASaveInAuditTable(NPADetailsBean bean, String cgpan, String userId);
	
	public List<Map<String, Object>> getNPAReportDetail(String userId, Date toDate,
			Date fromDate, String memberId, String role);
	public List<PortfolioDetailsBean> getExposureDetailsDASHBOARD(String userId);
	public Object getExposureExpiredCount(String userId);
	public NPADetailsBean getCGPANExpir(String cgpan, String userId);
	
}
