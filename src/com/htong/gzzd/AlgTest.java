package com.htong.gzzd;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.htong.alg.MyLvBo;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class AlgTest {

	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws UnknownHostException, MongoException, ParseException {
		Mongo m = new Mongo( "127.0.0.1" , 27017 );
		DB db = m.getDB( "idata" );
		DBCollection coll = db.getCollection("WellData_草13-斜502");
		
		BasicDBObject query = new BasicDBObject();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startStr = "2012-09-21 07:00:00";
		String endStr = "2012-10-15 15:00:00";
		Date start = sdf.parse(startStr);
		Date end = sdf.parse(endStr);
		
		BasicDBObject index = new BasicDBObject();// 时间条件
		index.put("$gte", start);
		index.put("$lt", end);

		query.put("device_time", index);
		
		DBObject dbo  = coll.findOne(query);
		System.out.println(coll.getName());
		System.out.println(sdf.format((Date)dbo.toMap().get("device_time")));
		Map map = dbo.toMap();
		
		ArrayList<Double> weiyiList = (ArrayList<Double>) map.get("weiyi");
		ArrayList<Double> zaiheList = (ArrayList<Double>) map.get("zaihe");
		float weiyi[] = new float[200];
		float zaihe[] = new float[200];
		for(int i=0;i<200;i++) {
			weiyi[i] = weiyiList.get(i).floatValue();
			zaihe[i] = zaiheList.get(i).floatValue();
		}
		
		MyLvBo.myLvBo(weiyi, zaihe);
		
		GTDataComputerProcess gdcp = new GTDataComputerProcess();
		gdcp.calcSGTData(weiyi, zaihe, 0, ((Double)map.get("chong_cheng_time")).floatValue(), 70, 1, 90, 20);

	}

}
