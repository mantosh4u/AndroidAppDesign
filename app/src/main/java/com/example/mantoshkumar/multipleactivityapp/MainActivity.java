package com.example.mantoshkumar.multipleactivityapp;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;



public class MainActivity extends Activity {

    private EditText first_name;
    private EditText last_name;
    private EditText email_id;
    private EditText phone_number;
    private EditText home_address;

    private Button  add_button;
    private Button  get_button;
    private Button  delete_button;


    // The object that allows me to manipulate the database
    private DataBaseHelper dbTools = new DataBaseHelper(this);


    // call the insert stuff into database from(get) from various widgets
    private void setAddButtononClick(View v)
    {
        HashMap<String, String> insertvalues = new HashMap<String, String>();
        insertvalues.put("first_name",first_name.getText().toString());
        insertvalues.put("last_name", last_name.getText().toString());
        insertvalues.put("email_id",  email_id.getText().toString());
        insertvalues.put("phone_number",  phone_number.getText().toString());
        insertvalues.put("home_address",  home_address.getText().toString());

        boolean output = validateEntryBeforeDatabaseAdd(insertvalues);
        if(output == true) {
            dbTools.insertContact(insertvalues);
            clearContent();
        }
    }

    boolean validateEntryBeforeDatabaseAdd(HashMap<String, String> entry)
    {
        // As of now first_name & email_id seems to be mandatory as we display it inside
        // our BasicListEntryActivity class. Rest all can be empty.
        boolean returnValue = true;
        String first_name = entry.get("first_name");
        String email_id  = entry.get("email_id");

        if((first_name.isEmpty() == true)&&(email_id.isEmpty()== true))
        {
            returnValue = false;
        }

        return returnValue;
    }


    private void clearContent()
    {
        // Before that clear the contents so that user would not have any confusion.
        first_name.setText(" ");
        last_name.setText(" ");
        email_id.setText(" ");
        phone_number.setText(" ");
        home_address.setText(" ");

    }

    // call the fetch stuff from database and update(set) the various widgets
    private void setGetButtononClick(View v)
    {
        clearContent();

        //Setup the another activity, which would list(minimal) of all entries
        Intent intent = new Intent(this,BasicListEntryActivity.class);
        startActivity(intent);
    }

    // Delete the current table(hence all entry) and also create the scehma of table.
    private void setDeleteButtononClick(View v)
    {
        SQLiteDatabase database = dbTools.getWritableDatabase();
        int oldversion = dbTools.current_version;
        dbTools.onUpgrade(database,oldversion,oldversion+1);
    }


    private void setClickListner()
    {
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAddButtononClick(v);
            }
        });

        get_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setGetButtononClick(v);
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDeleteButtononClick(v);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // As the activity_main.xml has been set as layout of MainActivity
        // we can fetch the required widgets where we are interested.
        first_name   = (EditText)findViewById(R.id.first_name);
        last_name    = (EditText)findViewById(R.id.last_name);
        email_id     = (EditText)findViewById(R.id.email_id);
        phone_number = (EditText)findViewById(R.id.phone_number);
        home_address = (EditText)findViewById(R.id.home_address);

        add_button    = (Button)findViewById(R.id.add_button);
        get_button    = (Button)findViewById(R.id.get_button);
        delete_button = (Button)findViewById(R.id.delete_button);

        setClickListner();

    }
}
