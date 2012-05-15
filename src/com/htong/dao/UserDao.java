package com.htong.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.stereotype.Repository;

import com.htong.domain.User;
import com.htong.util.mongo.CollectionConstants;

@Repository
public class UserDao{
	
	private static final Logger log = Logger.getLogger(UserDao.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;

	public User getUserByName(String username) {
		Query query = new Query(Criteria.where("username").is(username));
		User user = mongoTemplate.findOne(CollectionConstants.USER, query, User.class);
		return user;
	}

	public User getUserByOid(Integer oid) {
		Query query = new Query(Criteria.where("oid").is(oid));
		User user = mongoTemplate.findOne(CollectionConstants.USER, query, User.class);
		return user;
	}

}
