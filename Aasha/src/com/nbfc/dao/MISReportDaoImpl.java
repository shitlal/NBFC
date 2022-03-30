package com.nbfc.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibm.icu.math.BigDecimal;
import com.nbfc.bean.MISReportbanforClaim;
import com.nbfc.bean.MisReportbean;
import com.nbfc.bean.NbfcAppropriationBean;
import com.nbfc.bean.OtherMisRepor;
import com.nbfc.bean.PortfolioDetailsBean;
import com.raistudies.domain.CustomExceptionHandler;

import oracle.jdbc.OracleTypes;

@Repository("MisReportdao")
public class MISReportDaoImpl implements MisReportdao {

	@Autowired
	SessionFactory sessionFactory;

	public String getMemberId(String userId) {

		// TODO Auto-generated method stub

		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		String memberId = null;
		int result1 = 0;
		Double total = 0.0;
		ArrayList<NbfcAppropriationBean> receivedPayments = null;

		Connection conn = session.connection();
		if (userId != null && userId != "ADMIN") {
			try {

				Connection connection = null;
				CallableStatement getReceivedPaymentsStmt = null;

				ResultSet resultSet = null;
				String exception = "";
				getReceivedPaymentsStmt = conn.prepareCall("{?=call FuncNBFCGetMemberId(?,?,?)}");
				getReceivedPaymentsStmt.registerOutParameter(1, Types.INTEGER);

				getReceivedPaymentsStmt.setString(2, userId);
				getReceivedPaymentsStmt.registerOutParameter(3, OracleTypes.VARCHAR);
				getReceivedPaymentsStmt.registerOutParameter(4, Types.VARCHAR);
				getReceivedPaymentsStmt.execute();

				int functionReturnValue = getReceivedPaymentsStmt.getInt(1);
				// System.out.println("functionReturnValue:"+functionReturnValue);

				if (functionReturnValue == 1) {

					String error = getReceivedPaymentsStmt.getString(4);
					// System.out.println("Error:" + error);

					getReceivedPaymentsStmt.close();
					getReceivedPaymentsStmt = null;

					conn.rollback();

					throw new CustomExceptionHandler(error);

				} else {

					memberId = getReceivedPaymentsStmt.getString(3);
					System.out.println("memberId :" + memberId);
					exception = getReceivedPaymentsStmt.getString(4);

				}

				getReceivedPaymentsStmt.close();
				getReceivedPaymentsStmt = null;

				conn.commit();

			} catch (Exception exception) {

				try {
					conn.rollback();
				} catch (SQLException ignore) {
					throw new CustomExceptionHandler(ignore.getMessage());
				}

				throw new CustomExceptionHandler(exception.getMessage());
			} finally {
				session.close();

			}

			// return receivedPayments;
		}
		return memberId;
	}

