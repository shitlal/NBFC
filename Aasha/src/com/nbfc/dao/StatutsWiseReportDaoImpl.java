package com.nbfc.dao;

import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nbfc.bean.ApplicationStatusDetailsBean;
import com.nbfc.bean.MliWiseReportDetailBean;
import com.nbfc.bean.NPADetailsBean;
import com.nbfc.helper.DateFormate;
import com.nbfc.model.CGTMSEExposureMasterMLIName;
import com.raistudies.domain.CustomExceptionHandler;

import oracle.jdbc.OracleTypes;

@Repository("StatutsWiseReportDao")
public class StatutsWiseReportDaoImpl implements StatutsWiseReportDao {
	@Autowired
	SessionFactory sessionFactory;
	ResultSet resultset = null;
	ResultSet resultset1 = null;
	ResultSetMetaData resultSetMetaData = null;

	public ArrayList<Object[]> StatutsWiseReport(String userId, String toDate, String fromDate, String status) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		try {
			session = sessionFactory.openSession();
			// String qStr="select
			// h1.NAME_BRANCH_VERTICAL,h.APP_STATUS,h.FINANCIAL_YEAR,h.QUARTER from
			// HV_MODULE_FWD_STATUS h,HV_PARTA_FORM1 h1 where h.APPCODE=h1.APPCODE AND
			// h.EMP_ZONE_CD in("+EMP_ZONE_CD+") AND h.EMP_BR_CD IN("+BranchCode+") AND
			// h.FINANCIAL_YEAR in("+FINANCIAL_YEAR+") AND QUARTER in("+QUARTER+") and
			// APP_STATUS in("+APP_STATUS+") order by NAME_BRANCH_VERTICAL";
			String qStr = "SELECT D.STATE,D.MEM_BNK_ID|| D.MEM_ZNE_ID|| D.MEM_BRN_ID,D.DISTRICT,D.STATUS,D.GUARANTEE_AMOUNT,D.MSE_NAME,D.CGPAN, D.NBFC_UPLOADED_DATE,ADD_MONTHS(TRUNC(D.FIRST_DISBURSEMENT_DATE),TENOR_IN_MONTHS) EXPAIRY_DATE FROM NBFC_INTERFACE_UPLOAD D WHERE STATUS = "
					+ status + " AND USER_ID=" + userId + " AND TRUNC (NBFC_UPLOADED_DATE) BETWEEN p_fromdate="
					+ fromDate + " AND p_todat=" + toDate + "";
			result = (ArrayList<Object[]>) session.createSQLQuery(qStr).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;

	}

