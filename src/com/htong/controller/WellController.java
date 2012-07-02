package com.htong.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.testng.log4testng.Logger;

import com.htong.service.WellService;

@Controller
public class WellController {
	private static final Logger log = Logger.getLogger(WellController.class);
	@Autowired
	private WellService wellService;
	
	@RequestMapping("/getWellTreeInfo.html")
	@ResponseBody
	public Map<String, Object> getWellTreeInfo() {
		log.debug("getWellTreeInfo");
		Map<String, Object> map = wellService.getWellTreeInfo();
		
		return map;
	}

}
