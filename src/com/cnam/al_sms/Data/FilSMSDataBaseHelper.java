package com.cnam.al_sms.Data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class FilSMSDataBaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_FIL = "fil";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SNIPPET = "snippet";
	public static final String COLUMN_MESSAGECOUNT = "message_count";

	private static final String DATABASE_NAME = "alsms.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table " + TABLE_FIL
			+ " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ " INTEGER," + COLUMN_SNIPPET + " TEXT," + COLUMN_MESSAGECOUNT
			+ " INTEGER" + ");";

	public FilSMSDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public FilSMSDataBaseHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIL);
		onCreate(db);
	}

}
