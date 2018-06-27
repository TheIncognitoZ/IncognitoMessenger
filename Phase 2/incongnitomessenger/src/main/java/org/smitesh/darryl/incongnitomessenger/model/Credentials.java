package org.smitesh.darryl.incongnitomessenger.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Credentials {

	private String username; 	
	private String password;
	private String email;
	public Credentials()
	{
		
	}
	public Credentials(String email, String username, String password)
	{
		super();
		this.email = email;
		this.username = username;
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email= email;
	}
}
