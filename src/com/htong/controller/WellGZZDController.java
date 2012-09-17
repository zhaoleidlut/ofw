package com.htong.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htong.alg.MyLvBo;
import com.htong.domain.WellData;
import com.htong.domain.WellModel;
import com.htong.gzzd.GTDataComputerProcess;
import com.htong.service.WellDataService;
import com.htong.service.WellService;

@Controller
public class WellGZZDController {
	@Autowired
	private WellDataService wellDataService;
	@Autowired
	private WellService wellService;

	private static final Logger log = Logger
			.getLogger(WellGZZDController.class);

	@RequestMapping("/getHistoryGzzdInfo.html")
	@ResponseBody
	public Map<String, Object> getDayProductByHourGT(
			@RequestParam(value = "wellNum", required = true) String wellNum,
			@RequestParam(value = "date", required = true) String date,
			@RequestParam(value = "time", required = true) String time) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTime = null;
		try {
			dateTime = sdf.parse(date + " " + time + ":00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		
		WellData wellData = wellDataService.getHistoryWellData(wellNum,
				calendar);

		if (wellData == null) {
			map.put("hasData", "no");
			return map;
		} else {
			map.put("hasData", "yes");
		}
		
		if(wellData.getZaihe()[0]<0.5 && wellData.getZaihe()[1]<0.5 && wellData.getZaihe()[2]<0.5) {
			map.put("zero", "yes");
			return map;
		} else {
			map.put("zero", "no");
		}
		
		WellModel wellModel = wellService.getWellByNum(wellNum);
		
		//加入滤波算法
		MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getZaihe());
		
		GTDataComputerProcess sp = new GTDataComputerProcess();
		Map<String,Object> calcMap = sp.calcSGTData(wellData.getWeiyi(), wellData.getZaihe(), 0,
				wellData.getChong_cheng_time(),
				Float.valueOf(wellModel.getBengjing()),
				Float.valueOf(wellModel.getOilDensity()),
				Float.valueOf(wellModel.getHanshui()),15);
		
		String timeStr = sdf.format(wellData.getDevice_time()); // 数据时间

		BigDecimal bd = new BigDecimal(60 / wellData.getChong_cheng_time());
		float newChongCi = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		String chongCi = String.valueOf(newChongCi); // 冲次

		float shijiChongCheng = (Float) calcMap.get("chongcheng");
		String chongCheng = String.valueOf(shijiChongCheng); // 实际冲程

		float minZaihe = (Float) calcMap.get("minZaihe");
		String minZaiHe = String.valueOf(minZaihe); // 最小载荷

		float maxZaihe = (Float) calcMap.get("maxZaihe");
		String maxZaiHe = String.valueOf(maxZaihe); // 最大载荷
		
		Float liquidProduct = (Float) calcMap.get("liquidProduct");
		
		
		Float yxcc = (Float)calcMap.get("youxiaochongcheng");
		
		JSONArray jsonArrayResult = new JSONArray(); // 最终的数组
		JSONArray jsonArray = new JSONArray();
		
		JSONArray weiyi = JSONArray.fromObject(wellData.getWeiyi());
		JSONArray zaihe = JSONArray.fromObject(wellData.getZaihe());

		for (int i = 0; i < weiyi.size(); i++) {
			JSONArray json = new JSONArray();
			json.add(weiyi.get(i));
			json.add(zaihe.get(i));

			jsonArray.add(json);
		}
		jsonArrayResult.add(jsonArray);
		
		// 位移 载荷
		map.put("data", jsonArrayResult);
		// 时间
		map.put("time", timeStr);
		map.put("chongcheng", chongCheng);
		map.put("chongci", chongCi);
		map.put("minzaihe", minZaiHe);
		map.put("maxzaihe", maxZaiHe);
		
		
		Float f = (Float) calcMap.get("liquidProduct");
		
		
		Float zaiheCha = (Float) calcMap.get("zaiheCha");
		
		Float ax = (Float) calcMap.get("AX");
		
		
		float addChongCheng = 0;
		if(yxcc < shijiChongCheng*3/4) {
			addChongCheng = ax * (shijiChongCheng-ax-yxcc)/(shijiChongCheng-ax);
			System.out.println("附加有效冲程：" + addChongCheng);
		}
		
		map.put("yxcc", yxcc+addChongCheng);	//有效冲程
		map.put("zhc", zaiheCha);
		
		float loss = 0;
		if (wellModel.getLoss() != null) {
			loss = Float.valueOf(wellModel.getLoss());
		}
		float bz_zhc = 0;	//标准载荷差
//		if(yxcc<=0.1) {
//			yxcc = 1;
//		}
		float chanyeliang = 0;
		
		if(wellModel.getZhc() != null && !wellModel.getZhc().equals("0")) {
			bz_zhc = Float.valueOf(wellModel.getZhc());
			chanyeliang = liquidProduct * 24 + liquidProduct * 24*addChongCheng/yxcc - (loss * 0.5f + loss*0.5f*zaiheCha/bz_zhc); // 平均值+补偿值
		} else {
			chanyeliang = liquidProduct * 24 + liquidProduct * 24*addChongCheng/yxcc - loss; // 平均值+补偿值
		}
		
		
		BigDecimal yeBd = new BigDecimal(chanyeliang);
		float newChanyeliang = yeBd.setScale(3, BigDecimal.ROUND_HALF_UP)
				.floatValue();

		if (newChanyeliang < 0) {
			newChanyeliang = 0;
		}
//		if(loss<0) {
//			if(cyl*24<(0-loss)) {
//				newChanyeliang = 0;
//			}
//		}
		
		map.put("llcyl", liquidProduct * 24 + liquidProduct * 24*addChongCheng/yxcc);	//理论产液量
		map.put("cyl", newChanyeliang);	//实际产液量
		
		map.put("gzzd", calcMap.get("gzzd"));
		map.put("fault_level", calcMap.get("fault_level"));
		
		map.put("AX", calcMap.get("AX"));
		map.put("AY", calcMap.get("AY"));
		map.put("BX", calcMap.get("BX"));
		map.put("BY", calcMap.get("BY"));
		map.put("CX", calcMap.get("CX"));
		map.put("CY", calcMap.get("CY"));
		map.put("DX", calcMap.get("DX"));
		map.put("DY", calcMap.get("DY"));
		
		map.put("EX", calcMap.get("EX"));
		map.put("EY", calcMap.get("EY"));
		map.put("FX", calcMap.get("FX"));
		map.put("FY", calcMap.get("FY"));
		
		map.put("PJSZ", calcMap.get("pjsz"));
		map.put("PJXZ", calcMap.get("pjxz"));
		
		return map;
		
	}
	
	
}
