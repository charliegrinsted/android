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
import android.widget.Button;
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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SingleEventActivity extends Activity {

    TextView loadingText;
    String thisEventID;
    JSONObject jsonResponse;
    ProgressBar loadingSpinner;
    View loadingParts;

    TextView singleEventTitle;
    TextView singleEventDescription;
    TextView singleEventStartTime;
    Button openMap;

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
        singleEventDescription = (TextView) findViewById(R.id.singleEventDescription);
        singleEventStartTime = (TextView) findViewById(R.id.singleEventStartTime);
        openMap = (Button) findViewById(R.id.mapBtn);

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

                    // StartTime wrangling

                    String tempStartTime = single_event.getString("startTime"); // get the JSON string
                    DateTime formattedStartTime = ISODateTimeFormat.dateTimeParser().parseDateTime(tempStartTime); // make it a Date object

                    DateTimeFormatter outputFormat = DateTimeFormat.forPattern("d MMMM yyyy - HH:mm"); // set the pattern for formatting
                    String outputStartTime = formattedStartTime.toString(outputFormat); // format Date using the pattern, make a String

                    event.setStartTime(outputStartTime); // set the string in the Event object

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

        singleEventTitle.setText(event.getEventTitle());
        singleEventDescription.setText(event.getEventInfo());

        singleEventStartTime.setText(event.getStartTime());

        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleEventDescription.setText("lol");
            }
        });

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
