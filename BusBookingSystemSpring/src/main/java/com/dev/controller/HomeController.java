package com.dev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
//	@GetMapping("/")
//	public String userHomePage (){
//		return "AdminHomePage";
//	}
//
//	@GetMapping("/usersearchHome")
//	public String userHomePage (){
//		return "displayUser";
//	}
	
	@GetMapping("/userHome")
	public String adminHomePage (){
		return "AdminHomePage";
	}
	
//	@GetMapping("/updatebusHome")
//	public String updateBusPage (){
//		return "UpdateBus";
//	}
	


}
