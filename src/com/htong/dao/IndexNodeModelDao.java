package com.htong.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.stereotype.Repository;

import com.htong.domain.IndexNodeModel;
import com.htong.util.mongo.CollectionConstants;

@Repository
public class IndexNodeModelDao {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public IndexNodeModel getIndexNodeModel() {
		Query query = new Query(Criteria.where("name").exists(true));
		IndexNodeModel indexNodeModel = mongoTemplate
					.findOne(CollectionConstants.TAG_COLLECTION_NAME, query,
							IndexNodeModel.class);
		return indexNodeModel;
	}

}
