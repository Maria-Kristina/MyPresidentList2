package com.example.mypresidentlist;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by M-K on 25.8.2017.
 */

public class PresidentProvider extends ContentProvider{

    static final String PROVIDER_NAME = "com.example.provider.PresidentProvider";
    static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/presidents");
    private static HashMap<String, String> PRESIDENTS_PROJECTION_MAP;

    static final int PRESIDENTS = 1;
    static final int PRESIDENT_ID = 2;
    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "presidents", PRESIDENTS);
        uriMatcher.addURI(PROVIDER_NAME, "presidents/#", PRESIDENT_ID);
    }

    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "Presidents";
    private static final String PRESIDENTS_TABLE_NAME = "presidents";
    private static final int DATABASE_VERSION = 3;

    static final String _ID = "_id";
    static final String FNAME = "fname";
    static final String LNAME = "lname";
    static final String AYEAR = "ayear";
    static final String LYEAR = "lyear";
    static final String DETAIL = "detail";





    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase(); //database luodaan?




        if (db == null){
            Log.d("ONCREATE", "DATABASE CREATION FAILED");
            return false;
        } else {
            return true;
        }

    }


    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(PRESIDENTS_TABLE_NAME);


        switch (uriMatcher.match(uri)){
            case PRESIDENTS:
                qb.setProjectionMap(PRESIDENTS_PROJECTION_MAP);
                break;
            case PRESIDENT_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = AYEAR;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;

    }




    @Override
    public String getType(Uri uri) {

        //MITÄ HITTOA TÄMÄ TEKEE???
        switch (uriMatcher.match(uri)){
            case PRESIDENTS:
                return "vnd.android.cursor.dir/vnd.example.presidents";
            case PRESIDENT_ID:
                return "vnd.android.cursor.item/vnd.example.presidents";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(PRESIDENTS_TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }






    static class DatabaseHelper extends SQLiteOpenHelper {
        private List<President> presidentList;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String PRESIDENTS_TABLE_CREATE =
                    "CREATE TABLE " + PRESIDENTS_TABLE_NAME +
                            " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            " " + FNAME + " TEXT," +
                            " " + LNAME + " TEXT," +
                            " " + AYEAR + " TEXT," +
                            " " + LYEAR + " TEXT," +
                            " " + DETAIL + " TEXT);";
            db.execSQL(PRESIDENTS_TABLE_CREATE);

            presidentList = new ArrayList<>();
            makePresidents();


            ContentValues values = new ContentValues();
            //Looppi joka lukee pressojen tiedot ja lisää ne db
            for (President president : presidentList ) {
                values.put(PresidentProvider.FNAME, president.firstName);
                values.put(PresidentProvider.LNAME, president.lastName);
                values.put(PresidentProvider.AYEAR, president.aloitusVuosi);
                values.put(PresidentProvider.LYEAR, president.lopetusVuosi);
                values.put(PresidentProvider.DETAIL, president.detail);

                db.insert(PRESIDENTS_TABLE_NAME, null, values);
                values.clear();
            }



        }

        public void makePresidents (){

            presidentList.add(new President("K. J.","Ståhlberg",1919,1925,"Esihistoriaa"));
            presidentList.add(new President("Lauri Kristian","Relander",1925,1931,"Kuka tietää"));
            presidentList.add(new President("P. E.","Svinhufvud",1931,1937,"Varmasti jokaiselle tuttu"));
            presidentList.add(new President("Kyösti","Kallio",1937,1940,"Kova jätkä"));
            presidentList.add(new President("Risto","Ryti",1940,1944,"Vielä kovempi jätkä"));
            presidentList.add(new President("Gustaf","Mannerheim",1944,1946,"Patsas, junou"));
            presidentList.add(new President("J. K.","Paasikivi",1946,1956,"Kivinen tyyppi"));
            presidentList.add(new President("Urho","Kekkonen",1956,1982,"isoisosetä!"));
            presidentList.add(new President("Mauno","Koivisto",1982,1994,"Vai Männikkö, heh heh"));
            presidentList.add(new President("Martti","Ahtisaari",1994,2000,"Nobel voittaja"));
            presidentList.add(new President("Tarja","Halonen",2000,2012,"Setan entinen puheenjohtaja!"));
            presidentList.add(new President("Sauli","Niinistö",2012,2018,"Nykyinen"));


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + PRESIDENTS_TABLE_NAME);
            onCreate(db);
        }
    }


}


