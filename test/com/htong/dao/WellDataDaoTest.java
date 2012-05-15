package com.htong.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

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
		System.out.println(wellDataDao.getLatedWellDataByWellNum("草13-115").getChong_cheng_time());
	}
}
