package org.smitesh.darryl.incongnitomessenger.resources;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.HeaderParam;

import org.smitesh.darryl.incongnitomessenger.model.Message;
import org.smitesh.darryl.incongnitomessenger.service.MessageService;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

@Path("messages")
public class MessageResource {
	public MessageService messageService = new MessageService();
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public List<Message> getMessages()
//	{
//		return messageService.getallMessages();
//	}
//	
	@GET
	@Path("/{messageID}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getMessage(@PathParam("messageID") String messageID, @HeaderParam("Authorization") String auth) 
	{
		String jwt = auth;
		final String secret = "{I AM A PEN}";
		try {
		    final JWTVerifier verifier = new JWTVerifier(secret);
		    final Map<String, Object> claims= verifier.verify(jwt);
		    String got = claims.get("usr").toString();
		    if(got.equals(messageID))
		    {
		    	return messageService.getMessage(messageID);
		    }
		    else
		    {
		    	throw new JWTVerifyException();
		    }
		} catch (JWTVerifyException e) {
		    return messageService.getMessage("invalid");
		} catch (InvalidKeyException e) {
			return messageService.getMessage("invalid");
		} catch (NoSuchAlgorithmException e) {
			return messageService.getMessage("invalid");
		} catch (IllegalStateException e) {
			return messageService.getMessage("invalid");
		} catch (SignatureException e) {
			return messageService.getMessage("invalid");
		} catch (IOException e) {
			return messageService.getMessage("invalid");
		}
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Message putMessage(Message message,  @HeaderParam("Authorization") String auth)
	{
		String jwt = auth;
		final String secret = "{I AM A PEN}";
		try {
		    final JWTVerifier verifier = new JWTVerifier(secret);
		    final Map<String, Object> claims= verifier.verify(jwt);
		    String got = claims.get("usr").toString();
		    if(got.equals(message.getAuthor()))
		    {
		    	return messageService.putMessage(message);
		    }
		    else
		    {
		    	throw new JWTVerifyException();
		    }
		} catch (JWTVerifyException e) {
		    return new Message ("Invalid","Invalid","Invalid","Invalid","Invalid");
		} catch (InvalidKeyException e) {
			return new Message ("Invalid","Invalid","Invalid","Invalid","Invalid");
		} catch (NoSuchAlgorithmException e) {
			return new Message ("Invalid","Invalid","Invalid","Invalid","Invalid");
		} catch (IllegalStateException e) {
			return new Message ("Invalid","Invalid","Invalid","Invalid","Invalid");
		} catch (SignatureException e) {
			return new Message ("Invalid","Invalid","Invalid","Invalid","Invalid");
		} catch (IOException e) {
			return new Message ("Invalid","Invalid","Invalid","Invalid","Invalid");
		}
		
	}
	@POST
	@Path("/newuser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Message newUser(Message message)
	{
		return messageService.newUser(message);
	}
	
}
