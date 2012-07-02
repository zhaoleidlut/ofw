package com.htong.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.log4testng.Logger;

import com.htong.alg.SGTDataComputerProcess;
import com.htong.dao.WellDataDao;
import com.htong.dao.WellModelDao;
import com.htong.domain.WellData;
import com.htong.domain.WellModel;

@Service
public class WellDataService {
	
	private static final Logger log = Logger.getLogger(WellDataService.class); 
	
	@Autowired
	private WellDataDao wellDataDao;
	@Autowired
	private WellModelDao wellModelDao;
	
	public WellData getLatestWellDataByWellNum(String wellNum) {
		return wellDataDao.getLatedWellDataByWellNum(wellNum);
	}
	
	public WellData getHistoryWellData(String wellNum, Calendar calendar) {
		return wellDataDao.getHistoryWellData(wellNum, calendar);
	}
	
	/**
	 * 获得一天日产量
	 * @param wellNum
	 * @param startDate
	 * @return
	 */
	public String getDayProduct(String wellNum, Calendar startCalendar) {
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(startCalendar.getTime());
		endCalendar.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		System.out.println("起始时间：" + sdf.format(startCalendar.getTime()));
		System.out.println("结束时间：" + sdf.format(endCalendar.getTime()));
		
		List<WellData> wellDataList = wellDataDao.getHistoryWellData(wellNum, startCalendar, endCalendar);
		
		if(wellDataList == null) {
			return "0";
		}
		
		List<Float> resultList = new ArrayList<Float>(); // 24小时的产液量
		List<Float> yxccList = new ArrayList<Float>();	//有效冲程
		List<String> timeList = new ArrayList<String>();	//时间
		
		WellModel wellModel = wellModelDao.getWellByNum(wellNum);
		
		for(WellData wellData : wellDataList) {
			if(wellData == null || (wellData.getZaihe()[0]<0.5 && wellData.getZaihe()[0]<0.5&&wellData.getZaihe()[0]<0.5)) {
				continue;//数据均为0
			}
			SGTDataComputerProcess sp = new SGTDataComputerProcess();
			Map<String,Object> map = sp.calcSGTData(wellData.getWeiyi(), wellData.getZaihe(), 0,
					wellData.getChong_cheng_time(),
					Float.valueOf(wellModel.getBengjing()),
					Float.valueOf(wellModel.getOilDensity()),
					Float.valueOf(wellModel.getHanshui()));
			Float f = (Float) map.get("liquidProduct");
			resultList.add(f);
			
			Float yxcc = (Float)map.get("youxiaochongcheng");
			yxccList.add(yxcc);
			
			timeList.add(sdf.format(wellData.getDevice_time()));
			
			log.debug("设备时间：" + sdf.format(wellData.getDevice_time()));
		}
		
		for(int jjj=0;jjj<resultList.size();jjj++) {
			log.warn("【时间】：" + timeList.get(jjj));
			log.warn("有效冲程：" + yxccList.get(jjj));
			log.warn("产液量：" + resultList.get(jjj));
		}
		
		log.info("*************数据个数为：" + resultList.size());

		// 处理结果
		// 冒泡法，从小到大
		int deal_num = resultList.size();
		for (int i1 = 0; i1 < deal_num; i1++) {
			for (int i2 = 0; i2 < deal_num - i1 - 1; i2++) {
				if (resultList.get(i2 + 1) < resultList.get(i2)) {
					float temp = resultList.get(i2 + 1);
					resultList.set(i2 + 1, resultList.get(i2));
					resultList.set(i2, temp);
				}
			}
		}
//		for (float f : resultList) {
//			log.debug("产液量的值：" + f);
//		}
		
		//处理有效冲程
		for (int i1 = 0; i1 < deal_num; i1++) {
			for (int i2 = 0; i2 < deal_num - i1 - 1; i2++) {
				if (yxccList.get(i2 + 1) < yxccList.get(i2)) {
					float temp = yxccList.get(i2 + 1);
					yxccList.set(i2 + 1, yxccList.get(i2));
					yxccList.set(i2, temp);
				}
			}
		}


		int numCount = resultList.size();
		int numStart = numCount * 1 / 3;
		int numEnd = numCount * 2 / 3;
		float zuihoujieguo = 0; // 产液量结果，一个小时的
		for (int i = numStart; i < numEnd; i++) {
			zuihoujieguo += resultList.get(i);
		}
		log.debug(numEnd + " " + numStart + ":" + (numEnd - numStart));
		
		if((numEnd-numStart)<=0) {
			zuihoujieguo = 0;
		} else {
			zuihoujieguo /= (numEnd - numStart);
		}

		log.debug("*************产液量平均值：" + zuihoujieguo);
		log.debug("*************产液量最终值：" + zuihoujieguo*24);
		
//		float yxccpjz = 0;
//		for (int i = numStart; i < numEnd; i++) {
//			yxccpjz += yxccList.get(i);
//		}
//		log.debug(numEnd + " " + numStart + ":" + (numEnd - numStart));
//		
//		if((numEnd-numStart)<=0) {
//			yxccpjz = 0;
//		} else {
//			yxccpjz /= (numEnd - numStart);
//		}
//		log.debug("**************有效冲程平均值：" + yxccpjz);


//		int timeLong = (int) ((cc2.getTime().getTime() - cc.getTime().getTime())
//				/ (1000 * 60 * 60) + 24); // 小时
//		log.debug("时间小时差：" + timeLong);

		float loss = 0;
		if (wellModel.getLoss() != null) {
			loss = Float.valueOf(wellModel.getLoss());
		}
		
		int timeLong = 24;

		float chanyeliang = zuihoujieguo * timeLong + timeLong * loss / 24; // 平均值+补偿值
		log.debug("最后产液量：" + chanyeliang);
		BigDecimal yeBd = new BigDecimal(chanyeliang);
		float newChanyeliang = yeBd.setScale(3, BigDecimal.ROUND_HALF_UP)
				.floatValue();

		if (newChanyeliang < 0) {
			newChanyeliang = 0;
		}
		
		
		return String.valueOf(newChanyeliang);
	}
	
