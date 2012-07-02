package com.htong.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.htong.dao.ElecDataDao;
import com.htong.domain.ElecData;
@Service
public class ElecDataService {
	@Autowired
	private ElecDataDao elecDataDao;
	
	public List<ElecData> getElecDataByWellNum(String wellNum) {
		return elecDataDao.getElecDataList(wellNum);
	}

}
