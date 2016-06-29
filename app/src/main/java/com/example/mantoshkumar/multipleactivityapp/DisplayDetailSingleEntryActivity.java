package com.example.mantoshkumar.multipleactivityapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.HashMap;

public class DisplayDetailSingleEntryActivity extends BaseCustomWithDataBaseSupportActivity {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailId;
    private EditText mPhoneNumber;
    private EditText mHomeAddress;

    // The below Id would be used to exchange the information to next activity.
    private final static String INTERNAL_CUSTOMER_ID = "@string/shared_customer_id";

    private Button mUpdateButton;
    private String mCurrentSelectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_detail_single_entry);

        // Find all relevent widgets where things neeeds to be updated.
        mFirstName   = (EditText)findViewById(R.id.first_name);
        mLastName    = (EditText)findViewById(R.id.last_name);
        mEmailId     = (EditText)findViewById(R.id.email_id);
        mPhoneNumber = (EditText)findViewById(R.id.phone_number);
        mHomeAddress = (EditText)findViewById(R.id.home_address);

        mUpdateButton = (Button)findViewById(R.id.update_button);
        setClickListner();

        // Get the intent and find the ID which is selected by the user.
        Intent intent = getIntent();
        mCurrentSelectedId   = intent.getStringExtra(BasicListEntryActivity.INTERNAL_CUSTOMER_ID);
        getCustomerIDAndDisplay(mCurrentSelectedId);
    }

    private void setClickListner()  {
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpdateButtononClick(v);
            }
        });
    }


    private  void setUpdateButtononClick(View v) {
        // In this case, we need to update database/table with new values.
        HashMap<String, String> insertValues = new HashMap<String, String>();

        insertValues.put(DataBaseSchema.FIRST_NAME,    mFirstName.getText().toString());
        insertValues.put(DataBaseSchema.LAST_NAME,     mLastName.getText().toString());
        insertValues.put(DataBaseSchema.EMAIL_ID,      mEmailId.getText().toString());
        insertValues.put(DataBaseSchema.PHONE_NUMBER,  mPhoneNumber.getText().toString());
        insertValues.put(DataBaseSchema.HOME_ADDRESS,  mHomeAddress.getText().toString());

        getDbTools().updateTableRow(mCurrentSelectedId, insertValues);
    }

    private void getCustomerIDAndDisplay(String index)  {
        HashMap<String, String> getOneEntry = getDbTools().getContactInfo(index);

        // Update all widgets based on above values
        mFirstName.setText(getOneEntry.get(DataBaseSchema.FIRST_NAME).toString());
        mLastName.setText(getOneEntry.get(DataBaseSchema.LAST_NAME).toString());
        mEmailId.setText(getOneEntry.get(DataBaseSchema.EMAIL_ID).toString());
        mPhoneNumber.setText(getOneEntry.get(DataBaseSchema.PHONE_NUMBER).toString());
        mHomeAddress.setText(getOneEntry.get(DataBaseSchema.HOME_ADDRESS).toString());
    }

}
