package com.charliegrinsted.gameplan.gameplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.charliegrinsted.gameplan.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyEventsFragment extends Fragment {

    JSONObject jsonResponse;
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Events> returnedEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.my_events, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) ll.findViewById(R.id.my_events_swipe_refresh_layout);
        listView = (ListView) ll.findViewById(R.id.my_events_list);
        new ReloadMyEventsTask().execute();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                new ReloadMyEventsTask().execute();
            }
        });

        return ll;

    }

    private class ReloadMyEventsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            InputStream inputStream;
            String result;

            try {

                final SharedPreferences storedSharedPreferences = getActivity().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE); // create a SharedPreferences instance

                String userName = storedSharedPreferences.getString("UserName", "Not Found"); // extract JSONWebToken from SharedPreferences
                String auth_token = storedSharedPreferences.getString("AuthToken", "Not Found"); // extract JSONWebToken from SharedPreferences

                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://planaga.me/api/users/" + userName);

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
                    JSONObject user = jsonResponse.getJSONObject("user"); // pull out the user object
                    JSONArray items = user.getJSONArray("eventsAttending"); // pull out array of user's events
                    returnedEvents = new ArrayList<>();

                    if (items.length() > 0){
                        for (int i = 0; i < items.length(); i++){

                            Events event = new Events();

                            JSONObject single_event = items.getJSONObject(i);
                            event.setEventTitle(single_event.getString("eventTitle"));
                            event.setEventInfo(single_event.getString("eventInfo"));
                            event.setEventID(single_event.getString("id"));

                            returnedEvents.add(i, event);

                        }
                        populateListView();

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

        swipeRefreshLayout.setRefreshing(false); // end the swipeRefreshLayout refresh

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
