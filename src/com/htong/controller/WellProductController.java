package com.htong.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htong.service.WellDataService;
import com.htong.service.WellService;

@Controller
public class WellProductController {
	@Autowired
	private WellDataService wellDataService;
	@Autowired
	private WellService wellService;

	private static final Logger log = Logger
			.getLogger(WellProductController.class);
	
	/**
	 * 获得产液量
	 * 
	 * @param wellNum
	 * @return
	 */
	@RequestMapping("/getProduct.html")
	@ResponseBody
	public Map<String, Object> getDayProduct(
			@RequestParam(value = "wellNum", required = true) String wellNum,
			@RequestParam(value = "starttime", required = true) String startTime,
			@RequestParam(value = "endtime", required = true) String endTime) {
		log.debug(wellNum + startTime + endTime);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = null;
		Date endDate = null;
		
		try {
			startDate = sdf.parse(startTime + " 00:00:00");
			endDate = sdf.parse(endTime + " 23:59:59");
		} catch (ParseException e) {
			log.error("日期转换错误！");
			e.printStackTrace();
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		
		String product = "";
		if(startTime.equals(endTime)) {//一天日产量查询
			product = wellDataService.getDayProduct(wellNum, startCalendar);
		} else {//自定义时间段
			product = wellDataService.getCustomDayProduct(wellNum, startCalendar, endCalendar);
		}
		log.debug("产液量：" + product);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", product);
		return map;
	}

}
