<%@page import="com.capg.bbs.beans.User"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
<jsp:include page="navBar.jsp"></jsp:include>
</head>
<body>

<h1>Welcome to Bus Booking System </h1>

<% User user = (User)session.getAttribute("user");
	if(user != null){
		out.print("<h1>Hello , " + user.getUserName() +"</h1>");
	}else{
		response.sendRedirect("./login");
	}
%>


</body>
</html>