	public ArrayList<MisReportbean> getReportdataforMis(String userId, Date fromDate, Date toDate) {
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		ResultSetMetaData resultSetMetaData = null;
		ResultSet resultSet1 = null;
		String exception1 = "";
		//List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		ArrayList<MisReportbean> list = new ArrayList<MisReportbean>();
		MisReportbean misReportbean = null;
		Connection conn = session.connection();
		try {
			CallableStatement getReceivedPaymentsStmt = null;
			getReceivedPaymentsStmt = conn.prepareCall("{call ReportForMisMonthly(?,?,?,?)}");
			//getReceivedPaymentsStmt.registerOutParameter(1, Types.INTEGER);
			getReceivedPaymentsStmt.setDate(1, new java.sql.Date(toDate.getTime()));
			getReceivedPaymentsStmt.setDate(2, new java.sql.Date(fromDate.getTime()));
			getReceivedPaymentsStmt.registerOutParameter(3, OracleTypes.CURSOR);
			getReceivedPaymentsStmt.registerOutParameter(4, Types.VARCHAR);  
			getReceivedPaymentsStmt.execute();
			//int functionReturnValue = getReceivedPaymentsStmt.getInt(1);
			// System.out.println("functionReturnValue:"+functionReturnValue);
			int functionReturnValue = 0;
			if (functionReturnValue == 1) {
				String error = getReceivedPaymentsStmt.getString(5);
				// System.out.println("Error:" + error);
				getReceivedPaymentsStmt.close();
				getReceivedPaymentsStmt = null;
				conn.rollback();
				throw new CustomExceptionHandler(error);
			} else {
				resultSet1 = (ResultSet) getReceivedPaymentsStmt.getObject(3);
				exception1 = getReceivedPaymentsStmt.getString(4);	
					while (resultSet1.next()) {
					misReportbean = new MisReportbean();
					
					
					misReportbean.setCgpan(resultSet1.getString(1));
					misReportbean.setMEM_BANK_NAME(resultSet1.getString(2));
					misReportbean.setGUARANTEE_AMOUNT(resultSet1.getString(3));
					misReportbean.setRANGE_DATA(resultSet1.getString(4));
					misReportbean.setWomen(resultSet1.getString(5));
					misReportbean.setState(resultSet1.getString(6));
					misReportbean.setStatus(resultSet1.getString(7));
					misReportbean.setDistrict(resultSet1.getString(8));
					misReportbean.setCity(resultSet1.getString(9));
					misReportbean.setCategory(resultSet1.getString(10));
					misReportbean.setPartialSecurityFlag(resultSet1.getString(11));
					misReportbean.setMicroAndSmall(resultSet1.getString(12));
					misReportbean.setINDUSTRY_SECTOR(resultSet1.getString(13));
					misReportbean.setINDUSTRY_NATURE(resultSet1.getString(14));
					misReportbean.setMSE_ADDRESS(resultSet1.getString(15));
					misReportbean.setNpaCreatedModDate(resultSet1.getString(16));
					misReportbean.setNpaOutstadAmt(resultSet1.getString(17));
					misReportbean.setMINORITY_COMMUNITY(resultSet1.getString(18));
					misReportbean.setRetail_trade(resultSet1.getString(19));
					misReportbean.setCgtmsc_checker_date(resultSet1.getString(20));
					list.add(misReportbean);
				}
			}
			getReceivedPaymentsStmt.close();
			// getReceivedPaymentsStmt = null;

			conn.commit();

		} catch (Exception exception) {

			try {
				conn.rollback();
			} catch (SQLException ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}

			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session.close();

		}

		// return receivedPayments;

		return list;
	}


	public ArrayList<MISReportbanforClaim> getReportdataforClaimMis(String userId, Date fromDate, Date toDate) {
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		ResultSetMetaData resultSetMetaData = null;
		ResultSet resultSet1 = null;
		String exception1 = "";
		//List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		ArrayList<MISReportbanforClaim> list = new ArrayList<MISReportbanforClaim>();
		MISReportbanforClaim  misReportbean = null;
		Connection conn = session.connection();
		try {
			CallableStatement getReceivedPaymentsStmt = null;
			getReceivedPaymentsStmt = conn.prepareCall("{call ClaimReportForMIS(?,?,?,?)}");
			getReceivedPaymentsStmt.setDate(1, new java.sql.Date(toDate.getTime()));
			getReceivedPaymentsStmt.setDate(2, new java.sql.Date(fromDate.getTime()));
			getReceivedPaymentsStmt.registerOutParameter(3, OracleTypes.CURSOR);
			getReceivedPaymentsStmt.registerOutParameter(4, Types.VARCHAR);  
			getReceivedPaymentsStmt.execute();
			int functionReturnValue = 0;
			if (functionReturnValue == 1) {
				String error = getReceivedPaymentsStmt.getString(5);
				// System.out.println("Error:" + error);
				getReceivedPaymentsStmt.close();
				getReceivedPaymentsStmt = null;
				conn.rollback();
				throw new CustomExceptionHandler(error);
			} else {
				resultSet1 = (ResultSet) getReceivedPaymentsStmt.getObject(3);
				exception1 = getReceivedPaymentsStmt.getString(4);		
				while (resultSet1.next()) {
					misReportbean = new MISReportbanforClaim ();
					misReportbean.setMEM_BANK_NAME(resultSet1.getString(1));
					misReportbean.setState(resultSet1.getString(2));
					misReportbean.setWomen(resultSet1.getString(3));
					misReportbean.setCategory(resultSet1.getString(4));
					misReportbean.setIndustrySector(resultSet1.getString(5));
					misReportbean.setIndustryNature(resultSet1.getString(6));
					misReportbean.setFirstInstallMentClaim(resultSet1.getString(7));
					misReportbean.setFirstClaimSettle(resultSet1.getString(8));
					misReportbean.setCgpan(resultSet1.getString(9));
					misReportbean.setRangeOutstandingAmount(resultSet1.getString(10));	
					double Dan_Amount = Math.round(resultSet1.getDouble(11));
					BigDecimal dan_amt = new BigDecimal(Dan_Amount);
					String sumOutstnadamount=""+dan_amt;
				    misReportbean.setSumOutstandingAmout(sumOutstnadamount);
					//misReportbean.setSumOutstandingAmout(resultSet1.getString(11));
					misReportbean.setCount1(resultSet1.getString(12));
					misReportbean.setRangeGuaranteeAmount(resultSet1.getString(13));
					misReportbean.setSumGuaranteeAmount(resultSet1.getString(14));
					misReportbean.setCount2(resultSet1.getString(15));
					list.add(misReportbean);
				}
			}
			getReceivedPaymentsStmt.close();
			conn.commit();

		} catch (Exception exception) {

			try {
				conn.rollback();
			} catch (SQLException ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}

			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session.close();

		}

		// return receivedPayments;

		return list;
	}

	

