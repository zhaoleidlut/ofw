package com.htong.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.htong.dao.EnergyDataDao;
import com.htong.domain.EnergyData;

@Service
public class EnergyDataService {
	@Autowired
	private EnergyDataDao energyDataDao;
	
	public List<EnergyData> getEnergyData(String wellNum) {
		return energyDataDao.getEnergyData(wellNum);
	}

}
