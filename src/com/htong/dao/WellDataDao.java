package com.htong.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Order;
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

//	public WellData getLatedWellDataByWellNum(String wellNum) {
//		Query myQuery = new Query(Criteria.where("well_num").is(wellNum));
//		myQuery.sort().on("device_time", Order.DESCENDING);
//		Calendar currentCalendar = Calendar.getInstance();
//
//		boolean exist = mongoTemplate.collectionExists(CollectionConstants
//				.getWellDataCollection(wellNum, currentCalendar));
//		// 查找表
//		int count = 10;
//		while (count > 0 && !exist) {
//			//log.warn(CollectionConstants.getWellDataCollection(wellNum, currentCalendar));
//			currentCalendar.add(Calendar.MONTH, -1);
//			exist = mongoTemplate.collectionExists(CollectionConstants
//					.getWellDataCollection(wellNum, currentCalendar));
//			count--;
//		}
//
//		log.warn("最新数据的表名：" + CollectionConstants.getWellDataCollection(wellNum,
//				currentCalendar));
//		List<WellData> wellDataList = mongoTemplate.find(CollectionConstants
//				.getWellDataCollection(wellNum, currentCalendar), myQuery,
//				WellData.class);
//		if(wellDataList != null) {
//			return wellDataList.get(0);
//		}
//		return null;
//	}
	
	public WellData getLatedWellDataByWellNum(String wellNum) {
		Query myQuery = new Query(Criteria.where("well_num").is(wellNum));
		myQuery.sort().on("device_time", Order.DESCENDING);

//		boolean exist = mongoTemplate.collectionExists(CollectionConstants
//				.getWellDataCollection(wellNum, currentCalendar));
//		// 查找表
//		int count = 10;
//		while (count > 0 && !exist) {
//			//log.warn(CollectionConstants.getWellDataCollection(wellNum, currentCalendar));
//			currentCalendar.add(Calendar.MONTH, -1);
//			exist = mongoTemplate.collectionExists(CollectionConstants
//					.getWellDataCollection(wellNum, currentCalendar));
//			count--;
//		}

//		log.warn("最新数据的表名：" + CollectionConstants.getWellDataCollection(wellNum,
//				currentCalendar));
		List<WellData> wellDataList = mongoTemplate.find(CollectionConstants
				.getWellDataCollection(wellNum), myQuery,
				WellData.class);
		if(wellDataList != null && !wellDataList.isEmpty()) {
			return wellDataList.get(0);
		}
		return null;
	}
	
	/**
	 * 获得次最近数据
	 * @param wellNum
	 * @return
	 */
	public WellData getSecondLatedWellDataByWellNum(String wellNum) {
		Query myQuery = new Query(Criteria.where("well_num").is(wellNum));
		myQuery.sort().on("device_time", Order.DESCENDING);

		List<WellData> wellDataList = mongoTemplate.find(CollectionConstants
				.getWellDataCollection(wellNum), myQuery,
				WellData.class);
		if(wellDataList != null && !wellDataList.isEmpty() && wellDataList.size()>1) {
			for(WellData wellData:wellDataList) {
				if(!wellData.getDevice_time().equals(wellDataList.get(0).getDevice_time())) {
					return wellData;
				}
			}
		}
		return null;
	}

	public List<WellData> getHistoryWellData(String wellNum,
			Calendar startCalendar, Calendar endCalendar) {
		List<WellData> wellDataList = new ArrayList<WellData>();
//		while (endCalendar.after(startCalendar)) {
			BasicDBObject query = new BasicDBObject();

			BasicDBObject index = new BasicDBObject();// 时间条件
			index.put("$gte", startCalendar.getTime());
			index.put("$lt", endCalendar.getTime());

			query.put("datetime", index);

			Query myQuery = new Query(Criteria.where("well_num").is(wellNum)
					.and("device_time").is(index));
			
			myQuery.sort().on("device_time", Order.DESCENDING);

			List<WellData> myWellDataList = mongoTemplate.find(
					CollectionConstants.getWellDataCollection(wellNum), myQuery, WellData.class);
			
			Date date = new Date();
			for(WellData wd : myWellDataList) {
				if(date.equals(wd.getDevice_time())) {
					log.debug("时间标签重复！");
					continue;
				}
				wellDataList.add(wd);
				date = wd.getDevice_time();
			}
			
//			wellDataList.addAll(myWellDataList);

//			startCalendar.add(Calendar.MONTH, 1);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			log.warn(sdf.format(startCalendar.getTime()));
			System.out.println(sdf.format(startCalendar.getTime()));
//		}
		return wellDataList;
	}

	public WellData getHistoryWellData(String wellNum, Calendar startCalendar) {

		BasicDBObject query = new BasicDBObject();

		BasicDBObject index = new BasicDBObject();// 时间条件
		index.put("$gte", startCalendar.getTime());

		startCalendar.add(Calendar.HOUR, 1);

		index.put("$lt", startCalendar.getTime());

		query.put("datetime", index);

		Query myQuery = new Query(Criteria.where("well_num").is(wellNum)
				.and("device_time").is(index));

		WellData wellData = mongoTemplate.findOne(
				CollectionConstants.getWellDataCollection(wellNum),
				myQuery, WellData.class);

		return wellData;
	}
	
}