	/**
	 * 获得自定义时间段日产量
	 * @param wellNum
	 * @param startDate
	 * @return
	 */
	public String getCustomDayProduct(String wellNum, Calendar startCalendar, Calendar endCalendar) {

		
		List<WellData> wellDataList = wellDataDao.getHistoryWellData(wellNum, (Calendar)startCalendar.clone(), (Calendar)endCalendar.clone());
		
		if(wellDataList == null) {
			return "0";
		}
		
		List<Float> resultList = new ArrayList<Float>(); // 24小时的产液量
		List<Float> yxccList = new ArrayList<Float>();	//有效冲程
		List<String> timeList = new ArrayList<String>();	//时间
		
		WellModel wellModel = wellModelDao.getWellByNum(wellNum);
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		for(WellData wellData : wellDataList) {
			if(wellData == null || (wellData.getZaihe()[0]<0.5 && wellData.getZaihe()[0]<0.5&&wellData.getZaihe()[0]<0.5)) {
				continue;//数据均为0
			}
			
			SGTDataComputerProcess sp = new SGTDataComputerProcess();
			Map<String,Object> map = sp.calcSGTData(wellData.getWeiyi(), wellData.getZaihe(), 0,
					wellData.getChong_cheng_time(),
					Float.valueOf(wellModel.getBengjing()),
					Float.valueOf(wellModel.getOilDensity()),
					Float.valueOf(wellModel.getHanshui()));
			Float f = (Float) map.get("liquidProduct");
			resultList.add(f);
			
			Float yxcc = (Float)map.get("youxiaochongcheng");
			yxccList.add(yxcc);
			
			timeList.add(sdf.format(wellData.getDevice_time()));
			
			log.debug("设备时间：" + sdf.format(wellData.getDevice_time()));
		}
		
		for(int jjj=0;jjj<resultList.size();jjj++) {
			log.debug("【时间】：" + timeList.get(jjj));
			log.debug("有效冲程：" + yxccList.get(jjj));
			log.debug("产液量：" + resultList.get(jjj));
		}
		
		log.info("*************数据个数为：" + resultList.size());

		// 处理结果
		// 冒泡法，从小到大
		int deal_num = resultList.size();
		for (int i1 = 0; i1 < deal_num; i1++) {
			for (int i2 = 0; i2 < deal_num - i1 - 1; i2++) {
				if (resultList.get(i2 + 1) < resultList.get(i2)) {
					float temp = resultList.get(i2 + 1);
					resultList.set(i2 + 1, resultList.get(i2));
					resultList.set(i2, temp);
				}
			}
		}
//		for (float f : resultList) {
//			log.warn("产液量的值：" + f);
//		}
		
		//处理有效冲程
		for (int i1 = 0; i1 < deal_num; i1++) {
			for (int i2 = 0; i2 < deal_num - i1 - 1; i2++) {
				if (yxccList.get(i2 + 1) < yxccList.get(i2)) {
					float temp = yxccList.get(i2 + 1);
					yxccList.set(i2 + 1, yxccList.get(i2));
					yxccList.set(i2, temp);
				}
			}
		}

		int numCount = resultList.size();
		int numStart = numCount * 1 / 3;
		int numEnd = numCount * 2 / 3;
		float zuihoujieguo = 0; // 产液量结果，一个小时的
		for (int i = numStart; i < numEnd; i++) {
			zuihoujieguo += resultList.get(i);
		}
		log.debug(numEnd + " " + numStart + ":" + (numEnd - numStart));
		
		if((numEnd-numStart)<=0) {
			zuihoujieguo = 0;
		} else {
			zuihoujieguo /= (numEnd - numStart);
		}

		log.warn("*************产液量平均值：" + zuihoujieguo);
		log.debug("*************产液量最终值：" + zuihoujieguo*24);

//		log.warn("结束时间" + sdf.format(endCalendar.getTime()));
//		log.warn("起始时间" + sdf.format(startCalendar.getTime()));
		int timeLong = (int) ((endCalendar.getTime().getTime() - startCalendar.getTime().getTime())
				/ (1000 * 60 * 60)); // 小时
		log.warn("时间小时差：" + timeLong);

		float loss = 0;
		if (wellModel.getLoss() != null) {
			loss = Float.valueOf(wellModel.getLoss());
		}

		float chanyeliang = zuihoujieguo * timeLong + timeLong * loss / 24; // 平均值+补偿值
		log.warn("最后产液量：" + chanyeliang);
		BigDecimal yeBd = new BigDecimal(chanyeliang);
		float newChanyeliang = yeBd.setScale(3, BigDecimal.ROUND_HALF_UP)
				.floatValue();

		if (newChanyeliang < 0) {
			newChanyeliang = 0;
		}
				
		return String.valueOf(newChanyeliang);
	}

}
