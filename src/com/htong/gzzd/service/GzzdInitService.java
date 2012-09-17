package com.htong.gzzd.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.htong.alg.MyLvBo;
import com.htong.dao.GzzdHistoryDao;
import com.htong.dao.WellDataDao;
import com.htong.dao.WellModelDao;
import com.htong.domain.GzzdRealtimeModel;
import com.htong.domain.WellData;
import com.htong.domain.WellModel;
import com.htong.gzzd.GTDataComputerProcess;

/**
 * 故障诊断初始化
 * @author 赵磊
 *
 */
@Service
public class GzzdInitService {
	
	private static final Logger log = Logger.getLogger(GzzdInitService.class);
	@Autowired
	private GzzdHistoryDao gzzdHistoryDao;
	@Autowired
	private WellModelDao wellModelDao;
	@Autowired
	private WellDataDao wellDataDao;
	
	public static Map<String, GzzdRealtimeModel> gzzdRealtimeMap = new ConcurrentHashMap<String, GzzdRealtimeModel>();
	
	private void init() {
		log.debug("init");
		List<WellModel> wellList = wellModelDao.getAllWells();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(wellList != null && !wellList.isEmpty()) {
			for(WellModel well : wellList) {
				WellData latestWellData = wellDataDao.getLatedWellDataByWellNum(well.getNum());
				WellData secondLatestWellData = wellDataDao.getSecondLatedWellDataByWellNum(well.getNum());
				
				GzzdRealtimeModel gzzdRealtimeModel = gzzdRealtimeMap.get(well.getNum());
				
				if(latestWellData != null && secondLatestWellData != null) {
					long date1 = latestWellData.getDevice_time().getTime();
					long date2 = secondLatestWellData.getDevice_time().getTime();
					long currentDate = System.currentTimeMillis();
					long result1 = date1-date2-5*3600*1000;//5个小时内
					long result2 = currentDate - date2 -5*3600*1000;//5个小时内
					log.debug(result1+ " " + result2);
					if(result1 > 0 && result2>0) {
						//加入滤波算法
						MyLvBo.myLvBo(latestWellData.getWeiyi(), latestWellData.getZaihe());
						
						GTDataComputerProcess sp_1 = new GTDataComputerProcess();
						Map<String,Object> calcMap_1 = sp_1.calcSGTData(latestWellData.getWeiyi(), latestWellData.getZaihe(), 0,
								latestWellData.getChong_cheng_time(),
								Float.valueOf(well.getBengjing()),
								Float.valueOf(well.getOilDensity()),
								Float.valueOf(well.getHanshui()),15);
						int fault_code_1 = (Integer) calcMap_1.get("gzzd");
						int fault_level_1 = (Integer) calcMap_1.get("fault_level");
						
						MyLvBo.myLvBo(secondLatestWellData.getWeiyi(), secondLatestWellData.getZaihe());
						
						GTDataComputerProcess sp_2 = new GTDataComputerProcess();
						Map<String,Object> calcMap_2 = sp_2.calcSGTData(secondLatestWellData.getWeiyi(), secondLatestWellData.getZaihe(), 0,
								secondLatestWellData.getChong_cheng_time(),
								Float.valueOf(well.getBengjing()),
								Float.valueOf(well.getOilDensity()),
								Float.valueOf(well.getHanshui()),15);
						int fault_code_2 = (Integer) calcMap_2.get("gzzd");
						int fault_level_2 = (Integer) calcMap_2.get("fault_level");
						
						if(fault_code_1 ==fault_code_2) {
							if(gzzdRealtimeMap.get(well.getNum())==null) {
								GzzdRealtimeModel gzzdRealtimeModel = new GzzdRealtimeModel();
								gzzdRealtimeModel.setWellNum(well.getNum());
								gzzdRealtimeModel.setDtuNum(well.getDtuId());
								gzzdRealtimeModel.setFaultCode(fault_code_1);
								gzzdRealtimeModel.setFaultLevel(fault_level_1>=fault_level_2?fault_level_1:fault_level_2);
								if(fault_code_1>0) {
									gzzdRealtimeModel.setFaultFlag(true);
								} else {
									gzzdRealtimeModel.setFaultFlag(false);
								}
								
								gzzdRealtimeModel.setTime(sdf.format(Calendar.getInstance().getTime()));
								
								gzzdRealtimeMap.put(well.getNum(), gzzdRealtimeModel);
							} else {
								GzzdRealtimeModel gzzdRealtimeModel = gzzdRealtimeMap.get(well.getNum());
								int fault_code_old = gzzdRealtimeModel.getFaultCode();
								int fault_level_old = gzzdRealtimeModel.getFaultLevel();
								if(fault_code_old != fault_code_1) {//故障类型变
									if(fault_code_1 > 0) {
										gzzdRealtimeModel.setFaultFlag(true);
									} else {
										gzzdRealtimeModel.setFaultFlag(false);
									}
									gzzdRealtimeModel.setFaultCode(fault_code_1);
									gzzdRealtimeModel.setFaultLevel(getMax(fault_level_old, fault_level_1, fault_level_2));
								} else {
									
									if(fault_level_old != fault_level_1) {//故障程度变
										gzzdRealtimeModel.setFaultCode(fault_code_1);
										gzzdRealtimeModel.setFaultFlag(true);
									}
								}
							}
						}
					}
				}
			}
		}
		
	}
	
	private int getMax(int int1,int int2,int int3) {
		if(int1<int2) {
			int1=int2;
		}
		if(int1<int3){
			int1=int3;
		}
		return int1;
	}

}
