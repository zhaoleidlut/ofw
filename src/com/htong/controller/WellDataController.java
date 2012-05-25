package com.htong.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.htong.alg.LvBo;
import com.htong.alg.SGTDataComputerProcess;
import com.htong.domain.WellData;
import com.htong.domain.WellModel;
import com.htong.service.WellDataService;
import com.htong.service.WellService;

@Controller
public class WellDataController {
	@Autowired
	private WellDataService wellDataService;
	@Autowired
	private WellService wellService;
	
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
		
		float chongChengTime = wellData.getChong_cheng_time();
		
		WellModel wellModel = wellService.getWellByNum(wellNum);
		
		SGTDataComputerProcess sp = new SGTDataComputerProcess();
		Map<String, Object> result = sp.calcSGTData(wellData.getWeiyi(), wellData.getZaihe(), 0,
				chongChengTime, Float.valueOf(wellModel.getBengjing()),
				Float.valueOf(wellModel.getOilDensity()),
				Float.valueOf(wellModel.getHanshui()));

		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(wellData.getDevice_time()); // 数据时间
		
		BigDecimal bd = new BigDecimal(60/chongChengTime);
		float newChongCi = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		String chongCi = String.valueOf(newChongCi); // 冲次

		float shijiChongCheng = (Float) result.get("chongcheng");
		String chongCheng = String.valueOf(shijiChongCheng); // 实际冲程

		float minZaihe = (Float) result.get("minZaihe");
		String minZaiHe = String.valueOf(minZaihe); // 最小载荷

		float maxZaihe = (Float) result.get("maxZaihe");
		String maxZaiHe = String.valueOf(maxZaihe); // 最大载荷
		
		Map<String, Object> map = new HashMap<String, Object>();
		//位移 载荷
		map.put("data", jsonArrayResult);
		//时间
		map.put("time", time);
		map.put("chongcheng", chongCheng);
		map.put("chongci", chongCi);
		map.put("minzaihe", minZaiHe);
		map.put("maxzaihe", maxZaiHe);

		return map;
	}
	
	/**
	 * 获得产液量
	 * @param wellNum
	 * @return
	 */
	@RequestMapping("/getDayProduct.html")
	@ResponseBody
	public Map<String, Object> getDayProduct(@RequestParam(value="wellNum", required=true)String wellNum,
			@RequestParam(value="starttime", required=true)String startTime,
			@RequestParam(value="endtime", required=false)String endTime) {
		log.debug(wellNum + startTime + endTime);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result",Math.random() * 100);
		
		return map;
	}
}
