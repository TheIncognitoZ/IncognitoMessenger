package com.the_incognito.darry.incognitochatmessengertest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

//import static com.the_incognito.darry.incognitochatmessengertest.LoginActivity.token;
import static com.the_incognito.darry.incognitochatmessengertest.LoginActivity.username;
import static com.the_incognito.darry.incognitochatmessengertest.R.id.chat_view;

public class ChatActivity extends AppCompatActivity {
    private static ChatActivity chatSInstance;
    private static ChatActivity chatRInstance;
    public static final String TAG = "VolleyPatterns";
    private RequestQueue chatRequestQueue;
    private String bud;//receiver name
    EditText inputMsg;
    Button sendButton;
    String rcvdMsg;
    private ChatUser buddy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatSInstance = this;
        chatRInstance = this;
        bud = getIntent().getStringExtra("username");
        final String token = getIntent().getStringExtra("token");
        System.out.println("bud(username) is :" + bud);
        inputMsg = (EditText) findViewById(R.id.input_edit_text);
        final String msg = inputMsg.getText().toString();
        ChatView chatView = (ChatView) findViewById(chat_view);

        chatView.addMessage(new ChatMessage("Message received", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
        chatView.addMessage(new ChatMessage("Message received", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            //final String msg = inputMsg.getText().toString();
            //final String bud = etPassword.getText().toString();

            // Response received from the server
            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //JSONObject jsonResponse = new JSONObject(response);
                        VolleyLog.v("Response:%n %s", response.toString(4));
                        //boolean success = response.getBoolean("success");
                        System.out.println("Chat Activity post response is :"+response.toString(4));
                        final String msgReceived = response.getString("content");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            };


            //Volley.newRequestQueue(ChatActivity.this);//}
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                String typedMsg = inputMsg.getText().toString();
                PostMsg pmsg = new PostMsg(typedMsg, bud,username,token, responseListener);//sending content, receiver, author to POST
                Volley.newRequestQueue(ChatActivity.this);


                return true;
            }

        });
        Response.Listener<JSONArray> msgReceived = new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                Log.d("TAG", response.toString());
                try {
                    ChatView chatView = (ChatView) findViewById(chat_view);
                    for(int i = 0; i < response.length(); i++) {
                        JSONObject jsonResponse = response.getJSONObject(i);
                        String content = jsonResponse.getString("content");
                        System.out.println("Chat Activity get response is :"+response.toString(4));
                        Log.d("content", content);
                        VolleyLog.v("Response:%n %s", response.toString(4));
                        chatView.addMessage(new ChatMessage(content, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
       // GetMsg gmsg = new GetMsg(username,token, msgReceived);//sending author to GET
       // Volley.newRequestQueue(ChatActivity.this);
        final String URL = "https://api.incognitomessenger.me/incongnitomessenger/webapi/messages/"+username;
        final JsonArrayRequest req = new
                JsonArrayRequest(URL, //jsonObject,
                    new com.android.volley.Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("TAG", response.toString());
                            try {
                                ChatView chatView = (ChatView) findViewById(chat_view);
                                for(int i = 0; i < response.length(); i++) {
                                    JSONObject jsonResponse = response.getJSONObject(i);
                                    String content = jsonResponse.getString("content");
                                    System.out.println("Chat Activity get response is :"+response.toString(4));
                                    Log.d("content", content);
                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                    chatView.addMessage(new ChatMessage(content, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("TAG", "Error: " + error.getMessage());
                        //pDialog.dismiss();

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
        ChatActivity.getChatRInstance().addToRequestQueue(req);

        chatView.setTypingListener(new ChatView.TypingListener() {
            @Override
            public void userStartedTyping() {

            }

            @Override
            public void userStoppedTyping() {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
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
        if (chatRequestQueue == null) {
            chatRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return chatRequestQueue;
    }

    public static synchronized ChatActivity getChatSInstance() {
        return chatSInstance;
    }
    public static synchronized ChatActivity getChatRInstance() {
        return chatRInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (chatRequestQueue != null) {
            chatRequestQueue.cancelAll(tag);
        }
    }
    private boolean validate() {
        if (inputMsg.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */



}
