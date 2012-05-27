package com.htong.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.stereotype.Repository;
import org.testng.log4testng.Logger;

import com.htong.domain.WellData;
import com.htong.util.mongo.CollectionConstants;
import com.mongodb.BasicDBObject;

@Repository
public class WellDataDao {
	private static final Logger log = Logger.getLogger(WellDataDao.class);
	@Autowired
	private MongoTemplate mongoTemplate;

	public WellData getLatedWellDataByWellNum(String wellNum) {
		Query myQuery = new Query(Criteria.where("well_num").is(wellNum));
		Calendar currentCalendar = Calendar.getInstance();

		boolean exist = mongoTemplate.collectionExists(CollectionConstants
				.getWellDataCollection(wellNum, currentCalendar));

		// 查找表
		int count = 10;
		while (count > 0 && !exist) {
			exist = mongoTemplate.collectionExists(CollectionConstants
					.getWellDataCollection(wellNum, currentCalendar));
			currentCalendar.add(Calendar.MONTH, -1);
			// System.out.println(count);
			count--;
		}

		log.debug(CollectionConstants.getWellDataCollection(wellNum,
				currentCalendar));
		WellData wellData = mongoTemplate.findOne(CollectionConstants
				.getWellDataCollection(wellNum, currentCalendar), myQuery,
				WellData.class);

		return wellData;
	}

	public List<WellData> getHistoryWellData(String wellNum,
			Calendar startDate, Calendar endDate) {
		List<WellData> wellDataList = new ArrayList<WellData>();

		while (endDate.after(startDate)) {
			BasicDBObject query = new BasicDBObject();

			BasicDBObject index = new BasicDBObject();// 时间条件
			index.put("$gte", startDate.getTime());
			index.put("$lt", endDate.getTime());

			query.put("datetime", index);

			Query myQuery = new Query(Criteria.where("well_num").is(wellNum)
					.and("device_time").is(index));

			List<WellData> myWellDataList = mongoTemplate.find(
					CollectionConstants.getWellDataCollection(wellNum,
							startDate), myQuery, WellData.class);
			wellDataList.addAll(myWellDataList);

			startDate.add(Calendar.MONTH, 1);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			log.debug(sdf.format(startDate.getTime()));
			System.out.println(sdf.format(startDate.getTime()));
		}
		return wellDataList;
	}

	public WellData getHistoryWellData(String wellNum, Calendar startDate) {

		BasicDBObject query = new BasicDBObject();

		BasicDBObject index = new BasicDBObject();// 时间条件
		index.put("$gte", startDate.getTime());

		startDate.add(Calendar.HOUR, 1);

		index.put("$lt", startDate.getTime());

		query.put("datetime", index);

		Query myQuery = new Query(Criteria.where("well_num").is(wellNum)
				.and("device_time").is(index));

		WellData wellData = mongoTemplate.findOne(
				CollectionConstants.getWellDataCollection(wellNum, startDate),
				myQuery, WellData.class);

		return wellData;
	}

}
