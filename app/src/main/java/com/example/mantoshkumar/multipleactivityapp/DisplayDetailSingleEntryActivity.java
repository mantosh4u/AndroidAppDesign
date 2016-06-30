package com.example.mantoshkumar.multipleactivityapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.HashMap;


/** This activity displays detailed view of a particular row and post that it allows user **/
/** to update any attributes and updated values would be again stored into database.      **/
public class DisplayDetailSingleEntryActivity extends BaseCustomWithDataBaseSupportActivity {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailId;
    private EditText mPhoneNumber;
    private EditText mHomeAddress;

    private Button mUpdateButton;
    private String mCurrentSelectedId;

    private Handler mFetchWhileStartupThreadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){

            /** Update all widgets based on above values. UI logic should execute in main thread **/
            Bundle bundle = msg.getData();
            mFirstName.setText(bundle.getString(DataBaseSchema.FIRST_NAME) );
            mLastName.setText(bundle.getString(DataBaseSchema.LAST_NAME) );
            mEmailId.setText(bundle.getString(DataBaseSchema.EMAIL_ID) );
            mPhoneNumber.setText(bundle.getString(DataBaseSchema.PHONE_NUMBER) );
            mHomeAddress.setText(bundle.getString(DataBaseSchema.HOME_ADDRESS) );

            /** Now we can call the common logic applicable for all thread handler. **/
            commonHandleMessage(DisplayDetailSingleEntryActivity.this, msg,
                    getString(R.string.result_fetch_while_startup));
        }
    };

    private Handler mUpdateButtonThreadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            commonHandleMessage(DisplayDetailSingleEntryActivity.this, msg,
                                getString(R.string.result_update_button));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_detail_single_entry);

        /** Find all relevant widgets where things need to be updated. **/
        mFirstName   = (EditText)findViewById(R.id.first_name);
        mLastName    = (EditText)findViewById(R.id.last_name);
        mEmailId     = (EditText)findViewById(R.id.email_id);
        mPhoneNumber = (EditText)findViewById(R.id.phone_number);
        mHomeAddress = (EditText)findViewById(R.id.home_address);

        mUpdateButton = (Button)findViewById(R.id.update_button);
        setClickListner();

        /**  Get the intent and find the ID which is selected by the user. **/
        Intent intent = getIntent();
        mCurrentSelectedId   = intent.getStringExtra(BasicListEntryActivity.INTERNAL_CUSTOMER_ID);
        getCustomerIDAndDisplay(mCurrentSelectedId);
    }

    private void setClickListner()  {
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpdateButtonOnClick(v);
            }
        });
    }


    private  void setUpdateButtonOnClick(View v) {
        /** In this case, we need to update database/table with new values. **/
        final HashMap<String, String> insertValues = new HashMap<String, String>();

        insertValues.put(DataBaseSchema.FIRST_NAME,    mFirstName.getText().toString());
        insertValues.put(DataBaseSchema.LAST_NAME,     mLastName.getText().toString());
        insertValues.put(DataBaseSchema.EMAIL_ID,      mEmailId.getText().toString());
        insertValues.put(DataBaseSchema.PHONE_NUMBER,  mPhoneNumber.getText().toString());
        insertValues.put(DataBaseSchema.HOME_ADDRESS,  mHomeAddress.getText().toString());

        /** This is UPDATE operation in database and hence it should execute himself **/
        /** in separate thread.                                                      **/
        Runnable updateButtonOnRunnable = new Runnable() {
            @Override
            public void run() {
                String result;
                try {
                    getDbTools().updateTableRow(mCurrentSelectedId, insertValues);
                    result = getString(R.string.correct);
                }
                catch (Exception ec) {
                    result = getString(R.string.incorrect);
                }
                Message msg = mUpdateButtonThreadHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.result_update_button), result);
                msg.setData(bundle);
                mUpdateButtonThreadHandler.sendMessage(msg);
            }
        };

        Thread threadAddButton = new Thread(updateButtonOnRunnable);
        threadAddButton.start();

    }


    private void getCustomerIDAndDisplay(String index)  {
        final String indexvalue = index;
        /** This is fetch operation from database for a particular index while startup of **/
        /** activity and hence it should execute himself in separate thread.              **/
        Runnable fetchWhileStartupRunnable = new Runnable() {
            @Override
            public void run() {
                String result;
                HashMap<String, String> getOneEntry = null;
                try {
                    getOneEntry = getDbTools().getContactInfo(indexvalue);
                    result = getString(R.string.correct);
                }
                catch (Exception ec) {
                    result = getString(R.string.incorrect);
                }
                Message msg = mFetchWhileStartupThreadHandler.obtainMessage();
                Bundle bundle = new Bundle();
                /** Now store all values of getOneEntry(map) into bundle **/
                if(getOneEntry != null) {
                    bundle.putString(DataBaseSchema.FIRST_NAME,
                                     getOneEntry.get(DataBaseSchema.FIRST_NAME).toString());
                    bundle.putString(DataBaseSchema.LAST_NAME,
                            getOneEntry.get(DataBaseSchema.LAST_NAME).toString());
                    bundle.putString(DataBaseSchema.EMAIL_ID,
                            getOneEntry.get(DataBaseSchema.EMAIL_ID).toString());
                    bundle.putString(DataBaseSchema.PHONE_NUMBER,
                            getOneEntry.get(DataBaseSchema.PHONE_NUMBER).toString());
                    bundle.putString(DataBaseSchema.HOME_ADDRESS,
                            getOneEntry.get(DataBaseSchema.HOME_ADDRESS).toString());
                }

                bundle.putString(getString(R.string.result_fetch_while_startup), result);
                msg.setData(bundle);
                mFetchWhileStartupThreadHandler.sendMessage(msg);
            }
        };

        Thread threadAddButton = new Thread(fetchWhileStartupRunnable);
        threadAddButton.start();
    }
}
