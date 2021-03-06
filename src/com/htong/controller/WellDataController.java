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

import com.htong.alg.DFT;
import com.htong.alg.LvBo;
import com.htong.alg.MyLvBo;
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

	private static final Logger log = Logger
			.getLogger(WellDataController.class);

	/**
	 * 获得实时示功图曲线数据
	 * 
	 * @param wellNum
	 * @return
	 */
	@RequestMapping("/getSGTPlotData.html")
	@ResponseBody
	public Map<String, Object> getSGTPlotData(
			@RequestParam(value = "wellNum", required = true) String wellNum) {

		WellData wellData = wellDataService.getLatestWellDataByWellNum(wellNum);

		JSONArray jsonArrayResult = new JSONArray(); // 最终的数组
		JSONArray jsonArray = new JSONArray();

		Map<String, Object> map = new HashMap<String, Object>();
		// 无数据
		if (wellData == null) {
			map.put("hasData", "no");
			return map;
		} else {
			map.put("hasData", "yes");
		}
		
		if(wellData.getZaihe()[50]<0.5 && wellData.getZaihe()[51]<0.5 && wellData.getZaihe()[52]<0.5) {
			map.put("zero", "yes");
			return map;
		} else {
			map.put("zero", "no");
		}
		
		
		MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getZaihe());

		

		float chongChengTime = wellData.getChong_cheng_time();

		WellModel wellModel = wellService.getWellByNum(wellNum);

		SGTDataComputerProcess sp = new SGTDataComputerProcess();
		Map<String, Object> result = sp.calcSGTData(wellData.getWeiyi(),
				wellData.getZaihe(), 0, chongChengTime,
				Float.valueOf(wellModel.getBengjing()),
				Float.valueOf(wellModel.getOilDensity()),
				Float.valueOf(wellModel.getHanshui()));
		
		JSONArray weiyi = JSONArray.fromObject(wellData.getWeiyi());
		JSONArray zaihe = JSONArray.fromObject(wellData.getZaihe());

		for (int i = 0; i < weiyi.size(); i++) {
			JSONArray json = new JSONArray();
			json.add(weiyi.get(i));
			json.add(zaihe.get(i));

			jsonArray.add(json);
		}
		jsonArrayResult.add(jsonArray);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(wellData.getDevice_time()); // 数据时间

		BigDecimal bd = new BigDecimal(60 / chongChengTime);
		float newChongCi = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		String chongCi = String.valueOf(newChongCi); // 冲次

		float shijiChongCheng = (Float) result.get("chongcheng");
		String chongCheng = String.valueOf(shijiChongCheng); // 实际冲程

		float minZaihe = (Float) result.get("minZaihe");
		String minZaiHe = String.valueOf(minZaihe); // 最小载荷

		float maxZaihe = (Float) result.get("maxZaihe");
		String maxZaiHe = String.valueOf(maxZaihe); // 最大载荷
		
		Float f = (Float) result.get("liquidProduct");
		map.put("cyl", f);
		
		Float yxcc = (Float)result.get("youxiaochongcheng");
		map.put("yxcc", yxcc);

		// 位移 载荷
		map.put("data", jsonArrayResult);
		// 时间
		map.put("time", time);
		map.put("chongcheng", chongCheng);
		map.put("chongci", chongCi);
		map.put("minzaihe", minZaiHe);
		map.put("maxzaihe", maxZaiHe);

		return map;
	}

	/**
	 * 获得实时电功图曲线数据
	 * 
	 * @param wellNum
	 * @return
	 */
	@RequestMapping("/getDGTPlotData.html")
	@ResponseBody
	public Map<String, Object> getDGTPlotData(
			@RequestParam(value = "wellNum", required = true) String wellNum) {

		WellData wellData = wellDataService.getLatestWellDataByWellNum(wellNum);
		
		//log.debug(wellNum);

		JSONArray jsonArrayResult = new JSONArray(); // 最终的数组
		JSONArray jsonArray = new JSONArray();

		Map<String, Object> map = new HashMap<String, Object>();
		// 无数据
		if (wellData == null) {
			map.put("hasData", "no");
			return map;
		} else {
			map.put("hasData", "yes");
		}
		
		if(wellData.getZaihe()[50]<0.5 && wellData.getZaihe()[51]<0.5 && wellData.getZaihe()[52]<0.5) {
			map.put("zero", "yes");
			return map;
		} else {
			map.put("zero", "no");
		}

		// 无电功图数据
		if (wellData.getDgt() == null) {
			map.put("hasDGTData", "no");
			return map;
		} else {
			map.put("hasDGTData", "yes");
		}

		MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getDgt());

		

		float chongChengTime = wellData.getChong_cheng_time();

		WellModel wellModel = wellService.getWellByNum(wellNum);

		SGTDataComputerProcess sp = new SGTDataComputerProcess();
		Map<String, Object> result = sp.calcSGTData(wellData.getWeiyi(),
				wellData.getDgt(), 0, chongChengTime,
				Float.valueOf(wellModel.getBengjing()),
				Float.valueOf(wellModel.getOilDensity()),
				Float.valueOf(wellModel.getHanshui()));
		
		JSONArray weiyi = JSONArray.fromObject(wellData.getWeiyi());
		JSONArray zaihe = JSONArray.fromObject(wellData.getDgt());

		for (int i = 0; i < weiyi.size(); i++) {
			JSONArray json = new JSONArray();
			json.add(weiyi.get(i));
			json.add(zaihe.get(i));

			jsonArray.add(json);
		}
		jsonArrayResult.add(jsonArray);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(wellData.getDevice_time()); // 数据时间

		BigDecimal bd = new BigDecimal(60 / chongChengTime);
		float newChongCi = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		String chongCi = String.valueOf(newChongCi); // 冲次

		float shijiChongCheng = (Float) result.get("chongcheng");
		String chongCheng = String.valueOf(shijiChongCheng); // 实际冲程

		float minZaihe = (Float) result.get("minZaihe");
		String minZaiHe = String.valueOf(minZaihe); // 最小载荷

		float maxZaihe = (Float) result.get("maxZaihe");
		String maxZaiHe = String.valueOf(maxZaihe); // 最大载荷

		// 位移 载荷
		map.put("data", jsonArrayResult);
		// 时间
		map.put("time", time);
		map.put("chongcheng", chongCheng);
		map.put("chongci", chongCi);
		map.put("minzaihe", minZaiHe);
		map.put("maxzaihe", maxZaiHe);
		
		//log.debug("返回电功图数据");

		return map;
	}

	

	/**
	 * 获得历史示功图曲线数据
	 * 
	 * @param wellNum
	 * @return
	 */
	@RequestMapping("/getHistorySGTPlotData.html")
	@ResponseBody
	public Map<String, Object> getHistorySGTPlotData(
			@RequestParam(value = "wellNum", required = true) String wellNum,
			@RequestParam(value = "date", required = true) String date,
			@RequestParam(value = "time", required = true) String time) {

		Map<String, Object> map = new HashMap<String, Object>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = date + " " + time + ":0:0";

		//log.debug(dateString);

		Date myDate = null;
		try {
			myDate = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		log.debug(sdf.format(myDate));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(myDate);

		WellData wellData = wellDataService.getHistoryWellData(wellNum,
				calendar);

		if (wellData == null) {
			map.put("hasData", "no");
			return map;
		} else {
			map.put("hasData", "yes");
		}
		
		if(wellData.getZaihe()[50]<0.5 && wellData.getZaihe()[51]<0.5 && wellData.getZaihe()[52]<0.5) {
			map.put("zero", "yes");
			return map;
		} else {
			map.put("zero", "no");
		}

		JSONArray jsonArrayResult = new JSONArray(); // 最终的数组
		JSONArray jsonArray = new JSONArray();

		MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getZaihe());

		

		float chongChengTime = wellData.getChong_cheng_time();

		WellModel wellModel = wellService.getWellByNum(wellNum);

		SGTDataComputerProcess sp = new SGTDataComputerProcess();
		Map<String, Object> result = sp.calcSGTData(wellData.getWeiyi(),
				wellData.getZaihe(), 0, chongChengTime,
				Float.valueOf(wellModel.getBengjing()),
				Float.valueOf(wellModel.getOilDensity()),
				Float.valueOf(wellModel.getHanshui()));
		
		JSONArray weiyi = JSONArray.fromObject(wellData.getWeiyi());
		JSONArray zaihe = JSONArray.fromObject(wellData.getZaihe());
//		log.debug(weiyi.size());
		for (int i = 0; i < weiyi.size(); i++) {
			JSONArray json = new JSONArray();
			json.add(weiyi.get(i));
			json.add(zaihe.get(i));

			jsonArray.add(json);
		}
		jsonArrayResult.add(jsonArray);

		String datetime = sdf.format(wellData.getDevice_time()); // 数据时间

		BigDecimal bd = new BigDecimal(60 / chongChengTime);
		float newChongCi = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		String chongCi = String.valueOf(newChongCi); // 冲次

		float shijiChongCheng = (Float) result.get("chongcheng");
		String chongCheng = String.valueOf(shijiChongCheng); // 实际冲程

		float minZaihe = (Float) result.get("minZaihe");
		String minZaiHe = String.valueOf(minZaihe); // 最小载荷

		float maxZaihe = (Float) result.get("maxZaihe");
		String maxZaiHe = String.valueOf(maxZaihe); // 最大载荷
		
		Float f = (Float) result.get("liquidProduct");
		map.put("cyl", f);
		
		
		
		Float yxcc = (Float)result.get("youxiaochongcheng");
		map.put("yxcc", yxcc);

		// 位移 载荷
		map.put("data", jsonArrayResult);
		// 时间
		map.put("time", datetime);
		map.put("chongcheng", chongCheng);
		map.put("chongci", chongCi);
		map.put("minzaihe", minZaiHe);
		map.put("maxzaihe", maxZaiHe);

		return map;
	}

	/**
	 * 获得历史电功图曲线数据
	 * 
	 * @param wellNum
	 * @return
	 */
	@RequestMapping("/getHistoryDGTPlotData.html")
	@ResponseBody
	public Map<String, Object> getHistoryDGTPlotData(
			@RequestParam(value = "wellNum", required = true) String wellNum,
			@RequestParam(value = "date", required = true) String date,
			@RequestParam(value = "time", required = true) String time) {

		Map<String, Object> map = new HashMap<String, Object>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = date + " " + time + ":0:0";

		Date myDate = null;
		try {
			myDate = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(myDate);

		WellData wellData = wellDataService.getHistoryWellData(wellNum,
				calendar);

		if (wellData == null) {
			map.put("hasData", "no");
			return map;
		} else {
			map.put("hasData", "yes");
		}
		
		if(wellData.getZaihe()[50]<0.5 && wellData.getZaihe()[51]<0.5 && wellData.getZaihe()[52]<0.5) {
			map.put("zero", "yes");
			return map;
		} else {
			map.put("zero", "no");
		}

		// 无电功图数据
		if (wellData.getDgt() == null) {
			map.put("hasDGTData", "no");
			return map;
		} else {
			map.put("hasDGTData", "yes");
		}

		JSONArray jsonArrayResult = new JSONArray(); // 最终的数组
		JSONArray jsonArray = new JSONArray();

		MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getDgt());

		float chongChengTime = wellData.getChong_cheng_time();

		WellModel wellModel = wellService.getWellByNum(wellNum);

		SGTDataComputerProcess sp = new SGTDataComputerProcess();
		Map<String, Object> result = sp.calcSGTData(wellData.getWeiyi(),
				wellData.getDgt(), 0, chongChengTime,
				Float.valueOf(wellModel.getBengjing()),
				Float.valueOf(wellModel.getOilDensity()),
				Float.valueOf(wellModel.getHanshui()));
		
		JSONArray weiyi = JSONArray.fromObject(wellData.getWeiyi());
		JSONArray zaihe = JSONArray.fromObject(wellData.getDgt());
		log.debug(weiyi.size());
		for (int i = 0; i < weiyi.size(); i++) {
			JSONArray json = new JSONArray();
			json.add(weiyi.get(i));
			json.add(zaihe.get(i));

			jsonArray.add(json);
		}
		jsonArrayResult.add(jsonArray);

		String datetime = sdf.format(wellData.getDevice_time()); // 数据时间

		BigDecimal bd = new BigDecimal(60 / chongChengTime);
		float newChongCi = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		String chongCi = String.valueOf(newChongCi); // 冲次

		float shijiChongCheng = (Float) result.get("chongcheng");
		String chongCheng = String.valueOf(shijiChongCheng); // 实际冲程

		float minZaihe = (Float) result.get("minZaihe");
		String minZaiHe = String.valueOf(minZaihe); // 最小载荷

		float maxZaihe = (Float) result.get("maxZaihe");
		String maxZaiHe = String.valueOf(maxZaihe); // 最大载荷

		// 位移 载荷
		map.put("data", jsonArrayResult);
		// 时间
		map.put("time", datetime);
		map.put("chongcheng", chongCheng);
		map.put("chongci", chongCi);
		map.put("minzaihe", minZaiHe);
		map.put("maxzaihe", maxZaiHe);

		return map;
	}
	

	/**
	 * 获得功图和电数据
	 */
	@RequestMapping("/getHistorySGTDianData.html")
	@ResponseBody
	public Map<String, Object> getHistorySGTDianData(
			@RequestParam(value = "wellNum", required = true) String wellNum,
			@RequestParam(value = "date", required = true) String date,
			@RequestParam(value = "time", required = true) String time) {

		Map<String, Object> map = new HashMap<String, Object>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = date + " " + time + ":0:0";

		//log.debug(dateString);

		Date myDate = null;
		try {
			myDate = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		log.debug(sdf.format(myDate));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(myDate);

		WellData wellData = wellDataService.getHistoryWellData(wellNum,
				calendar);

		if (wellData == null) {
			map.put("hasData", "no");
			return map;
		} else {
			map.put("hasData", "yes");
		}
		
		if(wellData.getZaihe()[50]<0.5 && wellData.getZaihe()[51]<0.5 && wellData.getZaihe()[52]<0.5) {
			map.put("zero", "yes");
			return map;
		} else {
			map.put("zero", "no");
		}

		JSONArray jsonArrayResult = new JSONArray(); // 示功图
		JSONArray jsonArrayResultGL = new JSONArray(); // 功率图
		JSONArray jsonArrayResultDL = new JSONArray(); // 电流
		JSONArray jsonArrayResultGLYS = new JSONArray(); // 功率因数
		
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayGL = new JSONArray();
		JSONArray jsonArrayDL = new JSONArray();
		JSONArray jsonArrayGLYS = new JSONArray();
		

		MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getZaihe());

		

		float chongChengTime = wellData.getChong_cheng_time();

		WellModel wellModel = wellService.getWellByNum(wellNum);

		SGTDataComputerProcess sp = new SGTDataComputerProcess();
		Map<String, Object> result = sp.calcSGTData(wellData.getWeiyi(),
				wellData.getZaihe(), 0, chongChengTime,
				Float.valueOf(wellModel.getBengjing()),
				Float.valueOf(wellModel.getOilDensity()),
				Float.valueOf(wellModel.getHanshui()));
		
		JSONArray weiyi = JSONArray.fromObject(wellData.getWeiyi());
		JSONArray zaihe = JSONArray.fromObject(wellData.getZaihe());
		JSONArray gonglv = JSONArray.fromObject(wellData.getPower());	//功率
		JSONArray ib = JSONArray.fromObject(wellData.getIb());	//电流
		JSONArray glys = JSONArray.fromObject(wellData.getPower_factor());	//功率因数

		for (int i = 0; i < weiyi.size(); i++) {
			JSONArray json = new JSONArray();
			JSONArray jsonGL = new JSONArray();
			JSONArray jsonDL = new JSONArray();
			JSONArray jsonGLYS = new JSONArray();
			json.add(weiyi.get(i));
			jsonGL.add(weiyi.get(i));
			jsonDL.add(weiyi.get(i));
			jsonGLYS.add(weiyi.get(i));
			
			json.add(zaihe.get(i));
			jsonGL.add(gonglv.get(i));
			jsonDL.add(ib.get(i));
			jsonGLYS.add(glys.get(i));

			jsonArray.add(json);
			jsonArrayGL.add(jsonGL);
			jsonArrayDL.add(jsonDL);
			jsonArrayGLYS.add(jsonGLYS);
			
		}
		jsonArrayResult.add(jsonArray);
		jsonArrayResultGL.add(jsonArrayGL);
		jsonArrayResultDL.add(jsonArrayDL);
		jsonArrayResultGLYS.add(jsonArrayGLYS);
		
		float sgl = 0;	//上冲程功率
		float xgl = 0;	//下冲程功率
		for(int i = 0;i<100;i++) {
			sgl += wellData.getPower()[i];
			xgl += wellData.getPower()[100+i];
		}
		
		if(sgl<0.5) {
			map.put("wellbalance", 0);
		} else {
			map.put("wellbalance", xgl/sgl);
		}

		String datetime = sdf.format(wellData.getDevice_time()); // 数据时间

		BigDecimal bd = new BigDecimal(60 / chongChengTime);
		float newChongCi = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		String chongCi = String.valueOf(newChongCi); // 冲次

		float shijiChongCheng = (Float) result.get("chongcheng");
		String chongCheng = String.valueOf(shijiChongCheng); // 实际冲程

		float minZaihe = (Float) result.get("minZaihe");
		String minZaiHe = String.valueOf(minZaihe); // 最小载荷

		float maxZaihe = (Float) result.get("maxZaihe");
		String maxZaiHe = String.valueOf(maxZaihe); // 最大载荷
		
		Float f = (Float) result.get("liquidProduct");
		map.put("cyl", f);
		
		
		
		Float yxcc = (Float)result.get("youxiaochongcheng");
		map.put("yxcc", yxcc);

		// 位移 载荷
		map.put("sgt", jsonArrayResult);
		map.put("gl", jsonArrayResultGL);
		map.put("dl", jsonArrayResultDL);
		map.put("glys", jsonArrayResultGLYS);
		// 时间
		map.put("time", datetime);
		map.put("chongcheng", chongCheng);
		map.put("chongci", chongCi);
		map.put("minzaihe", minZaiHe);
		map.put("maxzaihe", maxZaiHe);

		return map;
	}

}
