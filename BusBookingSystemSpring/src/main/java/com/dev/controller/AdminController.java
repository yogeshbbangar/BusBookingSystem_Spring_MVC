package com.dev.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dev.beans.Admin;
import com.dev.beans.Available;
import com.dev.beans.Bus;
import com.dev.beans.Suggestion;
import com.dev.beans.Ticket;
import com.dev.beans.User;
import com.dev.services.Services;

@Controller
public class AdminController {
	
	@Autowired
	private Services service;
	
	AnnotationConfigApplicationContext userctx= 
			new AnnotationConfigApplicationContext(User.class);

	AnnotationConfigApplicationContext busctx= 
			new AnnotationConfigApplicationContext(Bus.class);

	AnnotationConfigApplicationContext ticketctx= 
			new AnnotationConfigApplicationContext(Ticket.class);

	AnnotationConfigApplicationContext availctx= 
			new AnnotationConfigApplicationContext(Available.class);

	AnnotationConfigApplicationContext feedctx= 
			new AnnotationConfigApplicationContext(Suggestion.class);



	
	//admin login
	

	@GetMapping("/adminlogin")
	public String loginPage () {
		return "adminLogin";
	}
	
	@PostMapping("/adminlogin")

	public ModelAndView loginUser(HttpServletRequest req, 
			ModelAndView mv ){
		int admin_id = Integer.parseInt(req.getParameter("adminid"));
		String password = req.getParameter("passwd");

		Admin admin = service.adminLogin(admin_id, password);
		if(admin !=null) {	
			HttpSession session = req.getSession();
			session.setAttribute("admin", admin);
			mv.setViewName("redirect:./userHome");
		}else{
			mv.setViewName("redirect:./adminlogin");
		}

		return mv;
	}
	
//admin logout
	
	
	@GetMapping("/adminlogout")
	public void adminlogout(HttpServletRequest req, HttpServletResponse resp){
		HttpSession session=req.getSession();
		if(session!=null){
			session.invalidate();
			Cookie cookie [] = req.getCookies();
			if(cookie!=null){
				for(Cookie c: cookie){
					if(c.getName().equals("JSESSIONID")){
						c.setMaxAge(0);
						resp.addCookie(c);
					}
				}
			}
			try {
				resp.sendRedirect("./adminlogin");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				resp.sendRedirect("./adminlogin");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
//search user
	
	@GetMapping("/searchUser")
	public String searchPage () {
		return "searchUser";
	}
	@PostMapping("/searchUser")
	
	public ModelAndView searchUser(HttpServletRequest req, 
			ModelAndView mv ){
		int userId=0;
		String UserId = req.getParameter("user_id");
		
		userId=Integer.parseInt(UserId);
		User user = userctx.getBean(User.class,"user");

		 user = service.searchUser(userId);
		if(user !=null) {	
			mv.addObject("user", user);
			mv.setViewName("searchUser");
		}

		return mv;
	}
	
	
	//update bus
	
	
	@GetMapping("/busUpdate")
	public String updateBusPage () {
		return "BusUpdate";
	}
	
	
	@PostMapping("/searchupBus")
	public ModelAndView searchupBus(HttpServletRequest req, 
			ModelAndView mv ){

		int busId=0;
		String sBusId = req.getParameter("busid");
		busId=Integer.parseInt(sBusId);

		Bus bus = busctx.getBean(Bus.class,"bus");

		bus=service.searchBus(busId);
		
		mv.addObject("bus", bus); 
		mv.setViewName("BusUpdate");
		req.setAttribute("bus", bus);
		return mv;
	}
	
	@PostMapping("/busUpdate")
	public ModelAndView updateBus(HttpServletRequest req, 
			ModelAndView mv){
		
		Bus bus = busctx.getBean(Bus.class);
		bus.setBusId(Integer.parseInt(req.getParameter("busid")));
		bus.setBusName(req.getParameter("busname"));
		bus.setBusType(req.getParameter("bustype"));
		bus.setSource(req.getParameter("source"));
		bus.setDestination(req.getParameter("destination"));
		bus.setTotalSeats(Integer.parseInt(req.getParameter("totalseats")));
		bus.setPrice(Double.parseDouble(req.getParameter("price")));

		
		
		
		Boolean b = service.updateBus(bus);
		String msg ="Fail to Update Bus";
		if(b) {	
			msg = "Bus Successfully Updated ";			
		}
		mv.addObject("msg",msg);
		mv.setViewName("BusUpdate");
		
		return mv;
	}
	
	//add bus
	
	@GetMapping("/addBus")
	public String addBusPage () {
		return "AddBus";
	}
	
	@PostMapping("/addBus")
	public ModelAndView addBus(HttpServletRequest req, 
			ModelAndView mv,Bus bus ){
	
		Boolean b = service.createBus(bus);
		String msg ="Fail to Add Bus";
		if(b) {	
			msg = "Bus Successfully Added ";
			
		}
		mv.addObject("msg", msg);
		mv.setViewName("AddBus");

		return mv;
	}
	
	
	
	//delete bus
	
	@GetMapping("/deleteBus")
	public String deleteBusPage () {
		return "DeleteBus";
	}
	
	@PostMapping("/deleteBus")
	public ModelAndView deleteBus(HttpServletRequest req, 
			ModelAndView mv ){
		
		int bus_id = Integer.parseInt(req.getParameter("busid"));
		
		Boolean b = service.deletebus(bus_id);
		String msg ="Fail to Delete Bus";
		if(b) {	
			msg = "Bus Successfully Deleted ";
			
		}
		mv.addObject("msg", msg);
		mv.setViewName("DeleteBus");

		return mv;
	}

	//search bus
	
	
	@GetMapping("/searchBus")
	public String searchBusPage () {
		return "AdminSearchBus";
	}
	
	@PostMapping("/searchBus")
	public ModelAndView searchBus(HttpServletRequest req, 
			ModelAndView mv ){
		int busId=0;
		String BusId = req.getParameter("busid");
		busId=Integer.parseInt(BusId);

		Bus bus = busctx.getBean(Bus.class,"bus");
		
			bus = service.searchBus(busId);
		String msg="No bus found";
		if(bus!=null) {

			msg=bus.toString();

		}
		mv.addObject("msg", msg); 
		mv.setViewName("AdminSearchBus");
		return mv;
	}
	
	//set bus availability
	
		@GetMapping("/setAvailibilityBus")
	public String setAvailableBusPage () {
		return "SetAvailablity";
	}
	
	@PostMapping("/setAvailibilityBus")
	public ModelAndView setAvailableBus(HttpServletRequest req, 
			ModelAndView mv ){
		Available available = availctx.getBean(Available.class);
		int bus_id = Integer.parseInt(req.getParameter("busid"));
		Bus bus=service.searchBus(bus_id);			

		available.setBusid(bus_id);
		available.setAvailableSeats(Integer.parseInt(req.getParameter("availseats")));
		String tempdate = req.getParameter("journeydate");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=null;
		try {
			date = sdf.parse(tempdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		available.setAvailableDate(date);

		Boolean avail = service.setAvailability(available);
		if(avail) {	

			mv.addObject("bus", bus);

			mv.addObject("available", available);
			mv.setViewName("SetAvailablity");	
		}
		
		return mv;
	}
 	@PostMapping("/searchavBus")
	public ModelAndView searchavBus(HttpServletRequest req, 
			ModelAndView mv ){
		int bus_id = Integer.parseInt(req.getParameter("busid"));
		
		Bus bus = service.searchBus(bus_id);
		String msg="No bus found";
		if(bus!=null) {

			msg=bus.toString();

		}
		mv.addObject("msg", msg); 
		mv.setViewName("SetAvailablity");
		return mv;
	}

	
	
	//view feedback

	
	@GetMapping("/viewFeedback")
	public ModelAndView viewFeedback(HttpServletRequest req, 
			ModelAndView mv ){
		//Suggestion sugg = new Suggestion();
		Suggestion suggestion = feedctx.getBean(Suggestion.class,"suggestion");

		List<Suggestion> feedbackList = service.getAllSuggestions(suggestion);
		
		
			mv.addObject("feedbackList", feedbackList);
			mv.setViewName("ViewFeedback");
		
		return mv;
	}
	
	
	
	
	
	}
	