	public List<Map<String, Object>> getStatutsDetails(String userId, String role, Date toDate, Date fromDate,
			String state) {
		// TODO Auto-generated method stub

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Session session6 = sessionFactory.openSession();
		Transaction tn = session6.beginTransaction();

		Connection conn = session6.connection();
		String statuts = "";
		if (state.equals("A")) {
			statuts = "CCA";
		}
		try {
			CallableStatement cs = conn.prepareCall("{? = call MIS_REPORT.MIS_STATUSWISE_REPORT(?,?,?,?,?,?,?)}");
			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, role);
			cs.setString(3, userId);
			cs.setDate(4, new java.sql.Date(toDate.getTime()));
			System.out.println(new java.sql.Date(fromDate.getTime()));
			cs.setDate(5, new java.sql.Date(fromDate.getTime()));
			cs.setString(6, statuts);
			cs.registerOutParameter(7, OracleTypes.CURSOR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(8);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :" + pouterror);
			} else {
				// Procedure execution
				resultset = (ResultSet) cs.getObject(7);
				resultSetMetaData = resultset.getMetaData();
				int coulmnCount = resultSetMetaData.getColumnCount();
				while (resultset.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
					for (int i = 1; i <= coulmnCount; i++) {
						columns.put(resultSetMetaData.getColumnLabel(i), resultset.getObject(i));
					}
					rows.add(columns);
				}
			}

		} catch (Exception exception) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}
			exception.printStackTrace();
			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session6.close();
		}
		return rows;
	}

	@Override
	public List<Map<String, Object>> getMliWeise(String role, String userid, Date fromDate, Date toDate, String MliId,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Session session5 = sessionFactory.openSession();
		Transaction tn = session5.beginTransaction();
		Connection conn = session5.connection();

		try {
			CallableStatement cs = conn.prepareCall("{? = call MIS_REPORT.MIS_MLIWISE_REPORT(?,?,?,?,?,?,?)}");
			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, role);
			cs.setString(3, userid);
			cs.setDate(4, new java.sql.Date(fromDate.getTime()));
			cs.setDate(5, new java.sql.Date(toDate.getTime()));
			cs.setString(6, MliId);
			cs.registerOutParameter(7, OracleTypes.CURSOR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(8);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :" + pouterror);
			} else {
				// Procedure execution
				resultset1 = (ResultSet) cs.getObject(7);
				resultSetMetaData = resultset1.getMetaData();
				int coulmnCount = resultSetMetaData.getColumnCount();
				BigDecimal gurTotCount;
				BigDecimal sumGurTotCount = new BigDecimal(0);
				BigDecimal gurTotAmt;
				BigDecimal sumGurTotAmt = new BigDecimal(0);
				while (resultset1.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
					for (int i = 1; i <= coulmnCount; i++) {
						// System.out.println("column Name==="+resultSetMetaData.getColumnLabel(i));
						// System.out.println("Value==="+resultset1.getObject(i));
						if (resultSetMetaData.getColumnLabel(i).equals("GUARANTEED COUNT")) {
							gurTotCount = (BigDecimal) resultset1.getObject(i);
							sumGurTotCount = sumGurTotCount.add(gurTotCount);
						}
						if (resultSetMetaData.getColumnLabel(i).equals("GUARANTEED AMOUNT")) {
							gurTotAmt = (BigDecimal) resultset1.getObject(i);
							sumGurTotAmt = sumGurTotAmt.add(gurTotAmt);
						}
						columns.put(resultSetMetaData.getColumnLabel(i), resultset1.getObject(i));
					}

					rows.add(columns);
				}
				request.setAttribute("sumGurTotCountKey", sumGurTotCount);
				request.setAttribute("sumGurTotAmtKey", sumGurTotAmt);

				// System.out.println("sumGurTotCount==="+sumGurTotCount);
				// System.out.println("sumGurTotAmt==="+sumGurTotAmt);
			}

		} catch (Exception exception) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}
			exception.printStackTrace();
			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session5.close();
		}
		return rows;
	}

	public List<Object[]> getMliWiseDate(String role, String userid, String fromDate, String toDate, String MliId) {
		Session session = null;
		List<Object[]> result = new ArrayList<Object[]>();
		try {
			session = sessionFactory.openSession();
			String queary = "SELECT MAX (v.MEM_BANK_NAME) AS mli_name,MAX (v.MEM_BNK_ID || v.MEM_ZNE_ID || v.MEM_BRN_ID) AS MLIID,  "
					+ "COUNT (*) AS CNT,  ROUND (SUM (d.GUARANTEE_AMOUNT), 2) AS AMT FROM NBFC_MEMBER_INFO v,"
					+ " nbfc_interface_upload d WHERE     v.MEM_BNK_ID || v.MEM_ZNE_ID || v.MEM_BRN_ID =  d.MEM_BNK_ID || d.MEM_ZNE_ID || d.MEM_BRN_ID  AND TRUNC (NBFC_UPLOADED_DATE)"
					+ " BETWEEN '" + fromDate + "'  AND '" + toDate + "'  GROUP BY v.MEM_BANK_NAME";
			// String qStr="select
			// h1.NAME_BRANCH_VERTICAL,h.APP_STATUS,h.FINANCIAL_YEAR,h.QUARTER from
			// HV_MODULE_FWD_STATUS h,HV_PARTA_FORM1 h1 where h.APPCODE=h1.APPCODE AND
			// h.EMP_ZONE_CD in("+EMP_ZONE_CD+") AND h.EMP_BR_CD IN("+BranchCode+") AND
			// h.FINANCIAL_YEAR in("+FINANCIAL_YEAR+") AND QUARTER in("+QUARTER+") and
			// APP_STATUS in("+APP_STATUS+") order by NAME_BRANCH_VERTICAL";
			result = session.createSQLQuery(queary).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	public List<Object[]> getStatutsWiseData(String MemberId, String role, String fromDate, String toDate,
			String statuts) {
		System.out.println("statuts===" + statuts);
		List<Object[]> result = new ArrayList<Object[]>();
		List<Object[]> result1 = new ArrayList<Object[]>();
		String apStatuts = "";
		Session session5 = sessionFactory.openSession();
		if (statuts.equals("A")) {
			apStatuts = "'CCA','ACL','NCL','EX','CS1'";
		} else if (statuts.equals("R")) {
			apStatuts = "'CMR','CCR','RE'";
		}

		else if (statuts.equals("E")) {
			apStatuts = "'EX'";
		} else if (statuts.equals("C")) {
			apStatuts = "'ACL','NCL'";
		} else if (statuts.equals("N")) {
			apStatuts = "'NCA','CMA'";
		}
		try {
			if (role.equals("CCHECKER") || role.equals("CMAKER")) {
				String queary1 = "SELECT  D.CGPAN,D.STATE, (D.MEM_BNK_ID || D.MEM_BRN_ID  || D.MEM_ZNE_ID) AS MLIID, "
						+ "D.DISTRICT, D.STATUS, D.LOAN_ACCOUNT_NO,ROUND(D.GUARANTEE_AMOUNT), D.MSE_NAME, TRUNC (D.NBFC_UPLOADED_DATE), "
						+ "TRUNC (B.DCI_GUARANTEE_START_DT),ADD_MONTHS (TRUNC (D.FIRST_DISBURSEMENT_DATE), "
						+ "TENOR_IN_MONTHS) EXPAIRY_DATE FROM NBFC_INTERFACE_UPLOAD D,"
						+ "NBFC_DAN_CGPAN_INFO B WHERE D.DAN_ID=B.DAN_ID AND D.STATUS in(" + apStatuts
						+ ") AND TRUNC (D.NBFC_UPLOADED_DATE) BETWEEN '" + fromDate + "' AND '" + toDate + "'";
				System.out.println("STATUS WISE QUERY FOR ALL MLI===" + queary1);
				result = session5.createSQLQuery(queary1).list();
			} else {
				String queary1 = "SELECT  D.CGPAN,D.STATE, (D.MEM_BNK_ID || D.MEM_BRN_ID  || D.MEM_ZNE_ID) AS MLIID, "
						+ "D.DISTRICT, D.STATUS, D.LOAN_ACCOUNT_NO,ROUND(D.GUARANTEE_AMOUNT), D.MSE_NAME, TRUNC (D.NBFC_UPLOADED_DATE), "
						+ "TRUNC (B.DCI_GUARANTEE_START_DT),ADD_MONTHS (TRUNC (D.FIRST_DISBURSEMENT_DATE), "
						+ "TENOR_IN_MONTHS) EXPAIRY_DATE FROM NBFC_INTERFACE_UPLOAD D,"
						+ "NBFC_DAN_CGPAN_INFO B WHERE D.DAN_ID=B.DAN_ID AND D.STATUS in(" + apStatuts
						+ ") AND TRUNC (D.NBFC_UPLOADED_DATE) BETWEEN '" + fromDate + "' AND '" + toDate + "'"
						+ "AND D.MEM_BNK_ID|| D.MEM_ZNE_ID|| D.MEM_BRN_ID ='" + MemberId + "'";
				System.out.println("STATUS WISE QUERY FOR SPECIFIC MLI===" + queary1);
				result = session5.createSQLQuery(queary1).list();

			}
			for (Object[] obj : result) {
				Object[] obj1 = new Object[11];
				DateFormate dt = new DateFormate();
				String obj0 = (String) obj[0];
				// System.out.println("obj0==="+obj0);
				if (obj0 == null) {
					obj1[0] = "";
				} else {
					obj1[0] = obj[0];
				}
				obj1[1] = obj[1];
				obj1[2] = obj[2];
				obj1[3] = obj[3];
				obj1[4] = obj[4];
				obj1[5] = obj[5];
				obj1[6] = obj[6];
				obj1[7] = obj[7];
				String date1 = dt.datechangeformate(obj[8].toString());
				obj1[8] = date1;
				String date2 = dt.datechangeformate(obj[9].toString());
				obj1[9] = date2;
				String date3 = dt.datechangeformate(obj[10].toString());
				obj1[10] = date3;
				result1.add(obj1);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session5.close();
		}
		return result1;
	}

	@Override
	public List<MliWiseReportDetailBean> getMliWeiseReport(String role, String userid, Date fromDate, Date toDate,
			String MliId) {
		ResultSet resultset = null;
		ResultSetMetaData resultSetMetaData = null;
		MliWiseReportDetailBean MliWiseDetailBean = null;
		List<MliWiseReportDetailBean> MliWiseReportDetailList = new ArrayList<MliWiseReportDetailBean>();
		Session session5 = sessionFactory.openSession();
		Transaction tn = session5.beginTransaction();
		Connection conn = session5.connection();
		try {
			CallableStatement cs = conn.prepareCall("{? = call MIS_REPORT.MIS_MLIWISE_REPORT(?,?,?,?,?,?,?)}");
			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, role);
			cs.setString(3, userid);
			cs.setDate(4, new java.sql.Date(fromDate.getTime()));
			cs.setDate(5, new java.sql.Date(toDate.getTime()));
			cs.setString(6, MliId);
			cs.registerOutParameter(7, OracleTypes.CURSOR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(8);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :" + pouterror);
			} else {
				// Procedure execution
				resultset = (ResultSet) cs.getObject(7);
				resultSetMetaData = resultset.getMetaData();
				int coulmnCount = resultSetMetaData.getColumnCount();
				while (resultset.next()) {
					MliWiseDetailBean = new MliWiseReportDetailBean();
					MliWiseDetailBean.setSrNo(resultset.getString("SR.NO."));
					MliWiseDetailBean.setMliName(resultset.getString("NAME OF THE NBFC"));
					MliWiseDetailBean.setMliId(resultset.getString("MLI ID"));
					MliWiseDetailBean.setMliCount(resultset.getString("GUARANTEED COUNT"));
					MliWiseDetailBean.setTolalApprovedAmt(resultset.getString("GUARANTEED AMOUNT"));
					MliWiseReportDetailList.add(MliWiseDetailBean);
				}
			}
		} catch (Exception exception) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}
			exception.printStackTrace();
			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session5.close();
		}
		return MliWiseReportDetailList;

	}

	@Override
	public List<Map<String, Object>> getAsfReportDetail(String role, String userid, Date fromDate, Date toDate,
			String MliId) {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Session session5 = sessionFactory.openSession();
		Transaction tn = session5.beginTransaction();
		Connection conn = session5.connection();

		try {
			CallableStatement cs = conn.prepareCall("{? = call MIS_REPORT.ASF_SUMMERY_REPORT(?,?,?,?,?,?,?)}");
			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, role);
			cs.setString(3, userid);
			cs.setDate(4, new java.sql.Date(fromDate.getTime()));
			cs.setDate(5, new java.sql.Date(toDate.getTime()));
			cs.setString(6, MliId);
			cs.registerOutParameter(7, OracleTypes.CURSOR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(8);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :" + pouterror);
			} else {
				// Procedure execution
				resultset1 = (ResultSet) cs.getObject(7);
				resultSetMetaData = resultset1.getMetaData();
				int coulmnCount = resultSetMetaData.getColumnCount();
				while (resultset1.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
					for (int i = 1; i <= coulmnCount; i++) {
						System.out.println("c====" + resultSetMetaData.getColumnLabel(i));
						System.out.println("v====" + resultset1.getObject(i));
						columns.put(resultSetMetaData.getColumnLabel(i), resultset1.getObject(i));

					}
					rows.add(columns);
				}
			}

		} catch (Exception exception) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}
			exception.printStackTrace();
			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session5.close();
		}
		return rows;
	}

	@Override
	public List<MliWiseReportDetailBean> getASFfReportDetail(String role, String userid, Date fromDate, Date toDate,
			String MliId) {
		ResultSet resultset2 = null;
		ResultSetMetaData resultSetMetaData = null;
		MliWiseReportDetailBean MliWiseDetailBean1 = null;
		List<MliWiseReportDetailBean> MliWiseReportDetailList1 = new ArrayList<MliWiseReportDetailBean>();
		Session session5 = sessionFactory.openSession();
		Transaction tn = session5.beginTransaction();
		Connection conn = session5.connection();
		try {
			CallableStatement cs = conn.prepareCall("{? = call MIS_REPORT.ASF_SUMMERY_REPORT(?,?,?,?,?,?,?)}");
			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, role);
			cs.setString(3, userid);
			cs.setDate(4, new java.sql.Date(fromDate.getTime()));
			cs.setDate(5, new java.sql.Date(toDate.getTime()));
			cs.setString(6, MliId);
			cs.registerOutParameter(7, OracleTypes.CURSOR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(8);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :" + pouterror);
			} else {
				// Procedure execution
				resultset2 = (ResultSet) cs.getObject(7);
				// resultSetMetaData = resultset2.getMetaData();
				// int coulmnCount = resultSetMetaData.getColumnCount();
				while (resultset2.next()) {
					MliWiseDetailBean1 = new MliWiseReportDetailBean();
					MliWiseDetailBean1.setSrNo(resultset2.getString("SR.NO."));
					MliWiseDetailBean1.setNameoftheNBFC(resultset2.getString("NAME OF THE NBFC"));
					MliWiseDetailBean1.setMliId(resultset2.getString("MLI ID"));
					MliWiseDetailBean1.setDANID(resultset2.getString("DAN ID"));
					MliWiseDetailBean1.setNOOFASFDUE(resultset2.getString("NO.OF ASF DUE"));
					MliWiseDetailBean1.setASFAMNTDUEINRS(resultset2.getString("ASF AMOUNT DUE IN RS."));
					MliWiseDetailBean1.setASFAppropriationflag(resultset2.getString("ASFAppropriationflag"));
					MliWiseReportDetailList1.add(MliWiseDetailBean1);
				}

			}
		} catch (Exception exception) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}
			exception.printStackTrace();
			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session5.close();
		}
		return MliWiseReportDetailList1;

	}

	@Override
	public List<Map<String, Object>> getDanASFDetail(String role, String userid, Date fromDate, Date toDate,
			String MliId, String ssName) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Session session5 = sessionFactory.openSession();
		Transaction tn = session5.beginTransaction();
		Connection conn = session5.connection();

		try {
			CallableStatement cs = conn.prepareCall("{? = call MIS_REPORT.DAN_GF_REPORT(?,?,?,?,?,?,?,?)}");
			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, role);
			cs.setString(3, userid);
			cs.setDate(4, new java.sql.Date(toDate.getTime()));
			cs.setDate(5, new java.sql.Date(fromDate.getTime()));
			cs.setString(6, MliId);
			cs.setString(7, ssName);
			cs.registerOutParameter(8, OracleTypes.CURSOR);
			cs.registerOutParameter(9, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(9);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :" + pouterror);
			} else {
				// Procedure execution
				resultset1 = (ResultSet) cs.getObject(8);
				resultSetMetaData = resultset1.getMetaData();
				int coulmnCount = resultSetMetaData.getColumnCount();
				while (resultset1.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
					for (int i = 1; i <= coulmnCount; i++) {
						columns.put(resultSetMetaData.getColumnLabel(i), resultset1.getObject(i));
					}
					rows.add(columns);
				}
			}

		} catch (Exception exception) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}
			exception.printStackTrace();
			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session5.close();
		}
		return rows;
	}

	public List<MliWiseReportDetailBean> getDanGfReportDowanload(String role, String userid, Date fromDate, Date toDate,
			String MliId, String ssName) {
		ResultSet resultset2 = null;
		ResultSetMetaData resultSetMetaData = null;
		MliWiseReportDetailBean MliWiseDetailBean1 = null;
		List<MliWiseReportDetailBean> MliWiseReportDetailList1 = new ArrayList<MliWiseReportDetailBean>();
		Session session5 = sessionFactory.openSession();
		Transaction tn = session5.beginTransaction();
		Connection conn = session5.connection();
		try {
			CallableStatement cs = conn.prepareCall("{? = call MIS_REPORT.DAN_GF_REPORT(?,?,?,?,?,?,?,?)}");
			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, role);
			cs.setString(3, userid);
			cs.setDate(4, new java.sql.Date(toDate.getTime()));
			cs.setDate(5, new java.sql.Date(fromDate.getTime()));
			cs.setString(6, MliId);
			cs.setString(7, ssName);
			cs.registerOutParameter(8, OracleTypes.CURSOR);
			cs.registerOutParameter(9, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(9);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :" + pouterror);
			} else {
				// Procedure execution
				resultset2 = (ResultSet) cs.getObject(8);
				resultSetMetaData = resultset2.getMetaData();
				int coulmnCount = resultSetMetaData.getColumnCount();
				while (resultset2.next()) {
					MliWiseDetailBean1 = new MliWiseReportDetailBean();
					MliWiseDetailBean1.setSrNo(resultset2.getString("SrNo"));
					MliWiseDetailBean1.setGimandAdvaceNo(resultset2.getString("Demand Advice Number"));
					MliWiseDetailBean1.setGeneratedOnDate(resultset2.getString("Generated On Date"));
					MliWiseDetailBean1.setNoOfapplication(resultset2.getString("No Of Application"));
					MliWiseDetailBean1.setMemberId(resultset2.getString("Member Id"));
					MliWiseDetailBean1.setSsiName(resultset2.getString("Ssi Name"));
					MliWiseDetailBean1.setBaseAmount(resultset2.getString("Base Amount"));
					MliWiseDetailBean1.setDanAmount(resultset2.getString("Dan Amount"));
					MliWiseDetailBean1.setIgstAmount(resultset2.getString("IGST Amount"));
					MliWiseDetailBean1.setCgstAmount(resultset2.getString("CGST Amount"));
					MliWiseDetailBean1.setSgstAmount(resultset2.getString("SGST Amount"));
					MliWiseReportDetailList1.add(MliWiseDetailBean1);
				}

			}
		} catch (Exception exception) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}
			exception.printStackTrace();
			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session5.close();
		}
		return MliWiseReportDetailList1;

	}

