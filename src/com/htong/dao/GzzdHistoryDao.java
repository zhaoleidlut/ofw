package com.htong.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.htong.domain.GzzdHistoryModel;
import com.htong.util.mongo.CollectionConstants;

@Repository
public class GzzdHistoryDao{
	
	private static final Logger log = Logger.getLogger(GzzdHistoryDao.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;

	public void saveGzzdHistory(GzzdHistoryModel gzzdHistoryModel) {
		mongoTemplate.insert(CollectionConstants.GZZD_HISTORY, gzzdHistoryModel);
	}

}
