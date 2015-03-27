package com.charliegrinsted.gameplan.gameplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.charliegrinsted.gameplan.R;

import java.util.ArrayList;

/**
 * Created by Charlie on 04/03/15.
 */
class CustomAdapter extends ArrayAdapter<Events> {

    Context context;
    ArrayList<Events> itemsArray;

    // constructor jiggery
    CustomAdapter(Context context, ArrayList<Events> returnedEvents){
        super(context, R.layout.list_item, R.id.list_item_Title, returnedEvents);
        this.context = context;
        this.itemsArray = returnedEvents;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        // inflate the XML view to a Java object
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View single_row = inflater.inflate(R.layout.list_item, parent, false);
        TextView row_title = (TextView) single_row.findViewById(R.id.list_item_Title);
        TextView row_description = (TextView) single_row.findViewById(R.id.list_item_Description);
        TextView row_distance = (TextView) single_row.findViewById(R.id.list_item_Distance);

        Events thisEvent = itemsArray.get(position);

        row_title.setText(thisEvent.getEventTitle());
        row_description.setText(thisEvent.getEventInfo());
        row_distance.setText(thisEvent.getEventDistance());

        return single_row;

    }
}
