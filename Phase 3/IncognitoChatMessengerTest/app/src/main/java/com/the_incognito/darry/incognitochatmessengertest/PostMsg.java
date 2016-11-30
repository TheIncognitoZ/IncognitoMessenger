package com.the_incognito.darry.incognitochatmessengertest;

/**
 * Created by darry on 11/30/2016.
 */

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
/**
 * Created by darry on 11/29/2016.
 */

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;

import co.intentservice.chatui.models.ChatMessage;

public class PostMsg {
    private HashMap<String, String> params;
    private static final String LOGIN_REQUEST_URL = "http://requestb.in/13u3n2q1";//https://api.incognitomessenger.me/incongnitomessenger/webapi/auth/login";


    //private Map<String, String> params;

    public PostMsg(String msg, String bud,String author, Response.Listener<JSONObject> listener) {
        //super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        System.out.println("postmsg is :"+ msg);
        ChatMessage chatMessage = new ChatMessage(msg,  bud,  author,   listener);

        params = new HashMap<>();
        params.put("receiver", chatMessage.getReceiver());
        params.put("author", chatMessage.getAuthor());
        params.put("content", chatMessage.getMessage());
        System.out.println("value of message :"+chatMessage.getMessage());
        System.out.println("value of receiver :"+chatMessage.getReceiver());
        System.out.println("value of author :"+chatMessage.getAuthor());
        JsonObjectRequest req = new JsonObjectRequest(LOGIN_REQUEST_URL, new JSONObject(params),
                listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: PostMsg error", error.getMessage());
            }
        });
        ChatActivity.getInstance().addToRequestQueue(req);
    }
}
