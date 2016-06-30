package com.example.mantoshkumar.multipleactivityapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;


/** This is generic helper class which can be used if any activity requires database support. **/
public class DataBaseHelper extends SQLiteOpenHelper {

    private static int sCurrentVersion = DataBaseSchema.DATABASE_VERSION;

    /** Returns the current version of database. **/
    public static int getsCurrentVersion() { return sCurrentVersion; }

    /** Constructor for this class to create an object. **/
    public DataBaseHelper(Context context) {
        super(context, DataBaseSchema.DATABASE_NAME, null, sCurrentVersion);
    }

    /** Create the table as stored in query "sCreateTableQuery". **/
    @Override
    public void onCreate(SQLiteDatabase database) {
            database.execSQL(DataBaseSchema.sCreateTableQuery);
    }

    /** Delete the table so that all entries would be gone and then create a new empty table. **/
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(DataBaseSchema.sDeleteTableQuery);
        onCreate(database);
    }

    /** Insert a particular row based on input values received from the client. **/
    public void insertContact(HashMap<String, String> queryValues){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        /** Use a particular table specific logic from class DataBaseSchema which is written **/
        /** specific to this particular application.                                         **/
        DataBaseSchema.insertRowIntoTableHelper(values,queryValues);

        database.insert(DataBaseSchema.TABLE_CONTACTS, null, values);
        database.close();
    }

    /** Fetch all rows from database table and return to client. **/
    public ArrayList<HashMap<String, String>> getAllContactInfo(){

        ArrayList<HashMap<String, String>> allList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getReadableDatabase();

        /** Use a particular table specific logic from class DataBaseSchema which is written **/
        /** specific to this particular application.                                         **/
        Cursor cursor = database.rawQuery(DataBaseSchema.sSelectAllQuery, null);
        DataBaseSchema.getAllFromTableHelper(cursor, allList);

        database.close();
        return allList;
    }


    public HashMap<String, String> getContactInfo(String id){
        HashMap<String, String> contactMap = new HashMap<String, String>();
        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = DataBaseSchema.getQueryBasedOnId(id);
        Cursor cursor = database.rawQuery(selectQuery, null);

        /** Use a particular table specific logic from class DataBaseSchema which is written **/
        /** specific to this particular application.                                         **/
        DataBaseSchema.getContactInfoHelper(cursor, contactMap);

        database.close();
        return contactMap;
    }


    /** Update a particular row using the new values received from client. **/
    public int updateTableRow(String id, HashMap<String, String> newValue) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        /** Use a particular table specific logic from class DataBaseSchema which is written **/
        /** specific to this particular application.                                         **/
        DataBaseSchema.updateTableRowHelper(values,newValue);
        /** TODO: Move the below logic in DataBaseSchema so that current class would be generic **/
        int affectedRows = database.update(
                           DataBaseSchema.TABLE_CONTACTS,
                           values,
                           DataBaseSchema.CONTACT_ID+"="+id,
                           null
                           );

        database.close();
        return affectedRows;
    }
}
