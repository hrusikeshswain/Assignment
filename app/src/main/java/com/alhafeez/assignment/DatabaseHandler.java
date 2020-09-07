package com.alhafeez.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database configuration
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "assignment";
    /*private static final String TABLE_LOGIN = "user";*/
    private static final String TABLE_ITEMS = "items";
    private static final String TABLE_ORDERS = "orders";

    // Item table column names
    private static final String KEY_PRIMARYKEY = "item_primarykey";
    private static final String KEY_ID = "item_id";
    private static final String KEY_NAME = "item_name";


    // Order table column names
    private static final String KEY_ORDER_PRIMARYKEY = "order_primarykey";
    private static final String KEY_ORDER_ID = "order_id";
    private static final String KEY_DATE = "order_date";
    private static final String KEY_ORDER_LISTITEMS = "order_listitems";
    private static final String KEY_ORDER_STATUS = "order_status";


    // Creating database
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // User login table
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_PRIMARYKEY + " INTEGER PRIMARY KEY,"
                + KEY_ID + " TEXT,"
                + KEY_NAME + " TEXT"+ ")";

        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + KEY_ORDER_PRIMARYKEY + " INTEGER PRIMARY KEY,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_ORDER_LISTITEMS + " TEXT,"
                + KEY_ORDER_STATUS + " TEXT"+ ")";

        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    /**
     * Re create database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete all rows from tables
        db.delete(TABLE_ITEMS, null, null);
        //db.delete(TABLE_BOOKS, null, null);
        //db.delete(TABLE_CHAPTERS, null, null);
        db.close();
    }

    /**
     * Storing user details in database
     * */
    /*public void addUser(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_PASSWORD, password); // Password
        db.insert(TABLE_LOGIN, null, values);
        db.close();
    }*/


    public void addItems(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_ID, id); // Password
        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }

    public String getChapterDetails(String itemID){

        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS + " WHERE "
                + KEY_ID + " = '" + itemID+"'";
        String name  = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                name =   cursor.getString(2);
            }
        }

        cursor.close();
        db.close();

        return name;
    }

    public void updateStatus(String bid, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_ORDERS + " SET "
                + KEY_ORDER_STATUS + " = '" + status + "' WHERE "
                + KEY_ORDER_ID + " = '" + bid + "'";
        db.execSQL(updateQuery);
        db.close();
    }


    public void deleteOrder(String bid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_ORDERS + " WHERE "
                + KEY_ORDER_ID + " = '" + bid + "'";

        db.execSQL(deleteQuery);
        db.close();
    }


    public void addOrders(String orderid,String orderdate, String orderlist, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_ID, orderid); // id
        values.put(KEY_DATE, orderdate); // date
        values.put(KEY_ORDER_LISTITEMS, orderlist); // items
        values.put(KEY_ORDER_STATUS, status); // status
        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }

    /**
     * Getting user data from database
     * */
   /* public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("name", cursor.getString(1));
            user.put("password", cursor.getString(2));
        }

        cursor.close();
        db.close();

        return user;
    }*/

    public ArrayList<HashMap<String, String>> getAllItems(){
        ArrayList<HashMap<String,String>> arList = new ArrayList<HashMap<String,String>>();
        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                HashMap<String, String> users = new HashMap<String, String>();
                users.put("id", cursor.getString(1));
                users.put("name", cursor.getString(2));
                arList.add(users);
            }
        }

        cursor.close();
        db.close();

        return arList;
    }

    public ArrayList<HashMap<String, String>> getAllRequests(){
        ArrayList<HashMap<String,String>> arList = new ArrayList<HashMap<String,String>>();
        String selectQuery = "SELECT  * FROM " + TABLE_ORDERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                HashMap<String, String> users = new HashMap<String, String>();
                users.put("orderid", cursor.getString(1));
                users.put("orderdate", cursor.getString(2));
                users.put("orderitems", cursor.getString(3));
                users.put("orderstatus", cursor.getString(4));
                arList.add(users);
            }
        }

        cursor.close();
        db.close();

        return arList;
    }



    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }


}