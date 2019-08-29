package com.dev.dao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.dev.beans.Admin;
import com.dev.beans.Available;
import com.dev.beans.Bus;
import com.dev.beans.Suggestion;
import com.dev.beans.Ticket;
import com.dev.beans.User;
import com.dev.exception.RegisterException;

@Repository
public class BusBookingJPAImpl implements BusBookingDAO{

	private final static EntityManagerFactory emf = 
			Persistence.createEntityManagerFactory("myPersistanceUnit");


	public Boolean createUser(User user)throws RegisterException {
		Boolean state = false;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			//create user
			em.persist(user);
			em.getTransaction().commit();
			em.close();
			state = true;
		} catch(Exception e) {
			throw new RegisterException("Registration Exception Occured");
		}
		return state;
	}

	public Boolean updateUser(User user) {
		Boolean state = false;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			
			//Getting user's details
			User dbuser = em.find(User.class, user.getUserId());
			
			//updating details
			dbuser.setUserName(user.getUserName());
			dbuser.setEmail(user.getEmail());
			dbuser.setContact(user.getContact());
			dbuser.setUserPassword(user.getUserPassword());
			em.getTransaction().commit();
			em.close();
			state = true;
		} catch (Exception e) {
			//Custom Exception
			e.printStackTrace();
		}
		return state;
	}


	public Boolean deleteUser(int user_id, String password) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		User user = em.find(User.class, user_id);	
		if(user.getUserPassword().equals(password)) {
			
		//Deleting user
			em.remove(user);
			em.getTransaction().commit();
			em.close();
			return true;
		}
		return false;	
	}

	public User loginUser(int user_id, String password) {
		User user = null;
		try{
			EntityManager em = emf.createEntityManager();
			TypedQuery<User> query = em.createQuery("from User u where userId= :id and userPassword= :passwd", User.class);
			
			//setting paramters
			query.setParameter("id", user_id);
			query.setParameter("passwd", password);
			List<User> users = query.getResultList();
			if(users.size()>0) {
				user = users.get(0);
			}
			em.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	public User searchUser(int user_id) {
		EntityManager em = emf.createEntityManager();
		//searching user
		User user = em.find(User.class, user_id);
		em.close();
		return user;
	}

	public Boolean createBus(Bus bus) {
		Boolean state = false;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			
			//create bus
			em.persist(bus);
			em.getTransaction().commit();
			em.close();
			state = true;
		} catch(Exception e) {
			//throw custom exception
			System.out.println("Error occured");
		}
		return state;
	}


	public Boolean updateBus(Bus bus) {
		Boolean state = false;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			
			//search bus
			Bus buss = em.find(Bus.class,bus.getBusId());
			
			//update bus
			buss.setBusName(bus.getBusName());
			buss.setBusType(bus.getBusType());
			buss.setSource(bus.getSource());
			buss.setDestination(bus.getDestination());
			buss.setTotalSeats(bus.getTotalSeats());
			buss.setPrice(bus.getPrice());
			em.getTransaction().commit();
			em.close();
			state = true;
		} catch (Exception e) {
			//Custom Exception
			e.printStackTrace();
		}
		return state;
	}

	public Bus searchBus(int bus_id) {
		EntityManager em = emf.createEntityManager();
		//search  bus
		Bus bus = em.find(Bus.class, bus_id);
		em.close();
		return bus;

	}

	public Boolean deletebus(int bus_id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Bus bus = em.find(Bus.class, bus_id);	
		//delete bus
		em.remove(bus);
		em.getTransaction().commit();
		em.close();
		return true;
	}

	public Admin adminLogin(int admin_id, String password) {
		Admin admin= null;
		try {
			EntityManager em = emf.createEntityManager();
			TypedQuery<Admin> query =
				em.createQuery("from Admin a where adminId= :id and adminPassword= :passwd",Admin.class);
			
			//setting paramters in query
			query.setParameter("id", admin_id);
			query.setParameter("passwd", password);
			List<Admin> admins = query.getResultList();
			if(admins.size()>0) {
				admin = admins.get(0);
			}
			em.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return admin;
	}

	public Ticket bookTicket(Ticket ticket) {
		Available available = null;
		
		//gives cuurent date time
		ticket.setDateTime(new java.util.Date());
		Ticket bookedTicket=null;
		try{
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();

			TypedQuery<Available> query = em.createQuery("from Available a where availableDate= :adate and busid= :id", Available.class);


			//booking ticket
			em.persist(ticket);

			//update the available seats after booking
			query.setParameter("adate", ticket.getJourneyDate());
			query.setParameter("id", ticket.getBusid());

			List<Available> avail=query.getResultList();
			available=avail.get(0);
			available.setAvailableSeats(available.getAvailableSeats()-ticket.getNoofSeats());
			em.getTransaction().commit();
			em.close();
			return ticket;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return bookedTicket;
	}

	public Boolean cancelTicket(int booking_id) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<Available> query = em.createQuery("from Available a where busid=:bid", Available.class);

		em.getTransaction().begin();

		Ticket ticket = em.find(Ticket.class, booking_id);
		query.setParameter("bid", ticket.getBusid());
		
		//delete ticket
		em.remove(ticket);
		Available available=query.getSingleResult();
		if(available != null) {
			available.setAvailableSeats(available.getAvailableSeats() + ticket.getNoofSeats());
			em.getTransaction().commit();
			em.close();
			return true;
		}else {
			return false;
		}


	}

	public Ticket getTicket(int booking_id,int userid) {
		Ticket ticket = null;

		EntityManager em = emf.createEntityManager();
		TypedQuery<Ticket> query = em.createQuery("from Ticket t where bookingId= :bid and userid= :uid",Ticket.class);

		query.setParameter("bid", booking_id);
		query.setParameter("uid", userid);
		
		//lisrt to give set of results
		List<Ticket> tickets = query.getResultList();
		if(tickets.size() > 0) {
			ticket = tickets.get(0);
		}
		em.close();
		return ticket;
	}

	public List<Available> checkAvailability(String source, String destination, Date date) {
		List<Available> availList=new ArrayList<Available>();
		List<Available> resulList=null;
		List<Bus> busList=null;
		try{
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			TypedQuery<Bus> query = em.createQuery("from Bus b where source= :busSource and destination= :busDestination", Bus.class);
			
			//setting paramaters in bus
			query.setParameter("busSource", source);
			query.setParameter("busDestination", destination);
			busList = query.getResultList();
			TypedQuery<Available> availQuery=em.createQuery("from Available a where busid= :bid and availableDate= :aDate",Available.class);
			if(busList.size()>0) {
				for(Bus bus : busList) {

					//setting parameters in availQuery
					availQuery.setParameter("bid", bus.getBusId());
					availQuery.setParameter("aDate", date);

					//get the result from table 
					resulList=availQuery.getResultList();

					//store the Availability in availList
					availList.addAll(resulList);



				}
			}
			em.getTransaction().commit();
			em.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return availList;

	}

	public Integer checkAvailability(int bus_id, Date date) {

		EntityManager em = emf.createEntityManager();
		TypedQuery<Integer> query = em.createQuery("select a.availableSeats from Available a where busid= :id and availableDate= :date",Integer.class);
		query.setParameter("id", bus_id);
		query.setParameter("date", date);
		Integer availableSeats = query.getSingleResult();
		em.close();
		return availableSeats;

	}

	public Boolean giveFeedback(Suggestion sugg) {
		Boolean state = false;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			em.persist(sugg);
			em.getTransaction().commit();
			em.close();
			state = true;
		} catch(Exception e) {
			//throw custom exception
		}
		return state;
	}


	public List<Suggestion> getAllSuggestions(Suggestion sugg) {
		List<Suggestion> feed=null;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			TypedQuery<Suggestion> query = em.createQuery("from Suggestion s",Suggestion.class);
			feed = query.getResultList();
			em.getTransaction().commit();
			em.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return feed;
	}

	@Override
	public Boolean setAvailability(Available available) {
		Boolean state = false;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			em.persist(available);
			em.getTransaction().commit();
			em.close();
			state = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return state;
	}

	@Override
	public List<Ticket> getAllTicket(int userId) {
		List<Ticket> ticketLi=null;
		try{
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			TypedQuery<Ticket> query = em.createQuery("from Ticket t where userid= :uid", Ticket.class);
			query.setParameter("uid", userId);
			ticketLi = query.getResultList();
			em.getTransaction().commit();
			em.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ticketLi;	
	}

}
