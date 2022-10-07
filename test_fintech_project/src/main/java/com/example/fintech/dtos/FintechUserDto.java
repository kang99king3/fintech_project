package com.example.fintech.dtos;


public class FintechUserDto {

	private String userName;
	private String userEmail;
	private String userPassword;
	private String userAccessToken;
	private String userRefreshToken;
	private String userSeqNo;
	public FintechUserDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public FintechUserDto(String userName, String userEmail, String userPassword, String userAccessToken,
			String userRefreshToken, String userSeqNo) {
		super();
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userAccessToken = userAccessToken;
		this.userRefreshToken = userRefreshToken;
		this.userSeqNo = userSeqNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserAccessToken() {
		return userAccessToken;
	}
	public void setUserAccessToken(String userAccessToken) {
		this.userAccessToken = userAccessToken;
	}
	public String getUserRefreshToken() {
		return userRefreshToken;
	}
	public void setUserRefreshToken(String userRefreshToken) {
		this.userRefreshToken = userRefreshToken;
	}
	public String getUserSeqNo() {
		return userSeqNo;
	}
	public void setUserSeqNo(String userSeqNo) {
		this.userSeqNo = userSeqNo;
	}
	@Override
	public String toString() {
		return "FintechUser [userName=" + userName + ", userEmail=" + userEmail + ", userPassword=" + userPassword
				+ ", userAccessToken=" + userAccessToken + ", userRefreshToken=" + userRefreshToken + ", userSeqNo="
				+ userSeqNo + "]";
	}
	
}
