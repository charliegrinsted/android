package com.charliegrinsted.gameplan.gameplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.charliegrinsted.gameplan.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends ActionBarActivity {

    private SharedPreferences storedSharedPreferences;
    private String storedUsername;
    private String storedPassword;

    EditText loginUsername;
    EditText loginPassword;
    TextView loginErrorMessage;
    Button loginBtn;
    JSONObject jsonResponse;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginErrorMessage = (TextView) findViewById(R.id.loginErrorMessage);

        storedSharedPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storedUsername = loginUsername.getText().toString();
                storedPassword = loginPassword.getText().toString();

                if (storedUsername.equals("") || storedPassword.equals("")){
                    loginErrorMessage.setText("Please complete all fields"); // display an error if the fields haven't been filled in
                } else {
                    loginErrorMessage.setText("Off we go...");
                    new LoginTask().execute(); // execute login task
                }
            }
        });

    }

    private class LoginTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            InputStream inputStream;
            String result;

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://10.0.1.8:1337/api/session/create/");

                List<NameValuePair> loginDetails = new ArrayList<>(2);
                loginDetails.add(new BasicNameValuePair("userName", storedUsername)); // add the username as a POST parameter
                loginDetails.add(new BasicNameValuePair("password", storedPassword)); // add the password as a POST parameter

                httppost.setEntity(new UrlEncodedFormEntity(loginDetails)); // add parameters to request

                HttpResponse response = httpclient.execute(httppost); // Execute HTTP Post Request

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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String auth_token = null;
                String firstName = null;
                String lastName = null;
                String userName = null;

                try {
                    auth_token = jsonResponse.getString("token");
                    firstName = jsonResponse.getString("firstName");
                    lastName = jsonResponse.getString("lastName");
                    userName = jsonResponse.getString("userName");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = storedSharedPreferences.edit();
                editor.putString("AuthToken", auth_token);
                editor.putString("UserName", userName);
                editor.putString("FirstName", firstName);
                editor.putString("LastName", lastName);
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);

            } else {
                loginErrorMessage.setText("Unable to connect at this time. Please try again later.");
            }
        }
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
