package com.example.halifaxtransit;

//database methods

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.halifaxtransit.Constants.TABLE_STOP_TIMES;

public class SqliteDbHelper extends SQLiteOpenHelper {

    SqliteDbHelper(Context context)
    {
        super(context,TABLE_STOP_TIMES,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
