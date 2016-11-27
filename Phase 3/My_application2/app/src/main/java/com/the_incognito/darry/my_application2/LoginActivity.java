package com.the_incognito.darry.my_application2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
    private static LoginActivity lInstance;
    public static final String TAG = "VolleyPatterns";
    private RequestQueue lRequestQueue;
    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lInstance = this;
        setContentView(R.layout.activity_login);

        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        final Button bLogin = (Button) findViewById(R.id.bSignIn);

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etName.getText().toString();
                final String password = etPassword.getText().toString();

                // Response received from the server
                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //JSONObject jsonResponse = new JSONObject(response);
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            boolean success = response.getBoolean("success");
                            System.out.println("response is :"+response.toString(4));
                            if (success) {
                                String username = response.getString("username");
                                token = response.getString("token");
                                System.out.println("token is :"+token);
                                System.out.println("success is :"+success);
                                System.out.println("username is :"+username);
                                System.out.println("response is :"+response.toString(4));
                                Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                                //intent.putExtra("username", username);
                                LoginActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed"+success)
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                //LoginRequest loginRequest =
                new LoginRequest(name, password, responseListener);
                //RequestQueue queue =
                Volley.newRequestQueue(LoginActivity.this);
                //queue.add(loginRequest);
            }
        });
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (lRequestQueue == null) {
            lRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return lRequestQueue;
    }

    public static synchronized LoginActivity getInstance() {
        return lInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (lRequestQueue != null) {
            lRequestQueue.cancelAll(tag);
        }
    }

}
