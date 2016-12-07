package org.smitesh.darryl.incongnitomessenger.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import org.smitesh.darryl.incongnitomessenger.mail.TLSEmail;
import org.smitesh.darryl.incongnitomessenger.model.Credentials;
import org.smitesh.darryl.incongnitomessenger.model.Response;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.internal.org.apache.commons.lang3.RandomStringUtils;

public class AuthService {
	public Response NewUser(Credentials creds) {
		//Credentials data = new Credentials();
		Response result = new Response(true);
		try{
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/messagedb";
		Connection myConn = DriverManager.getConnection(url , "root", "");
		Statement myStmt = myConn.createStatement();
		//System.out.println("ID"+m.getId()+"\nAuthor"+m.getAuthor()+"\nContent"+m.getContent());
		String query = "INSERT INTO users (`username`,`password`,`email`) VALUES ('"+creds.getUsername()+"','"+creds.getPassword()+"','"+creds.getEmail()+"')";
		String query1 = "CREATE TABLE `messagedb`.`"+creds.getUsername()+"` (`id` INT NOT NULL AUTO_INCREMENT,`sender` VARCHAR(45) NOT NULL,`receiver` VARCHAR(45) NOT NULL,`content` VARCHAR(200) NULL,`publickey` VARCHAR(200),`sign` VARCHAR(200),PRIMARY KEY (`id`))";
		System.out.println(query);
		System.out.println(query1);
		myStmt.executeUpdate(query);
		myStmt.executeUpdate(query1);
		result.setSuccess(true);
		if(result.getSuccess())
		{
			String abc =RandomStringUtils.randomAlphanumeric(7);
			String query2 = "UPDATE `messagedb`.`users` SET `URL`='"+abc+"' WHERE `username`='"+creds.getUsername()+"'";
			myStmt.executeUpdate(query2);
			TLSEmail mailer = new TLSEmail();
			mailer.EmailSendMain(creds.getEmail(), " http://localhost:8080/incongnitomessenger/webapi/auth/verify/"+abc);
		}
		}
		catch(Exception e)
		{
			result.setSuccess(false);
			e.printStackTrace();	
		}
		return result;
	}

	public Response Login(Credentials creds) {
			Response result= new Response();
			try{
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/messagedb";
			Connection myConn = DriverManager.getConnection(url , "root", "");
			Statement myStmt = myConn.createStatement();
			System.out.println("Username = "+creds.getUsername());
			//System.out.println("ID"+m.getId()+"\nAuthor"+m.getAuthor()+"\nContent"+m.getContent());
			String query = "SELECT * from users where username='"+creds.getUsername()+"' AND password='"+creds.getPassword()+"'";
			System.out.println(query);
			ResultSet myRes = myStmt.executeQuery(query);
			myRes.next();
			System.out.println("Checking if verified:"+myRes.getString("Verified"));
			if(myRes.getInt("Verified")==1)
			{
					final String issuer = "https://localhost:8080/incongnitomessenger";
					final String secret = "{I AM A PEN}";
					final long iat = System.currentTimeMillis() / 1000L; // issued at claim 
					final long exp = iat + 6000000L; // expires claim. In this case the token expires in 60 seconds
			
					final JWTSigner signer = new JWTSigner(secret);
					final HashMap<String, Object> claims = new HashMap<String, Object>();
					claims.put("iss", issuer);
					claims.put("exp", exp);
					claims.put("iat", iat);
					claims.put("usr", creds.getUsername());
					final String jwt = signer.sign(claims);
					result.setSuccess(true);
					result.setUsername(creds.getUsername());
					result.setToken(jwt);
			}
			else
			{
				result.setUsername("Email ID Not Verified");
				result.setSuccess(false);
				result.setToken("Invalid Username");
			}
			}
			catch(Exception e)
			{
				e.printStackTrace();	
			}
			return result;
		}
	public Response AccountCheck(Credentials email)
	{
		Response result= new Response();
		try{
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/messagedb";
		Connection myConn = DriverManager.getConnection(url , "root", "");
		Statement myStmt = myConn.createStatement();
		//System.out.println("Username = "+creds.getUsername());
		//System.out.println("ID"+m.getId()+"\nAuthor"+m.getAuthor()+"\nContent"+m.getContent());
		String query = "SELECT * from users where email='"+email.getEmail()+"'";
		ResultSet myRes = myStmt.executeQuery(query);
		if(myRes.next())
		{
			result.setUsername(myRes.getString("username"));
			result.setSuccess(true);
		}
		else
		{
			result.setSuccess(false);
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	public Response Initialize()
	{
		Response result= new Response();
		try{
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/messagedb";
		Connection myConn = DriverManager.getConnection(url , "root", "Smitesh123");
		Statement myStmt = myConn.createStatement();
		//System.out.println("Username = "+creds.getUsername());
		//System.out.println("ID"+m.getId()+"\nAuthor"+m.getAuthor()+"\nContent"+m.getContent());
		String query1 = "DROP TABLE `messagedb`.`users`";
		String query = "CREATE TABLE `messagedb`.`users` (`username` VARCHAR(45) NOT NULL,`password` VARCHAR(45) NOT NULL,`email` VARCHAR(45) NULL,PRIMARY KEY (`email`))";
		myStmt.executeUpdate(query1);
		myStmt.executeUpdate(query);
		result.setSuccess(true);
		}
		catch(Exception e)
		{
			result.setSuccess(false);
			e.printStackTrace();	
		}
		return result;
	}
	public boolean validateEmail(String randurl)
	{
		boolean flag = false;
		try{
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/messagedb";
		Connection myConn = DriverManager.getConnection(url , "root", "");
		Statement myStmt = myConn.createStatement();
		//System.out.println("Username = "+creds.getUsername());
		//System.out.println("ID"+m.getId()+"\nAuthor"+m.getAuthor()+"\nContent"+m.getContent());
		String query = "SELECT username from users where URL='"+randurl+"'";
		ResultSet myRes = myStmt.executeQuery(query);
		if(myRes.next())
		{
			myStmt.executeUpdate("UPDATE `messagedb`.`users` SET `Verified`=1 WHERE `username`='"+myRes.getString("username")+"'");
			flag=true;
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();	
			flag=false;
		}
		return flag;
	}
}
