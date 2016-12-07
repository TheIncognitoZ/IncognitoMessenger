package com.the_incognito.darry.incognitochatmessengertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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
import org.spongycastle.jce.interfaces.ECPublicKey;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.util.encoders.Base64;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

import static com.the_incognito.darry.incognitochatmessengertest.BouncyCastleIM.hmac;
import static com.the_incognito.darry.incognitochatmessengertest.R.id.chat_view;

public class ChatActivity extends AppCompatActivity {
    private static ChatActivity chatSInstance;
    private static ChatActivity chatRInstance;
    public static final String TAG = "VolleyPatterns";
    private RequestQueue chatRequestQueue;
    private String bud;//receiver name
    EditText inputMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatSInstance = this;
        chatRInstance = this;
        bud = getIntent().getStringExtra("username");
        final String token = getIntent().getStringExtra("token");
        final String secretKey = getIntent().getStringExtra("secretKey");
        final String username = getIntent().getStringExtra("author");
        System.out.println("receiver name is :" + bud);
        System.out.println("secretKey is :" + secretKey);
        inputMsg = (EditText) findViewById(R.id.input_edit_text);
        final String msg = inputMsg.getText().toString();
        ChatView chatView = (ChatView) findViewById(chat_view);
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            // Response received from the server
            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        VolleyLog.v("Response:%n %s", response.toString(4));
                        System.out.println("Chat Activity post response is :"+response.toString(4));
                        final String msgReceived = response.getString("content");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            };
            //Volley.newRequestQueue(ChatActivity.this);//}
            @Override
            public boolean sendMessage(ChatMessage chatMessage) throws Exception {
                if(!secretKey.equals(null)||(secretKey.length()>0))
                {
                    String typedMsg = inputMsg.getText().toString();
                    final BouncyCastleIM enc = new BouncyCastleIM();
                    String encodedMsg = enc.encryption(secretKey, typedMsg);
                    String genSigInput =hmac(username+encodedMsg);
                    KeyPair g=enc.generateKeys();
                    PrivateKey priv = g.getPrivate();
                    final BouncyCastleIM getSig = new BouncyCastleIM();
                    String signature = getSig.genSign(priv,genSigInput);
                    java.security.PublicKey pub = (ECPublicKey) g.getPublic();
                    System.out.println("PublicKey pub = "+pub);
                    byte[] publicKeyBytes = pub.getEncoded();
                    String pubKeyStr = Base64.toBase64String(publicKeyBytes);
                    System.out.println("Public key pubKeyStr is :"+pubKeyStr);
                    System.out.println("username in ChatActivity is :"+username);
                    PostMsg pmsg = new PostMsg(encodedMsg, bud,username,token,signature,pubKeyStr, responseListener);//sending content, receiver, author to POST
                    Volley.newRequestQueue(ChatActivity.this);
                }
                return true;
            }
        });
        final String URL = "http://192.168.1.16:8080/incongnitomessenger/webapi/messages/"+username;
        //final String URL = "https://api.incognitomessenger.me/incongnitomessenger/webapi/messages/"+username;
        final JsonArrayRequest req = new
            JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    Log.d("RESPONSE IS BLANK", response.toString());
                    try {
                        ChatView chatView = (ChatView) findViewById(chat_view);
                        for(int i = 0; i < response.length(); i++) {
                            JSONObject jsonResponse = response.getJSONObject(i);
                            String author = jsonResponse.getString("author");
                            String content = jsonResponse.getString("content");
                            String receiver = jsonResponse.getString("receiver");
                            String signature = jsonResponse.getString("sign");
                            String pubkeyString = jsonResponse.getString("publickey");
                            byte[] publicKeyBytes = Base64.decode(pubkeyString);
                            KeyFactory factory = KeyFactory.getInstance("ECDSA", new BouncyCastleProvider());
                            java.security.PublicKey ecPublicKey = (ECPublicKey) factory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
                            System.out.println("Chat Activity get response is :"+response.toString(4));
                            Log.d("content", content);
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            String msgMAC = hmac(author+content);
                            BouncyCastleIM dec = new BouncyCastleIM();
                            String decodedMsg = dec.decryption(secretKey, content);
                            if(BouncyCastleIM.vrfySig(signature,ecPublicKey,msgMAC)&& (!decodedMsg.equals("Purposefull long Invalid String so that this message is dropped and will never reach the intended recipient. Kenny Dalglish is King."))){
                            chatView.addMessage(new ChatMessage(decodedMsg, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
                            }else{
                                Toast.makeText(getBaseContext(), "Invalid message found and dropped!", Toast.LENGTH_LONG).show();
                                System.out.println("Invalid message found and dropped ---------------------------------------------------------------");}
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Send Message Response is some kinda error!", "Error: " + error.getMessage());
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
        ;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
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
}
