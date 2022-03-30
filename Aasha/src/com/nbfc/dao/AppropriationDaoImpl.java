package com.nbfc.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nbfc.bean.AppropriationBean;
import com.raistudies.domain.CustomExceptionHandler;

@Repository("AppropriationDao")
public class AppropriationDaoImpl implements AppropriationDao{

	@Autowired
	SessionFactory sessionFactory;

	/*Add by VinodSingh For Tenure Modification on 12-DEC-2020 */
	@Override                                          
	public List<AppropriationBean> getAppropriationDetails(String paymentStatus, Date toDate,Date fromDate, String role, String mliIdOrName) {
		System.out.println("fromDate:::"+fromDate+" ,  toDate::"+toDate+""+" ,Role:::"+role+",mliIdOrName:::"+mliIdOrName+" ,paymentStatus::"+paymentStatus);			
		AppropriationBean appropriationBean = null;		
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();		
		Double total = 0.0;
		ArrayList<AppropriationBean> receivedPayments = null;		
		Connection conn = session.connection();

		try { 			
			CallableStatement getReceivedPaymentsStmt = null;
			ResultSet resultSet = null;
			String exception = "";
			/*getReceivedPaymentsStmt = conn.prepareCall("{?=call PackNBFCGetGFListforBatAppr.FuncNBFCGetListforBatApprDt(?,?,?,?)}");
			getReceivedPaymentsStmt.registerOutParameter(1, Types.INTEGER);
			getReceivedPaymentsStmt.setDate(2, new java.sql.Date(fromDate.getTime()));
			getReceivedPaymentsStmt.setDate(3, new java.sql.Date(toDate.getTime()));			
			getReceivedPaymentsStmt.registerOutParameter(4, OracleTypes.CURSOR);
			getReceivedPaymentsStmt.registerOutParameter(5, Types.VARCHAR);*/
			
			
			
	//		getReceivedPaymentsStmt = conn.prepareCall("{?=call PackNBFCGetGFListforBatAppr.FuncNBFCGetListforBatApprDt1(?,?,?,?,?,?)}");
			getReceivedPaymentsStmt = conn.prepareCall("{?=call FuncNBFCGetListforBatApprDt1(?,?,?,?,?,?)}");
			getReceivedPaymentsStmt.registerOutParameter(1, Types.INTEGER);
			getReceivedPaymentsStmt.setDate(2, new java.sql.Date(fromDate.getTime()));
			getReceivedPaymentsStmt.setDate(3, new java.sql.Date(toDate.getTime()));
			getReceivedPaymentsStmt.setString(4, paymentStatus);
			getReceivedPaymentsStmt.setString(5, mliIdOrName);
			getReceivedPaymentsStmt.registerOutParameter(6, OracleTypes.CURSOR);
			getReceivedPaymentsStmt.registerOutParameter(7, Types.VARCHAR);
			
			getReceivedPaymentsStmt.execute();
			System.out.println("paymentStatus"+paymentStatus+" ,mliIdOrName::"+mliIdOrName);
			int functionReturnValue = getReceivedPaymentsStmt.getInt(1);
			System.out.println("functionReturnValue:"+functionReturnValue);

			if (functionReturnValue == 1) {

			//	String error = getReceivedPaymentsStmt.getString(7);
				String error = getReceivedPaymentsStmt.getString(5);
				 System.out.println("Error:" + error);

				getReceivedPaymentsStmt.close();
				getReceivedPaymentsStmt = null;
				conn.rollback();
				throw new CustomExceptionHandler(error);

			} else {
				resultSet = (ResultSet) getReceivedPaymentsStmt.getObject(6);
			//	resultSet = (ResultSet) getReceivedPaymentsStmt.getObject(4);
				exception = getReceivedPaymentsStmt.getString(7);
			//	exception = getReceivedPaymentsStmt.getString(5);
				String memberId = "";
				String memberName = "";
				String paymentId = "";				
				String instrumentNo = "";
				int payAmount = 0;
				int noOfRecords = 0;
				int allocatedAmt = 0;				
				int inwardAmount = 0;
				String status="";
				String danType="";
				Date instrumentDt ;
				while (resultSet.next()) {
					if (noOfRecords == 0) {
						receivedPayments = new ArrayList();
					}
					       					
					paymentId = resultSet.getString(1);//PAY_ID RPNumber
					memberId = resultSet.getString(2); //MEMBER_ID
					memberName = resultSet.getString(3); //MEMBER_NAME NAME OF NBFC
					instrumentNo = resultSet.getString(4); //VIRTUAL_ACCOUNT_NO
					instrumentDt = resultSet.getDate(5);  ///PAYMENT_DATE
					payAmount = resultSet.getInt(6);  //AMOUNT
					allocatedAmt = resultSet.getInt(7);	//AMOUNT				
					inwardAmount = resultSet.getInt(8);  //AMOUNT
					status       = resultSet.getString(9);  //AMOUNT
					danType      = resultSet.getString(10);
					AppropriationBean approbean = new AppropriationBean();
					approbean.setMemberId(memberId);
					approbean.setMemberName(memberName);
					approbean.setRpNumber(paymentId);
					approbean.setAllocatedAmount(allocatedAmt);
					approbean.setAmount(payAmount);
					approbean.setVirtualAccountNumber(instrumentNo);					
					approbean.setInwardAmount(inwardAmount);
					approbean.setStatus(status);
					approbean.setDan_type(danType);
					receivedPayments.add(approbean);

					++noOfRecords;
				}
			}

			resultSet.close();
			getReceivedPaymentsStmt.close();
			getReceivedPaymentsStmt = null;

			conn.commit();


		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException ignore) {
				throw new CustomExceptionHandler(ignore.getMessage());
			}

			throw new CustomExceptionHandler(e.getMessage());
		}

