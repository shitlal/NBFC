package com.nbfc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nbfc.bean.LoginBean;
import com.nbfc.dao.LoginDao;
import com.nbfc.model.Login;
import com.nbfc.model.NBFCPrivilegeMaster;
import com.nbfc.model.UserPerivilegeDetails;

@Service("loginService")
@Transactional()
public class LoginServiceImpl implements LoginService{
		@Autowired
		private LoginDao loginDao;
		
		
		public String verifyUserLogin(LoginBean loginBean) {
			return loginDao.verifyUserLogin(loginBean);
		}
		
		
		public List<Object> gerLoginUserPrivilege(String loginUsrId,String usrFlag) {
			return loginDao.gerLoginUserPrivilege(loginUsrId,usrFlag);
		}

		
		public Login userDetails(String userID) {
			return loginDao.userDetails(userID);
		}

		
		public UserPerivilegeDetails getUserPrivlageDtl(String uID, String uStatus) {
			return loginDao.getUserPrivlageDtl(uID, uStatus);
		}

		
		public NBFCPrivilegeMaster getPrivlageMstDtl(int prv_id) {
			return loginDao.getPrivlageMstDtl(prv_id);
		}
		
		public boolean changePassword(Login login ) {
			// TODO Auto-generated method stub
			return loginDao.changePassword(login);
		}
}
