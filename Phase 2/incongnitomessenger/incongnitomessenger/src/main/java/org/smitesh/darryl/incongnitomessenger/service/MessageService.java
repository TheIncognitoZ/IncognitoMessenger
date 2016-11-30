package org.smitesh.darryl.incongnitomessenger.service;

import java.util.ArrayList;
import java.util.List;

import org.smitesh.darryl.incongnitomessenger.model.Message;
import java.sql.*;
public class MessageService {

	public List<Message> getallMessages()
	{
		List<Message> list = new ArrayList<>();
		Message m;
		try{
			Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/messagedb";
		Connection myConn = DriverManager.getConnection(url , "root", "Smitesh123");
		Statement myStmt = myConn.createStatement();
		ResultSet myRes = myStmt.executeQuery("select * from messages");
		while(myRes.next())
		{
			m = new Message( myRes.getString("sender"), myRes.getString("content"), myRes.getString("recevier")); 
			list.add(m);
		}
 		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//Message m1 = new Message (1L,"Smitu","Hello World");
		//Message m2 = new Message (2L,"Smitu","Hello Jersey");
		//list.add(m1);
		//list.add(m2);
		return list;
	}
	public List<Message> getMessage(String username)
	{
		List<Message> list = new ArrayList<Message>();
		Message m = new Message();
		try{
			Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/messagedb";
		Connection myConn = DriverManager.getConnection(url , "root", "Smitesh123");
		Statement myStmt = myConn.createStatement();
		ResultSet myRes = myStmt.executeQuery("select * from "+username);
		while(myRes.next())
		{
			m = new Message(myRes.getString("sender"), myRes.getString("content"), myRes.getString("receiver")); 
			list.add(m);
		}
 		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	public Message putMessage(Message m)
	{
		try{
			Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/messagedb";
		Connection myConn = DriverManager.getConnection(url , "root", "Smitesh123");
		Statement myStmt = myConn.createStatement();
		//System.out.println("ID"+m.getId()+"\nAuthor"+m.getAuthor()+"\nContent"+m.getContent());
		String query = "INSERT INTO "+m.getReceiver()+" (`sender`,`content`) VALUES ('"+m.getAuthor()+"','"+m.getContent()+"')";
		System.out.println(query);
		myStmt.executeUpdate(query);
		}
		catch(Exception e)
		{
			e.printStackTrace();	
		}
		return m;
	}
	public Message newUser(Message m)
	{
	try{
		Class.forName("com.mysql.jdbc.Driver");
	String url = "jdbc:mysql://localhost:3306/messagedb";
	Connection myConn = DriverManager.getConnection(url , "root", "Smitesh123");
	Statement myStmt = myConn.createStatement();
	//System.out.println("ID"+m.getId()+"\nAuthor"+m.getAuthor()+"\nContent"+m.getContent());
	String query = "CREATE table "+m.getAuthor()+"( id INT NOT NULL AUTO_INCREMENT, `receiver` VARCHAR(45) NOT NULL,`content` VARCHAR(45) NULL, `timestamp` TIMESTAMP(6) NULL, PRIMARY KEY (`id`));";
	myStmt.executeUpdate(query);
	}
	catch(Exception e)
	{
		e.printStackTrace();	
	}
	return m;
	}
}
