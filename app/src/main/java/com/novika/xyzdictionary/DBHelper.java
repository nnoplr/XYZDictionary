package com.novika.xyzdictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final  String DB_NAME = "DBFavorites";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "FavoriteWords";
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DEFINITION = "definition";
    public static final String FIELD_URL = "url";
    public static final String FIELD_TYPE = "type";

    private static String CREATE_FAVORITE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            FIELD_NAME + " TEXT," +
            FIELD_DEFINITION + " TEXT," +
            FIELD_URL + " TEXT," +
            FIELD_TYPE + " TEXT)";

    private static final String DROP_FAVORITE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_FAVORITE);
        onCreate(db);
    }
}
