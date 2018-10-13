<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>Welcome to Sample OpenID Connect application</h1>
<h4>Username :
	<input type="text" name="uname" />
	<br>
</h4>
<h4>Password :
	<input type="password" name="pass">
	<br>
	</h4>
	<input type ="submit" value="Login">
	<br>
	<br>
<form action='https://auth.miniorange.com/moas/idp/openidsso' method="get">
	<input type="hidden" name="client_id" value="wh4yd9gwSus_dvs" />
	<input type="hidden" name="state" value="abdcefghijklmnop" />
	<input type="hidden" name="nonce" value="abdcefghijklmnop" />
	<input type="hidden" name="response_type" value="idtoken" />
	<!-- Redirect URI is the URL Handled at the client side -->
	<input type="hidden" name="redirect_uri" value="http://localhost:8080/openid-connect-webapp/OpenIdResponse" />
	<input type="submit" value="Login With miniOrange" />
</form>
</body>
</html>