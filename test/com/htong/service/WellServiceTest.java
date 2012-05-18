package com.htong.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.log4testng.Logger;

import com.htong.dao.WellDataDao;

public class WellServiceTest {
	private static final Logger log = Logger.getLogger(WellServiceTest.class);
	private ApplicationContext appContext;
	
	private WellService wellService;
  @BeforeTest
  public void beforeTest() {
	  appContext = new ClassPathXmlApplicationContext(
				"com/htong/spring/spring.xml");
	  wellService = (WellService) appContext.getBean("wellService");
  }


  @Test
  public void getWellTreeInfo() {
    Map<String , Object> map = wellService.getWellTreeInfo();
    System.out.println(map.toString());
  }
}
