package ru.oepak22.simpleweatherpatterna.sqlite;

import android.net.Uri;
import android.provider.BaseColumns;

public class CityContract implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://"
            + CityContentProvider.AUTHORITY + "/wallet");

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jwei512.notes";

    //public static final String WALLET_ID = "_id";

    //public static final String TITLE = "title";

    //public static final String TOTAL = "total";

    public static final String CITY_ID = "_id";

    public static final String NAME = "name";

    public static final String TEMP = "temp";

    public static final String PRESSURE = "pressure";

    public static final String HUMIDITY = "humidity";

    public static final String MAIN = "main";

    public static final String SPEED = "speed";
}
