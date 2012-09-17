package com.htong.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htong.alg.MyLvBo;
import com.htong.alg.SGTDataComputerProcess;
import com.htong.dao.WellModelDao;
import com.htong.domain.WellData;
import com.htong.domain.WellModel;
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
	
	@RequestMapping("/getDayProductByHourGT.html")
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
		
		SGTDataComputerProcess sp = new SGTDataComputerProcess();
		Map<String,Object> calcMap = sp.calcSGTData(wellData.getWeiyi(), wellData.getZaihe(), 0,
				wellData.getChong_cheng_time(),
				Float.valueOf(wellModel.getBengjing()),
				Float.valueOf(wellModel.getOilDensity()),
				Float.valueOf(wellModel.getHanshui()));
		
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
		
		Float cyl = (Float) calcMap.get("liquidProduct");
		
		
		Float yxcc = (Float)calcMap.get("youxiaochongcheng");
		
		
		JSONArray jsonArrayResult = new JSONArray(); // 最终的数组
		JSONArray jsonArray = new JSONArray();
		
		MyLvBo.myLvBo(wellData.getWeiyi(), wellData.getZaihe());

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
		System.out.println(sdf.format(wellData.getDevice_time()));
		System.out.println("理论产液量：" + f);
		
		
		Float zhc = (Float) calcMap.get("zhc");
		System.out.println("载荷差：" + zhc);
		
		Float zsp = (Float) calcMap.get("zs");
		
		
		float addChongCheng = 0;
		System.out.println("原始有效冲程：" + yxcc);
		if(yxcc < shijiChongCheng*3/4) {
			addChongCheng = zsp * (shijiChongCheng-zsp-yxcc)/(shijiChongCheng-zsp);
			System.out.println("附加有效冲程：" + addChongCheng);
		}
		
		map.put("yxcc", yxcc+addChongCheng);	//有效冲程
		map.put("zhc", zhc);
		
		float loss = 0;
		if (wellModel.getLoss() != null) {
			loss = Float.valueOf(wellModel.getLoss());
		}
		float bz_zhc = 0;
//		if(yxcc<=0.1) {
//			yxcc = 1;
//		}
		float chanyeliang = 0;
		
		if(wellModel.getZhc() != null && !wellModel.getZhc().equals("0")) {
			bz_zhc = Float.valueOf(wellModel.getZhc());
			chanyeliang = cyl * 24 + cyl * 24*addChongCheng/yxcc - (loss * 0.5f + loss*0.5f*zhc/bz_zhc); // 平均值+补偿值
		} else {
			chanyeliang = cyl * 24 + cyl * 24*addChongCheng/yxcc - loss; // 平均值+补偿值
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
		
		map.put("llcyl", cyl * 24 + cyl * 24*addChongCheng/yxcc);
		map.put("cyl", newChanyeliang);
		
		return map;
		
	}
	
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
	
	
	
	@RequestMapping("/getZHC.html")
	@ResponseBody
	public Map<String, Object> getZHC(
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
			product = wellDataService.getZHC(wellNum, startCalendar);
		} else {//自定义时间段
			product = wellDataService.getCustomDayProduct(wellNum, startCalendar, endCalendar);
		}
		log.debug("产液量：" + product);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", product);
		return map;
	}
	
	@RequestMapping("/getP.html")
	@ResponseBody
	public Map<String, Object> getP() throws IOException {
		String month = "08";
		
		Map<String, Object> map = new HashMap<String, Object>();
		 File f = new File("d:\\"+month+"月修正刀把理论值.txt");
		   if(f.exists()){
		    System.out.print("文件存在");
		   }else{
		    System.out.print("文件不存在");
		    f.createNewFile();//不存在则创建
		   }
		   
		   BufferedWriter output = new BufferedWriter(new FileWriter(f));

		
		List<WellModel> welllist = wellService.getAllWells();
		for(WellModel well:welllist) {
			 output.write(well.getNum() + "  " + month + "月修正刀把理论值\r\n");
			 for(int i = 1;i<=7;i++) {
				String startTime = "2012-" + month + "-" + i;
				String endTime = "2012-" + month + "-" + i;
				
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
//				
				String product = "";
				product = wellDataService.getDayProduct(well.getNum(), startCalendar);
				
//				String yxcc = "";
//				yxcc = wellDataService.getYXCC(well.getNum(), startCalendar);
				
//				String zhc = "";
//				zhc = wellDataService.getZHC(well.getNum(), startCalendar);
				
				
//				output.write(yxcc +  "\r\n");
				output.write(product+"\r\n");
//				output.write(zhc+"\r\n");
				
//				map.put(month +"月" + String.valueOf(i) + "日产液量", product);
			}
		}
		
		 output.close();
		
		return map;
	}
	
	@RequestMapping("/getMonthProduct.html")
	@ResponseBody
	public Map<String, Object> getMonthProduct(
			@RequestParam(value = "dtu", required = true) String dtu, @RequestParam(value = "month", required = true) String month) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		WellModel well = wellService.getWellByDtuNum(dtu);
		if(well == null) {
			map.put("错误", "不存在"+dtu+"的DTU");
			return map;
		} else {
			map.put(dtu + "所属井", well.getNum());
		}
		
		for(int i = 1;i<=30;i++) {
			String startTime = "2012-" + month + "-" + i;
			String endTime = "2012-" + month + "-" + i;
			
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
			
			product = wellDataService.getMouthProduct(dtu, startCalendar);
			
			log.debug("产液量：" + product);

			
			map.put(month +"月" + String.valueOf(i) + "日产液量", product);
		}
		
		return map;
	}

}
