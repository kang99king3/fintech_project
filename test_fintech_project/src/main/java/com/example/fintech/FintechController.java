package com.example.fintech;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.fintech.dtos.FintechUserDto;
import com.example.fintech.service.IUserService;


@Controller
public class FintechController {
	
	@Autowired
	private IUserService userService;

	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	
	@GetMapping("/authresult")
	public String authResult(String code, Model model, HttpServletRequest request) throws IOException, ParseException {
		
		//사용자의 동의 및 계좌 인증을 통해 사용자 인증 진행 후 인증 코드 받기: code
		System.out.println("Code: "+code);
		
		HttpURLConnection con=null;
		JSONObject result=null;
		
		//사용자 토큰을 받기 위해 먼저 받아온 인증 code를 포함하여 api 요청하기
		URL url = new URL(
				"https://testapi.openbanking.or.kr/oauth/2.0/token?"
				+"code="+code
				+"&client_id=4987e938-f84b-4e23-b0a2-3b15b00f4ffd"
				+"&client_secret=3ff7570f-fdfb-4f9e-8f5a-7b549bf2adec"
				+"&redirect_uri=http://localhost:8090/authresult"
				+"&grant_type=authorization_code"
		);
		
		con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");//post로 요청하게 되어 있어서 설정함
//		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Content-Type", "application/json");
//		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);//데이터 가져오려면 true로 설정해야 한다고 함
		System.out.println(con.getInputStream());
		//받아온 결과값이 inputStream이므로 문자열을 읽어서 response 변수에 저장해둠(문자열 형태는 json)
		try(
				BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"))
			) {
			    	StringBuilder response = new StringBuilder();
			    	String responseLine = null;
			    	while ((responseLine = br.readLine()) != null) {
			    		System.out.println(responseLine.trim());
			    		response.append(responseLine.trim());
			    	}
			    	//문자열을 정말 json객체로 변환한다--> 그래야 키별로 값을 가져오기 쉬움(문자열 검색 및 자르기를 하면 복잡함)
			    	result=(JSONObject) new JSONParser().parse(response.toString());
//			    	System.out.println(response.toString());refresh_token    user_seq_no
			   }  
		
		//json객체로 변환된 결과 값을 가져온다.
		String access_token=result.get("access_token").toString();
		String refresh_token=result.get("refresh_token").toString();
		String user_seq_no=result.get("user_seq_no").toString();
		
		//출력해보기
		System.out.println("access_token:"+access_token);
		System.out.println("refresh_token:"+refresh_token);
		System.out.println("user_seq_no:"+user_seq_no);
		
		//클라이언트로 전달하기 위해 model에 저장---->나중에는 세션에 저장하자
		model.addAttribute("access_token", access_token);
		model.addAttribute("refresh_token", refresh_token);
		model.addAttribute("user_seq_no", user_seq_no);
		
		return "authresult";
	}
	//'Content-Type' : 'application/x-www-form-urlencoded
	
	//회원가입 폼 이동
	@GetMapping("/signup")
	public String signUp(String code, Model model) {
		System.out.println("Code: "+code);
		model.addAttribute("code", code);
		return "signup";
	}
	
	//회원가입하기
	@PostMapping("/adduser")
	public String addUser(FintechUserDto dto) {
		System.out.println("dto:"+dto.toString());
		boolean isS=userService.addUser(dto);
		if(isS) {
			System.out.println("회원가입성공");
			return "redirect:/";
		}else {
			System.out.println("회원가입실패");
			return "error";
		}	
	}
	
	//로그인 폼 이동
	@GetMapping("/signin_form")
	public String signInForm(Model model) {
		System.out.println("로그인폼이동");
		
		return "signin_form";
	}
	
