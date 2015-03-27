package com.charliegrinsted.gameplan.gameplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.charliegrinsted.gameplan.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SingleEventActivity extends Activity {

    TextView singleEventID;
    String thisEventID;
    JSONObject jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event);

        Intent intent = getIntent();

        thisEventID = intent.getStringExtra("eventID");

        new FindSingleEventTask().execute();

        singleEventID = (TextView) findViewById(R.id.single_event_id);
        singleEventID.setText(thisEventID);

    }

    private class FindSingleEventTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            InputStream inputStream;
            String result;

            try {

                final SharedPreferences storedSharedPreferences = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE); // create a SharedPreferences instance
                String auth_token = storedSharedPreferences.getString("AuthToken", "Not Found"); // extract JSONWebToken from SharedPreferences

                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://10.0.1.8:1337/api/events/" + thisEventID);

                httpget.setHeader("token", auth_token); // add JSONWebToken as a header on the HTTP request

                HttpResponse response = httpclient.execute(httpget); // Execute HTTP Request

                inputStream = response.getEntity().getContent();
                result = convertInputStreamToString(inputStream);

                return result;

            } catch (Exception e) {

                e.printStackTrace();
                return null;

            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {

                try {

                    jsonResponse = new JSONObject(result);

                    // work from here
                    //JSONArray items = jsonResponse.getJSONArray("results");
                    //returnedEvents = new ArrayList<>();
                    singleEventID.setText(jsonResponse.toString());

                    populateSingleEvent();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void populateSingleEvent(){

        Log.e("Event Fired:", "YES");

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}
