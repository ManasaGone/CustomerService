package com.user.response;





public class JwtResponse {
	private String accessToken;
	private String type = "Bearer";
	private int customerId;
	private String username;
	private String email;
 
	
	
	public JwtResponse(String accessToken, int CustomerId, String username, String email) {
		this.accessToken = accessToken;
		this.customerId = customerId;
		this.username = username;
		this.email = email;
	}
 
	public String getAccessToken() {
		return accessToken;
	}
 
	
 
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
 
	public String getTokenType() {
		return type;
	}
 
	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}
 
	public int getCustomerId() {
		return customerId;
	}
 
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
 
	public String getEmail() {
		return email;
	}
 
	public void setEmail(String email) {
		this.email = email;
	}
 
	public String getUsername() {
		return username;
	}
 
	public void setUsername(String username) {
		this.username = username;
	}
 
}
 