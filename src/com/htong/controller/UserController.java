package com.htong.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htong.domain.User;
import com.htong.service.UserService;
import com.opensymphony.xwork2.ActionContext;

@Controller
public class UserController {
	private static final Logger log = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping("/login.html")
	@ResponseBody
	public Map<String, Object> login(User user, HttpServletRequest request) {
		boolean success = userService.isRightUserByName(user);
		log.debug(user.getUsername());
		Map<String, Object> map = new HashMap<String, Object>();
		if (success) {
			map.put("success", true);
			map.put("username", user.getUsername());

			request.getSession().setAttribute("user", user.getUsername());
			
			log.debug(user.getUsername());
			
			return map;
		} else {
			map.put("success", false);
			return map;
		}

	}

	@RequestMapping("/logout.html")
	@ResponseBody
	public Map<String, Object> logout(HttpServletRequest request) {
		request.getSession().removeAttribute("user");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		log.debug("注销成功！");

		return map;
	}

}
