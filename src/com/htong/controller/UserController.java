package com.htong.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.htong.domain.User;
import com.htong.service.UserService;

@Controller
public class UserController {
	private static final Logger log = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping("/login.html")
	public ModelAndView login(User user) {
		boolean success = userService.isRightUserByName(user);
		System.out.println(user.getUsername());
		if (success) {
			Map<String, Object> map = new HashMap<String, Object>();
			ModelAndView mav = new ModelAndView();
			mav.setViewName("data");

			map.put("success", true);
			map.put("username", user.getUsername());

			JSONObject jsonObject = JSONObject.fromObject(map);
			String jsonString = jsonObject.toString();

			mav.addObject("data", jsonString);

			return mav;
		} else {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("data");
			mav.addObject("data", "{\"success\":false}");

			return mav;
		}

	}

}
