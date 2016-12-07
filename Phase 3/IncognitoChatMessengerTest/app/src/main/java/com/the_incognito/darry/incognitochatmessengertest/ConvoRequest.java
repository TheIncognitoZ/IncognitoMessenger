package com.the_incognito.darry.incognitochatmessengertest;

/**
 * Created by darry on 11/29/2016.
 */

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConvoRequest {
    private HashMap<String, String> params;
    private static final String EMAIL_CHECK_URL = "http://192.168.1.16:8080/incongnitomessenger/webapi/auth/emailcheck";
    //private static final String EMAIL_CHECK_URL ="https://api.incognitomessenger.me/incongnitomessenger/webapi/auth/emailcheck";//nalula@gmail.com http://requestb.in/1hjw87e1";//
    //private Map<String, String> params;

    public ConvoRequest(String email, Response.Listener<JSONObject> listener) {
        params = new HashMap<>();
        params.put("email", email);
        System.out.println("email is :"+email);

        JsonObjectRequest req = new JsonObjectRequest(EMAIL_CHECK_URL, new JSONObject(params),
                listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: SOme error", error.getMessage());
            }
        });
        ConvoActivity.getInstance().addToRequestQueue(req);
    }
}

