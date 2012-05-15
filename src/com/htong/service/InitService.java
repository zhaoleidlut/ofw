package com.htong.service;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.data.document.mongodb.MongoTemplate;

import com.htong.domain.Person;

public class InitService {
	 
	 @Resource(name="mongoTemplate")
	 private MongoTemplate mongoTemplate;

	 private void init() {
	  // Populate our MongoDB database
	  
	  // Drop existing collection
	  mongoTemplate.dropCollection("employees");
	  
	  // Create new object
	  Person p = new Person ();
	  p.setPid(UUID.randomUUID().toString());
	  p.setFirstName("John");
	  p.setLastName("Smith");
	  p.setMoney(1000.0);
	  
	  // Insert to db
	     mongoTemplate.insert("employees", p);
	     // Create new object
	  p = new Person ();
	  p.setPid(UUID.randomUUID().toString());
	  p.setFirstName("Jane");
	  p.setLastName("Adams");
	  p.setMoney(2000.0);
	  
	  // Insert to db
	     mongoTemplate.insert("employees", p);
	        
	     // Create new object
	  p = new Person ();
	  p.setPid(UUID.randomUUID().toString());
	  p.setFirstName("Jeff");
	  p.setLastName("Mayer");
	  p.setMoney(3000.0);
	  
	  // Insert to db
	     mongoTemplate.insert("employees", p);
	 }
}
