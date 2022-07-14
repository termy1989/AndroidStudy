package ru.oepak22.simpleweatherpatterna.sqlite;

import ru.arturvasilov.sqlite.core.SQLiteConfig;
import ru.arturvasilov.sqlite.core.SQLiteContentProvider;
import ru.arturvasilov.sqlite.core.SQLiteSchema;
import ru.oepak22.simpleweatherpatterna.tables.AllCitiesTable;
import ru.oepak22.simpleweatherpatterna.tables.RequestTable;

// класс ContentProvider для приложения
public class WeatherContentProvider extends SQLiteContentProvider {

    private static final String DATABASE_NAME = "simpleweather.db";
    private static final String CONTENT_AUTHORITY = "ru.oepak22.simpleweather";

    // основная конфигурация
    @Override
    protected void prepareConfig(SQLiteConfig config) {
        config.setDatabaseName(DATABASE_NAME);
        config.setAuthority(CONTENT_AUTHORITY);
    }

    // установка схемы
    @Override
    protected void prepareSchema(SQLiteSchema schema) {
        schema.register(RequestTable.TABLE);
        schema.register(AllCitiesTable.TABLE);
    }
}