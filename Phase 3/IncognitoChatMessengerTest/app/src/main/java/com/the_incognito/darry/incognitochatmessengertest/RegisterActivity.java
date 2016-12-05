package com.the_incognito.darry.incognitochatmessengertest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.lambdaworks.crypto.SCryptUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RegisterActivity extends Activity {
    private static RegisterActivity sInstance;
    public static final String TAG = "VolleyPatterns";
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInstance = this;
        setContentView(R.layout.activity_register);

        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            final String name = etName.getText().toString();
            final String password = etPassword.getText().toString();
            final String email = etEmail.getText().toString();
            final String emailComparison = etEmail.getText().toString().trim();
            final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (emailComparison.matches(emailPattern) && !name.equals(null) && password.length()>=7){
            final byte[] salt = new byte[16];
            try {
                SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
                    File f = getFilesDir();
                    String path = f.getAbsolutePath();
                    System.out.println("File path :"+path+"____________________________________");

                try {
                    FileOutputStream fos = openFileOutput("My_file.txt",MODE_PRIVATE);
                    fos.write(salt);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            String scrypt = SCryptUtil.scrypt(salt,password, 16384, 8, 1);
            //System.out.println("password scrypt is :"+scrypt);
            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        VolleyLog.v("Response:%n %s", response.toString(4));
                        boolean success = response.getBoolean("success");
                        System.out.println("success value in register reponse is :"+success);
                        if (success) {
                            Toast.makeText(getBaseContext(), "Registration Successful!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("salt",salt);
                            System.out.println("salt passed to Login is :"+ salt);
                            RegisterActivity.this.startActivity(intent);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("Registration Failed")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            try {
                new RegisterRequest(name, scrypt, email, responseListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Volley.newRequestQueue(RegisterActivity.this);
            }else{Toast.makeText(getBaseContext(), "Invalid username/password/e-mail!", Toast.LENGTH_LONG).show();}
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
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public static synchronized RegisterActivity getInstance() {
        return sInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}
