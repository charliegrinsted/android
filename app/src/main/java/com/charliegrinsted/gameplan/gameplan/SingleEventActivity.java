package com.charliegrinsted.gameplan.gameplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

    TextView loadingText;
    String thisEventID;
    JSONObject jsonResponse;
    ProgressBar loadingSpinner;
    View loadingParts;

    TextView singleEventTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event);

        Intent intent = getIntent();

        loadingParts = findViewById(R.id.loadingParts);
        loadingSpinner = (ProgressBar) findViewById(R.id.loadingSpinner);
        loadingText = (TextView) findViewById(R.id.loadingText);
        thisEventID = intent.getStringExtra("eventID");

        singleEventTitle = (TextView) findViewById(R.id.singleEventTitle);

        new FindSingleEventTask().execute();


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
                HttpGet httpget = new HttpGet("http://planaga.me/api/events/" + thisEventID);

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
                    JSONObject single_event = jsonResponse.getJSONObject("thisEvent"); // pull out the event object
                    Events event = new Events();
                    event.setEventTitle(single_event.getString("eventTitle"));
                    event.setEventInfo(single_event.getString("eventInfo"));
                    event.setEventID(single_event.getString("id"));

                    // work from here
                    //JSONArray items = jsonResponse.getJSONArray("results");
                    //returnedEvents = new ArrayList<>();
                    //singleEventID.setText(jsonResponse.toString());

                    populateSingleEvent(event);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void populateSingleEvent(Events event){


        // Hide loading spinner and text
        loadingParts.setVisibility(View.INVISIBLE);

        // Put logic here to display time (is it past the event start date? Shouldn't be, but check
        singleEventTitle.setText(event.getEventTitle());

        // Also whether the current user is already RSVP
        // if Yes, say "Going" and display "Cancel" button
        // else display the "Join" button
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
