package com.htong.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.htong.domain.User;
import com.htong.service.UserService;

@Controller
public class UserController {
	private static final Logger log = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping("/login.html")
	@ResponseBody
	public Map<String, Object> login(User user) {
		boolean success = userService.isRightUserByName(user);
		log.debug(user.getUsername());
		Map<String, Object> map = new HashMap<String, Object>();
		if (success) {
			map.put("success", true);
			map.put("username", user.getUsername());
			return map;
		} else {
			map.put("success", false);
			return map;
		}

	}

}
