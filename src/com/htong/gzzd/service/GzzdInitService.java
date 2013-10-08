package com.htong.gzzd.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.htong.dao.GzzdHistoryDao;
import com.htong.dao.WellDataDao;
import com.htong.dao.WellModelDao;
import com.htong.domain.GzzdRealtimeModel;
import com.htong.domain.WellData;
import com.htong.domain.WellModel;

/**
 * 故障诊断初始化
 * 
 * @author 赵磊
 * 
 */
@Service
public class GzzdInitService extends Thread {

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
//		this.start();
	}

	@Override
	public void run() {
		List<WellModel> wellList = wellModelDao.getAllWells();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		//先执行一次
		if (wellList != null && !wellList.isEmpty()) {
			for (int i = 0; i < wellList.size(); i++) {
				WellModel well = wellList.get(i);
				log.debug(well.getNum());

				GzzdThread gzzdThread = new GzzdThread(gzzdHistoryDao,wellDataDao, well);
				gzzdThread.start();
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			log.debug("执行完一遍故障诊断线程！");
		}
		

		c.add(Calendar.HOUR, 1);
		long nextTimeInterval = c.getTime().getTime();

		while (true) {
			long currentTime = System.currentTimeMillis();
			if (currentTime < nextTimeInterval) {
				//log.debug("下次时间：" + nextTimeInterval + "当前时间" + System.currentTimeMillis());
				//log.debug("实时诊断队列个数：" + gzzdRealtimeMap.size());
				try {
					sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			} else {
				if (wellList != null && !wellList.isEmpty()) {
					for (int i = 0; i < wellList.size(); i++) {
						WellModel well = wellList.get(i);
						GzzdThread gzzdThread = new GzzdThread(gzzdHistoryDao,wellDataDao, well);
						gzzdThread.start();
						try {
							sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					log.debug("执行完一遍故障诊断线程！");
				}

				c.add(Calendar.HOUR, 1);
				nextTimeInterval = c.getTime().getTime();
			}
			
			try {
				sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
