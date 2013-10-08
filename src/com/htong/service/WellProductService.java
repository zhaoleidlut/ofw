package com.htong.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.log4testng.Logger;

import com.htong.alg.LvBo;
import com.htong.alg.MyLvBo;
import com.htong.alg.SGTDataComputerProcess;
import com.htong.dao.WellDataDao;
import com.htong.dao.WellModelDao;
import com.htong.domain.WellData;
import com.htong.domain.WellModel;
import com.htong.gzzd.GTDataComputerProcess;

@Service
public class WellProductService {
	private static final Logger log = Logger.getLogger(WellProductService.class); 
	@Autowired
	private WellDataDao wellDataDao;
	@Autowired
	private WellModelDao wellModelDao;
	
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
		
		if(wellDataList == null || wellDataList.isEmpty()) {
			return "0";
		}
		
		List<Float> resultList = new ArrayList<Float>(); // 24小时的产液量
		List<Float> yxccList = new ArrayList<Float>();	//有效冲程
		List<String> timeList = new ArrayList<String>();	//时间
		
		List<Float> zsList = new ArrayList<Float>();	//左上点
		
		List<Float> zhcList = new ArrayList<Float>();	//载荷差
		
		List<Float> chongchengList = new ArrayList<Float>();	//冲程
		
		WellModel wellModel = wellModelDao.getWellByNum(wellNum);
		
		for(WellData wellData : wellDataList) {
			if(wellData == null || (wellData.getZaihe()[50]<0.5 && wellData.getZaihe()[51]<0.5&&wellData.getZaihe()[52]<0.5)) {
				continue;//数据均为0
			}
			//加入滤波算法
			MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getZaihe());
			
			SGTDataComputerProcess sp = new SGTDataComputerProcess();
			Map<String,Object> map = sp.calcSGTData(wellData.getWeiyi(), wellData.getZaihe(), 0,
					wellData.getChong_cheng_time(),
					Float.valueOf(wellModel.getBengjing()),
					Float.valueOf(wellModel.getOilDensity()),
					Float.valueOf(wellModel.getHanshui()));
			Float f = (Float) map.get("liquidProduct");
			resultList.add(f);
			System.out.println("设备时间：" + sdf.format(wellData.getDevice_time()));
			System.out.println("理论产液量：" + f);
			
			Float yxcc = (Float)map.get("youxiaochongcheng");
			yxccList.add(yxcc);
			System.out.println("有效冲程：" + yxcc);
			
			Float zhc = (Float) map.get("zhc");
			zhcList.add(zhc);
			
			Float zs = (Float) map.get("zs");
			zsList.add(zs);
			
			Float chongcheng = (Float)map.get("chongcheng");
			chongchengList.add(chongcheng);
			
			timeList.add(sdf.format(wellData.getDevice_time()));
			
			log.debug("设备时间：" + sdf.format(wellData.getDevice_time()));
		}

		//log.warn("*************数据个数为：" + resultList.size());
		
		float zuihoujieguo = LvBo.zhongzhiLB(resultList); // 产液量结果，一个小时的
		
		float zhc = LvBo.zhongzhiLB(zhcList);	//载荷差
		System.out.println("【日均载荷差值】：" + zhc);
		
		float zsp = LvBo.zhongzhiLB(zsList);	//左上均值
		System.out.println("左上点：" + zsp);
		
		float yxcc = LvBo.zhongzhiLB(yxccList);	//有效冲程
		System.out.println("平均有效冲程：" + yxcc);
		
		float chongcheng = LvBo.zhongzhiLB(chongchengList);
		System.out.println("实际冲程：" + chongcheng);
		float addChongCheng = 0;
		if(yxcc < chongcheng*3/4) {
			addChongCheng = zsp * (chongcheng-zsp-yxcc)/(chongcheng-zsp);
			System.out.println("附加有效冲程：" + addChongCheng);
		}

		int timeLong = 24;
		float chanyeliang = 0;

		float loss = 0;
