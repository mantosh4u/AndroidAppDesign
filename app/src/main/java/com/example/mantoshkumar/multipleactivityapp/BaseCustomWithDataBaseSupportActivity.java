package com.example.mantoshkumar.multipleactivityapp;

import android.app.Activity;

/* This is small base class written which has support for database connectivity. I wrote this
 * class so that all actual activity within this app can be derived from this. */
public class BaseCustomWithDataBaseSupportActivity extends Activity {

    // The object that allows me to manipulate the database
    private DataBaseHelper mDbTools = new DataBaseHelper(this);
    public DataBaseHelper getDbTools() {
        return mDbTools;
    }
}
