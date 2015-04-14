package com.charliegrinsted.gameplan.gameplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.charliegrinsted.gameplan.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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

public class FindEventsFragment extends Fragment {

    TextView findEventsStatus;
    Button findEventsButton;
    JSONObject jsonResponse;
    SeekBar searchRadius;
    ListView listView;
    ArrayList<Events> returnedEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.find_events, container, false);
        findEventsStatus = (TextView) ll.findViewById(R.id.findEventsStatus);
        searchRadius = (SeekBar) ll.findViewById(R.id.searchRadius);
        findEventsButton = (Button) ll.findViewById(R.id.findEventsBtn);
        listView = (ListView) ll.findViewById(R.id.resultsList);

        findEventsStatus.setText("Search radius: " + Integer.toString(searchRadius.getProgress()) + "km");

        searchRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

             @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                 findEventsStatus.setText("Search radius: " + Integer.toString(progress) + "km");
             }

             @Override public void onStartTrackingTouch(SeekBar seekBar){
             }
             @Override public void onStopTrackingTouch(SeekBar seekBar){
             }
        });

        findEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FindNearbyTask().execute(); // execute nearby events search
                findEventsStatus.setText("Searching...");
            }
        });


        return ll;
    }

    private class FindNearbyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            InputStream inputStream;
            String result;

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://planaga.me/api/search/events/nearby/");

                String searchDistance = Integer.toString(searchRadius.getProgress()); // take the slider value for the search distance

                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                String longitude = String.valueOf(location.getLongitude()); // get the longitude as a string
                String latitude = String.valueOf(location.getLatitude()); // get the latitude as a string
                Log.e("Longitude is:", longitude);
                Log.e("Latitude is:", latitude);


                List<NameValuePair> locationDetails = new ArrayList<>();
                // need to get the actual phone location and pass in instead of these dummy parameters

                locationDetails.add(new BasicNameValuePair("lat", latitude)); // add the latitude as a POST parameter
                locationDetails.add(new BasicNameValuePair("lng", longitude)); // add the longitude as a POST parameter
                locationDetails.add(new BasicNameValuePair("distance", searchDistance)); // add the longitude as a POST parameter

                httppost.setEntity(new UrlEncodedFormEntity(locationDetails)); // add parameters to request

                final SharedPreferences storedSharedPreferences = getActivity().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE); // create a SharedPreferences instance

                String auth_token = storedSharedPreferences.getString("AuthToken", "Not Found"); // extract JSONWebToken from SharedPreferences
                httppost.setHeader("token", auth_token); // add JSONWebToken as a header on the HTTP request

                Log.d("Header set to:", auth_token);
                Log.d("Search radius is:", searchDistance);

                HttpResponse response = httpclient.execute(httppost); // Execute HTTP Request

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
                    JSONArray items = jsonResponse.getJSONArray("results");
                    returnedEvents = new ArrayList<>();

                    if (items.length() > 0){
                        for (int i = 0; i < items.length(); i++){

                            Events event = new Events();

                            JSONObject single_event = items.getJSONObject(i);
                            JSONObject single_event_details = single_event.getJSONObject("obj");
                            event.setEventDistance(single_event.getString("dis"));
                            event.setEventTitle(single_event_details.getString("eventTitle"));
                            event.setEventInfo(single_event_details.getString("eventInfo"));
                            event.setEventID(single_event_details.getString("_id"));

                            returnedEvents.add(i, event);

                        }
                        findEventsStatus.setText("");
                        populateListView();

                    } else {
                        findEventsStatus.setText("No events found");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void populateListView() {

        CustomAdapter adapter = new CustomAdapter(getActivity(), returnedEvents);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Events thisEvent = returnedEvents.get(position);
                String thisEventID = thisEvent.getEventID();

                Intent intent = new Intent(parent.getContext(), SingleEventActivity.class);
                intent.putExtra("eventID", thisEventID);
                startActivity(intent);

            }
        });

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

