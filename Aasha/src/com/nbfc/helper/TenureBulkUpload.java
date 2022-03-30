package com.nbfc.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.web.multipart.MultipartFile;

import com.nbfc.exception.TenureBulkException;
import com.nbfc.model.UserInfo;
import com.raistudies.domain.CustomExceptionHandler;

import oracle.jdbc.OracleTypes;

// added by shashi date 17/10/2020
public class TenureBulkUpload {

	public void validateData(Connection con, ArrayList successRecord, ArrayList unsuccessRecord, String UploadId,
			String BulkName, LinkedHashMap<String, ExcelDeatailsBean> headers) throws SQLException {
		CallableStatement callableStmt1 = null;
		int status1 = -1;
		String errorCode1 = null;
		try {
			// callableStmt1 = con.prepareCall("{?=call BULKUPLOAD.Func_validate_data_" +
			// BulkName + "(?,?,?)}");
			callableStmt1 = con.prepareCall("{call TENURE_BULK_UPLOAD_STAG.PROC_VALIDATETENURE(?,?,?)}");
			// callableStmt1.registerOutParameter(1, java.sql.Types.INTEGER);
			callableStmt1.setString(1, "Admin");
			callableStmt1.setString(2, UploadId);
			callableStmt1.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableStmt1.execute();
			// status1 = callableStmt1.getInt(1);
			errorCode1 = callableStmt1.getString(3);
			/*
			 * if (status1 == 1) { // Log.log(Log.ERROR,
			 * "ClaimAction","getClaimSettlePaymentReportData()","SP // returns a 1. Error
			 * code is :" + errorCode); callableStmt1.close(); // throw new
			 * DatabaseException(errorCode);
			 * 
			 * } else if (status1 == 0) {
			 */
			System.out.println("Data inserted into staging table successfully");
			// }
			callableStmt1.close();
			callableStmt1 = null;
		} catch (SQLException sqlexception) {
			System.out.println("" + sqlexception.getStackTrace());
			System.out.println(sqlexception.getMessage());

//			 Log.log(Log.ERROR,"ClaimAction","getClaimSettlePaymentSavedMLIWiseCK2Data()","Error retrieving	 all Claim settled Payment Process Data!");
			// throw new DatabaseException(sqlexception.getMessage());
		} finally {
			// DBConnection.freeConnection(conn);
			con.commit();
		}
		// ####Validate Function####end####

		// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

		// ####Moving to main table Function####start####
		CallableStatement callableStmt2 = null;
		int status2 = -1;
		String errorCode2 = null;
		try {
			// ##
			callableStmt2 = con.prepareCall("{call  TENURE_BULK_UPLOAD_STAG.PROCMOVE_STAG_TO_MAIN_APPS (?,?,?)}");
			// callableStmt2.registerOutParameter(1, java.sql.Types.INTEGER);
			callableStmt2.setString(1, "Admin");
			callableStmt2.setString(2, UploadId);
			callableStmt2.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableStmt2.execute();
			// status2 = callableStmt2.getInt(1);
			errorCode2 = callableStmt2.getString(3);

//			if (status2 == 1) {
//				// Log.log(Log.ERROR, "ClaimAction","getClaimSettlePaymentReportData()","SP
//				// returns a 1. Error code is :" + errorCode);
//				callableStmt2.close();
//				// throw new DatabaseException(errorCode);
//
//			} else if (status2 == 0) {
			System.out.println("Data moved into main table successfully");
			// }
			callableStmt2.close();
			callableStmt2 = null;
		} catch (SQLException sqlexception) {
			System.out.println("" + sqlexception.getStackTrace());
			System.out.println(sqlexception.getMessage());
			// Log.log(Log.ERROR,
			// "ClaimAction","getClaimSettlePaymentSavedMLIWiseCK2Data()","Error retrieving
			// all Claim settled Payment Process Data!");
			// throw new DatabaseException(sqlexception.getMessage());
		} finally {
			// DBConnection.freeConnection(conn);

			con.commit();
			System.out.println("Data moved and commited into the table");
		}
		
		
		// ####Moving to main table Function####end####
//		if (BulkName.equals("Tenure")) {
//			// ####AUTO DAN GENERATION FOR ASF PROCEDURE ####start####
//			CallableStatement callableStmt12 = null;
//			int status12 = -1;
//			String errorCode12 = null;
//			try {
//				// ##
//				callableStmt12 = con.prepareCall("{call TENURE_BULK_UPLOAD_STAG.PACK_DANFORTENURE(?,?,?)}");
//				// callableStmt12.registerOutParameter(1, java.sql.Types.INTEGER);
//
//				callableStmt12.setString(1, UploadId);
//				callableStmt12.setString(2, "Admin");
//				callableStmt12.registerOutParameter(3, java.sql.Types.VARCHAR);
//				callableStmt12.execute();
//				// status12 = callableStmt12.getInt(1);
//				errorCode12 = callableStmt12.getString(3);
//
////				if (errorCode12 != null) {
////					throw new CustomExceptionHandler("Error Occured  :" + errorCode12);
////					// callableStmt12.close();
////					// throw new DatabaseException(errorCode);
////
////				} else {
//					System.out.println("UPDATE OS AMOUNT  main table successfully AND DAN GENERATED..");
//			//	}
//				callableStmt12.close();
//				callableStmt12 = null;
//			} catch (SQLException sqlexception) {
//				System.out.println(""+sqlexception.getStackTrace());
//				System.out.println(sqlexception.getMessage());
//				// Log.log(Log.ERROR,
//				// "ClaimAction","getClaimSettlePaymentSavedMLIWiseCK2Data()","Error retrieving
//				// all Claim settled Payment Process Data!");
//				// throw new DatabaseException(sqlexception.getMessage());
//			} finally {
//				// DBConnection.freeConnection(conn);
//				con.commit();
//			}
//			// ####Moving to main table Function####end####
//
//		}
		// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@3@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

		// ####Getting Successful and Unsuccessful data table Function####start####
		CallableStatement callableStmt3 = null;
		ResultSet resultset3i = null;
		ResultSet resultset3ii = null;
		int status3 = -1;
		String errorCode3 = null;
		ArrayList nestData = new ArrayList();

		// ##
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSetMetaData resultSetMetaData1 = null;
		ResultSetMetaData resultSetMetaData2 = null;
		ArrayList coulmName1 = new ArrayList();
		ArrayList coulmName2 = new ArrayList();

		ArrayList DataList1 = new ArrayList();
		ArrayList DataList2 = new ArrayList();

		ArrayList AllDataList1 = new ArrayList();
		ArrayList AllDataList2 = new ArrayList();
		// ##

		try {
			// ##
			callableStmt3 = con.prepareCall("{call TENURE_BULK_UPLOAD_STAG.PROC_get_uploaded_data_APPS(?,?,?,?)}");
			// callableStmt3.registerOutParameter(1, java.sql.Types.INTEGER);
			// callableStmt3.setString(1, "Admin");
			callableStmt3.setString(1, UploadId);
			callableStmt3.registerOutParameter(2, OracleTypes.CURSOR);
			callableStmt3.registerOutParameter(3, OracleTypes.CURSOR);
			callableStmt3.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStmt3.execute();
			// status3 = callableStmt3.getInt(1);
			errorCode3 = callableStmt3.getString(4);

			// if (status3 == 1) {
//				 Log.log(Log.ERROR, "ClaimAction","getClaimSettlePaymentReportData()","SP returns a 1. Error code is :" + errorCode);
//				callableStmt3.close();
//				 throw new DatabaseException(errorCode);
			// callableStmt3.close();

			// } else if (status3 == 0) {
			// Successful data start
			rs1 = (ResultSet) callableStmt3.getObject(2);
			resultSetMetaData1 = rs1.getMetaData();
			int coulmnCount1 = resultSetMetaData1.getColumnCount();
			for (int i = 1; i <= coulmnCount1; i++) {
				// String TempData=resultSetMetaData1.getColumnName(i);
				// System.out.println("TempData:"+TempData);
				// System.out.println("TempData Size:"+TempData.length());
				// coulmName1.add(TempData.substring(1,TempData.length()));
				coulmName1.add(resultSetMetaData1.getColumnName(i));

			}
			ArrayList<String> DisplayHeaderNameLst1 = getDisplayNameOfHeaderName(coulmName1, headers);

			System.out.println("%%%%%%%%%%%%coulmName1:" + coulmName1);
			System.out.println("%%%%%%%%%%%%DisplayHeaderNameLst:" + DisplayHeaderNameLst1);

			while (rs1.next()) {

				ArrayList columnValue1 = new ArrayList();
				for (int i = 1; i <= coulmnCount1; i++) {
					columnValue1.add(rs1.getString(i));
				}
				DataList1.add(columnValue1);
			}
			// System.out.println("list data " + nestData);
			// successRecord.add(0, coulmName1);
			successRecord.add(0, DisplayHeaderNameLst1);
			successRecord.add(1, DataList1);
			// Successful data end

			// UnSuccessful data start
			rs2 = (ResultSet) callableStmt3.getObject(3);
			resultSetMetaData2 = rs2.getMetaData();
			int coulmnCount2 = resultSetMetaData2.getColumnCount();
			for (int i = 1; i <= coulmnCount2; i++) {
				coulmName2.add(resultSetMetaData2.getColumnName(i));
			}

			ArrayList<String> DisplayHeaderNameLst2 = getDisplayNameOfHeaderName(coulmName2, headers);

			while (rs2.next()) {

				ArrayList columnValue2 = new ArrayList();
				for (int i = 1; i <= coulmnCount2; i++) {
					columnValue2.add(rs2.getString(i));
				}
				DataList2.add(columnValue2);
			}
			// System.out.println("list data " + nestData);
			// unsuccessRecord.add(0, coulmName2);
			unsuccessRecord.add(0, DisplayHeaderNameLst2);
			unsuccessRecord.add(1, DataList2);
			// UnSuccessful data end

			/*
			 * resultset3i = (ResultSet) callableStmt3.getObject(3); resultset3ii =
			 * (ResultSet) callableStmt3.getObject(4); while (resultset3i.next()) {
			 * ArrayList columnValue = new ArrayList();
			 * columnValue.add(resultset3i.getString(i));
			 * successRecord.add(resultset3i.getString(1)); nestData.add(columnValue); }
			 * while (resultset3ii.next()) { unsuccessRecord.add(resultset3ii.getString(1));
			 * }/
			 **/
			// }

			/*
			 * rs1.close(); rs1 = null;
			 * 
			 * rs2.close(); rs2 = null;
			 * 
			 * callableStmt3.close(); callableStmt3 = null; resultSetMetaData1 = null;
			 * resultSetMetaData2 = null;/
			 **/

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			System.out.println(sqlexception.getMessage());
			// Log.log(Log.ERROR,
			// "ClaimAction","getClaimSettlePaymentSavedMLIWiseCK2Data()","Error retrieving
			// all Claim settled Payment Process Data!");
			// throw new DatabaseException(sqlexception.getMessage());
		} finally {
			// DBConnection.freeConnection(conn);
			rs1.close();
			rs1 = null;

			rs2.close();
			rs2 = null;

			callableStmt3.close();
			callableStmt3 = null;
			resultSetMetaData1 = null;
			resultSetMetaData2 = null;
		}
		// ####Getting Successful and Unsuccessful data table Function####end####

	}

