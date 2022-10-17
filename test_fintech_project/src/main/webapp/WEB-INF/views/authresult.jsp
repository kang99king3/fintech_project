<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%request.setCharacterEncoding("utf-8"); %>
<%response.setContentType("text/html; charset=UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
</head>
<body>
<div id="accessToken">${access_token}</div>
<div id="refreshToken">${refresh_token}</div>
<div id="userseqNo">${user_seq_no}</div>
<script>   
	opener.document.getElementsByName("userAccessToken")[0].value = document.getElementById("accessToken").textContent;
   	opener.document.getElementsByName("userRefreshToken")[0].value = document.getElementById("refreshToken").textContent;  
 	opener.document.getElementsByName("userSeqNo")[0].value = document.getElementById("userseqNo").textContent;
	
	self.close();
</script>

</body>
</html>