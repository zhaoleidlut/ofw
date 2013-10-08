package com.htong.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.testng.log4testng.Logger;

import com.htong.model.DTUStatusVar;
import com.htong.ws.DtuStatusModel;
import com.htong.ws.WebServiceClass;
import com.htong.ws.WebServiceClassService;

@Controller
public class WebServiceController {
	
	private static final Logger log = Logger.getLogger(WebServiceController.class);
	
	@ResponseBody
	@RequestMapping("/getDTUStatus.html")
	public Map<String, Object> getDTUStatus() {
		Map<String, Object> dtuStatusMap = new HashMap<String, Object>();
		WebServiceClass wsc = null;
		try {
			wsc = new WebServiceClassService().getWebServiceClassPort();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("WebService连接不上！");
			dtuStatusMap.put("conn", "no");
			dtuStatusMap.put("rows", "");
			dtuStatusMap.put("total", 0);
			e1.printStackTrace();
			return dtuStatusMap;
		}
		dtuStatusMap.put("conn", "yes");
		List<DtuStatusModel> statusList = wsc.getDTUStatus();
		if(statusList != null && !statusList.isEmpty()) {
			List<DTUStatusVar> dtuList = new ArrayList<DTUStatusVar>();
			for(DtuStatusModel dtuStatusModel : statusList) {
				DTUStatusVar var = new DTUStatusVar();
				var.setWellNum(dtuStatusModel.getWellNum());
				var.setDtuNum(dtuStatusModel.getDtuNum());
				if(dtuStatusModel.isConnStatus() == null || !dtuStatusModel.isConnStatus()) {
					var.setConnStatus("未连接");
				} else {
					var.setConnStatus("已连接");
				}
//				if(dtuStatusModel.isCommStatus() == null || !dtuStatusModel.isCommStatus()) {
//					var.setCommStatus("离线");
//				} else {
//					var.setCommStatus("在线");
//				}
				if(dtuStatusModel.getHeartBeatTime() == null) {
					var.setHeartBeatTime("无心跳");
					var.setCommStatus("离线");
				} else {
					var.setHeartBeatTime(dtuStatusModel.getHeartBeatTime());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = null;
					try {
						date = sdf.parse(dtuStatusModel.getHeartBeatTime());
					} catch (ParseException e) {
						var.setCommStatus("离线");
						e.printStackTrace();
					}
					if ((new Date().getTime() - date.getTime()) > 18 * 60 * 1000) {
						var.setCommStatus("离线");
					} else {
						var.setCommStatus("在线");
					}
				}
				
				dtuList.add(var);
			}
			dtuStatusMap.put("rows", dtuList);
			dtuStatusMap.put("total", statusList.size());
		} else {
			dtuStatusMap.put("rows", "");
			dtuStatusMap.put("total", 0);
		}
		
		return dtuStatusMap;
	}

}
