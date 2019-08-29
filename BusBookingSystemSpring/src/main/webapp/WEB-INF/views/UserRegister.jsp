<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
</head>
<jsp:include page="navBar.jsp"></jsp:include>

<body>
	<div class="col-md-4 offset-4 mt-4 card">
		<div class="card-body">
			<h1 class="h1 text-center">Register</h1>
			<form action="./userRegister" method="post">

				<label for="username">User Id</label>
				<div class="form-group">
					<input type="text" class="form-control" name="userId" required>
				</div>
				<label for="username">Username</label>
				<div class="form-group">
					<input type="text" class="form-control" name="userName" required>
				</div>

				<label for="password">Password</label>
				<div class="form-group">

					<input type="password" class="form-control" name="password" required>

				</div>
				<label for="username">Email</label>
				<div class="form-group">
					<input type="email" class="form-control" name="email" required>
				</div>
				<label for="username">Contact</label>
				<div class="form-group">
					<input type="text" class="form-control" maxlength="10" minlength="10" name="contact" required>
				</div>
				<div class="form-group">
					<button class="btn btn-info float-right">Register</button>
				</div>
				<p>
					Already a member? <a href="./login">Login</a>
				</p>
			</form>
	
	
<h2>${msg}</h2>
</body>
</html>