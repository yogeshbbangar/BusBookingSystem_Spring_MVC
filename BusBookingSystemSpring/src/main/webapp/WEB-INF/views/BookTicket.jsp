<%@page import="com.dev.beans.Available"%>
<%@page import="com.dev.beans.User"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Book Ticket</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
</head>
	<jsp:include page="navBar.jsp"></jsp:include>

<body>

	<%
		User user = (User) session.getAttribute("user");
		if (user != null) {
			out.print("<h1>Hello, " + user.getUserName() + "</h1>");
		} else {
			response.sendRedirect("./login");
		}
	%>
	<div class="col-md-4 offset-4 mt-4 card">
		<div class="card-body">
			<h1 class="h1 text-center">Search Bus to Book</h1>
			<form action="./checkAvaialability" method="post">

				<label for="source">Source</label>
				<div class="form-group">
					<input type="text" class="form-control" name="source" required>
				</div>

				<label for="destination">Destination</label>
				<div class="form-group">

					<input type="text" class="form-control" name="destination" required>

				</div>
				<label for="date">Journey Date</label>
				<div class="form-group">

					<input type="text" class="form-control" name="journeyDate" required>

				</div>
				<div class="form-group">
					<button class="btn btn-info float-right">Check
						Availability</button>
				</div>
			</form>
		</div>
	</div>

	<center>
		<h3>List of Availability</h3>
	</center>
	<center>
		<h4>${avaiList}</h4>
	</center>
	<div class="col-md-4 offset-4 mt-4 card">
		<div class="card-body">
			<h1 class="h1 text-center">Book Ticket</h1>
			<form action="./bookTicket" method="post">

				<label for="busid">Bus Id</label>
				<div class="form-group">
					<input type="text" class="form-control" name="busId" required>
				</div>
				<label for="date">Journey Date</label>
				<div class="form-group">

					<input type="text" class="form-control" name="journeyDate" required>

				</div>
				<label for="destination">Number of Seats</label>
				<div class="form-group">

					<input type="text" class="form-control" name="noOfseats" required>

				</div>

				<div class="form-group">
					<button class="btn btn-info float-right">Book</button>
				</div>
			</form>
		</div>
	</div>
	<center>
		<h4>${state}</h4>
	</center>


</body>
</html>