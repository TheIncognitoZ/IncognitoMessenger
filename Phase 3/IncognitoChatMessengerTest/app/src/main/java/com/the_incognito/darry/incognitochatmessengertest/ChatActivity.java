package com.the_incognito.darry.incognitochatmessengertest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

import static com.the_incognito.darry.incognitochatmessengertest.LoginActivity.username;
import static com.the_incognito.darry.incognitochatmessengertest.R.id.chat_view;

public class ChatActivity extends AppCompatActivity {
    private static ChatActivity chatInstance;
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
        chatInstance = this;
        bud = getIntent().getStringExtra("username");
        System.out.println("bud(username) is :" + bud);
        inputMsg = (EditText) findViewById(R.id.input_edit_text);
        final String msg = inputMsg.getText().toString();
        ChatView chatView = (ChatView) findViewById(chat_view);

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
                        boolean success = response.getBoolean("success");
                        System.out.println("response is :"+response.toString(4));
                        if (success) {
                            /*username = response.getString("username");
                            token = response.getString("token");
                            Intent intent = new Intent(LoginActivity.this, ConvoActivity.class);
                            intent.putExtra("username", username);
                            LoginActivity.this.startActivity(intent);*/
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                            builder.setMessage("Send Msg Failed")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            /*if(inputMsg.getText().toString().trim().equals("")){
                Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();}
            else{*/


                //Volley.newRequestQueue(ChatActivity.this);//}
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                String typedMsg = inputMsg.getText().toString();
                PostMsg pmsg = new PostMsg(typedMsg, bud,username, responseListener);
                return true;
            }
        });
        chatView.setTypingListener(new ChatView.TypingListener() {
            @Override
            public void userStartedTyping() {

            }

            @Override
            public void userStoppedTyping() {

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
        if (chatRequestQueue == null) {
            chatRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return chatRequestQueue;
    }

    public static synchronized ChatActivity getInstance() {
        return chatInstance;
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


}
