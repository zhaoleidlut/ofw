package com.htong.gzzd.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.htong.alg.MyLvBo;
import com.htong.dao.GzzdHistoryDao;
import com.htong.domain.GzzdHistoryModel;
import com.htong.domain.GzzdRealtimeModel;
import com.htong.domain.WellData;
import com.htong.domain.WellModel;
import com.htong.gzzd.GTDataComputerProcess;

public class GzzdThread extends Thread {
	private static final Logger log = Logger.getLogger(GzzdThread.class);
	private GzzdHistoryDao gzzdHistoryDao;
	private WellData latestWellData;
	private WellData secondLatestWellData;
	private WellModel well;

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static int fault_level_limit = 2;

	public GzzdThread(GzzdHistoryDao gzzdHistoryDao, WellData latestWellData,
			WellData secondLatestWellData, WellModel well) {
		this.gzzdHistoryDao = gzzdHistoryDao;
		this.latestWellData = latestWellData;
		this.secondLatestWellData = secondLatestWellData;
		this.well = well;
	}

	@Override
	public void run() {
		log.debug(well.getNum() + "诊断程序执行start，执行时间：" + sdf.format(new Date()) + Thread.currentThread().getName());
		gzzd();
		log.debug(well.getNum() + "实时诊断队列个数：" + GzzdInitService.gzzdRealtimeMap.size());
		log.debug(well.getNum() + "诊断程序执行  end，执行时间：" + sdf.format(new Date()) + Thread.currentThread().getName());
	}

