package ru.arturvasilov.sqlite.core;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sqlite.database.sqlite.SQLiteDatabase;

/**
 * This class provides implementation for all operations in ContentProvider
 * and based on SQLite database.
 *
 * You only have to implement two methods {@link SQLiteContentProvider#prepareConfig(SQLiteConfig)}
 * and {@link SQLiteContentProvider#prepareSchema(SQLiteSchema)}, the rest is handled by the library.
 *
 * @author Artur Vasilov
 */
public abstract class SQLiteContentProvider extends ContentProvider {

    private SQLiteSchema mSchema;

    private SQLiteHelper mSQLiteHelper;

    private static String sContentAuthority;
    private static Uri sBaseUri;

    /**
     * In this method you can specify configuration for your database (for this moment only name and authority)
     *
     * @param config - configuration for SQLite database
     */
    protected abstract void prepareConfig(@NonNull SQLiteConfig config);

    /**
     * In this method you must add all tables you want to use in your app.
     * To add table call {@link SQLiteSchema#register(Table)}
     *
     * @param schema - schema for SQLite database
     */
    protected abstract void prepareSchema(@NonNull SQLiteSchema schema);

    // инициализация всех нужных для работы полей
    @Override
    public final boolean onCreate() {
        SQLiteConfig config = new SQLiteConfig(getContext());
        prepareConfig(config);

        // базовый URI для дальнейшего обращения к данным в ContentProvider
        sContentAuthority = config.getAuthority();
        sBaseUri = Uri.parse("content://" + sContentAuthority);

        // инициализация схемы данных в ContentProvider (добавление таблицы в базу данных)
        mSchema = new SQLiteSchema();
        prepareSchema(mSchema);

        // инициализация экземпляра доступа к хранилищу данных
        mSQLiteHelper = new SQLiteHelper(getContext(), config, mSchema);
        return true;
    }

    // получение типа данных, который содержится по переданному в качестве параметра URI
    @Nullable
    @Override
    public final String getType(@NonNull Uri uri) {
        return mSchema.findTable(uri);
    }

    // построение SQL-запроса и получение данных (соответствует HTTP методу GET)
    @Nullable
    @Override
    public final Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();
        String table = getType(uri);
        if (TextUtils.isEmpty(table)) {
            throw new IllegalArgumentException("No such table to query");
        } else {
            return database.query(table,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        }
    }

    // добавление элемента в ContentProvider (соответствует HTTP методу POST)
    @NonNull
    @Override
    public final Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();
        String table = getType(uri);
        if (TextUtils.isEmpty(table)) {
            throw new IllegalArgumentException("No such table to insert");
        } else {
            long id = database.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            return ContentUris.withAppendedId(uri, id);
        }
    }

    // переопределение метода вставки массива элементов отдельно по каждому элементу
    // для выполнения вставок одной транзакцией
    @Override
    public final int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();
        String table = getType(uri);
        if (TextUtils.isEmpty(table)) {
            throw new IllegalArgumentException("No such table to insert");
        } else {
            int numInserted = 0;
            database.beginTransaction();
            try {
                for (ContentValues contentValues : values) {
                    long id = database.insertWithOnConflict(table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                    if (id > 0) {
                        numInserted++;
                    }
                }
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }
            return numInserted;
        }
    }

    // удаление элемента из ContentProvider (соответствует HTTP методу DELETE)
    @Override
    public final int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();
        String table = getType(uri);
        if (TextUtils.isEmpty(table)) {
            throw new IllegalArgumentException("No such table to delete");
        } else {
            return database.delete(table, selection, selectionArgs);
        }
    }

    // изменение элемента в ContentProvider (соответствует HTTP методам PUT или PATCH)
    @Override
    public final int update(@NonNull Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();
        String table = getType(uri);
        if (TextUtils.isEmpty(table)) {
            throw new IllegalArgumentException("No such table to update");
        } else {
            return database.update(table, values, selection, selectionArgs);
        }
    }

    @NonNull
    static String getContentAuthority() {
        return sContentAuthority;
    }

    @NonNull
    static Uri getBaseUri() {
        return sBaseUri;
    }
}
