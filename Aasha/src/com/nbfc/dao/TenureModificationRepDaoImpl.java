package com.nbfc.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import oracle.jdbc.OracleTypes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nbfc.bean.TenureModificationRepBean;

@Repository("TenureModificationRepDao")
public class TenureModificationRepDaoImpl implements TenureModificationRepDao{

	@Autowired
	SessionFactory sessionFactory;

	/*Add by VinodSingh For Tenure Modification on 12-DEC-2020 */
	@Override
	public List<TenureModificationRepBean> getTenureModificationReport(String userId, Date toDate,Date fromDate, String role, String mliIdOrName) {
		System.out.println("fromDate:::"+fromDate+" ,  toDate::"+toDate+""+" ,Role:::"+role+",mliIdOrName:::"+mliIdOrName);
		ResultSet resultset = null;		
		TenureModificationRepBean tenureModiBean = null;
		List<TenureModificationRepBean> tenureModiRepBean = new ArrayList<TenureModificationRepBean>();
		try {     
			Session session4 = sessionFactory.openSession();            
			/* Transaction tn = session4.beginTransaction(); */
			Connection conn = session4.connection();
			CallableStatement cs = conn.prepareCall("{? = call FunTenureReportDetails(?,?,?,?,?,?)}");			
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setDate(2, new java.sql.Date(fromDate.getTime()));
			cs.setDate(3, new java.sql.Date(toDate.getTime()));	
			cs.setString(4, role);
			cs.setString(5, mliIdOrName);
			cs.registerOutParameter(6, OracleTypes.CURSOR);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			System.out.println("result::"+result);
			String pouterror = cs.getString(7);
			System.out.println("pouterror::"+pouterror);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			resultset = (ResultSet) cs.getObject(6);
			
			while (resultset.next()) {
				tenureModiBean  = new TenureModificationRepBean();
				tenureModiBean.setMse_name(resultset.getString("MSE_NAME")!=null ? resultset.getString("MSE_NAME") : "");		
				tenureModiBean.setCGPAN(resultset.getString("CGPAN")!=null ? resultset.getString("CGPAN") : "");

				String garnteAmount=resultset.getString("GUARANTEE_AMOUNT")!=null ? resultset.getString("GUARANTEE_AMOUNT") : "";
				if(garnteAmount.equals(""))
					tenureModiBean.setGuarantee_amount(garnteAmount);
				else
					tenureModiBean.setGuarantee_amount(String.format("%.02f", resultset.getFloat("GUARANTEE_AMOUNT")));

				String disbursementDate=resultset.getString("FIRST_DISBURSEMENT_DATE")!=null ? resultset.getString("FIRST_DISBURSEMENT_DATE") : "";
				if(disbursementDate.equals(""))
					tenureModiBean.setFirst_disbursement_date(disbursementDate);
				else
					tenureModiBean.setFirst_disbursement_date(dateFormat.format(resultset.getDate("FIRST_DISBURSEMENT_DATE")));

				//tenureModiBean.setTenor_in_months(resultset.getString("TENOR_IN_MONTHS")!=null ? resultset.getString("TENOR_IN_MONTHS") : "");

				//String oldExpDate=resultset.getString("OLD_EXP_DATE")!=null ? resultset.getString("OLD_EXP_DATE") : "";
		//		if(oldExpDate.equals(""))
			//		tenureModiBean.setOld_exp_date(oldExpDate);
			//	else
			//		tenureModiBean.setOld_exp_date(dateFormat.format(resultset.getDate("OLD_EXP_DATE")));

				tenureModiBean.setRevised_tenure(resultset.getString("REVISED_TENURE")!=null ? resultset.getString("REVISED_TENURE") : "");

				String revisedExpDate=resultset.getString("REVISED_EXPIRY_DATE")!=null ? resultset.getString("REVISED_EXPIRY_DATE") : "";
				if(revisedExpDate.equals(""))
					tenureModiBean.setRevised_expiry_date(revisedExpDate);
				else
					tenureModiBean.setRevised_expiry_date(dateFormat.format(resultset.getDate("REVISED_EXPIRY_DATE")));

				tenureModiBean.setModification_remarks(resultset.getString("MODIFICATION_REMARKS")!=null ? resultset.getString("MODIFICATION_REMARKS") : "");

				String danAmt=resultset.getString("DAN_AMT")!=null ? resultset.getString("DAN_AMT") : "";
				if(danAmt.equals(""))
					tenureModiBean.setDan_amt("0.00");
				else
					tenureModiBean.setDan_amt(String.format("%.02f", resultset.getFloat("DAN_AMT")));
				
				
				String tenureApprovalDate=resultset.getString("TENURE_APPROVAL_DATE")!=null ? resultset.getString("TENURE_APPROVAL_DATE") : "";
				if(tenureApprovalDate.equals(""))
					tenureModiBean.setTenure_approval_date(tenureApprovalDate);
				else
					tenureModiBean.setTenure_approval_date(dateFormat.format(resultset.getDate("TENURE_APPROVAL_DATE")));

					
				tenureModiBean.setMLIID(resultset.getString("mliid")!=null ? resultset.getString("mliid") : "");
				
				tenureModiBean.setBankName(resultset.getString("MEM_BANK_NAME")!=null ? resultset.getString("MEM_BANK_NAME") : "");	
				
				tenureModiRepBean.add(tenureModiBean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tenureModiRepBean;

	}

}
