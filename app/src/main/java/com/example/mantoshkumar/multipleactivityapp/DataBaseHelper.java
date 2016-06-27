package com.example.mantoshkumar.multipleactivityapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;


public class DataBaseHelper extends SQLiteOpenHelper {

    public static int current_version = DataBaseSchema.DATABASE_VERSION;

    public int getCurrent_version() {
        return current_version;
    }

    public DataBaseHelper(Context context) {
        super(context,DataBaseSchema.DATABASE_NAME, null, current_version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = "CREATE TABLE " +
                        DataBaseSchema.TABLE_CONTACTS + "(" +
                        DataBaseSchema.CONTACT_ID   + " INTEGER PRIMARY KEY," +
                        DataBaseSchema.FIRST_NAME   + " TEXT," +
                        DataBaseSchema.LAST_NAME    + " TEXT," +
                        DataBaseSchema.EMAIL_ID     + " TEXT," +
                        DataBaseSchema.PHONE_NUMBER + " TEXT," +
                        DataBaseSchema.HOME_ADDRESS + " TEXT" +
                        ")";

        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS "+ DataBaseSchema.TABLE_CONTACTS;
        database.execSQL(query);
        onCreate(database);
    }


    public void insertContact(HashMap<String, String> queryValues){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DataBaseSchema.FIRST_NAME,   queryValues.get(DataBaseSchema.FIRST_NAME));
        values.put(DataBaseSchema.LAST_NAME,    queryValues.get(DataBaseSchema.LAST_NAME));
        values.put(DataBaseSchema.EMAIL_ID,     queryValues.get(DataBaseSchema.EMAIL_ID));
        values.put(DataBaseSchema.PHONE_NUMBER, queryValues.get(DataBaseSchema.PHONE_NUMBER));
        values.put(DataBaseSchema.HOME_ADDRESS, queryValues.get(DataBaseSchema.HOME_ADDRESS));

        database.insert(DataBaseSchema.TABLE_CONTACTS, null, values);
        database.close();
    }


    public ArrayList<HashMap<String, String>> getAllContactInfo(){

        ArrayList<HashMap<String, String>> allList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getReadableDatabase();
        //String selectQuery = "SELECT * FROM contacts WHERE contact_id='" + id + "'";
        String selectQuery = "SELECT * FROM "+ DataBaseSchema.TABLE_CONTACTS;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                HashMap<String, String> contactMap = new HashMap<String, String>();
                contactMap.put(DataBaseSchema.CONTACT_ID,   cursor.getString(0));
                contactMap.put(DataBaseSchema.FIRST_NAME,   cursor.getString(1));
                contactMap.put(DataBaseSchema.LAST_NAME,    cursor.getString(2));
                contactMap.put(DataBaseSchema.EMAIL_ID,     cursor.getString(3));
                contactMap.put(DataBaseSchema.PHONE_NUMBER, cursor.getString(4));
                contactMap.put(DataBaseSchema.HOME_ADDRESS, cursor.getString(5));
                allList.add(contactMap);
            } while(cursor.moveToNext());
        }
        database.close();
        return allList;
    }

    public HashMap<String, String> getContactInfo(String id){
        HashMap<String, String> contactMap = new HashMap<String, String>();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DataBaseSchema.TABLE_CONTACTS +
                             " WHERE " + DataBaseSchema.CONTACT_ID +
                             "=" + "'" + id + "'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{

                contactMap.put(DataBaseSchema.CONTACT_ID,   cursor.getString(0));
                contactMap.put(DataBaseSchema.FIRST_NAME,   cursor.getString(1));
                contactMap.put(DataBaseSchema.LAST_NAME,    cursor.getString(2));
                contactMap.put(DataBaseSchema.EMAIL_ID,     cursor.getString(3));
                contactMap.put(DataBaseSchema.PHONE_NUMBER, cursor.getString(4));
                contactMap.put(DataBaseSchema.HOME_ADDRESS, cursor.getString(5));
            } while(cursor.moveToNext());
        }
        database.close();
        return contactMap;
    }


    public int updateTableRow(String id, HashMap<String, String> newValue) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DataBaseSchema.FIRST_NAME,   newValue.get(DataBaseSchema.FIRST_NAME));
        values.put(DataBaseSchema.LAST_NAME,    newValue.get(DataBaseSchema.LAST_NAME));
        values.put(DataBaseSchema.EMAIL_ID,     newValue.get(DataBaseSchema.EMAIL_ID));
        values.put(DataBaseSchema.PHONE_NUMBER, newValue.get(DataBaseSchema.PHONE_NUMBER));
        values.put(DataBaseSchema.HOME_ADDRESS, newValue.get(DataBaseSchema.HOME_ADDRESS));

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
