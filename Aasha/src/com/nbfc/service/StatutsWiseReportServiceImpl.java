package com.nbfc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nbfc.bean.ApplicationStatusDetailsBean;
import com.nbfc.bean.MliWiseReportDetailBean;
import com.nbfc.dao.ApplicationStatusDao;
import com.nbfc.dao.StatutsWiseReportDao;
@Service("StatutsWiseReportService")
public class StatutsWiseReportServiceImpl implements StatutsWiseReportService{
	@Autowired
	StatutsWiseReportDao statutsWiseReportDao;
	@Override
	public ArrayList<Object[]> StatutsWiseReport(String userId, String toDate, String fromDate, String status) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.StatutsWiseReport(userId, toDate, fromDate, status);
	}

	@Override
	public List<Map<String, Object>> getStatutsDetails(String userId,String role,
			Date toDate, Date fromDate, String status) {
		// TODO Auto-generated method stub
		   return statutsWiseReportDao.getStatutsDetails(userId, role, toDate, fromDate, status);
	}

	@Override
	public List<Map<String, Object>> getMliWeise(String role, String userid, Date fromDate, Date toDate, String MliId,HttpServletRequest request) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getMliWeise(role, userid, fromDate, toDate,  MliId,request);
	}

	 @Override
	public List<Object[]> getMliWiseDate(String role, String userid, String fromDate, String toDate, String MliId) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getMliWiseDate(role, userid, fromDate, toDate, MliId);
	} 

	/*@Override
	public List<Object[]> getStatutsWiseData(String userId, String role, String fromDate , String toDate, String statuts) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getStatutsWiseData(userId, role, fromDate, toDate, statuts);
	}*/
	
	@Override
	public List<Object[]> getStatutsWiseData(String userId, String role, String fromDate , String toDate, String statuts) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getStatutsWiseData(userId, role, fromDate, toDate, statuts);
	}

	@Override
	public List<MliWiseReportDetailBean> getMliWeiseReport(String role, String userid, Date fromDate, Date toDate,
			String MliId) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getMliWeiseReport(role, userid, fromDate, toDate, MliId);
	}

	@Override
	public List<Map<String, Object>> getAsfReportDetail(String role, String userid, Date fromDate, Date toDate,
			String MliId) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getAsfReportDetail(role, userid, fromDate, toDate,  MliId);
	}

	@Override
	public List<MliWiseReportDetailBean> getASFfReportDetail(String role, String userid, Date fromDate, Date toDate,
			String MliId) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getASFfReportDetail(role, userid, fromDate, toDate, MliId);
	}

	@Override
	public List<Map<String, Object>> getDanASFDetail(String role, String userid, Date fromDate, Date toDate,
			String MliId,String SSNmae) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getDanASFDetail(role, userid, fromDate, toDate, MliId,SSNmae);
	}

	@Override
	public List<MliWiseReportDetailBean> getDanGfReportDowanload(String role, String userid, Date fromDate, Date toDate,
			String MliId, String ssName) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getDanGfReportDowanload(role, userid, fromDate, toDate, MliId, ssName);
	}

	@Override
	public List<Object[]> getDANGFDetails(String role, String userid, String fromDate, String toDate, String MliId,
			String ssName) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getDANGFDetails(role, userid, fromDate,toDate,  MliId, ssName);
	}

	@Override
	public List<Object[]> getDanId(String DanId) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getDanId(DanId);
	}

	@Override
	public ApplicationStatusDetailsBean getapplicationDetails(String FileId) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getapplicationDetails(FileId);
	}

	@Override
	public List<Object[]> getDANASFDetails(String role, String userid, String fromDate, String toDate, String MliId,
			String ssName) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getDANASFDetails(role, userid, fromDate, toDate, MliId, ssName);
	}

	@Override
	public List<Object[]> getDanIdForASF(String DanId) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getDanIdForASF(DanId);
	}

	@Override
	public String getMEMBNKID(String UserId) {
		// TODO Auto-generated method stub
		return statutsWiseReportDao.getMEMBNKID(UserId);
	}

	@Override
	public Map<String ,String> getBankName(String BankId) {
		// TODO Auto-generated method stub
		return (Map<String, String>)statutsWiseReportDao.getBankName(BankId);
	}
	 
	Map<String ,String> mapObj1=new HashMap<String ,String>();
	/*@Override
	public List<Object[]> getMliWiseDate(String role, String userid, String fromDate, String toDate, String MliId) {
		// TODO Auto-generated method stub
		  return statutsWiseReportDao.getMliWeise(role, userid, fromDate, toDate,  MliId);
	}*/
	@Override
	public Map<String, String> getAllMliLongName() {
		// TODO Auto-generated method stub
		return (Map<String, String>)statutsWiseReportDao.getAllMliLongName();
	}
	 @Override
		public List<Object[]> getMLISDetails(String MemberBankID) {
			// TODO Auto-generated method stub
			return statutsWiseReportDao.getMLISDetails(MemberBankID);
		}
		@Override
		public List<Object[]> getListOfMlis(String MliID) {
			// TODO Auto-generated method stub
			return statutsWiseReportDao.getListOfMlis(MliID);
		}
		@Override
		public List<Map<String, Object>> getWomenEntrepreneurReportDetails(String role, String userid, Date fromDate,
				Date toDate, String state, String distict, String MliId) {
			// TODO Auto-generated method stub
			return statutsWiseReportDao.getWomenEntrepreneurReportDetails(role, userid, fromDate, toDate, state, distict, MliId);
		}
		
		@Override
		public List<MliWiseReportDetailBean> getWomenEntrepreneurDetails(String role, String userid, Date fromDate, Date toDate,
				String state, String distict, String MliId) {
			// TODO Auto-generated method stub
			return statutsWiseReportDao.getWomenEntrepreneurDetails(role, userid, fromDate, toDate, state, distict, MliId);
		}
		
		
		@Override
		public List<Object[]> getStateName(String role, String MemberId) {
			// TODO Auto-generated method stub
			return statutsWiseReportDao.getStateName(role, MemberId);
		}

		@Override
		public List<Object[]> getDistictName(String role, String MemberId,String state_code) {
			// TODO Auto-generated method stub
			return statutsWiseReportDao.getDistictName(role,MemberId,state_code);
		}

		@Override
		public String getsateNme(String state_code) {
			// TODO Auto-generated method stub
			return statutsWiseReportDao.getsateNme(state_code);
		}

		@Override
		public List<Object[]> getWomenEntrepreneurDetailList(String role, String userid, String fromDate, String toDate,
				String state, String distict, String MliId) {
			// TODO Auto-generated method stub
			return statutsWiseReportDao.getWomenEntrepreneurDetailList(role, userid, fromDate, toDate, state, distict, MliId);
		}
		@Override
		public List<Object[]> getDanTeaxtGstReportList(String fromDate, String toDate) {
			// TODO Auto-generated method stub
			return statutsWiseReportDao.getDanTeaxtGstReportList(fromDate,toDate);
		}

		@Override
		public List<Object[]> getDanTeaxtAsfGstReportList(String fromDate, String toDate) {
			// TODO Auto-generated method stub
			return statutsWiseReportDao.getDanTeaxtAsfGstReportList(fromDate,toDate);
		}

		@Override
		public List<Object[]> getDANAppropriationASFGFDetails(String role, String userId, String fromDate,
				String toDate, String danType) {
			// TODO Auto-generated method stub
			return statutsWiseReportDao.getDANAppropriationASFGFDetails(role, userId, fromDate, toDate, danType);
		}
		
		@Override
		public List<Map<String, Object>> getGuaranteeWise(String role, String userid, Date fromDate, Date toDate ,HttpServletRequest request) {
			// TODO Auto-generated method stub
			return statutsWiseReportDao.getGuaranteeWise(role, userid, fromDate, toDate,request);
		}


		 
}
