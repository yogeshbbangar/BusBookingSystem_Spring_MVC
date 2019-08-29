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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dev.beans.Available;
import com.dev.beans.Bus;
import com.dev.beans.Suggestion;
import com.dev.beans.Ticket;
import com.dev.beans.User;
import com.dev.services.Services;

@Controller
@RequestMapping(value="/user")
public class UserController {
	
	@Autowired
	private Services services;
	

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


	
	
	@RequestMapping(value="/userHome",method=RequestMethod.GET)
	public String HomePage(){
		return "UserHomePage";
	}
	
	
	
	//user login
		@GetMapping("/login")
		public String loginPage(){
			return "userLogin";
		}

		@PostMapping("/userLogin")
		public ModelAndView login(HttpServletRequest req,
				ModelAndView mv){
			int userId = Integer.parseInt(req.getParameter("user_id"));
			String passwd = req.getParameter("passwd");

			User user = services.loginUser(userId, passwd);
			System.out.println(user);
			if(user != null){
				HttpSession session = req.getSession();	
				session.setAttribute("user", user);
				mv.setViewName("redirect:./userHome");
			}else{
				mv.setViewName("redirect:./login");
			}
			return mv;
		}
		

		//logout
		@GetMapping("/logout")
		public void logOut(HttpServletRequest req,HttpServletResponse resp)
		{
			HttpSession session=req.getSession(false);
			if(session!=null){
				session.invalidate();
				Cookie cookie [] = req.getCookies();
				if(cookie!=null){
					for(Cookie c: cookie){
						if(c.getName().equals("JSESSIONID")){
							c.setMaxAge(0);
							resp.addCookie(c);
							break;
						}
					}
				}
				try {
					resp.sendRedirect("./login");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				try {
					resp.sendRedirect("./login");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		//user registration

		@GetMapping("/register")
		public String addUserPage(){
			return "UserRegister";
		}

		@PostMapping("/userRegister")
		public ModelAndView addUser(HttpServletRequest req, 
				ModelAndView mv){
			User user = userctx.getBean(User.class,"user");

			String regUserId=req.getParameter("userId");


			user.setUserId(Integer.parseInt(regUserId));
			user.setUserPassword(req.getParameter("password"));
			user.setUserName(req.getParameter("userName"));

			String regEmail=req.getParameter("email");
			user.setEmail(regEmail);

			String regContact=req.getParameter("contact");


			user.setContact(Long.parseLong(regContact));


			System.out.println(user);

			boolean state=services.createUser(user);
			String msg="Failed to Register";
			if(state) {
				msg="Successful Registered";
			}

			mv.addObject("msg", msg); //req.setAttribute("user",user)
			mv.setViewName("UserRegister");
			return mv;
		}
		
		//user update

		@GetMapping("/update")
		public String updateUserPage(){
			return "UserUpdate";
		}

		@PostMapping("/userUpdate")
		public ModelAndView updateUser(HttpServletRequest req, 
				ModelAndView mv ){
			User user = userctx.getBean(User.class,"user");
			User tempUser=(User) req.getSession().getAttribute("user");

			user.setUserId(tempUser.getUserId());

			user.setUserPassword(req.getParameter("password"));
			user.setUserName(req.getParameter("userName"));

			String updEmail=req.getParameter("email");
			user.setEmail(updEmail);

			String updContact=req.getParameter("contact");


			user.setContact(Long.parseLong(updContact));

 
			boolean state=services.updateUser(user);
			String msg="Failed to Update Profile";
			if(state) {
				msg="Profile Updated";
			}

			mv.addObject("msg", msg); //req.setAttribute("user",user)
			mv.setViewName("UserUpdate");
			return mv;
		}
		
		//user delete
		@GetMapping("/delete")
		public String deletePage(){
			return "UserDelete";
		}

		@PostMapping("/userDelete")
		public ModelAndView deleteUser(HttpServletRequest req,
				ModelAndView mv){

			User tempUser=(User) req.getSession().getAttribute("user");

			int userId = tempUser.getUserId();
			String passwd = req.getParameter("passwd");



			String msg=null;
			
				boolean state=services.deleteUser(userId, passwd);
				if(state) {
					mv.setViewName("redirect:./login");
				}else {
			mv.setViewName("UserDelete");
				}
			return mv;
		}
		
		//search bus
		@GetMapping("/searchBus")
		public String searchBusPage(){
			return "SearchBus";
		}

		@PostMapping("/searchBus")
		public ModelAndView searchBus(HttpServletRequest req,
				ModelAndView mv){

			int busId=0;
			String BusId = req.getParameter("busid");
			busId=Integer.parseInt(BusId);

			Bus bus = busctx.getBean(Bus.class,"bus");

			bus=services.searchBus(busId);
			String msg="No bus found";
			if(bus!=null) {

				msg=bus.toString();

			}
			mv.addObject("msg", msg); 
			mv.setViewName("SearchBus");
			return mv;
		}

		//check Availability and ticket booking

		@GetMapping("/bookTicket")
		public String addBooking(){
			return "BookTicket";
		}

		@PostMapping("/checkAvaialability")
		public ModelAndView checkAvaialability(HttpServletRequest req, 
				ModelAndView mv){

			String source = req.getParameter("source");
			String destination = req.getParameter("destination");

			String tempDate = req.getParameter("journeyDate");
			SimpleDateFormat simpleDate=new SimpleDateFormat("yyyy-MM-dd");
			Date date=null;
			try {
				date = simpleDate.parse(tempDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			List<Available> avaiList=services.checkAvailability(source, destination, date);
			System.out.println(avaiList);

			mv.addObject("avaiList", avaiList); //req.setAttribute("user",user)
			mv.setViewName("BookTicket");
			return mv;
		}

		//bookTicket
		@PostMapping("/bookTicket")
		public ModelAndView bookTicket(HttpServletRequest req, 
				ModelAndView mv){

			Ticket ticket=ticketctx.getBean(Ticket.class);

			User tempUser=(User) req.getSession().getAttribute("user");
			int userId = tempUser.getUserId();
			ticket.setUserid(userId);

			String bookBusId = req.getParameter("busId");
			int busId=Integer.parseInt(bookBusId);
			ticket.setBusid(busId);



			String bookNoOfSeats = req.getParameter("noOfseats");
			int noOfseats=Integer.parseInt(bookNoOfSeats);
			ticket.setNoofSeats(noOfseats);
			

			String tempDate = req.getParameter("journeyDate");
			SimpleDateFormat simpleDate=new SimpleDateFormat("yyyy-MM-dd");
			Date date=null;
			try {
				date = simpleDate.parse(tempDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			ticket.setJourneyDate(date);

			Ticket bookedTicket=services.bookTicket(ticket);
			System.out.println(bookedTicket);
			String state="Ticket booking Failed";
			if(bookedTicket!=null) {
				state=bookedTicket.toString();

			}

			mv.addObject("state", state); //req.setAttribute("user",user)
			mv.setViewName("BookTicket");
			return mv;
		}

		//get All ticket

		@GetMapping("/getTicket")
		public String getTicket(){
			return "GetTicket";
		}

		@PostMapping("/getAllTicket")
		public ModelAndView getTicket(HttpServletRequest req, 
				ModelAndView mv){

			User tempUser=(User) req.getSession().getAttribute("user");
			int userId = tempUser.getUserId();

			
			List<Ticket> ticketList=services.getAllTicket(userId);
			String msg ="";
			if(ticketList != null) {
			mv.addObject("ticketList", ticketList); //req.setAttribute("user",user)
			mv.setViewName("GetTicket");
			}else {
				 msg = " Tickets Not Found";
			}
			mv.addObject("msg", msg); 
			mv.setViewName("GetTicket");
			return mv;
		}
		
		//cancel Ticket
		@PostMapping("/cancelTicket")
		public ModelAndView cancelTicket(HttpServletRequest req, 
				ModelAndView mv){
		

			String BookingId = req.getParameter("bookingId");
			int bookingId=Integer.parseInt(BookingId);

			boolean state=services.cancelTicket(bookingId);

			String tickMsg="Ticket Cancellation Failed";
			if(state) {
				tickMsg="Successfully cancelled the ticket";					
			}


			mv.addObject("tickMsg", tickMsg); //req.setAttribute("user",user)
			mv.setViewName("GetTicket");
			return mv;
		}

		

		//write feedback
		@GetMapping("/writeFeedback")
		public String writeFeedPage(){
			return "writeFeedback";
		}

		@PostMapping("/writeFeedback")
		public ModelAndView writeFeed(HttpServletRequest req,
				ModelAndView mv){


			User tempUser=(User) req.getSession().getAttribute("user");
			Suggestion feedback=feedctx.getBean(Suggestion.class);

			int userId = tempUser.getUserId();
			feedback.setUserid(userId);

			String message = req.getParameter("feedback");
			feedback.setSuggest(message);




			Boolean state=services.giveFeedback(feedback);
			String msg="Failed to Submit Feedback";
			if(state) {
				msg="Feedback Submmited";
			}

			mv.addObject("msg", msg); //req.setAttribute("user",user)
			mv.setViewName("writeFeedback");
			return mv;
		}






}
