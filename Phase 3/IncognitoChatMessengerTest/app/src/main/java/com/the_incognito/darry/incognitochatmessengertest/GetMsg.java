package com.the_incognito.darry.incognitochatmessengertest;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by darry on 11/30/2016.
 */

public class GetMsg {


    public GetMsg(String author, final String token, Response.Listener<JSONArray> listener) {
        System.out.println("author in GET is :"+author);
        System.out.println("token in get is :"+token);
        final String URL = "https://api.incognitomessenger.me/incongnitomessenger/webapi/messages/"+author;//"http://192.168.1.21:8080/incongnitomessenger/webapi/messages/"+author;
        JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {//Log.d("sometag", "Value: "+response.toString(4));
                    System.out.println("n"+response.toString(4));
                    VolleyLog.v("Response:%n %s", response.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }){   @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("AUTHORIZATION", token);//sending jwt token in the header
            System.out.println("token in get header is :"+token);
            headers.put("CONTENT_TYPE", "application/json");
            return headers;
        }
        };
        //System.out.println("token is :"+token);
        //System.out.println("get req is :"+req.toString());
// add the request object to the queue to be executed
        ChatActivity.getChatRInstance().addToRequestQueue(req);
    }
}
