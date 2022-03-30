package com.nbfc.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nbfc.model.BankDetails;
import com.nbfc.model.MLIBankIdDetails;
import com.nbfc.model.MLIDetails;
import com.nbfc.model.MLIExposureIDDetails;
import com.nbfc.model.MLIExposureId;
import com.nbfc.model.MLIIdDetails;
import com.nbfc.model.MLIMainEditDetails;
import com.nbfc.model.MLIName;
import com.nbfc.model.MLIRegistration;
import com.nbfc.model.PortfolioNumberMaster;
import com.nbfc.model.UseRoleModel;
import com.nbfc.model.User;
import com.nbfc.model.UserHistory;
import com.nbfc.model.UserInfo;
import com.nbfc.model.UserInfoModel;
import com.nbfc.model.UserPerivilegeDetails;

/**
 * @author Saurav Tyagi 2017
 * 
 */
@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void addEmployee(User employee) {
		
		sessionFactory.getCurrentSession().save(employee);
		System.out.println("Data saved in NBFC_USER_INFO table===");
	}

	@SuppressWarnings("unchecked")
	public List<User> listEmployeess() {
		System.out.println("Emp List");
		
//		Criteria crit=sessionFactory.getCurrentSession().createCriteria(User.class);	
//		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		return crit.list();
//	 
//
//		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(User.class);
        c.addOrder(Order.desc("createdDate"));
		return c.list();
//		return (List<User>) sessionFactory.getCurrentSession()
//				.createCriteria(User.class).list();

	}

	public User getEmployee(String fName) {
		return (User) sessionFactory.getCurrentSession().get(User.class, fName);
	}

	public void deleteEmployee(User employee) {
		sessionFactory
				.getCurrentSession()
				.createQuery(
						"DELETE FROM Nbfc_user_info WHERE  usr_first_name= "
								+ employee.getfName()).executeUpdate();
	}

	
	public MLIName getMLIName(String mem_bnk_id) {

		return (MLIName) sessionFactory.getCurrentSession().get(MLIName.class,
				mem_bnk_id);
	}

	
	public MLIDetails getBNKId(String userID) {
		return (MLIDetails) sessionFactory.getCurrentSession().get(
				MLIDetails.class, userID);
	}

	
	public List<PortfolioNumberMaster> getPortfolioNUmberForChecker(
			String exposureId) {
		System.out.println("hello");
		// return (List<PortfolioNumberMaster>)
		// sessionFactory.getCurrentSession().createCriteria(PortfolioNumberMaster.class).add(Restrictions.eq("longName",
		// mliName)).list();
		return (List<PortfolioNumberMaster>) sessionFactory.getCurrentSession()
				.createCriteria(PortfolioNumberMaster.class)
				.add(Restrictions.eq("exposureId", exposureId)).list();

	}

	
	public List<UserPerivilegeDetails> getUserPrivlageDetails() {

		return (List<UserPerivilegeDetails>) sessionFactory.getCurrentSession()
				.createCriteria(UserPerivilegeDetails.class)
				.add(Restrictions.eq("upr_flag", "A")).list();
	}

	
	public User getUserDetails(String userID) {
		System.out.println("User " + userID);
		return (User) sessionFactory.getCurrentSession()
				.createCriteria(User.class)
				.add(Restrictions.eq("usr_id", userID)).uniqueResult();
	}

	
	public UserPerivilegeDetails getUserPrivlageDetailsForEdit(String userID) {
		// TODO Auto-generated method stub
		return (UserPerivilegeDetails) sessionFactory.getCurrentSession()
				.createCriteria(UserPerivilegeDetails.class).add(Restrictions.eq("user_id", userID)).uniqueResult();
	}

	
	public void updateUserPrivlage(UserPerivilegeDetails userPerivilegeDetails) {
		System.out.println("update..");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(
					userPerivilegeDetails);
			System.out.println("update..");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public int deleteUser(String deleteUserId) {

		// TODO Auto-generated method stub
		int result = 0;

		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		try {

			Query query = sessionFactory.getCurrentSession().createQuery(
					"delete from User where usr_id = :usr_id");
			query.setParameter("usr_id", deleteUserId);

			result = query.executeUpdate();

			tn.commit();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			session.close();
		}

		return result;
	}

	
	public int userRegisteredCount(String mem_BNK_ID) {
		Integer count;

		try {
			String hql = "SELECT COUNT(*) FROM User where mem_bnk_id =:MEM_BNK_ID ";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("MEM_BNK_ID", mem_BNK_ID);
			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			System.out.println("Exception===" + e);
		}
		return 0;
	}

	
	public MLIExposureId getExposureId(String mem_bnk_id,String fyBasedOnStartAndEndDate) throws NullPointerException {
		// TODO Auto-generated method stub
		MLIExposureId mliExposureId = (MLIExposureId) sessionFactory
				.getCurrentSession().createCriteria(MLIExposureId.class)
				.add(Restrictions.eq("PORTFOLIO_BASE_YEAR", fyBasedOnStartAndEndDate))
				.add(Restrictions.eq("MEM_BNK_ID", mem_bnk_id))
				.add(Restrictions.eq("EXPOSURE_ACTIVE", "A")).uniqueResult(); //EXPOSURE_ACTIVE add by VinodSingh on 12-May-2021
		System.out.println("  mli list " + mliExposureId);
		return mliExposureId;
	}

	
	public MLIExposureId getMemBankId(String exposureID) throws NullPointerException {
		// TODO Auto-generated method stub
		return (MLIExposureId) sessionFactory.getCurrentSession()
				.createCriteria(MLIExposureId.class)
				.add(Restrictions.eq("EXPOSURE_ID", exposureID)).uniqueResult();
	}

	
	public BankDetails getMLILongName(String mem_bank_id) throws NullPointerException {
		// TODO Auto-generated method stub
		return (BankDetails) sessionFactory.getCurrentSession()
				.createCriteria(BankDetails.class)
				.add(Restrictions.eq("MEM_BNK_ID", mem_bank_id)).uniqueResult();
	}

	
	public MLIBankIdDetails getMLIId(String mliLongName)  throws NullPointerException {
		MLIBankIdDetails mliBankIdDetails = (MLIBankIdDetails) sessionFactory
				.getCurrentSession().createCriteria(MLIBankIdDetails.class)
				.add(Restrictions.eq("long_name", mliLongName)).uniqueResult();
		System.out.println("value  " + mliBankIdDetails);
		return mliBankIdDetails;

	}

	
	public void addEmployeeHistory1(String userId, String date) {
		// TODO Auto-generated method stub
		System.out.println("----------" + userId);
		Session session = sessionFactory.openSession();

		try {

			Query query = session
					.createQuery("INSERT INTO UserHistory (usr_id,fName,mName,lName,state,mobileNumber,phoneNumber,email,createdBy,userType,password,mem_bnk_id,mem_zne_id,mem_brn_id,hint_question,hint_ans,phone_code,LOGIN_STATUS,createdDate)"
							+ "SELECT usr_id,fName,mName,lName,state,mobileNumber,phoneNumber,email,createdBy,userType,password,mem_bnk_id,mem_zne_id,mem_brn_id,hint_question,hint_ans,phone_code,LOGIN_STATUS,createdDate FROM User WHERE usr_id = :userId ");
			query.setParameter("userId", userId);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

	
	public void UpdateUprflag(String userId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
	String hql = "UPDATE UserPerivilegeDetails set upr_flag = :upr_flag "  + 
	             "WHERE user_id = :user_id";
	Query query = session.createQuery(hql);
	query.setParameter("upr_flag", "D");
	query.setParameter("user_id",userId);
	int result = query.executeUpdate();
	System.out.println("Rows affected: " + result);
		
		
		System.out.println("update..");
//		try {
//			sessionFactory.getCurrentSession().saveOrUpdate(UserPerivilegeDetails.class);
//			System.out.println("update..");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

	
	public int userEmailIDCount(String email) {
		Integer count;

		try {
			String hql = "SELECT COUNT(*) FROM User where email =:Email_ID ";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("Email_ID", email);
			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			System.out.println("Exception===" + e);
		}
		return 0;
	}

	public void updateUserInfo(UserInfoModel userInfo) {
		// TODO Auto-generated method stub
		String mobno=userInfo.getMobileNumber();
		String phoneno=userInfo.getPhoneNumber();
		String email=userInfo.getEmail();
		String usertype=userInfo.getUserType();
		String userid=userInfo.getUsr_id();
		
		Session session = sessionFactory.openSession();
		String hql = "UPDATE UserInfoModel set mobileNumber =:mobileNumber1,phoneNumber=:phoneNumber1,email=:email1,userType=:userType1 WHERE usr_id = :user_id1"; 
		          
		Query query = session.createQuery(hql);
		query.setParameter("mobileNumber1",mobno);
		query.setParameter("phoneNumber1",phoneno);
		query.setParameter("email1",email);
		query.setParameter("userType1",usertype);
		query.setParameter("user_id1",userid);
		System.out.println("Query--------: " + query);
		int result = query.executeUpdate();
		System.out.println("Rows affected: " + result);
		
		
		
		
//		try {
//			
//			System.out.println("update.userid-------------."+userInfo.getUsr_id());
//			sessionFactory.getCurrentSession().saveOrUpdate(userInfo);
//			System.out.println("update..");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public MLIIdDetails getMLIID(String BankName) {
		// TODO Auto-generated method stub
		MLIIdDetails mliIdDetails = new MLIIdDetails();
		mliIdDetails = (MLIIdDetails) sessionFactory.getCurrentSession()
				.createCriteria(MLIIdDetails.class)
				.add(Restrictions.eq("MEM_BANK_NAME", BankName)).uniqueResult();
		System.out.println(mliIdDetails);
		return mliIdDetails;
	}

	@Override
	public UserInfo getUserInfo(String userId) {
		UserInfo userInfo = (UserInfo) sessionFactory.getCurrentSession()
				.createCriteria(UserInfo.class)
				.add(Restrictions.eq("USR_ID", userId)).uniqueResult();
		return userInfo;
	}
}
