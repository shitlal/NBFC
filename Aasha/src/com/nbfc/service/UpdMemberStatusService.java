package com.nbfc.service;

import java.util.List;

import com.nbfc.bean.UpdMemberStatusBean;

public interface UpdMemberStatusService {
	
	public List<UpdMemberStatusBean> getBankMandateDetailsFromCGTMSE();
	
	public String updBankMandateStausFromCGTMSE(String memberId,String loginUserId,String remarks);

}
