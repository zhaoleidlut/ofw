package com.htong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.htong.dao.GzzdHistoryDao;
import com.htong.domain.GzzdHistoryModel;

import java.util.List;
@Service
public class GzzdHistoryService {
	@Autowired
	private GzzdHistoryDao gzzdHistoryDao;
	
	public List<GzzdHistoryModel> getAllGzzdHistoryModel() {
		return gzzdHistoryDao.getAllGzzdHistoryModel();
	}
	public List<GzzdHistoryModel> getGzzdHistoryModelsByWellNum(String wellNum) {
		return gzzdHistoryDao.getGzzdHistoryModelsByWellNum(wellNum);
	}
	
	public void saveGzzdHistory(GzzdHistoryModel gzzdHistoryModel) {
		gzzdHistoryDao.saveGzzdHistory(gzzdHistoryModel);
	}

}