//,MAX (to_char(B.DCI_GUARANTEE_START_DT)) AS DCI_GUARANTEE_START_DT
	@Override
	public List<Object[]> getDANGFDetails(String role, String userid, String fromDate, String toDate, String MliId,
			String ssName) {
		List<Object[]> result = new ArrayList<Object[]>();
		Session session8 = sessionFactory.openSession();
		try {
			if (!MliId.equals("ALL")) {
				String queary = "SELECT DISTINCT  A.DAN_ID  AS DANID, MAX (to_char(C.DAN_GENERATED_DT))"
						+ " AS GENERATED_DATE,  COUNT (*) AS CNT,  MAX (A.MEM_BNK_ID || A.MEM_BRN_ID || A.MEM_ZNE_ID) "
						+ "AS MLI_ID,  MAX (A.MSE_NAME) AS UNIT_NAME, MAX(round (B.DCI_BASE_AMT)) AS BASE_AMOUNT, "
						+ " MAX(round (B.DCI_AMOUNT_RAISED)) AS DAN_AMOUNT,  MAX(round (B.IGST_AMT)) AS IGST_AMT, "
						+ " MAX(round(B.CGST_AMT)) AS CGST_AMT,  MAX(round(B.SGST_AMT)) AS SGST_AMT,MAX (to_char(B.DCI_GUARANTEE_START_DT))  FROM NBFC_INTERFACE_UPLOAD A,"
						+ "  NBFC_DAN_CGPAN_INFO B,  NBFC_DEMAND_ADVICE_INFO C  WHERE "
						+ " A.DAN_ID = B.DAN_ID  AND B.DAN_ID = C.DAN_ID  AND A.STATUS NOT IN ('DEL','CMR','CCR')"
						+ "  AND TRUNC (C.DAN_GENERATED_DT) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
						+ "AND (A.MEM_BNK_ID || A.MEM_BRN_ID || A.MEM_ZNE_ID) ='" + MliId + "' GROUP BY A.DAN_ID";

				System.out.println("DAN GF Report For Specific MLI Query==" + queary);
				result = session8.createSQLQuery(queary).list();
			} else {

				String queary = "SELECT DISTINCT  A.DAN_ID  AS DANID, MAX (to_char(C.DAN_GENERATED_DT))"
						+ " AS GENERATED_DATE,  COUNT (*) AS CNT,  MAX (A.MEM_BNK_ID || A.MEM_BRN_ID || A.MEM_ZNE_ID) "
						+ "AS MLI_ID,  MAX (A.MSE_NAME) AS UNIT_NAME, MAX(round (B.DCI_BASE_AMT)) AS BASE_AMOUNT, "
						+ " MAX(round (B.DCI_AMOUNT_RAISED)) AS DAN_AMOUNT,  MAX(round (B.IGST_AMT)) AS IGST_AMT, "
						+ " MAX(round(B.CGST_AMT)) AS CGST_AMT,  MAX(round(B.SGST_AMT)) AS SGST_AMT,MAX (to_char(B.DCI_GUARANTEE_START_DT)) FROM NBFC_INTERFACE_UPLOAD A,"
						+ "  NBFC_DAN_CGPAN_INFO B,  NBFC_DEMAND_ADVICE_INFO C  WHERE "
						+ " A.DAN_ID = B.DAN_ID  AND B.DAN_ID = C.DAN_ID  AND A.STATUS NOT IN ('DEL','CMR','CCR')"
						+ " AND TRUNC (C.DAN_GENERATED_DT) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
						+ " GROUP BY A.DAN_ID";
				System.out.println("DAN GF Report For ALL Query==" + queary);
				result = session8.createSQLQuery(queary).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session8.close();
		}
		return result;
	}

//ANAND
	public List<Object[]> getDanId(String DanId) {
		List<Object[]> result = new ArrayList<Object[]>();
		Session session8 = sessionFactory.openSession();
		try {
			String queary = "SELECT  cgpan,  MSE_NAME,  Round(guarantee_amount),  (to_char(NBFC_UPLOADED_DATE)),  portfolio_rate,"
					+ " LOAN_ACCOUNT_NO,  state,  Round(base_amount),  IGST_RATE ,  Round(IGST_AMT),  CGST_RATE,  Round(CGST_AMT),"
					+ " SGST_RATE,  Round(SGST_AMT),  Round(BASE_AMOUNT+IGST_AMT+CGST_AMT+SGST_AMT) AS DAN_AMOUNT FROM( SELECT  iu.cgpan, "
					+ " MSE_NAME,  guarantee_amount,  NBFC_UPLOADED_DATE,  DCI_STANDARD_RATE AS portfolio_rate, "
					+ " LOAN_ACCOUNT_NO,  state,  SANCTIONED_AMOUNT*(DCI_STANDARD_RATE/100) as base_amount,  IGST_RATE, "
					+ " CASE  WHEN IGST_RATE > 0 THEN round((guarantee_amount*(DCI_STANDARD_RATE/100)) * (IGST_RATE / 100),2)"
					+ " ELSE 0  END  AS IGST_AMT, CGST_RATE, CASE  WHEN CGST_RATE > 0 THEN round((guarantee_amount*(DCI_STANDARD_RATE/100)) * (CGST_RATE / 100),2)"
					+ " ELSE 0 END AS CGST_AMT,  SGST_RATE,  CASE  WHEN SGST_RATE > 0 THEN round((guarantee_amount*(DCI_STANDARD_RATE/100)) * (SGST_RATE / 100),2)"
					+ " ELSE 0 END AS SGST_AMT  FROM nbfc_interface_upload iu, nbfc_dan_cgpan_info"
					+ " di  WHERE IU.DAN_ID = DI.DAN_ID and iu.dan_id='" + DanId + "')";
			// String qStr="select
			// h1.NAME_BRANCH_VERTICAL,h.APP_STATUS,h.FINANCIAL_YEAR,h.QUARTER from
			// HV_MODULE_FWD_STATUS h,HV_PARTA_FORM1 h1 where h.APPCODE=h1.APPCODE AND
			// h.EMP_ZONE_CD in("+EMP_ZONE_CD+") AND h.EMP_BR_CD IN("+BranchCode+") AND
			// h.FINANCIAL_YEAR in("+FINANCIAL_YEAR+") AND QUARTER in("+QUARTER+") and
			// APP_STATUS in("+APP_STATUS+") order by NAME_BRANCH_VERTICAL";
			System.out.println("DAN ASF Report Query===" + queary);
			result = session8.createSQLQuery(queary).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session8.close();
		}
		return result;
	}

	public ApplicationStatusDetailsBean getapplicationDetails(String FileId) {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		ApplicationStatusDetailsBean bean = null;
		try {

			Session session4 = sessionFactory.openSession();
			/* Transaction tn = session4.beginTransaction(); */
			Connection conn = session4.connection();
			CallableStatement cs = conn.prepareCall("{? = call NBFC_PACKREPORT.Fun_get_LoanAccoutNo_Data(?,?,?)}");

			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, FileId);
			cs.registerOutParameter(3, OracleTypes.CURSOR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(4);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :" + pouterror);
			} else {
				// Procedure execution

				resultset = (ResultSet) cs.getObject(3);
				resultSetMetaData = resultset.getMetaData();
				// int coulmnCount = resultSetMetaData.getColumnCount();
				while (resultset.next()) {
					bean = new ApplicationStatusDetailsBean();
					bean.setLONE_TYPE(resultset.getString(1));
					bean.setPORTFOLIO_NO(resultset.getString(2));

					bean.setLOAN_ACCOUNT_NO(resultset.getString(4));
					bean.setCONSTITUTION(resultset.getString(5));
					bean.setMSE_NAME(resultset.getString(6));
					bean.setSANCTION_DATE(resultset.getString(7));
					bean.setSANCTIONED_AMOUNT(resultset.getString(8));
					bean.setFIRST_DISBURSEMENT_DATE(resultset.getString(9));
					bean.setINTEREST_RATE(resultset.getString(10));
					bean.setMICRO_SMALL(resultset.getString(11));
					bean.setTENOR_IN_MONTHS(resultset.getString(12));
					bean.setMSE_ADDRESS(resultset.getString(13));
					bean.setCITY(resultset.getString(14));
					bean.setDISTRICT(resultset.getString(15));
					bean.setPINCODE(resultset.getString(16));
					bean.setSTATE(resultset.getString(17));
					bean.setMSE_ITPAN(resultset.getString(18));
					bean.setUDYOG_AADHAR_NO(resultset.getString(19));
					bean.setINDUSTRY_NATURE(resultset.getString(20));
					bean.setINDUSTRY_SECTOR(resultset.getString(21));
					bean.setNO_OF_EMPLOYEES(resultset.getString(22));
					bean.setNEW_EXISTING_UNIT(resultset.getString(23));
					bean.setPREVIOUS_BANKING_EXPERIENCE(resultset.getString(24));
					bean.setCHIEF_PROMOTER_FIRST_NAME(resultset.getString(25));
					bean.setCHIEF_PROMOTER_MIDDLE_NAME(resultset.getString(26));
					bean.setCHIEF_PROMOTER_LAST_NAME(resultset.getString(27));
					bean.setCHIEF_PROMOTER_IT_PAN(resultset.getString(28));
					bean.setCHIEF_PROMOTER_MAIL_ID(resultset.getString(29));
					bean.setCHIEF_PROMOTER_CONTACT_(resultset.getString(30));
					bean.setMINORITY_COMMUNITY(resultset.getString(31));
					bean.setHANDICAPPED(resultset.getString(32));
					bean.setWOMEN(resultset.getString(33));
					bean.setCATEGORY(resultset.getString(34));
					bean.setPORTFOLIO_NAME(resultset.getString(36));

					bean.setDAN_ID(resultset.getString(38));
					bean.setGUARANTEE_AMOUNT(resultset.getString(39));
					bean.setCOLLETRAL_SECURITY_AMOUNT(resultset.getString(40));
					bean.setRETAIL_TRADE(resultset.getString(41));
					bean.setAADHAR_(resultset.getString(42));
					bean.setCGPAN(resultset.getString(43));
					bean.setOUTSTANDING_AMOUNT(resultset.getString(44));
					bean.setMEMBER_ID(resultset.getString(45) + resultset.getString(46) + resultset.getString(47));
					// bean.setMEM_BNK_ID(resultset.getString(76));
					// bean.setMEM_ZNE_ID(resultset.getString(77));
					// bean.setMEM_BRN_ID(resultset.getString(78));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;

	}

	@Override
	public List<Object[]> getDANASFDetails(String role, String userid, String fromDate, String toDate, String MliId,
			String ssName) {
		List<Object[]> result = new ArrayList<Object[]>();
		Session session8 = sessionFactory.openSession();
		try {
			if (!MliId.equals("ALL")) {
				String queary = "SELECT  MEMBER_ID , SSI_NAME," + " DANID , GENERATED_DATE"
						+ ", CNT  , BASE_AMOUNT  , DAN_AMOUNT " + ", IGST_AMT  , CGST_AMT ,"
						+ " SGST_AMT  FROM( SELECT DISTINCT A.MEM_BNK_ID || A.MEM_BRN_ID ||"
						+ " A.MEM_ZNE_ID MEMBER_ID, MAX (A.MSE_NAME) SSI_NAME, E.DAN_ID DANID, MAX (TO_CHAR "
						+ "(D.DAN_GENERATED_DT)) GENERATED_DATE, COUNT (*) AS CNT, MAX(round (C.DCI_BASE_AMT))"
						+ " AS BASE_AMOUNT, MAX (ROUND (C.DCI_AMOUNT_RAISED)) AS DAN_AMOUNT, MAX (ROUND (C.IGST_AMT))"
						+ " AS IGST_AMT, MAX (ROUND (C.CGST_AMT)) AS CGST_AMT, MAX (ROUND (C.SGST_AMT)) AS SGST_AMT "
						+ "FROM NBFC_INTERFACE_UPLOAD A, NBFC_MEMBER_INFO B, NBFC_DAN_CGPAN_INFO C,"
						+ " NBFC_DEMAND_ADVICE_INFO D, NBFC_OUTSTANDING_AMT E WHERE A.MEM_BNK_ID || A.MEM_BRN_ID ||"
						+ " A.MEM_ZNE_ID = B.MEM_BNK_ID || B.MEM_BRN_ID || B.MEM_ZNE_ID AND C.DAN_ID = D.DAN_ID AND "
						+ "E.FILE_ID = C.FILE_ID AND E.DAN_ID = C.DAN_ID AND A.STATUS NOT IN ('DEL','CMR','CCR') AND TRUNC"
						+ " (D.DAN_GENERATED_DT) BETWEEN '" + fromDate + "' AND '" + toDate + "' AND (A.MEM_BNK_ID || "
						+ "A.MEM_BRN_ID || A.MEM_ZNE_ID) = '" + MliId + "' GROUP BY A.MEM_BNK_ID || A.MEM_BRN_ID"
						+ " || A.MEM_ZNE_ID, E.DAN_ID)";
				System.out.println("DAN ASF Report Specific Query===" + queary);
				result = session8.createSQLQuery(queary).list();
			} else {
				String queary = "SELECT  MEMBER_ID , SSI_NAME," + " DANID , GENERATED_DATE"
						+ ", CNT  , BASE_AMOUNT  , DAN_AMOUNT " + ", IGST_AMT  , CGST_AMT ,"
						+ " SGST_AMT  FROM( SELECT DISTINCT A.MEM_BNK_ID || A.MEM_BRN_ID ||"
						+ " A.MEM_ZNE_ID MEMBER_ID, MAX (A.MSE_NAME) SSI_NAME, E.DAN_ID DANID, MAX (TO_CHAR "
						+ "(D.DAN_GENERATED_DT)) GENERATED_DATE, COUNT (*) AS CNT, MAX(round (C.DCI_BASE_AMT))"
						+ " AS BASE_AMOUNT, MAX (ROUND (C.DCI_AMOUNT_RAISED)) AS DAN_AMOUNT, MAX (ROUND (C.IGST_AMT))"
						+ " AS IGST_AMT, MAX (ROUND (C.CGST_AMT)) AS CGST_AMT, MAX (ROUND (C.SGST_AMT)) AS SGST_AMT "
						+ "FROM NBFC_INTERFACE_UPLOAD A, NBFC_MEMBER_INFO B, NBFC_DAN_CGPAN_INFO C,"
						+ " NBFC_DEMAND_ADVICE_INFO D, NBFC_OUTSTANDING_AMT E WHERE A.MEM_BNK_ID || A.MEM_BRN_ID ||"
						+ " A.MEM_ZNE_ID = B.MEM_BNK_ID || B.MEM_BRN_ID || B.MEM_ZNE_ID AND C.DAN_ID = D.DAN_ID AND "
						+ "E.FILE_ID = C.FILE_ID AND E.DAN_ID = C.DAN_ID AND A.STATUS NOT IN ('DEL','CMR','CCR') AND TRUNC"
						+ " (D.DAN_GENERATED_DT) BETWEEN '" + fromDate + "' AND '" + toDate
						+ "' GROUP BY A.MEM_BNK_ID || A.MEM_BRN_ID" + " || A.MEM_ZNE_ID, E.DAN_ID)";
				System.out.println("DAN ASF Report ALL Query===" + queary);
				result = session8.createSQLQuery(queary).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session8.close();
		}
		return result;
	}

	public List<Object[]> getDanIdForASF(String DanId) {
		List<Object[]> result = new ArrayList<Object[]>();
		Session session8 = sessionFactory.openSession();
		try {
			String queary = "SELECT cgpan, MSE_NAME, ROUND (guarantee_amount) GUA_AMNT,ROUND(OS_AMT) ,"
					+ "(TO_CHAR (NBFC_UPLOADED_DATE)) UPLOAD_DATE, portfolio_rate,  LOAN_ACCOUNT_NO, state, "
					+ "ROUND(base_amount), ROUND(IGST_RATE), ROUND(IGST_AMT), CGST_RATE, ROUND(CGST_AMT), SGST_RATE, ROUND(SGST_AMT),"
					+ " ROUND (BASE_AMOUNT + IGST_AMT + CGST_AMT + SGST_AMT) AS DAN_AMOUNT FROM "
					+ "(SELECT iu.cgpan, MSE_NAME, guarantee_amount, NBFC_UPLOADED_DATE, DCI_STANDARD_RATE "
					+ "AS portfolio_rate, LOAN_ACCOUNT_NO, state, SANCTIONED_AMOUNT * (DCI_STANDARD_RATE / 100)"
					+ " AS base_amount, IGST_RATE,ad.OUTSTANDING_AMOUNT OS_AMT, CASE WHEN IGST_RATE > 0 THEN ROUND ( (ad.ASF_FEE) * "
					+ "(di.IGST_RATE / 100), 2) ELSE 0 END AS IGST_AMT, CGST_RATE, CASE WHEN CGST_RATE >"
					+ " 0 THEN ROUND ( (ad.ASF_FEE) * (di.CGST_RATE / 100), 2) ELSE 0 END AS CGST_AMT, SGST_RATE,"
					+ " CASE WHEN SGST_RATE > 0 THEN ROUND ( (ad.ASF_FEE) * (di.SGST_RATE / 100), 2) "
					+ "ELSE 0 END AS SGST_AMT FROM nbfc_interface_upload iu, nbfc_dan_cgpan_info di,"
					+ " nbfc_asf_detail ad WHERE ad.ASF_DAN_ID = DI.DAN_ID AND ad.cgpan = iu.cgpan and "
					+ "ad.asf_dan_id='" + DanId + "')";
			// String qStr="select
			// h1.NAME_BRANCH_VERTICAL,h.APP_STATUS,h.FINANCIAL_YEAR,h.QUARTER from
			// HV_MODULE_FWD_STATUS h,HV_PARTA_FORM1 h1 where h.APPCODE=h1.APPCODE AND
			// h.EMP_ZONE_CD in("+EMP_ZONE_CD+") AND h.EMP_BR_CD IN("+BranchCode+") AND
			// h.FINANCIAL_YEAR in("+FINANCIAL_YEAR+") AND QUARTER in("+QUARTER+") and
			// APP_STATUS in("+APP_STATUS+") order by NAME_BRANCH_VERTICAL";
			result = session8.createSQLQuery(queary).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session8.close();
		}
		return result;
	}

	@Override
	public String getMEMBNKID(String UserId) {
		Session session = sessionFactory.openSession();
		String result = "";
		try {
			String qStr = "select MEM_BNK_ID from NBFC_USER_INFO where USR_ID='" + UserId + "'";
			result = (String) session.createSQLQuery(qStr).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public Map<String, String> getBankName(String BankId) {
		Session session = sessionFactory.openSession();
		Map<String, String> mapObj1 = new HashMap<String, String>();
		String result = "";
		try {
			String qStr = "SELECT  M.MEM_BANK_NAME FROM NBFC_USER_INFO U,NBFC_MEMBER_INFO M WHERE M.MEM_BNK_ID||M.MEM_ZNE_ID||M.MEM_BRN_ID=U.MEM_BNK_ID||U.MEM_ZNE_ID||U.MEM_BRN_ID AND U.USR_ID='"
					+ BankId + "'";
			result = (String) session.createSQLQuery(qStr).uniqueResult();
			mapObj1.put(result, result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return mapObj1;
	}

	@Override
	public Map<String, String> getAllMliLongName() {
		Map<String, String> mapObj = new LinkedHashMap<String, String>();
		mapObj.put("ALL", "All MLI Long Name");
		Session session = sessionFactory.openSession();
		try {
			String hql = "from CGTMSEExposureMasterMLIName cemm where cemm.status='CCA'";
			Query query = (Query) session.createQuery(hql);
			List<CGTMSEExposureMasterMLIName> listCategories3 = query.list();
			java.util.Iterator<CGTMSEExposureMasterMLIName> itr = listCategories3.iterator();
			while (itr.hasNext()) {
				Object o = itr.next();
				CGTMSEExposureMasterMLIName s1 = (CGTMSEExposureMasterMLIName) o;
				if (!s1.getMliLongName().equals("TESTBANK") && !s1.getMliLongName().equals("DEMO_USER")) {
					mapObj.put(s1.getMliLongName(), s1.getMliLongName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return mapObj;
	}

	@Override
	public List<Object[]> getListOfMlis(String MliID) {
		Session session = sessionFactory.openSession();
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		try {
			session = sessionFactory.openSession();

			if (MliID.equals("ALL")) {
				String qStr = "SELECT DISTINCT (A.MEM_BANK_NAME) AS MLI,COUNT (*) AS COUNT,MAX (MEM_BNK_ID) FROM NBFC_MEMBER_INFO A GROUP BY A.MEM_BANK_NAME";
				result = (ArrayList<Object[]>) session.createSQLQuery(qStr).list();
			} else {
				// String qStr="select
				// h1.NAME_BRANCH_VERTICAL,h.APP_STATUS,h.FINANCIAL_YEAR,h.QUARTER from
				// HV_MODULE_FWD_STATUS h,HV_PARTA_FORM1 h1 where h.APPCODE=h1.APPCODE AND
				// h.EMP_ZONE_CD in("+EMP_ZONE_CD+") AND h.EMP_BR_CD IN("+BranchCode+") AND
				// h.FINANCIAL_YEAR in("+FINANCIAL_YEAR+") AND QUARTER in("+QUARTER+") and
				// APP_STATUS in("+APP_STATUS+") order by NAME_BRANCH_VERTICAL";
				String qStr = "SELECT DISTINCT (A.MEM_BANK_NAME) AS MLI, COUNT (*) AS COUNT, MAX (MEM_BNK_ID) FROM NBFC_MEMBER_INFO A where"
						+ " A.MEM_BNK_ID||A.MEM_ZNE_ID||A.MEM_BRN_ID ='" + MliID + "' GROUP BY A.MEM_BANK_NAME";
				result = (ArrayList<Object[]>) session.createSQLQuery(qStr).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;

	}

	@Override
	public List<Object[]> getMLISDetails(String MemberBankID) {
		Session session = sessionFactory.openSession();
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		try {
			session = sessionFactory.openSession();
			String qStr = "SELECT DISTINCT A.MEM_DISTRICT_NAME||','||MEM_STATE_NAME||','||MEM_CITY||','||MEM_PINCODE AS ADDRESS, A.MEM_PHONE_CODE||'-'||A.MEM_PHONE_NUMBER AS PHONE, A.GSTIN_NO AS GST FROM NBFC_MEMBER_INFO A where MEM_BNK_ID='1020'";
			result = (ArrayList<Object[]>) session.createSQLQuery(qStr).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;

	}

	@Override
	public List<Map<String, Object>> getWomenEntrepreneurReportDetails(String role, String userid, Date fromDate,
			Date toDate, String state, String distict, String MliId) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Session session5 = sessionFactory.openSession();
		Transaction tn = session5.beginTransaction();
		Connection conn = session5.connection();

		try {
			CallableStatement cs = conn.prepareCall("{? = call FUNC_WOMEN_REPORT(?,?,?,?,?,?,?,?,?)}");
			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, role);
			cs.setString(3, userid);
			cs.setDate(4, new java.sql.Date(toDate.getTime()));
			cs.setDate(5, new java.sql.Date(fromDate.getTime()));
			cs.setString(6, state);
			cs.setString(7, distict);
			cs.setString(8, MliId);
			cs.registerOutParameter(9, OracleTypes.CURSOR);
			cs.registerOutParameter(10, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(10);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :" + pouterror);
			} else {
				// Procedure execution
				resultset1 = (ResultSet) cs.getObject(9);
				resultSetMetaData = resultset1.getMetaData();
				int coulmnCount = resultSetMetaData.getColumnCount();
				while (resultset1.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
					for (int i = 1; i <= coulmnCount; i++) {
						columns.put(resultSetMetaData.getColumnLabel(i), resultset1.getObject(i));
					}
					rows.add(columns);
				}
			}

		} catch (Exception exception) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}
			exception.printStackTrace();
			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session5.close();
		}
		return rows;
	}

	@Override
	public List<MliWiseReportDetailBean> getWomenEntrepreneurDetails(String role, String userid, Date fromDate,
			Date toDate, String state, String distict, String MliId) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		MliWiseReportDetailBean MliWiseDetailBean1 = null;
		List<MliWiseReportDetailBean> MliWiseReportDetailList1 = new ArrayList<MliWiseReportDetailBean>();
		Session session5 = sessionFactory.openSession();
		Transaction tn = session5.beginTransaction();
		Connection conn = session5.connection();

		try {
			CallableStatement cs = conn.prepareCall("{? = call FUNC_WOMEN_REPORT(?,?,?,?,?,?,?,?,?)}");
			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, role);
			cs.setString(3, userid);
			cs.setDate(4, new java.sql.Date(toDate.getTime()));
			cs.setDate(5, new java.sql.Date(fromDate.getTime()));
			cs.setString(6, state);
			cs.setString(7, distict);
			cs.setString(8, MliId);
			cs.registerOutParameter(9, OracleTypes.CURSOR);
			cs.registerOutParameter(10, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(10);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :" + pouterror);
			} else {
				// Procedure execution
				resultset1 = (ResultSet) cs.getObject(9);
				resultSetMetaData = resultset1.getMetaData();
				int coulmnCount = resultSetMetaData.getColumnCount();
				while (resultset1.next()) {
					MliWiseDetailBean1 = new MliWiseReportDetailBean();
					MliWiseDetailBean1.setSrNo(resultset1.getString("SR NO"));
					MliWiseDetailBean1.setNoOfCases(resultset1.getString("NO OF CASES"));
					MliWiseDetailBean1.setApprovedAmount(resultset1.getString("APPROVED AMOUNT(IN LAKH)"));
					MliWiseReportDetailList1.add(MliWiseDetailBean1);
				}
			}

		} catch (Exception exception) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}
			exception.printStackTrace();
			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session5.close();
		}
		return MliWiseReportDetailList1;
	}

	@Override
	public List<Object[]> getStateName(String role, String MemberId) {
		Map<String, String> mapObj = new LinkedHashMap<String, String>();
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		Object[] obj = new Object[1];
		Session session = sessionFactory.openSession();
		try {
			if (role.equals("CMAKER") || role.equals("CCHECKER")) {
				String hql = "SELECT STE_CODE ,STE_NAME  FROM NBFC_STATE_MASTER order by STE_NAME";
				Query query = (Query) session.createSQLQuery(hql);
				result = (ArrayList<Object[]>) query.list();
			} else {
				String hql = "SELECT STE_CODE ,STE_NAME  FROM NBFC_STATE_MASTER order by STE_NAME";
				Query query = (Query) session.createSQLQuery(hql);
				result = (ArrayList<Object[]>) query.list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public List<Object[]> getDistictName(String role, String MemberId, String state_code) {
		Map<String, String> mapObj = new LinkedHashMap<String, String>();
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		Session session = sessionFactory.openSession();
		try {
			if (role.equals("CMAKER") || role.equals("CCHECKER")) {
				String hql = "SELECT  DST_NAME , DST_NAME As dist FROM NBFC_DISTRICT_MASTER where STE_CODE='"
						+ state_code + "'  order by DST_NAME";
				Query query = (Query) session.createSQLQuery(hql);
				result = (ArrayList<Object[]>) query.list();
			} else {
				String hql = " SELECT  DST_NAME , DST_NAME As dist FROM NBFC_DISTRICT_MASTER where STE_CODE='"
						+ state_code + "'  order by DST_NAME";
				Query query = (Query) session.createSQLQuery(hql);
				result = (ArrayList<Object[]>) query.list();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public String getsateNme(String state_code) {
		Session session = null;
		String state = "";
		try {
			session = sessionFactory.openSession();
			String queary = "select STE_NAME from  NBFC_STATE_MASTER where STE_CODE='" + state_code + "'";
			// String qStr="select
			// h1.NAME_BRANCH_VERTICAL,h.APP_STATUS,h.FINANCIAL_YEAR,h.QUARTER from
			// HV_MODULE_FWD_STATUS h,HV_PARTA_FORM1 h1 where h.APPCODE=h1.APPCODE AND
			// h.EMP_ZONE_CD in("+EMP_ZONE_CD+") AND h.EMP_BR_CD IN("+BranchCode+") AND
			// h.FINANCIAL_YEAR in("+FINANCIAL_YEAR+") AND QUARTER in("+QUARTER+") and
			// APP_STATUS in("+APP_STATUS+") order by NAME_BRANCH_VERTICAL";
			state = (String) session.createSQLQuery(queary).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return state;
	}

	@Override
	public List<Object[]> getWomenEntrepreneurDetailList(String role, String userid, String fromDate, String toDate,
			String state, String distict, String MliId) {
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		Session session = sessionFactory.openSession();
		try {
			if (MliId.equals("ALL")) {
				String hql = " SELECT D.STATE, D.DISTRICT,  COUNT (*) AS CNT,  ROUND "
						+ "(SUM ( (D.GUARANTEE_AMOUNT)), 2) GUARANTEE_AMOUNT FROM NBFC_INTERFACE_UPLOAD D "
						+ "WHERE     D.WOMEN = 'Y'  AND TRUNC (D.NBFC_UPLOADED_DATE) BETWEEN '" + fromDate + "'"
						+ " AND '" + toDate + "' GROUP BY D.STATE, D.DISTRICT";
				Query query = (Query) session.createSQLQuery(hql);
				result = (ArrayList<Object[]>) query.list();

			} else if (MliId.equals("ALL") || distict.equals("ALL")) {
				String hql = " SELECT D.STATE,  D.DISTRICT, COUNT (*) AS CNT,  ROUND (SUM ( (D.GUARANTEE_AMOUNT)), 2)"
						+ " GUARANTEE_AMOUNT  FROM NBFC_INTERFACE_UPLOAD D  WHERE     D.WOMEN = 'Y'  AND TRUNC "
						+ "(D.NBFC_UPLOADED_DATE) BETWEEN '" + fromDate + "' AND '" + toDate + "'  " + "AND D.STATE = '"
						+ state + "'  GROUP BY D.STATE, D.DISTRICT";
				Query query = (Query) session.createSQLQuery(hql);
				result = (ArrayList<Object[]>) query.list();
			} else if (!MliId.equals("ALL") || distict.equals("ALL")) {
				String hql = " SELECT D.STATE, D.DISTRICT,  COUNT (*) AS CNT,  ROUND "
						+ "(SUM ( (D.GUARANTEE_AMOUNT)), 2) GUARANTEE_AMOUNT FROM NBFC_INTERFACE_UPLOAD D "
						+ "WHERE     D.WOMEN = 'Y'  AND TRUNC (D.NBFC_UPLOADED_DATE) BETWEEN '" + fromDate + "'"
						+ " AND '" + toDate + "'  AND D.STATE = '" + state + "'  AND D.MEM_BNK_ID "
						+ "|| D.MEM_ZNE_ID || D.MEM_BRN_ID = '" + MliId + "'  GROUP BY D.STATE, D.DISTRICT";
				Query query = (Query) session.createSQLQuery(hql);
				result = (ArrayList<Object[]>) query.list();
			} /*
				 * else if(!MliId.equals("ALL") || distict.equals("ALL")|| state.equals("ALL")){
				 * String hql = " SELECT D.STATE, D.DISTRICT,  COUNT (*) AS CNT,  ROUND " +
				 * "(SUM ( (D.GUARANTEE_AMOUNT)), 2) GUARANTEE_AMOUNT FROM NBFC_INTERFACE_UPLOAD D "
				 * + "WHERE     D.WOMEN = 'Y'  AND TRUNC (D.NBFC_UPLOADED_DATE) BETWEEN '"
				 * +fromDate+"'" + " AND '"+toDate+"' GROUP BY D.STATE, D.DISTRICT"; Query query
				 * = (Query) session.createSQLQuery(hql); result = (ArrayList<Object[]>)
				 * query.list(); }
				 */
			else {
				String hql = " SELECT D.STATE, D.DISTRICT,  COUNT (*) AS CNT,  ROUND "
						+ "(SUM ( (D.GUARANTEE_AMOUNT)), 2) GUARANTEE_AMOUNT FROM NBFC_INTERFACE_UPLOAD D "
						+ "WHERE     D.WOMEN = 'Y'  AND TRUNC (D.NBFC_UPLOADED_DATE) BETWEEN '" + fromDate + "'"
						+ " AND '" + toDate + "'  AND D.STATE = '" + state + "' AND D.DISTRICT = '" + distict
						+ "'  AND D.MEM_BNK_ID " + "|| D.MEM_ZNE_ID || D.MEM_BRN_ID = '" + MliId
						+ "'  GROUP BY D.STATE, D.DISTRICT";
				Query query = (Query) session.createSQLQuery(hql);
				result = (ArrayList<Object[]>) query.list();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	public List<Object[]> getDanTeaxtGstReportList(String fromDate, String toDate) {
		Session session = null;
		System.out.println("fromDatefromDatefromDatefromDate -" + fromDate);
		System.out.println("toDatetoDatetoDatetoDate  -" + toDate);
		List<Object[]> result = new ArrayList<Object[]>();
		try {
			session = sessionFactory.openSession();
			String queary = "SELECT DISTINCT E.DAN_ID, M.MEM_BANK_NAME, M.MEM_ZONE_NAME, E.MEM_BNK_ID || "
					+ "E.MEM_ZNE_ID || E.MEM_BRN_ID MLI, M.GSTIN_NO, E.PORTFOLIO_NAME, G.TAX_INV_ID,"
					+ " C.DCI_AMOUNT_RAISED DANAMT, round(C.DCI_BASE_AMT,2), C.IGST_AMT, C.IGST_RATE, C.CGST_AMT,"
					+ " C.CGST_RATE, C.SGST_AMT, C.SGST_RATE, C.DCI_APPROPRIATION_FLAG, (to_char(C.DCI_APPROPRIATION_DT)),"
					+ " (to_char(C.DCI_GUARANTEE_START_DT)), M.MEM_STATE_NAME FROM NBFC_MEMBER_INFO M, "
					+ "NBFC_DAN_CGPAN_INFO c, NBFC_INTERFACE_UPLOAD e, NBFC_GST_TAX_INVOICE G WHERE c.DAN_ID "
					+ "= E.DAN_ID AND G.PORTFOLIO_NAME = E.PORTFOLIO_NAME AND E.MEM_BNK_ID = M.MEM_BNK_ID AND "
					+ "E.MEM_BNK_ID || E.MEM_ZNE_ID || E.MEM_BRN_ID = M.MEM_BNK_ID || M.MEM_ZNE_ID ||"
					+ " M.MEM_BRN_ID AND TRUNC (c.DCI_GUARANTEE_START_DT)  BETWEEN '" + toDate + "' AND '" + fromDate
					+ "'";

			result = session.createSQLQuery(queary).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	public List<Object[]> getDanTeaxtAsfGstReportList(String fromDate, String toDate) {
		Session session = null;
		List<Object[]> result = new ArrayList<Object[]>();
		try {
			session = sessionFactory.openSession();
			String queary = "SELECT DISTINCT G.DAN_ID, M.MEM_BANK_NAME, M.MEM_ZONE_NAME,"
					+ "A.MEM_BNK_ID || A.MEM_ZNE_ID || A.MEM_BRN_ID MLI, M.GSTIN_NO, G.PORTFOLIO_NAME,"
					+ " G.TAX_INV_ID, C.DCI_AMOUNT_RAISED DANAMT, round(C.DCI_BASE_AMT,2), C.IGST_AMT, C.IGST_RATE,"
					+ " C.CGST_AMT, C.CGST_RATE, C.SGST_AMT, C.SGST_RATE, C.DCI_APPROPRIATION_FLAG,"
					+ " (to_char(C.DCI_APPROPRIATION_DT)),(to_char( C.DCI_GUARANTEE_START_DT)), M.MEM_STATE_NAME FROM NBFC_MEMBER_INFO M,"
					+ " NBFC_DAN_CGPAN_INFO c, NBFC_TAX_INVOICE_ASF G, NBFC_OUTSTANDING_AMT A WHERE"
					+ " A.DAN_ID=C.DAN_ID AND G.DAN_ID=C.DAN_ID AND A.MEM_BNK_ID = M.MEM_BNK_ID AND A.MEM_BNK_ID"
					+ " || A.MEM_ZNE_ID || A.MEM_BRN_ID = M.MEM_BNK_ID || M.MEM_ZNE_ID || M.MEM_BRN_ID AND "
					+ "TRUNC (c.DCI_GUARANTEE_START_DT) BETWEEN '" + toDate + "' AND '" + fromDate + "'";
			result = session.createSQLQuery(queary).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public List<Object[]> getDANAppropriationASFGFDetails(String role, String userId, String fromDate, String toDate,
			String danType) {
		List<Object[]> result = new ArrayList<Object[]>();
		Session session8 = sessionFactory.openSession();
		try {
			if (danType.equals("GF")) {
				String queary = "SELECT DISTINCT  A.DAN_ID  AS DANID, MAX (D.MEM_BANK_NAME) " + " AS NAMEOFNBFC,"
						+ "(round (sum(A.GUARANTEE_AMOUNT))) AS guarantee_AMOUNT,"
						+ " MAX(round (B.DCI_AMOUNT_RAISED)) AS DAN_AMOUNT,"
						+ "  MAX( (B.DCI_APPROPRIATION_FLAG)) AS flag" + "  FROM NBFC_INTERFACE_UPLOAD A,"
						+ "                   NBFC_DAN_CGPAN_INFO B,  NBFC_DEMAND_ADVICE_INFO C,NBFC_MEMBER_INFO d  WHERE "
						+ "                  A.DAN_ID = B.DAN_ID  AND B.DAN_ID = C.DAN_ID  AND A.STATUS NOT IN ('DEL','CMR','CCR')"
						+ "                  and D.MEM_BNK_ID = A.MEM_BNK_ID"
						+ "  AND TRUNC (C.DAN_GENERATED_DT) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
						+ " GROUP BY A.DAN_ID";

				System.out.println("DAN GF Report For Specific MLI Query==" + queary);
				result = session8.createSQLQuery(queary).list();
			} else {

				String queary = "SELECT DANID,Mli_name,OUTSTANDING_AMOUNT,"
						+ "       DAN_AMOUNT, FLAG FROM (  SELECT DISTINCT E.DAN_ID DANID,"
						+ "                 MAX (B.MEM_BANK_NAME) Mli_name,"
						+ "                 SUM (ROUND (E.OUTSTANDING_AMT)) AS OUTSTANDING_AMOUNT,"
						+ "                 MAX (ROUND (C.DCI_AMOUNT_RAISED)) AS DAN_AMOUNT,"
						+ "                 MAX ( (C.DCI_APPROPRIATION_FLAG)) AS FLAG"
						+ "            FROM NBFC_INTERFACE_UPLOAD A,NBFC_MEMBER_INFO B,"
						+ "                 NBFC_DAN_CGPAN_INFO C,NBFC_DEMAND_ADVICE_INFO D,"
						+ "                 NBFC_OUTSTANDING_AMT E"
						+ "           WHERE     A.MEM_BNK_ID || A.MEM_BRN_ID || A.MEM_ZNE_ID = B.MEM_BNK_ID || B.MEM_BRN_ID || B.MEM_ZNE_ID"
						+ "                 AND C.DAN_ID = D.DAN_ID AND E.CGPAN = A.CGPAN"
						+ "                 AND E.DAN_ID = C.DAN_ID"
						+ "                 AND A.STATUS NOT IN ('DEL', 'CMR', 'CCR', 'ACL')"
						+ "                 AND TRUNC (D.DAN_GENERATED_DT) BETWEEN '" + fromDate + "' AND '" + toDate+ "' "
						+ "   GROUP BY E.DAN_ID)";
				System.out.println("DAN GF Report For ALL Query==" + queary);
				result = session8.createSQLQuery(queary).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session8.close();
		}
		return result;
	}
	@Override
	public List<Map<String, Object>> getGuaranteeWise(String role, String userid, Date fromDate, Date toDate,HttpServletRequest request) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		  Session session5 = sessionFactory.openSession();
		    Transaction tn = session5.beginTransaction(); 
			Connection conn = session5.connection();
			 
		try {
			CallableStatement cs = conn .prepareCall("{? = call MIS_GUAR_WISE_REPORT(?,?,?,?,?,?)}");
	        // register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2,role);
			cs.setString(3,userid);
			cs.setDate(4,new java.sql.Date(fromDate.getTime()));
			cs.setDate(5,new java.sql.Date(toDate.getTime()));
			//cs.setString(6, MliId);
			cs.registerOutParameter(6, OracleTypes.CURSOR);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(7);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :" + pouterror);
			} else {
				// Procedure execution
				resultset1 = (ResultSet) cs.getObject(6);
				resultSetMetaData = resultset1.getMetaData();
				int coulmnCount = resultSetMetaData.getColumnCount();
				BigDecimal gurTotCount;
				BigDecimal sumGurTotCount = new BigDecimal(0);
				BigDecimal gurTotAmt;
				BigDecimal sumGurTotAmt =   new BigDecimal(0);
				while (resultset1.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
					for (int i = 1; i <= coulmnCount; i++) {
						//System.out.println("column Name==="+resultSetMetaData.getColumnLabel(i));
						//System.out.println("Value==="+resultset1.getObject(i));
						if(resultSetMetaData.getColumnLabel(i).equals("GUARANTEED COUNT")) {
							gurTotCount=(BigDecimal) resultset1.getObject(i);
							sumGurTotCount = sumGurTotCount.add(gurTotCount);
						}
						if(resultSetMetaData.getColumnLabel(i).equals("GUARANTEED AMOUNT")) {
							gurTotAmt=(BigDecimal)resultset1.getObject(i);
							sumGurTotAmt = sumGurTotAmt.add(gurTotAmt);
						}
						 columns.put(resultSetMetaData.getColumnLabel(i), resultset1.getObject(i));
					}
					
					rows.add(columns);
				}
				request.setAttribute("sumGurTotCountKey", sumGurTotCount);
				request.setAttribute("sumGurTotAmtKey", sumGurTotAmt);
				
				//System.out.println("sumGurTotCount==="+sumGurTotCount);
				//System.out.println("sumGurTotAmt==="+sumGurTotAmt);
			}

		} catch (Exception exception) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
				ignore.printStackTrace();
				throw new CustomExceptionHandler(ignore.getMessage());
			}
			exception.printStackTrace();
			throw new CustomExceptionHandler(exception.getMessage());
		}finally {
			session5.close();
		}
		return rows;
	}
	
	
	

}
