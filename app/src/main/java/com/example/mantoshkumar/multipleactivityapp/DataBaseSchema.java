package com.example.mantoshkumar.multipleactivityapp;


import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

/** This Represents all entries/names required to represent this particular table. **/
public class DataBaseSchema  {
    /** Initial Version Of Database.  **/
    public static final int DATABASE_VERSION = 1;
    /** Database Name. **/
    public static final String DATABASE_NAME = "address.db";
    /** Table Name. **/
    public static final String TABLE_CONTACTS = "contacts";
    /** Name for contact_id attribute of a particular row. **/
    public static final String CONTACT_ID   = "contact_id";
    /** CONTACT_ID attribute position. **/
    public static final int    CONTACT_ID_POS = 0;
    /** Name for first_name attribute of a particular row. **/
    public static final String FIRST_NAME   = "first_name";
    /** FIRST_NAME attribute position. **/
    public static final int    FIRST_NAME_POS = 1;
    /** Name for last_name attribute of a particular row. **/
    public static final String LAST_NAME    = "last_name";
    /** LAST_NAME attribute position. **/
    public static final int    LAST_NAME_POS = 2;
    /** Name for email_id attribute of a particular row. **/
    public static final String EMAIL_ID     = "email_id";
    /** EMAIL_ID attribute position. **/
    public static final int    EMAIL_ID_POS = 3;
    /** Name for phone_number of a particular row. **/
    public static final String PHONE_NUMBER = "phone_number";
    /** PHONE_NUMBER attribute position. **/
    public static final int    PHONE_NUMBER_POS = 4;
    /** Name for home_address attribute of a particular row. **/;
    public static final String HOME_ADDRESS = "home_address";
    /** HOME_ADDRESS attribute position. **/
    public static final int    HOME_ADDRESS_POS = 5;


    /** Query to create this particular "contacts" table. **/
    public static final String sCreateTableQuery = "CREATE TABLE " +
            DataBaseSchema.TABLE_CONTACTS + "(" +
            DataBaseSchema.CONTACT_ID   + " INTEGER PRIMARY KEY," +
            DataBaseSchema.FIRST_NAME   + " TEXT," +
            DataBaseSchema.LAST_NAME    + " TEXT," +
            DataBaseSchema.EMAIL_ID     + " TEXT," +
            DataBaseSchema.PHONE_NUMBER + " TEXT," +
            DataBaseSchema.HOME_ADDRESS + " TEXT" +
            ")";

    /** Query to delete the "contact" table. **/
    public static final String sDeleteTableQuery = "DROP TABLE IF EXISTS " +
                                                   DataBaseSchema.TABLE_CONTACTS;

    /** Query to fetch all rows from the "contact" table. **/
    public static final String sSelectAllQuery = "SELECT * FROM " +
                                                  DataBaseSchema.TABLE_CONTACTS;

    /** Helper method for inserting a row into this particular table. **/
    public static void insertRowIntoTableHelper(ContentValues value,
                                                HashMap<String, String> queryValues) {

        value.put(DataBaseSchema.FIRST_NAME,   queryValues.get(DataBaseSchema.FIRST_NAME));
        value.put(DataBaseSchema.LAST_NAME,    queryValues.get(DataBaseSchema.LAST_NAME));
        value.put(DataBaseSchema.EMAIL_ID,     queryValues.get(DataBaseSchema.EMAIL_ID));
        value.put(DataBaseSchema.PHONE_NUMBER, queryValues.get(DataBaseSchema.PHONE_NUMBER));
        value.put(DataBaseSchema.HOME_ADDRESS, queryValues.get(DataBaseSchema.HOME_ADDRESS));
    }

    /** Helper method for fetching all rows and storing into its "list" argument.  **/
    public static void getAllFromTableHelper(Cursor cursor,
                                             ArrayList<HashMap<String, String>> list)  {
        if(cursor.moveToFirst()){
            do{
                HashMap<String, String> contactMap = new HashMap<String, String>();

                contactMap.put(DataBaseSchema.CONTACT_ID,   cursor.getString(CONTACT_ID_POS));
                contactMap.put(DataBaseSchema.FIRST_NAME,   cursor.getString(FIRST_NAME_POS));
                contactMap.put(DataBaseSchema.LAST_NAME,    cursor.getString(LAST_NAME_POS));
                contactMap.put(DataBaseSchema.EMAIL_ID,     cursor.getString(EMAIL_ID_POS));
                contactMap.put(DataBaseSchema.PHONE_NUMBER, cursor.getString(PHONE_NUMBER_POS));
                contactMap.put(DataBaseSchema.HOME_ADDRESS, cursor.getString(HOME_ADDRESS_POS));
                list.add(contactMap);
            } while(cursor.moveToNext());
        }
    }

    /** Helper method for fetching a particular row and storing value into "contact" argument. **/
    public static void  getContactInfoHelper(Cursor cursor, HashMap<String, String> contact) {

        if(cursor.moveToFirst()){
            do{

                contact.put(DataBaseSchema.CONTACT_ID,   cursor.getString(CONTACT_ID_POS));
                contact.put(DataBaseSchema.FIRST_NAME,   cursor.getString(FIRST_NAME_POS));
                contact.put(DataBaseSchema.LAST_NAME,    cursor.getString(LAST_NAME_POS));
                contact.put(DataBaseSchema.EMAIL_ID,     cursor.getString(EMAIL_ID_POS));
                contact.put(DataBaseSchema.PHONE_NUMBER, cursor.getString(PHONE_NUMBER_POS));
                contact.put(DataBaseSchema.HOME_ADDRESS, cursor.getString(HOME_ADDRESS_POS));
            } while(cursor.moveToNext());
        }
    }


    /** Helper method for updating a particular row based on newValue. **/
    public static void updateTableRowHelper(ContentValues value,
                                            HashMap<String, String> newValue) {

        value.put(DataBaseSchema.FIRST_NAME,   newValue.get(DataBaseSchema.FIRST_NAME));
        value.put(DataBaseSchema.LAST_NAME,    newValue.get(DataBaseSchema.LAST_NAME));
        value.put(DataBaseSchema.EMAIL_ID,     newValue.get(DataBaseSchema.EMAIL_ID));
        value.put(DataBaseSchema.PHONE_NUMBER, newValue.get(DataBaseSchema.PHONE_NUMBER));
        value.put(DataBaseSchema.HOME_ADDRESS, newValue.get(DataBaseSchema.HOME_ADDRESS));
    }


    public static String getQueryBasedOnId(String id) {
        String wherePart = " WHERE " + DataBaseSchema.CONTACT_ID;
        String selectQueryOnId = sSelectAllQuery +
                                 wherePart +
                                 "=" + "'" + id + "'";
        return selectQueryOnId;
    }
}

