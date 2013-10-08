package com.htong.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.testng.log4testng.Logger;

import com.htong.domain.WellModel;
import com.htong.model.WellCombo;
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
	
	@RequestMapping("/getWellList.html")
	@ResponseBody
	public Map<String, Object> getWellList() {
		List<WellModel> wellList = wellService.getAllWells();
		Map<String, Object> map = new HashMap<String, Object>();
		List<WellCombo> wellcomboList = new ArrayList<WellCombo>();
		for(WellModel well:wellList) {
			WellCombo w = new WellCombo();
			w.setLabel(well.getNum());
			w.setValue(well.getNum());
			wellcomboList.add(w);
		}
		map.put("data", wellcomboList);
		return map;
	}
	
	/**
	 * 获得电功图的井
	 * @return
	 */
	@RequestMapping("/getDGTWellList.html")
	@ResponseBody
	public Map<String, Object> getDGTWellList() {
		List<WellModel> wellList = wellService.getAllWells();
		Map<String, Object> map = new HashMap<String, Object>();
		List<WellCombo> wellcomboList = new ArrayList<WellCombo>();
		for(WellModel well:wellList) {
			if(well.getChouyoujiXinghao() !=null && well.getChouyoujiXinghao().equals("DGT")) {
				WellCombo w = new WellCombo();
				w.setLabel(well.getNum());
				w.setValue(well.getNum());
				wellcomboList.add(w);
			}
			
		}
		map.put("data", wellcomboList);
		return map;
	}

}
