package org.smitesh.darryl.incongnitomessenger.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Response {

	private Boolean success; 
	private String token;
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Response()
	{
		this.success=false;
		this.token="Invalid Token";
		this.username="Invalid Username";
	}
	public Response(Boolean success)
	{
		super();
		this.success=success;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}



	
}
