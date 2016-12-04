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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import co.intentservice.chatui.models.ChatMessage;

import static com.the_incognito.darry.incognitochatmessengertest.LoginActivity.username;

/**
 * Created by darry on 11/29/2016.
 */

public class PostMsg {
    private HashMap<String, String> params;
    private static final String POST_REQUEST_URL ="http://192.168.1.16:8080/incongnitomessenger/webapi/messages"; //"http://requestb.in/13u3n2q1"; https://api.incognitomessenger.me/incongnitomessenger/webapi/messages


    //private Map<String, String> params;

    public PostMsg(String msg, String bud, String author, final String token , final String signature, String publicKey, Response.Listener<JSONObject> listener) {
        //super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        System.out.println("postmsg is :"+ msg);
        //ChatMessage chatMessage = new ChatMessage(msg,  bud,  author,   listener);
        System.out.println("token in post is :"+ token);
        params = new HashMap<>();
        params.put("receiver", bud);
        params.put("author", author);
        params.put("content", msg);
        params.put("sign",signature);
        params.put("publickey",publicKey);
        System.out.println("value of message :"+msg);
        System.out.println("value of receiver :"+bud);
        System.out.println("value of author :"+author);
        System.out.println("value of signature :"+signature);
        System.out.println("value of publicKey :"+publicKey);

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
