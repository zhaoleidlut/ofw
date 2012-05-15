package com.htong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.htong.dao.UserDao;
import com.htong.domain.User;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	
	public boolean isRightUserByName(User user) {
		User iUser = userDao.getUserByName(user.getUsername());
		if(iUser == null) {
			return false;
		} else {
			if(user.getPassword().equals(iUser.getUsername())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
}
