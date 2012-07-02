package com.htong.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.stereotype.Repository;

import com.htong.domain.EnergyData;
import com.htong.util.mongo.CollectionConstants;
@Repository
public class EnergyDataDao {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<EnergyData> getEnergyData(String wellNum) {
		List<EnergyData> energyDataList = new ArrayList<EnergyData>();
		
		Query query = new Query(Criteria.where("well_num").is(wellNum));
		query.limit(500);
		
		energyDataList = mongoTemplate.find(CollectionConstants.ENERGY_DATA, query, EnergyData.class);
		return energyDataList;
	}

}
