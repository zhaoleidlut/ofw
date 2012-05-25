package com.htong.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.stereotype.Repository;

import com.htong.domain.WellModel;
import com.htong.util.mongo.CollectionConstants;

@Repository
public class WellModelDao {
	@Autowired
	private MongoTemplate mongoTemplate;
	/**
	 * 通过路径获得井列表
	 * @param path
	 * @return
	 */
	public List<WellModel> getWellsByPath(String path) {
		Query query = new Query(Criteria.where("path").is(path));
		List<WellModel> wellList = mongoTemplate.find(CollectionConstants.WELL_COLLECTION_NAME, query, WellModel.class);
		return wellList;
	}
	/**
	 * 通过井号获得井
	 * @param wellNum
	 * @return
	 */
	public WellModel getWellByNum(String wellNum) {
		Query query = new Query(Criteria.where("num").is(wellNum));
		WellModel well = mongoTemplate.findOne(CollectionConstants.WELL_COLLECTION_NAME, query, WellModel.class);
		return well;
	}

}
