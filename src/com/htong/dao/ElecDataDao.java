package com.htong.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.stereotype.Repository;

import com.htong.domain.ElecData;
import com.htong.util.mongo.CollectionConstants;

@Repository
public class ElecDataDao {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<ElecData> getElecDataList(String wellNum) {
		List<ElecData> elecDataList = new ArrayList<ElecData>();
		
		Query query = new Query(Criteria.where("well_num").is(wellNum));
		query.limit(500);
		
		elecDataList = mongoTemplate.find(CollectionConstants.ELEC_DATA, query, ElecData.class);
		return elecDataList;
		
	}

}
