package com.htong.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htong.domain.WellData;
import com.htong.domain.WellModel;
import com.htong.model.DTUStatusVar;
import com.htong.model.MonthProductModel;
import com.htong.model.WellHistoryStatus;
import com.htong.model.WellStatus;
import com.htong.service.WellDataService;
import com.htong.service.WellProductService;
import com.htong.service.WellService;
import com.htong.ws.DtuStatusModel;
import com.htong.ws.WebServiceClass;
import com.htong.ws.WebServiceClassService;

@Controller
public class WellStatusController {

	@Autowired
	private WellDataService wellDataService;
	@Autowired
	private WellService wellService;
	@Autowired
	private WellProductService wellPruductService;

	private static final Logger log = Logger
			.getLogger(WellStatusController.class);

	@RequestMapping("/getAllWellStatus.html")
	@ResponseBody
	public Map<String, Object> getAllWellStatus() {
		Map<String, Object> wellStatusMap = new HashMap<String, Object>();
		WebServiceClass wsc = null;
		try {
			wsc = new WebServiceClassService().getWebServiceClassPort();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("WebService连接不上！");
			wellStatusMap.put("conn", "no");
			wellStatusMap.put("rows", "");
			wellStatusMap.put("total", 0);
			e1.printStackTrace();
			return wellStatusMap;
		}
		wellStatusMap.put("conn", "yes");
		List<DtuStatusModel> statusList = wsc.getDTUStatus();
		if (statusList != null && !statusList.isEmpty()) {
			List<WellStatus> wellStatusList = new ArrayList<WellStatus>();

			for (DtuStatusModel dtuStatusModel : statusList) {
				WellStatus var = new WellStatus();
				var.setWellNum(dtuStatusModel.getWellNum());

				if (dtuStatusModel.getHeartBeatTime() == null) {
					var.setStatus("已停井");
				} else {
					String time = dtuStatusModel.getHeartBeatTime();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date date = null;
					try {
						date = sdf.parse(time);
					} catch (ParseException e) {
						var.setStatus("已停井");
						e.printStackTrace();
						continue;
					}
					if ((new Date().getTime() - date.getTime()) > 2 * 60 * 60 * 1000) {
						var.setStatus("已停井");
					} else {
						var.setStatus("运行");
					}
				}
				wellStatusList.add(var);
			}
			wellStatusMap.put("rows", wellStatusList);
			wellStatusMap.put("total", statusList.size());
		} else {
			wellStatusMap.put("rows", "");
			wellStatusMap.put("total", 0);
		}
		return wellStatusMap;
	}

	@RequestMapping("/getAllWellHistoryStatus.html")
	@ResponseBody
	public Map<String, Object> getAllWellHistoryStatus() {
		Map<String, Object> wellStatusMap = new HashMap<String, Object>();
		List<WellModel> wellList = wellService.getAllWells();
		List<WellHistoryStatus> wellHisStatusList = new ArrayList<WellHistoryStatus>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (WellModel well : wellList) {
			WellHistoryStatus whs = new WellHistoryStatus();
			whs.setWellNum(well.getNum());
			WellData wellData = wellDataService.getLatestWellDataByWellNum(well
					.getNum());
			if (wellData == null) {
				whs.setStart("");
				whs.setStop("");
			} else {
				whs.setStart(sdf.format(wellData.getStart_time()));
				whs.setStop(sdf.format(wellData.getStop_time()));
			}
			wellHisStatusList.add(whs);
		}
		wellStatusMap.put("rows", wellHisStatusList);
		wellStatusMap.put("total", wellList.size());
		return wellStatusMap;
	}
}
