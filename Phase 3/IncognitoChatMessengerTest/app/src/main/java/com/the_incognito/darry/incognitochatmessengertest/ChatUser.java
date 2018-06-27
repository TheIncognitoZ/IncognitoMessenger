package com.the_incognito.darry.incognitochatmessengertest;

/**
 * Created by darry on 11/30/2016.
 */


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by darry on 11/27/2016.
 */

public class ChatUser implements Serializable {

    public String id;
    public String username;
    public String email;
    private String author;
    private String content;
    private String receiver;

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

    public Boolean online;
    public ArrayList<String> room;

    public ChatUser() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ChatUser(/*String id,*/ String username, String email/*, Boolean online, ArrayList<String> room*/) {
        //this.id = id;
        this.username = username;
        this.email = email;
        //  this.online = online;
        //   this.room = room;
    }

    public String getUsername() {
        return username;
    }

/*
    public String getId() {
        return id;
    }
*/

    public String getEmail() {
        return email;
    }

/*    public void setId(String id) {
        this.id = id;
    }*/

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public void setOnline(Boolean online) {
        this.online = online;
    }

    public void setRoom(ArrayList<String> room) {
        this.room = room;
    }*/

}