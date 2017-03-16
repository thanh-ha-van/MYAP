package com.example.havan.mytrafficmap.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NTT on 3/13/2017.
 */

// handle the favorite list.
public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "favListManager";

    //table name
    private static final String TABLE_FAV = "favlist";

    //Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PLACE_ID = "placeid";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE =
                "CREATE TABLE "
                        + TABLE_FAV + "("
                        + KEY_ID
                        + " INTEGER PRIMARY KEY,"
                        + KEY_NAME + " TEXT,"
                        + KEY_ADDRESS + " TEXT,"
                        + KEY_PLACE_ID + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new place
    public void addPlace(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, dataModel.getName()); // Place Name
        values.put(KEY_ADDRESS, dataModel.getAddress()); // Place Phone
        values.put(KEY_PLACE_ID, dataModel.getPlaceID()); // Place Name

        // Inserting Row
        db.insert(TABLE_FAV, null, values);
        db.close(); // Closing database connection
    }

    public boolean checkIfExist(String placeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_FAV,
                new String[]{KEY_PLACE_ID},
                KEY_PLACE_ID + "=?",
                new String[]{String.valueOf(placeId)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;

    }

    // Getting single place
    public DataModel getPlace(String placeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_FAV,
                new String[]{
                        KEY_ID,
                        KEY_NAME,
                        KEY_ADDRESS,
                        KEY_PLACE_ID},
                KEY_PLACE_ID + "=?",
                new String[]{String.valueOf(placeId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DataModel dataModel = new DataModel(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
        );
        // return contact
        cursor.close();
        return dataModel;
    }

    // Getting All places
    public List<DataModel> getAllPLaces() {
        List<DataModel> contactList = new ArrayList<DataModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAV;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataModel contact = new DataModel();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setAddress(cursor.getString(2));
                contact.setPlaceID(cursor.getString(3));
                // Adding place to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return place list
        return contactList;
    }

    // Updating single place
    public int updatePlaces(DataModel place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, place.getName());
        values.put(KEY_ADDRESS, place.getAddress());
        values.put(KEY_PLACE_ID, place.getPlaceID());

        // updating row
        return db.update(TABLE_FAV, values, KEY_ID + " = ?",
                new String[]{String.valueOf(place.getId())});
    }

    // Deleting single place
    public void deletePlace(DataModel place) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAV, KEY_ID + " = ?",
                new String[]{String.valueOf(place.getId())});
        db.close();

    }


    // Getting place Count
    public int getPlaceCout() {
        String countQuery = "SELECT  * FROM " + TABLE_FAV;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
