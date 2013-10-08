package com.htong.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.htong.domain.WellData;
import com.htong.domain.WellModel;
import com.htong.model.MonthProductModel;
import com.htong.service.WellDataService;
import com.htong.service.WellProductService;
import com.htong.service.WellService;

@Controller
public class WellProductController {
	@Autowired
	private WellDataService wellDataService;
	@Autowired
	private WellService wellService;
	@Autowired
	private WellProductService wellPruductService;

	private static final Logger log = Logger
			.getLogger(WellProductController.class);
	
	/**
	 * 通过每小时的功图来计算产液量
	 * @param wellNum
	 * @param date
	 * @param time
	 * @return
	 */
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
		
		if(wellData.getZaihe()[50]<0.5 && wellData.getZaihe()[51]<0.5 && wellData.getZaihe()[52]<0.5) {
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
		
		map.put("llcyl", cyl * 24 + cyl * 24*addChongCheng/yxcc);
		map.put("cyl", newChanyeliang);
		
		return map;
	}
	
	/**
	 * 获得日产液量
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
		log.debug(wellNum +" "+ startTime +" "+ endTime);
		
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
		
		WellModel well = wellService.getWellByNum(wellNum);
		
		String product = "0";
		if(startTime.equals(endTime)) {//一天日产量查询
			if(well.getChouyoujiXinghao()!=null && well.getChouyoujiXinghao().equals("GGHX")) {
				product = wellPruductService.getDayProductGGHX(wellNum, startCalendar);
			} else {
				try {
					product = wellPruductService.getDayProduct(wellNum, startCalendar);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} else {//自定义时间段
			product = wellPruductService.getCustomDayProduct(wellNum, startCalendar, endCalendar);
		}
		log.debug("产液量：" + product);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", product);
		return map;
	}
	
	/**
	 * 通过井号获得一个月的产液量
	 * @param wellNum
	 * @param year
	 * @param month
	 * @return
	 */
	@RequestMapping("/getMonthProductByWellNum.html")
	@ResponseBody
	public Map<String, Object> getMonthProductByWellNum(
			@RequestParam(value = "wellNum", required = true) String wellNum, 
			@RequestParam(value = "year", required = true) String year,
			@RequestParam(value = "month", required = true) String month) {
		
		if("".equals(wellNum)|| "".equals(month) || "".equals(year)) {
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTime = null;
		try {
			dateTime = sdf.parse(year+"-" +month+ "-"+"01 "+"01:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		
		int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		List<MonthProductModel> monthProList = new ArrayList<MonthProductModel>();
		
		for(int i=1;i<=days;i++) {
			try {
				dateTime = sdf.parse(year+"-" +month+ "-"+(i<10?"0"+String.valueOf(i):String.valueOf(i))+" "+"00:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar c = Calendar.getInstance();
			c.setTime(dateTime);
			
			String product = "0";
			WellModel well = wellService.getWellByNum(wellNum);
			
			if(well.getChouyoujiXinghao()!=null && well.getChouyoujiXinghao().equals("GGHX")) {
				product = wellPruductService.getDayProductGGHX(wellNum, c);
			} else {
				try {
					product = wellPruductService.getDayProduct(wellNum, c);
				} catch (Exception e) {
					product = "0";
					e.printStackTrace();
				}
			}
			
			String m =month+"月" + (i<10?"0"+String.valueOf(i):String.valueOf(i)) + "日";
			MonthProductModel mpm = new MonthProductModel();
			mpm.setMonth(m);
			mpm.setProduct(product);
			
			monthProList.add(mpm);
		}
		map.put("rows", monthProList);
		map.put("total", days);
		
		return map;
	}
	
	/**
	 * 通过井号获得一个月的产液量
	 * @param wellNum
	 * @param year
	 * @param month
	 * @return
	 */
	@RequestMapping("/getMonthProductByWellNumForDGT.html")
	@ResponseBody
	public Map<String, Object> getMonthProductByWellNumForDGT(
			@RequestParam(value = "wellNum", required = true) String wellNum, 
			@RequestParam(value = "year", required = true) String year,
			@RequestParam(value = "month", required = true) String month) {
		
		if("".equals(wellNum)|| "".equals(month) || "".equals(year)) {
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTime = null;
		try {
			dateTime = sdf.parse(year+"-" +month+ "-"+"01 "+"01:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		
		int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		List<MonthProductModel> monthProList = new ArrayList<MonthProductModel>();
		
		for(int i=1;i<=days;i++) {
			try {
				dateTime = sdf.parse(year+"-" +month+ "-"+(i<10?"0"+String.valueOf(i):String.valueOf(i))+" "+"00:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar c = Calendar.getInstance();
			c.setTime(dateTime);
			
			String product = "0";
			WellModel well = wellService.getWellByNum(wellNum);
			
			if(well.getChouyoujiXinghao() != null && well.getChouyoujiXinghao().equals("DGT")) {
				product = wellPruductService.getDGTDayProduct(wellNum, c);
				map.put("isDGT", true);
			} else {
				map.put("isDGT", false);
				return map;
			}
			
//			if(well.getChouyoujiXinghao()!=null && well.getChouyoujiXinghao().equals("GGHX")) {
//				product = wellPruductService.getDayProductGGHX(wellNum, c);
//			} else {
//				product = wellPruductService.getDayProduct(wellNum, c);
//			}
			
			String m =month+"月" + (i<10?"0"+String.valueOf(i):String.valueOf(i)) + "日";
			MonthProductModel mpm = new MonthProductModel();
			mpm.setMonth(m);
			mpm.setProduct(product);
			
			monthProList.add(mpm);
		}
		map.put("rows", monthProList);
		map.put("total", days);
		
		return map;
	}
	
	@RequestMapping("/getP.html")
	@ResponseBody
	public Map<String, Object> getP() throws IOException {
		String month = "11";
		
		Map<String, Object> map = new HashMap<String, Object>();
		 File f = new File("d:\\"+month+"月理论值.txt");
		   if(f.exists()){
		    System.out.print("文件存在");
		   }else{
		    System.out.print("文件不存在");
		    f.createNewFile();//不存在则创建
		   }
		   
		BufferedWriter output = new BufferedWriter(new FileWriter(f));
		
		List<WellModel> welllist = wellService.getAllWells();
		for(WellModel well:welllist) {
			 output.write(well.getNum() + "  " + month + "月理论值\r\n");
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
				try {
					product = wellPruductService.getDayProduct(well.getNum(), startCalendar);
				} catch (Exception e) {
					product="0";
					e.printStackTrace();
				}
				
//				String yxcc = "";
//				try {
//					yxcc = wellPruductService.getYXCC(well.getNum(), startCalendar);
//				} catch (Exception e) {
//					yxcc = "0";
//					e.printStackTrace();
//				}
				
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
			product = wellPruductService.getZHC(wellNum, startCalendar);
		} else {//自定义时间段
			product = wellPruductService.getCustomDayProduct(wellNum, startCalendar, endCalendar);
		}
		log.debug("产液量：" + product);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", product);
		return map;
	}
	
	@RequestMapping("/getYearProductByWellNumForBar.html")
	@ResponseBody
	public Map<String, Object> getYearProductByWellNumForBar(
			@RequestParam(value = "wellNum", required = true) String wellNum, 
			@RequestParam(value = "year", required = true) String year) {
		
		if("".equals(wellNum)|| "".equals(year)) {
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray jsonArray = new JSONArray();
		JSONArray result = new JSONArray();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i = 1;i<13;i++) {
			Date dateTime = null;
			try {
				dateTime = sdf.parse(year+"-" +i+ "-"+"01 "+"01:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateTime);
			
			String product;
			try {
				product = wellPruductService.getDayProduct(wellNum, calendar);
			} catch (Exception e) {
				product = "0";
				e.printStackTrace();
			}
			
			JSONArray ja = new JSONArray();
			ja.add(i);
			ja.add(Float.parseFloat(product)*30);
			jsonArray.add(ja);
		}

		result.add(jsonArray);

		map.put("monthProduct", result);
		return map;
	}
	
	@RequestMapping("/getMonthProductByWellNumForLine.html")
	@ResponseBody
	public Map<String, Object> getMonthProductByWellNumForLine(
			@RequestParam(value = "wellNum", required = true) String wellNum, 
			@RequestParam(value = "year", required = true) String year,
			@RequestParam(value = "month", required = true) String month) {
		
		if("".equals(wellNum)|| "".equals(month) || "".equals(year)) {
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTime = null;
		try {
			dateTime = sdf.parse(year+"-" +month+ "-"+"01 "+"01:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		
		int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		JSONArray jsonArray = new JSONArray();
		JSONArray result = new JSONArray();
		
		for(int i=1;i<=days;i++) {
			try {
				dateTime = sdf.parse(year+"-" +month+ "-"+(i<10?"0"+String.valueOf(i):String.valueOf(i))+" "+"00:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar c = Calendar.getInstance();
			c.setTime(dateTime);
			
			String product = "0";
			WellModel well = wellService.getWellByNum(wellNum);
			
			if(well.getChouyoujiXinghao()!=null && well.getChouyoujiXinghao().equals("GGHX")) {
				product = wellPruductService.getDayProductGGHX(wellNum, c);
			} else {
				try {
					product = wellPruductService.getDayProduct(wellNum, c);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			JSONArray ja = new JSONArray();
			ja.add(i);
			ja.add(Float.parseFloat(product));
			jsonArray.add(ja);
		}
		result.add(jsonArray);

		map.put("monthProduct", result);
		return map;
	}
}