//		if (wellModel.getLoss() != null) {
//			loss = Float.valueOf(wellModel.getLoss());
//		}
		float bz_zhc = 0;
		if(zhc<=0.1) {
			zhc = 1;
		}
		
		if(yxcc > 0.05) {
			if(wellModel.getZhc() != null && !wellModel.getZhc().equals("0")) {
				bz_zhc = Float.valueOf(wellModel.getZhc());
				chanyeliang = zuihoujieguo * timeLong + zuihoujieguo * timeLong*addChongCheng/yxcc - timeLong * (loss * 0.7f + loss*0.3f*zhc/bz_zhc) / 24; // 平均值+补偿值
			} else {
				chanyeliang = zuihoujieguo * timeLong + zuihoujieguo * timeLong*addChongCheng/yxcc - timeLong * loss / 24; // 平均值+补偿值
			}
			
			log.warn("*************产液量平均值：" + String.valueOf(zuihoujieguo + zuihoujieguo * addChongCheng/yxcc));
			log.warn("*************产液量【理论值】：" + String.valueOf(zuihoujieguo*24 + zuihoujieguo * addChongCheng*24/yxcc));
		} else {
			log.warn("*************产液量平均值：0");
			log.warn("*************产液量【理论值】：0");
		}

		log.debug("去掉漏失量[" + loss + "]之后的产液量：" + chanyeliang);
		System.out.println("----产液量：" + chanyeliang);
		BigDecimal yeBd = new BigDecimal(chanyeliang);
		float newChanyeliang = yeBd.setScale(3, BigDecimal.ROUND_HALF_UP)
				.floatValue();

		if (newChanyeliang < 0) {
			newChanyeliang = 0;
		}
		return String.valueOf(newChanyeliang);
	}
	
	/**
	 * 计算电功图量油
	 * @param wellNum
	 * @param startCalendar
	 * @return
	 */
	public String getDGTDayProduct(String wellNum, Calendar startCalendar) {
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(startCalendar.getTime());
		endCalendar.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		System.out.println("起始时间：" + sdf.format(startCalendar.getTime()));
		System.out.println("结束时间：" + sdf.format(endCalendar.getTime()));
		
		List<WellData> wellDataList = wellDataDao.getHistoryWellData(wellNum, startCalendar, endCalendar);
		
		if(wellDataList == null || wellDataList.isEmpty()) {
			return "0";
		}
		
		List<Float> resultList = new ArrayList<Float>(); // 24小时的产液量
		List<Float> yxccList = new ArrayList<Float>();	//有效冲程
		List<String> timeList = new ArrayList<String>();	//时间
		
		List<Float> zsList = new ArrayList<Float>();	//左上点
		
		List<Float> zhcList = new ArrayList<Float>();	//载荷差
		
		List<Float> chongchengList = new ArrayList<Float>();	//冲程
		
		WellModel wellModel = wellModelDao.getWellByNum(wellNum);
		
		for(WellData wellData : wellDataList) {
			if(wellData == null ||wellData.getDgt() == null|| (wellData.getDgt()[50]<0.5 && wellData.getDgt()[51]<0.5&&wellData.getDgt()[52]<0.5)) {
				continue;//数据均为0
			}
			//加入滤波算法
			MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getDgt());
			
			SGTDataComputerProcess sp = new SGTDataComputerProcess();
			Map<String,Object> map = sp.calcSGTData(wellData.getWeiyi(), wellData.getDgt(), 0,
					wellData.getChong_cheng_time(),
					Float.valueOf(wellModel.getBengjing()),
					Float.valueOf(wellModel.getOilDensity()),
					Float.valueOf(wellModel.getHanshui()));
			Float f = (Float) map.get("liquidProduct");
			resultList.add(f);
			System.out.println("设备时间：" + sdf.format(wellData.getDevice_time()));
			System.out.println("理论产液量：" + f);
			
			Float yxcc = (Float)map.get("youxiaochongcheng");
			yxccList.add(yxcc);
			System.out.println("有效冲程：" + yxcc);
			
			Float zhc = (Float) map.get("zhc");
			zhcList.add(zhc);
			
			Float zs = (Float) map.get("zs");
			zsList.add(zs);
			
			Float chongcheng = (Float)map.get("chongcheng");
			chongchengList.add(chongcheng);
			
			timeList.add(sdf.format(wellData.getDevice_time()));
			
			log.debug("设备时间：" + sdf.format(wellData.getDevice_time()));
		}

		log.warn("*************数据个数为：" + resultList.size());
		
		float zuihoujieguo = LvBo.zhongzhiLB(resultList); // 产液量结果，一个小时的
		
		float zhc = LvBo.zhongzhiLB(zhcList);	//载荷差
		System.out.println("【日均载荷差值】：" + zhc);
		
		float zsp = LvBo.zhongzhiLB(zsList);	//左上均值
		System.out.println("左上点：" + zsp);
		
		float yxcc = LvBo.zhongzhiLB(yxccList);	//有效冲程
		System.out.println("平均有效冲程：" + yxcc);
		
		float chongcheng = LvBo.zhongzhiLB(chongchengList);
		System.out.println("实际冲程：" + chongcheng);
		float addChongCheng = 0;
		if(yxcc < chongcheng*3/4) {
			addChongCheng = zsp * (chongcheng-zsp-yxcc)/(chongcheng-zsp);
			System.out.println("附加有效冲程：" + addChongCheng);
		}

		int timeLong = 24;
		float chanyeliang = 0;

		float loss = 0;
		if (wellModel.getLoss() != null) {
			loss = Float.valueOf(wellModel.getLoss());
		}
		float bz_zhc = 0;
		if(zhc<=0.1) {
			zhc = 1;
		}
		
		if(yxcc > 0.05) {
			if(wellModel.getZhc() != null && !wellModel.getZhc().equals("0")) {
				bz_zhc = Float.valueOf(wellModel.getZhc());
				chanyeliang = zuihoujieguo * timeLong + zuihoujieguo * timeLong*addChongCheng/yxcc - timeLong * (loss * 0.7f + loss*0.3f*zhc/bz_zhc) / 24; // 平均值+补偿值
			} else {
				chanyeliang = zuihoujieguo * timeLong + zuihoujieguo * timeLong*addChongCheng/yxcc - timeLong * loss / 24; // 平均值+补偿值
			}
			
			log.warn("*************产液量平均值：" + String.valueOf(zuihoujieguo + zuihoujieguo * addChongCheng/yxcc));
			log.warn("*************产液量【理论值】：" + String.valueOf(zuihoujieguo*24 + zuihoujieguo * addChongCheng*24/yxcc));
		} else {
			log.warn("*************产液量平均值：0");
			log.warn("*************产液量【理论值】：0");
		}

		log.debug("去掉漏失量[" + loss + "]之后的产液量：" + chanyeliang);
		System.out.println("----产液量：" + chanyeliang);
		BigDecimal yeBd = new BigDecimal(chanyeliang);
		float newChanyeliang = yeBd.setScale(3, BigDecimal.ROUND_HALF_UP)
				.floatValue();

		if (newChanyeliang < 0) {
			newChanyeliang = 0;
		}
		return String.valueOf(newChanyeliang);
	}
	
	/**
	 * 计算光杆缓下产液量
	 * @param wellNum
	 * @param startCalendar
	 * @return
	 */
	public String getDayProductGGHX(String wellNum, Calendar startCalendar) {
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(startCalendar.getTime());
		endCalendar.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		System.out.println("起始时间：" + sdf.format(startCalendar.getTime()));
		System.out.println("结束时间：" + sdf.format(endCalendar.getTime()));
		
		List<WellData> wellDataList = wellDataDao.getHistoryWellData(wellNum, startCalendar, endCalendar);
		
		if(wellDataList == null || wellDataList.isEmpty()) {
			return "0";
		}
		
		List<Float> resultList = new ArrayList<Float>(); // 24小时的产液量
		
		List<Float> yxccList = new ArrayList<Float>();	//有效冲程
		
		List<Float> zsList = new ArrayList<Float>();	//左上点
		
		List<Float> chongchengList = new ArrayList<Float>();	//冲程
		
		WellModel wellModel = wellModelDao.getWellByNum(wellNum);
		
		for(WellData wellData : wellDataList) {
			if(wellData == null || (wellData.getZaihe()[50]<0.5 && wellData.getZaihe()[51]<0.5&&wellData.getZaihe()[52]<0.5)) {
				continue;//数据均为0
			}
			
			//加入滤波算法
			MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getZaihe());
			
			GTDataComputerProcess sp = new GTDataComputerProcess();
			Map<String,Object> map = sp.calcSGTData(wellData.getWeiyi(), wellData.getZaihe(), 0,
					wellData.getChong_cheng_time(),
					Float.valueOf(wellModel.getBengjing()),
					Float.valueOf(wellModel.getOilDensity()),
					Float.valueOf(wellModel.getHanshui()),5);
			Float f = (Float) map.get("liquidProduct");
			resultList.add(f);
			System.out.println(sdf.format(wellData.getDevice_time()));
			System.out.println("理论产液量：" + f);
			
			Float yxcc = (Float) map.get("youxiaochongcheng");
			yxccList.add(yxcc);
			System.out.println("有效冲程：" + yxcc);
			
			Float zs = (Float) map.get("AX");
			zsList.add(zs);
			
			Float chongcheng = (Float)map.get("chongcheng");
			chongchengList.add(chongcheng);

		}
		log.warn("*************数据个数为：" + resultList.size());
		
		float zuihoujieguo = LvBo.zhongzhiLB_GGHX(resultList); // 产液量结果，一个小时的
		
		float zsp = LvBo.zhongzhiLB_GGHX(zsList);	//左上均值
		System.out.println("左上点：" + zsp);
		
		float yxcc = LvBo.zhongzhiLB_GGHX(yxccList);	//有效冲程
		System.out.println("平均有效冲程：" + yxcc);
		
		float chongcheng = LvBo.zhongzhiLB_GGHX(chongchengList);
		System.out.println("实际冲程：" + chongcheng);
		float addChongCheng = 0;
		if(yxcc < chongcheng*3/4) {
			addChongCheng = zsp * (chongcheng-zsp-yxcc)/(chongcheng-zsp);
			System.out.println("附加有效冲程：" + addChongCheng);
		}
		
		float chanyeliang = 0;
		if(yxcc>0.05) {
			chanyeliang = zuihoujieguo * 24 + zuihoujieguo * 24*addChongCheng/yxcc;
		}
		
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
		
		if(wellDataList == null || wellDataList.isEmpty()) {
			return "0";
		}
		
		List<Float> resultList = new ArrayList<Float>(); // 24小时的产液量
		List<Float> yxccList = new ArrayList<Float>();	//有效冲程
		List<String> timeList = new ArrayList<String>();	//时间
		
		List<Float> zhcList = new ArrayList<Float>();	//载荷差
		
		WellModel wellModel = wellModelDao.getWellByNum(wellNum);
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		for(WellData wellData : wellDataList) {
			if(wellData == null || (wellData.getZaihe()[50]<0.5 && wellData.getZaihe()[51]<0.5&&wellData.getZaihe()[52]<0.5)) {
				continue;//数据均为0
			}
			
			//加入滤波算法
			MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getZaihe());
			
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
			
			Float zhc = (Float) map.get("zhc");
			zhcList.add(zhc);
			
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
		int numStart = 0;
		int numEnd = numCount - 1;
		if(numCount == 0) {
			return "0";
		} else if(numCount<=5 && numCount>2) {
			numStart = 1;
			numEnd = numCount - 2;
		} else if(numCount > 5) {
			numStart = numCount * 1/3;
			numEnd = numCount * 2/3;
		}
		
		for(Float f : resultList) {
			System.out.println(f);
		}
		
		float zuihoujieguo = 0; // 产液量结果，一个小时的
		for (int i = numStart; i <= numEnd; i++) {
			zuihoujieguo += resultList.get(i);
		}
		log.debug(numEnd + " " + numStart + ":" + (numEnd - numStart));
		
		if((numEnd-numStart)<0) {
			zuihoujieguo = 0;
		} else {
			zuihoujieguo /= (numEnd - numStart + 1);
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
		
		float chanyeliang = 0;
		
		float zhc = LvBo.zhongzhiLB(zhcList);
		
		float bz_zhc = 0;
		if(wellModel.getZhc() != null && !wellModel.getZhc().equals("0")) {
			bz_zhc = Float.valueOf(wellModel.getZhc());
			chanyeliang = zuihoujieguo * timeLong - timeLong * (loss * 0.7f + loss*0.3f*zhc/bz_zhc) / 24; // 平均值+补偿值
		} else {
			chanyeliang = zuihoujieguo * timeLong - timeLong * loss / 24; // 平均值+补偿值
		}

//		float chanyeliang = zuihoujieguo * timeLong - timeLong * loss / 24; // 平均值+补偿值
		log.warn("最后产液量：" + chanyeliang);
		BigDecimal yeBd = new BigDecimal(chanyeliang);
		float newChanyeliang = yeBd.setScale(3, BigDecimal.ROUND_HALF_UP)
				.floatValue();

		if (newChanyeliang < 0) {
			newChanyeliang = 0;
		}
				
		return String.valueOf(newChanyeliang);
	}
	

	
	
	/**
	 * 新
	 * @param dtuNum
	 * @param startCalendar
	 * @return
	 */
	public String getMouthProduct(String dtuNum, Calendar startCalendar) {
		WellModel wellModel = wellModelDao.getWellByDtuNum(dtuNum);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(startCalendar.getTime());
		endCalendar.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		System.out.println("起始时间：" + sdf.format(startCalendar.getTime()));
		System.out.println("结束时间：" + sdf.format(endCalendar.getTime()));
		
		List<WellData> wellDataList = wellDataDao.getHistoryWellData(wellModel.getNum(), startCalendar, endCalendar);
		
		if(wellDataList == null || wellDataList.isEmpty()) {
			return "0";
		}
		
		List<Float> resultList = new ArrayList<Float>(); // 24小时的产液量
		List<Float> yxccList = new ArrayList<Float>();	//有效冲程
		List<String> timeList = new ArrayList<String>();	//时间
		
		List<Float> zhcList = new ArrayList<Float>();	//载荷差
		
		List<Float> zsList = new ArrayList<Float>();	//左上点
		
		
		
		for(WellData wellData : wellDataList) {
			if(wellData == null || (wellData.getZaihe()[50]<0.5 && wellData.getZaihe()[51]<0.5&&wellData.getZaihe()[52]<0.5)) {
				continue;//数据均为0
			}
			
			//加入滤波算法
			MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getZaihe());
			
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
			
			Float zhc = (Float) map.get("zhc");
			zhcList.add(zhc);
			
			Float zs = (Float) map.get("zs");
			zsList.add(zs);
			
			timeList.add(sdf.format(wellData.getDevice_time()));
			
			log.debug("设备时间：" + sdf.format(wellData.getDevice_time()));
		}
		
		for(int jjj=0;jjj<resultList.size();jjj++) {
			log.warn("【时间】：" + timeList.get(jjj));
			log.warn("有效冲程：" + yxccList.get(jjj));
			log.warn("产液量：" + resultList.get(jjj));
			
			log.warn("载荷差：" + zhcList.get(jjj));
		}
		
		log.warn("*************数据个数为：" + resultList.size());

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

		int numCount = resultList.size();
		int numStart = numCount * 1 / 3;
		int numEnd = numCount * 2 / 3;
		float zuihoujieguo = 0; // 产液量结果，一个小时的
		for (int i = numStart; i < numEnd; i++) {
			log.warn("产液量选取点："+ resultList.get(i));
			zuihoujieguo += resultList.get(i);
		}
		log.debug(numEnd + " " + numStart + ":" + (numEnd - numStart));
		
		if((numEnd-numStart)<=0) {
			zuihoujieguo = 0;
		} else {
			zuihoujieguo /= (numEnd - numStart);
		}

		log.warn("*************产液量平均值：" + zuihoujieguo);
		log.warn("*************产液量直接值：" + zuihoujieguo*24);
		
		float zhc = LvBo.zhongzhiLB(zhcList);
		System.out.println("日均载荷差值：" + zhc);
		
		float yxcc = LvBo.zhongzhiLB(yxccList);
		
		float zsp = LvBo.zhongzhiLB(zsList);	//左上均值
		
		int timeLong = 24;
		float chanyeliang = 0;

		float loss = 0;
		if (wellModel.getLoss() != null) {
			loss = Float.valueOf(wellModel.getLoss());
		}
		float bz_zhc = 0;
		if(wellModel.getZhc() != null && !wellModel.getZhc().equals("0")) {
			bz_zhc = Float.valueOf(wellModel.getZhc());
			chanyeliang = zuihoujieguo * timeLong - timeLong * (loss * 0.7f + loss*0.3f*zhc/bz_zhc) / 24; // 平均值+补偿值
		} else {
			chanyeliang = zuihoujieguo * timeLong - timeLong * loss / 24; // 平均值+补偿值
		}

		log.debug("去掉漏失量[" + loss + "]之后的产液量：" + chanyeliang);
		BigDecimal yeBd = new BigDecimal(chanyeliang);
		float newChanyeliang = yeBd.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();

		if (newChanyeliang < 0) {
			newChanyeliang = 0;
		}
		
		
		return String.valueOf(newChanyeliang);
	}
	
	public String getZHC(String wellNum, Calendar startCalendar) {
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(startCalendar.getTime());
		endCalendar.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		
		List<WellData> wellDataList = wellDataDao.getHistoryWellData(wellNum, startCalendar, endCalendar);
		
		if(wellDataList == null || wellDataList.isEmpty()) {
			return "0";
		}
		
		List<Float> resultList = new ArrayList<Float>(); // 24小时的产液量
		List<Float> yxccList = new ArrayList<Float>();	//有效冲程
		List<String> timeList = new ArrayList<String>();	//时间
		
		List<Float> zhcList = new ArrayList<Float>();	//载荷差
		
		WellModel wellModel = wellModelDao.getWellByNum(wellNum);
		System.out.println(wellNum);
		
		for(WellData wellData : wellDataList) {
			if(wellData == null || (wellData.getZaihe()[50]<0.5 && wellData.getZaihe()[51]<0.5&&wellData.getZaihe()[52]<0.5)) {
				continue;//数据均为0
			}
			
			//加入滤波算法
			MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getZaihe());
			
			SGTDataComputerProcess sp = new SGTDataComputerProcess();
			Map<String,Object> map = sp.calcSGTData(wellData.getWeiyi(), wellData.getZaihe(), 0,
					wellData.getChong_cheng_time(),
					Float.valueOf(wellModel.getBengjing()),
					Float.valueOf(wellModel.getOilDensity()),
					Float.valueOf(wellModel.getHanshui()));
			
			Float zhc = (Float) map.get("zhc");
			zhcList.add(zhc);
			
			log.debug("设备时间：" + sdf.format(wellData.getDevice_time()));
		}
		
		log.warn("*************数据个数为：" + resultList.size());

		
		float zhc = LvBo.zhongzhiLB(zhcList);
		System.out.println("日均载荷差值：" + zhc);
		if (zhc < 0) {
			zhc = 0;
		}
		
		return String.valueOf(zhc);
	}
	
	/**
	 * 获得有效冲程
	 * @param wellNum
	 * @param startDate
	 * @return
	 */
	public String getYXCC(String wellNum, Calendar startCalendar) {
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(startCalendar.getTime());
		endCalendar.add(Calendar.DAY_OF_MONTH, 1);
		
		List<WellData> wellDataList = wellDataDao.getHistoryWellData(wellNum, startCalendar, endCalendar);
		
		if(wellDataList == null || wellDataList.isEmpty()) {
			return "0";
		}
		
		List<Float> yxccList = new ArrayList<Float>();	//有效冲程
		
		WellModel wellModel = wellModelDao.getWellByNum(wellNum);
		
		for(WellData wellData : wellDataList) {
			if(wellData == null || (wellData.getZaihe()[50]<0.5 && wellData.getZaihe()[51]<0.5&&wellData.getZaihe()[52]<0.5)) {
				continue;//数据均为0
			}
			//加入滤波算法
			MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getZaihe());
			
			SGTDataComputerProcess sp = new SGTDataComputerProcess();
			Map<String,Object> map = sp.calcSGTData(wellData.getWeiyi(), wellData.getZaihe(), 0,
					wellData.getChong_cheng_time(),
					Float.valueOf(wellModel.getBengjing()),
					Float.valueOf(wellModel.getOilDensity()),
					Float.valueOf(wellModel.getHanshui()));
			
			Float yxcc = (Float)map.get("youxiaochongcheng");
			yxccList.add(yxcc);

		}
		float yxcc = LvBo.zhongzhiLB(yxccList);

		if (yxcc < 0) {
			yxcc = 0;
		}

		return String.valueOf(yxcc);
	}

}
