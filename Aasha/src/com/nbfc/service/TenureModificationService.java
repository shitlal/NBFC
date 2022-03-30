package com.nbfc.service;

import java.util.List;
import java.util.Map;

import com.nbfc.bean.TenureModificationDetailsBean;
import com.nbfc.helper.CGTMSECheckerBatchUploadsPendingForApprovalHelper;
import com.nbfc.helper.CGTMSEMakerBatchUploadDetails;
import com.nbfc.model.CGTMSECheckerBatchUploadsPendingForApproval;


public interface TenureModificationService {

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


	int updateTenureDataDetails(TenureModificationDetailsBean bean, int tenId,
			String userId);


	TenureModificationDetailsBean getTenureDetailsForUpdate(Integer tenId);



	

}
