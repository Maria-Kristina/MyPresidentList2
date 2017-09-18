package com.example.mypresidentlist;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.TextView;


import static com.example.mypresidentlist.PresidentProvider.CONTENT_URI;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {


    SimpleCursorAdapter myAdapter;

    static final String[] PROJECTION = new String[] { "_id", "fname", "lname", "ayear", "lyear", "detail"};
    static final String SELECTION = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView detailView = (TextView)findViewById(R.id.detailView);
        String[] fromColumns = {"lname", "fname", "ayear", "lyear"};
        int[] toViews = {R.id.lastName, R.id.firstName, R.id.aloitus, R.id.lopetus};

        myAdapter = new SimpleCursorAdapter(
                this, R.layout.list_item_layout,
                null, fromColumns, toViews, 0);
        final ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(myAdapter);


        getLoaderManager().initLoader(0, null, this);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Log.d("ONITEMCLICK", String.valueOf(position));
                Cursor cursor = (Cursor) lv.getItemAtPosition(position);
                String stuff = cursor.getString(cursor.getColumnIndexOrThrow("fname"))
                        + ", " + cursor.getString(cursor.getColumnIndexOrThrow("lname"))
                        + " " + cursor.getString(cursor.getColumnIndexOrThrow("ayear"))
                        + " - " + cursor.getString(cursor.getColumnIndexOrThrow("lyear"))
                        + "\n" + cursor.getString(cursor.getColumnIndexOrThrow("detail"));

                Log.d("ONITEMCLICK", stuff);
                detailView.setText(stuff);


            }
        });
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                CONTENT_URI,
                PROJECTION, SELECTION, null, null);


    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        myAdapter.swapCursor(c);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myAdapter.swapCursor(null);
    }
}
