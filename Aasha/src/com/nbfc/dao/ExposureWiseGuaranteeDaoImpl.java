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

import oracle.jdbc.OracleTypes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nbfc.bean.GeneralReport;
import com.raistudies.domain.CustomExceptionHandler;
@Repository("ExposureWiseGuaranteeDao")
public class ExposureWiseGuaranteeDaoImpl implements ExposureWiseGuaranteeDao{

	
	@Autowired
	SessionFactory sessionFactory;
	ResultSet resultset = null;
	ResultSetMetaData resultSetMetaData = null;
	
	@Override
	public List<Map<String, Object>> getExposureGuaranteeData(String userId,String role, Date toDate, Date fromDate) {
		//public List<Map<String, Object>> getExposureGuaranteeData(String userId,String role, Date toDate, Date fromDate) {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Session session4 = sessionFactory.openSession();
		Transaction tn = session4.beginTransaction(); 
		Connection conn = session4.connection();
		try {
			
			CallableStatement cs = conn
					.prepareCall("{? = call MIS_REPORT.MIS_EXPOSURE_DATA_REPORT(?,?,?,?,?,?)}");

			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, userId);
			cs.setString(3, role);
			cs.setDate(4, new java.sql.Date(toDate.getTime()));
			cs.setDate(5, new java.sql.Date( fromDate.getTime()));
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

		} catch (SQLException exception) {
			try {
				conn.rollback();
			} catch (SQLException ignore) {
				throw new CustomExceptionHandler(ignore.getMessage());
			}

			throw new CustomExceptionHandler(exception.getMessage());
		}
		finally {
			session4.close();

		}
		return rows;

	}

	@Override
	public List<Map<String, Object>> getMliExposureDataByMLIName(String userId,
			String EXPOSURE_ID, String role, Date toDate, Date fromDate) {

		
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Session session4 = sessionFactory.openSession();
		Transaction tn = session4.beginTransaction(); 
		Connection conn = session4.connection();
		try {
			
			CallableStatement cs = conn
					.prepareCall("{? = call MIS_REPORT.MIS_EXPO_DATA_BYEXID_REPORT(?,?,?,?,?,?,?)}");

			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, userId);
			cs.setString(3, EXPOSURE_ID);
			cs.setString(4, role);
			cs.setDate(5, new java.sql.Date(toDate.getTime()));
			cs.setDate(6, new java.sql.Date( fromDate.getTime()));
		//	cs.setString(6, status);
			cs.registerOutParameter(7, OracleTypes.CURSOR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(8);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :"
						+ pouterror);
			} else {
				// Procedure execution
				resultset = (ResultSet) cs.getObject(7);
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

		} catch (SQLException exception) {
			try {
				conn.rollback();
			} catch (SQLException ignore) {
				throw new CustomExceptionHandler(ignore.getMessage());
			}

			throw new CustomExceptionHandler(exception.getMessage());
		}
		finally {
			session4.close();
		}
		return rows;
	}

	@Override
	public List<Map<String, Object>> getMliExposureDataBYPortfoioName(String userId,
			String portfolioName, String role, Date toDate, Date fromDate) {
		
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Session session4 = sessionFactory.openSession();
		Transaction tn = session4.beginTransaction(); 
		Connection conn = session4.connection();
		try {
			
			CallableStatement cs = conn
					.prepareCall("{? = call MIS_REPORT.MIS_EXPO_DATA_BYPORTFOLIO(?,?,?,?,?,?,?)}");

			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, userId);
			cs.setString(3, portfolioName);
			cs.setString(4, role);
			cs.setDate(5, new java.sql.Date(toDate.getTime()));
			cs.setDate(6, new java.sql.Date( fromDate.getTime()));
		//	cs.setString(6, status);
			cs.registerOutParameter(7, OracleTypes.CURSOR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(8);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :"
						+ pouterror);
			} else {
				// Procedure execution
				resultset = (ResultSet) cs.getObject(7);
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

		} catch (SQLException exception) {
			try {
				conn.rollback();
			} catch (SQLException ignore) {
				throw new CustomExceptionHandler(ignore.getMessage());
			}

			throw new CustomExceptionHandler(exception.getMessage());
		}
		finally {
			session4.close();
		}
		return rows;
	}

	@Override
	public List<Map<String, Object>> getMliExposureDataBYFileId(String userId,
			String fileId, String role, Date toDate, Date fromDate) {
		// TODO Auto-generated method stub

		
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Session session4 = sessionFactory.openSession();
		Transaction tn = session4.beginTransaction(); 
		Connection conn = session4.connection();
		try {
			
			CallableStatement cs = conn
					.prepareCall("{? = call MIS_REPORT.MIS_EXPO_DATA_BYFILEID(?,?,?,?,?,?,?)}");

			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, userId);
			cs.setString(3, fileId);
			cs.setString(4, role);
			cs.setDate(5, new java.sql.Date(toDate.getTime()));
			cs.setDate(6, new java.sql.Date( fromDate.getTime()));
		//	cs.setString(6, status);
			cs.registerOutParameter(7, OracleTypes.CURSOR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(8);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :"
						+ pouterror);
			} else {
				// Procedure execution
				resultset = (ResultSet) cs.getObject(7);
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

		} catch (SQLException exception) {
			try {
				conn.rollback();
			} catch (SQLException ignore) {
				throw new CustomExceptionHandler(ignore.getMessage());
			}

			throw new CustomExceptionHandler(exception.getMessage());
		}
		finally {
			session4.close();
		}
		return rows;
	
	}

	/*@Override
	public List<Map<String, Object>> getExcelDataBYPortfoioName(String userId,
			String excelPortfolioName, String role, Date toDate, Date fromDate) {

		
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Session session4 = sessionFactory.openSession();
		Transaction tn = session4.beginTransaction(); 
		Connection conn = session4.connection();
		try {
			
			CallableStatement cs = conn
					.prepareCall("{? = call MIS_REPORT.MIS_EXPO_BYPORTFOLIODATA(?,?,?,?,?,?,?)}");

			// register input parameters
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, userId);
			cs.setString(3, excelPortfolioName);
			cs.setString(4, role);
			cs.setDate(5, new java.sql.Date(toDate.getTime()));
			cs.setDate(6, new java.sql.Date( fromDate.getTime()));
		//	cs.setString(6, status);
			cs.registerOutParameter(7, OracleTypes.CURSOR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(8);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :"
						+ pouterror);
			} else {
				// Procedure execution
				resultset = (ResultSet) cs.getObject(7);
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

		} catch (SQLException exception) {
			try {
				conn.rollback();
			} catch (SQLException ignore) {
				throw new CustomExceptionHandler(ignore.getMessage());
			}

			throw new CustomExceptionHandler(exception.getMessage());
		}
		finally {
			session4.close();
		}
		return rows;
	
	}*/

	@Override
	public ArrayList<List> getGETuploadedFileData(String userId,String fileId,String role,Date toDate,Date fromDate) {

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		ArrayList<List> DataHV = new ArrayList<List>();
		ArrayList<String> Header = new ArrayList<String>();
		ArrayList<List> Data1 = new ArrayList<List>();
		GeneralReport bean = null;
		try {
		
			Session session4 = sessionFactory.openSession();
			/* Transaction tn = session4.beginTransaction(); */
			Connection conn = session4.connection();
			CallableStatement cs = conn
			.prepareCall("{? = call MIS_REPORT.MIS_EXPO_BYPORTFOLIODATA(?,?,?,?,?,?,?)}");

			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, userId);
			cs.setString(3, fileId);
			cs.setString(4, role);
			cs.setDate(5, new java.sql.Date(toDate.getTime()));
			cs.setDate(6, new java.sql.Date( fromDate.getTime()));
		//	cs.setString(6, status);
			cs.registerOutParameter(7, OracleTypes.CURSOR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(8);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :"
						+ pouterror);
			} else {
				// Procedure execution
				resultset = (ResultSet) cs.getObject(7);
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

	@Override
	public ArrayList<List> getExposureDataBYFileId(String userId,
			String fileId, String role, Date toDate, Date fromDate) {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		ArrayList<List> DataHV = new ArrayList<List>();
		ArrayList<String> Header = new ArrayList<String>();
		ArrayList<List> Data1 = new ArrayList<List>();
		GeneralReport bean = null;
		try {
		
			Session session4 = sessionFactory.openSession();
			/* Transaction tn = session4.beginTransaction(); */
			Connection conn = session4.connection();
			CallableStatement cs = conn
			.prepareCall("{? = call MIS_REPORT.MIS_EXPO_DATA_BYFILEID(?,?,?,?,?,?,?)}");

			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, userId);
			cs.setString(3, fileId);
			cs.setString(4, role);
			cs.setDate(5, new java.sql.Date(toDate.getTime()));
			cs.setDate(6, new java.sql.Date( fromDate.getTime()));
		//	cs.setString(6, status);
			cs.registerOutParameter(7, OracleTypes.CURSOR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			int result = cs.getInt(1);
			String pouterror = cs.getString(8);
			if (result != 0) {
				throw new CustomExceptionHandler("Exception occured  :"
						+ pouterror);
			} else {
				// Procedure execution
				resultset = (ResultSet) cs.getObject(7);
					resultSetMetaData = resultset.getMetaData();
					int coulmnCount = resultSetMetaData.getColumnCount();
					System.out.println("coulmnCount=="+coulmnCount);
					
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
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return DataHV;
	}
}
