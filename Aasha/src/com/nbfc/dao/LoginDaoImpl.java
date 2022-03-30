package com.nbfc.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nbfc.bean.LoginBean;
import com.nbfc.model.Login;
import com.nbfc.model.NBFCPrivilegeMaster;
import com.nbfc.model.UserPerivilegeDetails;
import com.raistudies.domain.CustomExceptionHandler;

@Repository("loginDao")
public class LoginDaoImpl implements LoginDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	public String verifyUserLogin(LoginBean loginBean) {
		Login loginEntity=null;
		if(sessionFactory!=null){
			String hql = "from Login where USR_ID = :USR_ID and USR_PASSWORD= :USR_PASSWORD";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("USR_ID", loginBean.getUsr_id());
			query.setParameter("USR_PASSWORD", loginBean.getUsr_password());
			loginEntity = (Login) query.uniqueResult();
		}else{
			throw new CustomExceptionHandler("connection is null");
		}
		
		if(loginEntity!=null) {
			
			System.out.println("User Status::::"+loginEntity.getUSR_STATUS());
			if(loginEntity.getUSR_STATUS()!=null && loginEntity.getUSR_STATUS().equals("A")) {
				if(loginEntity.getLOGIN_STATUS().equals("N")){

					return "firstTimeLogin";

				}else{

					return "success";
				}
			}else{
				return "inactiveuser";
			} 
		}else {
			return "invalidUser";
		}
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object> gerLoginUserPrivilege(String loginUsrId,String usrFlag) {
		//String hql = "select subPortfolioNo,count(distinct subPortfolioNo),status from CGTMSEMakerForBatchApprovalGetStatus where STATUS =:NCAStatus group by subPortfolioNo,status";
		String hql = "select nup.prvId,nup.uprFlag,nup.usrId,npm.prvId,npm.prvName,npm.prvDescription,npm.prvCreatedModifiedBy from NBFCUserPerivilege nup ,NBFCPrivilegeMaster npm  where nup.prvId=npm.prvId AND nup.usrId=:loginUsrId AND nup.uprFlag=:usrFlag";

		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("loginUsrId", loginUsrId);
		query.setParameter("usrFlag", usrFlag);
		System.out.println("QUERY1==="+query);
		
		List<Object> listCategories4 = query.list();
		java.util.Iterator<Object> itr1= listCategories4.iterator();
	
		while(itr1.hasNext()){
			Object[] obj1 = (Object[]) itr1.next();
		}
		return listCategories4;
	}

	
	public Login userDetails(String usr_id) {
		return (Login) sessionFactory.getCurrentSession().get(Login.class, usr_id);
	}

	
	public UserPerivilegeDetails getUserPrivlageDtl(String uID, String uStatus) {
		return (UserPerivilegeDetails) sessionFactory.getCurrentSession().createCriteria(UserPerivilegeDetails.class, uID).add(Restrictions.eq("user_id", uID)).add(Restrictions.eq("upr_flag", uStatus)).uniqueResult();
	}


	
	public NBFCPrivilegeMaster getPrivlageMstDtl(int prv_id) {
		
		return (NBFCPrivilegeMaster) sessionFactory.getCurrentSession().get(NBFCPrivilegeMaster.class, prv_id);

	}
	
	
	public boolean changePassword(Login login) {
		try{
			
		}catch(Exception e){
			System.out.println("Exception in LoginDaoImpl on change Password :"+e);
		}
		sessionFactory.getCurrentSession().saveOrUpdate(login);
		return true;
	}
	
}
