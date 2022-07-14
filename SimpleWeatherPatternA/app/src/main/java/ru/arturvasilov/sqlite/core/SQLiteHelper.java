package ru.arturvasilov.sqlite.core;

import android.content.Context;

import androidx.annotation.NonNull;

import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteOpenHelper;

/**
 * @author Artur Vasilov
 */
class SQLiteHelper extends SQLiteOpenHelper {

    private final SQLiteSchema mSchema;

    public SQLiteHelper(Context context, @NonNull SQLiteConfig config, @NonNull SQLiteSchema schema) {
        super(context, context.getDatabasePath(config.getDatabaseName()).getPath(), null, schema.calculateVersion());
        mSchema = schema;
        context.openOrCreateDatabase(config.getDatabaseName(), 0, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        for (Table table : mSchema) {
            table.onCreate(database);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        for (Table table : mSchema) {
            if (oldVersion < newVersion && newVersion <= table.getLastUpgradeVersion()) {
                table.onUpgrade(database);
            }
        }
    }
}