	public ArrayList<OtherMisRepor> getOverAllReportdataforClaimMis(String userId, Date fromDate, Date toDate) {
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		ResultSetMetaData resultSetMetaData = null;
		ResultSet resultSet1 = null;
		String exception1 = "";
		//List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		ArrayList<OtherMisRepor> list = new ArrayList<OtherMisRepor>();
		OtherMisRepor  misReportbean = null;
		Connection conn = session.connection();
		try {
			CallableStatement getReceivedPaymentsStmt = null;
			getReceivedPaymentsStmt = conn.prepareCall("{call ReportMonthlyMISOtherReport(?,?,?,?)}");
			getReceivedPaymentsStmt.setDate(1, new java.sql.Date(toDate.getTime()));
			getReceivedPaymentsStmt.setDate(2, new java.sql.Date(fromDate.getTime()));
			getReceivedPaymentsStmt.registerOutParameter(3, OracleTypes.CURSOR);
			getReceivedPaymentsStmt.registerOutParameter(4, Types.VARCHAR);  
			getReceivedPaymentsStmt.execute();
			int functionReturnValue = 0;
			if (functionReturnValue == 1) {
				String error = getReceivedPaymentsStmt.getString(5);
				// System.out.println("Error:" + error);
				getReceivedPaymentsStmt.close();
				getReceivedPaymentsStmt = null;
				conn.rollback();
				throw new CustomExceptionHandler(error);
			} else {
				resultSet1 = (ResultSet) getReceivedPaymentsStmt.getObject(3);
				exception1 = getReceivedPaymentsStmt.getString(4);		
				while (resultSet1.next()) {
					misReportbean = new OtherMisRepor ();
						misReportbean.setMSE_BANK_NAME(resultSet1.getString(1));
						misReportbean.setStatus(resultSet1.getString(2));
						misReportbean.setCgpan(resultSet1.getString(3));
						misReportbean.setState(resultSet1.getString(4));
						misReportbean.setDistrict(resultSet1.getString(5));
						misReportbean.setCity(resultSet1.getString(6));
						misReportbean.setWomen(resultSet1.getString(7));
						misReportbean.setCategory(resultSet1.getString(8));
						misReportbean.setIndustry_nature(resultSet1.getString(10));
						misReportbean.setIndustrySector(resultSet1.getString(11));
						misReportbean.setMse_address(resultSet1.getString(12));
						misReportbean.setRange_data(resultSet1.getString(13));
						misReportbean.setSumGuaranteeAmount(resultSet1.getString(14));
						misReportbean.setCount(resultSet1.getString(15));
					list.add(misReportbean);
				}
			}
			getReceivedPaymentsStmt.close();
			conn.commit();

		} catch (Exception exception) {

			try {
				conn.rollback();
			} catch (SQLException ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}

			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session.close();

		}

		// return receivedPayments;

		return list;
	}


	
	

}
