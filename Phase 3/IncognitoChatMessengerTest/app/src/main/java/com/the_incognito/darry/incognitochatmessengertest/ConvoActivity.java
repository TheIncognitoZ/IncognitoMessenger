package com.the_incognito.darry.incognitochatmessengertest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ConvoActivity extends Activity {
    // private static final String TAG = "ArrayAdapterListViewActivity";
    EditText editText, etKey;
    Button addButton;
    ConvoArrayAdapter adapter;
    ListView listview;
    ArrayList<String> arrayList;
    Runnable run;
    private static ConvoActivity cInstance;
    public static final String TAG = "VolleyPatterns";
    private RequestQueue cRequestQueue;
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convo);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        cInstance = this;
        final String token = getIntent().getStringExtra("token");
        addButton = (Button) findViewById(R.id.addButton);
        //textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        etKey = (EditText) findViewById(R.id.etKey);
        listview = (ListView) findViewById(R.id.listview);
        final Context context = this;
        final String author = getIntent().getStringExtra("username");

        arrayList = new ArrayList<String>();
        //ChatView chatView = (ChatView) findViewById(R.id.chat_view);
        adapter = new ConvoArrayAdapter(this,
                android.R.layout.simple_list_item_1, arrayList);
        //System.out.println("created adapter "+adapter);
        listview.setAdapter(adapter);

        //final String secretKey = etKey.getText().toString();
        /*editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return addItem();
                }
                return false;
            }
        });*/

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editText.getText().toString();
                // Response received from the server
                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    try {
                        //JSONObject jsonResponse = new JSONObject(response);
                        VolleyLog.v("Response:%n %s", response.toString(4));
                        boolean success = response.getBoolean("success");
                        if (success) {
                            //System.out.println("Your private key is :"+secretKey);
                            String username = response.getString("username");
                            addItem(username);
                            adapter.notifyDataSetChanged();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ConvoActivity.this);
                            builder.setMessage("Email not found")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                };
                if(!validate()){
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                }else{
                    new ConvoRequest(email, responseListener);
                    Volley.newRequestQueue(ConvoActivity.this);

                }

            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                final String secretKey = etKey.getText().toString();
                if(!secretKey.equals(null)&&(secretKey.length()>8)){
                    Intent intent = new Intent(ConvoActivity.this, ChatActivity.class);
                    intent.putExtra("username", item);//username is receiver name eg.nalula
                    intent.putExtra("token",token);
                    intent.putExtra("author",author);
                    intent.putExtra("secretKey",secretKey);
                    System.out.println("token passed to chat is :"+ token);
                    System.out.println("SecretKey passed to chat is :"+ secretKey);
                    ConvoActivity.this.startActivity(intent);
                System.out.println("onitem click edittext secret is :"+secretKey);}
                //view.animate().withEndAction(new Runnable() {
                   //         @Override
                   //         public void run() {


                  //              //etKey.setText("");
                   //         }
                  //      });
                //}
                else{
                  Toast.makeText(getBaseContext(), "Secret Word is not set!", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
    private boolean validate(){
        if(editText.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }
    private boolean addItem(String uname){
        adapter.add(uname);
        arrayList.add(uname);
        editText.setText("");

        adapter.notifyDataSetChanged();
        listview.smoothScrollToPosition(adapter.getCount() - 1);
        return true;
    }
    private boolean addItem(){
        adapter.add(editText.getText().toString());
        arrayList.add(editText.getText().toString());
        editText.setText("");
        adapter.notifyDataSetChanged();
        listview.smoothScrollToPosition(adapter.getCount() - 1);
        return true;
    }
    public static synchronized ConvoActivity getInstance() {
        return cInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (cRequestQueue != null) {
            cRequestQueue.cancelAll(tag);
        }
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
        if (cRequestQueue == null) {
            cRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return cRequestQueue;
    }

}