package com.htong.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class WellDataDaoTest {
	private ApplicationContext appContext;

	private WellDataDao wellDataDao;

	@BeforeTest
	public void beforeTest() {
		appContext = new ClassPathXmlApplicationContext(
				"com/htong/spring/spring.xml");
		wellDataDao = (WellDataDao) appContext.getBean("wellDataDao");
	}

	@Test
	public void getLatedWellDataByWellNum() {
		System.out.println(wellDataDao.getLatedWellDataByWellNum("草13-斜502").getDgt());
	}
	
	@Test
	public void getHistoryWellData() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		
		Calendar s1 = Calendar.getInstance();
		Calendar s2 = Calendar.getInstance();
		try {
			startDate.setTime(sdf.parse("2012-04-01 12:12:12"));
			endDate.setTime(sdf.parse("2012-05-23 12:12:12"));
			s1.setTime(sdf.parse("2012-04-23 12:12:12"));
			s2.setTime(sdf.parse("2012-05-23 12:12:12"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println("所有的：" + wellDataDao.getHistoryWellData("草13-115", startDate, endDate).size());
		System.out.println("前：" +wellDataDao.getHistoryWellData("草13-115", startDate, s1).size());
		System.out.println("后：" +wellDataDao.getHistoryWellData("草13-115", s1, s2).size());
	}
	
	@Test
	public void getHistoryWellData1() {
		
	}
}
