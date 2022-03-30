package com.nbfc.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.nbfc.bean.ApplicationStatusDetailsBean;

@Repository("ApplicationStatusDao")
public interface ApplicationStatusDao {
	public List<Map<String, Object>> getApplicationStatus(String userId,Date toDate,Date fromDate,String status);
	public List<Map<String, Object>> getFileData(String FileId);
	public ApplicationStatusDetailsBean getapplicationDetails(String FileId);
	public ArrayList<List> getGETuploadedFileData(
			String fileId);
}
