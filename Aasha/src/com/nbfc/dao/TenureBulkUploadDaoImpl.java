package com.nbfc.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import com.nbfc.bean.NPADetailsBean;
import com.nbfc.bean.NbfcAppropriationBean;
import com.nbfc.bean.TenureBulkUploadBean;
import com.raistudies.domain.CustomExceptionHandler;

import oracle.jdbc.OracleTypes;

// added by shashi
public class TenureBulkUploadDaoImpl implements TenureBulkUploadDao{
	
	@Autowired
	SessionFactory sessionFactory;
	
	public String getMemberId(String userId) {
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

	
	public List<TenureBulkUploadBean> getNPADetailsForApproval(String useId, String status) {

		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		int result1 = 0;
		Double total = 0.0;
		ArrayList<TenureBulkUploadBean> tenureBulkUploadList = null;

		Connection conn = session.connection();
		try {
			Connection connection = null;
			CallableStatement getTenureBulkUploadBean = null;

			ResultSet resultSet = null;
			String exception = "";
			getTenureBulkUploadBean = conn.prepareCall("{?=call FUNGetNPADelForCKApproval(?,?,?,?)}");
			getTenureBulkUploadBean.registerOutParameter(1, Types.INTEGER);
			getTenureBulkUploadBean.setString(2, useId);
			getTenureBulkUploadBean.setString(3, status);
			getTenureBulkUploadBean.registerOutParameter(4, OracleTypes.CURSOR);
			getTenureBulkUploadBean.registerOutParameter(5, Types.VARCHAR);
			getTenureBulkUploadBean.execute();

			int functionReturnValue = getTenureBulkUploadBean.getInt(1);

			if (functionReturnValue == 1) {

				String error = getTenureBulkUploadBean.getString(5);
				// System.out.println("Error:" + error);

				getTenureBulkUploadBean.close();
				getTenureBulkUploadBean = null;

				conn.rollback();

				throw new CustomExceptionHandler(error);

			} else {

				resultSet = (ResultSet) getTenureBulkUploadBean.getObject(4);
				exception = getTenureBulkUploadBean.getString(5);

				String cgpanNum = "";
				String npaId = "";
				Date npaEftDt = null;
				String npaResionTurningNPA = "";
				String npaReturnremark = "";
				String loanType = "";

				int noOfRecords = 0;

				// NbfcAppropriationBean na = null;//new
				// NbfcAppropriationBean();
				while (resultSet.next()) {
					if (noOfRecords == 0) {
						tenureBulkUploadList = new ArrayList();
					}
					// npaId = resultSet.getString(1);
					cgpanNum = resultSet.getString(3);
					npaEftDt = resultSet.getDate(4);
					npaResionTurningNPA = resultSet.getString(10);
					npaReturnremark = resultSet.getString(73);
					loanType = resultSet.getString(38);

					TenureBulkUploadBean na = new TenureBulkUploadBean();
//					na.setNpaId(resultSet.getString(1));
//					na.setCGPAN(cgpanNum);
//					na.setNpaDt(dateFormat.format(npaEftDt));
//					na.setNpaReason(npaResionTurningNPA);
//					na.setLoanType(loanType);
//					na.setRemarks("");
//					tenureBulkUploadList.add(na);
//					++noOfRecords;
				}
				// na.setChcktbl(receivedPayments);
			}

			resultSet.close();
			getTenureBulkUploadBean.close();
			getTenureBulkUploadBean = null;

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

		return tenureBulkUploadList;

	}

}
