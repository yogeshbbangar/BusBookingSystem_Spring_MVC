<%@page import="com.dev.beans.User"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Feedback</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
</head>
<jsp:include page="navBar.jsp"></jsp:include>
<body>
<% User user = (User)session.getAttribute("user");
	if(user != null){
		out.print("<h1>Hello " + user.getUserName() +"</h1>");
	}else{
		response.sendRedirect("./login");
	}
%>

<div class="col-md-4 offset-4 mt-4 card">
		<div class="card-body">
			<h1 class="h1 text-center">FeedBack</h1>
			<form action="./writeFeedback" method="post">
			<label for="source">Feedback</label>
				<div class="form-group">
					<input type="text" class="form-control" name="feedback" required>
				</div>

				<div class="form-group">
					<button class="btn btn-info float-right">Submit</button>
				</div>
			</form>
		</div>
	</div>
<center><h2>${msg}</h2></center>

	
</body>
</html>