<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%request.setCharacterEncoding("utf-8"); %>
<%response.setContentType("text/html; charset=UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인폼</title>
</head>
<body>
<h1>로그인하기</h1>
<form action="login" method="post">
	<table>
		<tr>
			<th>Email</th>
			<td><input type="email" name="userEmail"/></td>
		</tr>
		<tr>
			<th>password</th>
			<td><input type="password" name="userPassword"/></td>
		</tr>
		<tr>
			<td>
				<input type="submit" value="sing in"/>
			</td>
		</tr>
	</table>
</form>
</body>
</html>