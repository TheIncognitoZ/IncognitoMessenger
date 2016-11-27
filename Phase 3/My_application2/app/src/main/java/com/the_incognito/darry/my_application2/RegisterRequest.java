package com.the_incognito.darry.my_application2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest {
    private HashMap<String, String> params;
    private static final String REGISTER_REQUEST_URL = "http://192.168.1.21:8080/incongnitomessenger/webapi/auth/register";

    public RegisterRequest(String name, String password, /*String email,*/ Response.Listener<JSONObject> listener) {
        params = new HashMap<>();
        params.put("username", name);
        //params.put("email", email);
        params.put("password", password);
        JsonObjectRequest req = new JsonObjectRequest(REGISTER_REQUEST_URL, new JSONObject(params),
                listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: SOme error", error.getMessage());
            }
        });
        RegisterActivity.getInstance().addToRequestQueue(req);
    }

}
