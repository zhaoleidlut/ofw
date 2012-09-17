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

import com.htong.domain.ElecData;
import com.htong.service.ElecDataService;

@Controller
public class ElecDataController {
	@Autowired
	private ElecDataService elecDataService;
	
	@ResponseBody
	@RequestMapping("/getElecData.html")
	public Map<String, Object> getElecData(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "rows", required = true) int rows,
			@RequestParam(value = "wellNum", required = true) String wellNum) {
		Map<String, Object> elecDataMap = new HashMap<String, Object>();
		
		List<ElecData> elecDataList = elecDataService.getElecDataByWellNum(wellNum);
		if(elecDataList != null && !elecDataList.isEmpty()) {
			int len = elecDataList.size();
			System.out.println("数据长度：" + len);
			List<ElecData> resultData = new ArrayList<ElecData>();
			int start = (page-1)*rows;
			int end = (len - page*rows)>0?(page*rows):len;
			System.out.println("起始点：" + start);
			System.out.println("结束点：" + end);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(;start<end;start++) {
				ElecData elecData = elecDataList.get(start);
				String time = sdf.format(elecData.getSave_time());
				elecData.setTime(time);
				resultData.add(elecData);
			}
			elecDataMap.put("rows", resultData);
			elecDataMap.put("total", len);
		} else {
			elecDataMap.put("total", 0);
			elecDataMap.put("rows", "");
		}
		

		return elecDataMap;
	}

}
