package com.example.mantoshkumar.multipleactivityapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;



public class DataBaseHelper extends SQLiteOpenHelper {

    public static int current_version = 1;
    public DataBaseHelper(Context context)
    {
        super(context,"address.db",null, current_version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = "CREATE TABLE contacts ( contact_id INTEGER PRIMARY KEY, first_name TEXT, " +
                "last_name TEXT, email_id TEXT, phone_number TEXT, home_address TEXT)";

        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS contacts";
        database.execSQL(query);
        onCreate(database);
    }


    public void insertContact(HashMap<String, String> queryValues){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("first_name",   queryValues.get("first_name"));
        values.put("last_name",    queryValues.get("last_name"));
        values.put("email_id",     queryValues.get("email_id"));
        values.put("phone_number", queryValues.get("phone_number"));
        values.put("home_address", queryValues.get("home_address"));

        database.insert("contacts", null, values);

        database.close();

    }


    public ArrayList<HashMap<String, String>> getAllContactInfo(){

        ArrayList<HashMap<String, String>> allList = new ArrayList<HashMap<String, String>>();

        SQLiteDatabase database = this.getReadableDatabase();

        //String selectQuery = "SELECT * FROM contacts WHERE contact_id='" + id + "'";
        String selectQuery = "SELECT * FROM contacts";
        Cursor cursor = database.rawQuery(selectQuery, null);
        int nentry = cursor.getCount();

        if(cursor.moveToFirst()){
            do{
                HashMap<String, String> contactMap = new HashMap<String, String>();
                contactMap.put("contact_id",   cursor.getString(0));
                contactMap.put("first_name",   cursor.getString(1));
                contactMap.put("last_name",    cursor.getString(2));
                contactMap.put("email_id",     cursor.getString(3));
                contactMap.put("phone_number", cursor.getString(4));
                contactMap.put("home_address", cursor.getString(5));
                allList.add(contactMap);
            } while(cursor.moveToNext());
        }

        database.close();
        return allList;
    }

    public HashMap<String, String> getContactInfo(String id){
        HashMap<String, String> contactMap = new HashMap<String, String>();
        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM contacts WHERE contact_id='" + id + "'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{

                contactMap.put("contact_id",   cursor.getString(0));
                contactMap.put("first_name",   cursor.getString(1));
                contactMap.put("last_name",    cursor.getString(2));
                contactMap.put("email_id",     cursor.getString(3));
                contactMap.put("phone_number", cursor.getString(4));
                contactMap.put("home_address", cursor.getString(5));
            } while(cursor.moveToNext());
        }

        database.close();
        return contactMap;
    }


    public int updateTableRow(String id, HashMap<String, String> newValue){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("first_name",   newValue.get("first_name"));
        values.put("last_name",    newValue.get("last_name"));
        values.put("email_id",     newValue.get("email_id"));
        values.put("phone_number", newValue.get("phone_number"));
        values.put("home_address", newValue.get("home_address"));

        int affectedRows = database.update(
                           "contacts",
                           values,
                           "contact_id"+"="+id,
                           null
                           );

        database.close();
        return affectedRows;
    }

}
