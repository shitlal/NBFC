package com.nbfc.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nbfc.bean.ApplicationStatusDetailsBean;
import com.nbfc.bean.ClaimApprovalBean;
import com.nbfc.bean.ClaimLodgementBean;

import com.nbfc.bean.TenureModificationDetailsBean;
import com.raistudies.domain.CustomExceptionHandler;

@Repository("TenureModificationDao")
public class TenureModificationDaoImpl implements TenureModificationDao {
	@Autowired
	SessionFactory sessionFactory;
	ResultSet resultset = null;
	ResultSetMetaData resultSetMetaData = null;

	@Override
	public TenureModificationDetailsBean getTenureModificationDetails(
			String cgpan) {
		TenureModificationDetailsBean Bean = null;
		// System.out.println("getSaveClaimLodgmentData method called to procedure inside DAOImpl==");
		Session session3 = sessionFactory.openSession();
		Transaction tn = session3.beginTransaction();
		Connection con = session3.connection();
		CallableStatement callableStmt = null;
		ResultSet resultset = null;
		List<TenureModificationDetailsBean> dataList = new ArrayList<TenureModificationDetailsBean>();
		DecimalFormat formatter;
		BigDecimal bg;
		try {
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			CallableStatement callableStatement = (CallableStatement) con
					.prepareCall("{ call proc_getTenureModDetail(?,?,?) } ");
			callableStatement.setString(1, cgpan);
			callableStatement.registerOutParameter(2, Types.VARCHAR);

			callableStatement.registerOutParameter(3, OracleTypes.CURSOR);
			callableStatement.execute();
			resultset = (ResultSet) callableStatement.getObject(3);
			boolean empty = true;
			while (resultset.next()) {
				empty = false;
				Bean = new TenureModificationDetailsBean();
				Bean.setCgpan((resultset.getString("CGPAN")));
				Bean.setBankName((resultset.getString("MEM_BANK_NAME")));
				Bean.setMliID((resultset.getString("MLIID")));
				Bean.setTenure((resultset.getString("TENOR_IN_MONTHS")));
				Bean.setState((resultset.getString("MEM_STATE_NAME")));
				Bean.setGstNo(resultset.getString("GSTIN_NO"));
				Bean.setMseName((resultset.getString("MSE_NAME")));
				Bean.setExpiryDate((resultset.getString("EXPIRY_DATE")));
				Bean.setFirstDisburseDate((resultset.getString("FIRST_DISBURSEMENT_DATE")));

			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return Bean;
	}

	@Override
	public int submitTenureModificationDetails(
			TenureModificationDetailsBean tenureBean, String loginUserMemId,
			String usr_id) {
		System.out
				.println("1===submitTenureModificationDetails method called==="
						+ loginUserMemId + "CGPAN==" + tenureBean.getCgpan()
						+ "" + "" + "getModificationRemark=="
						+ tenureBean.getModificationRemark()
						+ "getReviseTenure" + tenureBean.getReviseTenure()+
						"getReviseExpirydate" +tenureBean.getExpiryDate());
		SimpleDateFormat formatObj = new SimpleDateFormat("dd/MM/yyyy");
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		Connection conn = session.connection();
		CallableStatement callStmt = null;
		int dataSaved = 0;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		try {
			if (tenureBean != null) {
				Date sysdate = new Date();
				ResultSet resultSet = null;
				String exception = "";
				callStmt = conn
						.prepareCall("{call pro_saveTenureModDetail(?,?,?,?,?,?,?,?,?,?)}");
				callStmt.setString(1, tenureBean.getCgpan());
				callStmt.setString(2, loginUserMemId);
				callStmt.setString(3, tenureBean.getModificationRemark());
				callStmt.setString(4, tenureBean.getReviseTenure());
				callStmt.setString(5, tenureBean.getReviseExpirydate());
				callStmt.setString(6, "TA");
				callStmt.setString(7, usr_id);
				callStmt.setString(8, tenureBean.getAccStandard());
				callStmt.setString(9, tenureBean.getBankAuthority());
				callStmt.setString(10, tenureBean.getNoIntrest());
				dataSaved = callStmt.executeUpdate();
				System.out.println("Record Save==" + dataSaved);
				conn.close();
				callStmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tn.commit();
			session.close();
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return dataSaved;
	}

	@Override
	public List<TenureModificationDetailsBean> getTenureModDetailsApproval(
			String loginUserMemId) {
		TenureModificationDetailsBean Bean = null;
		// System.out.println("getSaveClaimLodgmentData method called to procedure inside DAOImpl==");
		Session session3 = sessionFactory.openSession();
		Transaction tn = session3.beginTransaction();
		Connection con = session3.connection();
		CallableStatement callableStmt = null;
		ResultSet resultset = null;
		List<TenureModificationDetailsBean> dataList = new ArrayList<TenureModificationDetailsBean>();
		DecimalFormat formatter;
		BigDecimal bg;
		try {
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			CallableStatement callableStatement = (CallableStatement) con
					.prepareCall("{ call proc_getTenureDetailApproval(?,?,?) } ");
			callableStatement.setString(1, loginUserMemId);
			callableStatement.registerOutParameter(2, Types.VARCHAR);

			callableStatement.registerOutParameter(3, OracleTypes.CURSOR);
			callableStatement.execute();
			resultset = (ResultSet) callableStatement.getObject(3);
			boolean empty = true;
			while (resultset.next()) {
				empty = false;
				Bean = new TenureModificationDetailsBean();
				Bean.setCgpan((resultset.getString("CGPAN")));
				Bean.setStatus((resultset.getString("STATUS")));
				Bean.setMliID((resultset.getString("MLIID")));
				Bean.setTenure((resultset.getString("TENOR_IN_MONTHS")));
				Bean.setReviseTenure((resultset.getString("REVISED_TENURE")));
				Bean.setReviseExpirydate(resultset
						.getString("REVISED_EXPIRY_DATE"));
				Bean.setMseName((resultset.getString("MSE_NAME")));
				Bean.setGuaranteeStartDate((resultset
						.getString("DCI_GUARANTEE_START_DT")));
				Bean.setModificationRemark((resultset
						.getString("MODIFICATION_REMARKS")));
				Bean.setBankAuthority((resultset
						.getString("ISBANK_AUTHORITY")));
				Bean.setFirstDisburseDate((resultset
						.getString("FIRST_DISBURSEMENT_DATE")));
				Bean.settId((resultset
						.getInt("TENURE_ID")));
				dataList.add(Bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.close();
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return dataList;
	}

	@Override
	public TenureModificationDetailsBean updateStatusTenureApprovedReturn(
			String userId, Map<String, Object> claimStatusMapObj,
			String userRole,String CHK) {
		Map<String, String> mapObj = null;
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		Connection conn = session.connection();
		CallableStatement callStmt = null;
		int dataUpdated = 0;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		int count = 1;
		String success = "NoUpdated";
		TenureModificationDetailsBean bean = null;
		bean = new TenureModificationDetailsBean();
		System.out.println("claimStatusMapObj :" + claimStatusMapObj);
		Iterator Itr = claimStatusMapObj.keySet().iterator();
		String Status = "";
		String declaFlag = "";
		int x = 0;
		List<String> resultlist = new ArrayList<String>();
		while (Itr.hasNext()) {

			String key = (String) Itr.next();
			TenureModificationDetailsBean claim = (TenureModificationDetailsBean) claimStatusMapObj
					.get(key);

			System.out.println("Key : " + key + "   Value : "
					+ claim.getMLI_STATUS());

			if (claim.getMLI_STATUS().equals("Accept")) {

				Status = claim.getMLI_STATUS();

				bean.setMsg("Tenure Modification Approved  Successfully");
				declaFlag=CHK;

			} else if (claim.getMLI_STATUS().equals("Return")) {

				Status = claim.getMLI_STATUS();
				declaFlag="N";

				bean.setRmsg("Tenure Modification Returned. ");
			} else if (claim.getMLI_STATUS().equals("Select")) {

				Status = claim.getMLI_STATUS();
			}

			if (!Status.equals("Select")) {

				try {
					callStmt = conn.prepareCall("{call PROC_APP_RET_BFC_Tenure(?,?,?,?,?,?)}");

					callStmt.setString(1, key);
					callStmt.setString(2, Status);
					callStmt.setString(3, userId);
					callStmt.setString(4, claim.getReturnRemark());
					callStmt.setString(5, declaFlag);
					/* START Added by VinodSingh on 12-Mar-2021 */
					callStmt.registerOutParameter(6, Types.VARCHAR);
					System.out.println("    =======================");					
					dataUpdated = callStmt.executeUpdate();
					String procStatus = callStmt.getString(6);
					System.out.println("procStatus::"+procStatus);					
					resultlist.add(procStatus);
					System.out.println("dataUpdated ==" + dataUpdated);
					
				//	POUTRESULT

					success = "Updated";
				} catch (SQLException e) {
					// tn.commit();
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				x++;
				System.out.println("Count===" + x);

			}
			bean.setStatusList(resultlist);
		}
		try {
			 tn.commit();
			session.close();
			callStmt.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bean;

	}

	@Override
	public TenureModificationDetailsBean getValidateCgpanForTenureMod(
			String cgpan, String userId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		TenureModificationDetailsBean tenuremBean = null;
		int flag = 0;
		int result1 = 0;
		Double total = 0.0;

		Connection conn = session.connection();

		try {
			Connection connection = null;
			CallableStatement getReceivedPaymentsStmt = null;

			ResultSet resultSet = null;
			String exception = "";
			getReceivedPaymentsStmt = conn
					.prepareCall("{call Proc_Validate_Cgpan_tenure(?,?,?,?)}");
			// getReceivedPaymentsStmt.registerOutParameter(1, Types.INTEGER);
			getReceivedPaymentsStmt.setString(1, cgpan);
			getReceivedPaymentsStmt.setString(2, userId);
			getReceivedPaymentsStmt.registerOutParameter(3, Types.INTEGER);
			// getReceivedPaymentsStmt.registerOutParameter(5,
			// OracleTypes.CURSOR);
			getReceivedPaymentsStmt.registerOutParameter(4, Types.VARCHAR);
			getReceivedPaymentsStmt.execute();

			String error = getReceivedPaymentsStmt.getString(4);
			if (null != error) {
				if (error.equals("NO_DATA_FOUND")) {
					int cnt = getReceivedPaymentsStmt.getInt(3);
					tenuremBean = new TenureModificationDetailsBean();
					if (cnt == 0) {

						tenuremBean.setCnt(0);// yes
					}

					getReceivedPaymentsStmt.close();
					getReceivedPaymentsStmt = null;

				}

			} else {
				int cnt = getReceivedPaymentsStmt.getInt(3);
				tenuremBean = new TenureModificationDetailsBean();
				if (cnt == 1) {

					tenuremBean.setCnt(1);// yes
				} else {
					tenuremBean.setCnt(0);// yes
				}
			}
			getReceivedPaymentsStmt.close();
			getReceivedPaymentsStmt = null;

		} catch (Exception exception) {

			try {
				conn.close();
			} catch (SQLException ignore) {
				throw new CustomExceptionHandler(ignore.getMessage());
			}

			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session.close();

		}

		return tenuremBean;
	}

	@Override
	public List<TenureModificationDetailsBean> getTenureReturnedRecordsByNBFCChecker(
			String loginUserMemId) {
		TenureModificationDetailsBean Bean = null;
		System.out
				.println("getMliBorrowerNpaDtlsBeforClaimLodgement method called to procedure inside DAOImpl==");
		Session session3 = sessionFactory.openSession();
		Transaction tn = session3.beginTransaction();
		Connection con = session3.connection();
		@SuppressWarnings("unused")
		CallableStatement callableStmt = null;
		ResultSet resultset = null;
		ArrayList<TenureModificationDetailsBean> List = new ArrayList<TenureModificationDetailsBean>();

		BigDecimal bg;
		try {
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			CallableStatement callableStatement = (CallableStatement) con
					.prepareCall("{ call PROC_GET_TENURE_RETURN_DTLS(?,?,?) } ");
			callableStatement.setString(1, loginUserMemId);
			callableStatement.registerOutParameter(2, Types.VARCHAR);
			callableStatement.registerOutParameter(3, OracleTypes.CURSOR);
			callableStatement.execute();
			resultset = (ResultSet) callableStatement.getObject(3);
			while (resultset.next()) {
				Bean = new TenureModificationDetailsBean();

				Bean.setCgpan((resultset.getString(1)));
				Bean.setTenure((resultset.getString(2)));
				Bean.setReviseTenure((resultset.getString(3)));
				Bean.setReviseExpirydate((resultset.getString(4)));
				Bean.setModificationRemark((resultset.getString(5)));
				Bean.setStatus((resultset.getString(6)));
				Bean.setReturnRemark((resultset.getString(7)));
				Bean.settId((resultset.getInt(8)));
				List.add(Bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return List;
		
	}

	@Override
	public TenureModificationDetailsBean getTenureDetailsForUpdate(int t_ID) {
		// TODO Auto-generated method stub
		TenureModificationDetailsBean Bean = null;
		// System.out.println("getSaveClaimLodgmentData method called to procedure inside DAOImpl==");
		Session session3 = sessionFactory.openSession();
		Transaction tn = session3.beginTransaction();
		Connection con = session3.connection();
		CallableStatement callableStmt = null;
		ResultSet resultset = null;
		List<TenureModificationDetailsBean> dataList = new ArrayList<TenureModificationDetailsBean>();
		DecimalFormat formatter;
		BigDecimal bg;
		try {
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			CallableStatement callableStatement = (CallableStatement) con
					.prepareCall("{ call proc_getTenureDetailForModify(?,?,?) } ");
			callableStatement.setInt(1, t_ID);
			callableStatement.registerOutParameter(2, Types.VARCHAR);

			callableStatement.registerOutParameter(3, OracleTypes.CURSOR);
			callableStatement.execute();
			resultset = (ResultSet) callableStatement.getObject(3);
			boolean empty = true;
			while (resultset.next()) {
				empty = false;
				Bean = new TenureModificationDetailsBean();
				Bean.setCgpan((resultset.getString("CGPAN")));
				Bean.setBankName((resultset.getString("MEM_BANK_NAME")));
				Bean.setMliID((resultset.getString("MLIID")));
				Bean.setTenure((resultset.getString("TENOR_IN_MONTHS")));
				Bean.setState((resultset.getString("MEM_STATE_NAME")));
				Bean.setGstNo(resultset.getString("GSTIN_NO"));
				Bean.setMseName((resultset.getString("MSE_NAME")));
				Bean.setExpiryDate((resultset.getString(("EXPIRY_DATE"))));
				Bean.setReviseTenure((resultset.getString("REVISED_TENURE")));
				Bean.setReviseExpirydate((resultset
						.getString(("REVISED_EXPIRY_DATE"))));
				Bean.setModificationRemark((resultset
						.getString("MODIFICATION_REMARKS")));
				Bean.settId((resultset.getInt("TENURE_ID")));
				Bean.setAccStandard(resultset.getString("ISACC_STANDARD"));
				Bean.setBankAuthority(resultset.getString("ISBANK_AUTHORITY"));
				Bean.setNoIntrest(resultset.getString("ISNO_INTREST"));
				//Bean.setAccStandard();
				
				Bean.setFirstDisburseDate((resultset.getString("FIRST_DISBURSEMENT_DATE")));

			}
		} catch (Exception e) {
			e.printStackTrace();
	
		}finally{
			
			//callableStatement.close();
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Bean;
	}

	@Override
	public int updateTenureDataDetails(TenureModificationDetailsBean bean,
			int tenId, String userId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		Connection conn = session.connection();
		CallableStatement callStmt = null;
		int dataSaved = 0;
		try {
			if (bean != null) {
				Date sysdate = new Date();
				ResultSet resultSet = null;
				String exception = "";

				callStmt = conn
						.prepareCall("{call proc_update_tenure(?,?,?,?,?,?,?,?)}");
				callStmt.setInt(1, tenId);

				callStmt.setString(2, bean.getReviseTenure());

				callStmt.setString(3, bean.getReviseExpirydate());

				callStmt.setString(4, userId);
				callStmt.setString(5, bean.getModificationRemark());
				callStmt.setString(6, bean.getAccStandard());
				callStmt.setString(7, bean.getBankAuthority());
				callStmt.setString(8, bean.getNoIntrest());


				dataSaved = callStmt.executeUpdate();
				System.out.println("Record Updated==" + dataSaved);
				conn.close();
				callStmt.close();
				return dataSaved;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dataSaved;
	}
}
