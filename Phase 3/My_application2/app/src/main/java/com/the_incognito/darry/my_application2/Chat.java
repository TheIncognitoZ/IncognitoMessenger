package com.the_incognito.darry.my_application2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class Chat extends CustomActivity {
    /**
     * The Conversation list.
     */
    private ArrayList<Conversation> convList;

    /**
     * The chat adapter.
     */
    private ChatAdapter adp;

    /**
     * The Editext to compose the message.
     */
    private EditText txt;

    /**
     * The user name of buddy.
     */
    private ChatUser buddy;

    /**
     * The date of last message in conversation.
     */
    private Date lastMsgDate;

    private static Chat cInstance;

    private RequestQueue cRequestQueue;
    public static final String TAG = "VolleyPatterns";

    public static synchronized Chat getInstance() {
        return cInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        cInstance = this;

        convList = new ArrayList<Conversation>();
        ListView list = (ListView) findViewById(R.id.list);
        adp = new ChatAdapter();
        list.setAdapter(adp);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        txt = (EditText) findViewById(R.id.txt);
        txt.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        setTouchNClick(R.id.btnSend);

        buddy = (ChatUser) getIntent().getSerializableExtra("username");

        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.setTitle(buddy.getUsername());
    }
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadMsgsList(buddy);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /* (non-Javadoc)
     * @see com.socialshare.custom.CustomFragment#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnSend) {
            sendMessage();
        }

    }
    private void sendMessage() {
        if (txt.length() == 0)
            return;

    }
    /**
     * Load the conversation list from Parse server and save the date of last
     * message that will be used to load only recent new messages
     */
    private void loadMsgsList(ChatUser username) {
        final String URL = "http://192.168.1.21:8080/incongnitomessenger/webapi/messages/"+username;
        JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray> () {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    VolleyLog.v("Response:%n %s", response.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        Chat.getInstance().addToRequestQueue(req);


    }
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);
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
    /**
     * The Class ChatAdapter is the adapter class for Chat ListView. This
     * adapter shows the Sent or Receieved Chat message in each list item.
     */
    private class ChatAdapter extends BaseAdapter {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            return convList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Conversation getItem(int arg0) {
            return convList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @SuppressLint("InflateParams")
        @Override
        public View getView(int pos, View v, ViewGroup arg2) {
            /*Conversation c = getItem(pos);
            if (c.isSent())
                v = getLayoutInflater().inflate(R.layout.chat_item_sent, null);
            else
                v = getLayoutInflater().inflate(R.layout.chat_item_rcv, null);

            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            lbl.setText(DateUtils.getRelativeDateTimeString(Chat.this, c
                            .getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
                    DateUtils.DAY_IN_MILLIS, 0));

            lbl = (TextView) v.findViewById(R.id.lbl2);
            lbl.setText(c.getMsg());

            lbl = (TextView) v.findViewById(R.id.lbl3);
            if (c.isSent()) {
                if (c.getStatus() == Conversation.STATUS_SENT)
                    lbl.setText(R.string.delivered_text);
                else {
                    if (c.getStatus() == Conversation.STATUS_SENDING)
                        lbl.setText(R.string.sending_text);
                    else {
                        lbl.setText(R.string.failed_text);
                    }
                }
            } else
                lbl.setText("");
*/
            return v;
        }

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
