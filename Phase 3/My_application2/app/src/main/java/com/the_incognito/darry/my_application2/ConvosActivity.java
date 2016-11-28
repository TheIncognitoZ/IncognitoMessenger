package com.the_incognito.darry.my_application2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
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

public class ConvosActivity extends Activity {
   // private static final String TAG = "ArrayAdapterListViewActivity";
    EditText editText;
    Button addButton;
    TextView textView;
    ConvosArrayAdapter adapter;
    ListView listview;
    ArrayList<String> arrayList;
    Runnable run;
    private static ConvosActivity cInstance;
    public static final String TAG = "VolleyPatterns";
    private RequestQueue cRequestQueue;
    public static String token;
    //public String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convos);
        cInstance = this;

        addButton = (Button) findViewById(R.id.addButton);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        listview = (ListView) findViewById(R.id.listview);

        arrayList = new ArrayList<String>();

        adapter = new ConvosArrayAdapter(this,
                android.R.layout.simple_list_item_1, arrayList);
        System.out.println("created adapter "+adapter);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                //String email = editText.toString();
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(ConvosActivity.this, Chat.class).putExtra(Const.EXTRA_DATA,item));
                                /*Intent intent = new Intent(ConvosActivity.this, Chat.class);
                                intent.putExtra("username", item);*/
                            }
                        });
            }

        });

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
                                 String username = response.getString("username");
                                 addItem(username);
                             } else {
                                 AlertDialog.Builder builder = new AlertDialog.Builder(ConvosActivity.this);
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
                    Volley.newRequestQueue(ConvosActivity.this);

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
    public static synchronized ConvosActivity getInstance() {
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

   /* public void populate(){
        Cursor cursor = myDb.getAllRows();
    }*/

}
