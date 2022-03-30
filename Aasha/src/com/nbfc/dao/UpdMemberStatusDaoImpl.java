package com.nbfc.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nbfc.bean.UpdMemberStatusBean;
import com.raistudies.domain.CustomExceptionHandler;

@Repository("updMemStatusDao")
public class UpdMemberStatusDaoImpl implements UpdMemberStatusDao{

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<UpdMemberStatusBean> getBankMandateDetailsFromCGTMSE() {
		// TODO Auto-generated method stub
		UpdMemberStatusBean objMemberStatusBean=null;		
		System.out.println("getBankMandateDetailsFromCGTMSE method called to procedure inside DAOImpl==");
		Session session3 = sessionFactory.openSession();
		Transaction tn = session3.beginTransaction();
		Connection con = session3.connection();
		CallableStatement callStmt = null;
		ResultSet resultset = null;
		ArrayList<UpdMemberStatusBean> memberStatusArrayListObj = new ArrayList<UpdMemberStatusBean>();
		try {
			callStmt = (CallableStatement)con.prepareCall("{ call PROC_GET_BMDtls_BYCGTMSE(?) } ");
			callStmt.registerOutParameter(1, OracleTypes.CURSOR);
			callStmt.execute(); 
			resultset=(ResultSet) callStmt.getObject(1);
			while (resultset.next()) {
				objMemberStatusBean = new UpdMemberStatusBean();

				if(resultset.getString("ROWNUM") != null){
					objMemberStatusBean.setROWNUM(resultset.getString("ROWNUM"));
				}else{
					objMemberStatusBean.setROWNUM(null);
				}
				
				if(resultset.getString("MLI_NAME") != null){
					objMemberStatusBean.setMliName(resultset.getString("MLI_NAME"));
				}else{
					objMemberStatusBean.setMliName(null);
				}
				if(resultset.getString("MEM_ID") != null){
					objMemberStatusBean.setMemberId(resultset.getString("MEM_ID"));
				}else{
					objMemberStatusBean.setMemberId(null);
				}

				if(resultset.getString("MEM_CONT_NO") != null){
					objMemberStatusBean.setContactNo(resultset.getString("MEM_CONT_NO"));
				}else{
					objMemberStatusBean.setContactNo(null);
				}
				if(resultset.getString("MEM_MOB_NO") != null){
					objMemberStatusBean.setMobileNo(resultset.getString("MEM_MOB_NO"));
				}else{
					objMemberStatusBean.setMobileNo(null);
				}

				if(resultset.getString("MEM_EMAIL_ID") != null){
					objMemberStatusBean.setEmailId(resultset.getString("MEM_EMAIL_ID"));
				}else{
					objMemberStatusBean.setEmailId(null);
				}
				if(resultset.getString("MLI_BENEFICIARY_BANK_NAME") != null){
					objMemberStatusBean.setMliBeneficiaryBankName(resultset.getString("MLI_BENEFICIARY_BANK_NAME"));
				}else{
					objMemberStatusBean.setMliBeneficiaryBankName(null);
				}
				if(resultset.getString("MLI_BENEFICIARY_NAME") != null){
					objMemberStatusBean.setMliBeneficiaryName(resultset.getString("MLI_BENEFICIARY_NAME"));
				}else{
					objMemberStatusBean.setMliBeneficiaryName(null);
				}
				if(resultset.getString("MEM_BRN_NAME") != null){
					objMemberStatusBean.setBranchName(resultset.getString("MEM_BRN_NAME"));
				}else{
					objMemberStatusBean.setBranchName(null);
				}
				if(resultset.getString("MEM_ACC_TYPE") != null){
					objMemberStatusBean.setAccountType(resultset.getString("MEM_ACC_TYPE"));
				}else{
					objMemberStatusBean.setAccountType(null);
				}

				if(resultset.getString("MEM_BRN_CODE") != null){
					objMemberStatusBean.setBranchCode(resultset.getString("MEM_BRN_CODE"));
				}else{
					objMemberStatusBean.setBranchCode(null);
				}
				if(resultset.getString("MEM_MICR_CODE") != null){
					objMemberStatusBean.setMicrCode(resultset.getString("MEM_MICR_CODE"));
				}else{
					objMemberStatusBean.setMicrCode(null);
				}
				if(resultset.getString("MEM_ACC_NO") != null){
					objMemberStatusBean.setAccountNo(resultset.getString("MEM_ACC_NO"));
				}else{
					objMemberStatusBean.setAccountNo(null);
				}
				if(resultset.getString("MEM_IFSC_CODE") != null){
					objMemberStatusBean.setIfscCode(resultset.getString("MEM_IFSC_CODE"));
				}else{
					objMemberStatusBean.setIfscCode(null);
				}
				if(resultset.getString("MEM_RTGS_NO") != null){
					objMemberStatusBean.setRtgsNo(resultset.getString("MEM_RTGS_NO"));
				}else{
					objMemberStatusBean.setRtgsNo(null);
				}
				if(resultset.getString("MEM_STATUS") != null){
					objMemberStatusBean.setStatus(resultset.getString("MEM_STATUS"));
				}else{
					objMemberStatusBean.setStatus(null);
				}
				if(resultset.getString("NBFC_MAKER_REMARK") != null){
					objMemberStatusBean.setNbfcMakerRemarks(resultset.getString("NBFC_MAKER_REMARK"));
				}else{
					objMemberStatusBean.setNbfcMakerRemarks(null);
				}
				if(resultset.getString("NBFC_CHECKER_REMARK") != null){
					objMemberStatusBean.setNbfcCheckerRemarks(resultset.getString("NBFC_CHECKER_REMARK"));
				}else{
					objMemberStatusBean.setNbfcCheckerRemarks(null);
				}
				if(resultset.getString("CGTMSE_CHECKER_REMARK") != null){
					objMemberStatusBean.setNbfcCheckerRemarks(resultset.getString("CGTMSE_CHECKER_REMARK"));
				}else{
					objMemberStatusBean.setNbfcCheckerRemarks(null);
				}
				memberStatusArrayListObj.add(objMemberStatusBean);
			}		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session3.close();
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return  memberStatusArrayListObj;		
	}




	@Override
	public String updBankMandateStausFromCGTMSE(String memberId,String loginUserId,String remarks) {

		System.out.println("updBankMandateStausFromCGTMSE method called memberId::"+memberId+" ,loginUserId::"+loginUserId);
		Session session = sessionFactory.openSession();
		Transaction tn = session.beginTransaction();
		Connection conn = session.connection();
		CallableStatement ct = null;
		int functionReturnValue = 0;
		try {		   
			ct =conn.prepareCall("{?=call FUNUPDATEBYCHECKAR(?, ?, ?, ?)}");
			ct.registerOutParameter(1, Types.INTEGER);
			ct.setString(2,memberId);
			ct.setString(3,loginUserId);
			ct.setString(4,remarks);
			ct.registerOutParameter(5, Types.VARCHAR);
			ct.execute();

			functionReturnValue = ct.getInt(1);
			System.out.println("functionReturnValue :::"+functionReturnValue);
			if (functionReturnValue == 1) {

				String error = ct.getString(5); 
				System.out.println(error);
				ct.close(); 
				ct =  null;		  
				conn.rollback();		  
				throw new CustomExceptionHandler(error);

			}
			ct.close(); 
			ct = null;		  
			conn.commit();

		} catch (Exception exception) {
			exception.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException ignore) {
				throw new CustomExceptionHandler(ignore.getMessage());
			}

			throw new CustomExceptionHandler(exception.getMessage());
		} finally {
			session.close();

		}

		return  ""+functionReturnValue;		
	}


}
