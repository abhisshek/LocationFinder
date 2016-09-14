package com.infi.abhishekjha.locationfinder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Abhishek.jha on 9/12/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    private static final String TAG = "DatabaseHandler";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "location_data.db";
    public SQLiteDatabase db1;
    private Context mContext;
    private static DatabaseHandler mInstance;
    //Drop Table Query
    private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS ";
    // Main Category table name
    private static final String TABLE_LOCATION = "main_category";
    // Main Category Table Columns names
    private static final String _ID = "_id";
    private static final String LOCATION_LAT = "location_lat";
    private static final String LOCATION_LON = "location_lon";
    private static final String LOCATION_TIME = "location_time";
    private static final String LOCATION_ADDRESS = "location_address";

    //Main Category Table Statement
    private static final String CREATE_LOCATION_TABLE = "create table " + TABLE_LOCATION + "" +
            "(" + _ID + " integer primary key autoincrement , " + LOCATION_LAT + " integer , " +
            LOCATION_LON + " text, "+ LOCATION_ADDRESS + " text, " + LOCATION_TIME + " text )";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public static DatabaseHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHandler(context);
        }
        return mInstance;
    }

    public DatabaseHandler open() throws SQLException {
        db1 = getWritableDatabase();
        return this;
    }

    public void close() {
        db1.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion){
            db.execSQL(DROP_TABLE_QUERY + TABLE_LOCATION);
        }
    }

    public long mWriteCategory(String location_lat, String location_lon, String location_addr, String location_time) {
        long rows = 0;
        ContentValues values = new ContentValues();
        values.put(LOCATION_LAT, location_lat);
        values.put(LOCATION_LON, location_lon);
        values.put(LOCATION_TIME, location_time);
        values.put(LOCATION_ADDRESS, location_addr);
        //rows = db1.insertWithOnConflict(TABLE_MAIN_CATEGORY, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        //db1.insertWithOnConflict (String table, String nullColumnHack, ContentValues initialValues, int conflictAlgorithm)
        rows = db1.insertWithOnConflict(TABLE_LOCATION, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("rows: ", rows+"");
        return rows;
    }

    public Cursor getAllLocationData() {
        String searchQuery = "select * from " + TABLE_LOCATION +
                " ORDER BY " + _ID + " DESC";//ASC
        Cursor cursor = db1.rawQuery(searchQuery, null);
        return cursor;
    }

}
