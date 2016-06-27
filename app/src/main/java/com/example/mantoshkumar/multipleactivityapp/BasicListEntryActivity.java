package com.example.mantoshkumar.multipleactivityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class BasicListEntryActivity extends BaseCustomWithDataBaseSupportActivity {
    private TableLayout mMainTable;
    // Need to create & maintain list of rows which would be used later to embbed various widgets
    private ArrayList<TableRow>  mTableRowsList = new ArrayList<TableRow>();
    // Create two textview entry per row which would be added in mMainTable
    private TextView mContactId;
    private TextView mFirstName;
    private TextView mEmailId;
    ArrayList<HashMap<String, String>> mGetValues = null;
    // The below Id would be used to exchange the information to next activity.
    public static String INTERNAL_CUSTOMER_ID = "@string/shared_customer_id";

    // We need to add the logging to understand the lifecycle of activity. This is required as
    // resume the activity requires to update the view so that any updated information by user.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_list_entry);

        // Get the id of main container widget so that we can perfrom later activity
        mMainTable = (TableLayout)findViewById(R.id.container_widget);
        // Get the width of header widgets so that it can be applied to all created rows
        TextView headerId        = (TextView)findViewById(R.id.contact_id);
        TextView headerFirstName = (TextView)findViewById(R.id.first_name);
        TextView headerEmailId   = (TextView)findViewById(R.id.email_id);

        //Find All Contacts From Database and display one by one in list format
        mGetValues = getDbTools().getAllContactInfo();

        // Iterate through all entry and create that many row
        // which entern would add first_name & email_id
        for(int index = 0; index < mGetValues.size(); ++index)
        {
            HashMap<String, String> getOneEntry = mGetValues.get(index);

            // Create one table row per entry
            TableRow table_row = new TableRow(this);
            mMainTable.addView(table_row);

            // Create contact_id and set its value from db fetched information
            mContactId = new TextView(this);
            mContactId.setLayoutParams(headerId.getLayoutParams());
            mContactId.setText(getOneEntry.get(DataBaseSchema.CONTACT_ID));
            table_row.addView(mContactId);

            // Create first_name and set its value from db fetched information
            mFirstName = new TextView(this);
            mFirstName.setLayoutParams(headerFirstName.getLayoutParams());
            mFirstName.setText(getOneEntry.get(DataBaseSchema.FIRST_NAME));
            table_row.addView(mFirstName);

            // Create email_od and set its value from db fetched information
            mEmailId   = new TextView(this);
            mEmailId.setLayoutParams(headerEmailId.getLayoutParams());
            mEmailId.setText(getOneEntry.get(DataBaseSchema.EMAIL_ID));
            table_row.addView(mEmailId);

            // Now add into our container list data structure as it would be
            // required in later uses.
            mTableRowsList.add(table_row);
        }

        setClickListner();

    }
    @Override
    public void onResume() {
        super.onResume();
        // call onCreate method so that this activity can create/update its view.
        this.onCreate(null);
    }

    private void setClickListner() {
        // User would click on any row and application should show the details of that entry
        for(int index = 0; index < mTableRowsList.size(); ++index)
        {
            mTableRowsList.get(index).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAddButtononClick(v);
                }
            });
        }
    }

    private void setAddButtononClick(View v) {
        try
        {
            //Get the number of columns from the given row widgets
            TableRow clickedRow  = (TableRow)v;
            View clickedIdView   = clickedRow.getChildAt(0);
            TextView contactId   = (TextView) clickedIdView;
            String id = contactId.getText().toString();

            //Setup the another activity, which would display detailed view of selected entry
            Intent intent = new Intent(this, DisplayDetailSingleEntryActivity.class);
            intent.putExtra(INTERNAL_CUSTOMER_ID, id);
            startActivity(intent);
        }
        catch (Exception ec)
        {
            // Do Nothing as of now
        }
    }
}