	//로그인 하기
	@PostMapping("/login")
	public String login(FintechUserDto dto, HttpServletRequest request) {
		FintechUserDto ldto=userService.login(dto);
		if(ldto==null) {
			System.out.println("회원없음");
			return "redirect:signin_form";
		}else {
			System.out.println("회원이 맞음");
			HttpSession session=request.getSession();
			session.setAttribute("ldto", ldto);
			session.setMaxInactiveInterval(60*10);//10분간 요청이 없으면 session 값 사라짐
			return "main";
		}
	}
	
	//사용자 정보 조회
	@GetMapping("/myinfo")
	@ResponseBody
	public JSONObject myInfo(Model model, HttpServletRequest request) throws IOException, ParseException {
		System.out.println("나의 정보");
		HttpURLConnection con=null;
		JSONObject result=null;
		//로그인시 저장해준 세션에서 사용자의 사용자 이용 번호(user_seq_no)와 access_token을 구한다.
		HttpSession session=request.getSession();
		FintechUserDto ldto=(FintechUserDto)session.getAttribute("ldto");
		
		URL url=new URL("https://testapi.openbanking.or.kr/v2.0/user/me?"
				+ "user_seq_no="+ldto.getUserSeqNo());
		con = (HttpURLConnection)url.openConnection();
		//헤더 정보 
		con.setRequestMethod("GET");//post로 요청하게 되어 있어서 설정함
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", "Bearer "+ldto.getUserAccessToken());
		con.setDoOutput(true);//데이터 가져오려면 true로 설정해야 한다고 함
		System.out.println(con.getInputStream());
		//받아온 결과값이 inputStream이므로 문자열을 읽어서 response 변수에 저장해둠(문자열 형태는 json)
		try(
				BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"))
			) {
			    	StringBuilder response = new StringBuilder();
			    	String responseLine = null;
			    	while ((responseLine = br.readLine()) != null) {
			    		System.out.println(responseLine.trim());
			    		response.append(responseLine.trim());
			    	}
			    	//문자열을 정말 json객체로 변환한다--> 그래야 키별로 값을 가져오기 쉬움(문자열 검색 및 자르기를 하면 복잡함)
			    	result=(JSONObject) new JSONParser().parse(response.toString());
//			    	System.out.println(response.toString());refresh_token    user_seq_no
			   }  
		
		
		return result;
	}
	
	//현재시간 구하는 메서드
	public String getDateTime() {
		
		LocalDateTime now = LocalDateTime.now();
		
		String formatNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		
		return formatNow;
	}
	
	//이용기관 부여번호 9자리 만드는 메서드
	public String createNum() {
		//이용기관 부여번호 렌덤생성
		String createNum="";
		for (int i = 0; i < 9; i++) {
			createNum+=((int)(Math.floor(Math.random()*10)))+"";
		}
		System.out.println("이용기관부여번호9자리:"+createNum);
				
		return createNum;
	}
	
	
	//잔액조회
	@GetMapping("/balance")
	@ResponseBody
	public JSONObject balance(String fintech_use_num, Model model, HttpServletRequest request) throws IOException, ParseException {
		
		//servletContext에 저장 관리?
//		String num="";
//		if(request.getServletContext().getAttribute("fintech_num")!=null) {
//			num=(String)request.getServletContext().getAttribute("fintech_num");			
//		}
//		
//		for (int i = 0; i < num.length(); i++) {
//			
//		}
		
		
		//사용자의 동의 및 계좌 인증을 통해 사용자 인증 진행 후 인증 코드 받기: code
		System.out.println("fin_use_num: "+fintech_use_num);
		FintechUserDto ldto=(FintechUserDto)request.getSession().getAttribute("ldto");
		HttpURLConnection con=null;
		JSONObject result=null;
		
		
		
		//사용자 토큰을 받기 위해 먼저 받아온 인증 code를 포함하여 api 요청하기
		//bank_tran_id와 tran_dtime값은 생성하는 메서드나 코드 추가해야 함
		URL url = new URL(
				"https://testapi.openbanking.or.kr/v2.0/account/balance/fin_num?"
			  + "bank_tran_id=M202201886U"+createNum()   // bank_tran_id값은 "이용기관코드[client_use_code](10자리)+이용기관고유번호+이용기관 부여번호(9자리)"
			  + "&fintech_use_num="+fintech_use_num
			  + "&tran_dtime="+getDateTime()
		);
		
		con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");//post로 요청하게 되어 있어서 설정함
//		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Content-Type", "application/json"); 
		con.setRequestProperty("Authorization", "Bearer "+ldto.getUserAccessToken());
		//                                       형식   + access_token(session에서 가져오면 됨)
		con.setDoOutput(true);//데이터 가져오려면 true로 설정해야 한다고 함
		System.out.println(con.getInputStream());
		//받아온 결과값이 inputStream이므로 문자열을 읽어서 response 변수에 저장해둠(문자열 형태는 json)
		try(
				BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"))
			) {
			    	StringBuilder response = new StringBuilder();
			    	String responseLine = null;
			    	while ((responseLine = br.readLine()) != null) {
			    		System.out.println(responseLine.trim());
			    		response.append(responseLine.trim());
			    	}
			    	//문자열을 정말 json객체로 변환한다--> 그래야 키별로 값을 가져오기 쉬움(문자열 검색 및 자르기를 하면 복잡함)
			    	result=(JSONObject) new JSONParser().parse(response.toString());
//			    	System.out.println(response.toString());refresh_token    user_seq_no
			   }  
		
		
		return result;
	}
	
