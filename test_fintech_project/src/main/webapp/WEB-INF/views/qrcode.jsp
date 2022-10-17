<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%request.setCharacterEncoding("utf-8"); %>
<%response.setContentType("text/html; charset=UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<style type="text/css">
	#qrcode{ width:270px; margin: 0 auto;}
</style>
</head>
<body>
	 <p id="qrcode"></p>
	<script type="text/javascript" src="resources/js/qrcode.js"></script>
	<script>
	    function getQueryStringObject() {
			var a = window.location.search.substr(1);
			console.log("search내용:"+window.location.search);
			var b=a.split("=");
			return b;
		}
		var qs = getQueryStringObject();
		console.log(qs[1])
// 		var jwtToken = sessionStorage.getItem('ourToken');
		new QRCode(document.getElementById("qrcode"), 'http://localhost:8090/balance?fintech_use_num='+qs[1]);
	</script>
</body>
</html>