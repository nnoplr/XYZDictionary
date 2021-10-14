package com.novika.xyzdictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class FavoritesDB {

    private DBHelper dbHelper;

    public FavoritesDB(Context ctx) {
        dbHelper = new DBHelper(ctx);
    }

    public void insertProduct(Word word) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.FIELD_NAME, word.wordName);
        cv.put(DBHelper.FIELD_DEFINITION, word.definition);
        cv.put(DBHelper.FIELD_URL, word.url);
        cv.put(DBHelper.FIELD_TYPE, word.type);

        db.insert(DBHelper.TABLE_NAME, null, cv);

        db.close();
    }


    public ArrayList<Word> getAllFavorites() {

        ArrayList<Word> favoritesArray = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectDistinct = "SELECT DISTINCT " + DBHelper.FIELD_NAME +
                " FROM " + DBHelper.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectDistinct, null);

        Word word = null;

        if (cursor.moveToFirst()) {

            do {

                int idx_name = cursor.getColumnIndex(DBHelper.FIELD_NAME);

                word = new Word(cursor.getString(idx_name));
                favoritesArray.add(word);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return favoritesArray;
    }


    public ArrayList<Word> getAFavorite(String Name) {

        ArrayList<Word> favoritesArray = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "name=?";
        String[] selectionArgs = {"" + Name};

        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        Word word = null;

        if (cursor.moveToFirst()) {
            do {
                int idx_id = cursor.getColumnIndex(DBHelper.FIELD_ID);
                int idx_name = cursor.getColumnIndex(DBHelper.FIELD_NAME);
                int idx_def = cursor.getColumnIndex(DBHelper.FIELD_DEFINITION);
                int idx_url = cursor.getColumnIndex(DBHelper.FIELD_URL);
                int idx_type = cursor.getColumnIndex(DBHelper.FIELD_TYPE);

                word = new Word(cursor.getInt(idx_id), cursor.getString(idx_name), cursor.getString(idx_def), cursor.getString(idx_url), cursor.getString(idx_type));
                favoritesArray.add(word);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return favoritesArray;
    }

    public void deleteFavorite(String Nama) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper.FIELD_NAME + " = " + Nama);


    }

    public void delete(String Nama) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME, DBHelper.FIELD_NAME + "=?", new String[]{Nama});
    }

//    nama_tabel, where, whereArgs

}