		finally {
			session.close();

		}
		return receivedPayments;

	}
	
	
	@Override
	public String updatePaymentStatus(AppropriationBean appropriationBean) {
		
		Session session3 = sessionFactory.openSession();
		Transaction tn = session3.beginTransaction();
		Connection con = session3.connection();
		CallableStatement callStmt = null;
		ResultSet resultset = null;
		String res = "N";
		List<String> rplist =  appropriationBean.getChcktbl(); // checkbox list bean.getchecklist();		
		List<String> datelist = appropriationBean.getInstrumentDate();   // date list	
		List<String> utrlist =  appropriationBean.getInstrumentNo();   // amou
		
		List<String> dateArrylist = new ArrayList();   // date list	
		List<String> utrArrylist =  new ArrayList();
		String getdate=null;
		String getutrNo=null;
		for (int i = 0; i < datelist.size() ; i++) {			
			getdate = datelist.get(i);
			getutrNo = utrlist.get(i);
			if(!getdate.contains(",")){
				if((null != getdate && !getdate.isEmpty())){
					dateArrylist.add(getdate);				
					utrArrylist.add(getutrNo);
				}
			}						
		}
		System.out.println("rplist.size()::"+rplist.size() +"  ,Date:::::"+dateArrylist.size()+"  , Utr::::::::::"+utrArrylist.size());
		try {
			if((null != rplist && !rplist.isEmpty())){
				con.setAutoCommit(false);				
				for (int i = 0; i < rplist.size() ; i++) { // check box list
					String payId = rplist.get(i);
					String utrNo = utrArrylist.get(i);
					String date = dateArrylist.get(i);					
					System.out.println("i:::"+i+" ,payId :"+payId+" ,utrNo::"+utrNo+" ,date:"+date);
					callStmt = (CallableStatement)con.prepareCall("{ call proc_appropriation_payupdate(?,?,?,?) } ");
					callStmt.setString(1,payId);
					callStmt.setDate(2, getSqlDateFromString(date));
					callStmt.setString(3, utrNo);
					System.out.println("date::::::::::"+date);						
					callStmt.registerOutParameter(4, Types.VARCHAR);
					callStmt.execute(); 
					System.out.println("procedure executed:::::::::::");						
					String procStatus = callStmt.getString(4);
					System.out.println("procStatus::"+procStatus);
					res = "Y";									
				}
				con.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = "N";
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			if(null != con){
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}

	private static java.sql.Date getSqlDateFromString(String strDate) throws Exception {		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = sdf.parse(strDate);
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());		
		System.out.println("sqlDate:::"+sqlDate);
		return sqlDate;
	}
}
