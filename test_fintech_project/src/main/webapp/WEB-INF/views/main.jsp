<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%request.setCharacterEncoding("utf-8"); %>
<%response.setContentType("text/html; charset=UTF-8"); %>
<%@include file="header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<style type="text/css">
	body{margin: 50px;}
	.box{border: 1px solid gray;}
	.box > .sub_menu{text-align: right;}
	a{cursor: pointer;}
</style>

<script type="text/javascript">

	$(function(){
		$(".myinfo").click(function(event){
			event.preventDefault();
			$.ajax({
				url:"myinfo",
				method:"get",
				dataType:"json",
				success:function(data){
// 					alert(data.res_list[0].bank_name);
					var ul=$("<ul>");
					
// 					for(var i=0;i<data.res_list.length;i++){
// 						var li=$("<li>");
// 						ul.append(li.text(data.res_list[i].bank_name
// 								+" : "+data.res_list[i].account_num_masked
// 								+ "("+data.res_list[i].account_alias+")"));
// 					}

					for(var i=0;i<data.res_list.length;i++){
						$("#list").append(
							"<div class='box'>"+
							"	<div>"+
							"		<h1>"+data.res_list[i].account_alias+"</h1>"+
							"		<p>"+data.res_list[i].fintech_use_num+"</p>"+
							"	</div>"+
							"	<div class='sub_menu'>"+
							"		<a class='balance' onclick='balance(\""+data.res_list[i].fintech_use_num+"\",this)'>잔액조회</a>||||"+
							"		<a onclick='createQrcode(\""+data.res_list[i].fintech_use_num+"\")'>QR생성</a>"+
							"	</div>"+
							"   <div class='balance_amt'></div>"+
							"</div>"
						)
					}
					
					
// 					$("#list").html(ul);
				},
				error:function(){
					
				}
			})
		});
	});
	
	function balance(fintech_use_num,target){
		$.ajax({
			url:"/balance",
			method:"get",
			data:{"fintech_use_num":fintech_use_num},
			dataType:"json",
			success:function(data){
				var box=$(target).parents(".box").eq(0);
				box.find(".balance_amt").html("<p>잔액:"+data.balance_amt+"</p>"+
											  "<p><a onclick='transactionList(\""+fintech_use_num+"\")'>거래내역조회</a></p>"+
											  "<div class='transaction_list'></div>"
											);
			},error:function(){
				alert("에러");
			}
		})
	}
	
	function transactionList(fintech_use_num){
		$.ajax({
			url:"/transactionList",
			method:"get",
			data:{"fintech_use_num":fintech_use_num},
			dataType:"json",
			success:function(data){
				var list="<ul>";
				for(var i=0;i<data.res_list.length;i++){
					var res_list=data.res_list[i]
					list+="<li>["+res_list.branch_name+"] "+res_list.print_content+":"+res_list.tran_amt+"</li>";
				}
				list+="</ul>";
				$(".transaction_list").html(list);
			},error:function(){
				
			}
		});
	}
	
	function createQrcode(fintech_use_num){
		window.open('qrcode?fintech_use_num='+fintech_use_num,'QR생성','width=400px,height=400px');
	}
</script>
</head>
<body>
<h1>Main 페이지</h1>
<h2>로그인 후 화면</h2>
<div>${ldto.userName}님 로그인 중 || <a class="myinfo btn btn-default" >나의정보</a></div>
<div id="list">

</div>
<!-- <li><a href="signin_form">SignIN</a></li> -->
<!--                   <li><a href="signup">SignUp</a></li> -->
</body>
</html>


