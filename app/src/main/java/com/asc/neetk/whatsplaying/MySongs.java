package com.asc.neetk.whatsplaying;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;



public class MySongs extends ActionBarActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

            private SimpleCursorAdapter dataAdapter;
 @Override public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.yoursongs_list_fragment);

       displayListView();


        }

            @Override
    protected void onResume() {
        super.onResume();
        //Starts a new or restarts an existing Loader in this manager
        getLoaderManager().restartLoader(0, null, this);
        }

            private void displayListView() {


        // The desired columns to be bound
        String[] columns = new String[]{
                SongsDb.KEY_SONGNAME,
                SongsDb.KEY_ARTIST,
                SongsDb.KEY_ALBUM
        };

       // the XML defined views which the data will be bound to
        int[] to = new int[] {
       R.id.songname,
        R.id.artistname,
         R.id.albumname,
        };

       // create an adapter from the SimpleCursorAdapter
       dataAdapter = new SimpleCursorAdapter(
               this,
        R.layout.list_view_item_layout,
       null,
        columns,
        to,
        0);

       // get reference to the ListView
       ListView listView = (ListView) findViewById(R.id.list);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        //Ensures a loader is initialized and active.
        getLoaderManager().initLoader(0, null, this);


       }

            // This is called when a new Loader needs to be created.
 @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
       String[] projection = {
        SongsDb.KEY_ROWID,
       SongsDb.KEY_ALBUM,
        SongsDb.KEY_SONGNAME,
       SongsDb.KEY_ARTIST};
        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider.CONTENT_URI, projection, null, null, null);
       return cursorLoader;
        }

            @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        dataAdapter.swapCursor(data);
        }

            @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        dataAdapter.swapCursor(null);
        }

            @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.activity_main, menu);
       return true;
        }
}