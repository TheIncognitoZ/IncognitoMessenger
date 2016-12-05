package com.the_incognito.darry.incognitochatmessengertest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.lambdaworks.crypto.SCryptUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Security;

public class LoginActivity extends Activity {
    private static LoginActivity lInstance;
    public static final String TAG = "VolleyPatterns";
    private RequestQueue lRequestQueue;
    public String token;
    public static String username;
    private byte[]salt = new byte[16];

   static {
       Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lInstance = this;
        setContentView(R.layout.activity_login);

        //Debugging ConvoActivity
        /*Intent intent = new Intent(LoginActivity.this, ConvoActivity.class);
        intent.putExtra("username", "chotadon");
        intent.putExtra("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2xvY2FsaG9zdDo4MDgwL2luY29uZ25pdG9tZXNzZW5nZXIiLCJleHAiOjE0ODY4MTIyNTAsInVzciI6ImNob3RhZG9uIiwiaWF0IjoxNDgwODEyMjUwfQ.uSeyS4DIDLS5b4ufcIv4a47a2CsNM00cCf9pCdy3TNk");
        System.out.println("token passed to Convos is :"+ "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0ODA3MjI2NDIsImlzcyI6Imh0dHBzOi8vbG9jYWxob3N0OjgwODAvaW5jb25nbml0b21lc3NlbmdlciIsInVzciI6Ik5lZHUiLCJpYXQiOjE0ODA3MTY2NDJ9.mOaF3G54MJZok5ZbQ7MexPDA9UnadEjL7yDb_-Xk2KA");
        LoginActivity.this.startActivity(intent);*/
        //darryl's token eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0ODA3NTUzNTcsImlzcyI6Imh0dHBzOi8vbG9jYWxob3N0OjgwODAvaW5jb25nbml0b21lc3NlbmdlciIsInVzciI6ImRhcnJ5bCIsImlhdCI6MTQ4MDc0OTM1N30.9K_MuKRylX38z83xmLDmv0wM0BVoKDhWhTMseWkA3NM
        //nedu's token
        //nedu@gmail.com  smiteshsawantz@ola.com

        //Debugging ChatActivity
        /*Intent intent = new Intent(LoginActivity.this, ConvoActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("token",token);
        System.out.println("token passed to Convos is :"+ token);
        LoginActivity.this.startActivity(intent);*/

        salt = getIntent().getByteArrayExtra("salt");
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
                //byte[] passwordBytes = password.getBytes();
                String scrypt = "";
                String saltString = "";
                FileInputStream fis = null;
                if(salt==null) {
                try {
                    fis = openFileInput("My_file.txt");
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    StringBuffer b = new StringBuffer();
                    while(bis.available()!=0) {
                        char c = (char) bis.read();
                        b.append(c);
                    }
                    bis.close();
                    fis.close();
                    saltString = b.toString();
                    salt = saltString.getBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }}

                if(salt!=null) {
                    scrypt = SCryptUtil.scrypt(salt, password, 16384, 8, 1);
                    System.out.println("scrypt is :" + scrypt);
                }
                else{System.out.println("No salt found!");}
                //String hashed = "$s0$e0801$RtQneCUvZYdYC8ai3y0ivg==$mmrh5OX8krvPHUZls1b52mxgITnyoRJUS4gdG/IZp0E=";
                //boolean value = SCryptUtil.check(scrypt, hashed);
                //System.out.println("value is :"+value);

                // Response received from the server
                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //JSONObject jsonResponse = new JSONObject(response);
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            boolean success = response.getBoolean("success");
                            System.out.println("Login Activity response is :"+response.toString(4));
                            if (success) {
                                username = response.getString("username");
                                token = response.getString("token");
                                Intent intent = new Intent(LoginActivity.this, ConvoActivity.class);
                                intent.putExtra("username", username);
                                intent.putExtra("token",token);
                                System.out.println("token passed to Convos is :"+ token);
                                LoginActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                if(etName.getText().toString().trim().equals("") && etPassword.getText().toString().trim().equals("")){
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();}else{
                    System.out.println("Salt passed to LoginRequest is :"+salt);
                    new LoginRequest(name, scrypt, responseListener);
                    Volley.newRequestQueue(LoginActivity.this);}
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