	//거래내역조회
	@GetMapping("/transactionList")
	@ResponseBody
	public JSONObject transactionList(String fintech_use_num,String bank_tran_id, Model model, HttpServletRequest request) throws IOException, ParseException {
		

		//사용자의 동의 및 계좌 인증을 통해 사용자 인증 진행 후 인증 코드 받기: code
		System.out.println("fin_use_num: "+fintech_use_num);
		FintechUserDto ldto=(FintechUserDto)request.getSession().getAttribute("ldto");
		HttpURLConnection con=null;
		JSONObject result=null;
		
		
		
		//사용자 토큰을 받기 위해 먼저 받아온 인증 code를 포함하여 api 요청하기
		//bank_tran_id와 tran_dtime값은 생성하는 메서드나 코드 추가해야 함
		URL url = new URL(
				  "https://testapi.openbanking.or.kr/v2.0/account/transaction_list/fin_num?"
			    + "bank_tran_id=M202201886U"+createNum()
			    + "&fintech_use_num="+fintech_use_num
			    + "&inquiry_type=A"
			    + "&inquiry_base=D"
			    + "&from_date=20190101"
			    + "&to_date=20190101"
			    + "&sort_order=D"
			    + "&tran_dtime="+getDateTime()	
			
		);
		
		con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");//post로 요청하게 되어 있어서 설정함
//			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Content-Type", "application/json"); 
		con.setRequestProperty("Authorization", "Bearer "+ldto.getUserAccessToken());
		//                                       형식   + access_token(session에서 가져오면 됨)
		con.setDoOutput(true);//데이터 가져오려면 true로 설정해야 한다고 함
		System.out.println(con.getInputStream());
		//받아온 결과값이 inputStream이므로 문자열을 읽어서 response 변수에 저장해둠(문자열 형태는 json)
		try(
				BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"))
			) {
			    	StringBuilder response = new StringBuilder();
			    	String responseLine = null;
			    	while ((responseLine = br.readLine()) != null) {
			    		System.out.println(responseLine.trim());
			    		response.append(responseLine.trim());
			    	}
			    	//문자열을 정말 json객체로 변환한다--> 그래야 키별로 값을 가져오기 쉬움(문자열 검색 및 자르기를 하면 복잡함)
			    	result=(JSONObject) new JSONParser().parse(response.toString());
//				    	System.out.println(response.toString());refresh_token    user_seq_no
			   }  
		
		
		return result;
	}
	
	@GetMapping("qrcode")
	public String qrCode(String fintech_use_num) {
		System.out.println("fintech_use_num:"+fintech_use_num );
		return "qrcode";
	}
}
