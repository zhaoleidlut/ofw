package com.htong.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.testng.log4testng.Logger;

import com.htong.domain.EnergyData;
import com.htong.service.EnergyDataService;

@Controller
public class EnergyDataController {
	private static final Logger log = Logger.getLogger(EnergyDataController.class);
	@Autowired
	private EnergyDataService energyDataService;

	@ResponseBody
	@RequestMapping("/getEnergyData.html")
	public Map<String, Object> getEnergyData(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "rows", required = true) int rows,
			@RequestParam(value = "wellNum", required = true) String wellNum) {
		Map<String, Object> energyDataMap = new HashMap<String, Object>();
		
		List<EnergyData> energyDataList = energyDataService.getEnergyData(wellNum);
		if(energyDataList != null && !energyDataList.isEmpty()) {
			int len = energyDataList.size();
			System.out.println("数据长度：" + len);
			List<EnergyData> resultData = new ArrayList<EnergyData>();
			int start = (page-1)*rows;
			int end = (len - page*rows)>0?(page*rows):len;
			System.out.println("起始点：" + start);
			System.out.println("结束点：" + end);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for(;start<end;start++) {
				EnergyData energyData = energyDataList.get(start);
				String time = sdf.format(energyData.getSave_time());
				energyData.setTime(time);
				resultData.add(energyData);
			}
			energyDataMap.put("rows", resultData);
			energyDataMap.put("total", len);
		} else {
			energyDataMap.put("total", 0);
			energyDataMap.put("rows", "");
		}
		

		return energyDataMap;
	}

}
