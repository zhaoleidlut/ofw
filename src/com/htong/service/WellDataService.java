package com.htong.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.htong.dao.WellDataDao;
import com.htong.domain.WellData;

@Service
public class WellDataService {
	
	@Autowired
	private WellDataDao wellDataDao;
	
	public WellData getLatestWellDataByWellNum(String wellNum) {
		return wellDataDao.getLatedWellDataByWellNum(wellNum);
	}
	
	public WellData getHistoryWellData(String wellNum, Calendar calendar) {
		return wellDataDao.getHistoryWellData(wellNum, calendar);
	}

}
