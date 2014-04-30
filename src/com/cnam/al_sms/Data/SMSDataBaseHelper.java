package com.cnam.al_sms.Data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SMSDataBaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_SMS = "sms";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_THREADID = "thread_id";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_PERSON = "person";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_DATESENT = "date_sent";
	public static final String COLUMN_READ = "read";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_SUBJECT = "subject";
	public static final String COLUMN_BODY = "body";
	public static final String COLUMN_SEEN = "seen";

	private static final String DATABASE_NAME = "sms.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table " + TABLE_SMS
			+ " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ COLUMN_THREADID + " INTEGER," + COLUMN_ADDRESS + " TEXT,"
			+ COLUMN_PERSON + " INTEGER," + COLUMN_DATE + " INTEGER,"
			+ COLUMN_DATESENT + " INTEGER DEFAULT 0," + COLUMN_READ
			+ " INTEGER DEFAULT 0," + COLUMN_STATUS + " INTEGER DEFAULT -1,"
			+ COLUMN_TYPE + " INTEGER," + COLUMN_SUBJECT + " TEXT,"
			+ COLUMN_BODY + " TEXT," + COLUMN_SEEN + " INTEGER DEFAULT 0"
			+ ");";

	public SMSDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	public SMSDataBaseHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SMSDataBaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);
		onCreate(db);
	}

}
