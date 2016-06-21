package com.example.mantoshkumar.multipleactivityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class DisplayDetailSingleEntryActivity extends Activity {

    private EditText first_name;
    private EditText last_name;
    private EditText email_id;
    private EditText phone_number;
    private EditText home_address;

    // The below Id would be used to exchange the information to next activity.
    private final static String INTERNAL_CUSTOMER_ID = "com.example.mantoshkumar.customer_id";

    // The object that allows me to manipulate the database
    private DataBaseHelper dbTools = new DataBaseHelper(this);


    private Button update_button;
    private String currentSelectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_detail_single_entry);

        // Find all relevent widgets where things neeeds to be updated.
        first_name   = (EditText)findViewById(R.id.first_name);
        last_name    = (EditText)findViewById(R.id.last_name);
        email_id     = (EditText)findViewById(R.id.email_id);
        phone_number = (EditText)findViewById(R.id.phone_number);
        home_address = (EditText)findViewById(R.id.home_address);

        update_button = (Button)findViewById(R.id.update_button);
        setClickListner();

        // Get the intent and find the ID which is selected by the user.
        Intent intent = getIntent();
        currentSelectedId   = intent.getStringExtra(BasicListEntryActivity.INTERNAL_CUSTOMER_ID);
        getCustomerIDAndDisplay(currentSelectedId);
    }

    private void setClickListner()
    {
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpdateButtononClick(v);
            }
        });

    }


    private  void setUpdateButtononClick(View v)
    {
        // In this case, we need to update database/table with new values.
        HashMap<String, String> insertvalues = new HashMap<String, String>();

        insertvalues.put("first_name",    first_name.getText().toString());
        insertvalues.put("last_name",     last_name.getText().toString());
        insertvalues.put("email_id",      email_id.getText().toString());
        insertvalues.put("phone_number",  phone_number.getText().toString());
        insertvalues.put("home_address",  home_address.getText().toString());

        dbTools.updateTableRow(currentSelectedId, insertvalues);
    }

    private void getCustomerIDAndDisplay(String index)
    {
        HashMap<String, String> getOneEntry = dbTools.getContactInfo(index);

        // Update all widgets based on above values
        first_name.setText(getOneEntry.get("first_name").toString());
        last_name.setText(getOneEntry.get("last_name").toString());
        email_id.setText(getOneEntry.get("email_id").toString());
        phone_number.setText(getOneEntry.get("phone_number").toString());
        home_address.setText(getOneEntry.get("home_address").toString());
    }

}
