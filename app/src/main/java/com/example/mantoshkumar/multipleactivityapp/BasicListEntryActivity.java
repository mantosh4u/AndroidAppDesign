package com.example.mantoshkumar.multipleactivityapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/** The BasicListEntryActivity displays the minimal attributes of all rows so that  **/
/** client can select/touch on particular entry to get the detailed information.    **/
public class BasicListEntryActivity extends BaseCustomWithDataBaseSupportActivity {

    private TableLayout mMainTable;
    /** Create & maintain list of rows which would be used later to embed various widgets. **/
    private ArrayList<TableRow>  mTableRowsList = new ArrayList<TableRow>();
    /** Create two textview entry per row which would be added in mMainTable. **/
    private TextView mContactId;
    private TextView mFirstName;
    private TextView mEmailId;
    ArrayList<HashMap<String, String>> mGetValues;

    /** The below Id would be used to exchange the information to next activity. **/
    public static String INTERNAL_CUSTOMER_ID = "@string/shared_customer_id";

    private Handler mFetchAllEntryThreadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){

            /** Get the id of main container widget so that we can perform later activity. **/
            mMainTable = (TableLayout)findViewById(R.id.container_widget);
            /** Get the width of header widgets so that it can be applied to all created rows. **/
            TextView headerId        = (TextView)findViewById(R.id.contact_id);
            TextView headerFirstName = (TextView)findViewById(R.id.first_name);
            TextView headerEmailId   = (TextView)findViewById(R.id.email_id);

            /** Iterate through all entry and create that many row        **/
            /** which entern would add mContactId, mFirstName & mEmailId. **/
            for(int index = 0; index < mGetValues.size(); ++index)
            {
                HashMap<String, String> getOneEntry = mGetValues.get(index);

                /** Create one table row per entry  **/
                TableRow table_row = new TableRow(BasicListEntryActivity.this);
                mMainTable.addView(table_row);

                /** Create mContactId and set its value from db fetched information. **/
                mContactId = new TextView(BasicListEntryActivity.this);
                mContactId.setLayoutParams(headerId.getLayoutParams());
                mContactId.setText(getOneEntry.get(DataBaseSchema.CONTACT_ID));
                table_row.addView(mContactId);

                /** Create mFirstName and set its value from db fetched information. **/
                mFirstName = new TextView(BasicListEntryActivity.this);
                mFirstName.setLayoutParams(headerFirstName.getLayoutParams());
                mFirstName.setText(getOneEntry.get(DataBaseSchema.FIRST_NAME));
                table_row.addView(mFirstName);

                /** Create mEmailId and set its value from db fetched information. **/
                mEmailId   = new TextView(BasicListEntryActivity.this);
                mEmailId.setLayoutParams(headerEmailId.getLayoutParams());
                mEmailId.setText(getOneEntry.get(DataBaseSchema.EMAIL_ID));

                table_row.addView(mEmailId);

                /** Now add into our container list member 'mTableRowsList'. **/
                mTableRowsList.add(table_row);
            }
            /** Now we have all rows created with meaningful value, lets set their listner **/
            setClickListner();

            /** Now we can call the common logic applicable for all thread handler. **/
            commonHandleMessage(BasicListEntryActivity.this, msg,
                    getString(R.string.result_fetch_all_entry));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_list_entry);

        /** This is FETCH operation for all rows from database and hence it should execute **/
        /** himself in separate thread.                                                    **/
        Runnable fetchAllEntryRunnable = new Runnable() {
            @Override
            public void run() {
                String result;
                try {
                    mGetValues = getDbTools().getAllContactInfo();
                    result = getString(R.string.correct);
                }
                catch (Exception ec) {
                    result = getString(R.string.incorrect);
                }
                Message msg = mFetchAllEntryThreadHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.result_fetch_all_entry), result);
                msg.setData(bundle);
                mFetchAllEntryThreadHandler.sendMessage(msg);
            }
        };
        Thread threadAddButton = new Thread(fetchAllEntryRunnable);
        threadAddButton.start();

    }

    /** This is required as resume the activity requires to update its view to reflect the new **/
    /** updated information set by user. **/
    @Override
    public void onResume() {
        super.onResume();
        this.onCreate(null);
    }

    /** User may click on any row and application should display the detailed info **/
    private void setClickListner() {
        for(int index = 0; index < mTableRowsList.size(); ++index)
        {
            mTableRowsList.get(index).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRowOnClick(v);
                }
            });
        }
    }

    private void setRowOnClick(View v) {
        /** Get which row has been touched by user. To achieve this, first extract container  **/
        /** view(TableRow). In one row,first child is Id view so extract it and now fetch the **/
        /** id of that particular row. This would be passed to next activity. **/
        TableRow clickedRow  = (TableRow)v;
        View clickedIdView   = clickedRow.getChildAt(0);
        TextView contactId   = (TextView)clickedIdView;
        String id = contactId.getText().toString();

        /** Launch another activity,which would display detailed view of selected entry. **/
        Intent intent = new Intent(this, DisplayDetailSingleEntryActivity.class);
        intent.putExtra(INTERNAL_CUSTOMER_ID, id);
        startActivity(intent);
    }
}
