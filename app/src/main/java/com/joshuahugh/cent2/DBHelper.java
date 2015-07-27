package com.joshuahugh.cent2;

/**
 * Created by joshuahugh on 06/03/15.
 */
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Centurion";

    //User Table
    public static final String toiletTable = "Toilet";
    public static final String userID = "UserID";
    public static final String userName = "Name";
    public static final String shotsMissed = "ShotsMissed";


    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create user table
        String sql = "CREATE TABLE " + toiletTable + " (" + userID
                + " INTEGER PRIMARY KEY  AUTOINCREMENT, "
                + userName + " TEXT, "
                + shotsMissed + " Integer)";
        Log.i("DBHELPER", sql);
        db.execSQL(sql);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + toiletTable);
        onCreate(db);

    }

    public ArrayList<ToiletItem> getToilets(SQLiteDatabase database) {
        ArrayList<ToiletItem> toilets = new ArrayList<ToiletItem>();


        ArrayList<HashMap<String, String>> ingredientArrayList = new ArrayList<HashMap<String, String>>();
        String[] projection = {
                userID,
                userName,
                shotsMissed,
        };

        String whereClause = "";

        //String[] whereArgs = new String[] {
        //        username,
        //};

        String sortOrder ="";


        Cursor c = database.query(
                toiletTable,  // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if(c.moveToFirst()){
            do{
               ToiletItem t = new ToiletItem(c.getString(1), c.getInt(2), c.getInt(0));

                       toilets.add(t);
            }
            while(c.moveToNext());
        }
        return toilets;
    }

    public void addName(SQLiteDatabase db, String name) {
        Log.i("name", name);
        ContentValues values = new ContentValues();
        //values.put(userID, id);
        values.put(userName, name);
        values.put(shotsMissed, 0);

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                toiletTable,
                null,
                values);
        //db.close();
    }

    public void increment(SQLiteDatabase db) {
        ArrayList<ToiletItem> toiletItems = getToilets(db);
        for(ToiletItem t : toiletItems) {

            String strFilter = userID + "=" + t.getId();
            ContentValues args = new ContentValues();
            args.put(shotsMissed, t.getShots()+1);
            db.update(toiletTable, args, strFilter, null);


        }
    }

    public void remove(SQLiteDatabase db, int id) {
        String whereClause = userID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        db.delete(toiletTable, whereClause, whereArgs);
    }




}
