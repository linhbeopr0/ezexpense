package com.sanmboiboi.storage;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PersonDB extends SQLiteOpenHelper {
    public static String TAG = "LINH";

    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "person.db";
    private static String TABLE_NAME = "person";
    private static String COLLUMN_ID = "pId";
    private static String COLLUMN_NAME = "pName";
    private static String COLLUMN_BALANCE = "pBalance";
    private static String COLLUMN_PHOTO = "pPhoto";

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLLUMN_NAME + " TEXT," + COLLUMN_BALANCE + " DOUBLE,"
            + COLLUMN_PHOTO + " TEXT)";

    private static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public PersonDB(Context context, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "init PersonDB");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.d(TAG, "onCreateDb()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL(DROP_TABLE);
        Log.d(TAG, "onUpgradeDb");
        onCreate(db);
    }

    public void insert(PersonInfo p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLLUMN_NAME, p.name);
        values.put(COLLUMN_BALANCE, p.balance);
        values.put(COLLUMN_PHOTO, p.path);

        db.insert(TABLE_NAME, null, values);
        db.close();
        Log.d(TAG, "insert " + p.name);
    }
    
    public void delete(PersonInfo p) {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_NAME + " where " + COLLUMN_ID + "='" + p.id + "'";
        db.execSQL(deleteQuery);
        db.close();
        Log.d(TAG, "delete " + p.name + " - " + p.id);
    }

    public int update(PersonInfo p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLLUMN_NAME, p.name);
        values.put(COLLUMN_BALANCE, p.balance);
        values.put(COLLUMN_PHOTO, p.path);
        Log.d(TAG, "update " + p.name + " - " + p.id);
        return db.update(TABLE_NAME, values, COLLUMN_ID + " = ?", new String[] {String.valueOf(p.id)});
    }

    public PersonInfo get(int id) {
        PersonInfo p = new PersonInfo();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " where " + COLLUMN_ID + "='" + id + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                p.setName(c.getString(1));
                p.setBalance(c.getDouble(2));
                p.setPath(c.getString(3));
            } while (c.moveToNext());
        }
        Log.d(TAG, "get " + p.name);
        return p;
    }

    public ArrayList<PersonInfo> getall() {
        ArrayList<PersonInfo> allData = new ArrayList<PersonInfo>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                PersonInfo p = new PersonInfo();
                p.setName(c.getString(1));
                p.setBalance(c.getDouble(2));
                p.setPath(c.getString(3));
                p.id = c.getInt(0);
                Log.d("LINH", "ID = " + p.id);
                allData.add(p);
            } while (c.moveToNext());
        }
        Log.d(TAG, "getAll " + allData.size());
        return allData;
    }

}
