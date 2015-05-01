package com.asc.neetk.whatsplaying;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by utk994 on 01/05/15.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TheWorld";
    private static final int DATABASE_VERSION = 1;

    MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       SongsDb.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SongsDb.onUpgrade(db, oldVersion, newVersion);
    }


}