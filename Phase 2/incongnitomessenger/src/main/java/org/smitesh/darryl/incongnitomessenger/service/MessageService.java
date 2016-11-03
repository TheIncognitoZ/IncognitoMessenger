package org.smitesh.darryl.incongnitomessenger.service;

import java.util.ArrayList;
import java.util.List;

import org.smitesh.darryl.incongnitomessenger.model.Message;

public class MessageService {
	
	public List<Message> getallMessages()
	{
		Message m1 = new Message (1L,"Smitu","Hello World");
		Message m2 = new Message (2L,"Smitu","Hello Jersey");
		List<Message> list = new ArrayList<>();
		list.add(m1);
		list.add(m2);
		return list;
	}
	
}
