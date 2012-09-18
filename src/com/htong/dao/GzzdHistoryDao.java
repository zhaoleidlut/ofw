package com.htong.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Order;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.stereotype.Repository;

import com.htong.domain.EnergyData;
import com.htong.domain.GzzdHistoryModel;
import com.htong.util.mongo.CollectionConstants;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GzzdHistoryDao{
	
	private static final Logger log = Logger.getLogger(GzzdHistoryDao.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;

	public void saveGzzdHistory(GzzdHistoryModel gzzdHistoryModel) {
		mongoTemplate.insert(CollectionConstants.GZZD_HISTORY, gzzdHistoryModel);
	}
	
	public List<GzzdHistoryModel> getAllGzzdHistoryModel() {
		List<GzzdHistoryModel> gzzdHistoryModel = new ArrayList<GzzdHistoryModel>();
		
		Query query = new Query(Criteria.where("wellNum").exists(true));
		query.sort().on("gzzdTime", Order.DESCENDING);
		query.limit(1000);
		
		gzzdHistoryModel = mongoTemplate.find(CollectionConstants.GZZD_HISTORY, query, GzzdHistoryModel.class);
		return gzzdHistoryModel;
	}
	
	public List<GzzdHistoryModel> getGzzdHistoryModelsByWellNum(String wellNum) {
		List<GzzdHistoryModel> gzzdHistoryModel = new ArrayList<GzzdHistoryModel>();
		
		Query query = new Query(Criteria.where("wellNum").is(wellNum));
		query.sort().on("gzzdTime", Order.DESCENDING);
		query.limit(1000);
		
		gzzdHistoryModel = mongoTemplate.find(CollectionConstants.GZZD_HISTORY, query, GzzdHistoryModel.class);
		return gzzdHistoryModel;
	}

}
