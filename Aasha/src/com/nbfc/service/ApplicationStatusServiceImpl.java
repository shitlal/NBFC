package com.nbfc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nbfc.bean.ApplicationStatusDetailsBean;
import com.nbfc.dao.ApplicationStatusDao;

@Service("ApplicationStatusService")
public class ApplicationStatusServiceImpl implements ApplicationStatusService {
	@Autowired
	ApplicationStatusDao applicationStatusDao;
	public List<Map<String, Object>> getFileData(String FileId){
		return applicationStatusDao.getFileData(FileId);

	}
	public List<Map<String, Object>> getApplicationStatus(String userId,Date toDate,Date fromDate,String status){
		return applicationStatusDao.getApplicationStatus(userId,toDate, fromDate, status);
	}
	@Override
	public ApplicationStatusDetailsBean getapplicationDetails(String FileId) {
		// TODO Auto-generated method stub
		return applicationStatusDao.getapplicationDetails(FileId);
	}
	@Override
	public ArrayList<List> getGETuploadedFileData(
			String fileId) {
		// TODO Auto-generated method stub
		return applicationStatusDao.getGETuploadedFileData(fileId);
	}
}
