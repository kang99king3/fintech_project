<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%request.setCharacterEncoding("utf-8"); %>
<%response.setContentType("text/html; charset=UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<title>Full Width Pics - Start Bootstrap Template</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
<!-- Favicon-->
<link rel="icon" type="image/x-icon" href="resources/assets/favicon.ico" />
<!-- Core theme CSS (includes Bootstrap)-->
<link href="resources/css/styles.css" rel="stylesheet" />
<style type="text/css">
	body{margin: 50px;}
 	.box{border-bottom: 1px solid gray;} 
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
	//					alert(data.res_list[0].bank_name);
					var ul=$("<ul>");
					
	//					for(var i=0;i<data.res_list.length;i++){
	//						var li=$("<li>");
	//						ul.append(li.text(data.res_list[i].bank_name
	//								+" : "+data.res_list[i].account_num_masked
	//								+ "("+data.res_list[i].account_alias+")"));
	//					}
	
					for(var i=0;i<data.res_list.length;i++){
						$("#list").append(
							"<div class='box container'>"+
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
					
					
	//					$("#list").html(ul);
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
											  "<p><a onclick='transactionList(\""+fintech_use_num+"\",this)'>거래내역조회</a></p>"+
											  "<div class='transaction_list'></div>"
											);
			},error:function(){
				alert("에러");
			}
		})
	}
	
	function transactionList(fintech_use_num,target){
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
				$(target).parent().next(".transaction_list").html(list);
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
<!-- Responsive navbar-->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container">
                <a class="navbar-brand" href="#!">Start Fintech</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                        <li class="nav-item"><a class="nav-link active" aria-current="page" href="/">Home</a></li>
                        <li class="nav-item"><a class="nav-link" href="#!">About</a></li>
                        <li class="nav-item"><a class="nav-link" href="#!">Contact</a></li>
                        <li class="nav-item"><a class="nav-link">${ldto.userName}님</a></li>
                        <li class="nav-item"><a class="myinfo nav-link" >나의정보</a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <!-- Header - set the background image for the header in the line below-->
<!--         <header class="py-5 bg-image-full" style="background-image: url('https://source.unsplash.com/wfh8dDlNFOk/1600x900')"> -->
<!--             <div class="text-center my-5"> -->
<!--                 <img class="img-fluid rounded-circle mb-4" src="https://dummyimage.com/150x150/6c757d/dee2e6.jpg" alt="..." /> -->
<!--                 <h1 class="text-white fs-3 fw-bolder">Fintech Open Banking</h1> -->
<!--                 <p class="text-white-50 mb-0">Landing Page Template</p> -->
<!--             </div> -->
<!--         </header> -->
        <!-- Content section-->
        <section class="py-5">
            <div class="container my-5">
                <div class="row justify-content-center">
                    <div class="col-lg-12">
						<div id="list">
						
						</div>
						<br/><br/><br/>
                    </div>
                </div>
            </div>
        </section>
        
        <!-- Footer-->
        <footer class="py-5 bg-dark">
            <div class="container"><p class="m-0 text-center text-white">Copyright &copy; Your Website 2022</p></div>
        </footer>
        <!-- Bootstrap core JS-->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <!-- Core theme JS-->
        <script src="resources/js/scripts.js"></script>
        <!-- 합쳐지고 최소화된 최신 자바스크립트 -->
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
		
</body>
</html>

