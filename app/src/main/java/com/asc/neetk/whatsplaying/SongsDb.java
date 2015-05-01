package com.asc.neetk.whatsplaying;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by utk994 on 01/05/15.
 */
public class SongsDb {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_SONGNAME = "songname";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_ALBUM ="album";

    private static final String LOG_TAG = "SongsDb";
    public static final String SQLITE_TABLE = "Song";

    private static final String DATABASE_CREATE =
            " CREATE TABLE " + SQLITE_TABLE +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " album TEXT NOT NULL, " +
                    " artist TEXT NOT NULL, " +
                    " songname TEXT NOT NULL);";

    public static void onCreate(SQLiteDatabase db) {
        Log.w(LOG_TAG,DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }

}