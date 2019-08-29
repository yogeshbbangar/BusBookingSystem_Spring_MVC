<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
</head>
<jsp:include page="navBar.jsp"></jsp:include>

<title>Login</title>
</head>
<body>
	<div class="col-md-4 offset-4 mt-4 card">
		<div class="card-body">
			<h1 class="h1 text-center">LOGIN</h1>
			<form action="./userLogin" method="post">

				<label for="username">User Id</label>
				<div class="form-group">
					<input type="text" class="form-control" name="user_id" required>
				</div>

				<label for="password">Password</label>
				<div class="form-group">

					<input type="password" class="form-control" name="passwd" required>

				</div>
				<div class="form-group">
					<button class="btn btn-info float-right">Login</button>
				</div>
				<p>
					Not a member? <a href="./register">Register</a>
				</p>
			</form>
		</div>
	</div>
</body>
</html>