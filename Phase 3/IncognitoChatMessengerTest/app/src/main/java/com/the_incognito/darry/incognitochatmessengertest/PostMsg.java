package com.the_incognito.darry.incognitochatmessengertest;

/**
 * Created by darry on 11/30/2016.
 */

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.intentservice.chatui.models.ChatMessage;

/**
 * Created by darry on 11/29/2016.
 */

public class PostMsg {
    private HashMap<String, String> params;
    private static final String POST_REQUEST_URL ="https://api.incognitomessenger.me/incongnitomessenger/webapi/messages"; //"http://requestb.in/13u3n2q1";


    //private Map<String, String> params;

    public PostMsg(String msg, String bud, String author, final String token, Response.Listener<JSONObject> listener) {
        //super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        System.out.println("postmsg is :"+ msg);
        ChatMessage chatMessage = new ChatMessage(msg,  bud,  author,   listener);
        System.out.println("token in post is :"+ token);
        params = new HashMap<>();
        params.put("receiver", chatMessage.getReceiver());
        params.put("author", chatMessage.getAuthor());
        params.put("content", chatMessage.getMessage());

        System.out.println("value of message :"+chatMessage.getMessage());
        System.out.println("value of receiver :"+chatMessage.getReceiver());
        System.out.println("value of author :"+chatMessage.getAuthor());

        JsonObjectRequest req = new JsonObjectRequest(POST_REQUEST_URL, new JSONObject(params),
                listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: PostMsg error", error.getMessage());
                /*String body;
                //get status code here
                //get response body and parse with appropriate encoding
                if(error.networkResponse.data!=null) {

                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        }){   @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            System.out.println("token in postHeader is :"+ token);
            headers.put("AUTHORIZATION", token);//sending jwt token in the header
            headers.put("CONTENT_TYPE", "application/json");
            return headers;
        }
        };
        System.out.println("Post test req is :"+req);
        ChatActivity.getChatSInstance().addToRequestQueue(req);
    }
}