	private void gzzd() {
		try {
			sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		if (latestWellData != null && secondLatestWellData != null) {
			GzzdRealtimeModel gzzdRealtimeModel = GzzdInitService.gzzdRealtimeMap
					.get(well.getNum());

			long date1 = latestWellData.getDevice_time().getTime();
			long date2 = secondLatestWellData.getDevice_time().getTime();
			boolean ok_time = false;//date1与date2是否在一个小时与3个小时之间
			if(date1 - date2>3555*1000 && date1-date2<3655*1000) {
				ok_time = true;
			}
			log.debug(well.getNum() + "  最近时间："
					+ sdf.format(latestWellData.getDevice_time()));
			log.debug(well.getNum() + " 次最近时间："
					+ sdf.format(secondLatestWellData.getDevice_time()));

			boolean ok = false;//内存中数据是否与date1时间小于1个小时
			if (gzzdRealtimeModel == null) {
				ok = true;
			} else {
				long date3 = 0;
				try {
					date3 = sdf.parse(gzzdRealtimeModel.getDeviceTime())
							.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (date1 - date3 > 3555 * 1000) {// 大于1个小时
					ok = true;
				}
			}

			if (ok_time && ok) {
				// 加入滤波算法
				MyLvBo.myLvBo(latestWellData.getWeiyi(),
						latestWellData.getZaihe());

				GTDataComputerProcess sp_1 = new GTDataComputerProcess();
				Map<String, Object> calcMap_1 = sp_1.calcSGTData(
						latestWellData.getWeiyi(), latestWellData.getZaihe(),
						0, latestWellData.getChong_cheng_time(),
						Float.valueOf(well.getBengjing()),
						Float.valueOf(well.getOilDensity()),
						Float.valueOf(well.getHanshui()), 15);
				int fault_code_1 = (Integer) calcMap_1.get("gzzd");
				int fault_level_1 = (Integer) calcMap_1.get("fault_level");

				MyLvBo.myLvBo(secondLatestWellData.getWeiyi(),
						secondLatestWellData.getZaihe());

				GTDataComputerProcess sp_2 = new GTDataComputerProcess();
				Map<String, Object> calcMap_2 = sp_2.calcSGTData(
						secondLatestWellData.getWeiyi(),
						secondLatestWellData.getZaihe(), 0,
						secondLatestWellData.getChong_cheng_time(),
						Float.valueOf(well.getBengjing()),
						Float.valueOf(well.getOilDensity()),
						Float.valueOf(well.getHanshui()), 15);
				int fault_code_2 = (Integer) calcMap_2.get("gzzd");
				int fault_level_2 = (Integer) calcMap_2.get("fault_level");

				if (fault_code_1 == fault_code_2) {
					if (gzzdRealtimeModel == null) {
						gzzdRealtimeModel = new GzzdRealtimeModel();

						gzzdRealtimeModel.setWellNum(well.getNum());
						gzzdRealtimeModel.setDtuNum(well.getDtuId());
						gzzdRealtimeModel.setFaultCode(String.valueOf(fault_code_1));
						gzzdRealtimeModel
								.setFaultLevel(String.valueOf(fault_level_1 >= fault_level_2 ? fault_level_1
										: fault_level_2));
						if (fault_code_1 > 0) {
							gzzdRealtimeModel.setFaultFlag(true);
							gzzdRealtimeModel.setHasConfirm(false);

							GzzdHistoryModel gzzdHistoryModel = new GzzdHistoryModel();
							gzzdHistoryModel.setWellNum(well.getNum());
							gzzdHistoryModel.setDtuNum(well.getDtuId());
							gzzdHistoryModel.setFaultCode(String.valueOf(fault_code_1));
							gzzdHistoryModel
									.setFaultLevel(String.valueOf(fault_level_1 >= fault_level_2 ? fault_level_1
											: fault_level_2));
							gzzdHistoryModel
									.setGzzdTime(sdf.format(new Date()));
							gzzdHistoryModel.setDeviceTime(sdf
									.format(latestWellData.getDevice_time()));
							gzzdHistoryModel.setActionInfo("发生故障");
							// gzzdHistoryModel.setActionUser((String)ActionContext.getContext().getSession().get("user"));
							// log.debug((String)ActionContext.getContext().getSession().get("user"));

							saveToHistoryDatabase(gzzdHistoryModel);// 写入历史数据库
						} else {
							gzzdRealtimeModel.setFaultFlag(false);
							gzzdRealtimeModel.setHasConfirm(false);
						}
						gzzdRealtimeModel.setGzzdTime(sdf.format(new Date()));
						gzzdRealtimeModel.setDeviceTime(sdf
								.format(latestWellData.getDevice_time()));

						GzzdInitService.gzzdRealtimeMap.put(well.getNum(),
								gzzdRealtimeModel);
					} else {
						int fault_code_old = Integer.valueOf(gzzdRealtimeModel.getFaultCode());
						int fault_level_old = Integer.valueOf(gzzdRealtimeModel.getFaultLevel());
						if (fault_code_old != fault_code_1) {// 故障类型变
							if (fault_code_1 > 0) { // 发生故障
								gzzdRealtimeModel.setFaultFlag(true);
								gzzdRealtimeModel.setHasConfirm(false);

								GzzdHistoryModel gzzdHistoryModel = new GzzdHistoryModel();
								gzzdHistoryModel.setWellNum(well.getNum());
								gzzdHistoryModel.setDtuNum(well.getDtuId());
								gzzdHistoryModel.setFaultCode(String.valueOf(fault_code_1));
								gzzdHistoryModel
										.setFaultLevel(String.valueOf(fault_level_1 >= fault_level_2 ? fault_level_1
												: fault_level_2));
								gzzdHistoryModel.setGzzdTime(sdf
										.format(new Date()));
								gzzdHistoryModel
										.setDeviceTime(sdf
												.format(latestWellData
														.getDevice_time()));
								if (fault_code_old == 0) {
									gzzdHistoryModel.setActionInfo("发生故障");
								} else {
									gzzdHistoryModel.setActionInfo("故障改变");
								}
								saveToHistoryDatabase(gzzdHistoryModel);// 写入历史数据库
							} else { // 恢复正常
								gzzdRealtimeModel.setFaultFlag(false);
								gzzdRealtimeModel.setHasConfirm(false);

								GzzdHistoryModel gzzdHistoryModel = new GzzdHistoryModel();
								gzzdHistoryModel.setWellNum(well.getNum());
								gzzdHistoryModel.setDtuNum(well.getDtuId());
								gzzdHistoryModel.setFaultCode(String.valueOf(fault_code_1));
								gzzdHistoryModel.setFaultLevel("0");
								gzzdHistoryModel.setGzzdTime(sdf
										.format(new Date()));
								gzzdHistoryModel
										.setDeviceTime(sdf
												.format(latestWellData
														.getDevice_time()));
								gzzdHistoryModel.setActionInfo("故障恢复");
								saveToHistoryDatabase(gzzdHistoryModel);// 写入历史数据库
							}
							gzzdRealtimeModel.setFaultCode(String.valueOf(fault_code_1));
							gzzdRealtimeModel.setFaultLevel(String.valueOf(getMax(
									fault_level_old, fault_level_1,
									fault_level_2)));
							gzzdRealtimeModel.setGzzdTime(sdf.format(new Date()));
							gzzdRealtimeModel.setDeviceTime(sdf
									.format(latestWellData.getDevice_time()));
						} else {// 故障类型未变
							//两次程度相等，且均大于阈值
							if (fault_code_1 == fault_code_2 && Math.abs(fault_level_old - fault_level_1) >= fault_level_limit
									&& Math.abs(fault_level_old - fault_level_2) >= fault_level_limit) {// 故障程度变
								int fault_level = fault_level_1 >= fault_level_2 ? fault_level_1
										: fault_level_2;
								if (fault_level - fault_code_old > 0) {// 故障程度变高
									gzzdRealtimeModel.setFaultFlag(true);
									gzzdRealtimeModel.setHasConfirm(false);
								}
								gzzdRealtimeModel.setFaultLevel(String.valueOf(fault_level));
								gzzdRealtimeModel.setGzzdTime(sdf.format(new Date()));
								gzzdRealtimeModel
										.setDeviceTime(sdf
												.format(latestWellData
														.getDevice_time()));

								GzzdHistoryModel gzzdHistoryModel = new GzzdHistoryModel();
								gzzdHistoryModel.setWellNum(well.getNum());
								gzzdHistoryModel.setDtuNum(well.getDtuId());
								gzzdHistoryModel.setFaultCode(String.valueOf(fault_code_1));
								gzzdHistoryModel.setFaultLevel(String.valueOf(fault_level));
								gzzdHistoryModel.setGzzdTime(sdf
										.format(new Date()));
								gzzdHistoryModel
										.setDeviceTime(sdf
												.format(latestWellData
														.getDevice_time()));
								if (fault_level - fault_code_old > 0) {// 故障程度变高
									gzzdHistoryModel.setActionInfo("故障程度变高");
								} else {
									gzzdHistoryModel.setActionInfo("故障程度变低");
								}
								saveToHistoryDatabase(gzzdHistoryModel);// 写入历史数据库
							}
						}
					}
				}
			}

		}
	}

	private int getMax(int int1, int int2, int int3) {
		if (int1 < int2) {
			int1 = int2;
		}
		if (int1 < int3) {
			int1 = int3;
		}
		return int1;
	}

	private synchronized void saveToHistoryDatabase(
			GzzdHistoryModel gzzdHistoryModel) {
		gzzdHistoryDao.saveGzzdHistory(gzzdHistoryModel);// 写入历史数据库
	}
}
