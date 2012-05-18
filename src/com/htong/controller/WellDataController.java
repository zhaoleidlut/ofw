package com.htong.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.htong.alg.LvBo;
import com.htong.domain.WellData;
import com.htong.service.WellDataService;

@Controller
public class WellDataController {
	@Autowired
	private WellDataService wellDataService;
	
	private static final Logger log = Logger.getLogger(WellDataService.class);
	
	/**
	 * 获得实时示功图曲线数据
	 * @param wellNum
	 * @return
	 */
	@RequestMapping("/getSGTPlotData.html")
	@ResponseBody
	public Map<String, Object> getSGTPlotData(@RequestParam(value="wellNum", required=true)String wellNum) {

		WellData wellData = wellDataService.getLatestWellDataByWellNum(wellNum);
		
		JSONArray jsonArrayResult = new JSONArray();	//最终的数组
		JSONArray jsonArray = new JSONArray();
		
		LvBo.lvBo(wellData.getWeiyi(), wellData.getZaihe());
		
		JSONArray weiyi = JSONArray.fromObject(wellData.getWeiyi());
		JSONArray zaihe = JSONArray.fromObject(wellData.getZaihe());
		log.debug(weiyi.size());
		for(int i=0;i<weiyi.size();i++) {
			JSONArray json = new JSONArray();
			json.add(weiyi.get(i));
			json.add(zaihe.get(i));
			
			jsonArray.add(json);
		}
		jsonArrayResult.add(jsonArray);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", jsonArrayResult);

		return map;
	}
}
