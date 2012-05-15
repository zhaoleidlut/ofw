package com.htong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.htong.domain.Person;
import com.htong.service.PersonService;

//@Controller
//@RequestMapping
public class HelloController{

//	@Autowired 
//	@Qualifier("personService")
	PersonService personService;


//	@RequestMapping("/hello.do")
	public String test(ModelMap model){
		System.out.println(personService == null);
		int i = personService.getAll().size();
		
//		request.setAttribute("hello_1", "你好啊， Spring!");
//		request.setAttribute("hello_2",2);
		model.addAttribute("hello_1", "1");
		model.addAttribute("hello_2", i);
	  
	    return "hello";
	}
//	@RequestMapping("/ll.do")
	public String insertPerson(ModelMap model) {
		Person person = new Person();
		person.setPid("12");
		
		personService.add(person);
		System.out.println(personService.getAll().size());
		
		int i = personService.getAll().size();
		
		model.addAttribute("hello_1", "1");
		model.addAttribute("hello_2", i);
		
		return "hello";
	}
	
}
