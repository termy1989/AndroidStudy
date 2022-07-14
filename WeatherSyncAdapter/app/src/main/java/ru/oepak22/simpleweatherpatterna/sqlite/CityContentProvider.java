package ru.oepak22.simpleweatherpatterna.sqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;


public class CityContentProvider extends ContentProvider {

    private static final String DATABASE_NAME = "wallet.db";

    private static final int DATABASE_VERSION = 1;

    private static final String WALLET_TABLE_NAME = "wallet";

    public static final String AUTHORITY = "ru.oepak22.simpleweatherpatterna";

    public static final String ACOOUNT_TYPE = "ru.oepak22.simpleweatherpatterna";

    private static final UriMatcher sUriMatcher;

    private static final int WALLET = 1;

    private static final int WALLET_ID = 2;

    private static HashMap<String, String> notesProjectionMap;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, WALLET_TABLE_NAME, WALLET);
        sUriMatcher.addURI(AUTHORITY, WALLET_TABLE_NAME + "/#", WALLET_ID);

        notesProjectionMap = new HashMap<>();
        notesProjectionMap.put(CityContract.CITY_ID, CityContract.CITY_ID);
        notesProjectionMap.put(CityContract.NAME, CityContract.NAME);
        notesProjectionMap.put(CityContract.TEMP, CityContract.TEMP);
        notesProjectionMap.put(CityContract.PRESSURE, CityContract.PRESSURE);
        notesProjectionMap.put(CityContract.HUMIDITY, CityContract.HUMIDITY);
        notesProjectionMap.put(CityContract.MAIN, CityContract.MAIN);
        notesProjectionMap.put(CityContract.SPEED, CityContract.SPEED);
    }

    private DatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(WALLET_TABLE_NAME);
        qb.setProjectionMap(notesProjectionMap);

        switch (sUriMatcher.match(uri)) {
            case WALLET:
                break;
            case WALLET_ID:
                selection = selection + CityContract.CITY_ID + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case WALLET:
                return CityContract.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues initialValues) {
        if (sUriMatcher.match(uri) != WALLET) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(WALLET_TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(CityContract.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(uri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case WALLET:
                break;
            case WALLET_ID:
                where = where + CityContract.CITY_ID +uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        int count = db.delete(WALLET_TABLE_NAME, where, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case WALLET:
                count = db.update(WALLET_TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (sUriMatcher.match(uri) != WALLET) {
            return super.bulkInsert(uri, values);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        int returnCount = 0;
        try {
            for(ContentValues value : values){
                long rowId = db.insert(WALLET_TABLE_NAME, null, value);
                if(rowId != -1){
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + WALLET_TABLE_NAME + " (" + CityContract.CITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + CityContract.NAME + " VARCHAR(255),"
                    + CityContract.TEMP + " VARCHAR(255),"
                    + CityContract.PRESSURE + " VARCHAR(255),"
                    + CityContract.HUMIDITY + " VARCHAR(255),"
                    + CityContract.MAIN + " VARCHAR(255),"
                    + CityContract.SPEED + " VARCHAR(255)"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
