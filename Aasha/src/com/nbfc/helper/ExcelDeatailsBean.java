package com.nbfc.helper;

// Added by Shashi for tenure upload 17-11-2020
public class ExcelDeatailsBean {

	String ColumnAllowNullFlag="";
	String ColumnName="";
	String ColumnDataType="";
	int ColumnLength=0;
	int max_table_id=0;
	String DisplayColumnName="";
	int columnPrecision =0;
	int columnScale =0;
	
	public ExcelDeatailsBean() {
		
	}
	
	public String getColumnName() {
		return ColumnName;
	}
	public void setColumnName(String columnName) {
		ColumnName = columnName;
	}
	public String getColumnDataType() {
		return ColumnDataType;
	}
	public void setColumnDataType(String columnDataType) {
		ColumnDataType = columnDataType;
	}
	public int getColumnLength() {
		return ColumnLength;
	}
	public void setColumnLength(int columnLength) {
		ColumnLength = columnLength;
	}
	public int getMax_table_id() {
		return max_table_id;
	}
	public void setMax_table_id(int max_table_id) {
		this.max_table_id = max_table_id;
	}
	public String getDisplayColumnName() {
		return DisplayColumnName;
	}
	public void setDisplayColumnName(String displayColumnName) {
		DisplayColumnName = displayColumnName;
	}
	public int getColumnPrecision() {
		return columnPrecision;
	}
	public void setColumnPrecision(int columnPrecision) {
		this.columnPrecision = columnPrecision;
	}
	public int getColumnScale() {
		return columnScale;
	}
	public void setColumnScale(int columnScale) {
		this.columnScale = columnScale;
	}

	public String getColumnAllowNullFlag() {
		return ColumnAllowNullFlag;
	}
	public void setColumnAllowNullFlag(String columnAllowNullFlag) {
		ColumnAllowNullFlag = columnAllowNullFlag;
	}
	
	@Override
	public String toString() {
		return "ExcelDeatailsBean [ColumnName=" + ColumnName + ", ColumnDataType=" + ColumnDataType + ", ColumnLength="
				+ ColumnLength + ", max_table_id=" + max_table_id + ", DisplayColumnName=" + DisplayColumnName
				+ ", columnPrecision=" + columnPrecision + ", columnScale=" + columnScale + "]";
	}


	
	

}
