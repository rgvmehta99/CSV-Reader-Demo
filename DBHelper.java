package com.example.demoreadexcelfile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {


	public static final String DATABASE_NAME = "contacts.db";
	public static final int VERSION = 2;
	public static final String TABLE_CONTACT_DETAIL = "contact_detail";
	public static final String CONTACT_ID = "_id";
	public static final String CONTACT_NAME = "contact_name";
	public static final String CONTACT_NUMBER = "contact_number";
	
	static Context context;
	public static String DB_PATH;

	public SQLiteDatabase mDB;
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_CONTACT_DETAIL + " ( " + CONTACT_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + CONTACT_NAME + " text, "
				+ CONTACT_NUMBER + " text )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				db.execSQL("Drop table " + TABLE_CONTACT_DETAIL);
				onCreate(db);
	}
}
