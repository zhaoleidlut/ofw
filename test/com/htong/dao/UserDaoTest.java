package com.htong.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class UserDaoTest {
	private ApplicationContext appContext;
	
	private UserDao userDao;
  @BeforeTest
  public void beforeTest() {
	  appContext  = new ClassPathXmlApplicationContext("com/htong/spring/spring.xml");  
	  userDao = (UserDao) appContext.getBean("userDao");
  }


  @Test
  public void getUserById() {
   
  }

  @Test
  public void getUserByName() {
	  System.out.println(userDao.getUserByName("admin").getUsername());
  }
}
