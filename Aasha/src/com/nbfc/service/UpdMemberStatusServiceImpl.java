package com.nbfc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nbfc.bean.UpdMemberStatusBean;
import com.nbfc.dao.UpdMemberStatusDao;


@Service("updMemStatusService")
public class UpdMemberStatusServiceImpl implements UpdMemberStatusService{

	@Autowired
	UpdMemberStatusDao updMemStatusDao;

	@Override
	public List<UpdMemberStatusBean> getBankMandateDetailsFromCGTMSE() {
		// TODO Auto-generated method stub
		return updMemStatusDao.getBankMandateDetailsFromCGTMSE();
	}
	
	@Override
	public String updBankMandateStausFromCGTMSE(String memberId,String loginUserId,String remarks) {
		// TODO Auto-generated method stub
		return updMemStatusDao.updBankMandateStausFromCGTMSE(memberId,loginUserId,remarks);
	}
	
}
