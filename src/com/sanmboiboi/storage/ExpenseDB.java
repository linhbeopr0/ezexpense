package com.sanmboiboi.storage;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// on going db
public class ExpenseDB extends SQLiteOpenHelper {
	public static String TAG = "MCE";

	private static int DATABASE_VERSION = 1;
	private static String DATABASE_NAME = "expense.db";
	private static String TABLE_NAME = "expense";
	private static String COLLUMN_ID = "eId";
	private static String COLLUMN_NAME = "eName";
	private static String COLLUMN_BALANCE = "eBALANCE";
	private static String COLLUMN_PHOTO = "ePhoto";
	private static String COLLUMN_PEOPLE = "ePeople";
	private static String COLLUMN_DATE = "eDate";
	private static String COLLUMN_NOTE = "eNote";

	private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
			+ COLLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLLUMN_NAME
			+ " TEXT," + COLLUMN_BALANCE + " DOUBLE," + COLLUMN_PEOPLE
			+ " TEXT," + COLLUMN_DATE + " TEXT," + COLLUMN_PHOTO + " TEXT," + COLLUMN_NOTE + " TEXT)";

	private static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

	public ExpenseDB(Context context, int version) {
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

	public void insert(ExpenseInfo e) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(COLLUMN_NAME, e.category);
		values.put(COLLUMN_BALANCE, e.balance);
		values.put(COLLUMN_PHOTO, e.path);
		values.put(COLLUMN_PEOPLE, e.strPeopleParticipate);
		values.put(COLLUMN_DATE, e.date);
		values.put(COLLUMN_NOTE, e.note);

		db.insert(TABLE_NAME, null, values);
		db.close();
		Log.d(TAG, "insert " + e.category);
	}

	public void delete(ExpenseInfo e) {
		SQLiteDatabase db = this.getWritableDatabase();
		String deleteQuery = "DELETE FROM " + TABLE_NAME + " where "
				+ COLLUMN_ID + "='" + e.id + "'";
		db.execSQL(deleteQuery);
		db.close();
		Log.d(TAG, "delete " + e.category);
	}

	public int update(ExpenseInfo e) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLLUMN_NAME, e.category);
		values.put(COLLUMN_BALANCE, e.balance);
		values.put(COLLUMN_PHOTO, e.path);
		values.put(COLLUMN_PEOPLE, e.strPeopleParticipate);
		values.put(COLLUMN_DATE, e.date);
		values.put(COLLUMN_NOTE, e.note);
		String[] whereAgrs = { e.id + "" };
		Log.d(TAG, "update " + e.category + " - " + e.id);
		return db.update(TABLE_NAME, values, COLLUMN_ID + "= ?", whereAgrs);
	}

	public ExpenseInfo get(int id) {
		ExpenseInfo e = new ExpenseInfo();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " where "
				+ COLLUMN_ID + "='" + id + "'";
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				e.setCategory(c.getString(1));
				e.setBalance(c.getDouble(2));
				e.setStrPeopleParticipate(c.getString(3));
				e.setDate(c.getString(4));
				e.setPath(c.getString(5));
				e.setNote(c.getString(6));
			} while (c.moveToNext());
		}
		Log.d(TAG, "get " + e.category);
		return e;
	}

	public ArrayList<ExpenseInfo> getall() {
		ArrayList<ExpenseInfo> allData = new ArrayList<ExpenseInfo>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				ExpenseInfo e = new ExpenseInfo();
				e.setCategory(c.getString(1));
				e.setBalance(c.getDouble(2));
				e.setStrPeopleParticipate(c.getString(3));
				e.setDate(c.getString(4));
				e.setPath(c.getString(5));
				e.setNote(c.getString(6));
				e.id = c.getInt(0);
				allData.add(e);
			} while (c.moveToNext());
		}
		Log.d(TAG, "getAll " + allData.size());
		return allData;
	}

}