	// inserting the record in db
	public String prepareDataForInsertQuery(HSSFCell cell, ExcelDeatailsBean tableBeanObj) {
		String preparedValue = "''";
		if (tableBeanObj.getColumnDataType().equals("VARCHAR2")) {

			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					// Comment
					// System.out.println(tableBeanObj.getColumnName()+"prepareDataForInsertQuery:VARCHAR2[CELL_TYPE_NUMERIC]");
					// String tempcelVal=cell.toString();
					// tempcelVal=tempcelVal.substring(0,tempcelVal.length()-2);
					// preparedValue=tempcelVal.trim();
					// BigInteger BigNumData = new BigInteger(cell.getNumericCellValue());
					// Double.parseDouble(tempcelVal);
					BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
					// long val = bd;
					// preparedValue=tempcelVal;
					preparedValue = bd.toPlainString();
					// preparedValue=BigNumData;

					// System.out.println(BigNumData+" BigNumData
					// "+tableBeanObj.getColumnName()+"@@@@@@@@preparedValue@@@@@@@@@@@@@:"+tempcelVal);
					// System.out.println(bd.toPlainString()+" BigValue
					// "+tableBeanObj.getColumnName()+"@@@@@@@@preparedValue@@@@@@@@@@@@@:");

				} else {
					if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						// Comment
						// System.out.println(tableBeanObj.getColumnName()+"prepareDataForInsertQuery:VARCHAR2[CELL_TYPE_BLANK]");
						preparedValue = "''";
						// preparedValue=preparedValue.replace("''","'");
					} else {
						// Comment
						// System.out.println(tableBeanObj.getColumnName()+"~prepareDataForInsertQuery:VARCHAR2[else]~"+cell.toString());
						// preparedValue="'"+cell.toString().trim()+"'";
						preparedValue = "'" + cell.toString() + "'";
						// Comment System.out.println("@@preparedValue"+preparedValue);
						// preparedValue=preparedValue.replace("''","'");
					}
				}
			}

		} else if (tableBeanObj.getColumnDataType().equals("DATE")) {

			if (cell != null) {
				Date dateParsed1 = null;
				Date dateParsed2 = null;

				// ##
				try {
					dateParsed1 = new SimpleDateFormat("dd-MMM-yyyy").parse(cell.toString().trim());
				} catch (ParseException pex1) {

				}

				try {
					dateParsed2 = new SimpleDateFormat("dd/mm/yyyy").parse(cell.toString().trim());
				} catch (ParseException pex2) {

				}
				// Comment System.out.println("dateParsed1:"+dateParsed1);
				// Comment System.out.println("dateParsed2:"+dateParsed2);

				// ##
				if (dateParsed1 != null) {
					preparedValue = "'" + cell.toString().trim() + "'";
				}

				if (dateParsed2 != null) {
					preparedValue = "to_date('" + cell.toString().trim() + "','dd/mm/yyyy')";
				}

				// preparedValue="'"+cell.toString()+"'";
			}

		} else if (tableBeanObj.getColumnDataType().equals("NUMBER")) {

			if (cell != null) {

				if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					preparedValue = "''";
				} else {
					BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
					// long val = bd;
					// preparedValue=tempcelVal;
					preparedValue = bd.toPlainString();
					// preparedValue=cell.toString();
				}
			}
		}
		// System.out.println(tableBeanObj.getColumnName()+"$$$$$$$$$$$preparedValue.trim():$$$$$$$"+preparedValue.trim());
		return preparedValue.trim();
	}

	// for excel validation
	public String validateFieldType(HSSFCell cell, ExcelDeatailsBean tableBeanObj) {
		String isValidMsg = "INVALID";
//		System.out.println("#########validateFieldType###############");
//		System.out.println("cell Type:"+cell.getCellType());		
//		System.out.println("FieldType:"+FieldType);
//		System.out.println("###########validateFieldType#############");
		try {
			if (tableBeanObj.getColumnDataType().equals("VARCHAR2")) {

				if (cell != null) {

					// Comment System.out.println(tableBeanObj.getColumnName()+" #####IF######");

					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

						// System.out.println(tableBeanObj.getColumnName()+"##########CELL_TYPE_STRING##################");

						// System.out.println(cell.getStringCellValue().length()+"@@@@@@@@@lenthwith
						// trim@@@@@@@@@@@@@@@@@@@@@@@@trim:"+cell.getStringCellValue().trim().length()+"column:"+tableBeanObj.getColumnName()+"
						// column length:"+tableBeanObj.getColumnLength());

						if (cell.getStringCellValue().trim().length() > 0
								&& cell.getStringCellValue().trim().length() <= tableBeanObj.getColumnLength()) {

							if (cell.getStringCellValue().length() > 0
									&& cell.getStringCellValue().length() <= tableBeanObj.getColumnLength()) {

								isValidMsg = "VALID";
							} else {
								isValidMsg = tableBeanObj.getDisplayColumnName()
										+ " field length is more. The expected length is "
										+ tableBeanObj.getColumnLength();
							}
							// isValidMsg = true;
						} else {
							isValidMsg = tableBeanObj.getDisplayColumnName()
									+ " field length is more. The expected length is " + tableBeanObj.getColumnLength();
						}
					}

					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						System.out.println(
								tableBeanObj.getColumnName() + "##########CELL_TYPE_NUMERIC##################");
						// Number here comes as 400068.0 instead 400068
						String val = "" + cell.getNumericCellValue();
						// System.out.println("1##################numval:"+val);
						if (val.contains(".0")) {
							val = val.substring(0, val.length() - 2);
						} else {
							BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
							val = bd.toPlainString();
						}
						// System.out.println("2##################val:"+val);
						// String dd = String.valueOf(cell.getNumericCellValue());
						// System.out.println(val+" val.length() :"+val.length());

						if (val.length() > 0 && val.length() <= tableBeanObj.getColumnLength()) {
							// System.out.println("entered length is"+val.length());
							// System.out.println("Inner----isValid");
							isValidMsg = "VALID";
						} else {
							isValidMsg = tableBeanObj.getDisplayColumnName()
									+ " field length is more. The expected length is " + tableBeanObj.getColumnLength();
						}
					}

					if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						// System.out.println(tableBeanObj.getColumnName()+"##########CELL_TYPE_BLANK##################");
						if (tableBeanObj.getColumnAllowNullFlag().equals("Y")) {
							isValidMsg = "VALID";
						} else {
							isValidMsg = tableBeanObj.getDisplayColumnName() + " cannot be blank/empty.";
							;
						}
					}
				} else {
					// Comment System.out.println(tableBeanObj.getColumnName()+"####ELSE#####");
					if (tableBeanObj.getColumnAllowNullFlag().equals("Y")) {
						isValidMsg = "VALID";
					} else {
						isValidMsg = tableBeanObj.getDisplayColumnName() + " cannot be blank/empty.";
					}
				}
			}

			if (tableBeanObj.getColumnDataType().equals("NUMBER")) {
				if (cell != null) {

					// System.out.println(tableBeanObj.getColumnName()+"#####NUMBER#####CELL_TYPE_NUMERIC##################"+cell.getCellType());

					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

						int precision = tableBeanObj.getColumnPrecision() - tableBeanObj.getColumnScale();

						if (precision > 0) {
							Double cval = cell.getNumericCellValue();
							BigDecimal bd = new BigDecimal(cval.toString());
							long lonVal = bd.longValue();
							// System.out.println(lonVal);
							System.out.println("The expo numeric val is" + lonVal);
							// String cellval = ""+cell.getNumericCellValue();
							String cellval = "" + lonVal;
							System.out.println("The cell value  is " + cellval);
							int charcnt = 0;
							if (cellval.contains(".")) {
								System.out.println("Inside contains");
								for (char c : cellval.toCharArray())
									if (!("" + c).equals("."))
										charcnt++;
									else
										break;
								System.out.println("The charcnt is" + charcnt);
								if (charcnt > precision) {
									System.out.println("inside if of charcnt>2");
									isValidMsg = tableBeanObj.getDisplayColumnName() + " should be of " + precision
											+ " digits and upto " + tableBeanObj.getColumnScale() + " decimal places";
								} else {
									System.out.println("inside else of charcnt>2");
									isValidMsg = "VALID";
								}

							} else {
								System.out.println("inside else of contains");
								if (cellval.length() > precision) {
									isValidMsg = tableBeanObj.getDisplayColumnName() + " should be of " + precision
											+ " digits and upto " + tableBeanObj.getColumnScale() + " decimal places";
								} else
									isValidMsg = "VALID";
							}
						} else {
							System.out.println("inside else of precision>0");

							isValidMsg = "VALID";
						}
					}

					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						System.out.println(tableBeanObj.getColumnName() + " " + "CELL_TYPE_STRING ");
						isValidMsg = tableBeanObj.getDisplayColumnName() + " should be NUMBER and not TEXT";
					}

					if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						// System.out.println(tableBeanObj.getColumnName()+"#####NUMBER#####CELL_TYPE_BLANK######IF############");

						if (tableBeanObj.getColumnAllowNullFlag().equals("Y")) {
							isValidMsg = "VALID";
						} else {
							// System.out.println(tableBeanObj.getColumnName()+"#####NUMBER#####CELL_TYPE_BLANK##ELSE################");
							isValidMsg = tableBeanObj.getDisplayColumnName() + " cannot be blank/empty.";
						}
					}
				} else {
					// Comment
					// System.out.println(tableBeanObj.getColumnName()+"#####NUMBER#####ELSE##################");
					if (tableBeanObj.getColumnAllowNullFlag().equals("Y")) {
						isValidMsg = "VALID";
					} else {
						isValidMsg = tableBeanObj.getDisplayColumnName() + " cannot be blank/empty.";
					}
				}
			}

			if (tableBeanObj.getColumnDataType().equals("DATE")) {
				// Comment
				// System.out.println(tableBeanObj.getColumnName()+"#####DATE#####DATE##################");
				// Comment System.out.println("!!!!!!!!!!!!!!!!!! cell:"+cell);
				if (cell != null) {
					// Comment System.out.println("**********************************cell
					// type:"+cell.getCellType());
					Date date = null;
					// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					// #
					DateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy"); // for parsing input
					DateFormat df3 = new SimpleDateFormat("dd/MM/yyyy"); // for parsing input
					// DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy"); // for formatting output
					/*
					 * String inputDate = "30-mar-2016"; Date d = df1.parse(inputDate); String
					 * outputDate = df2.format(d);
					 */
					// #
					// format.setLenient(false);
					df1.setLenient(false);
					df3.setLenient(false);

					try {
						Date data_dt = df1.parse(cell.toString());
						// String output_data_dt = df2.format(data_dt);
						isValidMsg = "VALID";
					} catch (ParseException e) {

						// Comment System.out.println("e :"+e);
						// Comment System.out.println("cell type:"+cell.getCellType());
						// Comment
						// System.out.println("cell.toString().length()::"+cell.toString().length());

						try {

							Date data_dt = df3.parse(cell.toString());
							// String output_data_dt = df2.format(data_dt);
							isValidMsg = "VALID";
						} catch (ParseException inner_e) {

							System.out.println("inner e :" + e);
							isValidMsg = tableBeanObj.getDisplayColumnName()
									+ " should of format dd/mm/yyyy or dd-MMM-yyyy";
						}

						// date=null;

					}
					if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						if (tableBeanObj.getColumnAllowNullFlag().equals("Y")) {
							isValidMsg = "VALID";
						} else {
							isValidMsg = tableBeanObj.getDisplayColumnName() + " cannot be blank/empty.";
						}
					}
				} else {

					if (tableBeanObj.getColumnAllowNullFlag().equals("Y")) {
						isValidMsg = "VALID";
					} else {
						isValidMsg = tableBeanObj.getDisplayColumnName() + " cannot be blank/empty.";
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
		System.out.println("Column Name:" + tableBeanObj.getColumnName() + " isValid :" + isValidMsg);
		return isValidMsg;
	}

	public LinkedHashMap<String, ExcelDeatailsBean> getTableHeaderData(Connection conn, String TableName,
			String BulkName) throws ParseException, TenureBulkException {
		// Log.log(Log.INFO, "ClaimAction", "getTableHeaderData()","Entered!");
		System.out.println("##Connection##" + conn);
		System.out.println("##TableName##" + TableName);
		System.out.println("##BulkName##" + BulkName);
		CallableStatement callableStmt = null;
		ResultSet resultset = null;
		int status = -1;
		String errorCode = null;
		LinkedHashMap<String, ExcelDeatailsBean> TableHeaderDataDetail = new LinkedHashMap<String, ExcelDeatailsBean>();
		try {
			// callableStmt = conn.prepareCall("{?=call
			// NBFC_BULK_TENURE_UPLOAD_DETAIL.Func_header_tenure_data(?,?,?,?,?)}");
			callableStmt = conn
					.prepareCall("{?=call NBFC.TENURE_BULK_UPLOAD_STAG.Func_header_tenure_data_Apps(?,?,?,?,?)}");

			callableStmt.registerOutParameter(1, java.sql.Types.INTEGER);
			callableStmt.setString(2, "Admin");
			callableStmt.setString(3, TableName);
			callableStmt.registerOutParameter(4, OracleTypes.CURSOR);
			callableStmt.registerOutParameter(5, java.sql.Types.NUMERIC);
			callableStmt.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStmt.execute();
			status = callableStmt.getInt(1);
			errorCode = callableStmt.getString(6);

			if (status == 1) {
				callableStmt.close();
				System.out.println("errorCode:" + errorCode);
				throw new TenureBulkException(errorCode);
			} else if (status == 0) {
				resultset = (ResultSet) callableStmt.getObject(4);
				int max_tab_id = callableStmt.getInt(5);
				// String DisplayColName = callableStmt.getString(6);
				// System.out.println("##getTableHeaderData## max_tab_id:"+max_tab_id);
				// System.out.println("##getTableHeaderData## firstColName:"+firstColName);
				while (resultset.next()) {
					ExcelDeatailsBean tableBeanObj = new ExcelDeatailsBean();
					tableBeanObj.setColumnName(resultset.getString("column_name"));
					tableBeanObj.setColumnDataType(resultset.getString("data_type"));
					tableBeanObj.setColumnLength(resultset.getInt("data_length"));
					tableBeanObj.setColumnAllowNullFlag(resultset.getString("NULLABLE"));
					tableBeanObj.setMax_table_id(max_tab_id);
					tableBeanObj.setDisplayColumnName(resultset.getString("DISPLAY_NAME"));
					TableHeaderDataDetail.put(resultset.getString("column_name"), tableBeanObj);
				}
			}
			resultset.close();
			resultset = null;
			callableStmt.close();
			callableStmt = null;
		} catch (SQLException sqlexception) {
			// Log.log(Log.ERROR,
			// "ClaimAction","getClaimSettlePaymentSavedMLIWiseCK2Data()","Error retrieving
			// all Claim settled Payment Process Data!");
			System.out.println("sqlexception :" + sqlexception.toString());
			throw new TenureBulkException(sqlexception.getMessage());

		} finally {
			// DBConnection.freeConnection(conn);
		}
		return TableHeaderDataDetail;
	}
// test and move forward

	public static void main(String[] args) {

	}

	public HashMap<String, ArrayList<String>> CheckExcelData(MultipartFile file,
			LinkedHashMap<String, ExcelDeatailsBean> headers, String TableName, Connection conn, UserInfo UserObj,
			String BulkName, Connection con) throws SQLException, IOException {
		HashMap UploadedStatus = null;
		UploadedStatus = new HashMap();

		ArrayList validapps = new ArrayList();
		ArrayList invalidapps = new ArrayList();
		FileInputStream fis = null;
		InputStream is = null;
		Statement Stat = null;
		Stat = con.createStatement();
		HashMap<String, String> Excelheaders = new HashMap<String, String>();
		HashMap<String, String> UnMatchheaders = new HashMap<String, String>();

		String ColumnNames = "";
		boolean setFirstColumFlag = false;
		int max_table_id = 0;
		System.out.println("##headers Size##:" + headers.size());
		System.out.println("##headers## :" + headers);
		int p = 0;
		// ##BATCH_ID##
		String pattern = "ddMMMyyyyhhmmss";
		String BatchId = new SimpleDateFormat(pattern).format(new Date());
		System.out.println("##BatchId## :" + BatchId);
		// ############

		// ##
		LinkedHashMap<String, String> TemplateHeaderDisplayName = new LinkedHashMap<String, String>();
		// ##

		for (Entry<String, ExcelDeatailsBean> entry : headers.entrySet()) {
			ColumnNames = ColumnNames + "," + entry.getKey();
			ExcelDeatailsBean tabledetailbean = new ExcelDeatailsBean();
			tabledetailbean = entry.getValue();
			TemplateHeaderDisplayName.put(tabledetailbean.getDisplayColumnName(), tabledetailbean.getColumnName());

		}

		ColumnNames = ColumnNames.substring(1);
		// String FirstColumn="";
		int columnCnt = 0;
		columnCnt = headers.size();
		// System.out.println("columnCnt:"+columnCnt);

		Boolean QueryAddedFlag = false;

		try {
			is = file.getInputStream();

			// is = new
			// FileInputStream("D:\\GenerateFiles\\UTR_NO_EXCEL_UPLOAD_FORMAT1.xls");
			// is = new
			// FileInputStream("D:\\GenerateFiles\\SBI_NEED2UPLOAD.xls");
			// is = new
			// FileInputStream("D:\\GenerateFiles\\ApplLodgementBulkUploadTemplate.xls");
			// is = new FileInputStream("D:\\GenerateFiles\\boi 2nd lot.xls");
			// is = new
			// FileInputStream("D:\\GenerateFiles\\CopyofAppl10LacTemplate2.xls");
			// is = new
			// FileInputStream("D:\\GenerateFiles\\Appl10LacTemplate.xls");
			// is = new FileInputStream("D:\\GenerateFiles\\BULKUPLOADNEW.xls");

			HSSFWorkbook book;
			book = new HSSFWorkbook(is);
			HSSFSheet sheet = book.getSheetAt(0);
			int ExcelNoOfColumns = sheet.getRow(0).getLastCellNum();

			int excelRowCnt = 0;
			excelRowCnt = sheet.getLastRowNum();
			// System.out.println(sheet.getSheetName()+"\t sheet
			// row"+sheet.getLastRowNum());
			Iterator rowItr = sheet.iterator();
			Iterator rowheaderItr = sheet.iterator();
			int row_cnt = 0;
			ArrayList errors = new ArrayList();
			ArrayList recordlevelerrors = new ArrayList();

			ArrayList DataHeadererrors = new ArrayList();
			ArrayList Dataerrors = new ArrayList();
			ArrayList Allerrors = new ArrayList();

			ArrayList columnNameLst = new ArrayList();

			while (rowheaderItr.hasNext()) {
				// System.out.println("INSIDE WHILE LOOP");

				HSSFRow row = (HSSFRow) rowheaderItr.next();
				HSSFCell celVal[] = new HSSFCell[columnCnt];
				if (row.getRowNum() == 0) {
					for (int k = 0; k < columnCnt; k++) {
						// System.out.println("AAaa");
						HSSFCell cellV = row.getCell(k) != null ? row.getCell(k) : null;
						celVal[k] = cellV != null ? cellV : null;
						if (null != celVal[k]) {
							Excelheaders.put(celVal[k].toString(), celVal[k].toString());
							columnNameLst.add(celVal[k].toString());
						}
					}

				} else {
					break;
				}
			}

			// $$
			for (Entry<String, String> entry : Excelheaders.entrySet()) {
				// Check if the current value is a key in the 2nd map
				System.out.println("entry===" + entry.getValue());
				if (!TemplateHeaderDisplayName.containsKey(entry.getKey())) {
					// hMap2 doesn't have the key for this value. Add key-value
					// in new map.
					UnMatchheaders.put(entry.getKey(), entry.getValue());
				}
				// ColumnNames=ColumnNames+","+entry.getKey();
			}
			// $$

			System.out.println("###Excelheaders :" + Excelheaders);
			System.out.println("###UnMatchheaders :" + UnMatchheaders);
			/*
			 * if(excelRowCnt<50){ //Excel Count not matching with Template count errors.
			 * add("Minimum Uploaded Excel Row should be 50 records [Uploaded Excel File contain rows:"
			 * +excelRowCnt+" ] "); UploadedStatus.put("error", errors);
			 * 
			 * }else
			 */

			if (ExcelNoOfColumns != columnCnt) {
				// Excel Count not matching with Template count
				errors.add("Uploaded Excel Column [Count:" + ExcelNoOfColumns
						+ "] not matched with Template Column Count [Count:" + columnCnt + "]");
				UploadedStatus.put("error", errors);
				System.out.println("unmatched count ExcelNoOfColumns:" + ExcelNoOfColumns + "  columnCnt:" + columnCnt);
				//
			} else if (UnMatchheaders.size() > 0) {
				// Excel Header name not matching with Template Header Name
				String unmatchedHeaderName = "";
				for (Entry<String, String> entry : UnMatchheaders.entrySet()) {
					unmatchedHeaderName = unmatchedHeaderName + "," + entry.getKey();
				}
				errors.add("Excel Header are not matching Unmatched Header Name: " + unmatchedHeaderName);
				System.out.println("Excel Header are not matching Unmatched Header Name: " + unmatchedHeaderName);
				UploadedStatus.put("error", errors);
			} else {
				// System.out.println("###########else####################");
				while (rowItr.hasNext()) {
					// System.out.println("1");
					// Boolean rowErorExistFlag=false;
					// errors = new ArrayList();
					recordlevelerrors = new ArrayList();
					int rowErorExistFlag = 0;
					ArrayList ValidrowDataLst = new ArrayList();
					ArrayList rowDataLst = new ArrayList();
					String insertQuery = "";
					HSSFRow row = (HSSFRow) rowItr.next();
					HSSFCell celVal[] = new HSSFCell[columnCnt];

					for (int k = 0; k < columnCnt; k++) {

						// System.out.println("k:"+row.getCell(k));
						HSSFCell cellV = row.getCell(k) != null ? row.getCell(k) : null;
						celVal[k] = cellV != null ? cellV : null;
						// System.out.println("rowErorExistFlag:"+rowErorExistFlag);

						// if(!rowErorExistFlag){

						String ExcelColumnName = "";
						String ColumnName = "";
						String ColumnDataType = "";
						String DisplayColumnName = "";
						int ColumnLength = 0;
						String ColumnAllowNullFlag = "";
						// System.out.println("columnNameLst :"+columnNameLst);
						ExcelColumnName = columnNameLst.get(k).toString();
						ColumnName = TemplateHeaderDisplayName.get(ExcelColumnName);
						// ColumnName = columnNameLst.get(k).toString();
						// System.out.println("################ColumnName:"+ColumnName);
						ExcelDeatailsBean tableBeanObj = headers.get(ColumnName);
						// System.out.println("obj colName"+tableBeanObj.getColumnName());
						// System.out.println("obj datatype:"+tableBeanObj.getColumnDataType());
						ColumnDataType = tableBeanObj.getColumnDataType();
						ColumnLength = tableBeanObj.getColumnLength();
						ColumnAllowNullFlag = tableBeanObj.getColumnAllowNullFlag();
						DisplayColumnName = tableBeanObj.getDisplayColumnName();

						if (!setFirstColumFlag) {
							max_table_id = tableBeanObj.getMax_table_id();
							// System.out.println("$$$$max_table_id$$:"+max_table_id);
							// FirstColumn=tableBeanObj.getFirstColumnName();
						}
						// ##
						// System.out.println("###################");
						// System.out.println("ColumnName:"+ColumnName);
						// System.out.println("ColumnDataType:"+ColumnDataType);
						// System.out.println("###################");
						// ##
						if (row_cnt != 0) {

							if (null == celVal[k]) {
								rowDataLst.add(celVal[k]);
								// System.out.println("%%%%%%%%%%%1");
							} else {
								rowDataLst.add(celVal[k].toString());
								// System.out.println("%%%%%%%%%%%2");
							}

							int row_number = row_cnt + 1;
							String isValidMsg = validateFieldType(celVal[k], tableBeanObj);
							// System.out.println("Excel Data:"+celVal[k]);
							if (!isValidMsg.equals("VALID")) {
								// System.out.println("#########1#############");
								// errors.add("["+row_number+"]Invalid Data type Column Name:"+ColumnName+"
								// Expected #"+ColumnDataType+"# "
								// );
								// errors.add(" Invalid Data type Column Name "+DisplayColumnName+" Expected
								// [Type:"+ColumnDataType+",Size:"+ColumnLength+",Mandatory:"+ColumnAllowNullFlag);
								String MandatoryFlag = "";
								if (ColumnAllowNullFlag.equals("Y")) {
									MandatoryFlag = "No";
								} else if (ColumnAllowNullFlag.equals("N")) {
									MandatoryFlag = "Yes";
								}
//								recordlevelerrors
//										.add(" Invalid Data type Column Name "
//												+ DisplayColumnName
//												+ " Expected [Type:"
//												+ ColumnDataType + ",Size:"
//												+ ColumnLength + ",Mandatory:"
//												+ MandatoryFlag + "]");
								recordlevelerrors.add(isValidMsg);
//								
								// rowErorExistFlag=true;
								rowErorExistFlag++;
							} else {

								String prepValues = prepareDataForInsertQuery(celVal[k], tableBeanObj);
								ValidrowDataLst.add(prepValues);
							}
						}
						// flag//}
					}
					// System.out.println("#######################");
					// System.out.println("%%%%%rowDataLst :"+rowDataLst);
					// ##
					// $$
					if (row_cnt == 1) {
						for (Entry<String, ExcelDeatailsBean> entry : headers.entrySet()) {
							ExcelDeatailsBean tabledetailbean = new ExcelDeatailsBean();
							tabledetailbean = entry.getValue();
							DataHeadererrors.add(tabledetailbean.getDisplayColumnName());
							// DataHeadererrors.add(entry.getKey().toString());

						}
						DataHeadererrors.add("Error Details");
					}
					// $$
					// ##
					// if(errors.size()>0){
					if (recordlevelerrors.size() > 0) {
						rowDataLst.add(
								recordlevelerrors.toString().substring(1, recordlevelerrors.toString().length() - 1));
						Dataerrors.add(rowDataLst);
					}

					// System.out.println("ValidrowDataLst.size() :"+ValidrowDataLst.size());
					// Comment
					// System.out.println("########row_cnt#########"+row_cnt);
					// System.out.println("columnCnt"+columnCnt);

					if ((ValidrowDataLst.size() == columnCnt) && (rowErorExistFlag == 0)) {

						if (!setFirstColumFlag) { // Appending UTR_ID FIRST
													// COLUMN IN INSERT QUERY

							ColumnNames = "RECORD_ID," + ColumnNames
									+ ",UPLOAD_ID,INSERT_DT,INSERT_BY,MEM_BNK_ID,MEM_ZNE_ID,MEM_BRN_ID";
							setFirstColumFlag = true;

						}
						// String ColumnValues = "";
						String ColumnValues = max_table_id + ",";
						ColumnValues = ColumnValues
								+ ValidrowDataLst.toString().substring(1, ValidrowDataLst.toString().length() - 1);

						ColumnValues = ColumnValues + ",'" + BatchId + "',SYSDATE,'" + UserObj.getUSR_ID() + "','"
								+ UserObj.getMEM_BNK_ID() + "','" + UserObj.getMEM_ZNE_ID() + "','"
								+ UserObj.getMEM_BRN_ID() + "'";
						insertQuery = "INSERT INTO " + TableName + "(" + ColumnNames + ") VALUES(" + ColumnValues + ")";
						max_table_id = max_table_id + 1;
						// System.out.println("ColumnName:"+ColumnNames);
						// System.out.println("ColumnValues:"+ColumnValues);
						Stat.addBatch(insertQuery);
						QueryAddedFlag = true;
						System.out.println("insertQuery : " + insertQuery);

					}

					row_cnt++;
					int counter = 0;

				} // end while loop
				int n[] = {};
				try {
					// System.out.println("QueryAddedFlag :"+QueryAddedFlag);
					if (QueryAddedFlag) {
						n = Stat.executeBatch();
						System.out.println("n[]" + n.length);
					}

				} catch (Exception err) {
					err.printStackTrace();
					throw new TenureBulkException("Error@Bulkupload-CheckExcelData:" + err.toString());

				}
				if (n.length > 0) {
					ArrayList successRecord = new ArrayList();
					ArrayList unsuccessRecord = new ArrayList();

					// validateData(con,successRecord,unsuccessRecord,BatchId,BulkName);
					validateData(con, successRecord, unsuccessRecord, BatchId, BulkName, headers);
					System.out.println("successRecord size:" + successRecord.size());
					System.out.println("unsuccessRecord size:" + unsuccessRecord.size());

					System.out.println("successRecord:" + successRecord);
					System.out.println("unsuccessRecord:" + unsuccessRecord);

					UploadedStatus.put("successRecord", successRecord);
					UploadedStatus.put("unsuccessRecord", unsuccessRecord);
					UploadedStatus.put("error", errors);

				}

			} // end of else loop for ExcelNoOfColumns!=columnCnt
			Allerrors.add(DataHeadererrors);
			Allerrors.add(Dataerrors);
			UploadedStatus.put("allerror", Allerrors);

			System.out.println("$$$$$$$$$$errors$$$$$$$$$$$START$$$");
			System.out.println("$$$$$$$$$$AllDataerrors$$$$$$$$$$$$$$:" + Allerrors);
			System.out.println("$$$$$$$$$$AllDataerrors.size()$$$$$$$$$$$$$$:" + Allerrors.size());
			// System.out.println("errors : "+errors);
			System.out.println("$$$$$$$$$$errors$$$$$$$$$$$$END$$");
		} catch (FileNotFoundException fi) {
			fi.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			Date d = new Date();
		} finally {
			if (is != null) {
				is.close();
			}
			if (Stat != null) {
				Stat.close();
			}
			return UploadedStatus;
		}
	}

	// added for headers
	ArrayList<String> getDisplayNameOfHeaderName(ArrayList<String> HeaderNameLst,
			LinkedHashMap<String, ExcelDeatailsBean> headers) {
		ArrayList<String> displayHeaderNameLst = new ArrayList<String>();
		for (Entry<String, ExcelDeatailsBean> entry : headers.entrySet()) {
			ExcelDeatailsBean t = entry.getValue();
			// System.out.println("[ "+t.getDisplayColumnName()+"] display #t# Name [
			// "+t.getColumnName()+" ]");
		}

		for (String header : HeaderNameLst) {
			if (null != headers.get(header)) {
				ExcelDeatailsBean tabledetailbean = headers.get(header);
				// System.out.println("##############getDisplayNameOfHeaderName###############:"+header);
				String display_header_name = tabledetailbean.getDisplayColumnName();
				// System.out.println("##############getDisplayNameOfHeaderName#######display_header_name########:"+display_header_name);
				displayHeaderNameLst.add(display_header_name);
			} else {
				displayHeaderNameLst.add(header);
			}
		} /**/
		System.out.println("displayHeaderNameLst :" + displayHeaderNameLst);
		System.out.println("DisplayHeaderNameLst Size:" + displayHeaderNameLst.size());
		System.out.println("HeaderNameLst Size:" + HeaderNameLst.size());
		return displayHeaderNameLst;
	}
}
