package com.nbfc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nbfc.bean.ApplicationStatusDetailsBean;

public interface ApplicationStatusService {
	
	public List<Map<String, Object>> getApplicationStatus(String userId,Date toDate,Date fromDate,String status);
	public List<Map<String, Object>> getFileData(String FileId);
	public ApplicationStatusDetailsBean getapplicationDetails(String FileId);
	public ArrayList<List> getGETuploadedFileData(
			String fileId);
}
