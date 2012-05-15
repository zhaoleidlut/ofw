package com.htong.dao;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.stereotype.Repository;
import org.testng.log4testng.Logger;

import com.htong.domain.WellData;
import com.htong.util.mongo.CollectionConstants;

@Repository
public class WellDataDao {
	private static final Logger log = Logger.getLogger(WellDataDao.class);
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public WellData getLatedWellDataByWellNum(String wellNum) {
		Query myQuery = new Query(Criteria.where("well_num").is(wellNum));
		Calendar currentCalendar = Calendar.getInstance();
		log.debug(CollectionConstants.getWellDataCollection(wellNum, currentCalendar));		
		WellData wellData = mongoTemplate.findOne(CollectionConstants.getWellDataCollection(wellNum,currentCalendar), myQuery, WellData.class);
		
		log.debug(wellData.getChong_cheng_time());
		
		return wellData;
		
	}

}
