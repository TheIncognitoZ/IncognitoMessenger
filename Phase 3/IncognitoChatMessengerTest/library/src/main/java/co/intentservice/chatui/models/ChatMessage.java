package co.intentservice.chatui.models;

import android.text.format.DateFormat;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class ChatMessage {
    private String message;
    private long timestamp;
    private Type type;
    private String receiver;
    private String author;

    public Response.Listener<JSONObject> getListener() {
        return listener;
    }

    public void setListener(Response.Listener<JSONObject> listener) {
        this.listener = listener;
    }

    private Response.Listener<JSONObject> listener;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ChatMessage(){

    }

    public ChatMessage(String message, String author, String receiver, long timestamp, Type type){
        this.message = message;
        this.author = author;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.type = type;
    }

    public ChatMessage(String message, String receiver, String author,  Response.Listener<JSONObject> listener){
        this.message = message;
        this.author = author;
        this.receiver = receiver;
        this.listener = listener;
    }

    public ChatMessage(String author, String message, String receiver){
        this.author = author;
        this.message = message;
        this.receiver = receiver;
    }

    public ChatMessage(String message, String receiver, long timestamp, Type type){
        this.message = message;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.type = type;
    }

    public ChatMessage(String message, long timestamp, Type type){
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public String getFormattedTime(){

        long oneDayInMillis = TimeUnit.DAYS.toMillis(1); // 24 * 60 * 60 * 1000;

        long timeDifference = System.currentTimeMillis() - timestamp;

        return timeDifference < oneDayInMillis
                ? DateFormat.format("hh:mm a", timestamp).toString()
                : DateFormat.format("dd MMM - hh:mm a", timestamp).toString();
    }

    public enum Type {
        SENT, RECEIVED
    }
}
