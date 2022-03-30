package com.nbfc.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.nbfc.bean.ApplicationStatusDetailsBean;
import com.nbfc.bean.TenureModificationDetailsBean;

@Repository("TenureModificationDao")
public interface TenureModificationDao {

	TenureModificationDetailsBean getTenureModificationDetails(
			String cgpan);

	
	int submitTenureModificationDetails(
			TenureModificationDetailsBean tenureBean, String loginUserMemId,
			String usr_id);


	List<TenureModificationDetailsBean> getTenureModDetailsApproval(
			String loginUserMemId);


	TenureModificationDetailsBean updateStatusTenureApprovedReturn(
			String userId, Map<String, Object> claimStatusMapObj,
			String userRole, String cHK);


	TenureModificationDetailsBean getValidateCgpanForTenureMod(String cgpan,
			String userId);


	List<TenureModificationDetailsBean> getTenureReturnedRecordsByNBFCChecker(
			String loginUserMemId);


	TenureModificationDetailsBean getTenureDetailsForUpdate(int t_ID);


	int updateTenureDataDetails(TenureModificationDetailsBean bean,
			int tenId, String userId);
	
}
