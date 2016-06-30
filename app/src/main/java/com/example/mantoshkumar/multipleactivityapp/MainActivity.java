package com.example.mantoshkumar.multipleactivityapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;

/** The main/start point for this particular application. **/
public class MainActivity extends BaseCustomWithDataBaseSupportActivity {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailId;
    private EditText mPhoneNumber;
    private EditText mHomeAddress;

    private Button  mAddButton;
    private Button  mGetButton;
    private Button  mDeleteButton;

    private static String sEmptyString = "";

    private Handler mDeleteButtonThreadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            commonHandleMessage(MainActivity.this, msg, getString(R.string.result_delete_button));
        }
    };

    private Handler mAddButtonThreadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            commonHandleMessage(MainActivity.this, msg, getString(R.string.result_add_button));
        }
    };

    /** perform the insert operation in database using widgets information(getText) **/
    private void setAddButtonOnClick(View v) {
        final HashMap<String, String> insertValues = new HashMap<String, String>();

        insertValues.put(DataBaseSchema.FIRST_NAME,    mFirstName.getText().toString());
        insertValues.put(DataBaseSchema.LAST_NAME,     mLastName.getText().toString());
        insertValues.put(DataBaseSchema.EMAIL_ID,      mEmailId.getText().toString());
        insertValues.put(DataBaseSchema.PHONE_NUMBER,  mPhoneNumber.getText().toString());
        insertValues.put(DataBaseSchema.HOME_ADDRESS,  mHomeAddress.getText().toString());

        boolean output = validateEntryBeforeDatabaseAdd(insertValues);

        if(output == true) {
            /** This is ADD operation in database and hence it should execute himself **/
            /** in separate thread.                                                   **/
            Runnable addButtonOnRunnable = new Runnable() {
                @Override
                public void run() {
                    String result;
                    try {
                        getDbTools().insertContact(insertValues);
                        result = getString(R.string.correct);
                    }
                    catch (Exception ec) {
                        result = getString(R.string.incorrect);
                    }
                    Message msg = mAddButtonThreadHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.result_add_button), result);
                    msg.setData(bundle);
                    mAddButtonThreadHandler.sendMessage(msg);
                }
            };
            Thread threadAddButton = new Thread(addButtonOnRunnable);
            threadAddButton.start();

            clearContent();
        }
    }

    /** As of now mFirstName & mEmailId seems to be mandatory as we display it inside **/
    /** our BasicListEntryActivity class. Hence rest all can be empty.                **/
    private boolean validateEntryBeforeDatabaseAdd(HashMap<String, String> entry) {

        boolean returnValue = true;
        final String firstName = entry.get(DataBaseSchema.FIRST_NAME);
        final String emailId  = entry.get(DataBaseSchema.EMAIL_ID);

        if((firstName.isEmpty() == true)||(emailId.isEmpty()== true)) {
            returnValue = false;
        }
        return returnValue;
    }

    /** Before new screen display,clear the previous contents so that **/
    /** user would not have any confusion.                            **/
    private void clearContent() {
        mFirstName.setText(sEmptyString);
        mLastName.setText(sEmptyString);
        mEmailId.setText(sEmptyString);
        mPhoneNumber.setText(sEmptyString);
        mHomeAddress.setText(sEmptyString);
    }

    /** Perform the fetch/query operation from database and update/set all widgets **/
    private void setGetButtonOnClick(View v) {
        clearContent();
        /** Launch the new activity, which would list of all so far stored entries **/
        Intent intent = new Intent(this, BasicListEntryActivity.class);
        startActivity(intent);
    }

    /** Perform the delete operation in current table/database and also create new empty table  **/
    private void setDeleteButtonOnClick(View v) {
        /** This is DELETE operation in database and hence it should execute himself **/
        /** in separate thread.                                                      **/
        Runnable deleteButtonRunnable = new Runnable() {
            @Override
            public void run() {
                String result;
                try {
                    SQLiteDatabase database = getDbTools().getWritableDatabase();
                    int oldVersion = getDbTools().getsCurrentVersion();
                    getDbTools().onUpgrade(database, oldVersion, oldVersion + 1);
                    result = getString(R.string.correct);
                }
                catch (Exception ec) {
                    result = getString(R.string.incorrect);
                }
                Message msg = mDeleteButtonThreadHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.result_delete_button), result);
                msg.setData(bundle);
                mDeleteButtonThreadHandler.sendMessage(msg);
            }
        };

        Thread threadDeleteButton = new Thread(deleteButtonRunnable);
        threadDeleteButton.start();
    }


    private void setClickListner() {
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddButtonOnClick(v);
            }
        });

        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGetButtonOnClick(v);
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDeleteButtonOnClick(v);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /** As the activity_main.xml has been set as layout of MainActivity  **/
        /** we can fetch the required widgets View of our interest.          **/
        mFirstName    = (EditText)findViewById(R.id.first_name);
        mLastName     = (EditText)findViewById(R.id.last_name);
        mEmailId      = (EditText)findViewById(R.id.email_id);
        mPhoneNumber  = (EditText)findViewById(R.id.phone_number);
        mHomeAddress  = (EditText)findViewById(R.id.home_address);

        mAddButton    = (Button)findViewById(R.id.add_button);
        mGetButton    = (Button)findViewById(R.id.get_button);
        mDeleteButton = (Button)findViewById(R.id.delete_button);

        setClickListner();
    }
}
