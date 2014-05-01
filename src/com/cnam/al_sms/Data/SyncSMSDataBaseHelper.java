package com.cnam.al_sms.Data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SyncSMSDataBaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_SYNCHRONISATION = "synchronisation";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DATE = "date";

	/**
	 * <h1>Type de la synchronisation</h1> 
	 * <ul>
	 * 	<li>0: Synchronisation sur période</li> 
	 * 	<li>1: Synchronisation à la volée</li>
	 * </ul>
	 */
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_FISRTSMS = "first_sms";
	public static final String COLUMN_LASTSMS = "last_sms";

	private static final String DATABASE_NAME = "alsms.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_SYNCHRONISATION + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + " INTEGER," + COLUMN_DATE
			+ " INTEGER," + COLUMN_TYPE + " INTEGER," + COLUMN_FISRTSMS
			+ " INTEGER," + COLUMN_LASTSMS + " INTEGER" + ");";

	public SyncSMSDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public SyncSMSDataBaseHelper(Context context, String name,
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNCHRONISATION);
		onCreate(db);
	}

}
