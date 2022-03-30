package com.nbfc.dao;

import java.util.List;

import com.nbfc.bean.UpdMemberStatusBean;

public interface UpdMemberStatusDao {

	public List<UpdMemberStatusBean> getBankMandateDetailsFromCGTMSE();
	
	public String updBankMandateStausFromCGTMSE(String memberId,String loginUserId,String remarks);
}
