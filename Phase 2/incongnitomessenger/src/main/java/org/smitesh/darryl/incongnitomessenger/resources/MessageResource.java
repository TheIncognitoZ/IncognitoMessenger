package org.smitesh.darryl.incongnitomessenger.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.smitesh.darryl.incongnitomessenger.model.Message;
import org.smitesh.darryl.incongnitomessenger.service.MessageService;

@Path("messages")
public class MessageResource {
	public MessageService messageService = new MessageService();
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getMessages()
	{
		return messageService.getallMessages();
	}
	
	@GET
	@Path("/{messageID}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getMessage(@PathParam("messageID") String messageID)
	{
		return "Got the message "+ messageID;
	}
}
