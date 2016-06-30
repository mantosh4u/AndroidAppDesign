package com.example.mantoshkumar.multipleactivityapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

/** This is small base class written which has support for database connectivity. I wrote  **/
/** this class so that all actual activity within this app can be derived from this.       **/
public class BaseCustomWithDataBaseSupportActivity extends Activity {

    private DataBaseHelper mDbTools = new DataBaseHelper(this);
    /** The object that allows me to manipulate the database. **/
    public DataBaseHelper getDbTools() { return mDbTools; }

    /** This is used to display the message(success/failure) based on helper thread outcome. **/
    public void commonHandleMessage(Context context, Message msg, String keyName) {
        /** Fetch what child thread has set the values while completing its execution. **/
        Bundle bundle = msg.getData();
        String string = bundle.getString(keyName);
        /** Toast message when child/helper thread complete with success/failure. **/
        if(string.equals(getString(R.string.correct)) == true) {
            Toast.makeText(context, R.string.correct, Toast.LENGTH_SHORT).show();
        } else if(string.equals(getString(R.string.incorrect)) == true) {
            Toast.makeText(context, R.string.incorrect, Toast.LENGTH_SHORT).show();
        }
    }

}
