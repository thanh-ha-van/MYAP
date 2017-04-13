package com.example.havan.mytrafficmap.Route;

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
public class RouteDatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ROUTESDATA";

    //table name
    private static final String TABLE_FAV_PLACE = "ROUTESTABLE";

    //Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PLACE_LAT = "placelat";
    private static final String KEY_PLACE_LON = "placelon";
    private static final String KEY_VALUE= "value";


    public RouteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE =
                "CREATE TABLE "
                        + TABLE_FAV_PLACE + "("
                        + KEY_ID + " INTEGER PRIMARY KEY,"
                        + KEY_NAME + " TEXT,"
                        + KEY_ADDRESS + " TEXT,"
                        + KEY_PLACE_LAT + " TEXT,"
                        + KEY_PLACE_LON + " TEXT, "
                        + KEY_VALUE + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV_PLACE);

        // Create tables again
        onCreate(db);
    }

    // Adding new place
    public void Addroute(RouteModel routeModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, routeModel.getName()); // Place Name
        values.put(KEY_ADDRESS, routeModel.getAddress()); // Place Phone
        values.put(KEY_PLACE_LAT, String.valueOf(routeModel.getPlaceLat())); // Place latitude
        values.put(KEY_PLACE_LON, String.valueOf(routeModel.getPlaceLon())); // Place longitude
        values.put(KEY_VALUE, routeModel.getValue()); // Place Name

        // Inserting Row
        db.insert(TABLE_FAV_PLACE, null, values);
        db.close(); // Closing database connection
    }

    public boolean checkIfExist(String placename) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_FAV_PLACE,
                new String[]{KEY_NAME},
                KEY_NAME + "=?",
                new String[]{String.valueOf(placename)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;

    }


    // Getting All places
    public List<RouteModel> getAllRoute() {
        List<RouteModel> routeModels = new ArrayList<RouteModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAV_PLACE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RouteModel contact = new RouteModel();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setAddress(cursor.getString(2));
                contact.setPlaceLat(Double.valueOf(cursor.getString(3)));
                contact.setPlaceLon(Double.valueOf(cursor.getString(4)));
                contact.setValue(cursor.getString(5));
                // Adding place to list
                routeModels.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return place list
        return routeModels;
    }

    // Deleting single place
    public void deleteRoute(RouteModel routeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAV_PLACE, KEY_ID + " = ?",
                new String[]{String.valueOf(routeModel.getId())});
        db.close();

    }

}
