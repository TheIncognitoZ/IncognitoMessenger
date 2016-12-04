package org.smitesh.darryl.incongnitomessenger.model;
//import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class Message {
	
	//private long id;
	private String author;
	//private Date created;
	private String content;
	private String receiver;
	private String publickey;
	private String sign;
	public Message()
	{
		
	}
	
	public Message(String author, String content, String receiver, String publickey, String sign) {
		super();
		//this.id = id;
		this.author = author;
		this.content = content;
		this.receiver = receiver;
		this.publickey = publickey;
		this.sign = sign;
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getPublickey() {
		return publickey;
	}

	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
