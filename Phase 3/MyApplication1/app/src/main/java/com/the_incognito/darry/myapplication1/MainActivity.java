package com.the_incognito.darry.myapplication1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

//import com.yoursite.helloworld.R;

public class MainActivity extends Activity implements OnClickListener {

    TextView tvIsConnected,receivedMsg;
    EditText etName,etContent,etReceiver;
    Button btnPost,btnGet,btnReg;

    Person person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get reference to the views
        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
        receivedMsg = (TextView) findViewById(R.id.received);
        etName = (EditText) findViewById(R.id.etName);
        etContent = (EditText) findViewById(R.id.etContent);
        etReceiver = (EditText) findViewById(R.id.etReceiver);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnReg = (Button) findViewById(R.id.btnReg);

        // check if you are connected or not
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are conncted");
        }
        else{
            tvIsConnected.setText("You are NOT conncted");
        }

        // add click listener to Button "POST"
        btnPost.setOnClickListener(this);
        btnGet.setOnClickListener(this);
        btnReg.setOnClickListener(this);

    }

    public static String POST(String url, Person person){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("receiver", person.getReceiver());
            jsonObject.accumulate("author", person.getAuthor());
            jsonObject.accumulate("content", person.getContent());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib 
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person); 

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content   
            //httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }
    public static String GET(String url, Person person){
        InputStream inputStream = null;
        String msg = "";
        List<String> list = new ArrayList<String>();
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make GET request to the given URL
            HttpGet httpGet = new HttpGet(url);

            String json = "";

            // 3. build jsonObject
            /*JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", person.getName());
            jsonObject.accumulate("country", person.getCountry());
            jsonObject.accumulate("twitter", person.getTwitter());
*/
            // 4. convert JSONObject to JSON to String
            /*json = jsonObject.toString();
*/
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            //StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            //httpGet.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");

            // 8. Execute Get request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
            JSONTokener tokener = new JSONTokener(builder.toString());
            JSONArray finalResult = new JSONArray(tokener);
            /*for(int i = 0; i < finalResult.length(); i++){
                list.add(finalResult.getJSONObject(i).getString("content"));
            }*/
            msg = finalResult.getJSONObject(3).getString("content");

            // 9. receive response as inputStream
            //inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            /*if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
*/
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return message
        return msg;
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btnPost:
                if(!validate())
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                // call AsynTask to perform network operation on separate thread
                new HttpAsyncTask().execute("http://192.168.1.21:8080/incongnitomessenger/webapi/messages");//http://localhost:8080/IncognitoMessenger/webapi/messages
                //http://requestb.in/14vbdrs1
                //receivedMsg.setText();
                break;

            case R.id.btnGet:
                new HttpAsyncTaskGet().execute("http://192.168.1.21:8080/incongnitomessenger/webapi/messages/darryl");
                break;

            case R.id.btnReg:
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                break;
        }

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            person = new Person();
            person.setAuthor(etName.getText().toString());
            person.setContent(etContent.getText().toString());
            person.setReceiver(etReceiver.getText().toString());

            return POST(urls[0],person);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }
    private class HttpAsyncTaskGet extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            person = new Person();
            person.setAuthor(etName.getText().toString());
            person.setContent(etContent.getText().toString());
            person.setReceiver(etReceiver.getText().toString());

            return GET(urls[0],person);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String msg) {
            receivedMsg.setText(msg);
            Toast.makeText(getBaseContext(), "Data Received!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validate(){
        if(etName.getText().toString().trim().equals(""))
            return false;
        else if(etContent.getText().toString().trim().equals(""))
            return false;
        else if(etReceiver.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}