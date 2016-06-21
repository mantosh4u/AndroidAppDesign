package com.example.mantoshkumar.multipleactivityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;



public class BasicListEntryActivity extends Activity {

    private TableLayout main_table;
    // Need to create & maintain list of rows which would be used later to embbed various widgets
    private ArrayList<TableRow>  table_rows_list = new ArrayList<TableRow>();
    // Create two textview entry per row which would be added in main_table
    private TextView contact_id;
    private TextView first_name;
    private TextView email_id;

    // The object that allows me to manipulate the database
    private DataBaseHelper dbTools = new DataBaseHelper(this);
    ArrayList<HashMap<String, String>> getValues = null;

    // The below Id would be used to exchange the information to next activity.
    public final static String INTERNAL_CUSTOMER_ID = "com.example.mantoshkumar.customer_id";

    // We need to add the logging to understand the lifecycle of activity. This is required as
    // resume the activity requires to update the view so that any updated information by user.

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_list_entry);

        // Get the id of main container widget so that we can perfrom later activity
        main_table = (TableLayout)findViewById(R.id.container_widget);
        // Get the width of header widgets so that it can be applied to all created rows
        TextView header_id = (TextView)findViewById(R.id.contact_id);
        TextView header_first_name = (TextView)findViewById(R.id.first_name);
        TextView header_email_id = (TextView)findViewById(R.id.email_id);

        //Find All Contacts From Database and display one by one in list format
        getValues = dbTools.getAllContactInfo();

        // Iterate through all entry and create that many row
        // which entern would add first_name & email_id
        for(int index = 0; index < getValues.size(); ++index)
        {
            HashMap<String, String> getOneEntry = getValues.get(index);

            // Create one table row per entry
            TableRow table_row = new TableRow(this);
            main_table.addView(table_row);


            // Create contact_id and set its value from db fetched information
            contact_id = new TextView(this);
            contact_id.setLayoutParams(header_id.getLayoutParams());
            contact_id.setText(getOneEntry.get("contact_id"));
            table_row.addView(contact_id);

            // Create first_name and set its value from db fetched information
            first_name = new TextView(this);
            first_name.setLayoutParams(header_first_name.getLayoutParams());
            first_name.setText(getOneEntry.get("first_name"));
            table_row.addView(first_name);

            // Create email_od and set its value from db fetched information
            email_id   = new TextView(this);
            email_id.setLayoutParams(header_email_id.getLayoutParams());
            email_id.setText(getOneEntry.get("email_id"));
            table_row.addView(email_id);

            // Now add into our container list data structure as it would be
            // required in later uses.
            table_rows_list.add(table_row);
        }

        setClickListner();

    }
    @Override
    public void onResume() {
        super.onResume();
        // call onCreate method so that this activity can create/update its view.
        this.onCreate(null);
    }

    private void setClickListner()
    {
        // User would click on any row and application should show the details of that entry
        for(int index = 0; index < table_rows_list.size(); ++index)
        {
            table_rows_list.get(index).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAddButtononClick(v);
                }
            });
        }
    }

    private void setAddButtononClick(View v)
    {
        try
        {
            //Get the number of columns from the given row widgets
            TableRow clickedRow = (TableRow) v;
            int IdViewIndexInRow = 0;

            View clickedIdView = clickedRow.getChildAt(0);

            TextView contactId = (TextView) clickedIdView;
            String Id = contactId.getText().toString();

            //Setup the another activity, which would display detailed view of selected entry
            Intent intent = new Intent(this, DisplayDetailSingleEntryActivity.class);
            intent.putExtra(INTERNAL_CUSTOMER_ID, Id);
            startActivity(intent);
        }
        catch (Exception ec)
        {
            // Do Nothing as of now
        }
    }
}
