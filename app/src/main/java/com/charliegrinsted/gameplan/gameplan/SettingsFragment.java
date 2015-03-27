package com.charliegrinsted.gameplan.gameplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.charliegrinsted.gameplan.R;

public class SettingsFragment extends Fragment {

    TextView ActiveUser;
    Button LogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        final SharedPreferences storedSharedPreferences = getActivity().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);

        LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.settings, container, false);
        ActiveUser = (TextView) ll.findViewById(R.id.activeUser);
        LogOut = (Button) ll.findViewById(R.id.logout);

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            SharedPreferences.Editor editor = storedSharedPreferences.edit(); // make an editor instance
            editor.clear(); // clear the stored user details
            editor.apply(); // save changes to SharedPreferences

            Intent intent = new Intent(getActivity(), MainActivity.class); // create intent to send user back to splash screen
            startActivity(intent); // launch the intent

            }
        });

        String firstName = storedSharedPreferences.getString("FirstName", "Not Found");
        String lastName = storedSharedPreferences.getString("LastName", "Not Found");

        ActiveUser.setText(firstName + " " + lastName);

        return ll;

    }

}
