package com.nbfc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nbfc.bean.ApplicationStatusDetailsBean;
import com.nbfc.bean.TenureModificationDetailsBean;
import com.nbfc.dao.ApplicationStatusDao;
import com.nbfc.dao.TenureModificationDao;

@Service("TenureModificationService")
public class TenureModificationServiceImpl implements TenureModificationService {
	@Autowired
	TenureModificationDao tenureModificationDao;

	@Override
	public TenureModificationDetailsBean getTenureModificationDetails(
			String cgpan) {
		// TODO Auto-generated method stub
		return tenureModificationDao.getTenureModificationDetails(cgpan);
	}

	@Override
	public int submitTenureModificationDetails(
			TenureModificationDetailsBean tenureBean, String loginUserMemId,
			String usr_id) {
		// TODO Auto-generated method stub
		return tenureModificationDao.submitTenureModificationDetails(tenureBean,loginUserMemId,usr_id);
	}

	@Override
	public List<TenureModificationDetailsBean> getTenureModDetailsApproval(
			String loginUserMemId) {
		// TODO Auto-generated method stub
		return tenureModificationDao.getTenureModDetailsApproval(loginUserMemId);
	}

	@Override
	public TenureModificationDetailsBean updateStatusTenureApprovedReturn(
			String userId, Map<String, Object> claimStatusMapObj,
			String userRole,String CHK) {
		// TODO Auto-generated method stub
		return tenureModificationDao.updateStatusTenureApprovedReturn(userId,claimStatusMapObj,userRole,CHK);
	}

	@Override
	public TenureModificationDetailsBean getValidateCgpanForTenureMod(
			String cgpan, String userId) {
		// TODO Auto-generated method stub
		return tenureModificationDao.getValidateCgpanForTenureMod(cgpan,userId);
	}

	@Override
	public List<TenureModificationDetailsBean> getTenureReturnedRecordsByNBFCChecker(
			String loginUserMemId) {
		// TODO Auto-generated method stub
		return tenureModificationDao.getTenureReturnedRecordsByNBFCChecker(loginUserMemId);
	}

	@Override
	public int updateTenureDataDetails(TenureModificationDetailsBean bean,
			int tenId, String userId) {
		// TODO Auto-generated method stub
		return tenureModificationDao.updateTenureDataDetails(bean,tenId,userId);
	}

	@Override
	public TenureModificationDetailsBean getTenureDetailsForUpdate(Integer tenId) {
		// TODO Auto-generated method stub
		return tenureModificationDao.getTenureDetailsForUpdate(tenId);
	}





	
}
