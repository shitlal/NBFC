package com.nbfc.dao;

import java.math.BigDecimal;
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

import oracle.jdbc.OracleTypes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nbfc.bean.ApplicationStatusDetailsBean;
import com.raistudies.domain.CustomExceptionHandler;

@Repository("ApplicationStatusDao")
public class ApplicationStatusDaoImpl implements ApplicationStatusDao {
	@Autowired
	SessionFactory sessionFactory;
	ResultSet resultset = null;
	ResultSetMetaData resultSetMetaData = null;
	public List<Map<String, Object>> getApplicationStatus(String userId,Date toDate,Date fromDate,String status){

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		try {
			Session session4 = sessionFactory.openSession();
			/* Transaction tn = session4.beginTransaction(); */
			Connection conn = session4.connection();
			CallableStatement cs = conn
					.prepareCall("{? = call NBFC_PACKREPORT.Fun_get_MLIFileUploaded_Data(?,?,?,?,?,?)}");

			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, userId);
			cs.setDate(3, new java.sql.Date(toDate.getTime()));
			cs.setDate(4, new java.sql.Date( fromDate.getTime()));
			cs.setString(5, status);
			cs.registerOutParameter(6, OracleTypes.CURSOR);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(7);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :"
						+ pouterror);
			} else {
				// Procedure execution
				resultset = (ResultSet) cs.getObject(6);
				resultSetMetaData = resultset.getMetaData();
				int coulmnCount = resultSetMetaData.getColumnCount();
				while (resultset.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
					for (int i = 1; i <= coulmnCount; i++) {
						columns.put(resultSetMetaData.getColumnLabel(i),
								resultset.getObject(i));
					}
					rows.add(columns);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;

	
		
	}
	@Override
	public List<Map<String, Object>> getFileData(String FileId) {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		try {
			Session session4 = sessionFactory.openSession();
			/* Transaction tn = session4.beginTransaction(); */
			Connection conn = session4.connection();
			CallableStatement cs = conn
					.prepareCall("{? = call NBFC_PACKREPORT.Fun_get_FileUploaded_Data(?,?,?)}");

			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, FileId);
			cs.registerOutParameter(3, OracleTypes.CURSOR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(4);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :"
						+ pouterror);
			} else {
				// Procedure execution

				resultset = (ResultSet) cs.getObject(3);
				resultSetMetaData = resultset.getMetaData();
				int coulmnCount = resultSetMetaData.getColumnCount();
				while (resultset.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();

					for (int i = 1; i <= coulmnCount; i++) {
						columns.put(resultSetMetaData.getColumnLabel(i),
								resultset.getObject(i));
					}

					rows.add(columns);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;

	}
	@Override
	public ApplicationStatusDetailsBean getapplicationDetails(String FileId) {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		ApplicationStatusDetailsBean bean = null;
		try {
			
			Session session4 = sessionFactory.openSession();
			/* Transaction tn = session4.beginTransaction(); */
			Connection conn = session4.connection();
			CallableStatement cs = conn
					.prepareCall("{? = call NBFC_PACKREPORT.Fun_get_LoanAccoutNo_Data(?,?,?)}");

			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, FileId);
			cs.registerOutParameter(3, OracleTypes.CURSOR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(4);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :"
						+ pouterror);
			} else {
				// Procedure execution

				resultset = (ResultSet) cs.getObject(3);
				resultSetMetaData = resultset.getMetaData();
				//int coulmnCount = resultSetMetaData.getColumnCount();
				while (resultset.next()) {
					bean = new ApplicationStatusDetailsBean();
					bean.setLONE_TYPE(resultset.getString(1));
					bean.setPORTFOLIO_NO(resultset.getString(2));
					
					bean.setLOAN_ACCOUNT_NO(resultset.getString(4));
					bean.setCONSTITUTION(resultset.getString(5));
					bean.setMSE_NAME(resultset.getString(6));
					bean.setSANCTION_DATE(resultset.getString(7)); 
					bean.setSANCTIONED_AMOUNT(resultset.getString(8));  //
					bean.setFIRST_DISBURSEMENT_DATE(resultset.getString(9));
					bean.setBIG_INTEREST_RATE(resultset.getBigDecimal(10).setScale(2,BigDecimal.ROUND_UP));
					System.out.println("INTEREST_RATE::::::::::"+bean.getBIG_INTEREST_RATE());
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
					bean.setBIG_GUARANTEE_AMOUNT(resultset.getBigDecimal(39).setScale(2,BigDecimal.ROUND_UP));
					System.out.println("GUARANTEE_AMOUNT::::::::::"+bean.getBIG_GUARANTEE_AMOUNT());
					bean.setCOLLETRAL_SECURITY_AMOUNT(resultset.getString(40));
					bean.setRETAIL_TRADE(resultset.getString(41));
					bean.setAADHAR_(resultset.getString(42));
					bean.setCGPAN(resultset.getString(43));
					bean.setBIG_OUTSTANDING_AMOUNT(resultset.getBigDecimal(44).setScale(2,BigDecimal.ROUND_UP));
					System.out.println("OUTSTANDING_AMOUNT::::::::::"+bean.getBIG_OUTSTANDING_AMOUNT());
					bean.setMEMBER_ID(resultset.getString(45)+resultset.getString(46)+resultset.getString(47));
					//bean.setMEM_BNK_ID(resultset.getString(76));
					bean.setREMARKS(resultset.getString(48));
					bean.setStatus(resultset.getString(49));
					//bean.setMEM_ZNE_ID(resultset.getString(77));
					//bean.setMEM_BRN_ID(resultset.getString(78));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;

	}
	@Override
	public ArrayList<List> getGETuploadedFileData(
			String fileId) {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		ArrayList<List> DataHV = new ArrayList<List>();
		ArrayList<String> Header = new ArrayList<String>();
		ArrayList<List> Data1 = new ArrayList<List>();
		ApplicationStatusDetailsBean bean = null;
		try {
		
			Session session4 = sessionFactory.openSession();
			/* Transaction tn = session4.beginTransaction(); */
			Connection conn = session4.connection();
			CallableStatement cs = conn
					.prepareCall("{? = call NBFC_PACKREPORT.Fun_FILE_DATA(?,?,?)}");

			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, fileId);
			cs.registerOutParameter(3, OracleTypes.CURSOR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(4);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :"+ pouterror);
			}else{
				 // Procedure execution
				    resultset = (ResultSet) cs.getObject(3);
					resultSetMetaData = resultset.getMetaData();
					int coulmnCount = resultSetMetaData.getColumnCount();
					System.out.println("coulmnCount=="+coulmnCount);
					
					
					
					
					//####
					
					
					
					
					int row_counter=1;
					while(resultset.next()){
						if(row_counter==1){
							
								for(int i=1 ; i<=coulmnCount; i++){
									
										Header.add(resultSetMetaData.getColumnLabel(i));
								}
						}
						ArrayList<String> Data = new ArrayList<String>();
						for(int n=1;n<=coulmnCount;n++){
							
							Data.add(resultset.getString(n));
							
					    }
						Data1.add(Data);
						
						
						
						
						row_counter++;
					}
					DataHV.add(Header);
					DataHV.add(Data1);
					
					
					//####
					
					
				/*	while (resultset.next()) {
						
						
						
					    Map<String, Object> columns = new LinkedHashMap<String, Object>();

					    for (int i = 1; i <= coulmnCount; i++) {
					        columns.put(resultSetMetaData.getColumnLabel(i), resultset.getObject(i));
					    }

					    rows.add(columns);
					}*/
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return DataHV;

	}
	
}
