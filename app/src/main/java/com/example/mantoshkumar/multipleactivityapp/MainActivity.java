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

public class MainActivity extends BaseCustomWithDataBaseSupportActivity {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailId;
    private EditText mPhoneNumber;
    private EditText mHomeAddress;

    private Button  mAddButton;
    private Button  mGetButton;
    private Button  mDeleteButton;

    private static String mEmptyString = "";

    private void commonHandleMessage(Message msg, String keyName) {
        Bundle bundle = msg.getData();
        String string = bundle.getString(keyName);
        // Just display the small toast message when child/helper thread complete
        //  with success/failure.
        if(string.equals(getString(R.string.correct)) == true) {
            Toast.makeText(MainActivity.this, R.string.correct, Toast.LENGTH_SHORT).show();
        } else if(string.equals(getString(R.string.incorrect)) == true) {
            Toast.makeText(MainActivity.this, R.string.incorrect, Toast.LENGTH_SHORT).show();
        }
    }

    Handler mDeleteButtonThreadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            commonHandleMessage(msg, getString(R.string.result_delete_button));
        }
    };

    Handler mAddButtonThreadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            commonHandleMessage(msg, getString(R.string.result_add_button));
        }
    };

    // call the insert stuff into database from(get) from various widgets
    private void setAddButtonOnClick(View v) {
        final HashMap<String, String> insertValues = new HashMap<String, String>();

        insertValues.put(DataBaseSchema.FIRST_NAME,    mFirstName.getText().toString());
        insertValues.put(DataBaseSchema.LAST_NAME,     mLastName.getText().toString());
        insertValues.put(DataBaseSchema.EMAIL_ID,      mEmailId.getText().toString());
        insertValues.put(DataBaseSchema.PHONE_NUMBER,  mPhoneNumber.getText().toString());
        insertValues.put(DataBaseSchema.HOME_ADDRESS,  mHomeAddress.getText().toString());

        boolean output = validateEntryBeforeDatabaseAdd(insertValues);

        if(output == true) {
            // This is Add operation on database and hence it should also be excecuted
            // in separate thread.
            Runnable addButtononRunnable = new Runnable() {
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
            Thread threadAddButton = new Thread(addButtononRunnable);
            threadAddButton.start();

            clearContent();
        }
    }

    boolean validateEntryBeforeDatabaseAdd(HashMap<String, String> entry) {
        // As of now first_name & email_id seems to be mandatory as we display it inside
        // our BasicListEntryActivity class. Rest all can be empty.
        boolean returnValue = true;
        final String firstName = entry.get(DataBaseSchema.FIRST_NAME);
        final String emailId  = entry.get(DataBaseSchema.EMAIL_ID);

        if((firstName.isEmpty() == true)||(emailId.isEmpty()== true)) {
            returnValue = false;
        }
        return returnValue;
    }


    private void clearContent() {
        // Before that clear the contents so that user would not have any confusion.
        mFirstName.setText(mEmptyString);
        mLastName.setText(mEmptyString);
        mEmailId.setText(mEmptyString);
        mPhoneNumber.setText(mEmptyString);
        mPhoneNumber.setText(mEmptyString);
    }

    // call the fetch stuff from database and update(set) the various widgets
    private void setGetButtonOnClick(View v) {
        clearContent();
        //Setup the another activity, which would list(minimal) of all entries
        Intent intent = new Intent(this, BasicListEntryActivity.class);
        startActivity(intent);
    }

    // Delete the current table(hence all entry) and also create the scehma of table.
    private void setDeleteButtonOnClick(View v) {
        // Since database operations can be blocking, hence we should execute these in
        // different thread.
        Runnable deleteButtonRunnable = new Runnable() {
            @Override
            public void run() {
                String result;
                try {
                    SQLiteDatabase database = getDbTools().getWritableDatabase();
                    int oldVersion = getDbTools().getCurrent_version();
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
        // As the activity_main.xml has been set as layout of MainActivity
        // we can fetch the required widgets where we are interested.
        mFirstName   = (EditText)findViewById(R.id.first_name);
        mLastName    = (EditText)findViewById(R.id.last_name);
        mEmailId     = (EditText)findViewById(R.id.email_id);
        mPhoneNumber = (EditText)findViewById(R.id.phone_number);
        mHomeAddress = (EditText)findViewById(R.id.home_address);

        mAddButton    = (Button)findViewById(R.id.add_button);
        mGetButton    = (Button)findViewById(R.id.get_button);
        mDeleteButton = (Button)findViewById(R.id.delete_button);

        setClickListner();
    }
}

